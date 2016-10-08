package com.jixiang52002.photomaster.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;

import com.jixiang52002.photomaster.R;
import com.jixiang52002.photomaster.customview.ImageMatrixView;

public class ImageMatrixTest extends Activity {

    private GridLayout mGridLayout;
    private ImageMatrixView mImageMatrixview;
    private int mWidth,mHeight;
    private float[] mImageMatrix=new float[9];
    private EditText[] mEts=new EditText[9];
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_matrix_test);
        mImageMatrixview= (ImageMatrixView) findViewById(R.id.imageview);
        String filePath=getIntent().getStringExtra("file");
        if(filePath!=null){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 30;//图片宽高都为原来的二分之一，即图片为原来的四分之一
            bitmap=BitmapFactory.decodeFile(filePath,options);
            mImageMatrixview.setImageBitmap(bitmap);
        }
        mGridLayout= (GridLayout) findViewById(R.id.gridlayout);
        mGridLayout.post(new Runnable() {
            @Override
            public void run() {
                mWidth=mGridLayout.getWidth()/3;
                mHeight=mGridLayout.getHeight()/3;
                addEts();
                initImageMatrix();
            }
        });
    }

    private void addEts() {
        for (int i = 0; i <9 ; i++) {
            EditText editText=new EditText(this);
            editText.setGravity(Gravity.CENTER);
            mEts[i]=editText;
            mGridLayout.addView(editText,mWidth,mHeight);
        }
    }

    private void initImageMatrix(){
        for (int i = 0; i <9 ; i++) {
            if(i%4==0){
                mEts[i].setText(String.valueOf(1));
            }else {
                mEts[i].setText(String.valueOf(0));
            }

        }
    }

    public void btnChange(View View){
        getMatrix();
        Matrix matrix=new Matrix();
        matrix.setValues(mImageMatrix);
        //使用Android自带api实现图像处理
//        matrix.setTranslate(150,150);
//        matrix.setScale(2,2);
        //实现效果的顺序显示
//        matrix.postTranslate(200,200);
        mImageMatrixview.setImageMatrix(matrix);
        mImageMatrixview.invalidate();

    }

    private void getMatrix() {
        for (int i = 0; i < 9; i++) {
            EditText editText=mEts[i];
            mImageMatrix[i]=Float.valueOf(editText.getText().toString());
        }
    }

    public void btnReset(View view){
        initImageMatrix();
        getMatrix();
        Matrix matrix=new Matrix();
        matrix.setValues(mImageMatrix);
        mImageMatrixview.setImageMatrix(matrix);
        mImageMatrixview.invalidate();


    }
}
