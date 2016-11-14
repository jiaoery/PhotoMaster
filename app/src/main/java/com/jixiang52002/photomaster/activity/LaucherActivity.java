package com.jixiang52002.photomaster.activity;

import android.app.Activity;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.ImageView;

import com.jixiang52002.photomaster.R;

import java.util.Timer;
import java.util.TimerTask;

import rk.or.Commands;
import rk.or.Model;
import rk.or.android.ModelSelection;
import rk.or.android.ModelView;
import rk.or.android.View3D;

public class LaucherActivity extends Activity{

    private ImageView imageView;
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
        setContentView(R.layout.activity_laucher);
        imageView= (ImageView) findViewById(R.id.image);
        imageView.setBackgroundResource(R.drawable.laucher);
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
