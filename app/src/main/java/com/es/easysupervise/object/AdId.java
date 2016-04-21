package com.es.easysupervise.object;

import com.amap.api.maps.model.LatLng;

/**
 * Created by xiaoxi on 15-12-18.
 */
public class AdId {
    private String MTBH;
    private String MTID;
    private double longitude;
    private double latitude;
    private LatLng latLng;

    public String getMTBH() {
        return MTBH;
    }

    public void setMTBH(String MTBH) {
        this.MTBH = MTBH;
    }

    public String getMTID() {
        return MTID;
    }

    public void setMTID(String MTID) {
        this.MTID = MTID;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
