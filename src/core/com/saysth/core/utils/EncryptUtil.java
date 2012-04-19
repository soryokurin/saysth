/**
 * 
 */
package com.saysth.core.utils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ArrayUtils;


/**
 * @version 2008-12-1
 * @author <a href="mailto:zixue@taobao.com">zixue</a>
 * 
 */
public class EncryptUtil {
	/**
	 * 生成有效签名
	 * 
	 * @param params
	 * @param secret
	 * @return
	 * @throws Exception 
	 */
	public static String signature(Map<String, Object> params, String secret,
			boolean isHMac, String signName) throws Exception {
		params.remove(signName);
		String[] names = params.keySet().toArray(ArrayUtils.EMPTY_STRING_ARRAY);
		Arrays.sort(names);
		StringBuilder sb = new StringBuilder();
		// append if not hmac
		if (!isHMac) 
		{
			sb.append(secret);
		}
		
		for (int i = 0; i < names.length; i++) 
		{
			String name = names[i];
			sb.append(name);
			sb.append(params.get(name));
		}
		
		if (!isHMac)
		{
			sb.append(secret);
		}
		
		String sign = null;
		try {
			if(isHMac) {
				//hmac
				sign = byte2hex(encryptHMAC(sb.toString().getBytes("utf-8"), secret.getBytes("utf-8")));
			} else {
				//md5
			sign = DigestUtils.md5Hex(sb.toString().getBytes("utf-8"))
					.toUpperCase();
			}
		} catch (UnsupportedEncodingException e) {
			throw new java.lang.RuntimeException(e);
		}
		return sign;
	}
	/**
	 * 生成有效签名
	 * 
	 * @param params
	 * @param secret
	 * @return
	 * @throws Exception 
	 */
	public static String signature2(Map<String, Object> params, String secret,
			boolean appendSecret, boolean isHMac, String signName) throws Exception {
		params.remove(signName);
		String[] names = params.keySet().toArray(ArrayUtils.EMPTY_STRING_ARRAY);
		Arrays.sort(names);
		StringBuilder sb = new StringBuilder();
		// append if not hmac
		if (!isHMac) {
			sb.append(secret);
		}
		for (int i = 0; i < names.length; i++) {
			String name = names[i];
			sb.append(name);
			sb.append(params.get(name));
		}
		if (appendSecret && !isHMac) {
			sb.append(secret);
		}
		String sign = null;
		try {
			if(isHMac) {
				//hmac
				sign = byte2hex(encryptHMAC(sb.toString().getBytes("utf-8"), secret.getBytes("utf-8")));
			} else {
				//md5
			sign = DigestUtils.md5Hex(sb.toString().getBytes("utf-8"))
					.toUpperCase();
			}
		} catch (UnsupportedEncodingException e) {
			throw new java.lang.RuntimeException(e);
		}
		return sign;
	}
	/**
	 * HMAC加密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception 
	 * @throws Exception
	 */
	public static byte[] encryptHMAC(byte[] data, byte[] key) throws Exception  {
		SecretKey secretKey = new SecretKeySpec(key, "HmacMD5");
		Mac mac = Mac.getInstance(secretKey.getAlgorithm());
		mac.init(secretKey);
		return mac.doFinal(data);

	}

	/**
	 * 二行制转字符串
	 * 
	 * @param b
	 * @return
	 */
	private static String byte2hex(byte[] b) {
		StringBuffer hs = new StringBuffer();
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs.append("0").append(stmp);
			else
				hs.append(stmp);
		}
		return hs.toString().toUpperCase();
	}
	
	
	public static void  main(String[] args) throws Exception
	{
		Map<String,Object> params = new TreeMap<String,Object>();
		params.put("timestamp", "1311747035940");
		params.put("app_key", "12016086");
		
		String secretcode = "648033533a99d28482a5cba3ed6f7270";
		
		String sign = EncryptUtil.signature2(params, secretcode, true, false, "sign");
		
		
		System.out.println(sign);
	}
}
