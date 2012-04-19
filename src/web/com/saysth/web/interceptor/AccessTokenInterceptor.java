package com.saysth.web.interceptor;

import java.lang.annotation.Annotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.mysql.jdbc.log.LogUtils;
import com.saysth.core.constants.ErrorCode;
import com.saysth.core.exceptions.ServiceException;
import com.saysth.core.utils.ParamContextUtils;
import com.saysth.web.annotation.AccessTokenNeedNotIntercept;
import com.saysth.web.commons.model.ParamContext;
import com.saysth.web.commons.utils.RequestUtils;

/**
 * @author RamosLi
 * access_token拦截器
 */
@Component("accessTokenInterceptor")
public class AccessTokenInterceptor extends AbstractAPIInterceptor {
	
	protected final Log logger = LogFactory.getLog(AccessTokenInterceptor.class);

	
	@Override
	public Class<? extends Annotation> getInterceptorExclusionAnnotationClass() {
		return AccessTokenNeedNotIntercept.class;
	}
	
	@Override
	public boolean before(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String accessToken = request.getParameter("access_token");
		UserAccessToken userAccessToken = null;
		try {
			userAccessToken = lUserAccessTokenService.verify(accessToken);
		} catch (ServiceException e) {
			RequestUtils.createAndStoreResponseModel(request, e.getCode());
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (userAccessToken == null) {
			logger.error("UserAccessToken is null. for access_token: " + accessToken);
			RequestUtils.createAndStoreResponseModel(request, ErrorCode.SERVER_BUSY);
			return false;
		}
		ParamContext context = ParamContextUtils.obtain(request);
		context.setUserId(userAccessToken.getUserId());
		return true;
	}
}
