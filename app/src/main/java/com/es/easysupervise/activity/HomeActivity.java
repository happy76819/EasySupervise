package com.es.easysupervise.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.es.easysupervise.Listener.GaoDeLocationListener;
import com.es.easysupervise.R;
import com.es.easysupervise.adapter.MySpinnerAdapter;
import com.es.easysupervise.object.AdId;
import com.es.easysupervise.object.KeyValue;
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

@EActivity(R.layout.activity_home)
public class HomeActivity extends AppCompatActivity implements LocationSource,
        AMap.InfoWindowAdapter,AMap.OnMarkerClickListener,AMap.OnMapClickListener,
        AdapterView.OnItemClickListener,OkHttpClientManager.StringCallback,
        AdapterView.OnItemSelectedListener
{

    @ViewById
    DrawerLayout mDrawerLayout;
    @ViewById
    ImageView imgNavLeft;
    @ViewById
    ImageView imgNavRight;
    @ViewById
    TextView tvNavCollect;
    @ViewById
    TextView tvNavSearch;
    @ViewById
    TextView tvNavSupervise;
    @ViewById
    TextView tvNavMakesure;
    @ViewById
    EditText edtSearch;
    @ViewById
    Spinner spMediaType;
    @ViewById
    EditText edtCount;
    @ViewById
    EditText edtMediaAdd;
    @ViewById
    LinearLayout llCollectInfo;
    @ViewById
    MapView mapview;
    @ViewById
    ImageView imgTakePic;
    @ViewById
    Spinner spAreaType;
    @ViewById
    TextView tvAreaContent;
    @ViewById
    ImageView imgHead;
    @ViewById
    EditText edtClientname;
    @ViewById
    EditText edtClientnum;
    @ViewById
    ImageView imgTakedPic;
    @ViewById
    ImageView imgShowBig;



    AMap aMap;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    public GaoDeLocationListener gdLocationListener;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    public OnLocationChangedListener mListener;
    AMapLocation aMapLocation;
    String picName;
    Marker AD;
    ArrayList<LatLng> coordinateList = new ArrayList<>();
    ArrayList<Marker> markerList = new ArrayList<>();
    Marker showingMarker;
    public int TYPE = 0;
    int myProvince = 0;
    int myCity = 0;
    int myDistrict = 0;
    boolean runonce = false; //填写地址信息只运行一次

    ArrayList<KeyValue> listAreaType,listMediaType;
    ArrayList<AdId> listAdId = new ArrayList<>();
    String province,city,district,selectMediaType,selectAreaType;
    String longitude,latitude;
    String imgBase64;
    Dialog loading;
    double exitTime;


    @AfterViews
    void init()
    {
        if(!Util.getNetStatus(getApplicationContext()))
        {
            Util.toastShotShow(getApplicationContext(),"当前网络不可用");
        }

        loading = Util.createLoadingDialog(this,"正在加载....");

        mapview.onCreate(null);// 必须要写
        aMap = mapview.getMap();

        aMap.setLocationSource(this);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        MyLocationStyle style = new MyLocationStyle();
        style.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.mypoint));
        aMap.setMyLocationStyle(style);

        aMap.setInfoWindowAdapter(this);
        aMap.setOnMarkerClickListener(this);
        aMap.setOnMapClickListener(this);

        InputStream inMediaType = getResources().openRawResource(R.raw.mediatype);
        InputStream inAreaType = getResources().openRawResource(R.raw.areatype);
        try{
            listAreaType = GetData.getAreaType(inAreaType);
            listMediaType = GetData.getMediaType(inMediaType);
            Log.e("list size = " , "" + listAreaType.size());
            Log.e("list size = " , "" + listMediaType.size());
        }
        catch (Exception e)
        {
            Log.e("error","error");
        }

        MySpinnerAdapter mediatypeAdapter = new MySpinnerAdapter(this,listMediaType);
        MySpinnerAdapter areatypeAdapter = new MySpinnerAdapter(this,listAreaType);
        spMediaType.setAdapter(mediatypeAdapter);
        spAreaType.setAdapter(areatypeAdapter);
        spMediaType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("select ", "mediatype" + listMediaType.get(position).getValue());
                selectMediaType = listMediaType.get(position).getKey();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spAreaType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("select ", "mediatype" + listAreaType.get(position).getValue());
                selectAreaType = listAreaType.get(position).getKey();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //进入监测界面
        tvNavSupervise();

    }

    @Click(R.id.imgNavLeft)
    void imgNavLeft_click()
    {
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    @Click
    void tvNavCollect()
    {
        TYPE = 0;
        tvNavCollect.setTextColor(getResources().getColor(R.color.MAINNAVTAB_HILIGHT));
        tvNavCollect.setBackgroundResource(R.drawable.nav_selected_bg);
        tvNavSearch.setTextColor(getResources().getColor(R.color.MAINNAVTAB_NORMAL));
        tvNavSearch.setBackgroundResource(0);
        tvNavSupervise.setTextColor(getResources().getColor(R.color.MAINNAVTAB_NORMAL));
        tvNavSupervise.setBackgroundResource(0);
        tvNavMakesure.setTextColor(getResources().getColor(R.color.MAINNAVTAB_NORMAL));
        tvNavMakesure.setBackgroundResource(0);
        llCollectInfo.setVisibility(View.VISIBLE);
        mapview.setVisibility(View.GONE);
    }
    @Click
    void tvNavSupervise()
    {
        TYPE = 1;
        tvNavSupervise.setTextColor(getResources().getColor(R.color.MAINNAVTAB_HILIGHT));
        tvNavSupervise.setBackgroundResource(R.drawable.nav_selected_bg);
        tvNavSearch.setTextColor(getResources().getColor(R.color.MAINNAVTAB_NORMAL));
        tvNavSearch.setBackgroundResource(0);
        tvNavCollect.setTextColor(getResources().getColor(R.color.MAINNAVTAB_NORMAL));
        tvNavCollect.setBackgroundResource(0);
        tvNavMakesure.setTextColor(getResources().getColor(R.color.MAINNAVTAB_NORMAL));
        tvNavMakesure.setBackgroundResource(0);
        llCollectInfo.setVisibility(View.GONE);
        mapview.setVisibility(View.VISIBLE);
        for (int i = 0;i < markerList.size();i++)
        {
            Log.e("home activity","remove " + TYPE);
            markerList.get(i).remove();
        }
    }
    @Click
    void tvNavSearch()
    {
        TYPE = 2;
        tvNavSearch.setTextColor(getResources().getColor(R.color.MAINNAVTAB_HILIGHT));
        tvNavSearch.setBackgroundResource(R.drawable.nav_selected_bg);
        tvNavCollect.setTextColor(getResources().getColor(R.color.MAINNAVTAB_NORMAL));
        tvNavCollect.setBackgroundResource(0);
        tvNavSupervise.setTextColor(getResources().getColor(R.color.MAINNAVTAB_NORMAL));
        tvNavSupervise.setBackgroundResource(0);
        tvNavMakesure.setTextColor(getResources().getColor(R.color.MAINNAVTAB_NORMAL));
        tvNavMakesure.setBackgroundResource(0);
        llCollectInfo.setVisibility(View.GONE);
        mapview.setVisibility(View.VISIBLE);
        for (int i = 0;i < markerList.size();i++)
        {
            Log.e("home activity","remove " + TYPE);
            markerList.get(i).remove();
        }
    }

    @Click
    void tvNavMakesure()
    {
        TYPE = 3;
        tvNavMakesure.setTextColor(getResources().getColor(R.color.MAINNAVTAB_HILIGHT));
        tvNavMakesure.setBackgroundResource(R.drawable.nav_selected_bg);
        tvNavSearch.setTextColor(getResources().getColor(R.color.MAINNAVTAB_NORMAL));
        tvNavSearch.setBackgroundResource(0);
        tvNavSupervise.setTextColor(getResources().getColor(R.color.MAINNAVTAB_NORMAL));
        tvNavSupervise.setBackgroundResource(0);
        tvNavCollect.setTextColor(getResources().getColor(R.color.MAINNAVTAB_NORMAL));
        tvNavCollect.setBackgroundResource(0);
        llCollectInfo.setVisibility(View.GONE);
        mapview.setVisibility(View.VISIBLE);

        for (int i = 0;i < markerList.size();i++)
        {
            Log.e("home activity","remove " + TYPE);
            markerList.get(i).remove();
        }



    }

    @Click
    void imgHead()
    {
        Intent intent = new Intent(this,PersonalInfoActivity_.class);
        intent.putExtra("front", "0");
        startActivity(intent);
    }

    @Click
    void tvMenu1()
    {
        Intent intent = new Intent(this,MyLocationActivity_.class);
        startActivity(intent);
    }
    @Click
    void tvMenu2()
    {
        Intent intent = new Intent(this,MyTaskActivity_.class);
        intent.putExtra("lng",longitude);
        intent.putExtra("lat",latitude);
        startActivity(intent);
    }
    @Click
    void tvMenu3()
    {
        Intent intent = new Intent(this,NearbyAdActivity_.class);
        startActivity(intent);
    }
    @Click
    void tvMenu4()
    {
        Intent intent = new Intent(this,DiscoverActivity_.class);
        startActivity(intent);
    }
    @Click
    void tvMenu5()
    {
        Intent intent = new Intent(this,SettingActivity_.class);
        startActivity(intent);
    }
    @Click
    void tvMenu6()
    {
        Intent intent = new Intent(this,HelpFeedbackActivity_.class);
        startActivity(intent);
    }
    @Click
    void tvMenu7()
    {
        Intent intent = new Intent(this,MyCollectActivity_.class);
        startActivity(intent);
    }


    @Click//拍照片
    void imgTakePic ()
    {
        openCamera(HomeActivity.this);
    }

    @Click //上传信息
    void btnUpload()
    {
        if(tvAreaContent.getText().toString().length() == 0)
        {
            Util.toastShotShow(this,"请定位后再上传信息");
        }
        else if(imgBase64 == null || imgBase64.length() == 0)
        {
            Util.toastShotShow(this,"未拍照");
        }
        else if(edtCount.getText().toString().length() == 0)
        {
            Util.toastShotShow(this,"请输入版面数");
        }
        else if(selectMediaType.equals("0") )
        {
            Util.toastShotShow(this,"请选择媒体形式");
        }
        else if(selectAreaType.equals("0"))
        {
            Util.toastShotShow(this,"请选择区域属性");
        }
        else
        {
            loading.show();
            Log.e("imgbase64 len = ", "" + imgBase64.length());
            SharedPreferences sharedPreferences = getSharedPreferences(Util.SHAREDPREFERENCENAME,MODE_PRIVATE);
            String userid = sharedPreferences.getString(Util.USERID, "");
            String username = sharedPreferences.getString(Util.USERNAME,"");
            String mediaAddGBK = null,provinceGBK = null,cityGBK = null,districtGBK = null;
            try
            {
                mediaAddGBK = URLEncoder.encode(edtMediaAdd.getText().toString(),"GBK");
                provinceGBK = URLEncoder.encode(province,"GBK");
                cityGBK = URLEncoder.encode(city,"GBK");
                districtGBK = URLEncoder.encode(district,"GBK");
            }
            catch (Exception e)
            {
                Util.toastShotShow(HomeActivity.this,"转码失败");

            }
//            String url = Util.MAINURL + Util.doCollect + "bianhao=111&xingshiid=" +
//                    selectMediaType + "&sxid=" + selectAreaType + "&userid=" +
//                    userid + "&username=" + username + "&jingdu=" + longitude +
//                    "&weidu=" + latitude + "&mianshu=" + edtCount.getText().toString() +
//                    "&mtdz=" + mediaAddGBK + "&shen=" + provinceGBK +
//                    "&shi=" + cityGBK + "&qu=" + districtGBK  + "&zhaopian=" + imgBase64;

//            Log.e("gbk = ",mediaAddGBK + "," + provinceGBK + "," + cityGBK + "," + districtGBK);
            String url = Util.MAINURL + "caijiapp.action";
            OkHttpClientManager.Param[] params = {
                    new OkHttpClientManager.Param("bianhao","111"),
                    new OkHttpClientManager.Param("xingshiid",selectMediaType),
                    new OkHttpClientManager.Param("sxid",selectAreaType),
                    new OkHttpClientManager.Param("userid",userid),
                    new OkHttpClientManager.Param("username",username),
                    new OkHttpClientManager.Param("jingdu",longitude),
                    new OkHttpClientManager.Param("weidu",latitude),
                    new OkHttpClientManager.Param("mianshu",edtCount.getText().toString()),
                    new OkHttpClientManager.Param("mtdz",mediaAddGBK),
                    new OkHttpClientManager.Param("sheng",provinceGBK),
                    new OkHttpClientManager.Param("shi",cityGBK),
                    new OkHttpClientManager.Param("qu",districtGBK),
                    new OkHttpClientManager.Param("clientname",edtClientname.getText().toString()),
                    new OkHttpClientManager.Param("clientnum",edtClientnum.getText().toString()),
                    new OkHttpClientManager.Param("zhaopian",imgBase64),

            };
//            Log.e("Long")
            OkHttpClientManager.postAsyn(url,this,Util.COLLECTAD,params);


//            Log.e("url === ",url);
//
//            OkHttpClientManager.getAsyn(url, this, Util.COLLECTAD);
        }


    }



    //点击照片 显示大图
    @Click (R.id.imgTakedPic)
    void imgTakedPic_click()
    {
        imgShowBig.setImageBitmap(imgTakedPic.getDrawingCache());
        imgShowBig.setVisibility(View.VISIBLE);
    }

    @Click (R.id.imgShowBig)
    void imgShowBig_click()
    {
        imgShowBig.setVisibility(View.GONE);

    }


    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            gdLocationListener = new GaoDeLocationListener(getApplicationContext(),locationHandler);
            mLocationClient = new AMapLocationClient(getApplicationContext());
            mLocationClient.setLocationListener(gdLocationListener);

            initLocationOption();
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.startLocation();

        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    public void initLocationOption()
    {
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(10000);
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapview.onResume();
        mLocationClient.startLocation();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapview.onPause();
        mLocationClient.stopLocation();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapview.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapview.onDestroy();
        mLocationClient.onDestroy();
    }


    /**
     * 接受定位信息
     */
    public Handler locationHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle b = msg.getData();
            aMapLocation = b.getParcelable("location");
//            Log.e("tag","loc = " + aMapLocation.getAddress());
            mListener.onLocationChanged(aMapLocation);
            province = aMapLocation.getProvince();
            city = aMapLocation.getCity();
            district = aMapLocation.getDistrict();
            longitude = aMapLocation.getLongitude() + "";
            latitude = aMapLocation.getLatitude() + "";
            setCollectView();
            loadAdByLocation(aMapLocation.getLongitude(),aMapLocation.getLatitude());
//                loadAdByLocation(117,36);

        }
    };

    //打开摄像头
    private void openCamera(Context context)
    {
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
                                imageUri = Uri.fromFile(new File(Util.PATH, picName));
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
                Bitmap bitmap = ImageTools.getBitmapFromUri(this.getContentResolver(), uri);

                if (bitmap != null)
                {
                    bitmap = ImageTools.imageZoom(bitmap);
                    if(fromCamera)
                    {
                        MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "", "");
                    }
//                    imgTakePic.setVisibility(View.GONE);
                    imgTakedPic.setVisibility(View.VISIBLE);
                    Bitmap bitmap1 = ImageTools.small(bitmap,0.6f);
                    imgTakedPic.setImageBitmap(bitmap1);
                    imgTakedPic.buildDrawingCache();
                    imgTakedPic.setDrawingCacheEnabled(true);
                    byte[] bytes = ImageTools.bitmapToBytes(bitmap);
//                    Log.e("bytes = ",bytes.toString());
                    imgBase64 = Base64.encodeToString(bytes, Base64.DEFAULT);
//                    imgBase64 = bytes;
                }
                else
                {
                    Log.e("take pic","error error");
                }
            }
        }
    }

    /**
     * 设置广告 popup
     */
    public void setAdMarker()
    {
        for(int i = 0; i < markerList.size();i++)
        {
            markerList.get(i).remove();
        }
        markerList.clear();
        if(TYPE == 3)
        {
            for(int i = 0; i < listAdId.size(); i++)
            {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(listAdId.get(i).getLatLng());
                markerOptions.title("" + listAdId.get(i).getMTID());
                Marker marker = aMap.addMarker(markerOptions);

                markerList.add(marker);
            }
        }
        else
        {
            for(int i = 0; i < listAdId.size(); i++)
            {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(listAdId.get(i).getLatLng());
                markerOptions.title("" + listAdId.get(i).getMTBH());
                Marker marker = aMap.addMarker(markerOptions);

                markerList.add(marker);
            }
        }
    }


    /**************InforWindowAdapter***********************************/
    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow = getLayoutInflater().inflate(
                R.layout.map_pup_window, null);
        render(marker, infoWindow);
        return infoWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View infoWindow = getLayoutInflater().inflate(
                R.layout.map_pup_window, null);
        render(marker, infoWindow);
        return infoWindow;
    }

    /**
     * 自定义infowinfow窗口
     */
    public void render(final Marker marker, View view) {
        TextView btnMediaInfo = (TextView)view.findViewById(R.id.btnAdInfo);
        btnMediaInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AdId adId = getAdidByTitle(marker.getTitle(),TYPE);
                String bh = adId.getMTBH();
                String mtzl_id = adId.getMTID();
                Intent intent = new Intent(HomeActivity.this,AdDetailsActivity_.class);
                intent.putExtra("TYPE",TYPE);
                intent.putExtra("id",mtzl_id);
                intent.putExtra("bh",bh);
                startActivity(intent);
            }
        });

        TextView btnNavigate = (TextView)view.findViewById(R.id.btnNavigate);
        btnNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double myLat = aMapLocation.getLatitude();
                double myLon = aMapLocation.getLongitude();

                Bundle bundle = new Bundle();
                bundle.putDouble("startLat", myLat);
                bundle.putDouble("startLon", myLon);
                bundle.putDouble("endLat", getAdidByTitle(marker.getTitle(),TYPE).getLatitude());
                bundle.putDouble("endLon", getAdidByTitle(marker.getTitle(),TYPE).getLongitude());

                Intent intent = new Intent(HomeActivity.this, NavigateActivity_.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        showingMarker = marker;
        aMap.animateCamera(CameraUpdateFactory.changeLatLng(marker.getPosition()));
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        showingMarker.hideInfoWindow();
    }


    public void setCollectView()
    {
        runonce = true;
        String strHead = aMapLocation.getProvince() + aMapLocation.getCity() + aMapLocation.getDistrict();
        Log.e("sfsdf", "he " + strHead);
        String addr = aMapLocation.getAddress().substring(strHead.length());
        edtMediaAdd.setText(addr);
        tvAreaContent.setText(aMapLocation.getProvince() + aMapLocation.getCity() + aMapLocation.getDistrict());

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (view.getId())
        {
            case R.id.spMediaType:
                Util.toastShotShow(HomeActivity.this, listMediaType.get(position).getValue());
                break;
            case R.id.spAreaType:
                Util.toastShotShow(HomeActivity.this, listAreaType.get(position).getValue());
                break;

        }
    }


    //根据当前位置获取广告列表
    public void loadAdByLocation(double longitude, double latitude)
    {
        String url;
        if(TYPE == 3)
        {
            url = Util.MAINURL + Util.doCheckGuanggaoMSG +
                    "JINGDU=" + aMapLocation.getLongitude() +
                    "&WEIDU=" + aMapLocation.getLatitude();
//            url = Util.MAINURL + Util.doCheckGuanggaoMSG +
//                    "JINGDU=121.377189&WEIDU=37.467389";
            OkHttpClientManager.getAsyn(url,this,Util.CHECKGUANGGAO);
        }
        else
        {
            url = Util.MAINURL + Util.doAdByLocation + "MTZL_JIGNDU=" + longitude +
                "&MTZL_WEIDU=" + latitude;

            OkHttpClientManager.getAsyn(url, this, Util.ADBYLOACTION);
        }
//        Log.e("url = ",url);

    }

    @Override
    public void onFailure(Request request, IOException e) {
        loading.dismiss();
        Util.toastShotShow(HomeActivity.this,"网络连接超时");
    }

    @Override
    public void onResponse(String response, int type) {

        if(type == Util.COLLECTAD)
        {
            loading.dismiss();
//            Log.e("res = ",response);
        }

//        Log.e("res = "+ type,response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            String reason = jsonObject.getString("reason");
            String rescode = jsonObject.getString("rescode");
            if(rescode.equals("1"))
            {
                switch (type)
                {
                    case Util.ADBYLOACTION:
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        listAdId.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject temp = (JSONObject) jsonArray.get(i);
                            double lng = Double.valueOf(temp.getString("MTZL_JIGNDU"));
                            double lat = Double.valueOf(temp.getString("MTZL_WEIDU"));
                            AdId adId = new AdId();
                            adId.setMTBH(temp.getString("MTZL_MTBH"));
                            adId.setMTID(temp.getString("MTZL_ID"));
                            adId.setLongitude(lng);
                            adId.setLatitude(lat);
                            adId.setLatLng(new LatLng(lat,lng));
                            listAdId.add(adId);
                        }
                        if(listAdId.size() == 0)
                        {
                            Util.toastLongShow(HomeActivity.this,"附近无广告牌。");
                        }
                        setAdMarker();
//                        getAdInfoList(listAdId);
                        break;
                    case Util.COLLECTAD:
                        Util.toastShotShow(HomeActivity.this, reason);
                        edtCount.setText("");
                        imgTakePic.setVisibility(View.VISIBLE);
                        imgTakedPic.setVisibility(View.GONE);
                        imgBase64 = null;
                        break;
                    case Util.CHECKGUANGGAO:
                        jsonArray = jsonObject.getJSONArray("result");
                        listAdId.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject temp = (JSONObject) jsonArray.get(i);
                            double lng = Double.valueOf(temp.getString("MTZL_JINGDU"));
                            double lat = Double.valueOf(temp.getString("MTZL_WEIDU"));
                            AdId adId = new AdId();
                            adId.setMTBH(temp.getString("FWBGMT_MTBH"));
                            adId.setMTID(temp.getString("FWBGMT_ID"));
                            adId.setLongitude(lng);
                            adId.setLatitude(lat);
                            adId.setLatLng(new LatLng(lat,lng));
                            listAdId.add(adId);
                        }
                        if(listAdId.size() == 0)
                        {
                            Util.toastLongShow(HomeActivity.this,"附近无需要审核的广告牌。");
                        }
                        setAdMarker();
                        break;


                }
            }
            else
            {
                if(type == Util.ADBYLOACTION)
                {
                    Util.toastShotShow(HomeActivity.this,reason);
                }
            }

        }
        catch (Exception e)
        {
            Util.toastShotShow(HomeActivity.this,"网络错误" + type);
        }


    }


    public String getAreaTypeID(String areaType)
    {
        for(int i = 0; i < listAreaType.size();i++)
        {
            KeyValue keyValue = listAreaType.get(i);
            if(areaType.equals(keyValue.getValue()))
                return keyValue.getKey();
        }
        return "";
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId())
        {
            case R.id.spMediaType:

                break;
            case R.id.spAreaType:
                Log.e("area type","select" + listAreaType.get(position).getValue());
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private AdId getAdidByTitle(String title,int type)
    {
        AdId adId = null;
        if(type == 3)
        {
            for(int i = 0;i < listAdId.size(); i++)
            {
                if(title.equals(listAdId.get(i).getMTID()))
                {
                    adId = listAdId.get(i);
                    return adId;
                }
            }
        }
        else
        {
            for(int i = 0;i < listAdId.size(); i++)
            {
                if(title.equals(listAdId.get(i).getMTBH()))
                {
                    adId = listAdId.get(i);
                    return adId;
                }
            }
        }
        return adId;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Util.toastLongShow(HomeActivity.this,"再按一次返回退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
