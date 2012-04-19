package com.saysth.core.utils;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SignUUIDUtil {

    /**
     * 生成uuid
     */
    public static String generateUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

     /**
     * 生成sign验证串
     * 因iphone的base64复杂，暂时只是MD5
     */
    public static String getSignature(String parameters, String secret) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
//        byte[] digest = md.digest((parameters + secret).getBytes());

        // 对运算结果做BASE64运算并加密

//        BASE64Encoder encode = new BASE64Encoder();
//        encode.encode(digest);
        //TODO 因iphone手机客户端暂时无法base64,现阶段只返回MD5
        StringBuffer result = new StringBuffer();
        try {
            for (byte b : md.digest((parameters + secret).getBytes("UTF-8"))) {
                result.append(Integer.toHexString((b & 0xf0) >>> 4));
                result.append(Integer.toHexString(b & 0x0f));
            }
        } catch (UnsupportedEncodingException e) {
            for (byte b : md.digest((parameters + secret).getBytes())) {
                result.append(Integer.toHexString((b & 0xf0) >>> 4));
                result.append(Integer.toHexString(b & 0x0f));
            }
        }
        return result.toString();
    }



   /**
     * 做param的sign验证
     * 将param按字典排序后再md5
     */
    public static boolean validateSign(String sign, String secret,Map<String,String[]> paramMap) {
        if (sign == null || "".equals(sign)) {
            return false;
        }
        StringBuffer stringBuffer = new StringBuffer();
        List<String> params = new ArrayList();

        for (Map.Entry<String, String[]> m : paramMap.entrySet()) {
            
                String value = "";
                for (String s : m.getValue()) {
                    value += s;
                }
                params.add(m.getKey() + "=" + value);
            
        }

        Collections.sort(params);
        for (String param : params) {
            stringBuffer.append(param);
        }
        return sign.equals(SignUUIDUtil.getSignature(stringBuffer.toString(), secret));
    }
}
