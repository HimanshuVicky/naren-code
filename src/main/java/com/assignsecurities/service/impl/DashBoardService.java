package com.assignsecurities.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.bean.DashBoardCountBean;
import com.assignsecurities.bean.DashBoardCountKayValueBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.converter.DashBoardCountKayValueConverter;
import com.assignsecurities.domain.DashBoardCountKayValue;
import com.assignsecurities.repository.impl.DashBoardRepo;

import lombok.extern.slf4j.Slf4j;

@Service("DashBoardService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
@Slf4j
public class DashBoardService {
	public static final String CASE_DIAPLAY_STATUS_PENDING_LAWYER_INPUT = "Pending Lawyer Input";

	public static final String CASE_DIAPLAY_STATUS_PENDING_CASE_RESPONSE = "Pending Case Response";

	public static final String CASE_DIAPLAY_STATUS_UPLOADED_RTA_RESPONSE = "Uploaded RTA Response";

	public static final String CASE_DIAPLAY_STATUS_PENDING_RTA_RESPONSE = "Pending RTA Response";

	public static final String CASE_DIAPLAY_STATUS_PENDING_RTA_SUBMISSION = "Pending RTA Submission";

	public static final String CASE_DIAPLAY_STATUS_PENDING_CASE_UPLOAD = "Pending Case Upload";

	public static final String CASE_DIAPLAY_STATUS_PENDING_RTA_LETTER = "Pending RTA Letter";

	public static final String CASE_DIAPLAY_STATUS_PENDING_FRENCHISE_ASSIGNMENT = "Pending Frenchise Assignment";

	public static final String CASE_DIAPLAY_STATUS_PENDING_APPROVAL = "Pending Approval";

	public static final String CASE_DIAPLAY_STATUS_PROVIDE_RESPONSE = "Provide Response";

	public static final String CASE_DIAPLAY_STATUS_PENDING_SUBMISSION = "Pending Submission";

	@Autowired
	private DashBoardRepo dashBoardRepo;
	
	public static Map<String, String> displayApplicationStatusMapping = new HashedMap<>();
	static {
		displayApplicationStatusMapping.put(AppConstant.APPLICATION_STATUS_WAITING_FOR_VALIDATION,"Waiting Validation");
		displayApplicationStatusMapping.put(AppConstant.APPLICATION_STATUS_READY_FOR_PROC,"Waiting Processing");
	}
	
	public static Map<String, Integer> displayApplicationStatusMappingForOrdering = new HashedMap<>();
	static {
		displayApplicationStatusMappingForOrdering.put("Favourite Scripts",1);
		displayApplicationStatusMappingForOrdering.put("Waiting Validation",2);
		displayApplicationStatusMappingForOrdering.put("Waiting Processing",3);
		displayApplicationStatusMappingForOrdering.put(AppConstant.APPLICATION_STATUS_WAITING_FOR_VALIDATION,2);
		displayApplicationStatusMappingForOrdering.put(AppConstant.APPLICATION_STATUS_PENDING_FEE_ALLOCATION,3);
		displayApplicationStatusMappingForOrdering.put(AppConstant.APPLICATION_STATUS_READY_FOR_PROC,4);
		displayApplicationStatusMappingForOrdering.put(AppConstant.APPLICATION_STATUS_IN_PROGRESS,5);
		displayApplicationStatusMappingForOrdering.put(AppConstant.APPLICATION_STATUS_REJECT,6);
	}
	
	
	
	
//	public static Map<String, String> displayCaseStatusMapping = new HashedMap<>();
//	static {
//		displayCaseStatusMapping.put(AppConstant.CaseStatus.PendingSubmission.label,CASE_DIAPLAY_STATUS_PENDING_SUBMISSION);
//		displayCaseStatusMapping.put(AppConstant.CaseStatus.PendingCustomerResponse.label,CASE_DIAPLAY_STATUS_PROVIDE_RESPONSE);
//		displayCaseStatusMapping.put(AppConstant.CaseStatus.PendingAdminApproval.label,CASE_DIAPLAY_STATUS_PENDING_APPROVAL);
//		displayCaseStatusMapping.put(AppConstant.CaseStatus.PendingFrenchiseAssignment.label,CASE_DIAPLAY_STATUS_PENDING_FRENCHISE_ASSIGNMENT);
//		displayCaseStatusMapping.put(AppConstant.CaseStatus.PendingRTALetterCreation.label,CASE_DIAPLAY_STATUS_PENDING_RTA_LETTER);
//		displayCaseStatusMapping.put(AppConstant.CaseStatus.PendingCaseUpload.label,CASE_DIAPLAY_STATUS_PENDING_CASE_UPLOAD);
//		displayCaseStatusMapping.put(AppConstant.CaseStatus.PendingRTASubmission.label,CASE_DIAPLAY_STATUS_PENDING_RTA_SUBMISSION);
//		displayCaseStatusMapping.put(AppConstant.CaseStatus.PendingRTAResponse.label,CASE_DIAPLAY_STATUS_PENDING_RTA_RESPONSE);
//		displayCaseStatusMapping.put(AppConstant.CaseStatus.UploadedRTAResponse.label,CASE_DIAPLAY_STATUS_UPLOADED_RTA_RESPONSE);
//		displayCaseStatusMapping.put(AppConstant.CaseStatus.PendingCaseResponse.label,CASE_DIAPLAY_STATUS_PENDING_CASE_RESPONSE);
//		displayCaseStatusMapping.put(AppConstant.CaseStatus.PendingLawyerInput.label,CASE_DIAPLAY_STATUS_PENDING_LAWYER_INPUT);
//	}
	
//	public static Map<String, Integer> displayCaseStatusMappingForOrdering = new HashedMap<>();
//	static {
//		displayCaseStatusMappingForOrdering.put("My Cases",0);
//		displayCaseStatusMappingForOrdering.put(AppConstant.CaseStatus.PendingSubmission.label,1);
//		displayCaseStatusMappingForOrdering.put(CASE_DIAPLAY_STATUS_PENDING_SUBMISSION,1);
//		
//		displayCaseStatusMappingForOrdering.put(AppConstant.CaseStatus.PendingAdminApproval.label,2);
//		displayCaseStatusMappingForOrdering.put(CASE_DIAPLAY_STATUS_PENDING_APPROVAL,2);
//		
//		displayCaseStatusMappingForOrdering.put(AppConstant.CaseStatus.PendingFrenchiseAssignment.label,3);
//		displayCaseStatusMappingForOrdering.put(CASE_DIAPLAY_STATUS_PENDING_FRENCHISE_ASSIGNMENT,3);
//		
//		displayCaseStatusMappingForOrdering.put(AppConstant.CaseStatus.PendingRTALetterCreation.label,4);
//		displayCaseStatusMappingForOrdering.put(CASE_DIAPLAY_STATUS_PENDING_RTA_LETTER,4);
//		
//		displayCaseStatusMappingForOrdering.put(AppConstant.CaseStatus.PendingCaseUpload.label,5);
//		displayCaseStatusMappingForOrdering.put(CASE_DIAPLAY_STATUS_PENDING_CASE_UPLOAD,5);
//		
//		displayCaseStatusMappingForOrdering.put(AppConstant.CaseStatus.PendingRTASubmission.label,6);
//		displayCaseStatusMappingForOrdering.put(CASE_DIAPLAY_STATUS_PENDING_RTA_SUBMISSION,6);
//		
//		displayCaseStatusMappingForOrdering.put(AppConstant.CaseStatus.PendingRTAResponse.label,7);
//		displayCaseStatusMappingForOrdering.put(CASE_DIAPLAY_STATUS_PENDING_RTA_RESPONSE,7);
//		
//		displayCaseStatusMappingForOrdering.put(AppConstant.CaseStatus.UploadedRTAResponse.label,8);
//		displayCaseStatusMappingForOrdering.put(CASE_DIAPLAY_STATUS_UPLOADED_RTA_RESPONSE,8);
//		
//		displayCaseStatusMappingForOrdering.put(AppConstant.CaseStatus.PendingCustomerResponse.label,9);
//		displayCaseStatusMappingForOrdering.put(CASE_DIAPLAY_STATUS_PROVIDE_RESPONSE,9);
//		
//		displayCaseStatusMappingForOrdering.put(AppConstant.CaseStatus.PendingCaseResponse.label,10);
//		displayCaseStatusMappingForOrdering.put(CASE_DIAPLAY_STATUS_PENDING_CASE_RESPONSE,10);
//		
//		displayCaseStatusMappingForOrdering.put(AppConstant.CaseStatus.PendingLawyerInput.label,11);
//		displayCaseStatusMappingForOrdering.put(CASE_DIAPLAY_STATUS_PENDING_LAWYER_INPUT,11);
//	}

	public DashBoardCountBean getMyApplicationCount(UserLoginBean userLoginBean) {
		log.info("get Application Count");
		List<String> statuses = getApplicationStatusByUser(userLoginBean);
		List<DashBoardCountKayValue> kayValueModels = dashBoardRepo.getMyApplicationCount(userLoginBean, statuses);
		List<DashBoardCountKayValueBean> myApplications = DashBoardCountKayValueConverter.convert(kayValueModels);
		loadMissingData(userLoginBean, statuses, myApplications);
		mapDisplayStatus(userLoginBean, myApplications);
		doApplicationStatusOrdering(userLoginBean, myApplications);
		return DashBoardCountBean.builder().myApplications(myApplications).build();
	}
	
	public DashBoardCountBean getMyFavouriteCount(UserLoginBean userLoginBean) {
		log.info("get MyFavourite Count ");
		List<DashBoardCountKayValue> kayValueModels = dashBoardRepo.getMyFavouriteCount(userLoginBean);
		List<DashBoardCountKayValueBean> myApplications = DashBoardCountKayValueConverter.convert(kayValueModels);
		return DashBoardCountBean.builder().myApplications(myApplications).build();
	}
	
	private void doApplicationStatusOrdering(UserLoginBean userLoginBean,
			List<DashBoardCountKayValueBean> myApplications) {
		myApplications.forEach(ac->{
			if(displayApplicationStatusMappingForOrdering.containsKey(ac.getKey())) {
				ac.setSortOrder(displayApplicationStatusMappingForOrdering.get(ac.getKey()));	
			}else {
				ac.setSortOrder(999);
			}
		});
		myApplications.sort(Comparator.comparing(DashBoardCountKayValueBean::getSortOrder));
	}

	private void mapDisplayStatus(UserLoginBean userLoginBean, List<DashBoardCountKayValueBean> myApplications) {
		if (AppConstant.USER_TYPE_END_USER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
			myApplications.forEach(application->{
				if(displayApplicationStatusMapping.containsKey(application.getKey())) {
					application.setKey(displayApplicationStatusMapping.get(application.getKey()));
				}
			});
		}
	}

	private void loadMissingData(UserLoginBean userLoginBean, List<String> statuses,
			List<DashBoardCountKayValueBean> myApplications) {
		List<DashBoardCountKayValueBean> myApplicationDefaultCount = new ArrayList<DashBoardCountKayValueBean>();
//		if (AppConstant.USER_TYPE_END_USER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
			statuses.forEach(status -> {
				if (myApplications.stream().filter(a -> status.equalsIgnoreCase(a.getKey())).count() == 0) {
					myApplicationDefaultCount.add(DashBoardCountKayValueBean.builder().key(status).count(0L).build());
				}
			});
//		}
		myApplications.addAll(myApplicationDefaultCount);
	}

	private List<String> getApplicationStatusByUser(UserLoginBean userLoginBean) {
		List<String> statuses = new ArrayList<>();
		if (AppConstant.USER_TYPE_END_USER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
			statuses.add(AppConstant.APPLICATION_STATUS_WAITING_FOR_VALIDATION);
			statuses.add(AppConstant.APPLICATION_STATUS_READY_FOR_PROC);
			statuses.add(AppConstant.APPLICATION_STATUS_PENDING_FEE_ALLOCATION);
			statuses.add(AppConstant.APPLICATION_STATUS_REJECT);
		}else if (AppConstant.USER_TYPE_ADMIN.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())
				|| (AppConstant.USER_TYPE_CC.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType()))) {
			statuses.add(AppConstant.APPLICATION_STATUS_WAITING_FOR_VALIDATION);
			statuses.add(AppConstant.APPLICATION_STATUS_READY_FOR_PROC);
			statuses.add(AppConstant.APPLICATION_STATUS_PENDING_FEE_ALLOCATION);
			statuses.add(AppConstant.APPLICATION_STATUS_REJECT);
//		}else if (AppConstant.USER_TYPE_CC.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//			statuses.add(AppConstant.APPLICATION_STATUS_WAITING_FOR_VALIDATION);
		}else {
			statuses.add("None");
		}
		return statuses;
	}
	
	public DashBoardCountBean getMyCasesCount(UserLoginBean userLoginBean) {
		log.info("get Case Count");
//		List<String> statuses = getCaseStatusByUser(userLoginBean);
		List<String> statuses = new ArrayList<String>();
		statuses.add("Stage 1");
		statuses.add("Stage 2");
		statuses.add("Stage 3");
		List<DashBoardCountKayValue> kayValueModels = dashBoardRepo.getMyCasesCount(userLoginBean);
		List<DashBoardCountKayValueBean> myCases = DashBoardCountKayValueConverter.convert(kayValueModels);
		loadMissingDataForCase(userLoginBean, statuses, myCases);
		myCases.sort(Comparator.comparing(DashBoardCountKayValueBean::getKey));
		return DashBoardCountBean.builder().myApplications(myCases).build();
	}
	
	
	private void loadMissingDataForCase(UserLoginBean userLoginBean, List<String> statuses,
			List<DashBoardCountKayValueBean> myCases) {
		List<DashBoardCountKayValueBean> myApplicationDefaultCount = new ArrayList<DashBoardCountKayValueBean>();
//		if (AppConstant.USER_TYPE_END_USER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
			statuses.forEach(status -> {
				if (myCases.stream().filter(a -> status.equalsIgnoreCase(a.getKey())).count() == 0) {
					myApplicationDefaultCount.add(DashBoardCountKayValueBean.builder().key(status).count(0L).build());
				}
			});
//		}
		myCases.addAll(myApplicationDefaultCount);
	}
}
