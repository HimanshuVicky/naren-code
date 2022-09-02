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
import com.assignsecurities.bean.ApplicationFeatureBean;
import com.assignsecurities.bean.ValidationError;
import com.assignsecurities.service.impl.ApplicationFeatureService;

@RestController
public class ApplicationFeatureController extends BaseController {
	
	@Autowired
	private ApplicationFeatureService applicationFeatureService;
	
	@RequestMapping(value = "/getApplicationFeature/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ApplicationFeatureBean gerApplicationFeature(@PathVariable("id") Long id) {
			
		return applicationFeatureService.getApplicationFeatureById(id);
	}
	
	@RequestMapping(value = "/addApplicationFeature", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Long addApplicationFeature(@Validated @RequestBody ApplicationFeatureBean bean, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
		
		List<ValidationError> errorList = new ArrayList<>();
		
		if (Objects.nonNull(applicationFeatureService.getApplicationFeatureByName(bean.getName())))
		{
			String message = String.format("Application feature  %s is already exists. Please provide different name",  bean.getName());
			errorList.add(ValidationError.builder().message(message).build());
		}
		
		if(ArgumentHelper.isNotEmpty(errorList)) {
			throw new ValidationException(errorList);
		}
		return applicationFeatureService.add(bean);
	}
	
	@RequestMapping(value = "/updateApplicationFeature", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateApplicationFeature(@Validated @RequestBody ApplicationFeatureBean bean, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
		
		List<ValidationError> errorList = new ArrayList<>();
		ApplicationFeatureBean applicationFeatureBean = applicationFeatureService.getApplicationFeatureById(bean.getId());
		if(!applicationFeatureBean.getName().equalsIgnoreCase(bean.getName()))
		{
			if (Objects.nonNull(applicationFeatureService.getApplicationFeatureByName(bean.getName())))
			{
				String message = String.format("Application feature %s is already exists. Please provide different name",  bean.getName());
				errorList.add(ValidationError.builder().message(message).build());
			}
		}

		if(ArgumentHelper.isNotEmpty(errorList)) {
			throw new ValidationException(errorList);
		}
		applicationFeatureService.update(bean);
	}
	
	@RequestMapping(value = "/getAllApplicationFeatures", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ApplicationFeatureBean> getAllApplicationFeatures() {
		
		return applicationFeatureService.getAllApplicationFeatureBean();
	}

}
