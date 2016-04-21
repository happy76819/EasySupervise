package com.es.easysupervise.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.es.easysupervise.R;
import com.es.easysupervise.object.AdInfo;

import java.util.ArrayList;

/**
 * Created by xiaoxi on 15-12-16.
 */
public class HomeTaskListAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<AdInfo> listData;
//    private Handler handler;
    LayoutInflater mInflater;

    public HomeTaskListAdapter(Context context,ArrayList<AdInfo> list)
    {
        this.context = context;
//        this.handler = handler;
        this.listData = list;
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
            convertView = mInflater.inflate(R.layout.item_home_task,null);
        }
        ImageView imgAd = (ImageView)convertView.findViewById(R.id.imgAd);
        TextView tvID = (TextView)convertView.findViewById(R.id.tvID);
        TextView tvMediaType = (TextView)convertView.findViewById(R.id.tvMediaType);
        TextView tvArea = (TextView)convertView.findViewById(R.id.tvArea);
        TextView tvAddress = (TextView)convertView.findViewById(R.id.tvAddress);
        tvID.setText(listData.get(position).getMediaId());
        tvMediaType.setText(listData.get(position).getMediaType());
        tvArea.setText(listData.get(position).getAreaAttr());
        tvAddress.setText(listData.get(position).getMediaAddr());
        return convertView;
    }
}
