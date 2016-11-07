package q.rorbin.fastimagesize.net;

import java.io.InputStream;

/**
 * Created by chqiu on 2016/10/26.
 */

public interface InputStreamProvider {
    InputStream getInputStream(String imagePath);
}
