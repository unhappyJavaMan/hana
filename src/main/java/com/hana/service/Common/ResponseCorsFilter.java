package com.hana.service.Common;

import com.hana.service.Utils.LogUtils;
import com.hana.service.Utils.Methods;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ResponseCorsFilter implements Filter {
	private static LogUtils logger = new LogUtils();

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
						 FilterChain chain) throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) req;
		final HttpServletResponse response = (HttpServletResponse) res;
		logger.info("ResponseCorsFilter start");

		logger.info("request.getHeader(Origin):" + request.getHeader("Origin"));
		logger.info("request.getHeader(Host):" + request.getHeader("Host"));
		logger.info("request.getHeader(Referer):"
				+ request.getHeader("Referer"));
		String allowedOrigin = "";
		if (request.getHeader("Origin") != null) {
			allowedOrigin = request.getHeader("Origin");
		}
		if ("".equals(allowedOrigin) && request.getHeader("Host") != null) {
			allowedOrigin = (request.isSecure() ? "https" : "http") + "://"
					+ request.getHeader("Host");
		}
		if ("".equals(allowedOrigin) && request.getHeader("Referer") != null) {
			allowedOrigin = request.getHeader("Referer");
			allowedOrigin = allowedOrigin.substring(0,
					allowedOrigin.length() - 1);
		}
		logger.info("allowedOrigin:" + allowedOrigin);
		response.setHeader("Access-Control-Allow-Origin", allowedOrigin);
		response.setHeader("Access-Control-Allow-Methods",
				"POST, PUT, GET, OPTIONS, DELETE");
		response.setHeader(
				"Access-Control-Allow-Headers",
				"Date, Content-Type, Accept, X-Requested-With, Authorization, From, X-Auth-Token, Request-Id");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Expose-Headers", "Set-Cookie");
		String requestId = Methods.getTransactionInvoiceString();
		request.setAttribute(Const.REQUEST_ID, requestId);

		if ("OPTIONS".equalsIgnoreCase(((HttpServletRequest) req).getMethod())) {
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
	}
}
