package com.assignsecurities.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.assignsecurities.bean.ApplicationFeatureBean;
import com.assignsecurities.converter.ApplicationFeatureConverter;
import com.assignsecurities.domain.ApplicationFeatureModel;
import com.assignsecurities.repository.impl.ApplicationFeatureRepo;

@Service("ApplicationFeatureService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class ApplicationFeatureService {
	
	private static final Logger logger = LogManager.getLogger(RoleService.class);

	@Autowired
	private ApplicationFeatureRepo applicationFeatureRepo;
	
	public ApplicationFeatureBean getApplicationFeatureById(Long id) {
		return ApplicationFeatureConverter.convert(applicationFeatureRepo.getApplicationFeatureById(id));
		
	}
	
	public ApplicationFeatureBean getApplicationFeatureByName(String name) {
		return ApplicationFeatureConverter.convert(applicationFeatureRepo.getApplicationFeatureByName(name));
		
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public Long add(ApplicationFeatureBean bean) {
				
		long applicationFeatureId =  applicationFeatureRepo.add(ApplicationFeatureConverter.convert(bean));
		if(applicationFeatureId <= 0)
		 return null;
				
		return applicationFeatureId;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void update(ApplicationFeatureBean bean) {
		applicationFeatureRepo.update(ApplicationFeatureConverter.convert(bean));
	}
	
    public List<ApplicationFeatureBean> getAllApplicationFeatureBean() {
		
		List<ApplicationFeatureModel> roles = applicationFeatureRepo.getAllApplicationFeature();
		return ApplicationFeatureConverter.convert(roles);
	}

}
