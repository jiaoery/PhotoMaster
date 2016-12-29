package com.jixiang52002.photomaster.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.jixiang52002.photomaster.R;
import com.jixiang52002.photomaster.config.Constacts;
import com.jixiang52002.photomaster.utils.BaseUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends Activity {

    //美化图片，拍照
    public Button makePhoto, camera;

    public static final int REQ_1 = 1;

    public static final int REQ_2 = 2;

    public static final int REQ_3 = 3;

    //图片的保存地址
    String filePath = Environment.getExternalStorageDirectory().getPath() + "/temp.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    //初始化界面程序
    private void initView() {
        makePhoto = (Button) findViewById(R.id.beatifulphoto);
        camera = (Button) findViewById(R.id.camera);
    }

    /**
     * 打开相机
     *
     * @param view
     */

    public void openCamera(View view) {
       MainActivityPermissionsDispatcher.getCameraWithCheck(this);

    }

    @NeedsPermission({Manifest.permission.CAMERA})
    public void getCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = Uri.fromFile(new File(filePath));
        //让拍照的照片放在指定的目录下 uri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, REQ_2);
//        startActivity(intent);
    }


    /**
     * 美化图片
     *
     * @param view
     */

    public void beatifulPhoto(View view) {
       MainActivityPermissionsDispatcher.getPhotoWithCheck(this);
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE})
    public void getPhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQ_1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_1) {
                //选择相册图片
                if (data != null) {
                    Uri uri = data.getData();
                    Intent intent = new Intent(this, BeautifyCapture.class);
                    intent.putExtra(Constacts.CAPTURE_FILES, uri.getEncodedPath());
                    startActivity(intent);
                }
            } else if (requestCode == REQ_2) {
                //将拍摄的图片传递到图片美化的Activity
                Intent intent = new Intent(this, BeautifyCapture.class);
                intent.putExtra(Constacts.CAPTURE_FILES, filePath);
                startActivity(intent);
            }
        }
    }


}
