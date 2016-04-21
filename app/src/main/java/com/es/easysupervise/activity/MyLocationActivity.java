package com.es.easysupervise.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.es.easysupervise.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * 我的位置
 */
@EActivity(R.layout.activity_my_location)
public class MyLocationActivity extends AppCompatActivity implements LocationSource,
        AMapLocationListener
{

    @ViewById
    MapView mapView;
    @ViewById
    TextView tvTitle;
    @ViewById
    TextView tvMyLocation;

    AMap aMap;
    public AMapLocationClient aMapLocationClient;
    AMapLocationClientOption mLocationOption;
    OnLocationChangedListener mListener;
    AMapLocation aMapLocation;

    @AfterViews
    void init()
    {
        tvTitle.setText("我的位置");
        mapView.onCreate(null);
        aMap = mapView.getMap();

        aMap.setLocationSource(this);
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);
        aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        this.aMapLocation = aMapLocation;
        mListener.onLocationChanged(aMapLocation);
        tvMyLocation.setText(aMapLocation.getAddress());
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (aMapLocationClient == null) {
            aMapLocationClient = new AMapLocationClient(getApplicationContext());
            aMapLocationClient.setLocationListener(this);

            initLocationOption();
            aMapLocationClient.setLocationOption(mLocationOption);
            aMapLocationClient.startLocation();

        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (aMapLocationClient != null) {
            aMapLocationClient.stopLocation();
            aMapLocationClient.onDestroy();
        }
        aMapLocationClient = null;

    }
    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
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
        mLocationOption.setInterval(2000);
    }
    @Click
    void imgLeft()
    {
        this.finish();
    }
}
