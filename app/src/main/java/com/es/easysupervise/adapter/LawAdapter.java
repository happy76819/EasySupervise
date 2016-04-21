package com.es.easysupervise.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.es.easysupervise.R;
import com.es.easysupervise.object.LawObj;

import java.util.ArrayList;

/**
 * Created by xiaoxi on 15-12-21.
 */
public class LawAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<LawObj> listData;
    Handler handler;
    LayoutInflater mInflater;

    public LawAdapter(Context context, ArrayList<LawObj> listData) {
        this.context = context;
        this.listData = listData;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            convertView = mInflater.inflate(R.layout.item_law,null);
        }
        ImageView imageView = (ImageView)convertView.findViewById(R.id.imgAgreement);
        TextView tvCode = (TextView)convertView.findViewById(R.id.tvCode);
        TextView tvNotice = (TextView)convertView.findViewById(R.id.tvNotice);
        tvCode.setText(listData.get(position).getBianhao());
        tvNotice.setText(listData.get(position).getContent());
        return convertView;
    }
}
