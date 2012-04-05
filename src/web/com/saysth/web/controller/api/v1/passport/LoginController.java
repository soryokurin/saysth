package com.saysth.web.controller.api.v1.passport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dbay.component.welove.ServiceException;
import com.dbay.component.welove.model.LoveSpaceApp;
import com.dbay.component.welove.model.user.UserAccessToken;
import com.dbay.component.welove.service.ILoveSpaceService;
import com.dbay.component.welove.service.IUserAccessTokenService;
import com.dbay.component.welove.service.IPassportService;
import com.saysth.web.annotation.AccessTokenNeedNotIntercept;
import com.saysth.web.annotation.ActiveLoveSpaceNotRequired;
import com.saysth.web.annotation.AppKeyNeedIntercept;
import com.saysth.web.commons.model.ParamContext;
import com.saysth.web.commons.model.PassportModel;
import com.saysth.web.commons.utils.ParamContextUtils;
import com.saysth.web.commons.utils.RequestParamUtils;
import com.saysth.web.commons.utils.RequestUtils;
import com.saysth.web.controller.api.AbstractAPIController;
/**
 * @author RamosLi
 * 登录
 */
@AppKeyNeedIntercept
@AccessTokenNeedNotIntercept
@Controller("loginController")
public class LoginController extends AbstractAPIController {
	
	@Autowired
	private IPassportService passportService;
	
	
	@Autowired
	private IUserAccessTokenService userAccessTokenService;
	
	@RequestMapping(value = { "/v1/passport/login" }, method = { RequestMethod.POST })
	public void login(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		String email = RequestParamUtils.getRequiredString(request, "email");
		String password = RequestParamUtils.getRequiredString(request, "password");// 经过第一次加密后的密文
		ParamContext context = ParamContextUtils.obtain(request);
		String ip = context.getClientIp();
		long userId = passportService.login(email, password, ip);
		LoveSpaceApp spaceApp = context.getSpaceApp();
		long appId = spaceApp.getAppId();
		UserAccessToken token = userAccessTokenService.createAccessToken(userId, appId);
		// 生成Model
		PassportModel model = new PassportModel();
		model.setUser_id(userId);
		model.setAccess_token(token.getAccessToken());
		model.setExpire_in(token.getExpireIn());
		RequestUtils.storeResponseModel(request, model);
	}
}
