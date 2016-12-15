//package com.jixiang52002.photomaster.utils;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Matrix;
//import android.net.Uri;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.PopupWindow;
//import android.widget.SeekBar;
//import android.widget.Toast;
//
//import com.jixiang52002.photomaster.R;
//import com.jixiang52002.photomaster.activity.MainActivity;
//
///**
// * Created by zn on 2016/12/15.
// */
//
//public class PopUtils {
//    //弹出按钮
//    private PopupWindow popWatch;
//    private PopupWindow popIncrease;
//    private PopupWindow popEffect;
//    private PopupWindow popFrame;
//    private PopupWindow popPerson;
//
//
//    public void initPopWatch(){
//
//    }
//
//    /*
//            * 函数功能 PopupWindow窗体动画
//    * 获取自定义布局文件
//    */
//    public void initmPopupWindowView(int number) {
//        View customView = null;
//        //触屏标记默认为0 否则点一次"缩放"总能移动
//        flagOnTouch  = 0;
//      	/*
//    	 * number=1 查看
//    	 */
//        if(number==1) {
//            customView = getLayoutInflater().inflate(R.layout.popup_watch, null, false);
//            // 创建PopupWindow实例  (250,180)分别是宽度和高度
//            popupWindow1 = new PopupWindow(customView, 450, 150);
//            // 使其聚集 要想监听菜单里控件的事件就必须要调用此方法
//            popupWindow1.setFocusable(true);
//            // 设置动画效果 [R.style.AnimationFade 是自己事先定义好的]
//            popupWindow1.setOutsideTouchable(true);
//            popupWindow1.setAnimationStyle(R.style.AnimationPreview);
//            // 自定义view添加触摸事件
//            customView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if (popupWindow1 != null && popupWindow1.isShowing()) {
//                        popupWindow1.dismiss();
//                        popupWindow1 = null;
//                    }
//                    return false;
//                }
//            });
//            //判断点击子菜单不同按钮实现不同功能
//            //自定义引用类
//            watchProcess = new WatchProcessImage(bmp);
//            LinearLayout layoutWatch2 = (LinearLayout) customView.findViewById(R.id.layout_watch2);
//            layoutWatch2.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    textShow.setText("图像处理--水平翻转");
//                    popupWindow1.dismiss();
//                    //调用WatchProcessImage中函数实现水平翻转
//                    mbmp = watchProcess.FlipHorizontalImage(bmp,flagWatch2);
//                    imageShow.setImageBitmap(mbmp);
//                    //标记变量 0翻转 1变回原图
//                    if(flagWatch2 == 0) {
//                        flagWatch2 = 1;
//                    } else if(flagWatch2 == 1) {
//                        flagWatch2 =0;
//                    }
//                }
//            });
//            LinearLayout layoutWatch3 = (LinearLayout) customView.findViewById(R.id.layout_watch3);
//            layoutWatch3.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    textShow.setText("图像处理--垂直翻转");
//                    popupWindow1.dismiss();
//                    mbmp = watchProcess.FlipVerticalImage(bmp,flagWatch3);
//                    imageShow.setImageBitmap(mbmp);
//                    //标记变量 0翻转 1变回原图
//                    if(flagWatch3 == 0) {
//                        flagWatch3 = 1;
//                    } else if(flagWatch3 == 1) {
//                        flagWatch3 =0;
//                    }
//                }
//            });
//            LinearLayout layoutWatch1 = (LinearLayout) customView.findViewById(R.id.layout_watch1);
//            layoutWatch1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    textShow.setText("图像处理--旋转图片");
//                    popupWindow1.dismiss();
//                    //旋转一次表示增加45度 模8表示360度=0度
//                    flagWatch1 = (flagWatch1+1) % 8;
//                    //设置背景颜色黑色
//                    //imageShow.setBackgroundColor(Color.parseColor("#000000"));
//                    mbmp = watchProcess.TurnImage(bmp, flagWatch1);
//                    imageShow.setImageBitmap(mbmp);
//                }
//            });
//            LinearLayout layoutWatch4 = (LinearLayout) customView.findViewById(R.id.layout_watch4);
//            layoutWatch4.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    textShow.setText("图像处理--移动缩放");
//                    popupWindow1.dismiss();
//                    flagOnTouch = 1; //标志变量
//                    //动态设置android:scaleType="matrix"
//                    imageShow.setScaleType(ImageView.ScaleType.MATRIX);
//                    imageShow.setImageBitmap(bmp);
//                }
//            });
//            LinearLayout layoutWatch5 = (LinearLayout) customView.findViewById(R.id.layout_watch5);
//            layoutWatch5.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    textShow.setText("图像处理--绘制图片");
//                    popupWindow1.dismiss();
//                    flagOnTouch = 2; //标志变量
//                    //动态设置android:scaleType="matrix"
//                    imageShow.setScaleType(ImageView.ScaleType.MATRIX);
//                    //画图 图片移动至(0,0) 否则绘图线与手指存在误差
//                    matrix = new Matrix();
//                    matrix.postTranslate(0, 0);
//                    imageShow.setImageMatrix(matrix);
//                    canvas.drawBitmap(bmp, matrix, paint);
//                    imageShow.setImageBitmap(alteredBitmap); //备份图片
//                }
//            });
//        }
//    	/*
//    	 * number=2 增强
//    	 */
//        if(number==2) {
//            customView = getLayoutInflater().inflate(R.layout.popup_increase, null, false);
//            //设置子窗体PopupWindow高度500 饱和度 色相 亮度
//            popupWindow2 = new PopupWindow(customView, 600, 500);
//            // 使其聚集 要想监听菜单里控件的事件就必须要调用此方法
//            popupWindow2.setFocusable(true);
//            // 设置允许在外点击消失
//            popupWindow2.setOutsideTouchable(true);
//            popupWindow2.setAnimationStyle(R.style.AnimationPreview);
//            // 自定义view添加触摸事件
//            customView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if (popupWindow2 != null && popupWindow2.isShowing()) {
//                        popupWindow2.dismiss();
//                        popupWindow2 = null;
//                    }
//                    return false;
//                }
//            });
//            //SeekBar
//            seekBar1 = (SeekBar) customView.findViewById(R.id.seekBarSaturation);  //饱和度
//            seekBar2 = (SeekBar) customView.findViewById(R.id.seekBarHue);            //色相
//            seekBar3 = (SeekBar) customView.findViewById(R.id.seekBarLum);            //亮度
//             /*
//    	      * 设置Seekbar变化监听事件
//    	      * 注意:此时修改活动接口
//    	      * ProcessActivity extends Activity implements OnSeekBarChangeListener
//    	      */
//            seekBar1.setOnSeekBarChangeListener(this);
//            seekBar2.setOnSeekBarChangeListener(this);
//            seekBar3.setOnSeekBarChangeListener(this);
//            //自定义引用类
//            increaseProcess = new IncreaseProcessImage(bmp);
//        }
//    	/*
//    	 * number=3 效果
//    	 */
//        if(number==3) {
//            customView = getLayoutInflater().inflate(R.layout.popup_effect, null, false);
//            popupWindow3 = new PopupWindow(customView, 450, 150);
//            popupWindow3.setFocusable(true);
//            popupWindow3.setOutsideTouchable(true);
//            popupWindow3.setAnimationStyle(R.style.AnimationPreview);
//            // 自定义view添加触摸事件
//            customView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if (popupWindow3 != null && popupWindow3.isShowing()) {
//                        popupWindow3.dismiss();
//                        popupWindow3 = null;
//                    }
//                    return false;
//                }
//            });
//            //判断点击子菜单不同按钮实现不同功能
//            //自定义引用类
//            effectProcess = new EffectProcessImage(bmp);
//            LinearLayout layoutEffect1 = (LinearLayout) customView.findViewById(R.id.layout_effect_hj);
//            layoutEffect1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    textShow.setText("图像处理--怀旧效果");
//                    popupWindow3.dismiss();
//                    //调用EffectProcessImage.java中函数
//                    mbmp = effectProcess.OldRemeberImage(bmp);
//                    imageShow.setImageBitmap(mbmp);
//                }
//            });
//            LinearLayout layoutEffect2 = (LinearLayout) customView.findViewById(R.id.layout_effect_fd);
//            layoutEffect2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    textShow.setText("图像处理--浮雕效果");
//                    popupWindow3.dismiss();
//                    mbmp = effectProcess.ReliefImage(bmp);
//                    imageShow.setImageBitmap(mbmp);
//                }
//            });
//            LinearLayout layoutEffect3 = (LinearLayout) customView.findViewById(R.id.layout_effect_gz);
//            layoutEffect3.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    textShow.setText("图像处理--光照效果");
//                    popupWindow3.dismiss();
//                    mbmp = effectProcess.SunshineImage(bmp);
//                    imageShow.setImageBitmap(mbmp);
//                }
//            });
//            LinearLayout layoutEffect4 = (LinearLayout) customView.findViewById(R.id.layout_effect_sm);
//            layoutEffect4.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    textShow.setText("图像处理--素描效果");
//                    popupWindow3.dismiss();
//                    mbmp = effectProcess.SuMiaoImage(bmp);
//                    imageShow.setImageBitmap(mbmp);
//                }
//            });
//            LinearLayout layoutEffect5 = (LinearLayout) customView.findViewById(R.id.layout_effect_rh);
//            layoutEffect5.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    textShow.setText("图像处理--锐化效果");
//                    popupWindow3.dismiss();
//                    mbmp = effectProcess.SharpenImage(bmp);
//                    imageShow.setImageBitmap(mbmp);
//                }
//            });
//
//        }
//		/*
//		 * number=4 边框
//		 */
//        if(number==4) {
//            customView = getLayoutInflater().inflate(R.layout.popup_frame, null, false);
//            popupWindow4 = new PopupWindow(customView, 450, 150);
//            popupWindow4.setFocusable(true);
//            popupWindow4.setAnimationStyle(R.style.AnimationPreview);
//            // 自定义view添加触摸事件
//            customView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if (popupWindow4 != null && popupWindow4.isShowing()) {
//                        popupWindow4.dismiss();
//                        popupWindow4 = null;
//                    }
//                    return false;
//                }
//            });
//            //判断点击子菜单不同按钮实现不同功能
//            //自定义引用类
//            frameProcess = new FrameProcessImage(bmp);
//            LinearLayout layoutFrame3 = (LinearLayout) customView.findViewById(R.id.layout_frame3);
//            layoutFrame3.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    textShow.setText("图像处理--框架模式三");
//                    popupWindow4.dismiss();
//                    //获取相框 自定义函数getImageFromAssets 获取assets中资源
//                    Bitmap frameBitmap = getImageFromAssets("image_frame_big_3.png");
//                    //显示图像并增加相框
//                    mbmp = frameProcess.addFrameToImage(bmp,frameBitmap);
//                    imageShow.setImageBitmap(mbmp);
//                }
//            });
//            LinearLayout layoutFrame2 = (LinearLayout) customView.findViewById(R.id.layout_frame2);
//            layoutFrame2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    textShow.setText("图像处理--框架模式二");
//                    popupWindow4.dismiss();
//                    Bitmap frameBitmap = getImageFromAssets("image_frame_big_2.png");
//                    mbmp = frameProcess.addFrameToImage(bmp,frameBitmap);
//                    imageShow.setImageBitmap(mbmp);
//                }
//            });
//            LinearLayout layoutFrame1 = (LinearLayout) customView.findViewById(R.id.layout_frame1);
//            layoutFrame1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    textShow.setText("图像处理--框架模式一");
//                    popupWindow4.dismiss();
//                    Bitmap frameBitmap = getImageFromAssets("image_frame_big_1.png");
//                    mbmp = frameProcess.addFrameToImage(bmp,frameBitmap);
//                    imageShow.setImageBitmap(mbmp);
//                }
//            });
//            LinearLayout layoutFrame4 = (LinearLayout) customView.findViewById(R.id.layout_frame4);
//            layoutFrame4.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    textShow.setText("图像处理--圆角矩形");
//                    popupWindow4.dismiss();
//                    mbmp = frameProcess.RoundedCornerBitmap(bmp);
//                    imageShow.setImageBitmap(mbmp);
//                }
//            });
//            LinearLayout layoutFrame5 = (LinearLayout) customView.findViewById(R.id.layout_frame5);
//            layoutFrame5.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    textShow.setText("图像处理--圆形相框");
//                    popupWindow4.dismiss();
//                    mbmp = frameProcess.RoundedBitmap(bmp);
//                    imageShow.setImageBitmap(mbmp);
//                }
//            });
//        }
//        /*
//         * number=5 美白 -> 交互
//         */
//        if(number==5) {
//            customView = getLayoutInflater().inflate(R.layout.popup_person, null, false);
//            popupWindow5 = new PopupWindow(customView, 300, 150);
//            popupWindow5.setFocusable(true);
//            popupWindow5.setAnimationStyle(R.style.AnimationPreview);
//            // 自定义view添加触摸事件
//            customView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if (popupWindow5 != null && popupWindow5.isShowing()) {
//                        popupWindow5.dismiss();
//                        popupWindow5 = null;
//                    }
//                    return false;
//                }
//            });
//            //判断点击子菜单不同按钮实现不同功能
//            //自定义引用类
//            personProcess = new PersonProcessImage(bmp);
//            LinearLayout layoutPerson1 = (LinearLayout) customView.findViewById(R.id.layout_person1);
//            layoutPerson1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    textShow.setText("保存图像至SD卡");
//                    popupWindow5.dismiss();
//                    try {
//            			/*
//            			 * 注意：由于手机重启才能显示图片 所以定义广播刷新相册 其中saveBitmapToSD保存图片
//            			 */
//                        if(mbmp == null) { //防止出现mbmp空
//                            mbmp = bmp;
//                        }
//                        Uri uri = personProcess.saveBitmapToSD(mbmp);
//                        Intent intent  = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                        intent.setData(uri);
//                        sendBroadcast(intent);
//                        Toast.makeText(ProcessActivity.this, "图像保存成功", Toast.LENGTH_SHORT).show();
//                    } catch(Exception e) {
//                        e.printStackTrace();
//                        Toast.makeText(ProcessActivity.this, "图像保存失败", Toast.LENGTH_SHORT).show();
//                    }
//                    imageShow.setImageBitmap(mbmp);
//                }
//            });
//            LinearLayout layoutPerson2 = (LinearLayout) customView.findViewById(R.id.layout_person2);
//            layoutPerson2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    textShow.setText("取消处理操作--恢复原图");
//                    popupWindow5.dismiss();
//                    mbmp = bmp;
//                    imageShow.setImageBitmap(mbmp);
//                }
//            });
//            LinearLayout layoutPerson3 = (LinearLayout) customView.findViewById(R.id.layout_person3);
//            layoutPerson3.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    textShow.setText("上传图片至发布界面");
//                    popupWindow5.dismiss();
//                    try {
//                        if(mbmp == null) { //防止出现mbmp空
//                            mbmp = bmp;
//                        }
//                        //图像上传 先保存 后传递图片路径
//                        Uri uri = personProcess.loadBitmap(mbmp);
//                        Intent intent  = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                        intent.setData(uri);
//                        sendBroadcast(intent);
//                        //上传图片*
//                        Intent intentPut = new Intent(ProcessActivity.this, MainActivity.class);
//                        String pathImage = null;
//                        intentPut.putExtra("pathProcess", personProcess.pathPicture );
//	    				/*
//	    				 * 返回活动使用setResult 使用startActivity总是显示一张图片并RunTime
//	    				 * startActivity(intentPut);
//	    				 * 在onActivityResult中获取数据
//	    				 */
//                        setResult(RESULT_OK, intentPut);
//                        //返回上一界面
//                        Toast.makeText(ProcessActivity.this, "图片上传成功" , Toast.LENGTH_SHORT).show();
//                        ProcessActivity.this.finish();
//                    } catch(Exception e) {
//                        e.printStackTrace();
//                        Toast.makeText(ProcessActivity.this, "图像上传失败", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//
//        } //end if
//    }
//}
