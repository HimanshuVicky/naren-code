package com.assignsecurities.service.impl.core;

import javax.servlet.http.HttpServletRequest;

public interface RequestService {
	
	String getClientIp(HttpServletRequest request);
	
}