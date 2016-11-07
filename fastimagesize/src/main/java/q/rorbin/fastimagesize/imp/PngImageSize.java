package q.rorbin.fastimagesize.imp;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import q.rorbin.fastimagesize.ImageSize;
import q.rorbin.fastimagesize.ImageType;
import q.rorbin.fastimagesize.util.ByteArrayUtil;


/**
 * Created by chqiu on 2016/10/26.
 */

public class PngImageSize extends ImageSize {
    @Override
    public ImageType getSupportImageType() {
        return ImageType.PNG;
    }

    @Override
    public boolean isSupportImageType(byte[] buffer) {
        if (buffer == null || buffer.length <= 0)
            return false;
        // Png：89 50 4E 47 0D 0A 1A 0A
        if (buffer.length >= 8) {
            return (buffer[0] == (byte) 0x89 && buffer[1] == 0x50 && buffer[2] == 0x4E && buffer[3] == 0x47
                    && buffer[4] == 0x0D && buffer[5] == 0x0A && buffer[6] == 0x1A && buffer[7] == 0x0A);
        } else if (buffer.length >= 2) {
            return (buffer[0] == (byte) 0x89 && buffer[1] == 0x50);
        }
        return false;
    }

    @Override
    public int[] getImageSize(InputStream stream, byte[] buffer) throws IOException {
        int[] size = new int[2];
        if (buffer == null || buffer.length <= 0)
            return size;
        buffer = ByteArrayUtil.findSizeBytes(stream, buffer, 16, 8);
        size[0] = ByteBuffer.wrap(buffer, 0, 4).getInt();
        size[1] = ByteBuffer.wrap(buffer, 4, 4).getInt();
        return size;
    }
}
