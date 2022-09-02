package com.assignsecurities.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.exception.ValidationException;
import com.assignsecurities.bean.CaseReportBean;
import com.assignsecurities.bean.MyActionReferralBean;
import com.assignsecurities.bean.ReportBean;
import com.assignsecurities.bean.ScriptSearchLogBean;
import com.assignsecurities.bean.SearchReferralBean;
import com.assignsecurities.bean.SearchUserBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.bean.ValidationError;
import com.assignsecurities.service.impl.ReportService;

@RestController
public class ReportController extends BaseController {

	@Autowired
	private ReportService reportService;

	@RequestMapping(value = "/getScriptLogsReport", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ScriptSearchLogBean> getScriptLogsReport(@Validated @RequestBody ReportBean searchBean, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
		UserLoginBean userLoginBean = getUser();
		if (!(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN)
				|| userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_FRANCHISE))) {
			return Collections.emptyList();
		}
		return reportService.getScriptLogsReport(searchBean, userLoginBean);
	}

	@RequestMapping(value = "/getCaseReport", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CaseReportBean> getCaseReport(@Validated @RequestBody ReportBean searchBean, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
		UserLoginBean userLoginBean = getUser();
		if (!(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN)
				|| userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_CC)
				|| userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_FRANCHISE)
				|| userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_FRANCHISE_USER))) {
			return Collections.emptyList();
		}
		return reportService.getCaseReport(searchBean, userLoginBean);
	}
	
	@RequestMapping(value = "/getUserReport", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<SearchUserBean> getUserReport(@Validated @RequestBody ReportBean searchBean, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
		UserLoginBean userLoginBean = getUser();
		if (!(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN)
				|| userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_CC))) {
			return Collections.emptyList();
		}
		return reportService.getUserReport(searchBean, userLoginBean);
	}
	
	@RequestMapping(value = "/getReferralReport", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<SearchReferralBean> getReferralReport(@Validated @RequestBody ReportBean searchBean, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
		UserLoginBean userLoginBean = getUser();
		return reportService.getReferralReport(searchBean, userLoginBean);
	}
	
	@RequestMapping(value = "/getESignReport", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<MyActionReferralBean> getESignReport(@Validated @RequestBody ReportBean searchBean, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
		UserLoginBean userLoginBean = getUser();
		if (!userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN)) {
			return Collections.emptyList();
		}
		return reportService.getMyActionReport(userLoginBean);
	}
	
	
}
