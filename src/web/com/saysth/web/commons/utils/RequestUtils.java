package com.saysth.web.commons.utils;

import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.saysth.web.commons.model.BaseModel;
import com.saysth.web.commons.model.ErrorResponseModel;

public class RequestUtils {
	private static final String RESPONSE_MODEL_KEY = "__response_model";
	public static Object fetchResponseModel(HttpServletRequest request) {
		return request.getAttribute(RESPONSE_MODEL_KEY);
	}
	public static void storeResponseModel(HttpServletRequest request, Object model) {
		request.setAttribute(RESPONSE_MODEL_KEY, model);
	}
	public static void storeSuccessModel(HttpServletRequest request) {
		storeResponseModel(request, BaseModel.SUCCESS_MODEL);
	}
	public static void createAndStoreResponseModel(HttpServletRequest request, int errorCode) {
		String[] array = null; // 编译器不乖，声明这个是为了哄她
		createAndStoreResponseModel(request, errorCode, array);
	}
	public static void createAndStoreResponseModel(HttpServletRequest request, int errorCode, String... array) {
		String message = "error";
		ErrorResponseModel model = new ErrorResponseModel();
		model.setResult(errorCode);
		model.setError_msg(message);
		storeResponseModel(request, model);
	}
	@SuppressWarnings("unchecked")
	public static StringBuilder getAllRequestParams(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder("[ ");
		Map<String, String[]> map = request.getParameterMap();
		if (map != null) {
			Set<Entry<String, String[]>> set = map.entrySet();
			if (set != null) {
				for (Entry<String, String[]> e : set) {
					sb.append(e.getKey());
					sb.append(" : ");
					sb.append(e.getValue()[0]);
					sb.append(" , ");
				}
			}
		}
		sb.append(" ]");
		return sb;
	}
	@SuppressWarnings("unchecked")
	public static StringBuilder getAllRequestHeaders(HttpServletRequest request) {
		Enumeration<String> enu = request.getHeaderNames();
		StringBuilder sb = new StringBuilder("[ ");
		while (enu.hasMoreElements()) {
			String key = enu.nextElement();
			sb.append(key);
			sb.append(" : ");
			sb.append(request.getHeader(key));
			sb.append(" , ");
		}
		sb.append(" ]");
		return sb;
	}
}
