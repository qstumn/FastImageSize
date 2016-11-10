package q.rorbin.fastimagesize;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import q.rorbin.fastimagesize.imp.BmpImageSize;
import q.rorbin.fastimagesize.imp.DefaultImageSize;
import q.rorbin.fastimagesize.imp.GifImageSize;
import q.rorbin.fastimagesize.imp.JpgImageSize;
import q.rorbin.fastimagesize.imp.PngImageSize;
import q.rorbin.fastimagesize.net.InputStreamProvider;


/**
 * Created by chqiu on 2016/10/26.
 */

public class FastImageSize {
    private InputStreamProvider mProvider;
    private FastImageLoader mLoader;
    private List<ImageSize> mImageSizes;
    private static final ExecutorService mExecutor = Executors.newCachedThreadPool();
    private static final Handler mHandler = new Handler(Looper.getMainLooper());

    protected FastImageSize(InputStreamProvider provider, FastImageLoader loader) {
        mProvider = provider;
        mLoader = loader;
        initImageSize();
    }

    private void initImageSize() {
        mImageSizes = new ArrayList<>();
        Collections.addAll(mImageSizes, new PngImageSize(), new GifImageSize(), new BmpImageSize(), new JpgImageSize());
    }

    public static FastImageLoader with(String imagePath) {
        return new FastImageLoader(imagePath);
    }

    public int[] get() {
        int[] size = new int[2];
        InputStream stream = mProvider.getInputStream(mLoader.mImagePath);
        if (stream == null)
            return size;
        byte[] buffer = new byte[8];
        try {
            stream.read(buffer, 0, buffer.length);
            ImageSize imageSize = getImageSize(buffer);
            size = imageSize.getImageSize(stream, buffer);
            closeStream(stream, false);
            return size;
        } catch (IOException e) {
            e.printStackTrace();
            closeStream(stream, true);
            return size;
        }
    }

    public void get(final ImageSizeCallback callback) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final int[] size = get();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSizeReady(size);
                    }
                });
            }
        });
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

    private void closeStream(final InputStream stream, final boolean isClose) {
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

    public interface ImageSizeCallback {
        void onSizeReady(int[] size);
    }

}
