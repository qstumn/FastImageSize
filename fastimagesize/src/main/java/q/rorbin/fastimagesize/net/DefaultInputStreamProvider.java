package q.rorbin.fastimagesize.net;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author chqiu
 *         Email:qstumn@163.com
 */

public class DefaultInputStreamProvider implements InputStreamProvider {
    private static final DefaultInputStreamProvider INSTANCE = new DefaultInputStreamProvider();

    public static DefaultInputStreamProvider getInstance() {
        return INSTANCE;
    }

    @Override
    public InputStream getInputStream(String imagePath) {
        InputStream stream = null;
        try {
            if (imagePath.startsWith("http")) {
                URLConnection connection = new URL(imagePath).openConnection();
                connection.setConnectTimeout(1000);
                connection.setReadTimeout(1000);
                connection.connect();
                stream = connection.getInputStream();
            } else {
                File file = new File(imagePath);
                if (file.exists()) {
                    stream = new FileInputStream(file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return stream;
        }
        return stream;
    }
}
