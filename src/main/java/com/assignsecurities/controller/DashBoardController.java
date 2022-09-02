package com.assignsecurities.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.assignsecurities.bean.DashBoardCountBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.service.impl.DashBoardService;

@RestController
//@RequestMapping("/dashBoard")
public class DashBoardController extends BaseController {

	@Autowired
	private DashBoardService dashBoardService;
	
	@RequestMapping(value = "/myFavouriteCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public DashBoardCountBean getMyFavouriteCount() {
		UserLoginBean userLoginBean = getUser();
		return dashBoardService.getMyFavouriteCount(userLoginBean);
	}

	@RequestMapping(value = "/myApplicationsCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public DashBoardCountBean getMyApplicationCount() {
		UserLoginBean userLoginBean = getUser();
		return dashBoardService.getMyApplicationCount(userLoginBean);
	}

	@RequestMapping(value = "/myCasesCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public DashBoardCountBean getMyCasesCount() {
		UserLoginBean userLoginBean = getUser();
		return dashBoardService.getMyCasesCount(userLoginBean);
	}
}
