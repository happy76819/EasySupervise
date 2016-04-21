package com.es.easysupervise.activity;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.es.easysupervise.R;
import com.es.easysupervise.uil.OkHttpClientManager;
import com.es.easysupervise.uil.Util;
import com.squareup.okhttp.Request;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.io.IOException;

@EActivity(R.layout.activity_unfinished_task)
public class UnfinishedTask extends AppCompatActivity implements
        OkHttpClientManager.StringCallback
{

    @ViewById
    TextView tvTitle;
    @ViewById
    ListView lvUnfinished;


    private Dialog loading;

    @AfterViews
    void init()
    {
        loading = Util.createLoadingDialog(this,"正在加载...");
        //http://123.130.126.140/cjjcpt/services/querenCheckedMsg?FWBGMT_ID=20991
        String url = Util.MAINURL + Util.doCheckedMsg + "FWBGMT_ID=20991";

        OkHttpClientManager.getAsyn(url,this,Util.CHECKEDMSG);
    }

    @Click
    void imgLeft()
    {
        this.finish();
    }


    @Override
    public void onFailure(Request request, IOException e) {

    }

    @Override
    public void onResponse(String response, int type) {

        loading.dismiss();
        try {

            JSONObject jsonObject = new JSONObject(response);
            String rescode = jsonObject.getString("rescoede");
            String reason = jsonObject.getString("reason");
            if(rescode.equals("1"))
            {
                String FWBGMT_YWNR = jsonObject.getString("FWBGMT_YWNR");
            }
            else
            {
                Util.toastShotShow(UnfinishedTask.this,reason);
            }
        }
        catch (Exception e)
        {
            Util.toastShotShow(UnfinishedTask.this,"网络错误" + type);
        }


    }
}
