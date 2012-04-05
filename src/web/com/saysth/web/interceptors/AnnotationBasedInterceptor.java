package com.saysth.web.interceptors;

import java.lang.annotation.Annotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 一个拦截器基类，用Annotation来控制拦截精度。
 * 
 *
 */
public abstract class AnnotationBasedInterceptor extends HandlerInterceptorAdapter {
	protected final Log logger = LogFactory.getLog(AnnotationBasedInterceptor.class);

	/**
	 * 下面是三个子类可以重写的方法，用于控制拦截精度：
	 * （1）子类如果不重写任何一个方法，则该拦截器默认拦截所有请求；
	 * （2）只要子类重写了其中一个方法，则该方法定义的规则就起作用；
	 * （3）getInterceptorExclusionAnnotationClass的优先级更高
	 */

	/**
	 * @return 需要拦截的Http Method，只要当前请求的Http Method与此方法返回的相同，就会被此拦截器拦截
	 * 注意：这里的用词Handler用的是Spring的说法，在之前的代码中，Handler普遍被称作Controller
	 */
	public String getInterceptorHttpMethod() {
		return null;
	}

	/**
	 * @return 需要标记在Handler类上（注意：方法暂时不支持）的Annotation的类型信息（Class对象），标记了这个Annotation的Handler就会被此拦截器拦截
	 */
	public Class<? extends Annotation> getInterceptorAnnotationClass() {
		return null;
	}
	/**
	 * @return 需要标记在Handler类上（注意：方法暂时不支持）的Annotation的类型信息（Class对象），标记了这个Annotation的Handler就不会被此拦截器拦截。
	 */
	public Class<? extends Annotation> getInterceptorExclusionAnnotationClass() {
		return null;
	}

	/**
	 * 子类拦截器请重写此方法，在Handler执行之前执行
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public boolean before(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		return true;
	}
	/**
	 * 子类拦截器请重写此方法，在Handler执行之后且ViewResolver执行之前执行
	 * @param request
	 * @param response
	 * @param modelAndView
	 * @throws Exception
	 */
	public void after(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (shouldIntercept(request, handler)) {
			if (logger.isInfoEnabled()) {
				logger.info("COMMON_INTERCEPTOR: preHandle " + this.getClass().getCanonicalName() + " -- about to call before intercepting..., hanlder = " + handler + " for URL: " + request.getRequestURI());
			}
			return before(request, response, handler);
		}
		//放过
		if (logger.isInfoEnabled()) {
			logger.info("COMMON_INTERCEPTOR: preHandle " + this.getClass().getCanonicalName() + " -- ignore before intercepting..., hanlder = " + handler + " for URL: " + request.getRequestURI());
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if (shouldIntercept(request, handler)) {
			if (logger.isInfoEnabled()) {
				logger.info("COMMON_INTERCEPTOR: postHandle " + this.getClass().getCanonicalName() + " -- about to call after intercepting..., hanlder = " + handler + " for URL: " + request.getRequestURI());
			}
			after(request, response, modelAndView);
			return;
		}
		if (logger.isInfoEnabled()) {
			logger.info("COMMON_INTERCEPTOR: postHandle " + this.getClass().getCanonicalName() + " -- ignore after intercepting..., hanlder = " + handler + " for URL: " + request.getRequestURI());
		}
	}

	/**
	 * @param handler
	 * @return 是否应该拦截该请求
	 */
	private boolean shouldIntercept(HttpServletRequest request, Object handler) {
		String interceptorHttpMethod = getInterceptorHttpMethod();
		Class<? extends Annotation> interceptorAnnotationClass = getInterceptorAnnotationClass();
		Class<? extends Annotation> interceptorExclusionAnnotationClass = getInterceptorExclusionAnnotationClass();

		//先判断getInterceptorExclusionAnnotationClass，如果有这个标注，则不再匹配，而不管其它条件了
		if (interceptorExclusionAnnotationClass != null) {
			//如果在Handler类层次的标注上匹配到Annotation，则进行拦截
			Annotation handlerAnnotation = handler.getClass().getAnnotation(interceptorExclusionAnnotationClass);
			if (logger.isInfoEnabled()) {
				logger.info("COMMON_INTERCEPTOR: shouldIntercept " + this.getClass().getCanonicalName() + " -- got handler exclusion annotation = " + handlerAnnotation + " for handler = " + handler + " for URL: " + request.getRequestURI());
			}
			if (handlerAnnotation != null) {
				return false;
			}
		}

		if (interceptorHttpMethod == null && interceptorAnnotationClass == null) {
			//几个方法子类都没有重写，则默认拦截所有
			if (logger.isInfoEnabled()) {
				logger.info("COMMON_INTERCEPTOR: shouldIntercept " + this.getClass().getCanonicalName() + " -- intercepting any request for handler = " + handler + " for URL: " + request.getRequestURI());
			}
			return true;
		}

		if (interceptorHttpMethod != null && interceptorHttpMethod.equalsIgnoreCase(request.getMethod())) {
			if (logger.isInfoEnabled()) {
				logger.info("COMMON_INTERCEPTOR: shouldIntercept " + this.getClass().getCanonicalName() + " -- request http-method matched for handler = " + handler + " for URL: " + request.getRequestURI());
			}
			return true;
		}

		if (interceptorAnnotationClass != null) {
			//如果在Handler类层次的标注上匹配到Annotation，则进行拦截
			Annotation handlerAnnotation = handler.getClass().getAnnotation(interceptorAnnotationClass);
			if (logger.isInfoEnabled()) {
				logger.info("COMMON_INTERCEPTOR: shouldIntercept " + this.getClass().getCanonicalName() + " -- got handler annotation = " + handlerAnnotation + " for handler = " + handler + " for URL: " + request.getRequestURI());
			}
			if (handlerAnnotation != null) {
				return true;
			}
		}

		//不满足拦截条件，直接放过
		return false;
	}
}
