package com.es.easysupervise.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.es.easysupervise.R;
import com.es.easysupervise.adapter.MyCollectAdapter;
import com.es.easysupervise.object.MyCollect;
import com.es.easysupervise.uil.Util;
import com.es.easysupervise.widget.XListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity(R.layout.activity_my_collect)
public class MyCollectActivity extends AppCompatActivity implements
        XListView.IXListViewListener
{

    @ViewById
    XListView lvMyCollect;
    @ViewById
    TextView tvTitle;

    private MyCollectAdapter mAdapter;
    private ArrayList<MyCollect> items = new ArrayList<MyCollect>();
    private Handler mHandler;
    private int mIndex = 0;
    private int mRefreshIndex = 0;

    @AfterViews
    void init()
    {
        tvTitle.setText("我的收藏");
        geneItems();
        mHandler = new Handler();

        lvMyCollect.setPullRefreshEnable(true);
        lvMyCollect.setPullLoadEnable(true);
        lvMyCollect.setAutoLoadEnable(true);
        lvMyCollect.setXListViewListener(this);
        lvMyCollect.setRefreshTime(Util.getTime());

        mAdapter = new MyCollectAdapter(this,items,handler);
        lvMyCollect.setAdapter(mAdapter);

    }

    private void geneItems() {
        for (int i = 0; i < 10; ++i) {
            MyCollect myCollect = new MyCollect();
            myCollect.setAddress("烟海高速公路19K+700," + i);
            myCollect.setPrice("¥50000.00" + i);
            items.add(myCollect);
        }
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mIndex = ++mRefreshIndex;
                items.clear();
                geneItems();
                mAdapter = new MyCollectAdapter(MyCollectActivity.this,items,handler);
                lvMyCollect.setAdapter(mAdapter);
                onLoad();
            }
        }, 2500);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                geneItems();
                mAdapter.notifyDataSetChanged();
                onLoad();
            }
        }, 2500);
    }

    private void onLoad() {
        lvMyCollect.stopRefresh();
        lvMyCollect.stopLoadMore();
        lvMyCollect.setRefreshTime(Util.getTime());
    }



    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("msg",msg.what + "");
            items.remove(msg.what);
            mAdapter.notifyDataSetChanged();
        }
    };


    @Click
    void imgLeft()
    {
        this.finish();
    }
}
