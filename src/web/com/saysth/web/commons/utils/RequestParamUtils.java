package com.saysth.web.commons.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.saysth.core.constants.ErrorCode;
import com.saysth.core.exceptions.ServiceException;

/**
 * @author RamosLi
 * 请求的参数工具，封装了取常用参数的方法。在controller里从request中取参数均采用此类封装的方法
 */
public class RequestParamUtils {
	public static String getRequiredString(HttpServletRequest request, String name) throws ServiceException {
		String result = getStringParam(request, name, null, true);//result not null
        if (StringUtils.isEmpty(result)) {
            throw new ServiceException(ErrorCode.PARAM_LACK_REQUIRED_PARAM, name);
        }
        return result;
	}
	public static String getString(HttpServletRequest request, String name, String defaultVal) throws ServiceException {
        String value = getStringParam(request, name, defaultVal, false);
        return StringUtils.isEmpty(value) ? defaultVal : value;
    }
	public static int getRequiredInt(HttpServletRequest request, String name) throws ServiceException {
        Integer i = getIntegerParam(request, name, 0, true);
        return i.intValue();
    }
	public static int getInt(HttpServletRequest request, String name, int defaultVal) throws ServiceException {
        Integer i = getIntegerParam(request, name, defaultVal, false);
        return i.intValue();
    }
	public static int getPositiveInt(HttpServletRequest request, String name) throws ServiceException {
        int result = getRequiredInt(request, name);
        if (result < 1) {
            throw new ServiceException(ErrorCode.PARAM_MUST_BE_POSITIVE_INTEGER, name);
        }
        return result;
    }
	public static long getRequiredLong(HttpServletRequest request, String name) throws ServiceException {
        Long i = getLongParam(request, name, null, true);
        return i.longValue();
    }
	public static long getLong(HttpServletRequest request, String name, long defaultVal) throws ServiceException {
        Long i = getLongParam(request, name, defaultVal, false);
        return i.longValue();
    }
	public static long getPositiveLong(HttpServletRequest request, String name) throws ServiceException {
        long result = getRequiredLong(request, name);
        if (result < 1) {
            throw new ServiceException(ErrorCode.PARAM_MUST_BE_POSITIVE_LONG, name);
        }
        return result;
    }
	public static double getRequiredDouble(HttpServletRequest request, String name) throws ServiceException {
		String result = getStringParam(request, name, null, true);//result not null
        if (StringUtils.isBlank(result)) {
            throw new ServiceException(ErrorCode.PARAM_LACK_REQUIRED_PARAM, name);
        }
        try {
			return Double.parseDouble(result);
		} catch (NumberFormatException e) {
			throw new ServiceException(ErrorCode.PARAM_MUST_BE_DOUBLE, name);
		}
	}
	public static double getDouble(HttpServletRequest request, String name, double defaultVal) throws ServiceException {
        String value = getStringParam(request, name, String.valueOf(defaultVal), false);
        try {
        	return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			throw new ServiceException(ErrorCode.PARAM_MUST_BE_DOUBLE, name);
		}
    }
	private static String getStringParam(HttpServletRequest request, String name, String defaultVal, boolean required) throws ServiceException {
        String value = request.getParameter(name);
        if (StringUtils.isEmpty(value)) {
            if (required) {
                throw new ServiceException(ErrorCode.PARAM_LACK_REQUIRED_PARAM, name);
            } else {
                return defaultVal;
            }
        }
        return value;
    }
	private static Integer getIntegerParam(HttpServletRequest request, String name, Integer defaultVal, boolean required) throws ServiceException {
		Long num = getLongParam(request, name, defaultVal.longValue(), required);
		return num.intValue();
	}
	private static Long getLongParam(HttpServletRequest request, String name, Long defaultVal, boolean required) throws ServiceException {
		String value = getStringParam(request, name, null, required);
		// val三种情况： NULL,空串，非空串，default值
		Long num = defaultVal;
		try {
			if (required) {
				num = Long.valueOf(value);
			} else {
				if (StringUtils.isNotEmpty(value)) {
					try {
						num = Long.valueOf(value);
					} catch (NumberFormatException e) {
						// 什么都不做，也不抛异常，结果等于 默认值
					}
				}
			}
		} catch (NumberFormatException e) {
			throw new ServiceException(ErrorCode.PARAM_INVALID_FORMAT, name);
		}
		return num;
	}
	public static void main(String[] args) {
		System.out.println(Double.parseDouble("2"));
	}
}
