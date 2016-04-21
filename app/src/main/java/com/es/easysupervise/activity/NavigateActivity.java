package com.es.easysupervise.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.es.easysupervise.R;
import com.es.easysupervise.uil.TTSController;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_navigate)
public class NavigateActivity extends Activity implements AMapNaviListener,
        AMapNaviViewListener
{
    @ViewById
    AMapNaviView naviView;
    @ViewById
    TextView tvTitle;


    AMapNavi aMapNavi;
    TTSController ttsManager;
    NaviLatLng endLatlng;
    NaviLatLng startLatlng;
    List<NaviLatLng> startList = new ArrayList<NaviLatLng>();
    List<NaviLatLng> endList = new ArrayList<NaviLatLng>();
    List<NaviLatLng> wayPointList;

    @AfterViews
    void init()
    {
        tvTitle.setText("导航");

        openGPSSettings();

        //为了尽最大可能避免内存泄露问题，建议这么写
        ttsManager = TTSController.getInstance(NavigateActivity.this);
        ttsManager.init();
        ttsManager.startSpeaking();

        //为了尽最大可能避免内存泄露问题，建议这么写
        aMapNavi = AMapNavi.getInstance(NavigateActivity.this);
        aMapNavi.setAMapNaviListener(this);
        aMapNavi.setAMapNaviListener(ttsManager);
        aMapNavi.setEmulatorNaviSpeed(150);

        Bundle bundle = getIntent().getExtras();
        startLatlng = new NaviLatLng(bundle.getDouble("startLat"),
                bundle.getDouble("startLon"));
        endLatlng = new NaviLatLng(bundle.getDouble("endLat"),
                bundle.getDouble("endLon"));

        naviView.onCreate(null);
        naviView.setAMapNaviViewListener(this);
        aMapNavi.startGPS();



    }

    @Click
    void imgLeft()
    {
        this.finish();
    }


    private void openGPSSettings() {
        LocationManager alm = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        if (alm
                .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "GPS模块正常", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        Toast.makeText(this, "请开启GPS！", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
        startActivityForResult(intent, 0); //此为设置完成后返回到获取界面

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        naviView.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        naviView.onResume();
        startList.add(startLatlng);
        endList.add(endLatlng);
    }

    @Override
    protected void onPause() {
        super.onPause();
        naviView.onPause();

//        仅仅是停止你当前在说的这句话，一会到新的路口还是会再说的
        ttsManager.stopSpeaking();
//
//        停止导航之后，会触及底层stop，然后就不会再有回调了，但是讯飞当前还是没有说完的半句话还是会说完
//        aMapNavi.stopNavi();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        naviView.onDestroy();
        aMapNavi.destroy();
        ttsManager.destroy();
    }

    @Override
    public void onInitNaviFailure() {

        Log.e("onInitNaviFailure","error info ");
    }

    @Override
    public void onInitNaviSuccess() {
        boolean b = aMapNavi.calculateDriveRoute(startList,endList, wayPointList, 5);
        Log.e("onInitNaviSuccess = ","" + b);
    }

    @Override
    public void onStartNavi(int type) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation location) {

    }

    @Override
    public void onGetNavigationText(int type, String text) {

    }

    @Override
    public void onEndEmulatorNavi() {
    }

    @Override
    public void onArriveDestination() {
    }

    @Override
    public void onCalculateRouteSuccess() {
        //AMapNavi.EmulatorNaviMode 模拟导航
        //AMapNavi.GPSNaviMode 真实导航
        boolean b = aMapNavi.startNavi(AMapNavi.GPSNaviMode);
        Log.e("b = ","" + b);
    }

    @Override
    public void onCalculateRouteFailure(int errorInfo) {
        Log.e("calculate FAILE","error info " + errorInfo);
    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int wayID) {

    }

    @Override
    public void onGpsOpenStatus(boolean enabled) {
    }

    @Override
    public void onNaviSetting() {
    }

    @Override
    public void onNaviMapMode(int isLock) {

    }

    @Override
    public void onNaviCancel() {
        finish();
    }


    @Override
    public void onNaviTurnClick() {

    }

    @Override
    public void onNextRoadClick() {

    }


    @Override
    public void onScanViewButtonClick() {
    }

    @Deprecated
    @Override
    public void onNaviInfoUpdated(AMapNaviInfo naviInfo) {
    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviinfo) {
    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {
    }

    @Override
    public void hideCross() {
    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] laneInfos, byte[] laneBackgroundInfo, byte[] laneRecommendedInfo) {

    }

    @Override
    public void hideLaneInfo() {

    }


    @Override
    public void onLockMap(boolean isLock) {
    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }
}
