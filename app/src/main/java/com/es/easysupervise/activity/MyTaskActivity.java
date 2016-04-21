package com.es.easysupervise.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.es.easysupervise.R;
import com.es.easysupervise.uil.Util;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;


@EActivity(R.layout.activity_my_task)
public class MyTaskActivity extends AppCompatActivity {

    @ViewById
    TextView tvTitle;


    String longitude;
    String latitude;

    @AfterViews
    void init()
    {
        tvTitle.setText("我的任务");
        longitude = getIntent().getStringExtra("lng");
        latitude = getIntent().getStringExtra("lat");

    }

    @Click
    void imgLeft()
    {
        this.finish();
    }

    @Click
    void rlYicaiji()
    {
        gotoTaskListActivity(Util.YICAIJI);
    }
    @Click
    void rlWeicaiji()
    {
        gotoTaskListActivity(Util.WEICAIJI);
    }
    @Click
    void rlYijiance()
    {
        gotoTaskListActivity(Util.YIJIANCE);
    }
    @Click
    void rlWeijiance()
    {
        gotoTaskListActivity(Util.WEIJIANCE);
    }
    @Click
    void rlYishenhe()
    {
        gotoTaskListActivity(Util.YISHENHE);
    }
    @Click
    void rlWeishenhe()
    {
        gotoTaskListActivity(Util.WEISHENHE);
    }

    void gotoTaskListActivity(int type)
    {
        Intent intent = new Intent(MyTaskActivity.this,TaskListActivity_.class);
        intent.putExtra("lng",longitude);
        intent.putExtra("lat",latitude);
        intent.putExtra("TYPE",type);
        startActivity(intent);
    }

}
