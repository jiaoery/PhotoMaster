package com.jixiang52002.photomaster.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.jixiang52002.photomaster.R;

/**
 * Created by jixiang52002 on 2016/9/23.
 */
public class BitmapShaderView extends View {
    private Bitmap mBitmap;

    private Paint mParint;

    private BitmapShader bitmapShader;

    public BitmapShaderView(Context context) {
        super(context);
    }

    public BitmapShaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BitmapShaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public BitmapShaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }


    public void setImageBitmap(Bitmap bitmap){
        mBitmap=bitmap;
        //重绘图像
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mBitmap==null)
        mBitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        mParint=new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmapShader=new BitmapShader(mBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        mParint.setShader(bitmapShader);
        canvas.drawCircle(300,200,300,mParint);
    }
}
