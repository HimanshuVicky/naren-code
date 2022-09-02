package com.assignsecurities.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.assignsecurities.app.exception.ValidationException;
import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.bean.RoleBean;
import com.assignsecurities.bean.ValidationError;
import com.assignsecurities.service.impl.RoleService;

@RestController
public class RoleController extends BaseController {
	@Autowired
	private RoleService roleService;
	
	@RequestMapping(value = "/getRole/{roleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public RoleBean getRole(@PathVariable("roleId") Long roleId) {
			
		return roleService.getRoleById(roleId);
	}
	
	@RequestMapping(value = "/addRole", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Long addRole(@Validated @RequestBody RoleBean bean) {
		
		List<ValidationError> errorList = new ArrayList<>();
		
		if (Objects.nonNull(roleService.getRoleByName(bean.getName())))
		{
			String message = String.format("Role %s already exists. Please provide different name",  bean.getName());
			errorList.add(ValidationError.builder().message(message).build());
		}

		
		if(ArgumentHelper.isNotEmpty(errorList)) {
			throw new ValidationException(errorList);
		}
		return roleService.add(bean);
	}
	
	@RequestMapping(value = "/updateRole", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateRole(@Validated @RequestBody RoleBean bean) {
		
		List<ValidationError> errorList = new ArrayList<>();
		RoleBean roleBean = roleService.getRoleById(bean.getId());
		if(!roleBean.getName().equalsIgnoreCase(bean.getName()))
		{
			if (Objects.nonNull(roleService.getRoleByName(bean.getName())))
			{
				String message = String.format("Role %s already exists. Please provide different name",  bean.getName());
				errorList.add(ValidationError.builder().message(message).build());
			}
		}

		if(ArgumentHelper.isNotEmpty(errorList)) {
			throw new ValidationException(errorList);
		}
		 roleService.update(bean);
	}
	
	@RequestMapping(value = "/getAllRoles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<RoleBean> getAllRoles() {
		
		return roleService.getAllRoles();
	}
	

}
