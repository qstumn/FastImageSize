package q.rorbin.fastimagesize;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chqiu on 2016/10/26.
 */

public abstract class ImageSize {

    public abstract ImageType getSupportImageType();

    public abstract boolean isSupportImageType(byte[] buffer);

    /**
     * @return width : int[0] , height : int[1]
     */
    public abstract int[] getImageSize(InputStream stream, byte[] buffer) throws IOException;
}
