package com.jixiang52002.photomaster.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.jixiang52002.photomaster.R;

/**
 * Created by jixiang52002 on 2016/9/23.
 */
public class ReflectView extends View {

    private Bitmap mSrcBitmap;

    private Bitmap mReflectBitmap;

    private Paint mPaint;


    public ReflectView(Context context) {
        super(context);
        initView();
    }

    public ReflectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ReflectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    public void  setImageBitmap(Bitmap bitmap){
        mSrcBitmap=bitmap;
        initView();
        invalidate();

    }

   private void initView(){
       if(mSrcBitmap==null)
       mSrcBitmap= BitmapFactory.decodeResource(getResources(), R.drawable.test);
       Matrix matrix=new Matrix();
       //实现镜像效果
       matrix.setScale(1,-1);
       mReflectBitmap=Bitmap.createBitmap(mSrcBitmap,0,0,mSrcBitmap.getWidth(),mSrcBitmap.getHeight()
       ,matrix,true);
       mPaint=new Paint();
       //渐变效果
       mPaint.setShader(new LinearGradient(0,mSrcBitmap.getHeight(),0,mSrcBitmap.getHeight()*1.4F
       , 0XDD000000,0X00FF00, Shader.TileMode.CLAMP));
       mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(mSrcBitmap,0,0,null);
        canvas.drawBitmap(mReflectBitmap,0,mSrcBitmap.getHeight(),null);
        canvas.drawRect(new RectF(0,mReflectBitmap.getHeight(),mReflectBitmap.getWidth(),mReflectBitmap.getHeight()*2),mPaint);
    }
}
