package q.rorbin.fastimagesize.parser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import q.rorbin.fastimagesize.util.ByteArrayUtil;

/**
 * @author chqiu
 *         Email:qstumn@163.com
 */

public class BmpImageSize extends ImageSize {
    @Override
    public int getSupportImageType() {
        return ImageType.BMP;
    }

    @Override
    public boolean isSupportImageType(byte[] buffer) {
        if (buffer == null || buffer.length <= 0)
            return false;
        // Bmp  42 4D
        if (buffer.length >= 2) {
            return buffer[0] == 0x42 && buffer[1] == 0x4D;
        }
        return false;
    }

    @Override
    public int[] getImageSize(InputStream stream, byte[] buffer) throws IOException {
        int[] size = new int[3];
        if (buffer == null || buffer.length <= 0)
            return size;
        buffer = ByteArrayUtil.findSizeBytes(stream, buffer, 18, 8);
        size[0] = ByteBuffer.wrap(ByteArrayUtil.reverse(ByteArrayUtil.cut(buffer, 0, 4))).getInt();
        size[1] = ByteBuffer.wrap(ByteArrayUtil.reverse(ByteArrayUtil.cut(buffer, 4, 4))).getInt();
        size[2] = getSupportImageType();
        return size;
    }
}
