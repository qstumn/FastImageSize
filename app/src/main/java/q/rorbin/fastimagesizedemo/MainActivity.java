package q.rorbin.fastimagesizedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;
import java.util.Arrays;

import q.rorbin.fastimagesize.FastImageSize;
import q.rorbin.fastimagesize.net.DefaultInputStreamProvider;

public class MainActivity extends AppCompatActivity {
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = (TextView) findViewById(R.id.tv_result);
        findViewById(R.id.btn_measure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final long startL = System.currentTimeMillis();
                FastImageSize.with("http://192.168.12.115:8080/docs/api/111111.jpg")
                        .get(new FastImageSize.ImageSizeCallback() {
                    @Override
                    public void onSizeReady(int[] size) {
                        result.setText("图片尺寸: " + Arrays.toString(size) + "   用时 : " + (System.currentTimeMillis() - startL) + "毫秒");
                    }
                });
            }
        });
    }


}
