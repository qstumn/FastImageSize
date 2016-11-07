package q.rorbin.fastimagesize.imp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import q.rorbin.fastimagesize.ImageSize;
import q.rorbin.fastimagesize.ImageType;
import q.rorbin.fastimagesize.util.ByteArrayUtil;

/**
 * Created by chqiu on 2016/10/26.
 */

public class BmpImageSize extends ImageSize {
    @Override
    public ImageType getSupportImageType() {
        return ImageType.BMP;
    }

    @Override
    public boolean isSupportImageType(byte[] buffer) {
        if (buffer == null || buffer.length <= 0)
            return false;
        // Bmp ：42 4D
        if (buffer.length >= 2) {
            return buffer[0] == 0x42 && buffer[1] == 0x4D;
        }
        return false;
    }

    @Override
    public int[] getImageSize(InputStream stream, byte[] buffer) throws IOException {
        int[] size = new int[2];
        if (buffer == null || buffer.length <= 0)
            return size;
        buffer = ByteArrayUtil.findSizeBytes(stream, buffer, 18, 8);
        size[0] = ByteBuffer.wrap(ByteArrayUtil.reverse(ByteArrayUtil.cut(buffer, 0, 4))).getInt();
        size[1] = ByteBuffer.wrap(ByteArrayUtil.reverse(ByteArrayUtil.cut(buffer, 4, 4))).getInt();
        return size;
    }
}
