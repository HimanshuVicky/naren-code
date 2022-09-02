package com.assignsecurities.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.assignsecurities.domain.CaseSurepassClientidModel;
import com.assignsecurities.repository.impl.CaseSurepassClientidRepo;
@Service
public class CaseSurepassClientidService {
	@Autowired
	private CaseSurepassClientidRepo caseSurepassClientidRepo;

	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public void storeSurepassClientId(Long caseId, String clientId,String reqType,String reqStatus,String remarks) {
		caseSurepassClientidRepo.add(CaseSurepassClientidModel.builder().caseId(caseId)
				.clientId(clientId).reqType(reqType).reqStatus(reqStatus).remarks(remarks).build());
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void storeSurepassClientIdRequired(Long caseId, String clientId,String reqType,String reqStatus,String remarks) {
		caseSurepassClientidRepo.add(CaseSurepassClientidModel.builder().caseId(caseId)
				.clientId(clientId).reqType(reqType).reqStatus(reqStatus).remarks(remarks).build());
	}

}
