package com.jixiang52002.photomaster.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jixiang52002.photomaster.R;
import com.jixiang52002.photomaster.config.Constacts;
import com.jixiang52002.photomaster.utils.BaseUtils;
import com.jixiang52002.photomaster.utils.ImageUtils;

import java.io.File;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.PermissionUtils;
import permissions.dispatcher.RuntimePermissions;
@RuntimePermissions
public class BeautifyCapture extends Activity implements View.OnFocusChangeListener{

    ImageView imagerView;//待处理图片
    LinearLayout watch;//观看效果
    LinearLayout increase;//三原色效果增强
    LinearLayout effect;//特效
    LinearLayout frame;//相框
    LinearLayout person;//个人中心

    private boolean isChanged=false;//图片是否有更改

    private String filePath;

    Bitmap bitmap;//正在处理的图像


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         /*
         * 防止键盘挡住输入框
         * 不希望遮挡设置activity属性 android:windowSoftInputMode="adjustPan"
         * 希望动态调整高度 android:windowSoftInputMode="adjustResize"
         */
        getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_ADJUST_PAN);
        //锁定屏幕
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_beautify_capture);
        initView();
        BeautifyCapturePermissionsDispatcher.initImageWithCheck(this);

    }

    //初始化界面
    private void initView() {
        imagerView= (ImageView) findViewById(R.id.imageView1);
        watch= (LinearLayout) findViewById(R.id.layout_watch);
        increase= (LinearLayout) findViewById(R.id.layout_increase);
        effect= (LinearLayout) findViewById(R.id.layout_effect);
        frame= (LinearLayout) findViewById(R.id.layout_frame);
        person= (LinearLayout) findViewById(R.id.layout_person);
        //为底部菜单添加相应的focuse（焦点）改变检测
        watch.setOnFocusChangeListener(this);
        increase.setOnFocusChangeListener(this);
        effect.setOnFocusChangeListener(this);
        frame.setOnFocusChangeListener(this);
        person.setOnFocusChangeListener(this);
        //为顶部菜单添加点击事件
        findViewById(R.id.view_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //检测是否有更改
                if(isChanged){

                }else{
                    finish();
                }
            }
        });

        findViewById(R.id.view_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 初始化图像
     */
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void initImage(){
        //获取从其他intent中传过来的数据
        Intent intent=getIntent();
        String filePath=intent.getStringExtra(Constacts.CAPTURE_FILES);
        bitmap=ImageUtils.getBitmapFromFile(new File(filePath),480,320);
        imagerView.setImageBitmap(bitmap);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BeautifyCapturePermissionsDispatcher.onRequestPermissionsResult(this,requestCode,grantResults);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    @OnShowRationale({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void shyWhy(final PermissionRequest request) {
        BaseUtils.Toast(request.toString());
    }


    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void cancel() {
        BaseUtils.Toast("无法读取外部内存卡数据，请设置通过权限");
    }

    @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void never() {
        BaseUtils.Toast("永远无法读取外部内存卡数据，请设置通过权限");
    }
}
