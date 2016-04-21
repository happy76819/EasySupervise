package com.es.easysupervise.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.es.easysupervise.R;
import com.es.easysupervise.object.HomeTask;
import com.es.easysupervise.uil.OkHttpClientManager;
import com.es.easysupervise.uil.Util;
import com.squareup.okhttp.Request;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

@EActivity(R.layout.activity_task_list)
public class TaskListActivity extends AppCompatActivity implements
        OkHttpClientManager.StringCallback,AdapterView.OnItemClickListener

{

    @ViewById
    TextView tvTitle;
    @ViewById
    ListView lvData;
    @ViewById
    TextView tvNoData;

    int TYPE;
    Dialog loading;
    String userid;
    String longitude;
    String latitude;
    TaskListViewAdapter taskListViewAdapter;
    ArrayList<HomeTask> listTask = new ArrayList<>();

    @AfterViews
    void init()
    {
        Intent intent = getIntent();
        TYPE = intent.getIntExtra("TYPE", 0);
        longitude = intent.getStringExtra("lng");
        latitude = intent.getStringExtra("lat");
        SharedPreferences sharedPreferences = getSharedPreferences(Util.SHAREDPREFERENCENAME,MODE_PRIVATE);
        userid = sharedPreferences.getString(Util.USERID, "");

        loading = Util.createLoadingDialog(this, "正在加载...");
        taskListViewAdapter = new TaskListViewAdapter(this,listTask);
        lvData.setAdapter(taskListViewAdapter);
        lvData.setOnItemClickListener(this);
        if(TYPE != 0)
        {
            String url;

            loading.show();
            switch (TYPE)
            {
                case Util.YICAIJI:
                    tvTitle.setText("已采集");
                    lvData.setVisibility(View.VISIBLE);
                    tvNoData.setVisibility(View.GONE);
                    url = Util.MAINURL + Util.doYICAIJI + "USERID=" + userid;
                    OkHttpClientManager.getAsyn(url,this,Util.GETYICAIJI);
                    break;
                case Util.WEICAIJI:
                    tvTitle.setText("未采集");
                    lvData.setVisibility(View.GONE);
                    tvNoData.setVisibility(View.VISIBLE);
                    loading.dismiss();
                    break;
                case Util.YIJIANCE:
                    tvTitle.setText("已监测");
                    lvData.setVisibility(View.VISIBLE);
                    tvNoData.setVisibility(View.GONE);
                    url = Util.MAINURL + Util.doYIJIANCE + "USERID=" + userid +
                                "&jingdu=" + longitude + "&weidu=" + latitude;
                    OkHttpClientManager.getAsyn(url, this, Util.GETYIJIANCE);
                    break;
                case Util.WEIJIANCE:
                    tvTitle.setText("未监测");
                    lvData.setVisibility(View.VISIBLE);
                    tvNoData.setVisibility(View.GONE);
                    url = Util.MAINURL + Util.doWEIJIANCE + "USERID=" + userid +
                            "&jingdu=" + longitude + "&weidu=" + latitude;
                    Log.e("url ======= ",url);
                    OkHttpClientManager.getAsyn(url, this, Util.GETWEIJIANCE);
                    break;
                case Util.YISHENHE:
                    tvTitle.setText("已审核");
                    lvData.setVisibility(View.VISIBLE);
                    tvNoData.setVisibility(View.GONE);
                    url = Util.MAINURL + Util.doYISHENHE + "USERID=" + userid;
                    OkHttpClientManager.getAsyn(url, this, Util.GETYISHENHE);
                    break;
                case Util.WEISHENHE:
                    tvTitle.setText("未审核");
                    lvData.setVisibility(View.VISIBLE);
                    tvNoData.setVisibility(View.GONE);
                    url = Util.MAINURL + Util.doWEISHENHE + "USERID=" + userid;
                    OkHttpClientManager.getAsyn(url, this, Util.GETWEISHENHE);
                    break;
            }

        }
        else
        {
            Util.toastShotShow(this,"系统错误");
            this.finish();
        }
    }

    @Click
    void imgLeft()
    {
        finish();
    }

    @Override
    public void onFailure(Request request, IOException e) {
        loading.dismiss();
        Log.e("exception",e.toString());
        Util.toastShotShow(TaskListActivity.this,"网络连接超时");
    }

    @Override
    public void onResponse(String response, int type) {
        loading.dismiss();
        try
        {
            JSONObject jsonObject = new JSONObject(response);
            String reason = jsonObject.getString("reason");
            String rescode = jsonObject.getString("rescode");
            if(rescode.equals("1"))
            {
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                listTask.clear();
                if(type == Util.GETYICAIJI || type == Util.GETYIJIANCE || type == Util.GETYISHENHE)
                {
                    for(int i = 0;i < jsonArray.length();i++)
                    {
                        JSONObject temp = (JSONObject)jsonArray.get(i);
                        HomeTask homeTask = new HomeTask();
                        homeTask.setAdid(temp.getString("ID"));
                        homeTask.setAdbh(temp.getString("MTBH"));
                        homeTask.setAdtype(temp.getString("MTXS"));
                        homeTask.setAreaType(temp.getString("QYSX"));
                        homeTask.setAddress(temp.getString("MTDZ"));
                        homeTask.setArea(temp.getString("XZQY"));
                        homeTask.setImgUrl(temp.getString("PICTURE"));
                        if(type != Util.GETYICAIJI)
                            homeTask.setAdcontent(temp.getString("GGNR"));
                        listTask.add(homeTask);
                        if(listTask.size() == 0)
                        {
                            Util.toastLongShow(TaskListActivity.this,"暂无数据");
                        }
                        else
                        {
                            taskListViewAdapter.notifyDataSetChanged();
                        }

                    }

                }
                else
                {
                    for(int i = 0;i < jsonArray.length();i++)
                    {
                        JSONObject temp = (JSONObject)jsonArray.get(i);
                        HomeTask homeTask = new HomeTask();
                        homeTask.setAdid(temp.getString("ID"));
                        homeTask.setAdbh(temp.getString("MTBH"));
                        homeTask.setAdtype(temp.getString("MTXS"));
                        homeTask.setAddress(temp.getString("MTDZ"));
                        homeTask.setArea(temp.getString("XZQY"));
                        listTask.add(homeTask);
                        if(listTask.size() == 0)
                        {
                            Util.toastLongShow(TaskListActivity.this,"暂无数据");
                        }
                        else
                        {
                            taskListViewAdapter.notifyDataSetChanged();
                        }

                    }

                }
            }
            else
            {
                Util.toastShotShow(TaskListActivity.this, reason);
            }
        }
        catch (Exception e)
        {
            Util.toastShotShow(TaskListActivity.this,"网络错误" + type);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        if(TYPE == Util.YICAIJI || TYPE == Util.YIJIANCE || TYPE == Util.YISHENHE)
        {
            Intent intent = new Intent(TaskListActivity.this,DoneTaskActivity_.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("TASKOBJ",listTask.get(position));
            intent.putExtra("TYPE",TYPE);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(TaskListActivity.this,AdDetailsActivity_.class);
            if(TYPE == Util.WEIJIANCE)
                intent.putExtra("TYPE",Util.TAKEPICTURE);
            if(TYPE == Util.WEISHENHE)
                intent.putExtra("TYPE",Util.LIVE);
            intent.putExtra("id",listTask.get(position).getAdid());
            intent.putExtra("bh",listTask.get(position).getAdbh());
            startActivity(intent);
        }

    }

    /**
     * listview adapter
     */
    class TaskListViewAdapter extends BaseAdapter
    {
        ArrayList<HomeTask> listData;
        Context mContext;
        LayoutInflater mInflater;

        public TaskListViewAdapter(Context mContext,ArrayList<HomeTask> listData) {
            this.listData = listData;
            this.mContext = mContext;
            this.mInflater = LayoutInflater.from(mContext);
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
            tvID.setText(listData.get(position).getAdbh());
            tvMediaType.setText(listData.get(position).getAdtype());
            tvArea.setText(listData.get(position).getArea());
            tvAddress.setText(listData.get(position).getAddress());
            return convertView;
        }
    }




}

