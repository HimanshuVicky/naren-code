package com.assignsecurities.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.assignsecurities.domain.ESignSurepassClientidModel;
import com.assignsecurities.repository.impl.ESignSurepassClientidRepo;
@Service
public class ESignSurepassClientidService {
	@Autowired
	private ESignSurepassClientidRepo eSignSurepassClientidRepo;

	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public void storeSurepassClientId(Long userId, String clientId,String reqType,String reqStatus,String remarks) {
		eSignSurepassClientidRepo.add(ESignSurepassClientidModel.builder().userId(userId)
				.clientId(clientId).reqType(reqType).reqStatus(reqStatus).remarks(remarks).build());
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void storeSurepassClientIdRequired(Long userId, String clientId,String reqType,String reqStatus,String remarks) {
		eSignSurepassClientidRepo.add(ESignSurepassClientidModel.builder().userId(userId)
				.clientId(clientId).reqType(reqType).reqStatus(reqStatus).remarks(remarks).build());
	}

}
