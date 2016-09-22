package com.jixiang52002.photomaster.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;

import com.jixiang52002.photomaster.R;

/**
 * Created by jixiang52002 on 2016/9/22.
 */
public class ImageMatrixView extends View {
    private Bitmap mBitmap;

    private Matrix mMatrix;


    public ImageMatrixView(Context context) {
        super(context);
        initView();
    }

    public ImageMatrixView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ImageMatrixView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView(){
        mBitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        setImageMatrix(new Matrix());
    }


    public void setImageMatrix(Matrix matrix){
        mMatrix=matrix;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap,0,0,null);
        //对比图
        canvas.drawBitmap(mBitmap,mMatrix,null);
    }
}
