package com.es.easysupervise.activity;

import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.es.easysupervise.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * 设置常用地址
 */
@EActivity(R.layout.activity_commonuse_address)
public class CommonuseAddress extends AppCompatActivity {

    @ViewById
    TextView tvRight;
    @ViewById
    TextView tvTitle;
    @ViewById
    EditText edtHomeAdd;
    @ViewById
    EditText edtCompanyAdd;

    @AfterViews
    void init()
    {
        tvTitle.setText("设置常用地址");
        tvRight.setText("完成");
    }

    @Click
    void imgLeft()
    {
        this.finish();
    }



}
