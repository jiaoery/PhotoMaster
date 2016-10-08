package com.jixiang52002.photomaster.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.jixiang52002.photomaster.R;

/**
 * Created by jixiang52002 on 2016/9/22.
 * 实现图像的圆角处理
 */
public class RoundRectXfermodeView extends View {
    private Bitmap mBitmap;
    private Bitmap mOut;
    private Paint mPaint;

    public RoundRectXfermodeView(Context context) {
        super(context);
        initView();
    }

    public RoundRectXfermodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public RoundRectXfermodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

//    public RoundRectXfermodeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    public void setImageBitmap(Bitmap bitmap){
        mBitmap=bitmap;
        initView();
        invalidate();
    }


    private void initView(){
        //关闭硬件加速，防止xformadule无法生效
        setLayerType(LAYER_TYPE_SOFTWARE,null);
        if(mBitmap==null)
        mBitmap= BitmapFactory.decodeResource(getResources(), R.drawable.test1);
        mOut=Bitmap.createBitmap(mBitmap.getWidth(),mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(mOut);
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        //绘制圆角矩形 Dst
//        canvas.drawRoundRect(0,0,mBitmap.getWidth(),mBitmap.getHeight(),50,50,mPaint);
        canvas.drawRoundRect(new RectF(0, 0, mBitmap.getWidth(), mBitmap.getHeight()), 50, 50, mPaint);
//        canvas.drawRoundRect(null,50,50,mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //Src
        canvas.drawBitmap(mBitmap,0,0,mPaint);
        mPaint.setXfermode(null);
    }




    @Override
    protected void onDraw(Canvas canvas) {
       canvas.drawBitmap(mOut,0,0,null);
    }
}
