package q.rorbin.fastimagesize.request;

import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import q.rorbin.fastimagesize.parser.ImageType;
import q.rorbin.fastimagesize.R;

/**
 * @author chqiu
 *         Email:qstumn@163.com
 */

public class OverrideCallback implements ImageSizeCallback {
    private WeakReference<View> mWeakView;
    private int mOverrideSize;

    public OverrideCallback(View view, int overrideSize) {
        mWeakView = new WeakReference<>(view);
        mOverrideSize = overrideSize;
    }

    @Override
    public void onSizeReady(int[] size) {
        View view = mWeakView.get();
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        if (size[2] != ImageType.NULL) {
            params.width = size[0];
            params.height = size[1];
        }
        view.setLayoutParams(params);
        view.setTag(R.id.fast_image_size, size);
    }
}
