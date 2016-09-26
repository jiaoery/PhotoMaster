package com.jixiang52002.photomaster.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.jixiang52002.photomaster.R;

/**
 * Created by jixiang52002 on 2016/9/26.
 */
public class MeshView  extends View {

    private int WIDTH=200;

    private int HEIGHT=200;

    private int COUNT=(WIDTH+1)*(HEIGHT+1);
    //奇数保存x坐标，偶数表示y坐标
    private float[] verts=new float[COUNT*2];
    //原始的数组坐标
    private float[] origs=new float[COUNT*2];

    private float K=1f;

    private Bitmap mBitmap;
    public MeshView(Context context) {
        super(context);
        initView();
    }


    public MeshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MeshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        int index=0;
        mBitmap= BitmapFactory.decodeResource(getResources(), R.drawable.test1);
        float bmWidth=mBitmap.getWidth();
        float bmHeight=mBitmap.getHeight();
        //初始化像素点
        for (int i = 0; i <HEIGHT+1 ; i++) {
            float fy=bmHeight*i/HEIGHT;
            for (int j = 0; j <WIDTH+1 ; j++) {
                float fx=bmWidth*j/WIDTH;
                verts[index*2+0]=origs[index*2+0]=fx;
                verts[index*2+1]=origs[index*2+1]=fy;
                index+=1;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i <HEIGHT+1 ; i++) {
            for (int j = 0; j <WIDTH+1 ; j++) {
                verts[(i*(WIDTH+1)+j)*+0]+=0;
                //正弦函数分布
                float offsetY=(float) Math.sin((float)j/WIDTH*2*Math.PI+K);
                verts[(i*(WIDTH+1)+j)*2+1]=origs[(i*(WIDTH+1)+j)*2+1]+offsetY*50;

            }
        }
        K+=0.1F;
       canvas.drawBitmapMesh(mBitmap,WIDTH,HEIGHT,verts,0,null,0,null);
        invalidate();
    }
}
