package com.jixiang52002.photomaster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jixiang52002.photomaster.R;

import java.util.List;

/**
 * Created by jixiang52002 on 2016/10/8.
 */
public class FunctionAdapter extends BaseAdapter {
    //引用Adapter的内容
    private Context mContext;
    //功能名字
    private List<FunctionItem> mList;
    //layout的导入类，用于导入activity以外的布局
    private LayoutInflater inflater;
    public FunctionAdapter(Context context, List<FunctionItem> list){
        mContext=context;
        mList=list;
        inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return mList==null?0:mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //构筑单个item的view
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.funtion_item,null);
            holder.textView= (TextView) convertView.findViewById(R.id.text1);
            holder.item= (LinearLayout) convertView.findViewById(R.id.item);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();

        }
        //绑定数据
        if(position<mList.size()){
            holder.textView.setText(mList.get(position).text);
            holder.item.setBackgroundColor(mList.get(position).color);
        }
        return convertView;
    }

    //每个item的view的类
    class ViewHolder{
        TextView textView;
        LinearLayout item;
    }
}
