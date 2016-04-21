package com.es.easysupervise;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.es.easysupervise.activity.ForgetPwdActivity_;
import com.es.easysupervise.activity.HomeActivity_;
import com.es.easysupervise.activity.RegisterActivity_;
import com.es.easysupervise.uil.OkHttpClientManager;
import com.es.easysupervise.uil.Util;
import com.squareup.okhttp.Request;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * 登录
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements OkHttpClientManager.StringCallback
{

    InputMethodManager manager;

    @ViewById
    ScrollView scvBg;
    @ViewById
    EditText edtName;
    @ViewById
    EditText edtPwd;
    @ViewById
    Button btnLogin;
    @ViewById
    Button btnForget;
    @ViewById
    Button btnRegister;

    float touch_x;
    float touch_y;
    Dialog loading;

    @AfterViews
    void init()
    {

        if(!Util.getNetStatus(getApplicationContext()))
        {
            Util.toastShotShow(getApplicationContext(),"当前网络不可用");
        }

        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        scvBg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touch_x = event.getX();
                        touch_y = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (touch_y == event.getY() && touch_x == event.getX()) {
                            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                }
                return false;
            }

        });




    }

    @Click (R.id.btnRegister)
    void btnRegister_click()
    {
        Intent intent = new Intent(MainActivity.this, RegisterActivity_.class);
        startActivity(intent);

    }

    @Click(R.id.btnForget)
    void btnForget_click()
    {
        Intent intent = new Intent(MainActivity.this, ForgetPwdActivity_.class);
        startActivity(intent);
    }

    @Click(R.id.btnLogin)
    void btnLogin_click()
    {
//        String urll = "http://web.juhe.cn:8080/environment/air/pm";
//        String url = "http://apis.juhe.cn/mobile/get";
        String username = edtName.getText().toString();
        String pwd = edtPwd.getText().toString();
//        Log.e("username = " + username, "pwd = " + pwd);
        String url = Util.MAINURL + Util.doLOGIN + "USERNAME=" + username
                + "&PASSWORD=" + pwd;
//        Log.e("url = " ,"" + url);
        OkHttpClientManager.getAsyn(url, this, Util.LOGIN);
        loading = Util.createLoadingDialog(this,"正在加载....");
        loading.show();


    }

    @Override
    public void onFailure(Request request, IOException e) {
        loading.dismiss();
        Util.toastShotShow(MainActivity.this,"网络连接超时");
    }

    @Override
    public void onResponse(String response, int type) {

        loading.dismiss();
        if(type == Util.LOGIN)
        {
//            Log.e("login","str = " + response);
            try{
                JSONObject myJson = new JSONObject(response);
                String reason = myJson.getString("reason");
                String rescode = myJson.getString("rescode");

                if(rescode.equals("1"))
                {
                    JSONObject jsonResult = myJson.getJSONObject("result");
                    String userid = jsonResult.getString("USERID");
//                JSONArray jsonArray = new JSONArray(myJson.getJSONArray())
//                    Log.e("reason " + reason,"userid" + userid);

                    SharedPreferences sharedPreferences = getSharedPreferences(Util.SHAREDPREFERENCENAME,
                            MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Util.USERNAME, edtName.getText().toString());
                    editor.putString(Util.PWD,edtPwd.getText().toString());
                    editor.putString(Util.USERID,userid);
                    editor.commit();
                    Intent intent = new Intent(MainActivity.this, HomeActivity_.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                }
                else
                {
                    Util.toastShotShow(MainActivity.this,reason);
                }

            }
            catch (JSONException e)
            {
                Log.e("json fail","error");
            }
        }

    }
}
