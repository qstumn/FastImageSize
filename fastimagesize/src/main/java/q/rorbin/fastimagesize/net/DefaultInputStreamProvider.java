package q.rorbin.fastimagesize.net;

import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import static android.R.attr.scheme;
import static android.content.ContentValues.TAG;

/**
 * Created by chqiu on 2016/10/26.
 */

public class DefaultInputStreamProvider implements InputStreamProvider {
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
