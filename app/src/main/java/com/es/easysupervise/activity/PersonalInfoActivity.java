package com.es.easysupervise.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.es.easysupervise.R;
import com.es.easysupervise.adapter.MySpinnerAdapter;
import com.es.easysupervise.adapter.PersonalInfoAdapter;
import com.es.easysupervise.object.KeyValue;
import com.es.easysupervise.uil.ImageTools;
import com.es.easysupervise.uil.OkHttpClientManager;
import com.es.easysupervise.uil.Util;
import com.squareup.okhttp.Request;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

@EActivity(R.layout.activity_personal_info)
public class PersonalInfoActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener,OkHttpClientManager.StringCallback
{

//    @ViewById
//    ListView lvPersonalInfo;
    @ViewById
    ImageView imgLeft;
    @ViewById
    TextView tvTitle;
    @ViewById
    TextView tvRight;
    @ViewById
    ImageView imgHead;
    @ViewById
    EditText edtName;
    @ViewById
    EditText edtCompany;
    @ViewById
    EditText edtCompanyAdd;
    @ViewById
    EditText edtOfficTel;
    @ViewById
    EditText edtPosition;
    @ViewById
    EditText edtTel;
    @ViewById
    EditText edtMyAdd;
    @ViewById
    Spinner spSex;


    PersonalInfoAdapter piAdapter;
    private static final int TAKE_PICTURE = 0;
    private static final int CHOOSE_PICTURE = 1;
    private static final int CROP_PICTURE = 2;
    String selectedSex = "1";
    ArrayList<KeyValue> listSex = new ArrayList<>();
    Dialog loading;
    String username,pwd,userid;
    String front = "0";

    @AfterViews
    void init()
    {
        loading = Util.createLoadingDialog(this,"正在加载...");
        loading.show();


        SharedPreferences sharedPreferences = getSharedPreferences(Util.SHAREDPREFERENCENAME, MODE_PRIVATE);
        username = sharedPreferences.getString(Util.USERNAME, "");
        pwd = sharedPreferences.getString(Util.PWD,"");
        userid = sharedPreferences.getString(Util.USERID,"");

        front = getIntent().getStringExtra("front");

        String url = Util.MAINURL + Util.doLOADMSG + "USERNAME=" + username +
                "&PASSWORD=" + pwd + "&USERID=" + userid;

        OkHttpClientManager.getAsyn(url,this,Util.LOADMSG);

        tvTitle.setText("完善个人资料");
        tvRight.setText("完成");
        listSex.add(new KeyValue("0", "女"));
        listSex.add(new KeyValue("1", "男"));

        MySpinnerAdapter sexAdapter = new MySpinnerAdapter(this,listSex);
        spSex.setAdapter(sexAdapter);
        imgHead.setImageResource(R.drawable.headdefault);
    }

    @Click(R.id.imgLeft)
    void imgLeft_click()
    {
        if(front.equals("1"))
        {
            Intent intent = new Intent(PersonalInfoActivity.this,HomeActivity_.class);
            startActivity(intent);
            this.finish();
        }
        else
            PersonalInfoActivity.this.finish();
    }

    @Click
    void imgHead()
    {
        showPicturePicker(PersonalInfoActivity.this);
    }

    private void showPicturePicker(Context context)
    {
        AlertDialog.Builder builder  = new AlertDialog.Builder(context);
        builder.setTitle("图片来源");
        builder.setNegativeButton("取消", null);
        builder.setItems(new String[]{"拍照", "相册"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri imageUri = null;
                        String fileName = "head.jpg";
                        switch (which) {
                            case TAKE_PICTURE:


                                Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                //删除上一次截图的临时文件
                                SharedPreferences sharedPreferences = getSharedPreferences(Util.SHAREDPREFERENCENAME,
                                        MODE_PRIVATE);
                                ImageTools.deletePhotoAtPathAndName(Util.PATH, sharedPreferences.getString(Util.HEADIMAGENAME, ""));

                                //保存本次截图临时文件名字
//                                fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(Util.HEADIMAGENAME, fileName);
                                editor.commit();

                                imageUri = Uri.fromFile(new File(Util.PATH,fileName));
                                //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                startActivityForResult(openCameraIntent, TAKE_PICTURE);
                                break;

                            case CHOOSE_PICTURE:
                                Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                imageUri = Uri.fromFile(new File(Util.PATH,fileName));
                                openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                openAlbumIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                                startActivityForResult(openAlbumIntent, TAKE_PICTURE);
                                break;

                            default:
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    //接收头像点击事件
//    Handler headClickHandler = new Handler()
//    {
//        @Override
//        public void handleMessage(Message msg) {
//            if(msg.what == 0)
//            {
//                showPicturePicker(PersonalInfoActivity.this);
//            }
//            super.handleMessage(msg);
//        }
//    };

    //截取图片
    public void cropImage(Uri uri, int outputX, int outputY, int requestCode)
    {
        Intent in = new Intent("com.android.camera.action.CROP");
        //需要裁减的图片格式
        in.setDataAndType(uri, "image/*");
        //允许裁减
        in.putExtra("crop", "true");
        //剪裁后ImageView显时图片的宽
        in.putExtra("outputX", 250);
        //剪裁后ImageView显时图片的高
        in.putExtra("outputY", 250);
        //设置剪裁框的宽高比例
        in.putExtra("aspectX", 1);
        in.putExtra("aspectY", 1);
        in.putExtra("return-data", true);
        startActivityForResult(in, CROP_PICTURE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("requestCode", requestCode + "");
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case TAKE_PICTURE:
                    Uri uri = null;
                    if (data != null) {
                        uri = data.getData();
                        System.out.println("Data");
                    }else {
                        System.out.println("File");
                        String fileName = getSharedPreferences(Util.SHAREDPREFERENCENAME,Context.MODE_PRIVATE).getString(Util.HEADIMAGENAME, "");
                        uri = Uri.fromFile(new File(Util.PATH,fileName));
                    }
                    cropImage(uri, 500, 500, CROP_PICTURE);
                    break;

                case CROP_PICTURE:
                    Bitmap photo = null;
                    Uri photoUri = data.getData();
                    if (photoUri != null) {
                        photo = BitmapFactory.decodeFile(photoUri.getPath());
                    }
                    if (photo == null) {
                        Bundle extra = data.getExtras();
                        if (extra != null) {
                            photo = (Bitmap)extra.get("data");
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        }

                    }
                    piAdapter.setHeadBitmap(photo);
                    piAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedSex = listSex.get(position).getKey();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onFailure(Request request, IOException e) {
        loading.dismiss();
        Util.toastShotShow(PersonalInfoActivity.this,"网络连接超时");
    }

    @Override
    public void onResponse(String response, int type) {

        loading.dismiss();
        switch (type)
        {
            case Util.LOADMSG:
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    String reason = jsonObject.getString("reason");
                    String rescode = jsonObject.getString("rescode");
                    JSONObject jsonResult = jsonObject.getJSONObject("result");
                    if(rescode.equals("1") && jsonResult != null)
                    {
                        String pic = jsonResult.getString("USER_PICTURE");
                        String realname = jsonResult.getString("USER_REALNAME");
                        String position = jsonResult.getString("ZHIWEI");
                        String tel = jsonResult.getString("TEL");
                        String sex = jsonResult.getString("SEX");
                        String address = jsonResult.getString("ADDRESS");
                        String company = jsonResult.getString("USER_COMPANY");
                        String company_tel = jsonResult.getString("COMPANY_TEL");
                        String company_add = jsonResult.getString("COMPANY_ADDRESS");

                        if(!realname.equals("请填写"))
                            edtName.setText(realname);
                        if(!company.equals("请填写"))
                            edtCompany.setText(company);
                        if(!company_add.equals("请填写"))
                            edtCompanyAdd.setText(company_add);
                        if(!company_tel.equals("请填写"))
                            edtOfficTel.setText(company_tel);
                        if(!position.equals("请填写"))
                            edtPosition.setText(position);
                        if(!tel.equals("请填写"))
                            edtTel.setText(tel);
                        if(!address.equals("请填写"))
                            edtMyAdd.setText(address);
                        if(sex.equals("男"))
                            spSex.setSelection(1,false);
                        else
                            spSex.setSelection(0,false);
                    }

                }
                catch (Exception e)
                {

                }
                break;
            case Util.UPLOADMSG:
                Log.e("uploadmsg","" + response);
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    String reason = jsonObject.getString("reason");
                    String rescode = jsonObject.getString("rescode");
                    Util.toastShotShow(PersonalInfoActivity.this,reason);
                }
                catch (Exception e)
                {

                }
                break;

        }

    }


    @Click
    void tvRight()
    {
//        String pic =
        loading.show();
        String realnameGBK,positionGBK,addressGBK,companyGBK,company_addGBK;
        try
        {
            realnameGBK = URLEncoder.encode(edtName.getText().toString(),"GBK");
            positionGBK = URLEncoder.encode(edtPosition.getText().toString(),"GBK");
            addressGBK = URLEncoder.encode(edtMyAdd.getText().toString(),"GBK");
            companyGBK = URLEncoder.encode(edtCompany.getText().toString(),"GBK");
            company_addGBK = URLEncoder.encode(edtCompanyAdd.getText().toString(),"GBK");
        }
        catch (Exception e)
        {
            realnameGBK = "";
            positionGBK = "";
            addressGBK = "";
            companyGBK = "";
            company_addGBK = "";
        }
        String tel = edtTel.getText().toString();
        String sex = spSex.getSelectedItemPosition() + "";
        String company_tel = edtOfficTel.getText().toString();
        String url = Util.MAINURL + Util.doUPLOADMSG + "USERNAME=" + username +
                "&PASSWORD=" + pwd + "&USERID=" + userid + "&TEL=" + tel +
                "&ADDRESS=" + addressGBK + "&SEX=" + sex + "&USER_COMPANY=" + companyGBK +
                "&COMPANY_ADDRESS=" + company_addGBK + "&COMPANY_TEL=" + company_tel + "&ZHIWEI=" + positionGBK +
                "&USER_REALNAME=" + realnameGBK;
        Log.e(Util.doUPLOADMSG,"" + url);
//        String url = Util.MAINURL + Util.doUPLOADMSG;
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("USERNAME",username),
        new OkHttpClientManager.Param("PASSWORD",pwd),
        new OkHttpClientManager.Param("USERID",userid),
        new OkHttpClientManager.Param("TEL",tel),
        new OkHttpClientManager.Param("ADDRESS",addressGBK),
        new OkHttpClientManager.Param("SEX",sex),
        new OkHttpClientManager.Param("USER_COMPANY",companyGBK),
        new OkHttpClientManager.Param("COMPANY_ADDRESS",company_addGBK),
        new OkHttpClientManager.Param("COMPANY_TEL",company_tel),
        new OkHttpClientManager.Param("ZHIWEI",positionGBK),
        new OkHttpClientManager.Param("USER_REALNAME",realnameGBK),
//        new OkHttpClientManager.Param("USER_PICTURE",pwd);
        };
//        OkHttpClientManager.postAsyn(url,this,Util.UPLOADMSG,params);
        OkHttpClientManager.getAsyn(url,this,Util.UPLOADMSG);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            if(front.equals("1"))
            {
                Intent intent = new Intent(PersonalInfoActivity.this,HomeActivity_.class);
                startActivity(intent);
                this.finish();
            }
            else
                this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
