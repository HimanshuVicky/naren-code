package com.assignsecurities.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.bean.UserLoginBean;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class BaseController {

	@Autowired
	protected HttpServletRequest request;

	@Autowired
	private ServletContext servletContext;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ResponseEntity<Void> getResponseEntity(Map<String, Object> jsonResponse) {
		return new ResponseEntity(jsonResponse, HttpStatus.OK);
	}

	public UserLoginBean getUser() {
		HttpSession session = request.getSession(true);
		return (UserLoginBean) session.getAttribute(AppConstant.USER_CONTEXT);
	}
}
