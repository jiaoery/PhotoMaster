package com.jixiang52002.photomaster.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jixiang52002.photomaster.R;
import com.jixiang52002.photomaster.customview.RoundRectXfermodeView;

public class XfermoduleTest extends Activity {

    private RoundRectXfermodeView view;

    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xfermodule);
        view= (RoundRectXfermodeView) findViewById(R.id.view);
        String filePath=getIntent().getStringExtra("file");
        if(filePath!=null){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;//图片宽高都为原来的二分之一，即图片为原来的四分之一
            bitmap=BitmapFactory.decodeFile(filePath,options);
            view.setImageBitmap(bitmap);
        }
    }


}
