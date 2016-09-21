package com.jixiang52002.photomaster.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.jixiang52002.photomaster.R;

public class ColorMatrix extends AppCompatActivity {

    private ImageView mImageview;

    private GridLayout mGridlayout;

    private Bitmap bitmap;

    private int mWidth,mHeight;
    private EditText[] edts=new EditText[20];
    private float[] colorMatriax=new float[20];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_matrix);
        bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.test1);
        mImageview= (ImageView) findViewById(R.id.imageview);
        mImageview.setImageBitmap(bitmap);
        mGridlayout= (GridLayout) findViewById(R.id.gridlayout);
        //控件绘制完毕后调用
        mGridlayout.post(new Runnable() {
            @Override
            public void run() {
                mWidth=mGridlayout.getWidth()/5;
                mHeight=mGridlayout.getHeight()/4;
                addEts();
                intMatriax();
            }
        });


    }

    /**
     * 添加20个edittext
     */
    private void addEts(){
        for (int i = 0; i <20; i++) {
            EditText editText=new EditText(this);
            edts[i]=editText;
            mGridlayout.addView(editText,mWidth,mHeight);
        }
    }


    private void intMatriax(){
        for (int i = 0; i < 20; i++) {
            if(i%6==0){
                edts[i].setText(String.valueOf(1));
            }else{
                edts[i].setText(String.valueOf(0));
            }
        }
    }

    public void btnChange(View view){
        getMatrix();
        setImageMatrix();
    }

    private void getMatrix(){
        for (int i = 0; i <20 ; i++) {
            colorMatriax[i]=Float.valueOf(edts[i].getText().toString());

        }
    }

    /**
     * 根据矩阵做出颜色变换
     */
    private void setImageMatrix(){
        Bitmap bmp=Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        android.graphics.ColorMatrix colorMatrix=new android.graphics.ColorMatrix();
        colorMatrix.set(colorMatriax);
        Canvas canvas=new Canvas(bmp);
        Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap,0,0,paint);
        mImageview.setImageBitmap(bmp);
    }

    public void btnReset(View view){
       intMatriax();
        getMatrix();
        setImageMatrix();
    }
}
