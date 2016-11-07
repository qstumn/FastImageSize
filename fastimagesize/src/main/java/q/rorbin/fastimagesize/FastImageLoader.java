package q.rorbin.fastimagesize;

import android.media.Image;

import q.rorbin.fastimagesize.net.DefaultInputStreamProvider;
import q.rorbin.fastimagesize.net.InputStreamProvider;

/**
 * Created by chqiu on 2016/10/26.
 */

public class FastImageLoader {
    protected String mImagePath;

    protected FastImageLoader(String imagePath) {
        mImagePath = imagePath;
    }

    public FastImageSize customProvider(InputStreamProvider provider) {
        return new FastImageSize(provider, this);
    }

    public int[] get() {
        return new FastImageSize(new DefaultInputStreamProvider(), this).get();
    }

    public void get(FastImageSize.ImageSizeCallback callback) {
        new FastImageSize(new DefaultInputStreamProvider(), this).get(callback);
    }
}
