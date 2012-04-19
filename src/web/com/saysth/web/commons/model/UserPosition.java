package com.saysth.web.commons.model;

import java.io.Serializable;

/**
 * 用户地理位置信息
 */
public class UserPosition implements Serializable {

    private int uid;
    private double latitude;
    private double longitude;


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "UserPosition{" +
                "uid=" + uid +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
