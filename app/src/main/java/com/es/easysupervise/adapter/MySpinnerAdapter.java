package com.es.easysupervise.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.es.easysupervise.object.KeyValue;
import com.es.easysupervise.R;

import java.util.ArrayList;

/**
 * Created by xiaoxi on 15-12-8.
 * 下拉菜单adapter
 */
public class MySpinnerAdapter extends BaseAdapter implements SpinnerAdapter
{
    private ArrayList<KeyValue> listKV;
    private Context context;
    private Handler mHandler;
    LayoutInflater mInflater;

    public MySpinnerAdapter(Context context, ArrayList list)
    {
        this.listKV = list;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listKV.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mInflater=LayoutInflater.from(context);
        convertView=mInflater.inflate(R.layout.spinner_style, null);
        if(convertView!=null) {
            TextView tvKey=(TextView)convertView.findViewById(R.id.tvKey);
            tvKey.setText("" + listKV.get(position).getValue());
        }
        return convertView;

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.spinner_dropdown_style,null);
        TextView tv = (TextView)convertView.findViewById(R.id.tvKey);
        tv.setText("" + listKV.get(position).getValue());
        return convertView;
    }
}


