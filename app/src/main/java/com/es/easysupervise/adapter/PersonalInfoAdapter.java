package com.es.easysupervise.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.es.easysupervise.R;

import java.util.ArrayList;

/**
 * Created by xiaoxi on 15-12-8.
 * 完善个人信息列表adapter
 */
public class PersonalInfoAdapter extends BaseAdapter
{
    private ArrayList listKV;
    private Context context;
    private Handler mHandler;
    LayoutInflater mInflater;
    private Bitmap headBitmap = null;

    public void setHeadBitmap(Bitmap headBitmap) {
        this.headBitmap = headBitmap;
    }

    public PersonalInfoAdapter(Context context,ArrayList list,Handler handler)
    {
        this.listKV = list;
        this.context = context;
        this.mHandler = handler;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ArrayList listTemp = (ArrayList)this.listKV.get(position);
        if(convertView == null)
            convertView = mInflater.inflate(R.layout.lvpersonalinfo_item, null);
        if(position == 0)
        {

            RelativeLayout rlZeroItem = (RelativeLayout)convertView.findViewById(R.id.rlZeroItem);
            rlZeroItem.setVisibility(View.GONE);
            RelativeLayout rlFirstItem = (RelativeLayout)convertView.findViewById(R.id.rlFirstItem);
            rlFirstItem.setVisibility(View.VISIBLE);
            TextView tvFirstKey = (TextView)convertView.findViewById(R.id.tvFirstKey);
            TextView tvOneKey = (TextView)convertView.findViewById(R.id.tvOneKey);
            TextView tvTwoKey = (TextView)convertView.findViewById(R.id.tvTwoKey);
            EditText edtOneValue = (EditText)convertView.findViewById(R.id.edtOneValue);
            EditText edtTwoValue = (EditText)convertView.findViewById(R.id.edtTwoValue);
            ImageView imgHead = (ImageView)convertView.findViewById(R.id.imgHead);
            tvFirstKey.setText("" + listTemp.get(0));
            if(headBitmap != null)
                imgHead.setImageBitmap(headBitmap);
            else
                imgHead.setImageResource(R.drawable.headdefault);
            tvOneKey.setText("" + listTemp.get(2));
            edtOneValue.setHint("" + listTemp.get(3));
            tvTwoKey.setText("" + listTemp.get(4));
            edtTwoValue.setHint("" + listTemp.get(5));
            imgHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHandler.sendEmptyMessage(0);
                }
            });
        }
        else
        {
            RelativeLayout rlFirstItem = (RelativeLayout)convertView.findViewById(R.id.rlFirstItem);
            rlFirstItem.setVisibility(View.GONE);
            RelativeLayout rlZeroItem = (RelativeLayout)convertView.findViewById(R.id.rlZeroItem);
            rlZeroItem.setVisibility(View.VISIBLE);
            TextView tvZeroKey = (TextView)convertView.findViewById(R.id.tvZeroKey);
            EditText edtZeroValue = (EditText)convertView.findViewById(R.id.edtZeroValue);
            TextView tvOneKey = (TextView)convertView.findViewById(R.id.tvOneKey);
            EditText edtOneValue = (EditText)convertView.findViewById(R.id.edtOneValue);
            TextView tvTwoKey = (TextView)convertView.findViewById(R.id.tvTwoKey);
            EditText edtTwoValue = (EditText)convertView.findViewById(R.id.edtTwoValue);
            tvZeroKey.setText("" + listTemp.get(0));
            edtZeroValue.setHint("" + listTemp.get(1));
            tvOneKey.setText("" + listTemp.get(2));
            edtOneValue.setHint("" + listTemp.get(3));
            tvTwoKey.setText("" + listTemp.get(4));
            edtTwoValue.setHint("" + listTemp.get(5));
        }
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

}


