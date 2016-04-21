package com.es.easysupervise.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.es.easysupervise.MainActivity_;
import com.es.easysupervise.R;
import com.es.easysupervise.uil.Util;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;

import java.io.File;

@EActivity(R.layout.activity_wellcome)
@Fullscreen
public class WellcomeActivity extends AppCompatActivity {

    @ViewById
    ImageView imgWellcome;

    public boolean isFirstIn = true;


    public static final long DELAY_MILLIS = 3000;
    public static final int GO_HOME = 1;
    public static final int GO_GUIDE = 2;

    @AfterViews
    void init()
    {

        imgWellcome.setImageResource(R.drawable.wellcome);

        isFirstIn = getSharedPreferences(Util.SHAREDPREFERENCENAME,MODE_PRIVATE)
                        .getBoolean("isFirstIn",true);

        if(isFirstIn)
        {
            Log.e("tag ","sssssssss");
            handler.sendEmptyMessageDelayed(GO_GUIDE,DELAY_MILLIS);
        }
        else
        {
            Log.e("tag","ddddddd");
            handler.sendEmptyMessageDelayed(GO_HOME,DELAY_MILLIS);
        }


    }

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case GO_HOME:
                    go_Home();
                    break;
                case GO_GUIDE:
                    makeDir();
                    go_Guide();
//                    go_Home();
                    break;

            }
            super.handleMessage(msg);
        }
    };

    void go_Home()
    {
        Intent intent = new Intent(WellcomeActivity.this, MainActivity_.class);
        startActivity(intent);
        WellcomeActivity.this.finish();
    }

    void go_Guide()
    {
        Intent intent = new Intent(WellcomeActivity.this,GuideViewActivity_.class);
        startActivity(intent);
        WellcomeActivity.this.finish();
    }

    void makeDir()//创建目录
    {
        File file = new File(Util.PATH);
        file.mkdir();
    }

}
