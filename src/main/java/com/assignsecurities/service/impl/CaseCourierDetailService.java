package com.assignsecurities.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.assignsecurities.bean.CaseCourierDetailBean;
import com.assignsecurities.converter.CaseCourierDetailConverter;
import com.assignsecurities.domain.CaseCourierDetailModel;
import com.assignsecurities.repository.impl.CaseCourierDetailRepo;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class CaseCourierDetailService {
	
	@Autowired
    private CaseCourierDetailRepo caseCourierDetailRepo;
	
	public CaseCourierDetailBean getCaseCourierDetailById(Long id) {
		return CaseCourierDetailConverter.convert(caseCourierDetailRepo.getCseCourierDetailById(id));
		
	}
	
		
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public Long add(CaseCourierDetailBean bean) {
				
		long caseCourierDetailId =  caseCourierDetailRepo.add(CaseCourierDetailConverter.convert(bean));
		if(caseCourierDetailId <= 0)
		 return null;
				
		return caseCourierDetailId;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void update(CaseCourierDetailBean bean) {
		caseCourierDetailRepo.update(CaseCourierDetailConverter.convert(bean));
	}
	
	 public List<CaseCourierDetailBean> getAllCourierServiceDetailByGivenCaseId(Long caseId) {
	    	List<CaseCourierDetailModel> caseCourierDetailModel = caseCourierDetailRepo.getAllCourierServiceDetailByGivenCaseId(caseId);
			return CaseCourierDetailConverter.convert(caseCourierDetailModel);
		}
	    
	    public List<CaseCourierDetailBean> getAllCaseCourierServiceDetailByGivenCourierId(Long courierId) {
	    	List<CaseCourierDetailModel> caseCourierDetailModel = caseCourierDetailRepo.getAllCaseCourierServiceDetailByGivenCourierId(courierId);
			return CaseCourierDetailConverter.convert(caseCourierDetailModel);
		}

}
