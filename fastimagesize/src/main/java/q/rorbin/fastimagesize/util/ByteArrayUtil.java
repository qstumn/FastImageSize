package q.rorbin.fastimagesize.util;

import java.io.IOException;
import java.io.InputStream;

import static android.R.attr.value;

/**
 * Created by chqiu on 2016/10/26.
 */

public class ByteArrayUtil {
    public static byte[] merge(byte[] b1, byte[] b2) {
        byte[] result = new byte[b1.length + b2.length];
        System.arraycopy(b1, 0, result, 0, b1.length);
        System.arraycopy(b2, 0, result, b1.length, b2.length);
        return result;
    }

    public static byte[] reverse(byte[] source) {
        int length = source.length;
        byte temp;
        for (int i = 0; i < length >> 1; i++) {
            temp = source[i];
            source[i] = source[length - 1 - i];
            source[length - 1 - i] = temp;
        }
        return source;
    }

    public static byte[] cut(byte[] source, int start, int length) {
        byte[] result = new byte[length];
        for (int i = 0; i < length; i++) {
            result[i] = source[start + i];
        }
        return result;
    }

    public static byte[] findSizeBytes(InputStream stream, byte[] buffer, int frontLength, int sizeLength) throws IOException {
        int skipLength = frontLength - buffer.length;
        if (skipLength >= 0) {
            if (skipLength != 0) {
                stream.skip(skipLength);
            }
            buffer = new byte[sizeLength];
            stream.read(buffer, 0, buffer.length);
        } else {
            int finalLength = sizeLength - Math.abs(skipLength);
            if (finalLength > 0) {
                byte[] skipBuffer = new byte[finalLength];
                stream.read(skipBuffer, 0, skipBuffer.length);
                buffer = ByteArrayUtil.merge(buffer, skipBuffer);
            }
            buffer = ByteArrayUtil.cut(buffer, frontLength, sizeLength);
        }
        return buffer;
    }
}
