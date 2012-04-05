package com.saysth.web.commons.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class ResponseUtils {
	
	protected final static Logger logger = Logger.getLogger(ResponseUtils.class);

	public static void push2client(HttpServletResponse response, String result) {
		try {
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Content-Length", String.valueOf(result.getBytes("UTF-8").length));
			response.setContentType("application/json; charset=UTF-8");
			PrintWriter writer = response.getWriter();
			writer.write(result);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			logger.error("Push2Client failed! The data is :\r\n"
					+ result + " \r\n Exception:", e);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
