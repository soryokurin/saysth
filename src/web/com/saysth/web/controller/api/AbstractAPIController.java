package com.saysth.web.controller.api;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.saysth.core.constants.ErrorCode;
import com.saysth.core.exceptions.ServiceException;
import com.saysth.web.commons.utils.RequestUtils;

/**
 * @author RamosLi
 * 预留一个抽象的Controller
 * 可以把一些公用的逻辑放到里面
 */
public abstract class AbstractAPIController {
	protected Log logger = LogFactory.getLog(AbstractAPIController.class);
	
	@ExceptionHandler
	public void exception(HttpServletRequest request, Exception e) {
		if (e instanceof ServiceException) {
			ServiceException se = (ServiceException)e;
			RequestUtils.createAndStoreResponseModel(request, se.getCode());
		} else {
			// 其他未知错误
			e.printStackTrace();
			RequestUtils.createAndStoreResponseModel(request, ErrorCode.SERVER_BUSY);
		}
	}
}
