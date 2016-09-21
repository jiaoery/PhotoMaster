package com.jixiang52002.photomaster.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.jixiang52002.photomaster.R;
import com.jixiang52002.photomaster.utils.ImageUtils;

public class PixelsEffect extends AppCompatActivity {

    private ImageView imageView1;

    private ImageView imageView2;

    private ImageView imageView3;

    private ImageView imageView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pixels_effect);
        imageView1= (ImageView) findViewById(R.id.imageview1);
        imageView2= (ImageView) findViewById(R.id.imageview2);
        imageView3= (ImageView) findViewById(R.id.imageview3);
        imageView4= (ImageView) findViewById(R.id.imageview4);
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.test2);

        imageView1.setImageBitmap(bitmap);
        imageView2.setImageBitmap(ImageUtils.handleImageNegative(bitmap));
        imageView3.setImageBitmap(ImageUtils.handleImageOld(bitmap));
        imageView4.setImageBitmap(ImageUtils.handleImageRelief(bitmap));

    }
}
