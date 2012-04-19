package com.saysth.core.utils;

import org.apache.log4j.Logger;

import com.saysth.web.commons.model.UserPosition;


/**
 * 通过经纬度计算两个人之间的距离
 */
public class PositionUtil {

    private static Logger logger=Logger.getLogger(PositionUtil.class);
    public static double MIN_DISTANCE =0.02;

    //得到两人间的距离
    public static double getDistance(double lat1, double lat2, double lon1, double lon2) {
        double R = 6378.137; // WGS-84
        double distance = 0.0;
        double dLat = (lat2 - lat1) * Math.PI / 180;
        double dLon = (lon2 - lon1) * Math.PI / 180;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1 * Math.PI / 180)
                * Math.cos(lat2 * Math.PI / 180) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        distance = (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))) * R;
        logger.debug(lat1+","+lon1+","+lat2+","+lon2+   "------"+ "distance:"+distance);
        return distance;
    }

    //判断两人是否在一定距离内
    public static boolean isInRange(double lat1, double lat2, double lon1, double lon2) {
        if (lat1 == -1.0 && lat2 == -1.0) {
            return true;
        }
        if (getDistance(lat1, lat2, lon1, lon2) < MIN_DISTANCE) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isInRange(UserPosition p1, UserPosition p2) {
        return isInRange(p1.getLatitude(), p2.getLatitude(), p1.getLongitude(), p2.getLongitude());
    }


}
