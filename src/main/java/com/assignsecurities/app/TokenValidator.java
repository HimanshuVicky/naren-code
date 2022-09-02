package com.assignsecurities.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.assignsecurities.app.util.Util;
import com.assignsecurities.bean.SecuritySessionBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.service.impl.LoginService;

@Component
@Order(3)
public class TokenValidator implements Filter {
	@Autowired
	private LoginService loginService;

	private static List<String> allowedUrls = new ArrayList<>();

	static {
		allowedUrls.add("doAuth");
		allowedUrls.add("notAuthorised");
		allowedUrls.add("swagger-ui");
		allowedUrls.add("v2");
		allowedUrls.add("resources");
		allowedUrls.add("getCityList");
		allowedUrls.add("resetPin");
		allowedUrls.add("registerCustomer");
		allowedUrls.add("validateMobileNumber");
		allowedUrls.add("getCityListForGivenSateCode");
		allowedUrls.add("getStateList");
		allowedUrls.add("validateMobileNumber");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		String requestURI = request.getRequestURI();
		HttpSession session = request.getSession(true);
//		System.out.println("requestURI=======>"+requestURI);
//		if (session.getAttribute(AppConstant.USER_CONTEXT) == null) {
		String CSRFToken = request.getHeader("X-Auth-Token");
		String userAgent = request.getHeader("User-Agent");
//		System.out.println("userAgent=================>"+userAgent);
		if(!userAgent.contains("Mozilla")) {
			response.setStatus(HttpStatus.SC_UNAUTHORIZED);
			response.getWriter().write("Your are not authorised or token expired, Please re-login and continue.");
			return;
		}
		if (CSRFToken != null) {
			SecuritySessionBean securitySessionBean = loginService.getSecuritySession(CSRFToken);
			if (securitySessionBean != null) {
				UserLoginBean userLoginBean = loginService.getUserLogin(securitySessionBean.getUserId());
				session.setAttribute(AppConstant.USER_CONTEXT, userLoginBean);
				session.setAttribute(AppConstant.USER_TOKEN, CSRFToken);
			} else {
				session.removeAttribute(AppConstant.USER_CONTEXT);
				session.removeAttribute(AppConstant.USER_TOKEN);
//					if (!(requestURI.contains("doAuth") || requestURI.contains("notAuthorised")
//							|| requestURI.contains("swagger-ui")
//							|| requestURI.contains("v2")
//							|| requestURI.contains("resources"))) {
				if (!Util.valueContainsInTheList(allowedUrls, requestURI)) {
//					System.out.println("invlid CSRFToken=================");
//						response.sendRedirect("notAuthorised");
//						return;
//						throw new NotAuthorisedException("Your are not authorised or token expired, Please re-login and continue.");
					// custom error response class used across my project
					response.setStatus(HttpStatus.SC_UNAUTHORIZED);
					response.getWriter()
							.write("Your are not authorised or token expired, Please re-login and continue.");
					return;
				}
			}
		} else {
			System.out.println("111111*******************null CSRFToken=================");
//				if (!(requestURI.contains("doAuth") || requestURI.contains("notAuthorised")
//						|| requestURI.contains("swagger-ui")
//						|| requestURI.contains("v2")
//						|| requestURI.lastIndexOf("/") == (requestURI.length() - 1)
//						|| requestURI.contains("resources"))) {
			if (!Util.valueContainsInTheList(allowedUrls, requestURI)) {
//				System.out.println("invlid CSRFToken=================");
//					response.sendRedirect("notAuthorised");
//					return;
//					throw new NotAuthorisedException("Your are not authorised or token expired, Please re-login and continue.");
				response.setStatus(HttpStatus.SC_UNAUTHORIZED);
				response.getWriter().write("Your are not authorised or token expired, Please re-login and continue.");
				return;
			} else {
				System.out.println("????????????????????");
			}
		}
//		}
		filterChain.doFilter(request, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, filterConfig.getServletContext());
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
}
