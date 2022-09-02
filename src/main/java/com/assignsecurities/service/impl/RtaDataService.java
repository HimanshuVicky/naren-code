package com.assignsecurities.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.assignsecurities.bean.RtaDataBean;
import com.assignsecurities.converter.RtaDataConvertor;
import com.assignsecurities.domain.RtaDataModel;
import com.assignsecurities.repository.impl.RtaDataRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
@Slf4j
public class RtaDataService {
	@Autowired
	private RtaDataRepo rtaDataRepo;

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void save(RtaDataBean rtaDataBean) {
		log.info("Start of save");
		RtaDataModel model = RtaDataConvertor.convert(rtaDataBean);
		rtaDataRepo.add(model);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void update(RtaDataBean rtaDataBean) {
		RtaDataModel model = RtaDataConvertor.convert(rtaDataBean);
		rtaDataRepo.update(model);
	}

	public RtaDataBean getRtaDataBySecurityCode(String securityCode) {
		return RtaDataConvertor.convert(rtaDataRepo.getRtaDataBySecurityCode(securityCode));
	}

	public RtaDataBean getRtaDataByIsinNumber(String isinNumber) {
		return RtaDataConvertor.convert(rtaDataRepo.getRtaDataByIsinNumber(isinNumber));
	}
	public RtaDataBean getRtaDataByCompanyName(String companyName) {
		return RtaDataConvertor.convert(rtaDataRepo.getRtaDataByCompanyName(companyName));
	}
	
	public Boolean isRtaDataByCompanyName(String companyName) {
		return rtaDataRepo.isRtaDataByCompanyName(companyName);
	}
	
}
