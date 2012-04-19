package com.saysth.web.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.saysth.core.constants.ErrorCode;
import com.saysth.core.utils.SignUUIDUtil;
import com.saysth.web.commons.utils.RequestUtils;

/**
 * @author RamosLi
 * 签名认证
 */
@Component("signatureInterceptor")
public class SignatureInterceptor extends AbstractAPIInterceptor {
	
	protected final Log logger = LogFactory.getLog(SignatureInterceptor.class);

	
	@Override
	@SuppressWarnings("unchecked")
	public boolean before(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String sig = request.getParameter("sig");
		// spaceApp 不会为空
		String secret = "";
		
		Map<String, String[]> map = request.getParameterMap();
		String httpMethod = request.getMethod().toUpperCase();
	
		boolean isValid = SignUUIDUtil.validateSign(sig, secret,map);
		if (!isValid) {
			logger.error("Error sig!! all parameters: " + RequestUtils.getAllRequestParams(request));
			RequestUtils.createAndStoreResponseModel(request, ErrorCode.ERROR_SIG);
			return false;
		}
		return true;
	}
}
