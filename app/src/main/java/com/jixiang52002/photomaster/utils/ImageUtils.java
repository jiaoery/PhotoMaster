package com.jixiang52002.photomaster.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

/**
 * Created by jixiang52002 on 2016/9/18.
 */
public class ImageUtils {

    /**
     * 色光三元素的处理
     * @param bmp 需要处理的图像，这里的bitmap无法被修改
     * @param hue 图像的色调
     * @param saturation 图像的饱和度
     * @param lum 图像的亮度
     * @return
     */
    public static Bitmap handleImageEffect(Bitmap bmp,float hue,float saturation,float lum){
        //创建画板类bitmap
        Bitmap bm=Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), Bitmap.Config.ARGB_8888);
        //画布
        Canvas canvas=new Canvas(bm);
        //抗锯齿
        Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);

        //色调 axis=0 correspond to a rotation around the RED color axis=1 correspond to a rotation around the GREEN color axis=2 correspond to a rotation around the BLUE color
        ColorMatrix colorMatrix=new ColorMatrix();
        colorMatrix.setRotate(0,hue);
        colorMatrix.setRotate(1,hue);
        colorMatrix.setRotate(2,hue);
       //饱和度
        ColorMatrix saturationMatrix1=new ColorMatrix();
        saturationMatrix1.setSaturation(saturation);
        //亮度
        ColorMatrix lumMatrix=new ColorMatrix();
        lumMatrix.setScale(lum,lum,lum,1);

        //将特性设置进image中
        ColorMatrix imageMatrix=new ColorMatrix();
        imageMatrix.postConcat(colorMatrix);
        imageMatrix.postConcat(saturationMatrix1);
        imageMatrix.postConcat(lumMatrix);

        paint.setColorFilter(new ColorMatrixColorFilter(imageMatrix));

        canvas.drawBitmap(bmp,0,0,paint);

        return bm;
    }
}
