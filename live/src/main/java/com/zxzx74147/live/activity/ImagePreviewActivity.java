package com.zxzx74147.live.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.github.chrisbanes.photoview.PhotoView;
import com.zxzx74147.devlib.base.BaseActivity;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.image.ImageLoader;
import com.zxzx74147.live.R;
import com.zxzx74147.live.data.Text;
import com.zxzx74147.live.fragments.FeedReplyFragment;

public class ImagePreviewActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        IntentData<String> intentData = mIntentData;
        PhotoView photoView = findViewById(R.id.photo_view);
        ImageLoader.loadImageNoFill(photoView,intentData.data);
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition( 0,R.anim.scale_out);
    }
}
