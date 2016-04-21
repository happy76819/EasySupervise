package com.es.easysupervise.activity;

import android.app.Dialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

@EActivity(R.layout.activity_forget_pwd)
public class ForgetPwdActivity extends AppCompatActivity implements
        OkHttpClientManager.StringCallback
{

    @ViewById
    LinearLayout llVerify;
    @ViewById
    EditText edtPhone;
    @ViewById
    EditText edtVerifyCode;
    @ViewById
    Button btnGetVerifyCode;
    @ViewById
    Button btnNext;
    @ViewById
    LinearLayout llInputPwd;
    @ViewById
    EditText edtPwd1;
    @ViewById
    EditText edtPwd2;
    @ViewById
    ImageView imgShowPwd;
    @ViewById
    Button btnSubmit;
    @ViewById
    ImageView imgLeft;
    @ViewById
    TextView tvTitle;

    boolean isShow = false;//密码是否明文
    public int COUNTDOWN = 5;//获取验证码倒计时
    Handler handler = new Handler();
    String phone,pwd;
    Dialog loading;



    @AfterViews
    void init()
    {
        tvTitle.setText("输入帐号");
        llInputPwd.setVisibility(View.GONE);

    }

    @Click(R.id.imgShowPwd)
    void imgShowPwd_click()
    {
        if(isShow)//显示密文
        {
            imgShowPwd.setImageResource(R.drawable.pwdhide);
            edtPwd1.setTransformationMethod(PasswordTransformationMethod.getInstance());
            edtPwd2.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        else//显示明文
        {
            imgShowPwd.setImageResource(R.drawable.pwdshow);
            edtPwd1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            edtPwd2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        isShow = !isShow;
    }

    //获取验证码
    @Click(R.id.btnGetVerifyCode)
    void btnGetVerifyCode_click()
    {
        Log.e("sfdsdf","sdfsdfsdf");
        handler.postDelayed(runnable, 1000);
    }

    //返回
    @Click(R.id.imgLeft)
    void imgLeft_click()
    {
        ForgetPwdActivity.this.finish();
    }


    @Click(R.id.btnNext)
    void btnNext_click()
    {

        phone = edtPhone.getText().toString();
        String code = edtVerifyCode.getText().toString();
        if(phone.length() == 11 && code.length() != 0)
        {
            llVerify.setVisibility(View.GONE);
            llInputPwd.setVisibility(View.VISIBLE);
            tvTitle.setText("重新设置密码");
        }
        else
        {
            Util.toastLongShow(this,"请输入手机号和验证码");
        }

    }

    @Click(R.id.btnSubmit)
    void btnSubmit_click()
    {
        String pwd1 = edtPwd1.getText().toString();
        String pwd2 = edtPwd2.getText().toString();
        if(pwd1.equals(pwd2))
        {
            loading = Util.createLoadingDialog(this,"正在加载...");
            loading.show();

            String url = Util.MAINURL + Util.doForgetPWD +
                    "TEL=" + phone + "&PASSWORD=" + pwd1;
            OkHttpClientManager.getAsyn(url, this, Util.FORGETPWD);

        }
        else
        {
            Util.toastShotShow(this,"两次密码不一致");
        }
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

    @Override
    public void onFailure(Request request, IOException e) {
        loading.dismiss();
        Util.toastShotShow(ForgetPwdActivity.this,"网络连接超时");
    }

    @Override
    public void onResponse(String response, int type) {
        loading.dismiss();
        if(type == Util.FORGETPWD)
        {
            try
            {
                JSONObject jsonObject = new JSONObject(response);
                String reason = jsonObject.getString("reason");
                String rescode = jsonObject.getString("rescode");
                if(rescode.equals("1"))
                {
                    ForgetPwdActivity.this.finish();
                }
                Util.toastShotShow(ForgetPwdActivity.this,reason);
            }
            catch (Exception e)
            {
                Util.toastShotShow(ForgetPwdActivity.this,"网络错误");
            }
        }

    }
}
