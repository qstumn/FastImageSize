package q.rorbin.fastimagesize;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import q.rorbin.fastimagesize.cache.DiskLruCache;
import q.rorbin.fastimagesize.cache.MemoryCache;
import q.rorbin.fastimagesize.parser.BmpImageSize;
import q.rorbin.fastimagesize.parser.DefaultImageSize;
import q.rorbin.fastimagesize.parser.GifImageSize;
import q.rorbin.fastimagesize.parser.ImageSize;
import q.rorbin.fastimagesize.parser.ImageType;
import q.rorbin.fastimagesize.parser.JpgImageSize;
import q.rorbin.fastimagesize.parser.PngImageSize;
import q.rorbin.fastimagesize.request.ImageSizeCallback;
import q.rorbin.fastimagesize.request.OverrideCallback;
import q.rorbin.fastimagesize.util.GlobalUtil;


/**
 * @author chqiu
 *         Email:qstumn@163.com
 */

public class FastImageSize {
    private List<ImageSize> mImageSizes;
    private ExecutorService mExecutor;
    private Handler mHandler;
    private MemoryCache mMemoryCache;
    private DiskLruCache mDiskLruCache;

    private static final FastImageSize INSTANCE = new FastImageSize();

    private FastImageSize() {
        mImageSizes = new ArrayList<>();
        mExecutor = Executors.newCachedThreadPool();
        mHandler = new Handler(Looper.getMainLooper());
        mMemoryCache = new MemoryCache(500);
        File diskFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "FastImageSize");
        if (!diskFile.exists()) {
            diskFile.mkdirs();
        }
        try {
            mDiskLruCache = DiskLruCache.open(diskFile, 1, 1, 5 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.addAll(mImageSizes, new PngImageSize(), new GifImageSize(), new BmpImageSize(),
                new JpgImageSize());
    }

    public static FastImageLoader with(String imagePath) {
        if (TextUtils.isEmpty(imagePath)) {
            throw new IllegalStateException("imagePath must be not null");
        }
        return FastImageLoader.newInstance(imagePath, INSTANCE);
    }

    int[] get(FastImageLoader loader) {
        String cacheKey = GlobalUtil.hashKeyForLru(loader.mImagePath);
        int[] size = getByCache(cacheKey, loader.mUseCache);
        if (size == null) {
            InputStream stream = loader.mProvider.getInputStream(loader.mImagePath);
            if (stream == null) {
                return new int[3];
            }
            byte[] buffer = new byte[8];
            try {
                stream.read(buffer, 0, buffer.length);
                ImageSize imageSize = getImageSize(buffer);
                size = imageSize.getImageSize(stream, buffer);
                if (size[2] != ImageType.NULL) {
                    putToCache(cacheKey, size, loader.mUseCache);
                }
                closeStream(stream, false);
            } catch (IOException e) {
                e.printStackTrace();
                closeStream(stream, true);
            }
        }
        if (loader.mOverrideSize > 0 && size != null && size[2] != ImageType.NULL) {
            GlobalUtil.geometricScaling(size[0], size[1], loader.mOverrideSize, size);
        }
        return size;
    }

    void get(final ImageSizeCallback callback, final FastImageLoader loader) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final int[] size = get(loader);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSizeReady(size);
                    }
                });
            }
        });
    }

    void into(final View view, FastImageLoader loader) {
        get(new OverrideCallback(view, loader.mOverrideSize), loader);
    }

    private int[] getByCache(String key, boolean useCache) {
        if (!useCache) {
            return null;
        }
        int[] size = mMemoryCache.get(key);
        if (size == null) {
            size = getByDiskCache(key);
            if (size != null) {
                mMemoryCache.put(key, size);
            }
        }
        return size;
    }

    private int[] getByDiskCache(String key) {
        if (mDiskLruCache == null) {
            return null;
        }
        InputStream is = null;
        int[] size = null;
        try {
            DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
            if (snapShot == null) {
                return null;
            }
            is = snapShot.getInputStream(0);
            size = new int[3];
            byte[] buffer = new byte[4];
            for (int i = 0; i < 3; i++) {
                int read = is.read(buffer);
                if (read == -1 && i != 2) {
                    return null;
                }
                size[i] = ByteBuffer.wrap(buffer).getInt();
            }
            closeStream(is, false);
        } catch (IOException e) {
            e.printStackTrace();
            closeStream(is, true);
        }
        return size;
    }

    private void putToCache(String key, int[] size, boolean useCache) {
        if (!useCache) {
            return;
        }
        mMemoryCache.put(key, size);
        putToDiskCache(key, size);
    }

    private void putToDiskCache(String key, int[] size) {
        if (mDiskLruCache == null) {
            return;
        }
        OutputStream os = null;
        try {
            DiskLruCache.Editor editor = mDiskLruCache.edit(key);
            os = editor.newOutputStream(0);
            for (int i : size) {
                for (int j = 24; j >= 0; j -= 8) {
                    os.write((byte) ((i >> j) & 0xFF));
                }
            }
            editor.commit();
            mDiskLruCache.flush();
            closeStream(os, false);
        } catch (IOException e) {
            e.printStackTrace();
            closeStream(os, true);
        }
    }

    private ImageSize getImageSize(byte[] buffer) {
        for (int i = 0; i < mImageSizes.size(); i++) {
            ImageSize imageSize = mImageSizes.get(i);
            if (imageSize.isSupportImageType(buffer)) {
                return imageSize;
            }
        }
        return new DefaultImageSize();
    }

    private void closeStream(final Closeable stream, final boolean isClose) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (stream != null)
                        stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    if (!isClose) {
                        closeStream(stream, true);
                    }
                }
            }
        });
    }
}
