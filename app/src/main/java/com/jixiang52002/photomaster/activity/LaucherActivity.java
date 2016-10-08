package com.jixiang52002.photomaster.activity;

import android.app.Activity;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

import com.jixiang52002.photomaster.R;

import java.util.Timer;
import java.util.TimerTask;

import rk.or.Commands;
import rk.or.Model;
import rk.or.android.ModelSelection;
import rk.or.android.ModelView;
import rk.or.android.View3D;

public class LaucherActivity extends ModelView{


    public  static int MESSAGE_WHAT=1;
    TimerTask timerTask=new TimerTask() {
        @Override
        public void run() {
            handler.sendEmptyMessage(MESSAGE_WHAT);
        }
    };
    Timer timer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (View3D.front == 0) {
            View3D.texturesON = true;
            View3D.front = wuziqi.jixiang.com.orinsin3d.R.drawable.gally400x572;
//    		View3D.back = R.drawable.blue32x32;
            View3D.back = wuziqi.jixiang.com.orinsin3d.R.drawable.sunako400x572;
            View3D.background = R.drawable.guide;
        }
        timer=new Timer(true);
        timer.schedule(timerTask,5000,5000);

    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    timer.cancel();
                    Intent intent=new Intent(LaucherActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };
}
