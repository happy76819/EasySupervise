package com.es.easysupervise.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.es.easysupervise.R;
import com.es.easysupervise.adapter.MySpinnerAdapter;
import com.es.easysupervise.object.KeyValue;
import com.es.easysupervise.object.LawObj;
import com.es.easysupervise.uil.GetData;
import com.es.easysupervise.uil.ImageTools;
import com.es.easysupervise.uil.OkHttpClientManager;
import com.es.easysupervise.uil.Util;
import com.squareup.okhttp.Request;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;

@EActivity(R.layout.activity_ad_details)
public class AdDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        OkHttpClientManager.StringCallback
{

    @ViewById
    TextView tvTitle;
    @ViewById
    TextView tvMediaId;
    @ViewById
    TextView tvMediaType;
    @ViewById
    TextView tvAreaAttr;
    @ViewById
    TextView tvMediaAdd;
    @ViewById
    TextView tvDistrict;
    @ViewById
    TextView tvAdContent;
    @ViewById
    Button btnTPSubmit;
    @ViewById
    RelativeLayout rlLive;
    @ViewById
    RelativeLayout rlSearchDeal;
    @ViewById
    LinearLayout llRemark;
    @ViewById
    Button btnBuy;
    @ViewById
    Button btnCollect;
    @ViewById
    Button btnLegal;
    @ViewById
    Button btnIllegal;
    @ViewById
    TextView tvRight;
    @ViewById
    EditText edtAdContent;
    @ViewById
    RelativeLayout rlAdStatus;
    @ViewById
    Spinner spAdStatus;
    @ViewById
    LinearLayout llLaw;
    @ViewById
    Spinner spLaw;
    @ViewById
    ImageView imgAd;

    int TYPE;
    String picName;
    boolean takePic = false;
    ArrayList<KeyValue> listAdStatus = new ArrayList<>();
    String selectStatus;
    String mtbh,mtid;
    Dialog loading;
    Bitmap bitmap;
    String imgBase64;
    ArrayList<KeyValue> listCheck = new ArrayList<>();
    int[] lawChecks;
    ArrayList<LawObj> listLawObj = new ArrayList<>();
    String userid,username;
    boolean imgbig = false;

    @AfterViews
    void init()
    {
        tvTitle.setText("");
        Intent intent = getIntent();
        TYPE = intent.getIntExtra("TYPE", 0);
        mtid = intent.getStringExtra("id");
        mtbh = intent.getStringExtra("bh");
        SharedPreferences sharedPreferences = getSharedPreferences(Util.SHAREDPREFERENCENAME,MODE_PRIVATE);
        userid = sharedPreferences.getString(Util.USERID,"");
        username = sharedPreferences.getString(Util.USERNAME,"");


        loading = Util.createLoadingDialog(this, "正在加载...");
        switch (TYPE)
        {
            case Util.INFOCOLLECT:
                this.finish();
                break;
            case Util.TAKEPICTURE:
                tvTitle.setText(getResources().getString(R.string.NAVSUPERVISE));
                llRemark.setVisibility(View.GONE);
                rlLive.setVisibility(View.GONE);
                btnTPSubmit.setVisibility(View.VISIBLE);
                rlSearchDeal.setVisibility(View.GONE);
                tvAdContent.setVisibility(View.GONE);
                edtAdContent.setVisibility(View.VISIBLE);
                tvRight.setBackgroundResource(R.drawable.nav_camera);
                rlAdStatus.setVisibility(View.VISIBLE);
                InputStream inAdStatus = getResources().openRawResource(R.raw.adstatus);
                try{
                    listAdStatus = GetData.getAdStatus(inAdStatus);
                }
                catch (Exception e)
                {
                    Log.e("error","error");
                }
                MySpinnerAdapter adstatusAdapter = new MySpinnerAdapter(this,listAdStatus);
                spAdStatus.setAdapter(adstatusAdapter);
                spAdStatus.setOnItemSelectedListener(this);

                loading.show();

                String url = Util.MAINURL + Util.doGetAdMsg + "MTZL_ID=" + mtid +
                        "&MTZL_MTBH=" + mtbh;
//        String url = Util.MAINURL + Util.doGetAdMsg + "MTZL_ID=2502689&MTZL_MTBH=BJDCWSDJ8327";

                OkHttpClientManager.getAsyn(url, this, Util.GETADMSG);
                break;
            case Util.SEARCHDEAL:
                tvTitle.setText(getResources().getString(R.string.NAVSEARCH));
                llRemark.setVisibility(View.GONE);
                rlLive.setVisibility(View.GONE);
                btnTPSubmit.setVisibility(View.GONE);
                rlSearchDeal.setVisibility(View.VISIBLE);
                tvAdContent.setVisibility(View.VISIBLE);
                edtAdContent.setVisibility(View.GONE);
                rlAdStatus.setVisibility(View.GONE);
                loading.show();

                url = Util.MAINURL + Util.doGetAdMsg + "MTZL_ID=" + mtid +
                        "&MTZL_MTBH=" + mtbh;
//        String url = Util.MAINURL + Util.doGetAdMsg + "MTZL_ID=2502689&MTZL_MTBH=BJDCWSDJ8327";

                OkHttpClientManager.getAsyn(url, this, Util.GETADMSG);
                break;
            case Util.LIVE:
                initLawData();
                tvTitle.setText(getResources().getString(R.string.NAVMAKESURE));
                llRemark.setVisibility(View.VISIBLE);
                rlLive.setVisibility(View.VISIBLE);
                btnTPSubmit.setVisibility(View.GONE);
                rlSearchDeal.setVisibility(View.GONE);
                tvAdContent.setVisibility(View.VISIBLE);
                edtAdContent.setVisibility(View.GONE);
                rlAdStatus.setVisibility(View.GONE);
//                tvRight.setBackgroundResource(R.drawable.nav_camera);
                MySpinnerAdapter listLawAdapter = new MySpinnerAdapter(this,listCheck);
                spLaw.setAdapter(listLawAdapter);
                spLaw.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String thid = listCheck.get(position).getKey();
                        String url = Util.MAINURL + Util.doGETLAW + "thid=" + thid;
                        OkHttpClientManager.getAsyn(url, AdDetailsActivity.this, Util.LAW);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                loading.show();

                url = Util.MAINURL + Util.doCheckedMsg + "FWBGMT_ID=" + mtid;
                Log.e("mtid = " ,url);
//                url = Util.MAINURL + Util.doLiveGetMsg + "FWBGMT_ID=1056";
                OkHttpClientManager.getAsyn(url, this, Util.CHECKEDMSG);
                break;
        }
    }

    @Click
    void imgLeft()
    {
        this.finish();
    }

    @Click
    void tvRight()
    {
        if(TYPE == Util.TAKEPICTURE)
            openCamera(this);
    }

    @Click
    void imgAd()
    {
        ViewGroup.LayoutParams para;
        para = imgAd.getLayoutParams();
        ViewGroup.MarginLayoutParams mpara;
        mpara = (ViewGroup.MarginLayoutParams)imgAd.getLayoutParams();
        if(imgbig)
        {
            para.height = dp2px(this,150);
            mpara.setMargins(dp2px(this,40),0,dp2px(this,40),0);
        }
        else
        {
            para.height = dp2px(this,400);
            mpara.setMargins(dp2px(this,10),0,dp2px(this,10),0);

        }

        imgAd.setLayoutParams(para);
        imgAd.setLayoutParams(mpara);
        imgbig = !imgbig;
    }

    public static int dp2px(Context context, int dp)
    {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Click
    void btnTPSubmit()
    {
        if(!takePic)
        {
            Util.toastShotShow(this,"未拍照" + selectStatus);
        }
        else
        {
            if(edtAdContent.getText().toString().length() == 0)
                Util.toastShotShow(this,"请填写广告内容");
            else if(selectStatus.equals("0"))
            {
                Util.toastShotShow(this,"请选择损坏情况。");
            }
            else
            {
                Log.e("imgbase64 = ","" + imgBase64.length());
//                String url = Util.MAINURL + Util.doSupervise +
//                        "MTZL_ID=" + mtid + "&FWBGMT_YWNR=" + edtAdContent.getText() +
//                        "&FWBG_CODE=JCBG" + "&USER_ID=" + userid + "&PICTURE=" + imgBase64;
//                Log.e("supervise",url);
//                OkHttpClientManager.getAsyn(url, this, Util.SUPERVISE);
                String adcontentGBK = "";
                try
                {
                    adcontentGBK = URLEncoder.encode(edtAdContent.getText().toString(),"GBK");

                }
                catch (Exception e)
                {

                }
                String url = Util.MAINURL + Util.doSupervise;
                OkHttpClientManager.Param[] params = {
                        new OkHttpClientManager.Param("MTZL_ID",mtid),
                        new OkHttpClientManager.Param("FWBGMT_YWNR",adcontentGBK),
                        new OkHttpClientManager.Param("FWBG_CODE","JCBG"),
                        new OkHttpClientManager.Param("USERID",userid),
                        new OkHttpClientManager.Param("USERNAME",username),
                        new OkHttpClientManager.Param("USERNAME",username),
                        new OkHttpClientManager.Param("SHQK",selectStatus),
                        new OkHttpClientManager.Param("PICTURE",imgBase64)
                };
                OkHttpClientManager.postAsyn(url,this,Util.SUPERVISE,params);
                loading.show();
            }

        }
    }

    @Click
    void btnBuy()
    {
        Util.toastShotShow(this,"研发中");
    }

    @Click
    void btnCollect() {
        Util.toastShotShow(this, "收藏成功");
    }

    @Click
    void btnLegal()
    {
        String url = Util.MAINURL + Util.doLiveUpload + "FWBGMT_ID=" + mtid +
                "&SH_ZTCODE=20&USERID=" + userid;
        OkHttpClientManager.getAsyn(url,this,Util.LIVEUPLOAD);
    }

    @Click
    void btnIllegal()
    {
        String codes = "";
        for(int i = 0; i < lawChecks.length; i++)
        {
            if(lawChecks[i] == 1)
                codes += listLawObj.get(i).getBianhao() + ",";
        }
        if(codes.length() == 0)
        {
            Util.toastShotShow(AdDetailsActivity.this,"请选择违法内容。");
        }
        else
        {
            Log.e("codes = " + codes,codes.substring(0,codes.length() - 1));
            String url = Util.MAINURL + Util.doLiveUpload + "FWBGMT_ID=" + mtid +
                    "&SH_ZTCODE=25&aa=" + codes + "&USERID=" + userid;
            OkHttpClientManager.getAsyn(url,this,Util.LIVEUPLOAD);
        }

    }

    //打开摄像头
    private void openCamera(Context context)
    {
//        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        picName = String.valueOf(System.currentTimeMillis()) + ".jpg";
//        Uri imageUri = Uri.fromFile(new File(Util.PATH, picName));
//        //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
//        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        startActivityForResult(openCameraIntent, 0);

        AlertDialog.Builder builder  = new AlertDialog.Builder(context);
        builder.setTitle("图片来源");
        builder.setNegativeButton("取消", null);
        builder.setItems(new String[]{"拍照"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri imageUri = null;
                        String fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
                        switch (which) {
                            case Util.TAKEPIC:
                                Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                picName = String.valueOf(System.currentTimeMillis()) + ".jpg";
                                imageUri = Uri.fromFile(new File(Util.PATH,picName));
                                //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                startActivityForResult(openCameraIntent, 0);
                                break;

                            case Util.CHOOSEPIC:
                                Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                imageUri = Uri.fromFile(new File(Util.PATH, fileName));
                                openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                openAlbumIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                startActivityForResult(openAlbumIntent, Util.TAKEPIC);
                                break;

                            default:
                                break;
                        }
                    }
                });
        builder.create().show();
    }


    //拍照后回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("requestCode", requestCode + "");
        super.onActivityResult(requestCode, resultCode, data);
        boolean fromCamera = false;
        if (resultCode == RESULT_OK)
        {
            if (requestCode == 0)
            {
                Uri uri = null;
                if (data != null)
                {
                    uri = data.getData();
                }
                else
                {
                    uri = Uri.fromFile(new File(Util.PATH,picName));
                    fromCamera = true;
                }
                bitmap = ImageTools.getBitmapFromUri(this.getContentResolver(), uri);
                bitmap = ImageTools.imageZoom(bitmap);
                if(fromCamera)
                {
                    Log.e("run run run","runrun");
                    MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "", "");
                }
                if (bitmap != null)
                {
                    takePic = true;
                    byte[] bytes = ImageTools.bitmapToBytes(bitmap);
//                    Log.e("bytes = ",bytes.toString());
                    imgBase64 = Base64.encodeToString(bytes, Base64.DEFAULT);
//                    imgBase64 = ImageTools.Bitmap2Base64(bitmap);
//                    Log.e("base64",imgBase64.toString());
                    imgAd.setImageBitmap(bitmap);
                }
                else
                {
                    Log.e("take pic","error error");
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectStatus = listAdStatus.get(position).getKey();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onFailure(Request request, IOException e) {
        loading.dismiss();
        Util.toastShotShow(AdDetailsActivity.this,"网络连接超时");
    }

    @Override
    public void onResponse(String response, int type) {

//        if(type == Util.GETADMSG || type == Util.SUPERVISE ||
//                type == Util.LIVEGETMSG || type == Util.LIVEUPLOAD)
//        {
//            loading.dismiss();
//        }
        loading.dismiss();
        try {
            Log.e("get admsg response = ", response);
            JSONObject jsonObject = new JSONObject(response);
            String rescode = jsonObject.getString("rescode");
            String reason = jsonObject.getString("reason");
            if (rescode.equals("1"))
            {
                JSONObject jsonResult;
                String mediaId,mediaType,areaAttr,mediaAddr,direct,adContent,adimgurl;
                switch (type)
                {
                    case Util.GETADMSG:
                        jsonResult = jsonObject.getJSONObject("result");
                        mediaId = jsonResult.getString("MTZL_MTBH");
                        mediaType = jsonResult.getString("MTZL_MYXSTYPE_ID");
                        areaAttr = jsonResult.getString("MTZL_QYSXTYPE_ID");
                        mediaAddr = jsonResult.getString("MTZL_MTDZ");
//                        adContent = jsonResult.getString("MTZL_XZHQUYU");
                        direct = jsonResult.getString("MTZL_XZHQUYU");
                        adimgurl = jsonResult.getString("PICTURE");

                        tvMediaId.setText(mediaId);
                        tvMediaType.setText(mediaType);
                        tvAreaAttr.setText(areaAttr);
                        tvMediaAdd.setText(mediaAddr);
//                tvAdContent.setText(adContent);
                        tvDistrict.setText(direct);
                        OkHttpClientManager.displayImage(imgAd, adimgurl, R.drawable.ad_pic_default);
                        break;
                    case Util.SUPERVISE:
                        Util.toastShotShow(AdDetailsActivity.this,reason);
                        break;
                    case Util.LIVEUPLOAD:
//                        jsonResult = jsonObject.getJSONObject("result");
                        Util.toastShotShow(AdDetailsActivity.this,reason);
                        break;
                    case Util.LAW:
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        lawChecks = new int[jsonArray.length()];
                        listLawObj.clear();
                        llLaw.removeAllViews();
                        for (int i = 0; i < jsonArray.length();i++)
                        {
                            JSONObject temp = (JSONObject)jsonArray.get(i);
                            LawObj lawObj = new LawObj();
                            lawObj.setContent(temp.getString("content"));
                            lawObj.setBianhao(temp.getString("bianhao"));
                            lawChecks[i] = 0;
                            listLawObj.add(lawObj);
                            llLaw.addView(addView(lawObj,i));
                        }
                        break;
                    case Util.CHECKEDMSG:
                        jsonResult = jsonObject.getJSONObject("result");
                        mediaId = jsonResult.getString("FWBGMT_MTBH");
                        mediaType = jsonResult.getString("MTXS");
                        areaAttr = jsonResult.getString("QYSX");
                        mediaAddr = jsonResult.getString("MTDZ");
                        direct = jsonResult.getString("SHENG") +
                                jsonResult.getString("SHI") +
                                jsonResult.getString("QU");
                        adContent = jsonResult.getString("GGNR");
                        adimgurl = jsonResult.getString("PICTURE");

                        tvMediaId.setText(mediaId);
                        tvMediaType.setText(mediaType);
                        tvAreaAttr.setText(areaAttr);
                        tvMediaAdd.setText(mediaAddr);
                        tvAdContent.setText(adContent);
                        tvDistrict.setText(direct);
                        OkHttpClientManager.displayImage(imgAd, adimgurl,
                                R.drawable.ad_pic_default);
                        break;

                }
            }
            else
            {
                Util.toastShotShow(AdDetailsActivity.this, reason);
                if(type == Util.GETADMSG || type == Util.CHECKEDMSG)
                    AdDetailsActivity.this.finish();
            }
        }
        catch (Exception e)
        {
            Log.e("eeeee","" + e.toString());
            Util.toastShotShow(AdDetailsActivity.this,"网络错误"+type);
        }
    }


    private void initLawData()
    {
        listCheck.add(new KeyValue("01","药品广告管理规范"));
        listCheck.add(new KeyValue("02","医疗广告管理规范"));
        listCheck.add(new KeyValue("03","医疗器械广告管理规范"));
        listCheck.add(new KeyValue("04","农药广告管理规范"));
        listCheck.add(new KeyValue("05","兽药广告管理规范"));
        listCheck.add(new KeyValue("06","食品广告管理规范"));
        listCheck.add(new KeyValue("07","酒类广告管理规范"));
        listCheck.add(new KeyValue("08","烟草广告管理规范"));
        listCheck.add(new KeyValue("09","化妆品广告管理规范"));
        listCheck.add(new KeyValue("10","房地产广告管理规范"));
        listCheck.add(new KeyValue("11","广告综合性法律、法规、规章"));
        listCheck.add(new KeyValue("12","其他法律法规规章相关规定"));
        listCheck.add(new KeyValue("2009", "美容服务机构"));
    }

    private View addView(LawObj lawObj, final int position) {
        // TODO 动态添加布局(xml方式)
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//      LayoutInflater inflater1=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//      LayoutInflater inflater2 = getLayoutInflater();
        LayoutInflater inflater3 = LayoutInflater.from(this);
        View view = inflater3.inflate(R.layout.item_law, null);
        view.setLayoutParams(lp);
        TextView tvCode = (TextView)view.findViewById(R.id.tvCode);
        TextView tvContent = (TextView)view.findViewById(R.id.tvNotice);
        final ImageView imgCheck = (ImageView)view.findViewById(R.id.imgAgreement);
        tvCode.setText(lawObj.getBianhao());
        tvContent.setText(lawObj.getContent());
        imgCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lawChecks[position] == 0) {
                    lawChecks[position] = 1;
                    imgCheck.setImageResource(R.drawable.agreement_checked);
                } else {
                    lawChecks[position] = 0;
                    imgCheck.setImageResource(R.drawable.agreement_check);
                }
            }
        });
        return view;
    }


}
