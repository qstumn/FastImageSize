package q.rorbin.fastimagesize.imp;

import java.io.IOException;
import java.io.InputStream;

import q.rorbin.fastimagesize.ImageSize;
import q.rorbin.fastimagesize.ImageType;

/**
 * Created by chqiu on 2016/10/26.
 */

public class DefaultImageSize extends ImageSize {
    @Override
    public ImageType getSupportImageType() {
        return ImageType.NULL;
    }

    @Override
    public boolean isSupportImageType(byte[] buffer) {
        return false;
    }

    @Override
    public int[] getImageSize(InputStream stream, byte[] buffer) throws IOException {
        return new int[2];
    }
}
