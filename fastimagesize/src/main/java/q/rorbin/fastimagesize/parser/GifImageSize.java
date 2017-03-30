package q.rorbin.fastimagesize.parser;


import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import q.rorbin.fastimagesize.util.ByteArrayUtil;


/**
 * @author chqiu
 *         Email:qstumn@163.com
 */

public class GifImageSize extends ImageSize {
    @Override
    public int getSupportImageType() {
        return ImageType.GIF;
    }

    @Override
    public boolean isSupportImageType(byte[] buffer) {
        if (buffer == null || buffer.length <= 0)
            return false;
        // Gif 47 49 46 38 39|37 61
        if (buffer.length >= 6) {
            return buffer[0] == 0x47 && buffer[1] == 0x49 && buffer[2] == 0x46 && buffer[3] == 0x38
                    && (buffer[4] == 0x39 || buffer[4] == 0x37) && buffer[5] == 0x61;
        } else if (buffer.length >= 2) {
            return (buffer[0] == 0x47 && buffer[1] == 0x49);
        }
        return false;
    }

    @Override
    public int[] getImageSize(InputStream stream, byte[] buffer) throws IOException {
        int[] size = new int[3];
        if (buffer == null || buffer.length <= 0)
            return size;
        buffer = ByteArrayUtil.findSizeBytes(stream, buffer, 6, 4);
        byte[] mergeHead = new byte[]{0, 0};
        byte[] byte1 = ByteArrayUtil.merge(mergeHead, ByteArrayUtil.reverse(ByteArrayUtil.cut(buffer, 0, 2)));
        byte[] byte2 = ByteArrayUtil.merge(mergeHead, ByteArrayUtil.reverse(ByteArrayUtil.cut(buffer, 2, 2)));
        size[0] = ByteBuffer.wrap(byte1).getInt();
        size[1] = ByteBuffer.wrap(byte2).getInt();
        size[2] = getSupportImageType();
        return size;
    }
}
