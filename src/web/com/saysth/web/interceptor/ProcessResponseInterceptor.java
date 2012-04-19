package com.saysth.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.JsonSerializer;
import com.saysth.web.commons.utils.RequestUtils;
import com.saysth.web.commons.utils.ResponseUtils;

/**
 * @author RamosLi
 * 处理返回数据拦截器。此拦截器的目的为将javabean序列化成json，然后返回给client端
 * TODO 更好的方法是拿到modelandview后，用自定义的view生成器生成json然后返回，不过嘛，目前对spring还不是特熟悉，暂时这么着吧
 */
@Component("processResponseInterceptor")
public class ProcessResponseInterceptor extends AbstractAPIInterceptor {
	
	@Override
	public boolean before(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//		TimeCostUtils.init();
//		TimeCostUtils.record("Interceptor_begin");
		return true;
	}
	
	@Override
	public void after(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
//		modelAndView
	}
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		// 取出结果对象
		Object object = RequestUtils.fetchResponseModel(request);
		// 序列化成json
		if (object == null) {
			logger.error("response model is NULL!!! all request parameters: " + RequestUtils.getAllRequestParams(request));
			return;
		}
		Gson gson = new Gson();
		String result = gson.toJson(object);
//		TimeCostUtils.record("Interceptor_end");
//		System.out.println("TimeCostUtils " + TimeCostUtils.output());
//		TimeCostUtils.destroy();
//		System.out.println("============" + DateTimeUtils.format(System.currentTimeMillis()) + "========" + request.getRequestURI());
		System.out.println("All Request Params: " + RequestUtils.getAllRequestParams(request));
		System.out.println("Response: " + result);
		// 推给客户端
		ResponseUtils.push2client(response, result);
	}
	
}
