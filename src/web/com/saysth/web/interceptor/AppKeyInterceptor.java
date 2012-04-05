package com.saysth.web.interceptor;

import java.lang.annotation.Annotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.saysth.web.annotation.AppKeyNeedIntercept;

/**
 * @author RamosLi
 * 检查app_key是否合法
 */
@Component("appKeyInterceptor")
public class AppKeyInterceptor extends AbstractAPIInterceptor {
	
	@Override
	public Class<? extends Annotation> getInterceptorAnnotationClass() {
		return AppKeyNeedIntercept.class;
	}
	
	@Override
	public boolean before(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//		String appKey = request.getParameter("app_key");
//		LoveSpaceApp spaceApp = LoveSpaceAppManager.getInstance().get(appKey);
//		if (spaceApp == null) {
//			RequestUtils.createAndStoreResponseModel(request, ErrorCode.UNKNOW_APP_KEY);
//			return false;
//		}
//		// 放进去后面要用到
//		ParamContext context = ParamContextUtils.obtain(request);
//		context.setAppId(spaceApp.getAppId());
//		context.setLoveSpaceApp(spaceApp);
		
		return true;
	}
}
