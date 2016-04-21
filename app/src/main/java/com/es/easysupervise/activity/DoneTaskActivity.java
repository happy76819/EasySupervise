package com.es.easysupervise.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.es.easysupervise.R;
import com.es.easysupervise.object.HomeTask;
import com.es.easysupervise.uil.OkHttpClientManager;
import com.es.easysupervise.uil.Util;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_done_task)
public class DoneTaskActivity extends AppCompatActivity {

    @ViewById
    ImageView imgAd;
    @ViewById
    TextView tvMediaId;
    @ViewById
    TextView tvMediaType;
    @ViewById
    TextView tvAreaAttr;
    @ViewById
    TextView tvMediaAdd;
    @ViewById
    TextView tvDistrict;
    @ViewById
    TextView tvAdContent;
    @ViewById
    RelativeLayout rlADContent;
    @ViewById
    TextView tvTitle;


    int TYPE;

    @AfterViews
    void init()
    {
        Intent intent = getIntent();
        HomeTask homeTask = (HomeTask)intent.getSerializableExtra("TASKOBJ");
        TYPE = intent.getIntExtra("TYPE",0);

        switch (TYPE)
        {
            case Util.YICAIJI:
                rlADContent.setVisibility(View.GONE);
                tvTitle.setText("已采集");
                break;
            case Util.YIJIANCE:
                rlADContent.setVisibility(View.VISIBLE);
                tvTitle.setText("已监测");
                break;
            case Util.YISHENHE:
                rlADContent.setVisibility(View.VISIBLE);
                tvTitle.setText("已审核");
                break;
        }
        try
        {
            OkHttpClientManager.displayImage(imgAd,homeTask.getImgUrl(),R.drawable.ad_pic_default);
        }
        catch (Exception e)
        {
            Util.toastShotShow(this,"图片加载失败");
        }
        tvMediaId.setText(homeTask.getAdbh());
        tvMediaType.setText(homeTask.getAdtype());
        tvAreaAttr.setText(homeTask.getAreaType());
        tvMediaAdd.setText(homeTask.getAddress());
        tvDistrict.setText(homeTask.getArea());
        tvAdContent.setText(homeTask.getAdcontent());

    }

    @Click
    void imgLeft()
    {
        this.finish();
    }




}
