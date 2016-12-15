package com.jixiang52002.photomaster.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jixiang52002.photomaster.R;
import com.jixiang52002.photomaster.config.Constacts;
import com.jixiang52002.photomaster.utils.BaseUtils;
import com.jixiang52002.photomaster.utils.EffectProcessImage;
import com.jixiang52002.photomaster.utils.FrameProcessImage;
import com.jixiang52002.photomaster.utils.ImageUtils;
import com.jixiang52002.photomaster.utils.IncreaseProcessImage;
import com.jixiang52002.photomaster.utils.PersonProcessImage;
import com.jixiang52002.photomaster.utils.WatchProcessImage;

import java.io.File;
import java.io.IOException;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.PermissionUtils;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class BeautifyCapture extends Activity implements View.OnFocusChangeListener, SeekBar.OnSeekBarChangeListener {
    private TextView titleText;
    private ImageView imagerView;//待处理图片
    private LinearLayout watch;//观看效果
    private LinearLayout increase;//三原色效果增强
    private LinearLayout effect;//特效
    private LinearLayout frame;//相框
    private LinearLayout person;//个人中心

    private boolean isChanged = false;//图片是否有更改

    private String filePath;

    Bitmap bitmapFirst;//初始图像
    Bitmap bitmap;//正在处理的图像

    Bitmap bitmapAfter;//处理后的图像

    //弹出按钮
    private PopupWindow popWatch;
    private PopupWindow popIncrease;
    private PopupWindow popEffect;
    private PopupWindow popFrame;
    private PopupWindow popPerson;

    //图标
    private ImageView imageWatch;
    private ImageView imageIncrease;
    private ImageView imageEffect;
    private ImageView imageFrame;
    private ImageView imagePerson;

    //自定义引用图像处理类
    EffectProcessImage effectProcess = null;
    FrameProcessImage frameProcess = null;
    IncreaseProcessImage increaseProcess = null;
    WatchProcessImage watchProcess = null;
    PersonProcessImage personProcess = null;

    //触屏标志变量 1-缩放图片 2-画图
    private int flagOnTouch = 1;

    //分别为饱和度 色相 亮度
    private SeekBar seekBar1;
    private SeekBar seekBar2;
    private SeekBar seekBar3;
    //标记变量 Watch查看图片中
    private int flagWatch1 = 0;  //旋转次数,一次为45度 模360/45=8
    private int flagWatch2 = 0;  //水平翻转  =0第一次翻转 =1第二次翻转(原图)
    private int flagWatch3 = 0;  //垂直翻转  =0第一次翻转 =1第二次翻转(原图)

    private Canvas canvas; //画布
    private Paint paint; //画刷
    private float oldDist;
    private PointF startPoint = new PointF();
    private PointF middlePoint = new PointF();
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();

    //获取屏幕的宽高
    int width;
    int height;


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
        initSetting();
        //自定义函数 设置监听事件
        SetClickTouchListener();

    }

    //初始化界面
    private void initView() {
        titleText = (TextView) findViewById(R.id.textView1);
        imagerView = (ImageView) findViewById(R.id.imageView1);
        watch = (LinearLayout) findViewById(R.id.layout_watch);
        increase = (LinearLayout) findViewById(R.id.layout_increase);
        effect = (LinearLayout) findViewById(R.id.layout_effect);
        frame = (LinearLayout) findViewById(R.id.layout_frame);
        person = (LinearLayout) findViewById(R.id.layout_person);

        //图标
        imageWatch = (ImageView) findViewById(R.id.image_watch);
        imageIncrease = (ImageView) findViewById(R.id.image_increase);
        imageEffect = (ImageView) findViewById(R.id.image_effect);
        imageFrame = (ImageView) findViewById(R.id.image_frame);
        imagePerson = (ImageView) findViewById(R.id.image_person);
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
                finish();
            }
        });

        findViewById(R.id.view_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleText.setText("分享图片");
                if (bitmapAfter == null) {
                    bitmapAfter = bitmap;
                }
                BeautifyCapturePermissionsDispatcher.shareImageToOthersWithCheck(BeautifyCapture.this);

            }
        });
    }

    public void initSetting() {
        //获取当前屏幕的宽高
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();

        bitmapAfter = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmapAfter);  //画布
        paint = new Paint(); //画刷
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(5);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.DEFAULT_BOLD);  //无线粗体
        matrix = new Matrix();
        canvas.drawBitmap(bitmap, matrix, paint);
    }


    /*
     * 函数功能 设置监听事件
	 * 触摸监听事件 点击监听事件
	 */
    private void SetClickTouchListener() {
        /*
         * 按钮一 监听事件 查看图片
		 */
        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ProcessActivity.this, "点击按钮1", Toast.LENGTH_SHORT).show();
                //载入PopupWindow
                if (popWatch != null && popWatch.isShowing()) {
                    popWatch.dismiss();
                    return;
                } else {
                    initmPopupWindowView(1);   //当number=1时查看图片
                    int[] location = new int[2];
                    effect.getLocationOnScreen(location);
                    popWatch.showAtLocation(effect, Gravity.NO_GRAVITY, location[0], location[1] - popWatch.getHeight());
                }
            }
        });
        watch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //按下背景图片
                    watch.setBackgroundResource(R.drawable.image_home_layout_bg);
                    increase.setBackgroundResource(R.drawable.bottom_color_selector);
                    effect.setBackgroundResource(R.drawable.bottom_color_selector);
                    frame.setBackgroundResource(R.drawable.bottom_color_selector);
                    person.setBackgroundResource(R.drawable.bottom_color_selector);
                    //设置按钮图片
                    imageWatch.setImageDrawable(getResources().getDrawable(R.drawable.image_icon_watch_sel));
                    imageIncrease.setImageDrawable(getResources().getDrawable(R.drawable.image_icon_increase_nor));
                    imageEffect.setImageDrawable(getResources().getDrawable(R.drawable.image_icon_effect_nor));
                    imageFrame.setImageDrawable(getResources().getDrawable(R.drawable.image_icon_frame_nor));
                    imagePerson.setImageDrawable(getResources().getDrawable(R.drawable.image_icon_person_nor));
                }
                return false;
            }
        });
		/*
		 * 按钮二 监听事件增强图片
		 */
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //载入PopupWindow
                if (popIncrease != null && popIncrease.isShowing()) {
                    popIncrease.dismiss();
                    return;
                } else {
                    initmPopupWindowView(2);   //number=2
                    int[] location = new int[2];
                    effect.getLocationOnScreen(location);
                    popIncrease.showAtLocation(effect, Gravity.NO_GRAVITY, location[0], location[1] - popIncrease.getHeight());
                }
            }
        });
        increase.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //按下背景图片
                    watch.setBackgroundResource(R.drawable.bottom_color_selector);
                    increase.setBackgroundResource(R.drawable.image_home_layout_bg);
                    effect.setBackgroundResource(R.drawable.bottom_color_selector);
                    frame.setBackgroundResource(R.drawable.bottom_color_selector);
                    person.setBackgroundResource(R.drawable.bottom_color_selector);
                    //设置按钮图片
                    imageWatch.setImageDrawable(getResources().getDrawable(R.drawable.image_icon_watch_nor));
                    imageIncrease.setImageDrawable(getResources().getDrawable(R.drawable.image_icon_increase_sel));
                    imageEffect.setImageDrawable(getResources().getDrawable(R.drawable.image_icon_effect_nor));
                    imageFrame.setImageDrawable(getResources().getDrawable(R.drawable.image_icon_frame_nor));
                    imagePerson.setImageDrawable(getResources().getDrawable(R.drawable.image_icon_person_nor));
                }
                return false;
            }
        });
        /*
         * 按钮三 监听事件图片特效
         */
        effect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //载入PopupWindow
                if (popEffect != null && popEffect.isShowing()) {
                    popEffect.dismiss();
                    return;
                } else {
                    initmPopupWindowView(3);   //number=3
                    int[] location = new int[2];
                    effect.getLocationOnScreen(location);
                    popEffect.showAtLocation(effect, Gravity.NO_GRAVITY, location[0], location[1] - popEffect.getHeight());
                }
            }
        });
        effect.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //按下背景图片
                    watch.setBackgroundResource(R.drawable.bottom_color_selector);
                    increase.setBackgroundResource(R.drawable.bottom_color_selector);
                    effect.setBackgroundResource(R.drawable.image_home_layout_bg);
                    frame.setBackgroundResource(R.drawable.bottom_color_selector);
                    person.setBackgroundResource(R.drawable.bottom_color_selector);
                    //图标
                    imageWatch.setImageDrawable(getResources().getDrawable(R.drawable.image_icon_watch_nor));
                    imageIncrease.setImageDrawable(getResources().getDrawable(R.drawable.image_icon_increase_nor));
                    imageEffect.setImageDrawable(getResources().getDrawable(R.drawable.image_icon_effect_sel));
                    imageFrame.setImageDrawable(getResources().getDrawable(R.drawable.image_icon_frame_nor));
                    imagePerson.setImageDrawable(getResources().getDrawable(R.drawable.image_icon_person_nor));
                }
                return false;
            }
        });
		/*
		 * 按钮四 监听事件图片相框
		 */
        frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //载入PopupWindow
                if (popFrame != null && popFrame.isShowing()) {
                    popFrame.dismiss();
                    return;
                } else {
                    initmPopupWindowView(4);   //number=4
                    int[] location = new int[2];
                    v.getLocationOnScreen(location);
                    popFrame.showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1] - popFrame.getHeight());
                }
            }
        });
        frame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //按下背景图片
                    watch.setBackgroundResource(R.drawable.bottom_color_selector);
                    increase.setBackgroundResource(R.drawable.bottom_color_selector);
                    effect.setBackgroundResource(R.drawable.bottom_color_selector);
                    frame.setBackgroundResource(R.drawable.image_home_layout_bg);
                    person.setBackgroundResource(R.drawable.bottom_color_selector);
                    //图标
                    imageWatch.setImageDrawable(getResources().getDrawable(R.drawable.image_icon_watch_nor));
                    imageIncrease.setImageDrawable(getResources().getDrawable(R.drawable.image_icon_increase_nor));
                    imageEffect.setImageDrawable(getResources().getDrawable(R.drawable.image_icon_effect_nor));
                    imageFrame.setImageDrawable(getResources().getDrawable(R.drawable.image_icon_frame_sel));
                    imagePerson.setImageDrawable(getResources().getDrawable(R.drawable.image_icon_person_nor));
                }
                return false;
            }
        });
        /*
         * 按钮五 监听事件图片美白
         */
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //载入PopupWindow
                if (popPerson != null && popPerson.isShowing()) {
                    popPerson.dismiss();
                    return;
                } else {
                    initmPopupWindowView(5);   //number=5
                    int[] location = new int[2];
                    effect.getLocationOnScreen(location);
                    popPerson.showAtLocation(effect, Gravity.NO_GRAVITY, location[0], location[1] - popPerson.getHeight());
                }
            }
        });
        person.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //按下背景图片
                    watch.setBackgroundResource(R.drawable.bottom_color_selector);
                    increase.setBackgroundResource(R.drawable.bottom_color_selector);
                    effect.setBackgroundResource(R.drawable.bottom_color_selector);
                    frame.setBackgroundResource(R.drawable.bottom_color_selector);
                    person.setBackgroundResource(R.drawable.image_home_layout_bg);
                    //图标
                    imageWatch.setImageDrawable(getResources().getDrawable(R.drawable.image_icon_watch_nor));
                    imageIncrease.setImageDrawable(getResources().getDrawable(R.drawable.image_icon_increase_nor));
                    imageEffect.setImageDrawable(getResources().getDrawable(R.drawable.image_icon_effect_nor));
                    imageFrame.setImageDrawable(getResources().getDrawable(R.drawable.image_icon_frame_nor));
                    imagePerson.setImageDrawable(getResources().getDrawable(R.drawable.image_icon_person_sel));
                }
                return false;
            }
        });//结束监听5个事件
    }

    /*
	 * 函数功能 PopupWindow窗体动画
	 * 获取自定义布局文件
	 */
    public void initmPopupWindowView(int number) {
        View customView = null;
        //触屏标记默认为0 否则点一次"缩放"总能移动
        flagOnTouch = 0;
      	/*
    	 * number=1 查看
    	 */
        if (number == 1) {
            customView = getLayoutInflater().inflate(R.layout.popup_watch, null, false);
            // 创建PopupWindow实例  (250,180)分别是宽度和高度
            popWatch = new PopupWindow(customView, width, height / 5);
            // 使其聚集 要想监听菜单里控件的事件就必须要调用此方法
            popWatch.setFocusable(true);
            // 设置动画效果 [R.style.AnimationFade 是自己事先定义好的]
            popWatch.setOutsideTouchable(true);
            popWatch.setAnimationStyle(R.style.AnimationPreview);
            // 自定义view添加触摸事件
            customView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (popWatch != null && popWatch.isShowing()) {
                        popWatch.dismiss();
                        popWatch = null;
                    }
                    return false;
                }
            });
            //判断点击子菜单不同按钮实现不同功能
            //自定义引用类
            watchProcess = new WatchProcessImage(bitmap);
            LinearLayout layoutWatch2 = (LinearLayout) customView.findViewById(R.id.layout_watch2);
            layoutWatch2.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    titleText.setText("图像处理--水平翻转");
                    popWatch.dismiss();
                    //调用WatchProcessImage中函数实现水平翻转
                    bitmapAfter = watchProcess.FlipHorizontalImage(bitmap, flagWatch2);
                    imagerView.setImageBitmap(bitmapAfter);
                    //标记变量 0翻转 1变回原图
                    if (flagWatch2 == 0) {
                        flagWatch2 = 1;
                    } else if (flagWatch2 == 1) {
                        flagWatch2 = 0;
                    }
                }
            });
            LinearLayout layoutWatch3 = (LinearLayout) customView.findViewById(R.id.layout_watch3);
            layoutWatch3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    titleText.setText("图像处理--垂直翻转");
                    popWatch.dismiss();
                    bitmapAfter = watchProcess.FlipVerticalImage(bitmap, flagWatch3);
                    imagerView.setImageBitmap(bitmapAfter);
                    //标记变量 0翻转 1变回原图
                    if (flagWatch3 == 0) {
                        flagWatch3 = 1;
                    } else if (flagWatch3 == 1) {
                        flagWatch3 = 0;
                    }
                }
            });
            LinearLayout layoutWatch1 = (LinearLayout) customView.findViewById(R.id.layout_watch1);
            layoutWatch1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    titleText.setText("图像处理--旋转图片");
                    popWatch.dismiss();
                    //旋转一次表示增加45度 模8表示360度=0度
                    flagWatch1 = (flagWatch1 + 1) % 8;
                    //设置背景颜色黑色
                    //imageShow.setBackgroundColor(Color.parseColor("#000000"));
                    bitmapAfter = watchProcess.TurnImage(bitmap, flagWatch1);
                    imagerView.setImageBitmap(bitmapAfter);
                }
            });
            LinearLayout layoutWatch4 = (LinearLayout) customView.findViewById(R.id.layout_watch4);
            layoutWatch4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    titleText.setText("图像处理--移动缩放");
                    popWatch.dismiss();
                    flagOnTouch = 1; //标志变量
                    //动态设置android:scaleType="matrix"
                    imagerView.setScaleType(ImageView.ScaleType.MATRIX);
                    imagerView.setImageBitmap(bitmap);
                }
            });
            LinearLayout layoutWatch5 = (LinearLayout) customView.findViewById(R.id.layout_watch5);
            layoutWatch5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    titleText.setText("图像处理--绘制图片");
                    popWatch.dismiss();
                    flagOnTouch = 2; //标志变量
                    //动态设置android:scaleType="matrix"
                    imagerView.setScaleType(ImageView.ScaleType.MATRIX);
                    //画图 图片移动至(0,0) 否则绘图线与手指存在误差
                    matrix = new Matrix();
                    matrix.postTranslate(0, 0);
                    imagerView.setImageMatrix(matrix);
                    canvas.drawBitmap(bitmap, matrix, paint);
                    imagerView.setImageBitmap(bitmapAfter); //备份图片
                }
            });
        }
    	/*
    	 * number=2 增强
    	 */
        if (number == 2) {
            customView = getLayoutInflater().inflate(R.layout.popup_increase, null, false);
            //设置子窗体PopupWindow高度500 饱和度 色相 亮度
            popIncrease = new PopupWindow(customView, width, height / 5);
            // 使其聚集 要想监听菜单里控件的事件就必须要调用此方法
            popIncrease.setFocusable(true);
            // 设置允许在外点击消失
            popIncrease.setOutsideTouchable(true);
            popIncrease.setAnimationStyle(R.style.AnimationPreview);
            // 自定义view添加触摸事件
            customView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (popIncrease != null && popIncrease.isShowing()) {
                        popIncrease.dismiss();
                        popIncrease = null;
                    }
                    return false;
                }
            });
            //SeekBar
            seekBar1 = (SeekBar) customView.findViewById(R.id.seekBarSaturation);  //饱和度
            seekBar2 = (SeekBar) customView.findViewById(R.id.seekBarHue);            //色相
            seekBar3 = (SeekBar) customView.findViewById(R.id.seekBarLum);            //亮度
             /*
    	      * 设置Seekbar变化监听事件
    	      * 注意:此时修改活动接口
    	      * ProcessActivity extends Activity implements OnSeekBarChangeListener
    	      */
            seekBar1.setOnSeekBarChangeListener(this);
            seekBar2.setOnSeekBarChangeListener(this);
            seekBar3.setOnSeekBarChangeListener(this);
            //自定义引用类
            increaseProcess = new IncreaseProcessImage(bitmap);
        }
    	/*
    	 * number=3 效果
    	 */
        if (number == 3) {
            customView = getLayoutInflater().inflate(R.layout.popup_effect, null, false);
            popEffect = new PopupWindow(customView, width, height / 5);
            popEffect.setFocusable(true);
            popEffect.setOutsideTouchable(true);
            popEffect.setAnimationStyle(R.style.AnimationPreview);
            // 自定义view添加触摸事件
            customView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (popEffect != null && popEffect.isShowing()) {
                        popEffect.dismiss();
                        popEffect = null;
                    }
                    return false;
                }
            });
            //判断点击子菜单不同按钮实现不同功能
            //自定义引用类
            effectProcess = new EffectProcessImage(bitmap);
            LinearLayout layoutEffect1 = (LinearLayout) customView.findViewById(R.id.layout_effect_hj);
            layoutEffect1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    titleText.setText("图像处理--怀旧效果");
                    popEffect.dismiss();
                    //调用EffectProcessImage.java中函数
                    bitmapAfter = effectProcess.OldRemeberImage(bitmap);
                    imagerView.setImageBitmap(bitmapAfter);
                }
            });
            LinearLayout layoutEffect2 = (LinearLayout) customView.findViewById(R.id.layout_effect_fd);
            layoutEffect2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    titleText.setText("图像处理--浮雕效果");
                    popEffect.dismiss();
                    bitmapAfter = effectProcess.ReliefImage(bitmap);
                }
            });
            LinearLayout layoutEffect3 = (LinearLayout) customView.findViewById(R.id.layout_effect_gz);
            layoutEffect3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    titleText.setText("图像处理--光照效果");
                    popEffect.dismiss();
                    bitmapAfter = effectProcess.SunshineImage(bitmap);
                }
            });
            LinearLayout layoutEffect4 = (LinearLayout) customView.findViewById(R.id.layout_effect_sm);
            layoutEffect4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    titleText.setText("图像处理--素描效果");
                    popEffect.dismiss();
                    bitmapAfter = effectProcess.SuMiaoImage(bitmap);
                }
            });
            LinearLayout layoutEffect5 = (LinearLayout) customView.findViewById(R.id.layout_effect_rh);
            layoutEffect5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    titleText.setText("图像处理--锐化效果");
                    popEffect.dismiss();
                    bitmapAfter = effectProcess.SharpenImage(bitmap);
                    imagerView.setImageBitmap(bitmapAfter);
                }
            });

        }
		/*
		 * number=4 边框
		 */
        if (number == 4) {
            customView = getLayoutInflater().inflate(R.layout.popup_frame, null, false);
            popFrame = new PopupWindow(customView, width, height / 5);
            popFrame.setFocusable(true);
            popFrame.setAnimationStyle(R.style.AnimationPreview);
            // 自定义view添加触摸事件
            customView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (popFrame != null && popFrame.isShowing()) {
                        popFrame.dismiss();
                        popFrame = null;
                    }
                    return false;
                }
            });
            //判断点击子菜单不同按钮实现不同功能
            //自定义引用类
            frameProcess = new FrameProcessImage(bitmap);
            LinearLayout layoutFrame3 = (LinearLayout) customView.findViewById(R.id.layout_frame3);
            layoutFrame3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    titleText.setText("图像处理--框架模式三");
                    popFrame.dismiss();
                    //获取相框 自定义函数getImageFromAssets 获取assets中资源
                    Bitmap frameBitmap = ImageUtils.getImageFromAssets(getApplicationContext(), "image_frame_big_3.png");
                    //显示图像并增加相框
                    bitmapAfter = frameProcess.addFrameToImage(bitmap, frameBitmap);
                    imagerView.setImageBitmap(bitmapAfter);
                }
            });
            LinearLayout layoutFrame2 = (LinearLayout) customView.findViewById(R.id.layout_frame2);
            layoutFrame2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    titleText.setText("图像处理--框架模式二");
                    popFrame.dismiss();
                    Bitmap frameBitmap = ImageUtils.getImageFromAssets(getApplicationContext(), "image_frame_big_2.png");
                    //显示图像并增加相框
                    bitmapAfter = frameProcess.addFrameToImage(bitmap, frameBitmap);
                    imagerView.setImageBitmap(bitmapAfter);
                }
            });
            LinearLayout layoutFrame1 = (LinearLayout) customView.findViewById(R.id.layout_frame1);
            layoutFrame1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    titleText.setText("图像处理--框架模式一");
                    popFrame.dismiss();
                    Bitmap frameBitmap = ImageUtils.getImageFromAssets(getApplicationContext(), "image_frame_big_1.png");
                    //显示图像并增加相框
                    bitmapAfter = frameProcess.addFrameToImage(bitmap, frameBitmap);
                    imagerView.setImageBitmap(bitmapAfter);
                }
            });
            LinearLayout layoutFrame4 = (LinearLayout) customView.findViewById(R.id.layout_frame4);
            layoutFrame4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    titleText.setText("图像处理--圆角矩形");
                    popFrame.dismiss();
                    bitmapAfter = frameProcess.RoundedCornerBitmap(bitmap);
                    imagerView.setImageBitmap(bitmapAfter);
                }
            });
            LinearLayout layoutFrame5 = (LinearLayout) customView.findViewById(R.id.layout_frame5);
            layoutFrame5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    titleText.setText("图像处理--圆形相框");
                    popFrame.dismiss();
                    bitmapAfter = frameProcess.RoundedBitmap(bitmap);
                    imagerView.setImageBitmap(bitmapAfter);
                }
            });
        }
        /*
         * number=5 美白 -> 交互
         */
        if (number == 5) {
            customView = getLayoutInflater().inflate(R.layout.popup_person, null, false);
            popPerson = new PopupWindow(customView, width, height / 5);
            popPerson.setFocusable(true);
            popPerson.setAnimationStyle(R.style.AnimationPreview);
            // 自定义view添加触摸事件
            customView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (popPerson != null && popPerson.isShowing()) {
                        popPerson.dismiss();
                        popPerson = null;
                    }
                    return false;
                }
            });
            //判断点击子菜单不同按钮实现不同功能
            //自定义引用类
            personProcess = new PersonProcessImage(bitmap);
            LinearLayout layoutPerson1 = (LinearLayout) customView.findViewById(R.id.layout_person1);
            layoutPerson1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    titleText.setText("保存图像至SD卡");
                    popPerson.dismiss();
                    try {
            			/*
            			 * 注意：由于手机重启才能显示图片 所以定义广播刷新相册 其中saveBitmapToSD保存图片
            			 */
                        if (bitmapAfter == null) { //防止出现mbmp空
                            bitmapAfter = bitmapFirst;
                        }
                        BeautifyCapturePermissionsDispatcher.writeImageIntoSdcardWithCheck(BeautifyCapture.this);
//                        Uri uri = personProcess.saveBitmapToSD(bitmapAfter);
//                        Uri uri=BeautifyCapturePermissionsDispatcher.writeImageIntoSdcardWithCheck(this);
//                        Intent intent  = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                        intent.setData(uri);
//                        sendBroadcast(intent);
//                        BaseUtils.Toast("图像保存成功");
                    } catch (Exception e) {
                        e.printStackTrace();
                        BaseUtils.Toast("图像保存失败");
                    }
                    imagerView.setImageBitmap(bitmap);
                }
            });
            LinearLayout layoutPerson2 = (LinearLayout) customView.findViewById(R.id.layout_person2);
            layoutPerson2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    titleText.setText("取消处理操作--恢复原图");
                    popPerson.dismiss();
                    bitmapAfter = null;
                    imagerView.setImageBitmap(bitmap);
                }
            });
//            LinearLayout layoutPerson3 = (LinearLayout) customView.findViewById(R.id.layout_person3);
//            layoutPerson3.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    titleText.setText("上传图片至发布界面");
//                    popPerson.dismiss();
//                    try {
//                        if(bitmap == null) { //防止出现mbmp空
//                            bitmap = bitmapFirst;
//                        }
//                        //图像上传 先保存 后传递图片路径
//                        Uri uri = personProcess.loadBitmap(bitmap);
//                        Intent intent  = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                        intent.setData(uri);
//                        sendBroadcast(intent);
////                        //上传图片*
////                        Intent intentPut = new Intent(ProcessActivity.this, MainActivity.class);
////                        String pathImage = null;
////                        intentPut.putExtra("pathProcess", personProcess.pathPicture );
////	    				/*
////	    				 * 返回活动使用setResult 使用startActivity总是显示一张图片并RunTime
////	    				 * startActivity(intentPut);
////	    				 * 在onActivityResult中获取数据
////	    				 */
////                        setResult(RESULT_OK, intentPut);
////                        //返回上一界面
////                        Toast.makeText(ProcessActivity.this, "图片上传成功" , Toast.LENGTH_SHORT).show();
////                        ProcessActivity.this.finish();
//                    } catch(Exception e) {
//                        e.printStackTrace();
////                        Toast.makeText(ProcessActivity.this, "图像上传失败", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//
        } //end if
    }

            /**
             * 初始化图像
             */
            @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            public void initImage () {
                //获取从其他intent中传过来的数据
                Intent intent = getIntent();
                String filePath = intent.getStringExtra(Constacts.CAPTURE_FILES);
                bitmap = ImageUtils.getBitmapFromFile(new File(filePath), 480, 320);
                bitmapFirst = bitmap;
                imagerView.setImageBitmap(bitmap);

            }

            @Override
            public void onRequestPermissionsResult ( int requestCode, String[] permissions,
            int[] grantResults){
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                BeautifyCapturePermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
            }

            @Override
            public void onFocusChange (View v,boolean hasFocus){

            }


            @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE})
            void cancel () {
                BaseUtils.Toast("无法读取外部内存卡数据，请设置通过权限");
            }

            @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE})
            void never () {
                BaseUtils.Toast("永远无法读取外部内存卡数据，请设置通过权限");
            }

    /*
	 * 设置SeekBar监听事件
	 * 但是点击popIncrease才弹出该界面 会有影响吗?
	 */
        public void onProgressChanged (SeekBar seekBar,int progress, boolean fromTouch)
        {
            int flag = 0;
            switch (seekBar.getId()) {
                case R.id.seekBarSaturation: //饱和度
                    titleText.setText("图像增强--饱和度" + progress);
                    flag = 0;
                    increaseProcess.setSaturation(progress);
                    break;
                case R.id.seekBarHue: //色相
                    titleText.setText("图像增强--色相" + progress);
                    flag = 1;
                    increaseProcess.SetHue(progress);
                    break;
                case R.id.seekBarLum: //亮度
                    titleText.setText("图像增强--亮度" + progress);
                    flag = 2;
                    increaseProcess.SetLum(progress);
                    break;
            }
            bitmapAfter = increaseProcess.IncreaseProcessing(bitmap, flag);
            imagerView.setImageBitmap(bitmapAfter);
        }
        //SeekBar 开始拖动 否则ProcessActivity报错
        public void onStartTrackingTouch (SeekBar seekBar)
        {

        }
        //SeekBar 停止拖动
        public void onStopTrackingTouch (SeekBar seekBar)
        {

        }

        @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        void writeImageIntoSdcard () {
            Uri uri = null;
            try {
                uri = personProcess.saveBitmapToSD(bitmapAfter);
                Intent intent  = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(uri);
                sendBroadcast(intent);
                BaseUtils.Toast("图像保存成功");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    /**
     * 打开分享界面
     */
    private void showShare () {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
//关闭sso授权
        oks.disableSSOWhenAuthorize();

// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle(getString(R.string.app_name));
// titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://img0.imgtn.bdimg.com/it/u=1398349228,4122099846&fm=21&gp=0.jpg");
// text是分享文本，所有平台都需要这个字段
        oks.setText("请看我用拍照大师p的美图");
// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
// url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
// comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
// site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
// siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
/** imagePath是本地的图片路径，除Linked-In外的所有平台都支持这个字段 */
        oks.setImagePath(filePath);

// 启动分享GUI
        oks.show(this);
    }




        @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        void shareImageToOthers () {
            //图像上传 先保存 后传递图片路径
            Uri uri = null;
            try {
                if(personProcess==null){
                    personProcess = new PersonProcessImage(bitmapAfter);
                }
                uri = personProcess.loadBitmap(bitmapAfter);
                showShare();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        void whyWrite ( final PermissionRequest request){
            BaseUtils.Toast("保存图片需要读取外部存储的权限");
        }

        @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        void cancelWrite () {
            BaseUtils.Toast("您已经取消读取外部存储的权限，无法保存图片");
        }

        @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        void neverWrite () {
            BaseUtils.Toast("无法获取外部存储的权限，请打开权限");
        }
    }
