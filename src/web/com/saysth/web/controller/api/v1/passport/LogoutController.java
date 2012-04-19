package com.saysth.web.controller.api.v1.passport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dbay.component.welove.ServiceException;
import com.dbay.component.welove.service.IDeviceTokenService;
import com.dbay.component.welove.service.IUserAccessTokenService;
import com.saysth.web.annotation.ActiveLoveSpaceNotRequired;
import com.saysth.web.commons.model.ParamContext;
import com.saysth.web.commons.utils.ParamContextUtils;
import com.saysth.web.commons.utils.RequestUtils;
import com.saysth.web.controller.api.AbstractAPIController;


@Controller("logoutController")
public class LogoutController extends AbstractAPIController {
	
	@Autowired
	private IUserAccessTokenService userAccessTokenService;
	
	@Autowired
	private IDeviceTokenService deviceTokenService;
	
	@RequestMapping(value = { "/v1/passport/logout" }, method = { RequestMethod.POST })
	public void logout(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		ParamContext context = ParamContextUtils.obtain(request);
		long userId = context.getUserId();
		
		// 删除token
		userAccessTokenService.delete(userId);
		
		// 删除设备ID，登出后就不推送通知了
		deviceTokenService.delete(userId);
		
		RequestUtils.storeSuccessModel(request);
	}
}
