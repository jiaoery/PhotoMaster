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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.jixiang52002.photomaster.R;
import com.jixiang52002.photomaster.adapter.FunctionAdapter;
import com.jixiang52002.photomaster.adapter.FunctionItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class MainActivity extends Activity{

    private ImageView imageView;
    private Bitmap bitmap;
    //按钮
    private ImageButton camera,photograph,share,save;
    //功能列表
    private GridView functions;

    private List<FunctionItem> mList;

    public static final int REQ_1=1;

    public static final int REQ_2=2;

    public static final int REQ_3=3;

    String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        initView();
//        initFunction();
    }

//    //初始化界面程序
//    private void initView() {
//        imageView= (ImageView) findViewById(R.id.imageview);
//        camera= (ImageButton) findViewById(R.id.camera);
//        photograph= (ImageButton) findViewById(R.id.photograph);
//        share= (ImageButton) findViewById(R.id.share);
//        save= (ImageButton) findViewById(R.id.save);
//        functions= (GridView) findViewById(R.id.function);
//        camera.setOnClickListener(this);
//        photograph.setOnClickListener(this);
//        share.setOnClickListener(this);
//        save.setOnClickListener(this);
//
//    }
//
//    private void initFunction(){
//        mList=new ArrayList<>();
//        mList.add(new FunctionItem("RGBA三原色", Color.RED));
//        mList.add(new FunctionItem("颜色矩阵",Color.BLUE));
//        mList.add(new FunctionItem("像素块",Color.BLUE));
//        mList.add(new FunctionItem("基本矩阵",Color.BLUE));
//        mList.add(new FunctionItem("圆角图片",Color.BLUE));
//        mList.add(new FunctionItem("原型图片",Color.GREEN));
//        mList.add(new FunctionItem("倒影效果",Color.MAGENTA));
//        mList.add(new FunctionItem("数学函数",Color.TRANSPARENT));
//        FunctionAdapter adapter=new FunctionAdapter(this,mList);
//        functions.setAdapter(adapter);
//        functions.setOnItemClickListener(this);
//    }
//
//    /**
//     * 色光三原色调整
//     */
//    public void btnPrimaryColor(){
//        Intent intent=new Intent(this,PrimaryColor.class);
//        intent.putExtra("file",filePath);
//        startActivity(intent);
//    }
//
//    //矩阵变换实现图像处理
//    public void btnColorMatrix(){
//        Intent intent=new Intent(this,ColorMatrix.class);
//        intent.putExtra("file",filePath);
//        startActivity(intent);
//    }
//
//    //像素点阵实现图像处理
//    public void btnPiexlsEffect(){
//        Intent intent=new Intent(this,PixelsEffect.class);
//        intent.putExtra("file",filePath);
//        startActivity(intent);
//    }
//
//    //通过基本矩阵实现四种效果
//    public void btnMatrix(){
//        Intent intent=new Intent(this,ImageMatrixTest.class);
//        intent.putExtra("file",filePath);
//        startActivity(intent);
//    }
//
//    //Paint 的xformodule方式实现圆角图片
//    public void btnXfermodule(){
//        Intent intent=new Intent(this,XfermoduleTest.class);
//        intent.putExtra("file",filePath);
//        startActivity(intent);
//    }
//
//    //Paint 的shader方式实现圆角图片
//    public void btnShader(){
//        Intent intent=new Intent(this,BitmapShaderTest.class);
//        startActivity(intent);
//    }
//
//    //实现倒影效果和背景效果
//    public void btnReflect(){
//        Intent intent=new Intent(this,ReflectViewTest.class);
//        startActivity(intent);
//    }
//
//    //以数学函数的方式实现一个动态的效果图
//    public void btnMeshView(){
//        Intent intent=new Intent(this,MeshViewTest.class);
//        startActivity(intent);
//    }
//
//
//
//    private void showShare() {
//        ShareSDK.initSDK(this);
//        OnekeyShare oks = new OnekeyShare();
////关闭sso授权
//        oks.disableSSOWhenAuthorize();
//
//// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
//        oks.setTitle(getString(R.string.app_name));
//// titleUrl是标题的网络链接，QQ和QQ空间等使用
//        oks.setTitleUrl("http://img0.imgtn.bdimg.com/it/u=1398349228,4122099846&fm=21&gp=0.jpg");
//// text是分享文本，所有平台都需要这个字段
//        oks.setText("请看我用拍照大师p的美图");
//// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
////oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
//// url仅在微信（包括好友和朋友圈）中使用
//        oks.setUrl("http://sharesdk.cn");
//// comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
//// site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite(getString(R.string.app_name));
//// siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl("http://sharesdk.cn");
///** imagePath是本地的图片路径，除Linked-In外的所有平台都支持这个字段 */
//        oks.setImagePath(filePath);
//
//// 启动分享GUI
//        oks.show(this);
//    }
//
//    /**
//     * 打开相机获取图片
//     */
//    public void camera(){
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            //申请CAMERA权限
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 3);
//        } else {
//            openCamera();
//        }
//    }
//
//    private void openCamera() {
//        filePath= Environment.getExternalStorageDirectory().getPath()+"/"+System.currentTimeMillis()+".png";
//        Uri uri = Uri.fromFile(new File(filePath));
//        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(intent,REQ_1);
//        }
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (3 == requestCode) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                openCamera();
//            } else {
//                // 未授权
//                Log.d(this.toString(),"无访问权限");
//            }
//        }
//    }
//
//
//    /**
//     * 从相册中获取图片
//     */
//    public void photogragh(){
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("image/*");
//        intent.putExtra("crop", "true");
//        intent.putExtra("aspectX",1);
//        intent.putExtra("aspectY",1);
//        intent.putExtra("outputX", 80);
//        intent.putExtra("outputY", 80);
//        intent.putExtra("return-data",true);
//        startActivityForResult(intent,REQ_2);
//
//    }
//    //保存图片
//    public void save(){
//        String filePath=Environment.getExternalStorageDirectory().getPath()+"/"+System.currentTimeMillis()+".png";
//        File file=new File(filePath);
//        try {
//            FileOutputStream fos=new FileOutputStream(file);
//            if(bitmap!=null){
//                bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
//                Log.d(this.toString(),filePath+"保存完毕");
//                Toast.makeText(this,"保存完毕",Toast.LENGTH_SHORT).show();
//            }else{
//                Toast.makeText(this,"没有可保存的图片",Toast.LENGTH_SHORT).show();
//            }
//            fos.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            FileInputStream fis=null;
//            if (requestCode == REQ_1) {
//                //通过将拍照后的照片保存在指定的目录下，然后通过io流获取相应的图像数据
//                try {
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inSampleSize = 2;//图片宽高都为原来的二分之一，即图片为原来的四分之一
//                    fis = new FileInputStream(filePath);
//                    bitmap = BitmapFactory.decodeStream(fis,null,options);
//                    imageView.setImageBitmap(bitmap);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } finally {
//                    try {
//                        fis.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }else if(requestCode == REQ_2){
//                filePath=data.getData().getPath();
//                bitmap=BitmapFactory.decodeFile(filePath);
//                imageView.setImageBitmap(bitmap);
//            }
//        }
//    }
//
//    //点击事件
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.camera:
//                camera();
//                break;
//            case R.id.photograph:
//                photogragh();
//                break;
//            case R.id.share:
//                showShare();
//                break;
//            case R.id.save:
//                save();
//        }
//
//    }
//
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        switch (position){
//            case 0:
//                //色光三原色
//                btnPrimaryColor();
//                break;
//            case 1:
//                //颜色矩阵
//                btnColorMatrix();
//                break;
//            case 2:
//                //像素块
//                btnPiexlsEffect();
//                break;
//            case 3:
//                //基本矩阵变换
//                btnMatrix();
//                break;
//            case 4:
//                //圆角图片
//                btnXfermodule();
//                break;
//            case 5:
//                //圆形图片
//                btnShader();
//                break;
//            case 6:
//                //倒影效果
//                btnReflect();
//                break;
//            case 7:
//                //数学函数
//                btnMeshView();
//                break;
//        }
//
//    }
}
