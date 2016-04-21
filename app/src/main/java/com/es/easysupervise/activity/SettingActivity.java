package com.es.easysupervise.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.es.easysupervise.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_setting)
public class SettingActivity extends AppCompatActivity {

    @ViewById
    TextView tvTitle;
    @ViewById
    ImageView imgAudio;
    @ViewById
    TextView tvVersion;

    boolean isOpen = true;

    @AfterViews
    void init()
    {
        tvTitle.setText("设置");
    }

    @Click
    void rlSettingAdd()
    {
        Intent intent = new Intent(this,CommonuseAddress_.class);
        startActivity(intent);
    }
    @Click
    void rlSettingLaw()
    {
        Toast.makeText(this,"2",Toast.LENGTH_SHORT).show();
    }
    @Click
    void rlSettingVersion()
    {
        Toast.makeText(this,"4",Toast.LENGTH_SHORT).show();
    }
    @Click
    void rlSettingHelp()
    {
        Intent intent = new Intent(this,HelpFeedbackActivity_.class);
        startActivity(intent);
    }
    @Click
    void rlSettingGuide()
    {
        Toast.makeText(this,"6",Toast.LENGTH_SHORT).show();
    }
    @Click
    void rlSettingAbout()
    {
        Toast.makeText(this,"7",Toast.LENGTH_SHORT).show();
    }
    @Click
    void imgAudio()
    {
        if(isOpen)
        {
            isOpen = false;
            imgAudio.setImageResource(R.drawable.pwdhide);
        }
        else
        {
            isOpen = true;
            imgAudio.setImageResource(R.drawable.pwdshow);
        }
    }

    @Click
    void imgLeft()
    {
        this.finish();
    }

}
