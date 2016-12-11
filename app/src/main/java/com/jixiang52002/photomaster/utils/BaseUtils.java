package com.jixiang52002.photomaster.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by zn on 2016/12/11.
 * 基础操作的综合类
 */

public class BaseUtils {
    private static Context mContext;
    //初始化
    public static void init(Context context){
        mContext=context;
    }



    //普通打印
    public static void toast(String str){
        if(mContext==null){
            throw new RuntimeException("请先调用init()初始化");
        }else{
            Toast.makeText(mContext,str,Toast.LENGTH_SHORT).show();
        }

    }

    public static void toastLong(String str){
        if(mContext==null){
            throw new RuntimeException("请先调用init()初始化");
        }else{
            Toast.makeText(mContext,str,Toast.LENGTH_LONG).show();
        }
    }
}
