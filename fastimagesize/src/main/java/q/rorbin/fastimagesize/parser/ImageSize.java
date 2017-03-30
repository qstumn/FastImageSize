package q.rorbin.fastimagesize.parser;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author chqiu
 *         Email:qstumn@163.com
 */

public abstract class ImageSize {

    public abstract int getSupportImageType();

    public abstract boolean isSupportImageType(byte[] buffer);

    /**
     * @return width : int[0] , height : int[1]
     */
    public abstract int[] getImageSize(InputStream stream, byte[] buffer) throws IOException;
}
