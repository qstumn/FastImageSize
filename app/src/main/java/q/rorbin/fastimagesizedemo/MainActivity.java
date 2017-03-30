package q.rorbin.fastimagesizedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;

import q.rorbin.fastimagesize.FastImageSize;
import q.rorbin.fastimagesize.request.ImageSizeCallback;


public class MainActivity extends AppCompatActivity {
    TextView result;
    ImageView ivResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String url="http://image.hnol.net/c/2013-10/14/00/201310140015197351-4228429.jpg";
        result = (TextView) findViewById(R.id.tv_result);
        ivResult = (ImageView) findViewById(R.id.iv_result);
        findViewById(R.id.btn_measure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final long startL = System.currentTimeMillis();
                FastImageSize
                        .with(url)
                        .get(new ImageSizeCallback() {
                            @Override
                            public void onSizeReady(int[] size) {
                                result.setText("图片尺寸: " + Arrays.toString(size) + "   用时 : " + (System.currentTimeMillis() - startL) + "毫秒");
                            }
                        });
            }
        });
    }


}
