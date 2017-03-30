package q.rorbin.fastimagesize;

import android.view.View;

import q.rorbin.fastimagesize.net.DefaultInputStreamProvider;
import q.rorbin.fastimagesize.net.InputStreamProvider;
import q.rorbin.fastimagesize.parser.ImageType;
import q.rorbin.fastimagesize.request.ImageSizeCallback;

/**
 * @author chqiu
 *         Email:qstumn@163.com
 */

public class FastImageLoader {
    InputStreamProvider mProvider;
    String mImagePath;
    int mOverrideSize;
    private FastImageSize mFastImageSize;

    private FastImageLoader(String imagePath, FastImageSize fastImageSize) {
        mFastImageSize = fastImageSize;
        mImagePath = imagePath;
        mProvider = DefaultInputStreamProvider.getInstance();
        mOverrideSize = -1;
    }

    static FastImageLoader newInstance(String imagePath, FastImageSize fastImageSize) {
        return new FastImageLoader(imagePath, fastImageSize);
    }

    public FastImageLoader customProvider(InputStreamProvider provider) {
        if (provider == null) {
            throw new IllegalStateException("provider must be not null");
        }
        mProvider = provider;
        return this;
    }

    public FastImageLoader override(int overrideSize) {
        if (overrideSize <= 0) {
            throw new IllegalStateException("overrideSize must be bigger than 0");
        }
        mOverrideSize = overrideSize;
        return this;
    }

    /**
     * @return width:int[0]   height:int[1]   type:int[2] {@link ImageType}
     */
    public int[] get() {
        return mFastImageSize.get(this);
    }

    public void get(ImageSizeCallback callback) {
        mFastImageSize.get(callback, this);
    }

    public void into(View view) {
        mFastImageSize.into(view, this);
    }
}
