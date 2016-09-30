package com.jixiang52002.photomaster.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.jixiang52002.photomaster.R;
import com.jixiang52002.photomaster.utils.ImageUtils;

/**
 * Created by jixiang52002 on 2016/9/18.
 * RGBA色光三原色实现图片效果的处理
 */
public class PrimaryColor extends Activity implements SeekBar.OnSeekBarChangeListener{

    private ImageView mImageview;

    private SeekBar hueSeekBar;

    private SeekBar saturationSeekBar;

    private SeekBar lumSeekBar;

    private static int MAX_VALUE=255;
    private static int MID_VALUE=127;

    private float mHue,mSaturation,mLum;

    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.primary_color);
        initView();
    }

    private void initView() {
        bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.test3);
        mImageview= (ImageView) findViewById(R.id.imageview);
        hueSeekBar= (SeekBar) findViewById(R.id.seekbarhue);
        saturationSeekBar= (SeekBar) findViewById(R.id.seekbarsaturation);
        lumSeekBar= (SeekBar) findViewById(R.id.seekbarlum);
        hueSeekBar.setOnSeekBarChangeListener(this);
        saturationSeekBar.setOnSeekBarChangeListener(this);
        lumSeekBar.setOnSeekBarChangeListener(this);
        hueSeekBar.setMax(MAX_VALUE);
        saturationSeekBar.setMax(MAX_VALUE);
        lumSeekBar.setMax(MAX_VALUE);
        hueSeekBar.setProgress(MID_VALUE);
        saturationSeekBar.setProgress(MID_VALUE);
        lumSeekBar.setProgress(MID_VALUE);
        mImageview.setImageBitmap(bitmap);
    }


    //三原色的数值发生改变
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        switch (seekBar.getId()){
            case R.id.seekbarhue:
                mHue=(progress-MID_VALUE)*1.0F/MID_VALUE*180;
                break;
            case R.id.seekbarsaturation:
                mSaturation=progress*1.0f/MID_VALUE;
                break;
            case R.id.seekbarlum:
                mLum=progress*1.0f/MID_VALUE;
                break;
        }
        //产生效果
        mImageview.setImageBitmap(ImageUtils.handleImageEffect(bitmap,mHue,mSaturation,mLum));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
