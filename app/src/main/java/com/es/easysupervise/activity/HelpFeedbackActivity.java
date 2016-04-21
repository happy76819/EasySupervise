package com.es.easysupervise.activity;

import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.es.easysupervise.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * 帮助与反馈
 */
@EActivity(R.layout.activity_help_feedback)
public class HelpFeedbackActivity extends AppCompatActivity {

    @ViewById
    TextView tvTitle;

    @AfterViews
    void init()
    {
        tvTitle.setText("帮助与反馈");
    }

    @Click
    void imgLeft()
    {
        this.finish();
    }

}
