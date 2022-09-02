package com.assignsecurities.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.assignsecurities.bean.CourierDetailBean;
import com.assignsecurities.converter.CourierDetailConverter;
import com.assignsecurities.domain.CourierDetailModel;
import com.assignsecurities.repository.impl.CourierDetailRepo;


@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class CourierDetailService {
	
	@Autowired
    private CourierDetailRepo courierDetailRepo;
	
	
	public CourierDetailBean getCourierDetailById(Long id) {
		return CourierDetailConverter.convert(courierDetailRepo.getCourierDetailById(id));
		
	}
	
	public CourierDetailBean getCourierDetailByCourierServiceName(String courierServiceName) {
		return CourierDetailConverter.convert(courierDetailRepo.getCourierServiceByName(courierServiceName));
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public Long add(CourierDetailBean bean) {
				
		long roleId =  courierDetailRepo.add(CourierDetailConverter.convert(bean));
		if(roleId <= 0)
		 return null;
				
		return roleId;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void update(CourierDetailBean bean) {
		courierDetailRepo.update(CourierDetailConverter.convert(bean));
	}
	
    public List<CourierDetailBean> getAllCourierDetail() {
		
		List<CourierDetailModel> roles = courierDetailRepo.getAllCourierServiceDetails();
		return CourierDetailConverter.convert(roles);
	}
    

    

}
