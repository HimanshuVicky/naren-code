package com.assignsecurities.service.impl;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.MessageTemplateService;
import com.assignsecurities.bean.ApplicationUserBean;
import com.assignsecurities.bean.CaseBean;
import com.assignsecurities.bean.LawyerCaseCommentsDtlBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.converter.CaseConverter;
import com.assignsecurities.converter.LawyerCaseCommentsDtlConverter;
import com.assignsecurities.dm.PropertyKeys;
import com.assignsecurities.domain.LawyerCaseCommentsDtlModel;
import com.assignsecurities.domain.UserLogin;
import com.assignsecurities.repository.impl.CaseRepo;
import com.assignsecurities.repository.impl.LawyerCaseCommentsDtlRepo;
import com.assignsecurities.repository.impl.UserLoginRepo;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class LawyerCaseCommentsDtlService {
	@Autowired
    private LawyerCaseCommentsDtlRepo lawyerCaseCommentsDtlRepo;
	
	@Autowired
	private CaseService caseService;
	
	@Autowired
	private ApplicationUserService applicationUserService;
	
	@Autowired
	private UserLoginRepo userLoginRepo;
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private CaseRepo caseRepo;
	
	@Autowired 
	private SmsService smsService;
	
	public LawyerCaseCommentsDtlBean getLawyerCommentDtlById(Long id) {
		return LawyerCaseCommentsDtlConverter.convert(lawyerCaseCommentsDtlRepo.getLawyerCommentDtlById(id));
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public Long add(LawyerCaseCommentsDtlBean bean,UserLoginBean userLoginBean) {
				
		long lawyerCaseCommentsDtlId =  lawyerCaseCommentsDtlRepo.add(LawyerCaseCommentsDtlConverter.convert(bean));
		if(lawyerCaseCommentsDtlId <= 0)
		 return null;
		
		CaseBean caseBean = CaseConverter.convert(caseRepo.getById(bean.getCaseId(), userLoginBean));
		UserLoginBean appCaseUserBean = loginService.getUserLogin(caseBean.getUserId());
		
//		CaseBean caseBean = caseService.getById(bean.getCaseId(), userLoginBean);
		/*
		 * Send sms to admin & customer post lawyer add commnent to case.		
		 */
		  String smsText= AppConstant.LAWYER_CASE_COMMENT_ADD;  //MessageTemplateService.getMessage("CASE_ASSIGNED");
		  UserLoginBean custUserLoginBeanObj = loginService.getUserLogin(caseBean.getUserId()); 
		  
    		smsText = MessageFormat.format(smsText,custUserLoginBeanObj.getApplicationUserBean().getFirstName(), caseBean.getReferenceNumber());
			sendSMSPostCaseUpdateByLawyer(custUserLoginBeanObj.getMobileNo(),smsText);
		   
		  if(caseBean.getFranchiseId()>0)
		  {	ApplicationUserBean applicationFranchiseUserBean = applicationUserService
					.getApplicationUsersDtlForGivenFranchiseOwnerIdAndUserType(caseBean.getFranchiseId(),
							AppConstant.USER_TYPE_FRANCHISE);
						
			UserLoginBean franchiseUserLoginBeanObj = loginService.getUserLoginApplicationUserId(applicationFranchiseUserBean.getId());
			smsText= AppConstant.LAWYER_CASE_COMMENT_ADD; 
			smsText = MessageFormat.format(smsText, applicationFranchiseUserBean.getFirstName(), caseBean.getReferenceNumber());
			sendSMSPostCaseUpdateByLawyer(franchiseUserLoginBeanObj.getMobileNo(),smsText);
		
		  }
			// Admin
			UserLoginBean userLoginSysobj = loginService.getUserLoginApplicationUserId(AppConstant.SYS_USER_ID);
			smsText= AppConstant.LAWYER_CASE_COMMENT_ADD; 
			smsText = MessageFormat.format(smsText, userLoginSysobj.getApplicationUserBean().getFirstName(), caseBean.getReferenceNumber());
			sendSMSPostCaseUpdateByLawyer(userLoginSysobj.getMobileNo(),smsText);
			
		return lawyerCaseCommentsDtlId;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void update(LawyerCaseCommentsDtlBean bean) {
		lawyerCaseCommentsDtlRepo.update(LawyerCaseCommentsDtlConverter.convert(bean));
	}
	
    public List<LawyerCaseCommentsDtlBean> getAllLawyerCaseCommentsDtls(Long caseId) {
		List<LawyerCaseCommentsDtlModel> lawyerCaseCommentsDtlModelslst = lawyerCaseCommentsDtlRepo.getAllLawyerCaseCommentsDtls(caseId);
		return LawyerCaseCommentsDtlConverter.convert(lawyerCaseCommentsDtlModelslst);
	}
    
    public void sendSMSPostCaseUpdateByLawyer(String mobileNo, String smsText) {
		mobileNo = mobileNo.replaceAll("\\+", "");
		// "9921399990"
		smsText = smsText.replaceAll("<br/>", "");
		smsText = smsText.replaceAll("&nbsp;", "");
		smsService.sendSms(smsText, mobileNo);
	}

}
