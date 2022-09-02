package com.assignsecurities.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.assignsecurities.app.exception.ValidationException;
import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.bean.CaseCourierDetailBean;
import com.assignsecurities.bean.ValidationError;
import com.assignsecurities.service.impl.CaseCourierDetailService;

@RestController
public class CaseCourierDetailController extends BaseController {
	
	@Autowired
	private CaseCourierDetailService casecourierDetailService;
	
	@RequestMapping(value = "/getCaseCourierServiceCompanyDetail/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public CaseCourierDetailBean getCaseCourierServiceCompanyDetail(@PathVariable("id") Long id) {
			
		return casecourierDetailService.getCaseCourierDetailById(id);
	}
	
	@RequestMapping(value = "/addCaseCourierServiceDetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Long addCaseCourierServiceDetails(@Validated @RequestBody CaseCourierDetailBean bean, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
		
		List<ValidationError> errorList = new ArrayList<>();
		
//		if (Objects.nonNull(casecourierDetailService.getCaseCourierDetailById(id)ierDetailByCourierServiceName(bean.getName())))
//		{
//			String message = String.format("%s courier service company already exists. Please provide different name",  bean.getName());
//			errorList.add(ValidationError.builder().message(message).build());
//		}

		
		if(ArgumentHelper.isNotEmpty(errorList)) {
			throw new ValidationException(errorList);
		}
		return casecourierDetailService.add(bean);
	}
	
	@RequestMapping(value = "/updateCaseCourierServiceDetail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateCaseCourierServiceDetail(@Validated @RequestBody CaseCourierDetailBean bean, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
		
		List<ValidationError> errorList = new ArrayList<>();
//		CaseCourierDetailBean courierDetailBean = casecourierDetailService.getCaseCourierDetailById(bean.getId());
//		if(!courierDetailBean.getName().equalsIgnoreCase(bean.getName()))
//		{
//			if (Objects.nonNull(courierDetailService.getCourierDetailByCourierServiceName(bean.getName())))
//			{
//				String message = String.format("%s courier service company already exists. Please provide different name",  bean.getName());
//				errorList.add(ValidationError.builder().message(message).build());
//			}
//		}

		if(ArgumentHelper.isNotEmpty(errorList)) {
			throw new ValidationException(errorList);
		}
		casecourierDetailService.update(bean);
	}
	
	@RequestMapping(value = "/getAllCaseCourierServiceDetailByGivenCaseId", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CaseCourierDetailBean> getAllCourierServiceCompanyDetailsByGivenCaseId(Long caseId) {
		
		return casecourierDetailService.getAllCourierServiceDetailByGivenCaseId(caseId);
	}
	
	@RequestMapping(value = "/getAllCaseCourierServiceDetailByGivenCourierId", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CaseCourierDetailBean> getAllCaseCourierServiceDetailByGivenCourierId(Long couriereId) {
		
		return casecourierDetailService.getAllCaseCourierServiceDetailByGivenCourierId(couriereId);
	}
	
	
	

}
