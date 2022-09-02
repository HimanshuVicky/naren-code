package com.assignsecurities.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.bean.CityMasterBean;
import com.assignsecurities.bean.DocumentMasterBean;
import com.assignsecurities.bean.FeeMasterBean;
import com.assignsecurities.bean.KeyValueBean;
import com.assignsecurities.bean.PropertyBean;
import com.assignsecurities.bean.StateMasterBean;
import com.assignsecurities.bean.StatusMasterBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.converter.CityMasterConverter;
import com.assignsecurities.converter.DocumentMasterConverter;
import com.assignsecurities.converter.FeeMasterConverter;
import com.assignsecurities.converter.StateMasterConverter;
import com.assignsecurities.converter.StatusMasterConverter;
import com.assignsecurities.domain.ApplicationProperties;
import com.assignsecurities.repository.impl.UtilityRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
@Slf4j
public class UtilityService {
	private static final String STAGE_1 = "Stage 1";
	private static final String STAGE_2 = "Stage 2";
	@Autowired
	private UtilityRepo utilityRepo;
	
	@Autowired
	private ApplicationPropertiesService applicationPropertiesService;
	
	public List<FeeMasterBean> getMasterFeeList() {
		return FeeMasterConverter.convert(utilityRepo.getMasterFeeList());
	}
	
	public List<DocumentMasterBean> getMasterDocumentList(String uploadedOrGenerated) {
		return DocumentMasterConverter.convert(utilityRepo.getMasterDocumentList(uploadedOrGenerated));
	}
	
	public List<StatusMasterBean> getMasterStatusList(String stage) {
		
		List<StatusMasterBean> beans = StatusMasterConverter.convert(utilityRepo.getMasterStatusListForType("Case"));
		if(stage.equalsIgnoreCase(STAGE_1)) {
			List<StatusMasterBean> closeStatusBeans  =beans.stream().filter(stg->stg.getStatus().equalsIgnoreCase("Close")).collect(Collectors.toList());
			beans = beans.stream().filter(stg->stg.getStage().equalsIgnoreCase(STAGE_1)).collect(Collectors.toList());
			if(ArgumentHelper.isNotEmpty(closeStatusBeans)) {
				beans.add(closeStatusBeans.get(0));
			}
		}
		List<StatusMasterBean> beansToReturn = new ArrayList<>();
		List<String> statusToExclude = new ArrayList<>();
		statusToExclude.add("Waiting eAdhar Customer");
		statusToExclude.add("Waiting eAdhar Admin");
		statusToExclude.add("Waiting Submission");
		statusToExclude.add("Waiting Customer Aadhar");
		statusToExclude.add("Waiting Admin Aadhar");
		beans.forEach(b->{
			if(!statusToExclude.contains(b.getStatus()) ) {
				beansToReturn.add(b);
			}
		});
		
		return beansToReturn;
	}
	public List<StatusMasterBean> getMasterStatusListWithAll(String stage) {
		List<StatusMasterBean> beansFromDb = StatusMasterConverter.convert(utilityRepo.getMasterStatusList(stage));
		List<StatusMasterBean> beans = new ArrayList<>();
		beans.add(StatusMasterBean.builder().stage(stage).status("All").build());
		beans.addAll(beansFromDb);
		return beans;
	}
	
	public List<StatusMasterBean> getMasterStatusList(UserLoginBean userLoginBean) {
		return StatusMasterConverter.convert(utilityRepo.getMasterStatusList(userLoginBean));
	}
	

	public List<CityMasterBean> getCityList() {
		return CityMasterConverter.convert(utilityRepo.getCityList());
	}
	
	public List<StateMasterBean> getStateList() {
		return StateMasterConverter.convert(utilityRepo.getStateList());
	}
	
	public List<KeyValueBean> getAllPartners(UserLoginBean userLoginBean) {
		return utilityRepo.getAllPartners(userLoginBean);
	}
	
	
	public List<CityMasterBean> getCityListForGivenSateCode(String state_code) {
		return CityMasterConverter.convert(utilityRepo.getCityListForGivenSateCode(state_code));
	}

	public List<DocumentMasterBean> getMasterDocumentListStage1(String uploadedOrGenerated) {
		List<DocumentMasterBean> beans = new ArrayList<>();
		if(uploadedOrGenerated.equals("Uploaded")) {
//			i. Aadhar Card, ii. PAN Card, iii. Cancelled Cheque, iv. Death Certificate, v.RTA Letter 1 & vi.Courier Receipt
			beans.add(DocumentMasterBean.builder().particulars("Aadhar").type("Aadhar").build());
			beans.add(DocumentMasterBean.builder().particulars("PAN").type("PAN").build());
			beans.add(DocumentMasterBean.builder().particulars("Cancelled Cheque").type("Cancelled Cheque").build());
			beans.add(DocumentMasterBean.builder().particulars("Death Certificate 1").type("Death Certificate 1").build());
			beans.add(DocumentMasterBean.builder().particulars("RtaLetter1").type("RtaLetter1").build());
			beans.add(DocumentMasterBean.builder().particulars("Courier Receipt").type("Courier Receipt").build());
		}else {
			beans.add(DocumentMasterBean.builder().particulars("RtaLetter1").type("RtaLetter1").build());
			beans.add(DocumentMasterBean.builder().particulars("RtaLetter2").type("RtaLetter2").build());
		}
		return beans;
	}
	
	
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void updateProperty(PropertyBean propertyBean,
			UserLoginBean userLoginBean) {
		applicationPropertiesService.updatePrice(ApplicationProperties.builder().propName(propertyBean.getPropertyName())
				.currentValue(propertyBean.getPropertyValue()).dateModified(new Date()).modifiedBy(userLoginBean.getDisplayName()).build());
		applicationPropertiesService.loadApplicationProperties();
	}
	
	 public static final Map<String, Integer> applicationUserType = new HashMap<>();
	    static {
	    	applicationUserType.put("Admin",1);
	    	applicationUserType.put("Franchise",2);
	    	applicationUserType.put("User",3);
	    	applicationUserType.put("Advocate",4);
	    	applicationUserType.put("FranchiseUser",5);
	    	applicationUserType.put("Referral",6);
	    	applicationUserType.put("Notary",7);
	    	applicationUserType.put("CA",8);	        
	    }

}
