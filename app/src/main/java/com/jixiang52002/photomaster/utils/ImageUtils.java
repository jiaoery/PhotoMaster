package com.jixiang52002.photomaster.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import java.io.File;

/**
 * Created by jixiang52002 on 2016/9/18.
 */
public class ImageUtils {


    /**
     * 根据制定的大小设定图片
     * @param dst 图片的file路径
     * @param width 更改后图片的宽度px
     * @param height 更改后图片的高度px
     * @return
     */
    public static Bitmap getBitmapFromFile(File dst, int width, int height) {
        if (null != dst && dst.exists()) {
            BitmapFactory.Options opts = null;
            if (width > 0 && height > 0) {
                opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(dst.getPath(), opts);
                // 计算图片缩放比例
                final int minSideLength = Math.min(width, height);
                opts.inSampleSize = computeSampleSize(opts, minSideLength,
                        width * height);
                opts.inJustDecodeBounds = false;
                opts.inInputShareable = true;
                opts.inPurgeable = true;
            }
            try {
                return BitmapFactory.decodeFile(dst.getPath(), opts);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math
                .floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * 色光三元素的处理
     *
     * @param bmp        需要处理的图像，这里的bitmap无法被修改
     * @param hue        图像的色调
     * @param saturation 图像的饱和度
     * @param lum        图像的亮度
     * @return
     */
    public static Bitmap handleImageEffect(Bitmap bmp, float hue, float saturation, float lum) {
        //创建画板类bitmap
        Bitmap bm = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
        //画布
        Canvas canvas = new Canvas(bm);
        //抗锯齿
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        //色调 axis=0 correspond to a rotation around the RED color axis=1 correspond to a rotation around the GREEN color axis=2 correspond to a rotation around the BLUE color
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setRotate(0, hue);
        colorMatrix.setRotate(1, hue);
        colorMatrix.setRotate(2, hue);
        //饱和度
        ColorMatrix saturationMatrix1 = new ColorMatrix();
        saturationMatrix1.setSaturation(saturation);
        //亮度
        ColorMatrix lumMatrix = new ColorMatrix();
        lumMatrix.setScale(lum, lum, lum, 1);

        //将特性设置进image中
        ColorMatrix imageMatrix = new ColorMatrix();
        imageMatrix.postConcat(colorMatrix);
        imageMatrix.postConcat(saturationMatrix1);
        imageMatrix.postConcat(lumMatrix);

        paint.setColorFilter(new ColorMatrixColorFilter(imageMatrix));

        canvas.drawBitmap(bmp, 0, 0, paint);

        return bm;
    }

    /**
     * 底片效果 像素处理图像
     *
     * @param bitmap
     * @return
     */
    public static Bitmap handleImageNegative(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int color;
        int r, g, b, a;
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int[] oldPx = new int[width * height];
        int[] newPx = new int[width * height];
        //取出所有像素点
        bitmap.getPixels(oldPx, 0, width, 0, 0, width, height);

        for (int i = 0; i < width * height; i++) {
            //获取像素点
            color = oldPx[i];
            a = Color.alpha(color);
            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);
            //底片效果算法
            r = 255 - r;
            g = 255 - g;
            b = 255 - b;
            //防止RGB值越界
            if (r > 255) {
                r = 255;
            } else if (r < 0) {
                r = 0;
            }
            if (g > 255) {
                g = 255;
            } else if (g < 0) {
                g = 0;
            }

            if (b > 255) {
                b = 255;
            } else if (b < 0) {
                b = 0;
            }
            newPx[i] = Color.argb(a, r, g, b);
        }
        bmp.setPixels(newPx, 0, width, 0, 0, width, height);
        return bmp;

    }


    /**
     * 怀旧效果 像素处理图像
     *
     * @param bitmap
     * @return
     */
    public static Bitmap handleImageOld(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int color;
        int r, g, b, a;
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int[] oldPx = new int[width * height];
        int[] newPx = new int[width * height];
        //取出所有像素点
        bitmap.getPixels(oldPx, 0, width, 0, 0, width, height);

        for (int i = 0; i < width * height; i++) {
            //获取像素点
            color = oldPx[i];
            a = Color.alpha(color);
            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);
            //怀旧效果算法
            r = (int) (0.393 * r + 0.769 * g + 0.189 * b);
            g = (int) (0.349 * r + 0.686 * g + 0.168 * b);
            b = (int) (0.272 * r + 0.534 * g + 0.131 * b);
            //防止RGB值越界
            if (r > 255) {
                r = 255;
            } else if (r < 0) {
                r = 0;
            }
            if (g > 255) {
                g = 255;
            } else if (g < 0) {
                g = 0;
            }

            if (b > 255) {
                b = 255;
            } else if (b < 0) {
                b = 0;
            }
            newPx[i] = Color.argb(a, r, g, b);
        }
        bmp.setPixels(newPx, 0, width, 0, 0, width, height);
        return bmp;

    }


    /**
     * 浮雕效果 像素处理图像
     *
     * @param bitmap
     * @return
     */
    public static Bitmap handleImageRelief(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int color, colorBefore;
        int r, g, b, a, r1, g1, b1, a1;
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int[] oldPx = new int[width * height];
        int[] newPx = new int[width * height];
        //取出所有像素点
        bitmap.getPixels(oldPx, 0, width, 0, 0, width, height);

        for (int i = 1; i < width * height; i++) {
            colorBefore = oldPx[i - 1];
            a = Color.alpha(colorBefore);
            r = Color.red(colorBefore);
            g = Color.green(colorBefore);
            b = Color.blue(colorBefore);
            //获取像素点
            color = oldPx[i];
            a1 = Color.alpha(color);
            r1 = Color.red(color);
            g1 = Color.green(color);
            b1 = Color.blue(color);
            //浮雕效果算法
            r = r-r1+127;
            g = g-g1+127;
            b = b-b1+127;
            //防止RGB值越界
            if (r > 255) {
                r = 255;
            } else if (r < 0) {
                r = 0;
            }
            if (g > 255) {
                g = 255;
            } else if (g < 0) {
                g = 0;
            }

            if (b > 255) {
                b = 255;
            } else if (b < 0) {
                b = 0;
            }
            newPx[i] = Color.argb(a, r, g, b);
        }
        bmp.setPixels(newPx, 0, width, 0, 0, width, height);
        return bmp;

    }


}
