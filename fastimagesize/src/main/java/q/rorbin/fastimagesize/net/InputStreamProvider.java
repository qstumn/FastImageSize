package q.rorbin.fastimagesize.net;

import java.io.InputStream;

/**
 * @author chqiu
 *         Email:qstumn@163.com
 */

public interface InputStreamProvider {
    InputStream getInputStream(String imagePath);
}
