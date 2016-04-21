package com.es.easysupervise.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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

@EActivity(R.layout.activity_register)
public class RegisterActivity extends AppCompatActivity implements OkHttpClientManager.StringCallback
{

    @ViewById
    EditText edtName;
    @ViewById
    TextView tvTitle;
    @ViewById
    EditText edtPhone;
    @ViewById
    EditText edtVerifyCode;
    @ViewById
    EditText edtPwd;
    @ViewById
    Button btnGetVerifyCode;
    @ViewById
    ImageView imgCollectUser;
    @ViewById
    ImageView imgSuperviseUser;
    @ViewById
    ImageView imgEpaiUser;
    @ViewById
    ImageView imgAgreement;
    @ViewById
    TextView tvAgreement;
    @ViewById
    Button btnDoregister;
    @ViewById
    ScrollView scvBg;
    @ViewById
    LinearLayout llCollectUser;
    @ViewById
    LinearLayout llSuperviseUser;
    @ViewById
    LinearLayout llEpaiUser;

    boolean isAgree = false;
    InputMethodManager manager;
    public int COUNTDOWN = 5;//获取验证码倒计时
    Handler handler = new Handler();
    float touch_x;
    float touch_y;
    Dialog loading;
    /**
     * 账户类型选择。
     * 0 未选择
     * 1 采集用户
     * 2 监测用户
     * 3 易牌用户
     */
    public int USERCHECK = 0;

    @AfterViews
    void init()
    {
        tvTitle.setText("注册");
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        scvBg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        touch_x = event.getX();
                        touch_y = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if(touch_y == event.getY() && touch_x == event.getX())
                        {
                            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                }
                return false;
            }
        });


    }

    @Click(R.id.imgAgreement)
    void imgAgreement_click()
    {
        isAgree = !isAgree;
        if(isAgree)
            imgAgreement.setImageResource(R.drawable.agreement_checked);
        else
            imgAgreement.setImageResource(R.drawable.agreement_check);
    }

    @Click(R.id.btnGetVerifyCode)
    void btnGetVerifyCode_click()
    {
        Log.e("sfdsdf","sdfsdfsdf");
        handler.postDelayed(runnable, 1000);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Log.e("count = " + COUNTDOWN, "sdfswerwer");
            btnGetVerifyCode.setText("" + COUNTDOWN--);
            handler.postDelayed(this, 1000);
            if(COUNTDOWN < 0)
            {
                COUNTDOWN = 60;
                btnGetVerifyCode.setText(getResources().getString(R.string.BTNGETVERIFYCODE));
                handler.removeCallbacks(this);
            }
        }
    };
    @Click(R.id.imgLeft)
    void imgLeft_click()
    {
        RegisterActivity.this.finish();
    }

    @Click(R.id.btnDoregister)
    void btnDoregister_click()
    {
        String name,phone,code,pwd;
        int shenfen;
        name = edtName.getText().toString();
        phone = edtPhone.getText().toString();
        code = edtVerifyCode.getText().toString();
        pwd = edtPwd.getText().toString();
        if(name.length() == 0 || phone.length() == 0 || code.length() == 0)
        {
            Toast.makeText(this, "信息填写有误", Toast.LENGTH_SHORT).show();
            return;
        }
        if(USERCHECK == 0)
        {
            Toast.makeText(this,"请选择用户类型",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isAgree)
        {
            Toast.makeText(this,"请阅读并同意平台使用协议",Toast.LENGTH_SHORT).show();
            return;
        }
        loading = Util.createLoadingDialog(this,"正在加载...");
        loading.show();

        String url = Util.MAINURL + Util.doREGISTE + "USERNAME=" + name
                + "&PASSWORD=" + pwd + "&TEL=" + phone + "&SHENFEN=" + USERCHECK;
        Log.e("url = ",url);
        OkHttpClientManager.getAsyn(url, this, Util.REGISTE);
    }

    @Click(R.id.llCollectUser)
    void llCollectUser_click()
    {
        if(USERCHECK == 1)
        {
            imgCollectUser.setImageResource(R.drawable.agreement_check);
            USERCHECK = 0;
        }
        else
        {
            imgCollectUser.setImageResource(R.drawable.agreement_checked);
            imgSuperviseUser.setImageResource(R.drawable.agreement_check);
            imgEpaiUser.setImageResource(R.drawable.agreement_check);
            USERCHECK = 1;
        }
    }

    @Click(R.id.llSuperviseUser)
    void llSuperviseUser_click()
    {
        if(USERCHECK == 2)
        {
            imgSuperviseUser.setImageResource(R.drawable.agreement_check);
            USERCHECK = 0;
        }
        else
        {
            imgCollectUser.setImageResource(R.drawable.agreement_check);
            imgSuperviseUser.setImageResource(R.drawable.agreement_checked);
            imgEpaiUser.setImageResource(R.drawable.agreement_check);
            USERCHECK = 2;
        }
    }

    @Click(R.id.llEpaiUser)
    void llEpaiUser_click()
    {
        if(USERCHECK == 3)
        {
            imgEpaiUser.setImageResource(R.drawable.agreement_check);
            USERCHECK = 0;
        }
        else
        {
            imgCollectUser.setImageResource(R.drawable.agreement_check);
            imgSuperviseUser.setImageResource(R.drawable.agreement_check);
            imgEpaiUser.setImageResource(R.drawable.agreement_checked);
            USERCHECK = 3;
        }
    }

    @Override
    public void onFailure(Request request, IOException e) {
        loading.dismiss();
        Util.toastShotShow(RegisterActivity.this,"网络连接超时");
    }

    @Override
    public void onResponse(String response, int type) {

        loading.dismiss();
        Log.e("login", "str = " + response);
        if(type == Util.REGISTE)
        {
            try
            {
                JSONObject jsonObject = new JSONObject(response);
                String reason = jsonObject.getString("reason");
                String rescode = jsonObject.getString("rescode");
                if(rescode.equals("1"))
                {
                    JSONObject jsonResult = jsonObject.getJSONObject("result");
                    String userid = jsonResult.getString("USERID");

                    SharedPreferences sharedPreferences = getSharedPreferences(Util.SHAREDPREFERENCENAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Util.USERNAME,edtName.getText().toString());
                    editor.putString(Util.PWD,edtPwd.getText().toString());
                    editor.putString(Util.USERID,userid);
                    editor.commit();
                    Intent intent = new Intent(RegisterActivity.this,PersonalInfoActivity_.class);
                    intent.putExtra("front","1");
                    startActivity(intent);
                    RegisterActivity.this.finish();
                }
                else
                {
                    Util.toastShotShow(RegisterActivity.this,reason);
                }

            }catch (Exception e)
            {

            }

        }


    }
}
