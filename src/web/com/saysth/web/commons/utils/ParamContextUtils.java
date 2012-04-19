package com.saysth.web.commons.utils;

import javax.servlet.http.HttpServletRequest;

import com.saysth.web.commons.model.ParamContext;

/**
 * @author RamosLi
 *
 */
public class ParamContextUtils {
	private static final String CONTEXT_KEY = "__param_context";
	public static ParamContext obtain(HttpServletRequest request) {
		ParamContext context = (ParamContext)request.getAttribute(CONTEXT_KEY);
		if (context == null) {
			// 不用考虑线程安全问题
			context = new ParamContext(request);
			request.setAttribute(CONTEXT_KEY, context);
		}
		return context;
	}
}
