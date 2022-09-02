package com.assignsecurities.service.impl;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.MessageTemplateService;
import com.assignsecurities.app.exception.ValidationException;
import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.app.util.PersonNameUtil;
import com.assignsecurities.bean.AddFolioToApplicationBean;
import com.assignsecurities.bean.AddNewFolioToApplicationBean;
import com.assignsecurities.bean.ApplicationBean;
import com.assignsecurities.bean.ApplicationDetailsBean;
import com.assignsecurities.bean.ApplicationFeeBean;
import com.assignsecurities.bean.ApplicationQuestionerBean;
import com.assignsecurities.bean.ApplicationScriptBean;
import com.assignsecurities.bean.ApplicationUserBean;
import com.assignsecurities.bean.CreateApplicationBean;
import com.assignsecurities.bean.FeeBean;
import com.assignsecurities.bean.PersonName;
import com.assignsecurities.bean.QuestionerBean;
import com.assignsecurities.bean.RemoveApplicationScriptBean;
import com.assignsecurities.bean.ScriptBean;
import com.assignsecurities.bean.UpdateApplicationFeeBean;
import com.assignsecurities.bean.UpdateApplicationStatusBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.bean.ValidationError;
import com.assignsecurities.converter.AddressConverter;
import com.assignsecurities.converter.ApplicationConverter;
import com.assignsecurities.converter.ApplicationDetailsConverter;
import com.assignsecurities.converter.ApplicationQuestionerConvertor;
import com.assignsecurities.converter.ApplicationScriptConverter;
import com.assignsecurities.converter.ScriptConvertor;
import com.assignsecurities.converter.UserLoginConverter;
import com.assignsecurities.dm.PropertyKeys;
import com.assignsecurities.domain.ApplicationFeeModel;
import com.assignsecurities.domain.ApplicationModel;
import com.assignsecurities.domain.ApplicationQuestionerModel;
import com.assignsecurities.domain.ApplicationScriptModel;
import com.assignsecurities.domain.EmailBodyModel;
import com.assignsecurities.domain.EmailModel;
import com.assignsecurities.domain.EmailToModel;
import com.assignsecurities.domain.QuestionerModel;
import com.assignsecurities.domain.ScriptModel;
import com.assignsecurities.domain.UserLogin;
import com.assignsecurities.repository.impl.AddressRepo;
import com.assignsecurities.repository.impl.ApplicationFeeRepo;
import com.assignsecurities.repository.impl.ApplicationRepo;
import com.assignsecurities.repository.impl.ApplicationScriptRepo;
import com.assignsecurities.repository.impl.EmailReposatory;
import com.assignsecurities.repository.impl.ScriptRepo;
import com.assignsecurities.repository.impl.UserLoginRepo;
//import com.google.common.util.concurrent.AtomicDouble;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
@Slf4j
public class ApplicationService {
	@Autowired
	private ScriptRepo scriptRepo;

	@Autowired
	private ApplicationRepo applicationRepo;

	@Autowired
	private ApplicationScriptRepo applicationScriptRepo;

	@Autowired
	private ScriptService scriptService;

	@Autowired
	private AddressRepo addressRepo;
	
	@Autowired
	private SmsService smsService;
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private ApplicationFeeRepo applicationFeeRepo;
	
	@Autowired
	private QuestionerService questionerService;
	
	@Autowired
	private ApplicationUserService applicationUserService;
	
	@Autowired 
	private EmailReposatory emailReposatory;
	
	@Autowired
	private UserLoginRepo userLoginRepo;
	
	@Autowired
	private EmailService emailService;


	public List<ApplicationBean> getApplicationByStatus(String status, UserLoginBean userLoginBean) {
		return ApplicationConverter.convertL(applicationRepo.getApplicationByStatus(status, userLoginBean));
	}

	public ApplicationDetailsBean getById(Long id, UserLoginBean userLoginBean) {
		ApplicationModel model = applicationRepo.getById(id, userLoginBean);
		ApplicationDetailsBean bean = ApplicationDetailsConverter.convert(model);
//		model.setScripts(applicationScriptRepo.getByScriptByApplicationId(id));
		UserLoginBean appUserBean = loginService.getUserLogin(model.getUserId());
		bean.setAddress(AddressConverter
				.convert(addressRepo.getAddressById(appUserBean.getApplicationUserBean().getAddressId())));
		
		bean.setPhoneNumber(appUserBean.getMobileNo());
//		List<Long> scriptIds = model.getScripts().stream().map(s -> s.getScriptId()).distinct()
//				.collect(Collectors.toList());
		boolean mask = scriptService.isFolioNumberOrDpIdClientIdMasked(userLoginBean);
		List<ScriptBean> scriptBeans = ScriptConvertor.convert(scriptRepo.getScripts(id));
		scriptService.maskFolioNumberOrDpIdClientId(scriptBeans, mask);
		bean.setScripts(scriptBeans);
//		AtomicDouble totalCount = new AtomicDouble(0D);
//		bean.getScripts().forEach(s->{
//			totalCount.addAndGet(s.getMarketPrice());
//		});
//		bean.setTotalValue(totalCount.get());
		double totalCount =0;
		for(ScriptBean s  :bean.getScripts()) {
			totalCount = totalCount +s.getMarketPrice();
		}
		bean.setTotalValue(totalCount);
		List<FeeBean> feeBeans = getApplicationFeesById(id, userLoginBean);
		setDefaultFees(feeBeans);
		bean.setFeeBeans(feeBeans);
		bean.setSecurityCode(scriptBeans.get(0).getSecurityCode());
		bean.setIsinNumber(scriptBeans.get(0).getIsinCode());
		return bean;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void createApplication(CreateApplicationBean createApplicationBean, UserLoginBean userLoginBean) {
		List<ScriptModel> scriptModels = scriptRepo.getScripts(createApplicationBean.getScriptIds());
		List<ApplicationBean> applicationBeans = createApplicationModels(scriptModels, userLoginBean);
		List<ApplicationModel> models = ApplicationConverter.convert(applicationBeans);
		
		models.forEach(model -> {
			if (!ArgumentHelper.isValid(model.getFeeType()) && !ArgumentHelper.isPositive(model.getFeeValue())) {
				model.setFeeType(
						ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.DEFAULT_APPLICATION_FEE_TYPE));
				model.setFeeValue(Double.parseDouble(
						ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.DEFAULT_APPLICATION_FEE)));
			}
			Long applicationId = applicationRepo.add(model);
			/*
			 * send notificatoin for SMS & Email template code VALIDATION_SUBMISSION_SMS
			 */
			if (Objects.nonNull(userLoginBean)) {
				sendSMSAndEmailPostApplicationStatusChange(userLoginBean.getApplicationUserBean(),userLoginBean, applicationId, "VALIDATION_SUBMISSION_SMS");
				/*
				 * start
				 */
				String website = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.WEBSITE);
				String smsText= MessageTemplateService.getMessage("VALIDATION_SUBMISSION_SMS"); 
				smsText = MessageFormat.format(smsText, userLoginBean.getApplicationUserBean().getFirstName(), applicationId,website);
				sendSMSPostApplicationStatusChange(userLoginBean.getMobileNo(),smsText);
				
				// Admin
				UserLogin userLoginSysobj = userLoginRepo.getUserLoginApplicationUserId(AppConstant.SYS_USER_ID);
				sendSMSPostApplicationStatusChange(userLoginSysobj.getMobileNo(), smsText);
				
//				smsText= MessageTemplateService.getMessage("VALIDATION_SUBMISSION_SMS");//AppConstant.APP_RESET_SMS_TEXT; //MessageTemplateService.getMessage("APP_RESET_SMS_TEXT"); // VALIDATION_SUBMISSION_SMS
				
				String support_contactnumber = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.CUSTOMER_CARE_SUPPORT_NUMBER);
				String team="Findmymoney";
				String subject = MessageTemplateService.getMessage("VALIDATION_SUBMISSION_EMAIL_SUB");
				String eMailBoday = MessageTemplateService.getMessage("VALIDATION_SUBMISSION_EMAIL_BODY"); 
				eMailBoday = MessageFormat.format(eMailBoday, userLoginBean.getApplicationUserBean().getFirstName(), applicationId,support_contactnumber,team);

				List<String> emailCcIds = new ArrayList(10);

				ApplicationUserBean applicationAdminUserBean = applicationUserService
						.getApplicationUserById(AppConstant.SYS_USER_ID); // here assume Sys user Id is Admin.
				String ccEmailAdminAddress = applicationAdminUserBean.getEmailId();
				if (!Objects.isNull(ccEmailAdminAddress) && ccEmailAdminAddress.length() > 0) {
					emailCcIds.add(ccEmailAdminAddress);
				}
				
				emailService.sendEmail(userLoginBean.getApplicationUserBean(), userLoginBean, eMailBoday, subject, null, emailCcIds);
				
				
			}
			
			List<Long> scriptIds = new ArrayList<>();
			model.getScripts().forEach(script -> {
				script.setApplicationId(applicationId);
				scriptIds.add(script.getScriptId());
			});
			applicationScriptRepo.addAll(model.getScripts());
			scriptRepo.removeFromFavourite(scriptIds, userLoginBean);
		});
		
		
		
		
	}

	private List<ApplicationBean> createApplicationModels(List<ScriptModel> scriptModels, UserLoginBean userLoginBean) {
		log.info("Preparing Application Object.");
		List<ApplicationBean> applicationBeans = new ArrayList<>();
		Map<String, ApplicationBean> applicationMap = new HashMap<>();
		scriptModels.forEach(script -> {
			String investorName = script.getInvestorName();
			String applicationKey = investorName.trim() + ":" + script.getCompanyName().trim();
			log.info("applicationMap=====>" + applicationMap);
			log.info("applicationKey=====>" + applicationKey);
			if (applicationMap.containsKey(applicationKey)) {
				ApplicationBean applicationBean = applicationMap.get(applicationKey);
				List<ApplicationScriptBean> applicationScripts = applicationBean.getScripts();
				applicationScripts.add(ApplicationScriptBean.builder().scriptId(script.getId()).build());
				applicationBean.setScripts(applicationScripts);
			} else {
				ApplicationBean applicationBean = ApplicationBean.builder().build();
				PersonName personName = PersonNameUtil.splitFullName(investorName);
				applicationBean.setFirstName(personName.getFirstName());
				applicationBean.setMiddleName(personName.getMiddleName());
				applicationBean.setLastName(personName.getLastName());
				applicationBean.setCompanyName(script.getCompanyName());
				applicationBean.setStatus(ApplicationBean.STATUS_WAITING_FOR_VALIDATION);
				applicationBean.setUserId(userLoginBean.getId());
				applicationBean.setCreatedDate(LocalDateTime.now());

				List<ApplicationScriptBean> applicationScripts = new ArrayList<>();
				applicationScripts.add(ApplicationScriptBean.builder().scriptId(script.getId()).build());
				applicationBean.setScripts(applicationScripts);
				applicationBeans.add(applicationBean);
				applicationMap.put(applicationKey, applicationBean);
			}
		});
		return applicationBeans;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void saveApplicationQuestioner(ApplicationQuestionerBean createApplicationBean,
			UserLoginBean userLoginBean) {
		ApplicationQuestionerModel questionerModel = ApplicationQuestionerConvertor.convert(createApplicationBean);
		applicationRepo.deleteApplicationscriptques(questionerModel.getApplicationScriptId());
		applicationRepo.add(questionerModel);
	}

	public ApplicationQuestionerBean getApplicationQuestioner(Long applicationScriptId) {
		return ApplicationQuestionerConvertor.convert(applicationRepo.getApplicationQuestioner(applicationScriptId));
	}

	public ApplicationQuestionerBean getApplicationQuestionerAns(Long applicationScriptId, UserLoginBean userLoginBean) {
		ApplicationQuestionerBean bean = ApplicationQuestionerConvertor.convert(applicationRepo.getApplicationQuestionerAns(applicationScriptId));
		if(ArgumentHelper.isEmpty(bean.getQuestioners())) {
			List<QuestionerBean> questioners = questionerService.getActiveQuestioners();
			bean = ApplicationQuestionerBean.builder().applicationScriptId(applicationScriptId).questioners(questioners).build();
		}
		return bean;
	}
	public ApplicationQuestionerBean getApplicationQuestionerAnsWithScript(Long applicationScriptId, UserLoginBean userLoginBean) {
		ApplicationQuestionerBean bean = ApplicationQuestionerConvertor.convert(applicationRepo.getApplicationQuestionerAns(applicationScriptId));
		if(ArgumentHelper.isEmpty(bean.getQuestioners())) {
			List<QuestionerBean> questioners = questionerService.getActiveQuestioners();
			bean = ApplicationQuestionerBean.builder().applicationScriptId(applicationScriptId).questioners(questioners).build();
		}
		bean.setScript(scriptService.getScriptByApplicationScriptId(applicationScriptId, userLoginBean));
		return bean;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public List<ApplicationScriptBean> addNewFolioToApplication(AddNewFolioToApplicationBean addNewFolioToApplicationBean,
			UserLoginBean userLoginBean) {
		List<ValidationError> errorList = new ArrayList<>();
		ScriptModel scriptModel = scriptRepo.getFolioNumberOrDpIdClientId(addNewFolioToApplicationBean.getScript().getFolioNumberOrDpIdClientId());
		if(Objects.nonNull(scriptModel)) {
			String msg = String.format("Script %s (%s) already exists.", scriptModel.getFolioNumberOrDpIdClientId(),scriptModel.getCompanyName());
			errorList.add(ValidationError.builder().message(msg).build());
		}
		if (ArgumentHelper.isNotEmpty(errorList)) {
			throw new ValidationException(errorList);
		}
		List<Long> scriptIds = new ArrayList<>();
		if(!ArgumentHelper.isValid(addNewFolioToApplicationBean.getScript().getCountry())) {
			addNewFolioToApplicationBean.getScript().setCountry("India");
		}
		List<ScriptModel> scriptModels = scriptRepo.getScriptByCompanyCode(addNewFolioToApplicationBean.getScript().getCompanyName());
		if(ArgumentHelper.isNotEmpty(scriptModels)) {
			ScriptModel scriptModelTemp =scriptModels.get(0);
			Double marketPrice = scriptModelTemp.getMarketPrice()/scriptModelTemp.getNumberOfShare();
			addNewFolioToApplicationBean.getScript().setMarketPrice(marketPrice);
			addNewFolioToApplicationBean.getScript().setNominalValue(scriptModels.get(0).getNominalValue());
		}
		Long scriptId = scriptService.save(addNewFolioToApplicationBean.getScript());
		scriptIds.add(scriptId);
		return addFolioToApplication(AddFolioToApplicationBean.builder()
				.applicationId(addNewFolioToApplicationBean.getApplicationId()).scriptIds(scriptIds).build(),
				userLoginBean);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public List<ApplicationScriptBean> addFolioToApplication(AddFolioToApplicationBean addFolioToApplicationBean,
			UserLoginBean userLoginBean) {
		List<ApplicationScriptModel> applicationScriptModels = new ArrayList<>();
		addFolioToApplicationBean.getScriptIds().forEach(scriptId -> {
			applicationScriptModels.add(ApplicationScriptModel.builder()
					.applicationId(addFolioToApplicationBean.getApplicationId()).scriptId(scriptId).build());
		});
		ApplicationModel model = applicationRepo.getById(addFolioToApplicationBean.getApplicationId(), userLoginBean);
		model.setScripts(applicationScriptRepo.getByScriptByApplicationId(addFolioToApplicationBean.getApplicationId()));
		List<Long> scriptIds = model.getScripts().stream().map(s -> s.getScriptId()).distinct()
				.collect(Collectors.toList());
		List<ScriptModel> scriptModels = scriptRepo.getScripts(scriptIds);
		List<ValidationError> errorList = new ArrayList<>();
		scriptModels.forEach(script->{
			if (addFolioToApplicationBean.getScriptIds().contains(script.getId())) {
				String msg = String.format("Script %s (%s) already there in the application.", script.getFolioNumberOrDpIdClientId(),script.getCompanyName());
				errorList.add(ValidationError.builder().message(msg).build());
			}
		});
		
		if (ArgumentHelper.isNotEmpty(errorList)) {
			throw new ValidationException(errorList);
		}
		
		applicationScriptRepo.addAll(applicationScriptModels);
		return getByScriptIds(addFolioToApplicationBean.getScriptIds());
	}

	public List<ApplicationScriptBean> getByScriptIds(List<Long> scriptIds) {
		List<ApplicationScriptModel> models = applicationScriptRepo.getByScriptIds(scriptIds);
		List<ScriptModel> scriptModels = scriptRepo.getScripts(scriptIds);
		models.forEach(script -> {
			List<ScriptModel> appScriptModels = scriptModels.stream()
					.filter(s -> s.getId().equals(script.getScriptId())).distinct().collect(Collectors.toList());
			script.setScripts(appScriptModels);
		});

		return ApplicationScriptConverter.convert(models);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void saveOrRemoveApplicationFolio(RemoveApplicationScriptBean removeApplicationScriptBean,
			UserLoginBean userLoginBean) {
		removeApplicationScriptBean.getApplicationScriptIds().forEach(asId -> {
			applicationRepo.deleteApplicationscriptques(asId);
			applicationScriptRepo.deleteByIdAndApplicationId(asId, removeApplicationScriptBean.getApplicationId());
		});
		updateApplicationFee(UpdateApplicationFeeBean.builder()
				.applicationId(removeApplicationScriptBean.getApplicationId())
				/*.feeType(removeApplicationScriptBean.getFeeType())*/.remarks(removeApplicationScriptBean.getRemarks()).build(), userLoginBean);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void updateApplicationStatus(UpdateApplicationStatusBean applicationStatusBean,
			UserLoginBean userLoginBean) {
		// TODO add validation for Roles and Next Applicable Statues
		if(Objects.nonNull(applicationStatusBean.getApplicationFeeBean())) {
			saveApplicationFee(applicationStatusBean.getApplicationFeeBean(), userLoginBean);
		}
		if(applicationStatusBean.getStatus().equals(AppConstant.APPLICATION_STATUS_PENDING_FEE_ALLOCATION)
				||applicationStatusBean.getStatus().equals(AppConstant.APPLICATION_STATUS_READY_FOR_PROC)) {
			List<ApplicationScriptModel> applicationScriptModels = applicationScriptRepo.getByScriptByApplicationId(applicationStatusBean.getApplicationId());
			ApplicationQuestionerModel questionerModel =applicationRepo.getApplicationQuestionerAnsByApplicationId(applicationStatusBean.getApplicationId());
			List<QuestionerModel> questioners = questionerModel.getQuestioners();
			Optional<QuestionerModel> nonProvidedQuesAnsOpt= questioners.stream().filter(q-> Objects.isNull(q.getAnswer())|| q.getAnswer().equals("")).findAny();
			List<QuestionerModel> q1List= questioners.stream().filter(q-> q.getQuestion().contains("Q.1 :")).collect(Collectors.toList());
			if(ArgumentHelper.isEmpty(questioners) || nonProvidedQuesAnsOpt.isPresent() || q1List.size() !=applicationScriptModels.size()) {
				List<ValidationError> errorList = new ArrayList<>();
				errorList.add(ValidationError.builder().message("Please provide application questionnaire info for all scripts.").build() );
				throw new ValidationException(errorList);
			}
			if(applicationStatusBean.getStatus().equals(AppConstant.APPLICATION_STATUS_READY_FOR_PROC)) {
				List<ApplicationFeeModel> feeModels = applicationFeeRepo.getByApplicationId(applicationStatusBean.getApplicationId());
				Optional<ApplicationFeeModel> feeOpt = feeModels.stream().filter(fee->Objects.isNull(fee.getFeeValue())).findFirst();
				if(feeOpt.isPresent() || feeModels.isEmpty()) {
					List<ValidationError> errorList = new ArrayList<>();
					errorList.add(ValidationError.builder().message("Please provide application fee details.").build() );
					throw new ValidationException(errorList);
				}
			}
		}
		ApplicationModel applicationModel = applicationRepo.getById(applicationStatusBean.getApplicationId(), userLoginBean);
		applicationModel.setStatus(applicationStatusBean.getStatus());
		if(ArgumentHelper.isValid(applicationStatusBean.getRemarks())) {
			applicationModel.setRemarks(applicationStatusBean.getRemarks());
		}
		applicationRepo.update(applicationModel);
		
		/*
		 * Send SMS notification 
		 */

		if(applicationStatusBean.getStatus().equals(AppConstant.APPLICATION_STATUS_READY_FOR_PROC)) {
			UserLoginBean userLoginBeanObj = loginService.getUserLogin(applicationModel.getUserId());
			ApplicationUserBean applicationUserBean = userLoginBeanObj.getApplicationUserBean();
			if(Objects.isNull(applicationUserBean)) {
				applicationUserBean = applicationUserService.getApplicationUserById(userLoginBeanObj.getApplicationUserId());
			}
//			UserLoginBean userLoginBeanObj = loginService.getUserLoginApplicationUserId(applicationUserBean.getId());
			sendSMSAndEmailPostApplicationStatusChange(applicationUserBean,userLoginBeanObj, applicationStatusBean.getApplicationId(), "APPLICATION_APPROVED_SMS");
		}
		
		if(applicationStatusBean.getStatus().equals(AppConstant.APPLICATION_STATUS_REJECT)) {
			UserLoginBean userLoginBeanObj = loginService.getUserLogin(applicationModel.getUserId());
			ApplicationUserBean applicationUserBean = userLoginBeanObj.getApplicationUserBean();
			if(Objects.isNull(applicationUserBean)) {
				applicationUserBean = applicationUserService.getApplicationUserById(userLoginBeanObj.getApplicationUserId());
			}
//			UserLoginBean userLoginBeanObj = loginService.getUserLoginApplicationUserId(applicationUserBean.getId());
			sendSMSAndEmailPostApplicationStatusChange(applicationUserBean,userLoginBeanObj, applicationStatusBean.getApplicationId(), "ADMIN_APPLICATION_REJECTED_SMS");
		}
		
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void updateApplicationFee(UpdateApplicationFeeBean applicationFeeBean, UserLoginBean userLoginBean) {
		// TODO add validation for Roles and Next Applicable Statues
		ApplicationModel applicationModel = applicationRepo.getById(applicationFeeBean.getApplicationId(), userLoginBean);
//		applicationModel.setFeeType(applicationFeeBean.getFeeType());
//		applicationModel.setFeeValue(applicationFeeBean.getFeeValue());
		if(ArgumentHelper.isValid(applicationFeeBean.getRemarks())) {
			applicationModel.setRemarks(applicationFeeBean.getRemarks());
		}
		applicationRepo.update(applicationModel);

	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public Boolean sendReminderForPendingApplication(ApplicationBean applicationBean,
			UserLoginBean userLoginBean) {
		ApplicationModel applicationModel = applicationRepo.getById(applicationBean.getId(), userLoginBean);
		UserLoginBean userBean = loginService.getUserLoginApplicationUserId(applicationModel.getUserId());
		if(Objects.nonNull(userBean))
		{
			sendReminderApplicationSMS(applicationModel, userBean.getMobileNo());	
			return true;
		}
		return false;
	}
	

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void saveApplicationFee(ApplicationFeeBean applicationFeeBean, UserLoginBean userLoginBean) {
		applicationFeeRepo.delete(applicationFeeBean.getApplicationId());
		List<ApplicationFeeModel> models = new ArrayList<>();
		applicationFeeBean.getFees().forEach(fee->{
			models.add(ApplicationFeeModel.builder().applicationId(applicationFeeBean.getApplicationId())
					.feeFor(fee.getFeeFor()).feeType(fee.getFeeType()).feeValue(fee.getFeeValue())
					.isGSTApplicable(fee.getIsGSTApplicable()).build());
		});
		applicationFeeRepo.addAll(models);
	}
	
	public List<FeeBean> getApplicationFeesById(Long applicationId, UserLoginBean userLoginBean) {
		List<ApplicationFeeModel> applicationFeeModels = applicationFeeRepo.getByApplicationId(applicationId);
		List<FeeBean> feeBeans = new ArrayList<>();
		applicationFeeModels.forEach(fee->{
			feeBeans.add(FeeBean.builder().id(fee.getId()).feeFor(fee.getFeeFor())
					.feeType(fee.getFeeType()).feeValue(fee.getFeeValue())
					.isGSTApplicable(fee.getIsGSTApplicable()).build());
		});
		return feeBeans;
	}
	
//	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
//	public void saveApplicationMandatoryDocument(ApplicationDocumentBean applicationDocumentBean, UserLoginBean userLoginBean) {
//		applicationMandatoryDocumentRepo.delete(applicationDocumentBean.getApplicationId());
//		List<CustomerDocumentsRequiredModel> models = new ArrayList<>();
//		applicationDocumentBean.getDocuments().forEach(doc->{
//			models.add(CustomerDocumentsRequiredModel.builder().applicationId(applicationDocumentBean.getApplicationId())
//					.doccumentName(doc.getDoccumentName()).build());
//		});
//		applicationMandatoryDocumentRepo.addAll(models);
//	}
//	
//	public List<MandatorySupplementaryDocumentBean> getApplicationMandatoryDocumentsById(Long applicationId, UserLoginBean userLoginBean) {
//		List<CustomerDocumentsRequiredModel> applicationFeeModels = applicationMandatoryDocumentRepo.getByApplicationId(applicationId);
//		List<MandatorySupplementaryDocumentBean> beans = new ArrayList<>();
//		applicationFeeModels.forEach(doc->{
//			beans.add(MandatorySupplementaryDocumentBean.builder().id(doc.getId()).doccumentName(doc.getDoccumentName()).build());
//		});
//		
//		return beans;
//	}
//	
//	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
//	public void saveApplicationSupplementaryDocument(ApplicationDocumentBean applicationDocumentBean, UserLoginBean userLoginBean) {
//		applicationSupplementaryDocumentRepo.delete(applicationDocumentBean.getApplicationId());
//		List<ApplicationSupplementaryDocumentModel> models = new ArrayList<>();
//		applicationDocumentBean.getDocuments().forEach(doc->{
//			models.add(ApplicationSupplementaryDocumentModel.builder().applicationId(applicationDocumentBean.getApplicationId())
//					.doccumentName(doc.getDoccumentName()).build());
//		});
//		applicationSupplementaryDocumentRepo.addAll(models);
//	}
//	
//	public List<MandatorySupplementaryDocumentBean> getApplicationSupplementaryDocumentsById(Long applicationId, UserLoginBean userLoginBean) {
//		List<ApplicationSupplementaryDocumentModel> applicationFeeModels = 
//				applicationSupplementaryDocumentRepo.getByApplicationId(applicationId);
//		List<MandatorySupplementaryDocumentBean> beans = new ArrayList<>();
//		applicationFeeModels.forEach(doc->{
//			beans.add(MandatorySupplementaryDocumentBean.builder().id(doc.getId()).doccumentName(doc.getDoccumentName()).build());
//		});
//		
//		return beans;
//	}
	

	public void sendReminderApplicationSMS(ApplicationModel applicationModel, String mobileNo) {
		log.info("Penidng Application Reminder SMS..");
		// TOD Send SMS/Email to user.
		String sSMSText = MessageTemplateService.getMessage("REMIND_USER_SMS"); //AppConstant.APP_PENDING_REMINDER_SMS_TEXT; //REMIND_USER_SMS
		sSMSText = MessageFormat.format(sSMSText, applicationModel.getFirstName(), applicationModel.getId(),"http://www.findmymoney.in");
		mobileNo = mobileNo.replaceAll("\\+", "");
		// "9921399990"
		sSMSText = sSMSText.replaceAll("<br/>", "");
		sSMSText = sSMSText.replaceAll("&nbsp;", "");
		smsService.sendSms(sSMSText, mobileNo);
		return;

	}
	

	private void setDefaultFees(List<FeeBean> feeBeans) {
		if(ArgumentHelper.isEmpty(feeBeans)) {
			feeBeans.add(FeeBean.builder().feeFor("Processing Fees").feeType("FixValue").isGSTApplicable(true).build());
			feeBeans.add(FeeBean.builder().feeFor("Agreement Fees").feeType("Both").isGSTApplicable(true).build());
			feeBeans.add(FeeBean.builder().feeFor("Advance Fee").feeType("FixValue").isGSTApplicable(false).build());
		}
		
	}
	
	
	private void sendSMSAndEmailPostApplicationStatusChange(ApplicationUserBean applicationUserBean, UserLoginBean userLoginBean, long applicationId, String stateUpdateFor)
	{
		log.info("Eending Application Status change..");
		// TOD Send SMS/Email to user.
		EmailModel emailModel = new EmailModel();
		UserLogin userloginModel = null;
		userloginModel = UserLoginConverter.convert(userLoginBean, userloginModel);
		String senderEmailAddress = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.SEND_MAIL_AS);
		if (Objects.isNull(senderEmailAddress)) {
			senderEmailAddress = "support@tracksoft.com"; //support@findmymoney.in
		}
		String toEmailAddress = applicationUserBean.getEmailId();
		String smsText="";
		String subject ="";
		String eMailBoday = "";
		String website = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.WEBSITE); //"http://www.findmymoney.in";
		UserLoginBean userLoginBeanObj = loginService.getUserLoginApplicationUserId(AppConstant.SYS_USER_ID);
		String support_contactnumber = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.CUSTOMER_CARE_SUPPORT_NUMBER); //"91-8050570505";//userLoginBeanObj.getMobileNo();
		String team="Findmymoney";
		if(stateUpdateFor.equalsIgnoreCase("VALIDATION_SUBMISSION_SMS"))
		{
		 smsText= MessageTemplateService.getMessage("VALIDATION_SUBMISSION_SMS");//AppConstant.APP_RESET_SMS_TEXT; //MessageTemplateService.getMessage("APP_RESET_SMS_TEXT"); // VALIDATION_SUBMISSION_SMS
		 subject = MessageTemplateService.getMessage("VALIDATION_SUBMISSION_EMAIL_SUB");
		 eMailBoday = MessageTemplateService.getMessage("VALIDATION_SUBMISSION_EMAIL_BODY"); 
		 eMailBoday = MessageFormat.format(eMailBoday, applicationUserBean.getFirstName(), applicationId,support_contactnumber,team);

		}
		else if(stateUpdateFor.equalsIgnoreCase("APPLICATION_APPROVED_SMS"))
		{
			smsText= MessageTemplateService.getMessage("APPLICATION_APPROVED_SMS");//AppConstant.APP_RESET_SMS_TEXT; //MessageTemplateService.getMessage("APP_RESET_SMS_TEXT"); // VALIDATION_SUBMISSION_SMS
			subject = MessageTemplateService.getMessage("APPLICATION_APPROVED_EMAIL_SUB");//"Application approved";//
			eMailBoday = MessageTemplateService.getMessage("APPLICATION_APPROVED_EMAIL_BODY");
			eMailBoday = MessageFormat.format(eMailBoday, applicationUserBean.getFirstName(), applicationId,website, support_contactnumber,team);

		}
		else if(stateUpdateFor.equalsIgnoreCase("ADMIN_APPLICATION_REJECTED_SMS"))
		{
			smsText= AppConstant.APP_ADMIN_APPLICATION_REJECTED_SMS;//MessageTemplateService.getMessage("ADMIN_APPLICATION_REJECTED_SMS");//AppConstant.APP_RESET_SMS_TEXT; //MessageTemplateService.getMessage("APP_RESET_SMS_TEXT"); // VALIDATION_SUBMISSION_SMS
			//subject = MessageTemplateService.getMessage("APPLICATION_APPROVED_EMAIL_SUB");//"Application approved";//
			//eMailBoday = MessageTemplateService.getMessage("APPLICATION_APPROVED_EMAIL_BODY");
			//eMailBoday = MessageFormat.format(eMailBoday, applicationUserBean.getFirstName(), applicationId,website, support_contactnumber,team);

		}
		
		 smsText = MessageFormat.format(smsText, applicationUserBean.getFirstName(), applicationId,website);
		if (Objects.isNull(toEmailAddress)) {
			sendSMSPostApplicationStatusChange(userloginModel.getMobileNo(), smsText);
			return;
		}
		// emailTos
		List<EmailToModel> emailTos = new ArrayList<>();
		EmailToModel emailToModel = new EmailToModel();
		emailToModel.setEmail(toEmailAddress);
		emailTos.add(emailToModel);
		// TODO get From TEmplate
		// subject
//		String subject = MessageTemplateService.getMessage("VALIDATION_SUBMISSION_EMAIL_SUB");//AppConstant.APP_RESET_EMAIL_SUBJECT;

		// emailBody
//		String eMailBoday = MessageTemplateService.getMessage("VALIDATION_SUBMISSION_EMAIL_BODY"); // AppConstant.APP_RESET_EMAIL_BODY;
//		if (!isReset) {
//			subject = AppConstant.APP_GENERATE_EMAIL_SUBJECT;
//			eMailBoday = AppConstant.APP_GENERATE_EMAIL_BODY;
//		}
//		eMailBoday = MessageFormat.format(eMailBoday, applicationUserBean.getFirstName(), applicationId,website,team);

		EmailBodyModel emailBody = new EmailBodyModel();
		emailBody.seteMailBoday(eMailBoday.getBytes());

		emailModel.setFromEmail(senderEmailAddress);
		emailModel.setSubject(subject);
		emailModel.setEmailBody(emailBody);
		emailModel.setEmailTos(emailTos);
		emailReposatory.addEmail(emailModel, userLoginBean);
		log.info("Email Application submission Validation sucessfull " + emailModel.toString());
		
//		sendSMSPostApplicationStatusChange(userloginModel.getMobileNo(), smsText);
		if(stateUpdateFor.equalsIgnoreCase("VALIDATION_SUBMISSION_SMS"))
		{   // Customer / end user
			sendSMSPostApplicationStatusChange(userloginModel.getMobileNo(), smsText);
			// Admin
			UserLogin userLoginSysobj = userLoginRepo.getUserLoginApplicationUserId(AppConstant.SYS_USER_ID);
			sendSMSPostApplicationStatusChange(userLoginSysobj.getMobileNo(), smsText);
		}else
		{   // end user  / customer 
			sendSMSPostApplicationStatusChange(userloginModel.getMobileNo(), smsText);
		}
		
	}

	private void sendSMSPostApplicationStatusChange(String mobileNo, String smsText) {
			
			 mobileNo = mobileNo.replaceAll("\\+", "");
			// "9921399990"
			smsText = smsText.replaceAll("<br/>", "");
			smsText = smsText.replaceAll("&nbsp;", "");
			smsService.sendSms(smsText, mobileNo);
		
	}

}