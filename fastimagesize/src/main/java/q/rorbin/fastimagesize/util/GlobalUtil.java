package q.rorbin.fastimagesize.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * @author chqiu
 *         Email:qstumn@163.com
 */

public class GlobalUtil {

    public static void geometricScaling(int width, int height,
                                        float displaySize, int[] size) {
        if (width > height) {
            height = (int) (height / (width / displaySize));
            width = (int) displaySize;
        } else if (width < height) {
            width = (int) (width / (height / displaySize));
            height = (int) displaySize;
        } else {
            width = (int) displaySize;
            height = (int) displaySize;
        }
        size[0] = width;
        size[1] = height;
    }

    public static String hashKeyForLru(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            StringBuilder sb = new StringBuilder();
            byte[] bytes = mDigest.digest();
            for (byte aByte : bytes) {
                String hex = Integer.toHexString(0xFF & aByte);
                if (hex.length() == 1) {
                    sb.append('0');
                }
                sb.append(hex);
            }
            cacheKey = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }
}
