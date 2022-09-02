package com.assignsecurities.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.assignsecurities.bean.RoleBean;
import com.assignsecurities.converter.RoleConverter;
import com.assignsecurities.domain.RoleModel;
import com.assignsecurities.repository.impl.RoleRepo;

@Service("RoleService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class RoleService {

	private static final Logger logger = LogManager.getLogger(RoleService.class);

	@Autowired
	private RoleRepo roleRepo;
	
	public RoleBean getRoleById(Long id) {
		return RoleConverter.convert(roleRepo.getRoleById(id));
		
	}
	
	public RoleBean getRoleByName(String roleName) {
		return RoleConverter.convert(roleRepo.getRoleByName(roleName));
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public Long add(RoleBean bean) {
				
		long roleId =  roleRepo.add(RoleConverter.convert(bean));
		if(roleId <= 0)
		 return null;
				
		return roleId;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void update(RoleBean bean) {
		roleRepo.update(RoleConverter.convert(bean));
	}
	
    public List<RoleBean> getAllRoles() {
		
		List<RoleModel> roles = roleRepo.getAllRoles();
		return RoleConverter.convert(roles);
	}
	
}
