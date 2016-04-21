package com.es.easysupervise.adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.es.easysupervise.R;
import com.es.easysupervise.object.MyCollect;
import com.es.easysupervise.uil.OkHttpClientManager;

import java.util.ArrayList;

/**
 * Created by xiaoxi on 15-12-16.
 */
public class MyCollectAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<MyCollect> listData;
    Handler handler;
    LayoutInflater mInflater;
    String[] imgurl ={"http://pic2.ooopic.com/01/03/51/25b1OOOPIC19.jpg",
    "http://img2.3lian.com/img2007/19/33/005.jpg",
            "http://down.tutu001.com/d/file/20101129/2f5ca0f1c9b6d02ea87df74fcc_560.jpg",
    "http://pic.nipic.com/2007-11-09/200711912453162_2.jpg",
    "http://baike.soso.com/p/20090711/20090711101754-314944703.jpg",
            "http://pic2.ooopic.com/01/03/51/25b1OOOPIC19.jpg",
            "http://img2.3lian.com/img2007/19/33/005.jpg",
            "http://down.tutu001.com/d/file/20101129/2f5ca0f1c9b6d02ea87df74fcc_560.jpg",
            "http://pic.nipic.com/2007-11-09/200711912453162_2.jpg",
            "http://baike.soso.com/p/20090711/20090711101754-314944703.jpg"};

    public MyCollectAdapter(Context context,ArrayList<MyCollect> list,Handler handler)
    {
        this.context = context;
        this.listData = list;
        mInflater = LayoutInflater.from(context);
        this.handler = handler;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null)
        {
            Log.e("Position = ", "" + position);
            convertView = mInflater.inflate(R.layout.item_my_collect, null);

        }
        String strurl;
        ImageView imgAd = (ImageView)convertView.findViewById(R.id.imgAd);
        imgAd.setImageResource(R.drawable.ad_pic_default);
        try
        {
            OkHttpClientManager.displayImage(imgAd,imgurl[position],R.drawable.ad_pic_default);
        }
        catch (Exception e)
        {
            Log.e("error download image","error error");
        }
        TextView tvAddress = (TextView)convertView.findViewById(R.id.tvAddress);
        TextView tvPrice = (TextView)convertView.findViewById(R.id.tvPrice);
        TextView btnCancelCollect = (TextView)convertView.findViewById(R.id.btnCancelCollect);
        tvAddress.setText(listData.get(position).getAddress());
        tvPrice.setText(listData.get(position).getPrice());
        btnCancelCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(position);
            }
        });
        return convertView;
    }
}
