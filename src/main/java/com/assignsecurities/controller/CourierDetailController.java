package com.assignsecurities.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
import com.assignsecurities.bean.CourierDetailBean;
import com.assignsecurities.bean.ValidationError;
import com.assignsecurities.service.impl.CourierDetailService;

@RestController
public class CourierDetailController extends BaseController {
	@Autowired
	private CourierDetailService courierDetailService;
	
	@RequestMapping(value = "/getCourierServiceCompanyDetail/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public CourierDetailBean getCourierServiceCompanyDetail(@PathVariable("id") Long id) {
			
		return courierDetailService.getCourierDetailById(id);
	}
	
	@RequestMapping(value = "/addCourierServiceCompanyDetail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Long addCourierServiceCompanyDetail(@Validated @RequestBody CourierDetailBean bean, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
		
		List<ValidationError> errorList = new ArrayList<>();
		
		if (Objects.nonNull(courierDetailService.getCourierDetailByCourierServiceName(bean.getName())))
		{
			String message = String.format("%s courier service company already exists. Please provide different name",  bean.getName());
			errorList.add(ValidationError.builder().message(message).build());
		}

		
		if(ArgumentHelper.isNotEmpty(errorList)) {
			throw new ValidationException(errorList);
		}
		return courierDetailService.add(bean);
	}
	
	@RequestMapping(value = "/updateCourierServiceCompanyDetail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateCourierServiceCompanyDetail(@Validated @RequestBody CourierDetailBean bean) {
		
		List<ValidationError> errorList = new ArrayList<>();
		CourierDetailBean courierDetailBean = courierDetailService.getCourierDetailById(bean.getId());
		if(!courierDetailBean.getName().equalsIgnoreCase(bean.getName()))
		{
			if (Objects.nonNull(courierDetailService.getCourierDetailByCourierServiceName(bean.getName())))
			{
				String message = String.format("%s courier service company already exists. Please provide different name",  bean.getName());
				errorList.add(ValidationError.builder().message(message).build());
			}
		}

		if(ArgumentHelper.isNotEmpty(errorList)) {
			throw new ValidationException(errorList);
		}
		 courierDetailService.update(bean);
	}
	
	@RequestMapping(value = "/getAllCourierServiceCompanyDetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CourierDetailBean> getAllCourierServiceCompanyDetails() {
		
		return courierDetailService.getAllCourierDetail();
	}
	
}
