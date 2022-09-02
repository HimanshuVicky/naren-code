package com.assignsecurities.service.impl;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.MessageTemplateService;
import com.assignsecurities.app.exception.ValidationException;
import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.app.util.FileCloudUtil;
import com.assignsecurities.app.util.StringUtil;
import com.assignsecurities.bean.AddExistingCaseScriptBean;
import com.assignsecurities.bean.AddNewCaseScriptBean;
import com.assignsecurities.bean.AddressBean;
import com.assignsecurities.bean.ApplicationUserBean;
import com.assignsecurities.bean.CaseAadharBean;
import com.assignsecurities.bean.CaseAccountDetailBean;
import com.assignsecurities.bean.CaseBean;
import com.assignsecurities.bean.CaseCancelChequeBean;
import com.assignsecurities.bean.CaseCharteredAccountantBean;
import com.assignsecurities.bean.CaseCustomerDocumentsRequiredBean;
import com.assignsecurities.bean.CaseDocumentBean;
import com.assignsecurities.bean.CaseDocumentTemplateBean;
import com.assignsecurities.bean.CaseFeeBean;
import com.assignsecurities.bean.CaseFeesDetails;
import com.assignsecurities.bean.CaseFranchiseBean;
import com.assignsecurities.bean.CaseGeneratedDocumentsRequiredBean;
import com.assignsecurities.bean.CaseKYCBean;
import com.assignsecurities.bean.CaseLawyerBean;
import com.assignsecurities.bean.CaseNotaryBean;
import com.assignsecurities.bean.CasePanBean;
import com.assignsecurities.bean.CaseScriptBean;
import com.assignsecurities.bean.CaseScriptUpdateBean;
import com.assignsecurities.bean.CaseShareCertificateDetailsBean;
import com.assignsecurities.bean.CaseStatusUpdateBean;
import com.assignsecurities.bean.CaseSuretyInfoBean;
import com.assignsecurities.bean.CaseUpdateBean;
import com.assignsecurities.bean.CaseWarrantDetailsBean;
import com.assignsecurities.bean.CaseWintessBean;
import com.assignsecurities.bean.CustomerCasePendingDetails;
import com.assignsecurities.bean.CustomerCasePendingFolioDetails;
import com.assignsecurities.bean.CustomerDocumentsRequiredBean;
import com.assignsecurities.bean.CustomerKYCConfirmationBean;
import com.assignsecurities.bean.DeleteCaseDocumentBean;
import com.assignsecurities.bean.DocumentBean;
import com.assignsecurities.bean.DocumentMasterBean;
import com.assignsecurities.bean.FileBean;
import com.assignsecurities.bean.FranchiseBean;
import com.assignsecurities.bean.GeneratedDocumentsRequiredBean;
import com.assignsecurities.bean.LawyerCaseCommentsDtlBean;
import com.assignsecurities.bean.LawyerCaseDetailsBean;
import com.assignsecurities.bean.ProcessApplicationBean;
import com.assignsecurities.bean.QuestionerBean;
import com.assignsecurities.bean.ScriptBean;
import com.assignsecurities.bean.SuretyInfoBean;
import com.assignsecurities.bean.TemplateBean;
import com.assignsecurities.bean.UploadCaseDocument;
import com.assignsecurities.bean.UploadCaseDocumentBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.bean.ValidationError;
import com.assignsecurities.bean.WitnessBean;
import com.assignsecurities.controller.CaseValidator;
import com.assignsecurities.converter.AddressConverter;
import com.assignsecurities.converter.CaseAccountDetailConverter;
import com.assignsecurities.converter.CaseCommissionConverter;
import com.assignsecurities.converter.CaseConverter;
import com.assignsecurities.converter.CaseFeeConverter;
import com.assignsecurities.converter.CaseScriptConverter;
import com.assignsecurities.converter.CaseShareCertificateDetailsConverter;
import com.assignsecurities.converter.CaseWarrantDetailsConverter;
import com.assignsecurities.converter.CaseWitnessConverter;
import com.assignsecurities.converter.CustomerDocumentsRequiredConverter;
import com.assignsecurities.converter.FranchiseConverter;
import com.assignsecurities.converter.GeneratedDocumentsRequiredConverter;
import com.assignsecurities.converter.QuestionerConverter;
import com.assignsecurities.converter.ScriptConvertor;
import com.assignsecurities.converter.SuretyInfoConverter;
import com.assignsecurities.converter.UserLoginConverter;
import com.assignsecurities.converter.WitnessConverter;
import com.assignsecurities.dm.PropertyKeys;
import com.assignsecurities.domain.AddressModel;
import com.assignsecurities.domain.ApplicationFeeModel;
import com.assignsecurities.domain.ApplicationModel;
import com.assignsecurities.domain.ApplicationQuestionerModel;
import com.assignsecurities.domain.CaseAccountDetailModel;
import com.assignsecurities.domain.CaseCommissionModel;
import com.assignsecurities.domain.CaseDeathCertificateDtlModel;
import com.assignsecurities.domain.CaseDocumentModel;
import com.assignsecurities.domain.CaseFeeModel;
import com.assignsecurities.domain.CaseFieldsModel;
import com.assignsecurities.domain.CaseLogModel;
import com.assignsecurities.domain.CaseModel;
import com.assignsecurities.domain.CaseQuestionerModel;
import com.assignsecurities.domain.CaseScriptModel;
import com.assignsecurities.domain.CaseShareCertificateDetailsModel;
import com.assignsecurities.domain.CaseTemplateBean;
import com.assignsecurities.domain.CaseWarrantDetailsModel;
import com.assignsecurities.domain.CustomerDocumentsRequiredModel;
import com.assignsecurities.domain.DocumentMasterModel;
import com.assignsecurities.domain.DocumentModel;
import com.assignsecurities.domain.EmailBodyModel;
import com.assignsecurities.domain.EmailModel;
import com.assignsecurities.domain.EmailToModel;
import com.assignsecurities.domain.FeeMasterModel;
import com.assignsecurities.domain.GeneratedDocumentsRequiredModel;
import com.assignsecurities.domain.QuestionerModel;
import com.assignsecurities.domain.ReferralsCommisionDtlModel;
import com.assignsecurities.domain.RtaDataModel;
import com.assignsecurities.domain.RtaTemplateModel;
import com.assignsecurities.domain.ScriptModel;
import com.assignsecurities.domain.StatusMasterModel;
import com.assignsecurities.domain.SuretyInfoModel;
import com.assignsecurities.domain.UserLogin;
import com.assignsecurities.domain.WitnessModel;
import com.assignsecurities.repository.impl.AddressRepo;
import com.assignsecurities.repository.impl.ApplicationFeeRepo;
import com.assignsecurities.repository.impl.ApplicationRepo;
import com.assignsecurities.repository.impl.ApplicationScriptRepo;
import com.assignsecurities.repository.impl.CaseAccountDetailRepo;
import com.assignsecurities.repository.impl.CaseClientIdDao;
import com.assignsecurities.repository.impl.CaseCommissionDao;
import com.assignsecurities.repository.impl.CaseCustomerDocumentsRequiredRepo;
import com.assignsecurities.repository.impl.CaseDeathCertificateDtlRepo;
import com.assignsecurities.repository.impl.CaseDocumentDao;
import com.assignsecurities.repository.impl.CaseFeeDao;
import com.assignsecurities.repository.impl.CaseFieldsRepo;
import com.assignsecurities.repository.impl.CaseGeneratedDocumentsRequiredRepo;
import com.assignsecurities.repository.impl.CaseLogRepo;
import com.assignsecurities.repository.impl.CaseQuestionerRepo;
import com.assignsecurities.repository.impl.CaseRepo;
import com.assignsecurities.repository.impl.CaseScriptRepo;
import com.assignsecurities.repository.impl.CaseShareCertificateDetailsRepo;
import com.assignsecurities.repository.impl.CaseSuretyInfoDao;
import com.assignsecurities.repository.impl.CaseWarrantDetailsRepo;
import com.assignsecurities.repository.impl.CaseWitnessDao;
import com.assignsecurities.repository.impl.DocumentDao;
import com.assignsecurities.repository.impl.EmailReposatory;
import com.assignsecurities.repository.impl.FranchiseRepo;
import com.assignsecurities.repository.impl.LawyerCaseCommentsDtlRepo;
import com.assignsecurities.repository.impl.ReferralsCommisionDtlRepo;
import com.assignsecurities.repository.impl.RtaDataRepo;
import com.assignsecurities.repository.impl.RtaTemplatesDao;
import com.assignsecurities.repository.impl.ScriptRepo;
import com.assignsecurities.repository.impl.UserLoginRepo;
import com.assignsecurities.repository.impl.UtilityRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
@Slf4j
public class CaseService {

	@Autowired
	private ApplicationRepo applicationRepo;

	@Autowired
	private CaseRepo caseRepo;

	@Autowired
	private CaseScriptRepo caseScriptRepo;
	
	@Autowired
	private  ApplicationFeeRepo applicationFeeRepo;

	@Autowired
	private ApplicationScriptRepo applicationScriptRepo;

	@Autowired
	private AddressRepo addressRepo;

	@Autowired
	private DocumentDao documentDao;

	@Autowired
	private CaseDocumentDao caseDocumentDao;

	@Autowired
	private ScriptRepo scriptRepo;

	@Autowired
	private CaseWitnessDao caseWitnessDao;

	
	@Autowired
	private RtaTemplatesDao rtaTemplatesDao;

	@Autowired
	private CaseFeeDao caseFeeDao;
	
	@Autowired
	private RtaDataRepo rtaDataRepo;
	
	@Autowired
	private CaseQuestionerRepo caseQuestionerRepo;
	
	@Autowired
	private FranchiseRepo franchiseRepo;
	
	@Autowired
	private CaseTemplateService caseTemplateService;

	@Autowired
	private LoginService loginService;
	
	@Autowired
	private CaseAccountDetailRepo caseAccountDetailRepo;
	
	@Autowired
	private CaseSuretyInfoDao caseSuretyInfoDao;
	
	@Autowired
	private CaseClientIdDao caseClientIdDao;
	
	@Autowired
	private CaseCustomerDocumentsRequiredRepo caseCustomerDocumentsRequiredRepo;
	
	@Autowired
	private CaseGeneratedDocumentsRequiredRepo caseGeneratedDocumentsRequiredRepo;
	
	@Autowired
	private UtilityRepo utilityRepo;
	
	@Autowired
	private CaseKycChequeScanService caseKycChequeScanService;
	
	@Autowired
	private CaseShareCertificateDetailsRepo caseShareCertificateDetailsRepo; 
	
	@Autowired
	private CaseWarrantDetailsRepo caseWarrantDetailsRepo;
	
	@Autowired
	private DocumentService documentService;
	
	@Autowired
	private CaseLogRepo caseLogRepo; 
	
	@Autowired
	private SmsService smsService;
	
	@Autowired
	private ApplicationUserService  applicationUserService;
	
	@Autowired 
	private EmailReposatory emailReposatory;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private CaseDeathCertificateDtlRepo caseDeathCertificateDtlRepo;
	
	@Autowired
	private UserLoginRepo userLoginRepo;
	
	@Autowired
	private CaseCommissionDao caseCommissionDao;
	
	@Autowired
	private ReferralsCommisionDtlRepo referralsCommisionDtlRepo;
	
	@Autowired
	private LawyerCaseCommentsDtlService lawyerCaseCommentsDtlService;
	
	@Autowired
	private LawyerCaseCommentsDtlRepo lawyerCaseCommentsDtlRepo;
	
	@Autowired
	private CaseFieldsRepo caseFieldsRepo;
	
	@Autowired
	private UtilityService utilityService;
	

	public CaseBean getById(Long caseId, UserLoginBean userLoginBean) {
		CaseBean bean = CaseConverter.convert(caseRepo.getById(caseId, userLoginBean));
		UserLoginBean appUserBean = loginService.getUserLogin(bean.getUserId());
		bean.setMobileNumber(appUserBean.getMobileNo());
		bean.setAddress(AddressConverter.convert(addressRepo.getAddressById(bean.getAddressId())));
		bean.setCommAddress(AddressConverter.convert(addressRepo.getAddressById(bean.getCommAddressId())));
		bean.setEMail(appUserBean.getApplicationUserBean().getEmailId());
		//Script Folio
		List<CaseScriptModel> caseScriptModels = caseScriptRepo.getByCaseId(caseId);
		bean.setScripts(CaseScriptConverter.convert(caseScriptModels));
		List<Long> scriptIds = caseScriptModels.stream().map(script -> script.getScriptId()).distinct()
				.collect(Collectors.toList());
		List<ScriptBean> scripts = ScriptConvertor.convert(scriptRepo.getScripts(scriptIds));
//		Double 
		List<String> preeSigningStatuses = Arrays.asList(AppConstant.CaseStatus.WaitingSubmission.label, 
				AppConstant.CaseStatus.WaitingProcessingFee.label, AppConstant.CaseStatus.WaitingProcessingFeeConfirmation.label, AppConstant.CaseStatus.WaitingCustomerAadhar.label);
		AtomicBoolean maskFolio = new AtomicBoolean(Boolean.FALSE);

		if (preeSigningStatuses.contains(bean.getStatus()) && !(AppConstant.USER_TYPE_ADMIN
				.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())
				|| AppConstant.USER_TYPE_CC.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType()))) {
			maskFolio.set(Boolean.TRUE);
		}
		
//		AtomicDouble scriptTotal = new AtomicDouble(0);
		double scriptTotal =0;
		for(CaseScriptBean s  : bean.getScripts()) {
			Optional<ScriptBean> scriptOpt = scripts.stream().filter(sc -> sc.getId().equals(s.getScriptId()))
					.findFirst();
			if(scriptOpt.isPresent()) {
				ScriptBean script = scriptOpt.get();
				scriptTotal = scriptTotal +script.getMarketPrice();
				s.setScriptBean(script);
				if (maskFolio.get()) {
					if (ArgumentHelper.isValid(script.getFolioNumber())) {
						script.setFolioNumber(StringUtil.maskCardNumber(script.getFolioNumber(), 1, 3));
					} else if (ArgumentHelper.isValid(script.getDpIdClientId())) {
						script.setDpIdClientId(StringUtil.maskCardNumber(script.getDpIdClientId(), 1, 3));
					}
				}
			}
		}
		bean.setScriptTotal(scriptTotal);
		bean.setSecurityCode(bean.getScripts().get(0).getScriptBean().getSecurityCode());
		bean.setIsinNumber(bean.getScripts().get(0).getScriptBean().getIsinCode());
		bean.setMarketPrice(bean.getScripts().get(0).getScriptBean().getMarketPrice());
		bean.setNominalValue(bean.getScripts().get(0).getScriptBean().getNominalValue());
		bean.setNumberOfShare(bean.getScripts().get(0).getScriptBean().getNumberOfShare());
		
		CaseQuestionerModel caseQuestionerModel = caseQuestionerRepo.getCaseQuestionerAnsByCaseId(caseId);
		List<QuestionerModel> questioners = caseQuestionerModel.getQuestioners();
		List<CaseShareCertificateDetailsModel> existingcaseShareCertificateDetailsModels = caseShareCertificateDetailsRepo
				.getCaseShareCertificateDetailsByCaseId(caseId);
		List<CaseWarrantDetailsModel> existingWrrantDetailsModels =  caseWarrantDetailsRepo.getCaseShareCertificateDetailsByCaseId(caseId);
		
		bean.getScripts().forEach(s -> {
			List<QuestionerModel> questionersFiltered = questioners.stream().filter(sc -> sc.getParentScriptId().equals(s.getId())).collect(Collectors.toList());
			List<QuestionerBean> questionerBeans = QuestionerConverter.convert(questionersFiltered);
			s.setQuestioners(questionerBeans);
			List<CaseShareCertificateDetailsModel> caseShareCertificateDetailsModels = existingcaseShareCertificateDetailsModels.stream().filter(sc -> sc.getScriptId().equals(s.getScriptId())).collect(Collectors.toList());
			List<CaseShareCertificateDetailsBean> caseShareCertificateDetailsBeans = CaseShareCertificateDetailsConverter.convert(caseShareCertificateDetailsModels);
			s.setCaseShareCertificateDetails(caseShareCertificateDetailsBeans);
			
			List<CaseWarrantDetailsModel> caseWarrantDetailsModels  = existingWrrantDetailsModels.stream().filter(sc -> sc.getScriptId().equals(s.getScriptId())).collect(Collectors.toList());
			List<CaseWarrantDetailsBean> caseWarrantDetailsBeans =CaseWarrantDetailsConverter.convert(caseWarrantDetailsModels);
			s.setCaseWarrantDetails(caseWarrantDetailsBeans);
		});
		
		
		//Masking Pan and Aadhar
		if (!(AppConstant.USER_TYPE_ADMIN
				.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType()))){
			if(Objects.nonNull(bean.getAadharNumber()) && !"XXX".equalsIgnoreCase(bean.getAadharNumber())) {
				bean.setAadharNumber(StringUtil.maskCardNumber(bean.getAadharNumber(), 0, 4));
			}
			if(Objects.nonNull(bean.getPanNumber())) {
				bean.setPanNumber(StringUtil.maskCardNumber(bean.getPanNumber(), 0, 4));
			}
		}
			
		
		// Processing Payment Info
		// Only three Fee types copied from application
		List<CaseFeeModel> feeModels = caseFeeDao.getByCaseId(caseId);
		CaseFeesDetails paymentDetails = null;
		// Additional Fees Details
		CaseFeesDetails additionalFeesDetails = null;
//		CaseClientIdModel model = caseClientIdDao.getCaseById(caseId);
		AtomicBoolean hideFee = new AtomicBoolean(Boolean.FALSE);
		List<String> sendAllThreeFees = Arrays.asList(AppConstant.CaseStatus.WaitingSubmission.label, AppConstant.CaseStatus.WaitingProcessingFeeConfirmation.label, AppConstant.CaseStatus.WaitingCustomerAadhar.label);
		if (sendAllThreeFees.contains(bean.getStatus())) {
			hideFee.set(Boolean.TRUE);
		}
		List<CaseFeeBean> fees = new ArrayList<>();
		feeModels.forEach(fee->{
			if(fee.getFeeFor().contains("Processing Fees") || fee.getFeeFor().contains("Advance") || hideFee.get()) {
				fees.add(CaseFeeConverter.convert(fee));
			}
		});
		paymentDetails = CaseFeesDetails.builder().caseId(caseId).fees(fees).build();
		
		List<CaseFeeBean> feesToAdd  = new ArrayList<>();
		//Add Other fees
		List<FeeMasterModel> feeMasterModels =  utilityRepo.getMasterFeeList();
		feeMasterModels.forEach(feeMaster->{
			Optional<CaseFeeModel> caseFeeOpt = feeModels.stream().filter(sc -> sc.getFeeFor().equals(feeMaster.getFeeFor())).findFirst();
			if(feeMaster.getFeeFor().contains("Agreement Fees")){
				CaseFeeModel caseFee= caseFeeOpt.get();
				if(AppConstant.FeeType.Percent.name().equals(caseFee.getFeeType())) {
					Double calculatedFeeValue = bean.getScriptTotal() * caseFee.getFeeValue() /100;
					calculatedFeeValue = StringUtil.roundDouble(calculatedFeeValue, 2);
					caseFee.setFeeValue(calculatedFeeValue);
				}
				feesToAdd.add(CaseFeeConverter.convert(caseFee));
			}else  if(!(feeMaster.getFeeFor().contains("Processing Fees") || feeMaster.getFeeFor().contains("Advance"))) {
				if(caseFeeOpt.isPresent()) {
					feesToAdd.add(CaseFeeConverter.convert(caseFeeOpt.get()));
				}else {
					feesToAdd.add(CaseFeeBean.builder().caseId(caseId).feeFor(feeMaster.getFeeFor())
							.feeType(feeMaster.getFeeType()).isGSTApplicable(feeMaster.getIsGSTApplicable()).build());
				}
			}
		});
		Double feeValueTotal = 0D;
		Double receivedFeeValueTotal = 0D;
		for (CaseFeeBean feeBean : feesToAdd) {
			if(Objects.nonNull(feeBean.getFeeValue())) {
				feeValueTotal = feeValueTotal + feeBean.getFeeValue();
			}else {
				feeBean.setFeeValue(0D);
			}
			if(Objects.nonNull(feeBean.getReceivedFeeValue())) {
				receivedFeeValueTotal = receivedFeeValueTotal + feeBean.getReceivedFeeValue();
			}else {
				feeBean.setReceivedFeeValue(0D);
			}
		}
		additionalFeesDetails = CaseFeesDetails.builder().caseId(caseId).fees(feesToAdd)
//				.feeValueTotal(feeValueTotal)
//				.receivedFeeValueTotal(receivedFeeValueTotal)
				.build();
		bean.setPaymentDetails(paymentDetails);
		bean.setAdditionalFeesDetails(additionalFeesDetails);

		//eAdhar
		//case Commission Details
		bean.setCommission(CaseCommissionConverter.convert(caseCommissionDao.getCaseById(caseId)));
		
		//Assign Franchise
		if(Objects.nonNull(bean.getFranchiseId())  && bean.getFranchiseId()>0) {
			FranchiseBean franchiseBean =  FranchiseConverter.convert(franchiseRepo.getFranchiseById(bean.getFranchiseId()));
			if ((AppConstant.USER_TYPE_END_USER
					.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType()))) {
				franchiseBean.setOwnerFirstName("XXXXX XXXXX");
				franchiseBean.setName("XXXXX XXXXX");
			}
			bean.setFranchise(CaseFranchiseBean.builder().caseId(caseId).franchiseId(bean.getFranchiseId()).franchise(franchiseBean).build());
		}
		
		//Assign Lawyer
		if(Objects.nonNull(bean.getAssignLawyerId())  && bean.getAssignLawyerId()>0) {
			UserLoginBean lawyerUser = loginService.getUserLogin(bean.getAssignLawyerId());
			if(Objects.nonNull(lawyerUser)) {
				lawyerUser.getApplicationUserBean().setNonCustomerPin(null);
				lawyerUser.setPin(null);
			}
			bean.setLawyer(CaseLawyerBean.builder().caseId(caseId).lawyerId(bean.getAssignLawyerId()).lawyer(lawyerUser).build());
		}
		
	    
		//Lawyer comment details
		bean.setLawyerCaseCommentsDtlBeanlst(lawyerCaseCommentsDtlService.getAllLawyerCaseCommentsDtls(caseId));
		
		
		//Notary Partner
		if(Objects.nonNull(bean.getAssignNotaryId())  && bean.getAssignNotaryId()>0) {
			UserLoginBean notaryPartnerUser = loginService.getUserLogin(bean.getAssignNotaryId());
			if(Objects.nonNull(notaryPartnerUser)) {
				notaryPartnerUser.getApplicationUserBean().setNonCustomerPin(null);
				notaryPartnerUser.setPin(null);
			}
			bean.setNotaryPartner(CaseNotaryBean.builder().caseId(caseId).notaryPartnerId(bean.getAssignNotaryId()).notaryPartner(notaryPartnerUser).build());
		}
				
		//Chartered Accountant
		if(Objects.nonNull(bean.getAssignLawyerId())  && bean.getAssignLawyerId()>0) {
			UserLoginBean cAUser = loginService.getUserLogin(bean.getCharteredAccountantId());
			if(Objects.nonNull(cAUser)) {
				cAUser.getApplicationUserBean().setNonCustomerPin(null);
				cAUser.setPin(null);
			}
			bean.setCharteredAccountant(CaseCharteredAccountantBean.builder().caseId(caseId).charteredAccountantId(bean.getCharteredAccountantId()).charteredAccountant(cAUser).build());
		}
		
		//Generate Letter
		CaseDocumentBean rtaLetter =CaseDocumentBean.builder().caseId(caseId).build();
		List<CaseDocumentModel>  caseDocumentModels = caseDocumentDao.getByCaseId(caseId);
		boolean isShareHolderDeathCertDocPresent=false;
		if(ArgumentHelper.isNotEmpty(caseDocumentModels)) {
			caseDocumentModels.forEach(cd->{
				if(cd.getType().equalsIgnoreCase(AppConstant.DocumentType.RtaLetter1.name())
						||cd.getType().equalsIgnoreCase(AppConstant.DocumentType.RtaLetter1.label)) {
					rtaLetter.setRtaLetter1Id(cd.getDocumentId());
				}else if(cd.getType().equalsIgnoreCase(AppConstant.DocumentType.RtaLetter2.name())
						||cd.getType().equalsIgnoreCase(AppConstant.DocumentType.RtaLetter2.label)) {
					rtaLetter.setRtaLetter2Id(cd.getDocumentId());
				}
			});
			bean.setRtaLetter(rtaLetter);
			
			//Upload Signed Documents
			UploadCaseDocumentBean uploadSignedDocuments = UploadCaseDocumentBean.builder().caseId(caseId).build();
			List<UploadCaseDocument> signedDocuments= getUploadeSignedDocuments(caseDocumentModels);
			uploadSignedDocuments.setDocuments(signedDocuments);
			bean.setUploadSignedDocuments(uploadSignedDocuments);
			
			//Upload RTA Response
			UploadCaseDocumentBean uploadRtaDocuments = UploadCaseDocumentBean.builder().caseId(caseId).build();
			List<UploadCaseDocument> rtaDocuments = getUploadRTAResponse(caseDocumentModels);
			uploadRtaDocuments.setDocuments(rtaDocuments);
			bean.setUploadRtaResponseDocuments(uploadRtaDocuments);
			
			Optional<CaseDocumentModel> shareHolderDeathDoc = caseDocumentModels.stream()
					.filter(doc -> AppConstant.DocumentType.ShareHolderDeathCertificate.label.equalsIgnoreCase(doc.getType())).findFirst();
			
			if(shareHolderDeathDoc.isPresent()) {
				bean.setDhareHolderDeathCertificate1Id(shareHolderDeathDoc.get().getDocumentId());
				isShareHolderDeathCertDocPresent=true;
			}
			
			
		}
		
		//Account Details
		CaseAccountDetailBean accountDetailBean = CaseAccountDetailConverter.convert(caseAccountDetailRepo.getByCaseId(caseId));
//		if (AppConstant.USER_TYPE_END_USER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//			accountDetailBean.setIEPFUserName(null);
//		}
		bean.setAccountDetail(accountDetailBean);
		
		//Witness Info
		List<WitnessModel> witnessModels = caseWitnessDao.getByCaseId(caseId);
		if(ArgumentHelper.isNotEmpty(witnessModels)) {
			bean.setWintess(CaseWintessBean.builder().caseId(caseId).witnesses(WitnessConverter.convert(witnessModels)).build());
		}
		
		//Surety Info
		List<SuretyInfoModel> suretyInfoModels =caseSuretyInfoDao.getByCaseId(caseId);
		if(ArgumentHelper.isNotEmpty(suretyInfoModels)) {
			bean.setSuretyInfo(CaseSuretyInfoBean.builder().caseId(caseId).suretyInfo(SuretyInfoConverter.convert(suretyInfoModels)).build());
		}
		
		
		CaseQuestionerModel caseQuestionerModelNew = caseQuestionerRepo.getCaseQuestionerAnsByCaseId(caseId);
		List<QuestionerModel> questionersNew = caseQuestionerModelNew.getQuestioners();
		
		 Optional<QuestionerModel> quesQ1YesOpt = questionersNew.stream().filter(q->q.getQuestion().contains("Q.1 :")
	                && (q.getAnswer().equalsIgnoreCase("Yes") || q.getAnswer().equalsIgnoreCase("Y"))).findFirst();
	    if(quesQ1YesOpt.isPresent()) {
	    	bean.setIsShareholderDeathCertificationRequire(Boolean.TRUE);   
	    	if(!isShareHolderDeathCertDocPresent) {
	    		bean.setDhareHolderDeathCertificate1Id(0L);
	    	}
	    } else {
	    	bean.setIsShareholderDeathCertificationRequire(Boolean.FALSE); 
	    }
		
		
		
		//Customer Documents Required
		List<CustomerDocumentsRequiredModel> customerDocumentsRequiredModels = caseCustomerDocumentsRequiredRepo.getByCaseId(caseId);
		if(ArgumentHelper.isNotEmpty(customerDocumentsRequiredModels)) {
			
			List<CustomerDocumentsRequiredBean>  customerDocumentsRequiredBeans = CustomerDocumentsRequiredConverter.convert(customerDocumentsRequiredModels);
			customerDocumentsRequiredBeans.forEach(customerDocumentsRequiredBeanobj->{
				if(!(Objects.isNull(customerDocumentsRequiredBeanobj.getId()) || customerDocumentsRequiredBeanobj.getId() <=0))
				 {  customerDocumentsRequiredBeanobj.setIsCustomerDocumentRequire(Boolean.TRUE);
				 }
				else{
					customerDocumentsRequiredBeanobj.setIsCustomerDocumentRequire(Boolean.FALSE);
				}
				if(customerDocumentsRequiredBeanobj.getDoccumentName().equalsIgnoreCase(AppConstant.DocumentType.Aadhar.label)
						||(customerDocumentsRequiredBeanobj.getDoccumentName().equalsIgnoreCase(AppConstant.DocumentType.Pan.label))
						||(customerDocumentsRequiredBeanobj.getDoccumentName().equalsIgnoreCase(AppConstant.DocumentType.CancelCheque.label)))
						
				 {
					customerDocumentsRequiredBeanobj.setIsCustomerDocumentMandatory(Boolean.TRUE);
				 }
				else
				{
					customerDocumentsRequiredBeanobj.setIsCustomerDocumentMandatory(Boolean.FALSE);
				}
				
				if((customerDocumentsRequiredBeanobj.getDoccumentName().equalsIgnoreCase(AppConstant.DocumentType.CourierReceipt.label)))
				{
					customerDocumentsRequiredBeanobj.setIsCustomerDocumentRequire(Boolean.TRUE);
					customerDocumentsRequiredBeanobj.setIsCustomerDocumentMandatory(Boolean.TRUE);
				}
				if((customerDocumentsRequiredBeanobj.getDoccumentName().equalsIgnoreCase(AppConstant.DocumentType.ShareHolderDeathCertificate.label)) && (bean.getIsShareholderDeathCertificationRequire()))
				{
					customerDocumentsRequiredBeanobj.setIsCustomerDocumentRequire(Boolean.TRUE);
					customerDocumentsRequiredBeanobj.setIsCustomerDocumentMandatory(Boolean.TRUE);
				}
			});
			
			bean.setCustomerDocuments(CaseCustomerDocumentsRequiredBean.builder().caseId(caseId)
					.customerDocuments(customerDocumentsRequiredBeans).build());
		}
		
		//Generated Documents
		List<String> hideGneratedButtonList = new ArrayList<>();
		hideGneratedButtonList.add("RtaLetter1");
		hideGneratedButtonList.add("RtaLetter2");
		hideGneratedButtonList.add("CustomerAgreement");
		hideGneratedButtonList.add("CustomerAgreement");
		List<GeneratedDocumentsRequiredModel> generatedDocumentsRequiredModels = caseGeneratedDocumentsRequiredRepo.getByCaseId(caseId);
		if(ArgumentHelper.isNotEmpty(customerDocumentsRequiredModels)) {
			List<GeneratedDocumentsRequiredBean>  generatedDocumentsRequiredBeans = GeneratedDocumentsRequiredConverter.convert(generatedDocumentsRequiredModels);
			generatedDocumentsRequiredBeans.forEach(generatedDocumentsRequiredBeanobj->{
				if (!(Objects.isNull(generatedDocumentsRequiredBeanobj.getId())
						|| generatedDocumentsRequiredBeanobj.getId() <= 0)) {
					generatedDocumentsRequiredBeanobj.setIsGeneratedDocumentRequire(Boolean.TRUE);
				} else {
					generatedDocumentsRequiredBeanobj.setIsGeneratedDocumentRequire(Boolean.FALSE);
				}
				if(hideGneratedButtonList.contains(generatedDocumentsRequiredBeanobj.getDocumentName())
						|| hideGneratedButtonList.contains(generatedDocumentsRequiredBeanobj.getDocumentName().trim()) ) {
					generatedDocumentsRequiredBeanobj.setIsGenerateButtonVisible(Boolean.FALSE);
					generatedDocumentsRequiredBeanobj.setIsGeneratedDocumentRequire(Boolean.TRUE);
				}else {
					generatedDocumentsRequiredBeanobj.setIsGenerateButtonVisible(Boolean.TRUE);
				}
			});
			bean.setGeneratedDocuments(CaseGeneratedDocumentsRequiredBean.builder().caseId(caseId)
					.generatedDocuments(generatedDocumentsRequiredBeans).build());
		}
		
		/*
		 * Temp comment for Notary page developme
		 */
		
		List<StatusMasterModel> statusMasterModels =utilityRepo.getMasterStatusList(userLoginBean);
		CaseAccessControlHelper.setSectionAcessControl(bean, statusMasterModels, userLoginBean);
//		CaseClientIdModel caseClientIdModel = caseClientIdDao.getCaseById(caseId);
//		if(Objects.nonNull(caseClientIdModel) && Objects.nonNull(caseClientIdModel.getClientId1()) && Objects.nonNull(caseClientIdModel.getClientId2()) ) {
//			bean.setIsAdmineAdharComplete(Boolean.TRUE);
//		}else {
//			bean.setIsAdmineAdharComplete(Boolean.FALSE);
//		}
		return bean;
	}
	
	
	
	
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void updateCase(CaseUpdateBean caseUpdateBean, UserLoginBean userLoginBean) {
		
		List<ValidationError> errorList = new ArrayList<>();
		if(Objects.isNull(caseUpdateBean.getId())) {
			errorList.add(ValidationError.builder().message(String.format("Please provide case Id %d. ", caseUpdateBean.getId())).build());
		}
		if(ArgumentHelper.isTrueFAlse(caseUpdateBean.getIsFolioScriptModified()) && Objects.isNull(caseUpdateBean.getScript())) {
			errorList.add(ValidationError.builder().message("Please provide Script/Folio Details.").build());
		}
		if(ArgumentHelper.isTrueFAlse(caseUpdateBean.getIsPaymentDetailsModified()) && Objects.isNull(caseUpdateBean.getPaymentDetails())) {
			errorList.add(ValidationError.builder().message("Please provide Payment Details.").build());
		}else {
			 List<CaseFeeBean> fees = new ArrayList<>();
			 caseUpdateBean.getPaymentDetails().getFees().forEach(fee->{
					if(fee.getFeeFor().contains("Processing Fees") || fee.getFeeFor().contains("Advance")) {
						fees.add(fee);
					}
				});
			 if(fees.size()<2) {
				 errorList.add(ValidationError.builder().message("Please provide Payment Details.").build());
			 }
		}
		//Assign Franchise
		if(ArgumentHelper.isTrueFAlse(caseUpdateBean.getIsAssignFranchiseModified()) && Objects.isNull(caseUpdateBean.getFranchise())) {
			errorList.add(ValidationError.builder().message("Please provide Assign Franchise Details.").build());
		}
		//Assign Lawyer
		if(ArgumentHelper.isTrueFAlse(caseUpdateBean.getIsAssignLawyerModified()) && Objects.isNull(caseUpdateBean.getLawyer())) {
			errorList.add(ValidationError.builder().message("Please provide Assign Lawyer Details.").build());
		}
		
		//Assign Notary Partner
				if(ArgumentHelper.isTrueFAlse(caseUpdateBean.getIsAssignNotaryPartnerModified()) && Objects.isNull(caseUpdateBean.getNotaryPartner())) {
					errorList.add(ValidationError.builder().message("Please provide Assign Notary Partner Details.").build());
				}

		//Assign Chartered Accountant Partner
		if(ArgumentHelper.isTrueFAlse(caseUpdateBean.getIsAssignCharteredAccountantPartnerModified()) && Objects.isNull(caseUpdateBean.getCharteredAccountant())) {
			errorList.add(ValidationError.builder().message("Please provide Assign Chartered Accountant Details.").build());
		}
				
		//AdditionalFeesDetails
		if(ArgumentHelper.isTrueFAlse(caseUpdateBean.getIsAdditionalFeesDetailsModified()) && Objects.isNull(caseUpdateBean.getAdditionalFeesDetails())) {
			errorList.add(ValidationError.builder().message("Please provide Additional Fees Details.").build());
		}
		
		//Account Details
		if(ArgumentHelper.isTrueFAlse(caseUpdateBean.getIsAccountDetailModified()) && Objects.isNull(caseUpdateBean.getAccountDetail())) {
			errorList.add(ValidationError.builder().message("Please provide Account Details.").build());
		}
		
		//Witness Info
		if(ArgumentHelper.isTrueFAlse(caseUpdateBean.getIsWintessModified()) && Objects.isNull(caseUpdateBean.getWintess())) {
			errorList.add(ValidationError.builder().message("Please provide Witness Info Details.").build());
		}
		
		//Surety Info
		if(ArgumentHelper.isTrueFAlse(caseUpdateBean.getIsSuretyInfoModified()) && Objects.isNull(caseUpdateBean.getSuretyInfo())) {
			errorList.add(ValidationError.builder().message("Please provide Surety Info Details.").build());
		}
		CaseModel caseModel = caseRepo.getById(caseUpdateBean.getId(), userLoginBean);
		if(Objects.isNull(caseModel)) {
			errorList.add(ValidationError.builder().message(String.format("Provided case Id %d. not exists ", caseUpdateBean.getId())).build());
		}
		if (ArgumentHelper.isNotEmpty(errorList)) {
			throw new ValidationException(errorList);
		}
		String currentStatus = caseModel.getStatus();
		if(currentStatus.equals(caseUpdateBean.getStatus())) {
			caseUpdateBean.setStatus(null);
		}
		Long caseId = caseUpdateBean.getId();
		
		// Processing Payment Info
		List<CaseFeeModel>  caseFeeModelsPaymentDetails = Collections.emptyList();
		List<CaseFeeModel> feeModelsPrevious = caseFeeDao.getByCaseId(caseId);
		if(ArgumentHelper.isTrueFAlse(caseUpdateBean.getIsPaymentDetailsModified()) && Objects.nonNull(caseUpdateBean.getPaymentDetails())) {
			
			caseFeeDao.delete(caseId);
			//Add Processing Fee
			List<CaseFeeModel>  caseFeeModels = CaseFeeConverter.convertL(caseUpdateBean.getPaymentDetails().getFees());
			Optional<CaseFeeModel> agreementFeeOpt= caseFeeModels.stream().filter(cf-> cf.getFeeFor().contains("Agreement")).findFirst();
			if(!agreementFeeOpt.isPresent()) {
				feeModelsPrevious.forEach(fee->{
					if(fee.getFeeFor().contains("Agreement")) {
						caseFeeModels.add(fee);
					}
				});
			}
			caseFeeModels.forEach(cf->{
				if(Objects.isNull(cf.getCaseId()) || !ArgumentHelper.isPositive(cf.getCaseId())) {
					cf.setCaseId(caseId);
				}
			});
			caseFeeDao.addAll(caseFeeModels);
			caseFeeModelsPaymentDetails = caseFeeModels;
			if(Objects.isNull(caseUpdateBean.getStatus())) {
				if(ArgumentHelper.isTrueFAlse(caseUpdateBean.getIsFeeProcessed())) {
					caseModel.setStatusAutoUpdate(Boolean.TRUE);
					caseModel.setStatus(AppConstant.CaseStatus.WaitingCustomerAadhar.label);
					/*
					 * payment approved sms
					 */
					String website = "http://www.findmymoney.in";
					String smsText= MessageTemplateService.getMessage("PAYMENT_APPROVED_SMS");
					
//					ApplicationUserBean applicationUserBean = applicationUserService.getApplicationUserById(caseModel.getUserId());
//					UserLoginBean userLoginBeanObj = loginService.getUserLoginApplicationUserId(applicationUserBean.getId());
					UserLoginBean userLoginBeanObj = loginService.getUserLogin(caseModel.getUserId());
					ApplicationUserBean applicationUserBean = userLoginBeanObj.getApplicationUserBean();
					if(Objects.isNull(applicationUserBean)) {
						applicationUserBean = applicationUserService.getApplicationUserById(userLoginBeanObj.getApplicationUserId());
					}
					smsText = MessageFormat.format(smsText, applicationUserBean.getFirstName(), caseModel.getReferenceNumber(), website);
					sendSMSPostApplicationStatusChange(userLoginBeanObj.getMobileNo(),smsText);
					
				}else {
					List<CaseFeeModel> caseFeeModelsTemp = caseFeeModels.stream().filter(m -> ArgumentHelper.isValid(m.getRefNo()))
							.collect(Collectors.toList());
					//AppConstant.USER_TYPE_ADMIN.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType()) &&
					if(ArgumentHelper.isNotEmpty(caseFeeModelsTemp) && caseFeeModelsTemp.size()>=2) {
						caseModel.setStatusAutoUpdate(Boolean.TRUE);
						caseModel.setStatus(AppConstant.CaseStatus.WaitingProcessingFeeConfirmation.label);
						/*
						 * first payment updated SMS
						 */
						//String website = "http://www.findmymoney.in";
						String smsText= MessageTemplateService.getMessage("PAYMENT_UPDATED_SMS"); // CASE_CREATED_SMS
//						ApplicationUserBean applicationUserBean = applicationUserService.getApplicationUserById(AppConstant.SYS_USER_ID);
//						UserLoginBean userLoginBeanObj = loginService.getUserLoginApplicationUserId(applicationUserBean.getId());
						UserLoginBean userLoginBeanObj = loginService.getUserLogin(caseModel.getUserId());
						ApplicationUserBean applicationUserBean = userLoginBeanObj.getApplicationUserBean();
						if(Objects.isNull(applicationUserBean)) {
							applicationUserBean = applicationUserService.getApplicationUserById(userLoginBeanObj.getApplicationUserId());
						}
						smsText = MessageFormat.format(smsText, applicationUserBean.getFirstName(), caseModel.getReferenceNumber());
						sendSMSPostApplicationStatusChange(userLoginBeanObj.getMobileNo(),smsText);
						
					}
				}
			}
		}
		
		//Assign Franchise
		if(ArgumentHelper.isTrueFAlse(caseUpdateBean.getIsAssignFranchiseModified())
				&& Objects.nonNull(caseUpdateBean.getFranchise())
				&& Objects.nonNull(caseUpdateBean.getFranchise().getFranchiseId())) {
			
			if (caseUpdateBean.getFranchise().getFranchiseId() != caseModel.getFranchiseId() )
			{  String smsText= AppConstant.APP_ASSIGNED_CASE_SMS;  //MessageTemplateService.getMessage("CASE_ASSIGNED");
			   ApplicationUserBean applicationFranchiseUserBean = applicationUserService
										.getApplicationUsersDtlForGivenFranchiseOwnerIdAndUserType(caseUpdateBean.getFranchise().getFranchiseId(),
												AppConstant.USER_TYPE_FRANCHISE);
								
				UserLoginBean franchiseUserLoginBeanObj = loginService.getUserLoginApplicationUserId(applicationFranchiseUserBean.getId());
				smsText = MessageFormat.format(smsText, applicationFranchiseUserBean.getFirstName(), caseModel.getReferenceNumber());
				sendSMSPostApplicationStatusChange(franchiseUserLoginBeanObj.getMobileNo(),smsText);
				ReferralsCommisionDtlModel referralsCommisionDtlModelFranchise = referralsCommisionDtlRepo.getReferralPartnerCommisionDtlforGivenFranchiseId(caseUpdateBean.getFranchise().getFranchiseId());
				caseCommissionDao.updateReferralDocumentProcFeeComm(caseId, referralsCommisionDtlModelFranchise.getDocuProcessingfeeCommision());
			}
			caseModel.setFranchiseId(caseUpdateBean.getFranchise().getFranchiseId());
			
		}
		
		//Assign Lawyer
		if(ArgumentHelper.isTrueFAlse(caseUpdateBean.getIsAssignLawyerModified())
				&& Objects.nonNull(caseUpdateBean.getLawyer())
				&& Objects.nonNull(caseUpdateBean.getLawyer().getLawyerId())) {
			
			if (caseUpdateBean.getLawyer().getLawyerId() != caseModel.getAssignLawyerId())
			{    //String smsText= MessageTemplateService.getMessage("CASE_ASSIGNED");
				String smsText= AppConstant.APP_ASSIGNED_CASE_SMS; 
//				ApplicationUserBean applicationLawyerBean = applicationUserService.getApplicationUserById(caseUpdateBean.getLawyer().getLawyerId());
				UserLoginBean userLoginBeanObj = loginService.getUserLogin(caseUpdateBean.getLawyer().getLawyerId());
				ApplicationUserBean applicationLawyerBean = userLoginBeanObj.getApplicationUserBean();
				if(Objects.isNull(applicationLawyerBean)) {
					applicationLawyerBean = applicationUserService.getApplicationUserById(userLoginBeanObj.getApplicationUserId());
				}
					
				UserLoginBean lawyerLoginBeanObj = loginService.getUserLoginApplicationUserId(applicationLawyerBean.getId());
				smsText = MessageFormat.format(smsText,applicationLawyerBean.getFirstName(), caseModel.getReferenceNumber());
				sendSMSPostApplicationStatusChange(lawyerLoginBeanObj.getMobileNo(),smsText);
			}
				caseModel.setAssignLawyerId(caseUpdateBean.getLawyer().getLawyerId());
		}
		
		/*
		 * Assinged Notary Partner
		 */
		
				if(ArgumentHelper.isTrueFAlse(caseUpdateBean.getIsAssignNotaryPartnerModified())
						&& Objects.nonNull(caseUpdateBean.getNotaryPartner())
						&& Objects.nonNull(caseUpdateBean.getNotaryPartner().getNotaryPartnerId())) {
					
					if (caseUpdateBean.getNotaryPartner().getNotaryPartnerId() != caseModel.getAssignNotaryId())
					{    //String smsText= MessageTemplateService.getMessage("CASE_ASSIGNED");
						String smsText= AppConstant.APP_ASSIGNED_CASE_SMS; 
//						ApplicationUserBean applicationLawyerBean = applicationUserService.getApplicationUserById(caseUpdateBean.getLawyer().getLawyerId());
						UserLoginBean userLoginBeanObj = loginService.getUserLogin(caseUpdateBean.getNotaryPartner().getNotaryPartnerId());
						ApplicationUserBean applicationNPBean = userLoginBeanObj.getApplicationUserBean();
						if(Objects.isNull(applicationNPBean)) {
							applicationNPBean = applicationUserService.getApplicationUserById(userLoginBeanObj.getApplicationUserId());
						}
							
						UserLoginBean npLoginBeanObj = loginService.getUserLoginApplicationUserId(applicationNPBean.getId());
						smsText = MessageFormat.format(smsText,applicationNPBean.getFirstName(), caseModel.getReferenceNumber());
						sendSMSPostApplicationStatusChange(npLoginBeanObj.getMobileNo(),smsText);
					}
						caseModel.setAssignNotaryId(caseUpdateBean.getNotaryPartner().getNotaryPartnerId());
				}
				
				/*
				 * Assinged Chartered Partner
				 */
				
						if(ArgumentHelper.isTrueFAlse(caseUpdateBean.getIsAssignCharteredAccountantPartnerModified())
								&& Objects.nonNull(caseUpdateBean.getCharteredAccountant())
								&& Objects.nonNull(caseUpdateBean.getCharteredAccountant().getCharteredAccountantId())) {
							
							if (caseUpdateBean.getCharteredAccountant().getCharteredAccountantId() != caseModel.getCharteredAccountantId())
							{    //String smsText= MessageTemplateService.getMessage("CASE_ASSIGNED");
								String smsText= AppConstant.APP_ASSIGNED_CASE_SMS; 
//								ApplicationUserBean applicationLawyerBean = applicationUserService.getApplicationUserById(caseUpdateBean.getLawyer().getLawyerId());
								UserLoginBean userLoginBeanObj = loginService.getUserLogin(caseUpdateBean.getCharteredAccountant().getCharteredAccountantId());
								ApplicationUserBean applicationCABean = userLoginBeanObj.getApplicationUserBean();
								if(Objects.isNull(applicationCABean)) {
									applicationCABean = applicationUserService.getApplicationUserById(userLoginBeanObj.getApplicationUserId());
								}
									
								UserLoginBean caLoginBeanObj = loginService.getUserLoginApplicationUserId(applicationCABean.getId());
								smsText = MessageFormat.format(smsText,applicationCABean.getFirstName(), caseModel.getReferenceNumber());
								sendSMSPostApplicationStatusChange(caLoginBeanObj.getMobileNo(),smsText);
							}
								caseModel.setCharteredAccountantId(caseUpdateBean.getCharteredAccountant().getCharteredAccountantId());
						}
		
		if(ArgumentHelper.isTrueFAlse(caseUpdateBean.getIsAdditionalFeesDetailsModified()) && Objects.nonNull(caseUpdateBean.getAdditionalFeesDetails())) {
//			CaseFeeModel agreementFeeModel = caseFeeDao.getByCaseIdAndFeeFor(caseId, "Agreement Fees");
			List<CaseFeeModel> caseFeeModelsExisting = caseFeeDao.getByCaseId(caseId);
//			caseFeeDao.delete(caseId);
			List<CaseFeeModel>  caseFeeModels = CaseFeeConverter.convertL(caseUpdateBean.getAdditionalFeesDetails().getFees());
			
			List<CaseFeeModel>  caseFeeModelsPaymentDetailsToAdd = new ArrayList<>(); 
			if(ArgumentHelper.isEmpty(caseFeeModelsPaymentDetails)) {
				feeModelsPrevious.forEach(fee->{
					if(fee.getFeeFor().contains("Processing Fees") || fee.getFeeFor().contains("Advance")) {
						caseFeeModels.add(fee);
					}
				});
			}else {
				caseFeeModelsPaymentDetails.forEach(fee->{
					if(!fee.getFeeFor().contains("Agreement")) {
						caseFeeModelsPaymentDetailsToAdd.add(fee);
					}
				});
			}
			caseFeeModels.addAll(caseFeeModelsPaymentDetailsToAdd);
			List<CaseFeeModel>  caseFeeModelsToUpdate = new ArrayList<CaseFeeModel>();
			List<CaseFeeModel>  caseFeeModelsToAdd = new ArrayList<CaseFeeModel>();
			caseFeeModels.forEach(cf->{
				if(Objects.isNull(cf.getCaseId())) {
					cf.setCaseId(caseId);
				}
				Optional<CaseFeeModel> agreementFeeOpt = caseFeeModelsExisting.stream().filter(fee->fee.getFeeFor().contains(cf.getFeeFor()) ).findFirst();
				if(agreementFeeOpt.isPresent()) {
					if(cf.getFeeFor().contains("Agreement Fees")) {
						cf.setFeeValue(agreementFeeOpt.get().getFeeValue());
					}
					cf.setId(agreementFeeOpt.get().getId());
					caseFeeModelsToUpdate.add(cf);
				}else {
					caseFeeModelsToAdd.add(cf);
				}
			});
			
			caseFeeDao.addAll(caseFeeModelsToAdd);
			caseFeeDao.updateAll(caseFeeModelsToUpdate);
		}
		
		//Generate Letter
		//NA as this will stored with Other API
		
		//Account Details
		if(ArgumentHelper.isTrueFAlse(caseUpdateBean.getIsAccountDetailModified()) && Objects.nonNull(caseUpdateBean.getAccountDetail())) {
			caseAccountDetailRepo.deleteByCaseId(caseId);
			CaseAccountDetailBean accountDetailBean = caseUpdateBean.getAccountDetail();
			caseAccountDetailRepo.add(CaseAccountDetailModel.builder().caseId(caseId).dematAccountNumber(accountDetailBean.getDematAccountNumber())
					.iepfUserName(accountDetailBean.getIEPFUserName()).iepfPassword(accountDetailBean.getIEPFPassword())
					.rtaContact(accountDetailBean.getRtaContact()).rtaRefNo(accountDetailBean.getRtaRefNo()).build());
		}
		
		List<CustomerDocumentsRequiredModel> documentsRequiredModels = caseCustomerDocumentsRequiredRepo.getByCaseId(caseId);
		List<Long> oldDocIds = new ArrayList<Long>();
		List<CaseDocumentModel> caseDocs = caseDocumentDao.getByCaseId(caseId);
		oldDocIds.addAll(caseDocs.stream().map(ow->ow.getDocumentId()).distinct().collect(Collectors.toList()));
		
		List<Long> oldIdsForReqDoc = new ArrayList<Long>();
		oldIdsForReqDoc.addAll(documentsRequiredModels.stream().map(ow->ow.getId()).distinct().collect(Collectors.toList()));
		
		CaseCustomerDocumentsRequiredBean customerDocuments = caseUpdateBean.getCustomerDocuments();
		if(ArgumentHelper.isTrueFAlse(caseUpdateBean.getIsCustomerDocumentsModified()) && Objects.nonNull(customerDocuments)) {
			List<CustomerDocumentsRequiredBean> customerDocumentList = customerDocuments.getCustomerDocuments();
			if(ArgumentHelper.isNotEmpty(customerDocumentList)) {
				List<CustomerDocumentsRequiredModel> documentsRequiredModelsToFromFE = CustomerDocumentsRequiredConverter.convertL(customerDocumentList);
				List<CustomerDocumentsRequiredModel> documentsRequiredModelsToAdd = new ArrayList<>();
				List<CustomerDocumentsRequiredModel> documentsRequiredModelsToUpdate = new ArrayList<>();
				List<Long> documentsRequiredModelsToDelete = new ArrayList<>();
				documentsRequiredModelsToFromFE.forEach(feModel->{
					if(oldIdsForReqDoc.contains(feModel.getId()) && feModel.getId()>0){
						documentsRequiredModelsToUpdate.add(feModel);
					}else if(Objects.isNull(feModel.getId()) || feModel.getId()==0){
						documentsRequiredModelsToAdd.add(feModel);
					}
				});
				List<Long> feIdsForReqDoc = new ArrayList<Long>();
				feIdsForReqDoc.addAll(documentsRequiredModelsToFromFE.stream().map(ow->ow.getId()).distinct().collect(Collectors.toList()));
				
				oldIdsForReqDoc.forEach(oldId->{
					if(!feIdsForReqDoc.contains(oldId) && oldId>0) {
						documentsRequiredModelsToDelete.add(oldId);
					}
				});
				if(ArgumentHelper.isNotEmpty(documentsRequiredModelsToUpdate)) {
					caseCustomerDocumentsRequiredRepo.updateAll(caseId,documentsRequiredModelsToUpdate);
				}
				if(ArgumentHelper.isNotEmpty(documentsRequiredModelsToAdd)) {
					caseCustomerDocumentsRequiredRepo.addAll(caseId,documentsRequiredModelsToAdd);
				}
				if(ArgumentHelper.isNotEmpty(documentsRequiredModelsToDelete)) {
					caseCustomerDocumentsRequiredRepo.deleteAll(documentsRequiredModelsToDelete);
				}
				
			}
		}
		
		CaseGeneratedDocumentsRequiredBean generatedRequireDocuments = caseUpdateBean.getGeneratedDocuments();    
		if(ArgumentHelper.isTrueFAlse(caseUpdateBean.getIsGeneratedDocumentsModified()) && Objects.nonNull(generatedRequireDocuments)) {
			List<GeneratedDocumentsRequiredModel> generatedDocumentsRequiredModels = caseGeneratedDocumentsRequiredRepo.getByCaseId(caseId);
			List<Long> oldIdsForGenDoc = new ArrayList<Long>();
			oldIdsForGenDoc.addAll(generatedDocumentsRequiredModels.stream().map(ow->ow.getId()).distinct().collect(Collectors.toList()));
			List<GeneratedDocumentsRequiredBean> generatedDocumentsList = generatedRequireDocuments.getGeneratedDocuments();
			if(ArgumentHelper.isNotEmpty(generatedDocumentsList)) {
				if(ArgumentHelper.isNotEmpty(generatedDocumentsList)) {
					List<GeneratedDocumentsRequiredModel>  generatedRequireModelsToFromFE = GeneratedDocumentsRequiredConverter.convertL(generatedDocumentsList);
					List<GeneratedDocumentsRequiredModel> generatedRequireModelsToAdd = new ArrayList<GeneratedDocumentsRequiredModel>();
					List<GeneratedDocumentsRequiredModel> generatedRequireModelsToUpdate = new ArrayList<GeneratedDocumentsRequiredModel>();
					List<Long> generatedRequireModelsToDelete = new ArrayList<>();
					generatedRequireModelsToFromFE.forEach(feModel->{
						if(oldIdsForGenDoc.contains(feModel.getId()) && feModel.getId()>0){
							generatedRequireModelsToUpdate.add(feModel);
						}else {
							generatedRequireModelsToAdd.add(feModel);
						}
					});
					List<Long> feIdsForReqDoc = new ArrayList<Long>();
					feIdsForReqDoc.addAll(generatedRequireModelsToFromFE.stream().map(ow->ow.getId()).distinct().collect(Collectors.toList()));
					
					oldIdsForGenDoc.forEach(oldId->{
						if(!feIdsForReqDoc.contains(oldId) && oldId>0) {
							generatedRequireModelsToDelete.add(oldId);
						}
					});
					
					if(ArgumentHelper.isNotEmpty(generatedRequireModelsToUpdate)) {
						caseGeneratedDocumentsRequiredRepo.updateAll(caseId,generatedRequireModelsToUpdate);
					}
					if(ArgumentHelper.isNotEmpty(generatedRequireModelsToAdd)) {
						caseGeneratedDocumentsRequiredRepo.addAll(caseId,generatedRequireModelsToAdd);
					}
					
					if(ArgumentHelper.isNotEmpty(generatedRequireModelsToDelete)) {
						caseGeneratedDocumentsRequiredRepo.deleteAll(generatedRequireModelsToDelete);
					}
				}
			}
		}
		
		//Witness Info
		CaseWintessBean wintess =caseUpdateBean.getWintess();
		if(ArgumentHelper.isTrueFAlse(caseUpdateBean.getIsWintessModified()) && Objects.nonNull(wintess)) {
			List<WitnessBean> witnesses = wintess.getWitnesses();
			List<WitnessModel> oldWitnesses = caseWitnessDao.getByCaseId(caseId);
			List<Long> newDocIds = new ArrayList<Long>();
			newDocIds.addAll(witnesses.stream().map(ow->ow.getAdharDocumentId()).collect(Collectors.toList()));
			newDocIds.addAll(witnesses.stream().map(ow->ow.getPanDocumentId()).collect(Collectors.toList()));
			
			caseWitnessDao.deleteCaseById(caseId);
			if(ArgumentHelper.isNotEmpty(oldWitnesses)) {
				oldWitnesses.forEach(w->{
					Long oldDocId = w.getAdharDocumentId();
					if(!newDocIds.contains(oldDocId)) {
						caseCustomerDocumentsRequiredRepo.delete(caseId, oldDocId);
						caseDocumentDao.deleteByDocumentId(oldDocId);
						emailReposatory.deleteByDocId(oldDocId);
						documentDao.deleteById(oldDocId);
					}
					oldDocId = w.getPanDocumentId();
					if(!newDocIds.contains(oldDocId)) {
						caseCustomerDocumentsRequiredRepo.delete(caseId, oldDocId);
						caseDocumentDao.deleteByDocumentId(oldDocId);
						emailReposatory.deleteByDocId(oldDocId);
						documentDao.deleteById(oldDocId);
					}
				});
			}
			List<WitnessModel> models = WitnessConverter.convertL(witnesses);
			String aadharPlaceHolder = "Witness %d Adhaar Card";
			String panPlaceHolder = "Witness %d Pan Card";
			AtomicInteger counter = new AtomicInteger(1);
			List<CustomerDocumentsRequiredModel> modelsToAdd = new ArrayList<CustomerDocumentsRequiredModel>();
			List<DocumentMasterModel> documentMasterModels = utilityRepo.getMasterDocumentParticularsStartWith("Witness");
			List<CaseDocumentModel> caseDocumentModels = new ArrayList<CaseDocumentModel>();
			models.forEach(w->{
				w.setCreateBy(userLoginBean.getId());
				w.setCreatedDate(LocalDate.now());
				if(Objects.isNull(w.getCaseId()) || w.getCaseId()<1) {
					w.setCaseId(caseId);
				}
				if(!oldDocIds.contains(w.getAdharDocumentId())) {
					String aadharDocType = String.format(aadharPlaceHolder, counter.get());
					log.info("Witness :: aadharDocType===>"+aadharDocType);
					Optional<DocumentMasterModel> documentMasterModelOpt =  documentMasterModels.stream().filter(d->d.getParticulars().equalsIgnoreCase(aadharDocType)).findFirst();
					documentMasterModelOpt.ifPresent(documentMasterModel->{
						log.info("Witness :: Added :: aadharDocType===>");
						modelsToAdd.add(CustomerDocumentsRequiredModel.builder().caseId(caseId)
								.documentMasterId(documentMasterModel.getId())	.documentId(w.getAdharDocumentId()).build());
						CaseDocumentModel model = CaseDocumentModel.builder().build();
						model.setCaseId(caseId);
						model.setDocumentId(w.getAdharDocumentId());
						model.setType(documentMasterModel.getType());
						model.setUploadType("");
						model.setCreatedDate(LocalDateTime.now());
						model.setCreateBy(userLoginBean.getId());
						caseDocumentModels.add(model);
					});
				}
				if(!oldDocIds.contains(w.getPanDocumentId())) {
					String panDocType = String.format(panPlaceHolder, counter.get());
					log.info("Witness :: panDocType===>"+panDocType);
					Optional<DocumentMasterModel> documentMasterModelOpt1 =  documentMasterModels.stream().filter(d->d.getParticulars().equalsIgnoreCase(panDocType)).findFirst();
					documentMasterModelOpt1.ifPresent(documentMasterModel->{
						log.info("Witness :: Added :: panDocType===>");
						modelsToAdd.add(CustomerDocumentsRequiredModel.builder().caseId(caseId)
								.documentMasterId(documentMasterModel.getId())	.documentId(w.getPanDocumentId()).build());
						CaseDocumentModel model = CaseDocumentModel.builder().build();
						model.setCaseId(caseId);
						model.setDocumentId(w.getPanDocumentId());
						model.setType(documentMasterModel.getType());
						model.setUploadType("");
						model.setCreatedDate(LocalDateTime.now());
						model.setCreateBy(userLoginBean.getId());
						caseDocumentModels.add(model);
					});
				}
				counter.getAndIncrement();
			});
			caseWitnessDao.addAll(models);
			if(ArgumentHelper.isNotEmpty(modelsToAdd)) {
				caseCustomerDocumentsRequiredRepo.addAll(caseId, modelsToAdd);
				caseDocumentDao.addAll(caseDocumentModels);
			}
		}
		
		//Surety Info
		CaseSuretyInfoBean suretyInfoBean = caseUpdateBean.getSuretyInfo();
		if(ArgumentHelper.isTrueFAlse(caseUpdateBean.getIsSuretyInfoModified()) && Objects.nonNull(suretyInfoBean)) {
			List<SuretyInfoBean> suretyInfoBeans = suretyInfoBean.getSuretyInfo();
			caseSuretyInfoDao.deleteCaseById(caseId);
			List<SuretyInfoModel> oldsuretyInfos = caseSuretyInfoDao.getByCaseId(caseId);
			
			List<Long> newDocIds = new ArrayList<Long>();
			newDocIds.addAll(suretyInfoBeans.stream().map(ow->ow.getAdharDocumentId()).collect(Collectors.toList()));
			newDocIds.addAll(suretyInfoBeans.stream().map(ow->ow.getPanDocumentId()).collect(Collectors.toList()));
			newDocIds.addAll(suretyInfoBeans.stream().map(ow->ow.getItrDocumentId()).collect(Collectors.toList()));
			
			if(ArgumentHelper.isNotEmpty(oldsuretyInfos)) {
				oldsuretyInfos.forEach(s->{
					Long oldDocId = s.getAdharDocumentId();
					if(!newDocIds.contains(oldDocId)) {
						caseCustomerDocumentsRequiredRepo.delete(caseId, oldDocId);
						caseDocumentDao.deleteByDocumentId(oldDocId);
						emailReposatory.deleteByDocId(oldDocId);
						documentDao.deleteById(oldDocId);
					}
					oldDocId = s.getPanDocumentId();
					if(!newDocIds.contains(oldDocId)) {
						caseCustomerDocumentsRequiredRepo.delete(caseId, oldDocId);
						caseDocumentDao.deleteByDocumentId(oldDocId);
						emailReposatory.deleteByDocId(oldDocId);
						documentDao.deleteById(oldDocId);
					}
					oldDocId = s.getItrDocumentId();
					if(!newDocIds.contains(oldDocId)) {
						caseCustomerDocumentsRequiredRepo.delete(caseId, oldDocId);
						caseDocumentDao.deleteByDocumentId(oldDocId);
						emailReposatory.deleteByDocId(oldDocId);
						documentDao.deleteById(oldDocId);
					}
				});
			}
			String aadharPlaceHolder = "Surety %d Adhaar Card";
			String panPlaceHolder = "Surety %d Pan Card";
			String itrPlaceHolder = "Surety %d ITR Copy / Networth Certificate";
			AtomicInteger counter = new AtomicInteger(1);
			List<SuretyInfoModel> models = SuretyInfoConverter.convertL(suretyInfoBeans);
			List<DocumentMasterModel> documentMasterModels = utilityRepo.getMasterDocumentParticularsStartWith("Surety");
			List<CustomerDocumentsRequiredModel> modelsToAdd = new ArrayList<CustomerDocumentsRequiredModel>();
			List<CaseDocumentModel> caseDocumentModels = new ArrayList<CaseDocumentModel>();
			models.forEach(w->{
				w.setCreateBy(userLoginBean.getId());
				w.setCreatedDate(LocalDate.now());
				if(Objects.isNull(w.getCaseId())  || w.getCaseId()<1) {
					w.setCaseId(caseId);
				}
				if(!oldDocIds.contains(w.getAdharDocumentId())) {
					String aadharDocType = String.format(aadharPlaceHolder, counter.get());
					log.info("Surety :: aadharDocType===>"+aadharDocType);
					Optional<DocumentMasterModel> documentMasterModelOpt =  documentMasterModels.stream().filter(d->d.getParticulars().equalsIgnoreCase(aadharDocType)).findFirst();
					documentMasterModelOpt.ifPresent(documentMasterModel->{
						log.info("Surety :: Added :: aadharDocType===>");
						modelsToAdd.add(CustomerDocumentsRequiredModel.builder().caseId(caseId)
								.documentMasterId(documentMasterModel.getId())	.documentId(w.getAdharDocumentId()).build());
						CaseDocumentModel model = CaseDocumentModel.builder().build();
						model.setCaseId(caseId);
						model.setDocumentId(w.getAdharDocumentId());
						model.setType(documentMasterModel.getType());
						model.setUploadType("");
						model.setCreatedDate(LocalDateTime.now());
						model.setCreateBy(userLoginBean.getId());
						caseDocumentModels.add(model);
					});
				}
				if(!oldDocIds.contains(w.getPanDocumentId())) {
					String panDocType = String.format(panPlaceHolder, counter.get());
					log.info("Surety :: panDocType===>"+panDocType);
					Optional<DocumentMasterModel> documentMasterModelOpt =  documentMasterModels.stream().filter(d->d.getParticulars().equalsIgnoreCase(panDocType)).findFirst();
					documentMasterModelOpt.ifPresent(documentMasterModel->{
						log.info("Surety :: Added :: panDocType===>");
						modelsToAdd.add(CustomerDocumentsRequiredModel.builder().caseId(caseId)
								.documentMasterId(documentMasterModel.getId())	.documentId(w.getPanDocumentId()).build());
						CaseDocumentModel model = CaseDocumentModel.builder().build();
						model.setCaseId(caseId);
						model.setDocumentId(w.getPanDocumentId());
						model.setType(documentMasterModel.getType());
						model.setUploadType("");
						model.setCreatedDate(LocalDateTime.now());
						model.setCreateBy(userLoginBean.getId());
						caseDocumentModels.add(model);
					});
				}
				if(!oldDocIds.contains(w.getPanDocumentId())) {
					String itrDocType = String.format(itrPlaceHolder, counter.get());
					log.info("Surety :: itrDocType===>"+itrDocType);
					Optional<DocumentMasterModel> documentMasterModelOpt =  documentMasterModels.stream().filter(d->d.getParticulars().equalsIgnoreCase(itrDocType)).findFirst();
					documentMasterModelOpt.ifPresent(documentMasterModel->{
						log.info("Surety :: Added :: itrDocType===>");
						modelsToAdd.add(CustomerDocumentsRequiredModel.builder().caseId(caseId)
								.documentMasterId(documentMasterModel.getId())	.documentId(w.getItrDocumentId()).build());
						CaseDocumentModel model = CaseDocumentModel.builder().build();
						model.setCaseId(caseId);
						model.setDocumentId(w.getItrDocumentId());
						model.setType(documentMasterModel.getType());
						model.setUploadType("");
						model.setCreatedDate(LocalDateTime.now());
						model.setCreateBy(userLoginBean.getId());
						caseDocumentModels.add(model);
					});
				}
				counter.getAndIncrement();
			});
			caseSuretyInfoDao.addAll(models);
			if(ArgumentHelper.isNotEmpty(modelsToAdd)) {
				caseCustomerDocumentsRequiredRepo.addAll(caseId, modelsToAdd);
				caseDocumentDao.addAll(caseDocumentModels);
			}
		}
		
		
		//Script Folio
		//TODO add Validation
		updateScriptFolio(caseUpdateBean, caseModel);
		
		if(ArgumentHelper.isTrueFAlse(caseUpdateBean.getIsAddDetailsModified()) 
				&& Objects.nonNull(caseUpdateBean.getAddDetails())) {
			CustomerCasePendingFolioDetails addDetails = caseUpdateBean.getAddDetails();
			addDetails.setCaseId(caseId);
			saveCustomerCasePendingFolioDetails(addDetails, userLoginBean);
		}
		updateCaseParent(currentStatus, caseUpdateBean, caseModel,userLoginBean);
		
		if (!currentStatus.equals(caseModel.getStatus())) {
			caseLogRepo.add(CaseLogModel.builder().caseId(caseId).action("Status Updated To :" + caseModel.getStatus())
					.createBy(userLoginBean.getDisplayName()).build());
		}
		
	}

	private boolean updateScriptFolio(CaseUpdateBean caseUpdateBean, CaseModel caseModel) {
		boolean isModified = false;
		Long caseId = caseModel.getId();
		if(ArgumentHelper.isTrueFAlse(caseUpdateBean.getIsFolioScriptModified()) && Objects.nonNull(caseUpdateBean.getScript())) {
			//update
			CaseScriptUpdateBean script = caseUpdateBean.getScript();
			if(ArgumentHelper.isNotEmpty(script.getAddedScripts())) {
				//Add New Script and Questioner in the Case.
				List<AddExistingCaseScriptBean> addedScripts = script.getAddedScripts();
				addedScripts.forEach(s->{
					Long caseScriptId =caseScriptRepo.add(CaseScriptModel.builder().caseId(caseId).scriptId(s.getScriptId())
							.primaryCaseHolder(s.getPrimaryCaseHolder())
							.secondayCaseHolder(s.getSecondayCaseHolder())
							.isPrimaryCaseHolderDeceased(s.getIsPrimaryCaseHolderDeceased())
							.isSecondayCaseHolderDeceased(s.getIsSecondayCaseHolderDeceased())
							.primaryHolderGender(s.getPrimaryHolderGender())
							.secondayHolderGender(s.getSecondayHolderGender())
							.primaryHolderAge(s.getPrimaryHolderAge())
							.secondayHolderAge(s.getSecondayHolderAge())
							.primaryHolderFatherHusbandName(s.getPrimaryHolderFatherHusbandName())
							.secondayHolderFatherHusbandName(s.getSecondayHolderFatherHusbandName())
							.build());
					if(ArgumentHelper.isNotEmpty(s.getQuestioners())) {
						List<CaseQuestionerModel> models = new ArrayList<>(); 
						models.add(CaseQuestionerModel.builder().caseScriptId(caseScriptId).questioners(QuestionerConverter.convertL(s.getQuestioners())).build());
						caseQuestionerRepo.addAll(models);
					}
				});
				//if(AppConstant.CaseStatus.WaitingSubmission.label.equalsIgnoreCase(caseModel.getStatus())) {
					caseModel.setStatus(AppConstant.CaseStatus.WaitingCustomerAadhar.label);
					caseModel.setStatusAutoUpdate(Boolean.TRUE);
					caseModel.setIseAdharComplete(Boolean.FALSE);
				//}
				isModified=true;
			}
			
			if(ArgumentHelper.isNotEmpty(script.getNewlyAddedScripts())) {
				List<AddNewCaseScriptBean> newlyAddedScripts = script.getNewlyAddedScripts();
				List<ScriptModel> scriptModels = scriptRepo.getScriptByCompanyCode(newlyAddedScripts.get(0).getCompanyName());
				newlyAddedScripts.forEach(newScript->{
					Double marketPrice = newScript.getMarketPrice();
					Double nominalValue = newScript.getNominalValue();
					if(ArgumentHelper.isNotEmpty(scriptModels)) {
						marketPrice = scriptModels.get(0).getMarketPrice()/scriptModels.get(0).getNumberOfShare();
						nominalValue = scriptModels.get(0).getNominalValue();
					}
//					Double marketPrice = scriptModelTemp.getMarketPrice()/scriptModelTemp.getNumberOfShare();
//					addNewFolioToApplicationBean.getScript().setMarketPrice(marketPrice * addNewFolioToApplicationBean.getScript().getNumberOfShare());
					ScriptModel scriptModel = ScriptModel.builder().build();
					BeanUtils.copyProperties(newScript, scriptModel);
					scriptModel.setMarketPrice(marketPrice*scriptModel.getNumberOfShare());
					scriptModel.setNominalValue(nominalValue);
					Long scriptId = scriptRepo.add(scriptModel);
					Long caseScriptId =caseScriptRepo.add(CaseScriptModel.builder().caseId(caseId).scriptId(scriptId)
							.primaryCaseHolder(newScript.getPrimaryCaseHolder())
							.secondayCaseHolder(newScript.getSecondayCaseHolder())
							.isPrimaryCaseHolderDeceased(newScript.getIsPrimaryCaseHolderDeceased())
							.isSecondayCaseHolderDeceased(newScript.getIsSecondayCaseHolderDeceased())
							.primaryHolderGender(newScript.getPrimaryHolderGender())
							.secondayHolderGender(newScript.getSecondayHolderGender())
							.primaryHolderAge(newScript.getPrimaryHolderAge())
							.secondayHolderAge(newScript.getSecondayHolderAge())
							.primaryHolderFatherHusbandName(newScript.getPrimaryHolderFatherHusbandName())
							.secondayHolderFatherHusbandName(newScript.getSecondayHolderFatherHusbandName())
							.build());
					if(ArgumentHelper.isNotEmpty(newScript.getQuestioners())) {
						List<CaseQuestionerModel> models = new ArrayList<>(); 
						models.add(CaseQuestionerModel.builder().caseScriptId(caseScriptId).questioners(QuestionerConverter.convertL(newScript.getQuestioners())).build());
						caseQuestionerRepo.addAll(models);
					}
				});
				//if(AppConstant.CaseStatus.WaitingSubmission.label.equalsIgnoreCase(caseModel.getStatus())) {
					caseModel.setStatus(AppConstant.CaseStatus.WaitingCustomerAadhar.label);
					caseModel.setStatusAutoUpdate(Boolean.TRUE);
					caseModel.setIseAdharComplete(Boolean.FALSE);
				//}
				isModified=true;
			}
			if(ArgumentHelper.isNotEmpty(script.getRemovedCaseScriptIds())) {
				script.getRemovedCaseScriptIds().forEach(delCaseScriptId->{
					caseQuestionerRepo.deleteCaseScriptQues(delCaseScriptId);
					caseShareCertificateDetailsRepo.delete(delCaseScriptId);
					caseWarrantDetailsRepo.delete(delCaseScriptId);
					caseScriptRepo.deleteByIdAndCaseId(delCaseScriptId, caseId);
				});
				//if(AppConstant.CaseStatus.WaitingSubmission.label.equalsIgnoreCase(caseModel.getStatus())) {
					caseModel.setStatus(AppConstant.CaseStatus.WaitingCustomerAadhar.label);
					caseModel.setStatusAutoUpdate(Boolean.TRUE);
					caseModel.setIseAdharComplete(Boolean.FALSE);
				//}
				isModified=true;
			}
		}
		
		return isModified;
	}

	private void updateCaseParent(String currentStatus, CaseUpdateBean caseUpdateBean, CaseModel caseModel, UserLoginBean userLoginBean) {
		if(!caseModel.isStatusAutoUpdate() && ArgumentHelper.isValid(caseUpdateBean.getStatus())) {
			caseModel.setStatus(caseUpdateBean.getStatus());	
		}else {
			if (ArgumentHelper.isTrueFAlse(caseUpdateBean.getIsUploadRTAResponseVerified())
					&& !caseModel.getIsUploadRTAResponseVerified()
							.equals(ArgumentHelper.isTrueFAlse(caseUpdateBean.getIsUploadRTAResponseVerified()))) {
				caseModel.setIsUploadRTAResponseVerified(caseUpdateBean.getIsUploadRTAResponseVerified());
				caseModel.setStatus(AppConstant.CaseStatus.WaitingPostResponseUpdation.label);
			}
			if (ArgumentHelper.isTrueFAlse(caseUpdateBean.getIsFeeProcessed()) && !caseModel.getIsFeeProcessed()
					.equals(ArgumentHelper.isTrueFAlse(caseUpdateBean.getIsFeeProcessed()))) {
				caseModel.setIsFeeProcessed(caseUpdateBean.getIsFeeProcessed());
				if (caseModel.isStatusAutoUpdate()) {
					caseModel.setStatus(AppConstant.CaseStatus.WaitingCustomerAadhar.label);
				}
			}
			if (ArgumentHelper.isTrueFAlse(caseUpdateBean.getIsSignedDocumentsVerified())
					&& !caseModel.getIsSignedDocumentsVerified()
							.equals(ArgumentHelper.isTrueFAlse(caseUpdateBean.getIsSignedDocumentsVerified()))) {
				caseModel.setIsSignedDocumentsVerified(caseUpdateBean.getIsSignedDocumentsVerified());
				caseModel.setStatus(AppConstant.CaseStatus.WaitingRTAResponse.label);
			}
		}
		if(ArgumentHelper.isValid(caseUpdateBean.getRemarks())) {
			caseModel.setRemarks(caseUpdateBean.getRemarks());	
		}
		
		if(ArgumentHelper.isTrueFAlse(caseUpdateBean.getIsWintessInfoReceived())) {
			caseModel.setIsWintessInfoReceived(caseUpdateBean.getIsWintessInfoReceived());	
		}
		if(ArgumentHelper.isTrueFAlse(caseUpdateBean.getIsSuretyInfoReceived())) {
			caseModel.setIsSuretyInfoReceived(caseUpdateBean.getIsSuretyInfoReceived());	
		}
		if(ArgumentHelper.isTrueFAlse(caseUpdateBean.getIsCustomerDocumentsRequiredVerified())) {
			caseModel.setIsCustomerDocumentsRequiredVerified(caseUpdateBean.getIsCustomerDocumentsRequiredVerified());	
		}
		if(ArgumentHelper.isTrueFAlse(caseUpdateBean.getIsGeneratedDocumentsRequiredVerified())) {
			caseModel.setIsGeneratedDocumentsRequiredVerified(caseUpdateBean.getIsGeneratedDocumentsRequiredVerified());	
		}
		if (!currentStatus.equals(caseModel.getStatus())) {
			if(!Objects.isNull(caseUpdateBean.getStatus())) {
				if(AppConstant.CaseStatus.WaitingProcessingFee.label.equals(caseModel.getStatus())) {
					caseModel.setIsFeeProcessed(Boolean.FALSE);
//					caseModel.setIsSignedDocumentsVerified(Boolean.FALSE);
//					caseModel.setIsUploadRTAResponseVerified(Boolean.FALSE);
				}else if(AppConstant.CaseStatus.WaitingProcessingFeeConfirmation.label.equals(caseModel.getStatus())) {
					caseModel.setIsFeeProcessed(Boolean.FALSE);
//					caseModel.setIsSignedDocumentsVerified(Boolean.FALSE);
//					caseModel.setIsUploadRTAResponseVerified(Boolean.FALSE);
				}else if(AppConstant.CaseStatus.WaitingSignedDocumentsUpload.label.equals(caseModel.getStatus())) {
					caseModel.setIsSignedDocumentsVerified(Boolean.FALSE);
				}else if(AppConstant.CaseStatus.WaitingRTALetter1Generation.label.equals(caseModel.getStatus())) {
					caseModel.setIsUploadRTAResponseVerified(Boolean.FALSE);
//					caseModel.setIsSignedDocumentsVerified(Boolean.FALSE);
				}else if(AppConstant.CaseStatus.WaitingRTALetter2Generation.label.equals(caseModel.getStatus())) {
					caseModel.setIsUploadRTAResponseVerified(Boolean.FALSE);
				}else if(AppConstant.CaseStatus.WaitingRequiredDocumentList.label.equals(caseModel.getStatus())) {
					caseModel.setIsCustomerDocumentsRequiredVerified(Boolean.FALSE);
				}else if(AppConstant.CaseStatus.WaitingWitnessInfo.label.equals(caseModel.getStatus())) {
					caseModel.setIsWintessInfoReceived(Boolean.FALSE);
				}
			}
		}
		//Waiting Processing Fee, Waiting Processing Fee Confirmation, Waiting Franchise Assigment, Waiting RTA Letter 1 Generation
		//Waiting Signed Documents Upload, Waiting RTA Response, 
		List<StatusMasterModel> statusMasterModels =utilityRepo.getMasterStatusList(userLoginBean);
		List<String> stage1Statues = statusMasterModels.stream()
				.filter(sm -> sm.getStage().trim().equals(CaseAccessControlHelper.STAGE_1)).map(sm -> sm.getStatus().trim())
				.distinct().collect(Collectors.toList());
		if(stage1Statues.contains(caseUpdateBean.getStatus())) {
			caseModel.setIsUploadRTAResponseVerified(Boolean.FALSE);
		}
		setDefaultNullValue(caseModel);
		caseRepo.update(caseModel);
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void generateCustomerAgreement(Long caseId, UserLoginBean userLoginBean) {
		if (AppConstant.USER_TYPE_END_USER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
			CaseModel caseModel = caseRepo.getById(caseId, userLoginBean);
				if(caseModel.getIsFeeProcessed()) {
				caseModel.setMaskFolio(Boolean.FALSE);
				caseTemplateService.processTemplate(
						CaseDocumentBean.builder().caseId(caseModel.getId())
								.templateType(AppConstant.DocumentType.CustomerAgreement.name()).build(),
						userLoginBean, caseModel);
			}
		}
	}
	public List<CaseBean> getCasesByStatuses(String stage, String status, UserLoginBean userLoginBean) {
		List<String> statuses = new ArrayList<>();
		statuses.add(status);
//		if (status.equalsIgnoreCase("My Cases")) {
//			if (AppConstant.USER_TYPE_END_USER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//				statuses.addAll(AppConstant.endUserMyCaseStatuses);
//			}
//		} else if (DashBoardService.displayApplicationStatusMapping.containsValue(status)) {
//			// status
//			status = ArrayUtil.getKey(DashBoardService.displayCaseStatusMapping, status);
//			statuses.add(status);
//		}
		List<CaseBean> cases = CaseConverter.convert(caseRepo.getCasesByStatuses(stage, statuses, userLoginBean));
//		cases.forEach(application->{
//			if(DashBoardService.displayCaseStatusMapping.containsKey(application.getStatus())) {
//				application.setStatus(DashBoardService.displayCaseStatusMapping.get(application.getStatus()));
//			}
//		});
		return cases;
	}
	
	public List<CaseBean> getByReferenceNumber(String referenceNumber, UserLoginBean userLoginBean) {
		List<CaseBean> cases = CaseConverter.convert(caseRepo.getByReferenceNumber(referenceNumber, userLoginBean));
//		cases.forEach(application->{
//			if(DashBoardService.displayCaseStatusMapping.containsKey(application.getStatus())) {
//				application.setStatus(DashBoardService.displayCaseStatusMapping.get(application.getStatus()));
//			}
//		});
		return cases;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public CaseBean createCaseFromApplication(ProcessApplicationBean processApplicationBean,
			UserLoginBean userLoginBean) {
		ApplicationModel applicationModel = getByApplicationId(processApplicationBean.getApplicationId(),
				userLoginBean);
		// TODO VAlidate Application like status and other things
		CaseModel caseModel = createCaseModel(applicationModel, userLoginBean);
		Long caseId = caseRepo.add(caseModel);
		caseModel.setId(caseId);
		saveCaseFeeFromApplication(applicationModel, caseId);
		
		saveCaseCommission(userLoginBean, caseModel);
		
		caseModel.getScripts().forEach(s -> s.setCaseId(caseId));
		caseScriptRepo.addAll(caseModel.getScripts());
		
		List<CaseScriptModel> caseScriptModels = caseScriptRepo.getByCaseId(caseId);
		ApplicationQuestionerModel applicationQuestionerModel = applicationRepo.getApplicationQuestionerAnsByApplicationId(applicationModel.getId());
		List<CaseQuestionerModel> models = new ArrayList<>();
		Set<Long> uniqueSet1 = new HashSet<>();
		
		Map<Integer, List<QuestionerModel>> scriptQuestionMap = new HashMap<>();
//		List<QuestionerModel> questioners = new ArrayList<>();
		AtomicInteger counter = new AtomicInteger(0);
		applicationQuestionerModel.getQuestioners().forEach(q->{
			Integer tempCounter = 0;
			if(uniqueSet1.add(q.getParentScriptId())) {
				List<QuestionerModel> questioners = new ArrayList<>();
				tempCounter = counter.incrementAndGet();
				scriptQuestionMap.put(tempCounter, questioners);
			}
			if(counter.get()>0) {
				scriptQuestionMap.get(counter.get()).add(QuestionerModel.builder().id(q.getId()).answer(q.getAnswer()).build());
			}
		});
		AtomicInteger counter1 = new AtomicInteger(0);
		caseScriptModels.forEach(caseScript->{
			Integer tempCounter = counter1.incrementAndGet();
			List<QuestionerModel> questioners =scriptQuestionMap.get(tempCounter);
				models.add(CaseQuestionerModel.builder().caseScriptId(caseScript.getId()).questioners(questioners).build());
		});
		caseQuestionerRepo.addAll(models);
		
		applicationModel.setStatus(AppConstant.APPLICATION_STATUS_IN_PROGRESS);
		applicationRepo.update(applicationModel);
		caseModel.setId(caseId);
		
		//String website = "http://www.findmymoney.in";
		String website = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.WEBSITE);
		String smsText= MessageTemplateService.getMessage("CASE_CREATED_SMS"); // CASE_CREATED_SMS
		String caserefNo = getById(caseId, userLoginBean).getReferenceNumber();
		smsText = MessageFormat.format(smsText, userLoginBean.getApplicationUserBean().getFirstName(), caserefNo, website );
		sendSMSPostApplicationStatusChange(userLoginBean.getMobileNo(),smsText);
		
		// Admin
		UserLogin userLoginSysobj = userLoginRepo.getUserLoginApplicationUserId(AppConstant.SYS_USER_ID);
		sendSMSPostApplicationStatusChange(userLoginSysobj.getMobileNo(), smsText);
		return CaseConverter.convert(caseModel);
	}


	private void saveCaseCommission(UserLoginBean userLoginBean, CaseModel caseModel) {
		Long referralUserId = 0L;
		ReferralsCommisionDtlModel referralsCommisionDtlModel = null;
		if(Objects.nonNull(userLoginBean.getApplicationUserBean().getReferalFranchiseId()) 
				&& userLoginBean.getApplicationUserBean().getReferalFranchiseId()>0) {
			referralUserId  = franchiseRepo.getFranchiseOwnerUserIdById(userLoginBean.getApplicationUserBean().getReferalFranchiseId());
			referralsCommisionDtlModel = referralsCommisionDtlRepo.getReferralPartnerCommisionDtlforGivenFranchiseId(userLoginBean.getApplicationUserBean().getReferalFranchiseId());
		}else if(Objects.nonNull(userLoginBean.getApplicationUserBean().getReferalUserId()) 
					&& userLoginBean.getApplicationUserBean().getReferalUserId()>0) {
			referralUserId = userLoginBean.getApplicationUserBean().getReferalUserId();
			referralsCommisionDtlModel = referralsCommisionDtlRepo.getReferralPartnerCommisionDtlforGivenAppUserId(userLoginBean.getApplicationUserBean().getReferalUserId());
		}else {
			referralUserId  = franchiseRepo.getFranchiseOwnerUserIdById(1L);
			referralsCommisionDtlModel = referralsCommisionDtlRepo.getReferralPartnerCommisionDtlforGivenFranchiseId(1L);
		}
		ReferralsCommisionDtlModel referralsCommisionDtlModelFranchise = referralsCommisionDtlRepo.getReferralPartnerCommisionDtlforGivenFranchiseId(caseModel.getFranchiseId());
		referralsCommisionDtlModel.setDocuProcessingfeeCommision(referralsCommisionDtlModelFranchise.getDocuProcessingfeeCommision());
		caseCommissionDao.add(CaseCommissionModel.builder().caseId(caseModel.getId()).referralUserId(referralUserId)
				.referralProcFeeComm(referralsCommisionDtlModel.getProcesingFeeCommison())
				.referralAgreFeeComm(referralsCommisionDtlModel.getAgreementfeeCommision())
				.referralDocumentProcFeeComm(referralsCommisionDtlModel.getDocuProcessingfeeCommision()).build());
	}

	private void saveCaseFeeFromApplication(ApplicationModel applicationModel, Long caseId) {
		List<CaseFeeModel> models = new ArrayList<>();
		List<ApplicationFeeModel> applicationFeeModels = applicationFeeRepo.getByApplicationId(applicationModel.getId()); 
		applicationFeeModels.forEach(fm->{
			models.add(CaseFeeModel.builder().caseId(caseId).feeFor(fm.getFeeFor()).feeType(fm.getFeeType())
					.feeValue(fm.getFeeValue()).isGSTApplicable(fm.getIsGSTApplicable()).build());
		});
		caseFeeDao.addAll(models);
	}

	public FileBean downloadAggrement(Long caseId, UserLoginBean userLoginBean) {
		CaseModel caseModel = caseRepo.getById(caseId, userLoginBean);
		caseModel.setMaskFolio(Boolean.TRUE);
//		if(ArgumentHelper.isPositive(caseModel.getAggrmentDcoumentId())) {
		if(ArgumentHelper.isPositive(caseModel.getAggrmentDcoumentId())
				&& !(AppConstant.CaseStatus.WaitingCustomerAadhar.label.equalsIgnoreCase(caseModel.getStatus()) 
				|| AppConstant.CaseStatus.WaitingSubmission.label.equalsIgnoreCase(caseModel.getStatus()))) {
			DocumentBean documentBean =  documentService.getById(caseModel.getAggrmentDcoumentId());
			return documentBean.getFile();
		}
		caseModel.setMaskFolio(Boolean.TRUE);
		return caseTemplateService.processTemplate(
				CaseDocumentBean.builder().caseId(caseId)
						.templateType(AppConstant.DocumentType.CustomerAgreement.name()).build(),
				userLoginBean, caseModel);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public CaseBean continueAndSaveCase(CaseKYCBean caseKYCBean, UserLoginBean userLoginBean) {
		CaseModel caseModel = caseRepo.getById(caseKYCBean.getId(), userLoginBean);
		if (caseKYCBean.getIsCommAddrSameAsAddharAddr()) {
			caseModel.setCommAddressId(caseModel.getAddressId());
		} else {
			Long addressId = addressRepo.addAddress(AddressConverter.convert(caseKYCBean.getCommAddress()));
			caseModel.setCommAddressId(addressId);
		}
		setDefaultNullValue(caseModel);
		caseModel.setMaskFolio(Boolean.FALSE);
		caseTemplateService.processTemplate(
				CaseDocumentBean.builder().caseId(caseKYCBean.getId())
						.templateType(AppConstant.DocumentType.CustomerAgreement.name()).build(),
				userLoginBean, caseModel);
		caseRepo.update(caseModel);
		return CaseConverter.convert(caseModel);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public CaseBean saveAadharDetails(CaseAadharBean caseAadharBean, UserLoginBean userLoginBean) {
		CaseModel caseModel = caseRepo.getById(caseAadharBean.getId(), userLoginBean);
		caseModel.setFirstName(caseAadharBean.getAadharFirstName());
		caseModel.setMiddleName(caseAadharBean.getAadharMiddleName());
		caseModel.setLastName(caseAadharBean.getAadharLastName());
		caseModel.setDateOfBirth(caseAadharBean.getDateOfBirth());
		caseModel.setGender(caseAadharBean.getGender());
		Long addressId = addressRepo.addAddress(AddressConverter.convert(caseAadharBean.getAddress()));
		caseModel.setAddressId(addressId);
		caseModel.setAadharNumber(caseAadharBean.getAadharNumber());

		setDefaultNullValue(caseModel);
//		caseModel.setStatus(AppConstant.CaseStatus.AdharVerified.label);
		caseRepo.update(caseModel);

		return CaseConverter.convert(caseModel);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public CaseBean saveAadharImage(Long caseId, FileBean aadharImage, UserLoginBean userLoginBean) {
		CaseModel caseModel = caseRepo.getById(caseId, userLoginBean);
		String bucketName = ApplicationPropertiesService
				.getPropertyStringValue(PropertyKeys.AZ_S3_BUCKET_NAME);;
		aadharImage.setFileTitle(caseId + "_" + aadharImage.getFileTitle());
		DocumentModel documentModel = DocumentModel.builder().name(aadharImage.getFileTitle())
				.createdDate(LocalDateTime.now()).createBy(userLoginBean.getId())
				.type(AppConstant.DocumentType.Aadhar.name()).contentType(aadharImage.getFileContentType())
				.bucketName(bucketName).build();
		Long oldDocId = caseModel.getAadharDocId();
		Long aadharDocId = documentDao.add(documentModel);
		caseModel.setAadharDocId(aadharDocId);
		if(Objects.isNull(caseModel.getAadharNumber())) {
			boolean isIntegrationRequired = ApplicationPropertiesService
					.getPropertyBooleanValue(PropertyKeys.SURE_PASS_INTEGRATION_REQUIRED);
			if(!isIntegrationRequired) {
				caseModel.setAadharNumber("XXX");
			}
		}
		documentModel.setId(aadharDocId);
		intsertCaseDocument(caseModel.getId(), documentModel,null);
		setDefaultNullValue(caseModel);
//		caseModel.setStatus(AppConstant.CaseStatus.AdharVerified.label);
		caseRepo.update(caseModel);
		
		
		caseCustomerDocumentsRequiredRepo.delete(caseId, oldDocId);
		caseDocumentDao.deleteByDocumentId(oldDocId);
		emailReposatory.deleteByDocId(oldDocId);
		documentDao.deleteById(oldDocId);
		
		DocumentMasterModel documentMasterModel =  utilityRepo.getMasterDocument("Aadhar");
		caseCustomerDocumentsRequiredRepo.add(CustomerDocumentsRequiredModel.builder().caseId(caseId)
			.documentMasterId(documentMasterModel.getId())	.documentId(aadharDocId).build());
		
		caseTemplateService.saveFileToAS3(aadharImage, bucketName);

		return CaseConverter.convert(caseModel);
	}

	private void setDefaultNullValue(CaseModel caseModel) {
		if (Objects.isNull(caseModel.getAggrmentDcoumentId()) || caseModel.getAggrmentDcoumentId() < 1) {
			caseModel.setAggrmentDcoumentId(null);
		}
		if (Objects.isNull(caseModel.getLegalHireCertId()) || caseModel.getLegalHireCertId() < 1) {
			caseModel.setLegalHireCertId(null);
		}

		if (Objects.isNull(caseModel.getFranchiseId()) || caseModel.getFranchiseId() < 1) {
			caseModel.setFranchiseId(null);
		}
		if (Objects.isNull(caseModel.getCommAddressId()) || caseModel.getCommAddressId() < 1) {
			caseModel.setCommAddressId(null);
		}
		if (Objects.isNull(caseModel.getCancelChequeDoccumentId()) || caseModel.getCancelChequeDoccumentId() < 1) {
			caseModel.setCancelChequeDoccumentId(null);
		}
		if (Objects.isNull(caseModel.getPanDocId()) || caseModel.getPanDocId() < 1) {
			caseModel.setPanDocId(null);
		}
		if (Objects.isNull(caseModel.getAadharDocId()) || caseModel.getAadharDocId() < 1) {
			caseModel.setAadharDocId(null);
		}
		if (Objects.isNull(caseModel.getAssignLawyerId()) || caseModel.getAssignLawyerId() < 1) {
			caseModel.setAssignLawyerId(null);
		}
		if (Objects.isNull(caseModel.getAssignNotaryId()) || caseModel.getAssignNotaryId() < 1) {
			caseModel.setAssignNotaryId(null);
		}
		if (Objects.isNull(caseModel.getCharteredAccountantId()) || caseModel.getCharteredAccountantId() < 1) {
			caseModel.setCharteredAccountantId(null);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public CaseBean savePanDetails(CasePanBean casePanBean, UserLoginBean userLoginBean) {
		CaseModel caseModel = caseRepo.getById(casePanBean.getId(), userLoginBean);

		String bucketName = ApplicationPropertiesService
				.getPropertyStringValue(PropertyKeys.AZ_S3_BUCKET_NAME);
		caseModel.setPanNumber(casePanBean.getPanNumber());
		FileBean panImage = casePanBean.getPanImage();
		panImage.setFileTitle(casePanBean.getId() + "_" + panImage.getFileTitle());
		DocumentModel documentModel = DocumentModel.builder().name(panImage.getFileTitle())
				.createdDate(LocalDateTime.now()).createBy(userLoginBean.getId())
				.contentType(panImage.getFileContentType()).bucketName(bucketName)
				.type(AppConstant.DocumentType.Pan.name()).build();
		Long oldDocId = caseModel.getPanDocId();
		Long panDocId = documentDao.add(documentModel);
		caseModel.setPanDocId(panDocId);
//		private FileBean panImage;
		documentModel.setId(panDocId);
		intsertCaseDocument(caseModel.getId(), documentModel,null);

		setDefaultNullValue(caseModel);
//		caseModel.setStatus(AppConstant.CaseStatus.PANVerified.label);
		caseModel.setPanVerified(casePanBean.isPanVerified());
		caseRepo.update(caseModel);
		caseCustomerDocumentsRequiredRepo.delete(casePanBean.getId(), oldDocId);
		caseDocumentDao.deleteByDocumentId(oldDocId);
		emailReposatory.deleteByDocId(oldDocId);
		documentDao.deleteById(oldDocId);
		
		DocumentMasterModel documentMasterModel =  utilityRepo.getMasterDocument("PAN");
		caseCustomerDocumentsRequiredRepo.add(CustomerDocumentsRequiredModel.builder().caseId(casePanBean.getId())
			.documentMasterId(documentMasterModel.getId()).documentId(panDocId).build());
		caseTemplateService.saveFileToAS3(panImage, bucketName);

		return CaseConverter.convert(caseModel);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public CaseBean saveCancelChequeDetails(CaseCancelChequeBean caseCancelChequeBean, UserLoginBean userLoginBean) {
		CaseModel caseModel = caseRepo.getById(caseCancelChequeBean.getId(), userLoginBean);

		String bucketName = ApplicationPropertiesService
				.getPropertyStringValue(PropertyKeys.AZ_S3_BUCKET_NAME);
		FileBean cancelCheckImage = caseCancelChequeBean.getCancelChequeImage();
		cancelCheckImage.setFileTitle(caseCancelChequeBean.getId() + "_" + cancelCheckImage.getFileTitle());
		DocumentModel documentModel = DocumentModel.builder().name(cancelCheckImage.getFileTitle())
				.createdDate(LocalDateTime.now()).createBy(userLoginBean.getId())
				.contentType(cancelCheckImage.getFileContentType()).type(AppConstant.DocumentType.CancelCheque.label)
				.bucketName(bucketName).build();
		Long oldDocId = caseModel.getCancelChequeDoccumentId();
		String accountNumber = caseCancelChequeBean.getAccountNumber();
		String ifscCode = caseCancelChequeBean.getIfscCode();
		caseKycChequeScanService.scanChequeDetails(caseCancelChequeBean, userLoginBean);
		Long cancelChequeDoccumentId = documentDao.add(documentModel);
		caseModel.setCancelChequeDoccumentId(cancelChequeDoccumentId);
		caseModel.setChequeNumber(caseCancelChequeBean.getChequeNumber());
		caseModel.setBankName(caseCancelChequeBean.getBankName());
		caseModel.setBankAddress(caseCancelChequeBean.getBankAddress());
		if(ArgumentHelper.isValid(caseCancelChequeBean.getAccountNumber())) {
			caseModel.setAccountNumber(caseCancelChequeBean.getAccountNumber());
		}else {
			caseModel.setAccountNumber(accountNumber);
		}
		
		if(ArgumentHelper.isValid(caseCancelChequeBean.getIfscCode())) {
			caseModel.setIfscCode(caseCancelChequeBean.getIfscCode());
		}else {
			caseModel.setIfscCode(ifscCode);
		}
		
		
//		private FileBean cancelCheckImage;
		documentModel.setId(cancelChequeDoccumentId);
		intsertCaseDocument(caseModel.getId(), documentModel,null);
		setDefaultNullValue(caseModel);
//		caseModel.setStatus(AppConstant.CaseStatus.CancelChequeUpload.label);
		caseRepo.update(caseModel);
		caseCustomerDocumentsRequiredRepo.delete(caseModel.getId(), oldDocId);
		caseDocumentDao.deleteByDocumentId(oldDocId);
		emailReposatory.deleteByDocId(oldDocId);
		documentDao.deleteById(oldDocId);
		
		DocumentMasterModel documentMasterModel =  utilityRepo.getMasterDocument("Cancelled Cheque");
		caseCustomerDocumentsRequiredRepo.add(CustomerDocumentsRequiredModel.builder().caseId(caseModel.getId())
			.documentMasterId(documentMasterModel.getId()).documentId(cancelChequeDoccumentId).build());
		caseTemplateService.saveFileToAS3(cancelCheckImage, bucketName);
		return CaseConverter.convert(caseModel);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public Long uploadCaseDocument(UploadCaseDocument uploadCaseDocument, UserLoginBean userLoginBean) {
		CaseModel caseModel = caseRepo.getById(uploadCaseDocument.getId(), userLoginBean);
		CaseValidator.validateCaseExists(uploadCaseDocument.getId(), caseModel);

		String bucketName = ApplicationPropertiesService
				.getPropertyStringValue(PropertyKeys.AZ_S3_BUCKET_NAME);
		FileBean document = uploadCaseDocument.getDocument();
		List<String> nonOverwrittenDocTypes = new ArrayList<>();
		nonOverwrittenDocTypes.add("Warrant copy");
		nonOverwrittenDocTypes.add("Death Certificate");
		nonOverwrittenDocTypes.add("Death Certificate 1");
		nonOverwrittenDocTypes.add("Death Certificate 2");
		String fileTitle=null;
		if (nonOverwrittenDocTypes.contains(uploadCaseDocument.getDocumentType())) {
			fileTitle = System.currentTimeMillis() + "_" + uploadCaseDocument.getId() + "_"
					+ uploadCaseDocument.getDocumentType() + "_" + document.getFileTitle();
		} else {
			fileTitle = uploadCaseDocument.getId() + "_" + uploadCaseDocument.getDocumentType() + "_"
					+ document.getFileTitle();
		}
		document.setFileTitle(fileTitle);
		DocumentModel documentModel = DocumentModel.builder().name(document.getFileTitle())
				.createdDate(LocalDateTime.now()).createBy(userLoginBean.getId())
				.contentType(document.getFileContentType()).type(uploadCaseDocument.getDocumentType())
				.bucketName(bucketName).build();
		List<CaseDocumentModel> caseDocumentModels = null;
		if (!nonOverwrittenDocTypes.contains(uploadCaseDocument.getDocumentType())) {
			
			if (ArgumentHelper.isValid(uploadCaseDocument.getUploadType())) {
				caseDocumentModels = caseDocumentDao.getByCaseIdAndDocTypeUploadType(uploadCaseDocument.getId(),
						uploadCaseDocument.getDocumentType(), uploadCaseDocument.getUploadType());
			} else {
				caseDocumentModels = caseDocumentDao.getByCaseIdAndDocType(uploadCaseDocument.getId(),
						uploadCaseDocument.getDocumentType());
			}
		}
//		CancelChequeDoccumentId
//		AadharDocId
//		PanDocId
//		LegalHireCertId
		Long documentId = documentDao.add(documentModel);
		documentModel.setId(documentId);
		intsertCaseDocument(caseModel.getId(), documentModel,uploadCaseDocument.getUploadType());
		if (!ArgumentHelper.isValid(uploadCaseDocument.getUploadType())) {
			if ("Aadhar".equalsIgnoreCase(uploadCaseDocument.getDocumentType())) {
				caseModel.setAadharDocId(documentId);
				setDefaultNullValue(caseModel);
				caseRepo.update(caseModel);
			}
			if ("Pan".equalsIgnoreCase(uploadCaseDocument.getDocumentType())) {
				caseModel.setPanDocId(documentId);
				setDefaultNullValue(caseModel);
				caseRepo.update(caseModel);
			}
			if ("Cancelled Cheque".equalsIgnoreCase(uploadCaseDocument.getDocumentType())) {
				caseModel.setCancelChequeDoccumentId(documentId);
				setDefaultNullValue(caseModel);
				caseRepo.update(caseModel);
			}
			if ("Lega Heir Certificate".equalsIgnoreCase(uploadCaseDocument.getDocumentType())
					|| "Legal Heir Certificate".equalsIgnoreCase(uploadCaseDocument.getDocumentType())) {
				caseModel.setLegalHireCertId(documentId);
				setDefaultNullValue(caseModel);
				caseRepo.update(caseModel);
			}
		}
		
		
		if (!nonOverwrittenDocTypes.contains(uploadCaseDocument.getDocumentType())) {
			if (ArgumentHelper.isNotEmpty(caseDocumentModels)) {
				CaseDocumentModel caseDocumentModel = caseDocumentModels.get(0);
				caseWitnessDao.updateAdharDocumentId(documentId, caseDocumentModel.getDocumentId(),caseModel.getId());
				caseWitnessDao.updatePanDocumentId(documentId, caseDocumentModel.getDocumentId(),caseModel.getId());
				
				caseSuretyInfoDao.updateAdharDocumentId(documentId, caseDocumentModel.getDocumentId(),caseModel.getId());
				caseSuretyInfoDao.updatePanDocumentId(documentId, caseDocumentModel.getDocumentId(),caseModel.getId());
				caseSuretyInfoDao.updateItrDocumentId(documentId, caseDocumentModel.getDocumentId(),caseModel.getId());
				
				caseDocumentDao.deleteByDocumentId(caseDocumentModel.getDocumentId());
				caseGeneratedDocumentsRequiredRepo.deleteDocId(caseDocumentModel.getDocumentId());
				caseCustomerDocumentsRequiredRepo.deleteDocId(caseDocumentModel.getDocumentId());
				emailReposatory.deleteByDocId(caseDocumentModel.getDocumentId());
				documentDao.deleteById(caseDocumentModel.getDocumentId());
			}
		}
		if (!ArgumentHelper.isValid(uploadCaseDocument.getUploadType())) {
			DocumentMasterModel documentMasterModel =  utilityRepo.getMasterDocument(uploadCaseDocument.getDocumentType());
			if(Objects.nonNull(documentMasterModel)) {
				if(documentMasterModel.getUploadedOrGenerated().equalsIgnoreCase("Generated")) {
					caseGeneratedDocumentsRequiredRepo.add(GeneratedDocumentsRequiredModel.builder().caseId(caseModel.getId())
						.documentMasterId(documentMasterModel.getId()).documentId(documentId).build());
					if (documentMasterModel.getParticulars()
							.equalsIgnoreCase(AppConstant.DocumentType.RtaLetter1.name())) {
						updateStatus(
								CaseStatusUpdateBean.builder().caseId(caseModel.getId())
										.status(AppConstant.CaseStatus.WaitingRTAResponseVeirifcation.label).build(),
								userLoginBean);
					}
				} else {
					if(documentMasterModel.getUploadedOrGenerated().equalsIgnoreCase("Uploaded")) {
						caseCustomerDocumentsRequiredRepo.add(CustomerDocumentsRequiredModel.builder().caseId(caseModel.getId())
								.documentMasterId(documentMasterModel.getId()).documentId(documentId).build());
					}
				}
			}
		}
		
		caseTemplateService.saveFileToAS3(document, bucketName);
		
		/*
		 * notification for Document uploaded SMS
		 * */
		String smsText="";
		String smsTextFranchise="";
		ApplicationUserBean applicationUserBean = applicationUserService.getApplicationUserById(AppConstant.SYS_USER_ID);
		UserLoginBean userLoginBeanObj = loginService.getUserLoginApplicationUserId(applicationUserBean.getId());
		if(Objects.nonNull(uploadCaseDocument.getUploadType()) && uploadCaseDocument.getUploadType().equalsIgnoreCase("RTA"))
		{
			//smsText= MessageTemplateService.getMessage("RTA_DOCUMENTS_UPLOADED_SMS"); 
			smsText=AppConstant.APP_RTA_DOCUMENTS_UPLOADED_SMS;//"The RTA response has been upload for case {0}. Please verify.\r\nADHYATA IMF PVT. LTD";
			smsTextFranchise =AppConstant.APP_RTA_DOCUMENTS_UPLOADED_SMS;
		}
		else
		{	if(userLoginBean.getApplicationUserBean().getUserType().equalsIgnoreCase(AppConstant.USER_TYPE_ADVOCATE))
			{
				smsText= AppConstant.LAWYER_CASE_DOCUMENT_UPLOAD;
				smsTextFranchise =AppConstant.LAWYER_CASE_DOCUMENT_UPLOAD;
			}
		    else
		    {
			 smsText= MessageTemplateService.getMessage("DOCUMENTS_UPLOADED_SMS"); 
			 smsTextFranchise = MessageTemplateService.getMessage("DOCUMENTS_UPLOADED_SMS"); 
		    }
		}
		//sysadmin sms
		if(userLoginBean.getApplicationUserBean().getUserType().equalsIgnoreCase(AppConstant.USER_TYPE_ADVOCATE))
		{ 
			smsText = MessageFormat.format(smsText,applicationUserBean.getFirstName(), caseModel.getReferenceNumber());
		}
		else
		{
			smsText = MessageFormat.format(smsText, caseModel.getReferenceNumber());
		}
		sendSMSPostApplicationStatusChange(userLoginBeanObj.getMobileNo(),smsText);
		//franchise user sms
		ApplicationUserBean applicationFranchiseUserBean = applicationUserService
				.getApplicationUsersDtlForGivenFranchiseOwnerIdAndUserType(caseModel.getFranchiseId(),
						AppConstant.USER_TYPE_FRANCHISE);
		if(Objects.nonNull(applicationFranchiseUserBean)) {
			UserLoginBean franchiseUserLoginBeanObj = loginService.getUserLoginApplicationUserId(applicationFranchiseUserBean.getId());
			if(Objects.nonNull(franchiseUserLoginBeanObj)) {
				if(userLoginBean.getApplicationUserBean().getUserType().equalsIgnoreCase(AppConstant.USER_TYPE_ADVOCATE))
				{ 
					smsTextFranchise = MessageFormat.format(smsTextFranchise,applicationFranchiseUserBean.getFirstName(), caseModel.getReferenceNumber());
				}
				else
				{
					smsTextFranchise = MessageFormat.format(smsTextFranchise, caseModel.getReferenceNumber());
				}
				
				
				sendSMSPostApplicationStatusChange(franchiseUserLoginBeanObj.getMobileNo(),smsTextFranchise);
			}
		}
		
		
		if(userLoginBean.getApplicationUserBean().getUserType().equalsIgnoreCase(AppConstant.USER_TYPE_ADVOCATE))
		{
			/*
			 * To do include lawyer update case by uploading new document scenario.
			 */
			   String smsText1= AppConstant.LAWYER_CASE_DOCUMENT_UPLOAD;  //MessageTemplateService.getMessage("CASE_ASSIGNED");
			    UserLoginBean custUserLoginBeanObj = loginService.getUserLogin(caseModel.getUserId()); 
			  
	    		smsText1 = MessageFormat.format(smsText1,custUserLoginBeanObj.getApplicationUserBean().getFirstName(), caseModel.getReferenceNumber());
	    		sendSMSPostApplicationStatusChange(custUserLoginBeanObj.getMobileNo(),smsText1);
		}
		
		
		return documentId;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public Long deleteCaseDocument(DeleteCaseDocumentBean deleteCaseDocumentBean, UserLoginBean userLoginBean) {
		CaseModel caseModel = caseRepo.getById(deleteCaseDocumentBean.getCaseId(), userLoginBean);
		CaseValidator.validateCaseExists(deleteCaseDocumentBean.getCaseId(), caseModel);
		DocumentModel doc = documentDao.getById(deleteCaseDocumentBean.getDocumentId());
		caseDocumentDao.deleteByDocumentId(doc.getId());
		FileCloudUtil.delete(doc.getBucketName(), doc.getName());
		emailReposatory.deleteByDocId(deleteCaseDocumentBean.getDocumentId());
		return Long.valueOf(documentDao.deleteById(deleteCaseDocumentBean.getDocumentId()));
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public CaseBean customerKycConfirmation(CustomerKYCConfirmationBean customerKycConfirmation,
			UserLoginBean userLoginBean) {
		CaseModel caseModel = caseRepo.getById(customerKycConfirmation.getCaseId(), userLoginBean);
		String bucketName = ApplicationPropertiesService
				.getPropertyStringValue(PropertyKeys.AZ_S3_BUCKET_NAME);
		FileBean agreementFile = customerKycConfirmation.getAgreementBean();
		agreementFile.setFileTitle(
				caseModel.getId() + "_" + AppConstant.CUSTOMER_AGREEMENT_TEMPLATE_NAME.replaceAll("docx", "pdf"));
		agreementFile.setFileContentType(AppConstant.JAVA_SCRIPT_DATA_PDF_BASE64);
		DocumentModel documentModel = DocumentModel.builder().name(agreementFile.getFileTitle())
				.createdDate(LocalDateTime.now()).createBy(userLoginBean.getId())
				.type(AppConstant.DocumentType.CustomerAgreement.name())
				.contentType(AppConstant.JAVA_SCRIPT_DATA_PDF_BASE64).bucketName(bucketName).build();
		Long oldDocId = caseModel.getAggrmentDcoumentId();
		Long aggrmentDcoumentId = documentDao.add(documentModel);
		documentModel.setId(aggrmentDcoumentId);
		intsertCaseDocument(caseModel.getId(), documentModel,null);
		caseModel.setAggrmentDcoumentId(aggrmentDcoumentId);
		setDefaultNullValue(caseModel);
		caseRepo.update(caseModel);
		caseDocumentDao.deleteByDocumentId(oldDocId);
		emailReposatory.deleteByDocId(oldDocId);
		documentDao.deleteById(oldDocId);
		caseTemplateService.saveFileToAS3(agreementFile, bucketName);
		return CaseConverter.convert(caseModel);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public CaseBean saveCustomerCasePendingDetails(CustomerCasePendingDetails details, UserLoginBean userLoginBean) {
		CaseModel caseModel = caseRepo.getById(details.getCaseId(), userLoginBean);
//		witnesses
		List<WitnessModel> witnesses = CaseWitnessConverter.convertL(details.getWitnesses());
		witnesses.forEach(w -> {
			w.setCaseId(details.getCaseId());
			w.setCreatedDate(LocalDate.now());
			w.setCreateBy(userLoginBean.getId());
		});
		caseWitnessDao.addAll(witnesses);

		String bucketName = ApplicationPropertiesService
				.getPropertyStringValue(PropertyKeys.AZ_S3_BUCKET_NAME);
		FileBean hireFileBean = details.getHireCertificate();
		hireFileBean.setFileTitle(System.currentTimeMillis() + "_" + hireFileBean.getFileTitle());
		hireFileBean.setFileContentType(AppConstant.JAVA_SCRIPT_DATA_PDF_BASE64);
		DocumentModel documentModel = DocumentModel.builder().name(hireFileBean.getFileTitle())
				.createdDate(LocalDateTime.now()).createBy(userLoginBean.getId())
				.contentType(hireFileBean.getFileContentType()).type(AppConstant.DocumentType.Other.name())
				.bucketName(bucketName).build();
		Long oldDocId = caseModel.getLegalHireCertId();
		Long legalHireCertId = documentDao.add(documentModel);
		caseModel.setLegalHireCertId(legalHireCertId);
		setDefaultNullValue(caseModel);
		documentModel.setId(legalHireCertId);
		intsertCaseDocument(caseModel.getId(), documentModel, null);
		// Pending User Response
		caseRepo.update(caseModel);
		DocumentMasterModel documentMasterModel =  utilityRepo.getMasterDocument("Death Certificate 1");
		caseCustomerDocumentsRequiredRepo.add(CustomerDocumentsRequiredModel.builder().caseId(caseModel.getId())
			.documentMasterId(documentMasterModel.getId()).documentId(legalHireCertId).build());
		
		caseDocumentDao.deleteByDocumentId(oldDocId);
		emailReposatory.deleteByDocId(oldDocId);
		documentDao.deleteById(oldDocId);
		caseTemplateService.saveFileToAS3(hireFileBean, bucketName);
		return CaseConverter.convert(caseModel);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void saveCustomerCasePendingFolioDetails(CustomerCasePendingFolioDetails details,
			UserLoginBean userLoginBean) {
		Long caseId = details.getCaseId();
		CaseQuestionerModel caseQuestionerModel = caseQuestionerRepo.getCaseQuestionerAnsByCaseId(details.getCaseId());
		List<CaseShareCertificateDetailsModel> existingcaseShareCertificateDetailsModels = caseShareCertificateDetailsRepo
				.getCaseShareCertificateDetailsByCaseId(caseId);
		List<CaseWarrantDetailsModel> existingWrrantDetailsModels =  caseWarrantDetailsRepo.getCaseShareCertificateDetailsByCaseId(caseId);
		List<QuestionerModel> questioners = caseQuestionerModel.getQuestioners();
		details.getScripts().forEach(s -> {
			saveQuestionarShareCertAndWarrentDetails(caseId, existingcaseShareCertificateDetailsModels,
					existingWrrantDetailsModels, questioners, s);
			
		});
	}



	private void saveQuestionarShareCertAndWarrentDetails(Long caseId,
			List<CaseShareCertificateDetailsModel> existingcaseShareCertificateDetailsModels,
			List<CaseWarrantDetailsModel> existingWrrantDetailsModels, List<QuestionerModel> questioners,
			CaseScriptBean s) {
		s.setCaseId(caseId);
		Long scriptId = s.getScriptId();
		caseQuestionerRepo.deleteCaseScriptQuesCaseAndScriptId(caseId, scriptId);
		caseShareCertificateDetailsRepo.deleteByCaseIdAndScriptId(caseId, scriptId);
		caseWarrantDetailsRepo.deleteByCaseIdAndScriptId(caseId, scriptId);
		caseScriptRepo.deleteByCaseIdAndScriptId(caseId, scriptId);
		Long caseScriptId = caseScriptRepo.add(CaseScriptConverter.convert(s));
		List<CaseShareCertificateDetailsBean> caseShareCertificateDetails = s.getCaseShareCertificateDetails();
		if(ArgumentHelper.isNotEmpty(caseShareCertificateDetails)) {
			List<CaseShareCertificateDetailsBean> caseShareCertificateDetailsToSave = new ArrayList<CaseShareCertificateDetailsBean>();
			caseShareCertificateDetails.forEach(sc -> {
				if(ArgumentHelper.isValid(sc.getCertificateNo()) && ArgumentHelper.isValid(sc.getDistinctiveNo())) {
					sc.setCaseScriptId(caseScriptId);
					caseShareCertificateDetailsToSave.add(sc);
				}
			});
			caseShareCertificateDetailsRepo
					.addAll(CaseShareCertificateDetailsConverter.convertL(caseShareCertificateDetailsToSave));
		}else {
			//add Existing
			List<CaseShareCertificateDetailsModel> existingShareCertificateDetailsModelsForScript = existingcaseShareCertificateDetailsModels
					.stream().filter(shareCert -> shareCert.getScriptId().equals(scriptId))
					.collect(Collectors.toList());
			existingShareCertificateDetailsModelsForScript.forEach(sc->sc.setCaseScriptId(caseScriptId));
			caseShareCertificateDetailsRepo.addAll(existingShareCertificateDetailsModelsForScript);
		}
		List<CaseWarrantDetailsBean> caseWarrantDetails = s.getCaseWarrantDetails();
		if(ArgumentHelper.isNotEmpty(caseWarrantDetails)) {
			List<CaseWarrantDetailsBean> caseWarrantDetailToSave = new ArrayList<CaseWarrantDetailsBean>();
			caseWarrantDetails.forEach(sc -> {
				if(ArgumentHelper.isValid(sc.getWarrantNo()) && ArgumentHelper.isPositive(sc.getAmount())) {
					sc.setCaseScriptId(caseScriptId);
					caseWarrantDetailToSave.add(sc);
				}
			});
			caseWarrantDetailsRepo
					.addAll(CaseWarrantDetailsConverter.convertL(caseWarrantDetailToSave));
		}else {
			//add Existing
			List<CaseWarrantDetailsModel> existingWrrantDetailsModelsForScript = existingWrrantDetailsModels.stream().filter(shareCert -> shareCert.getScriptId().equals(scriptId))
			.collect(Collectors.toList());
			existingWrrantDetailsModelsForScript.forEach(sc->sc.setCaseScriptId(caseScriptId));
			caseWarrantDetailsRepo.addAll(existingWrrantDetailsModelsForScript);
		}
		List<QuestionerBean> questionerBeans = s.getQuestioners();
		if(ArgumentHelper.isEmpty(questionerBeans)) {
			//Existing
			List<QuestionerModel> scriptQuestioners = questioners.stream()
					.filter(q -> q.getScriptId().equals(scriptId)).collect(Collectors.toList());
			List<QuestionerModel> models = new ArrayList<>();
			scriptQuestioners.forEach(sq ->{
				models.add(QuestionerModel.builder().id(sq.getId()).question(sq.getQuestion()).answer(sq.getAnswer()).build());
			});
			caseQuestionerRepo.add(CaseQuestionerModel.builder().caseScriptId(caseScriptId).questioners(models).build());
		}else {
			List<QuestionerModel> models = new ArrayList<>();
			questionerBeans.forEach(sq ->{
				models.add(QuestionerModel.builder().id(sq.getId()).question(sq.getQuestion()).answer(sq.getAnswer()).build());
			});
			caseQuestionerRepo.add(CaseQuestionerModel.builder().caseScriptId(caseScriptId).questioners(models).build());
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public FileBean generateCaseDocument(CaseDocumentBean caseDocumentBean, UserLoginBean userLoginBean) {
		CaseModel caseModel = caseRepo.getById(caseDocumentBean.getCaseId(), userLoginBean);
		caseModel.setRtaLetter1PrviewOnly(caseDocumentBean.getRtaLetter1PrviewOnly());
		caseModel.setRtaLetter2PrviewOnly(caseDocumentBean.getRtaLetter2PrviewOnly());
//		caseModel.setLetter1PrviewOnly(caseDocumentBean.getLetterPrviewOnly());
		caseModel.setSignedDocList(caseDocumentBean.getSignedDocList());

		if (caseDocumentBean.getTemplateType().equalsIgnoreCase(AppConstant.DocumentType.RtaLetter1.name())) {
			caseDocumentBean.setTemplateType(AppConstant.DocumentType.RtaLetter1.label);
		} else if (caseDocumentBean.getTemplateType().equalsIgnoreCase(AppConstant.DocumentType.RtaLetter2.name())) {
			caseDocumentBean.setTemplateType(AppConstant.DocumentType.RtaLetter2.label);
		}
			
		FileBean fileTemplate = caseTemplateService.processTemplate(caseDocumentBean, userLoginBean, caseModel);
		
		/*
		 * notification for RTA Letter 1 generation SMS
		 * if document type RTA1 
		 * if(caseDocumentBean.getTemplateType().equals(AppConstant.DocumentType.RtaLetter1.label) && !caseDocumentBean.getRtaLetter1PrviewOnly()
		 */
		if(caseDocumentBean.getTemplateType().equals(AppConstant.DocumentType.RtaLetter1.label) && !ArgumentHelper.isTrueFAlse(caseDocumentBean.getRtaLetter1PrviewOnly()))
		{
			String smsText= MessageTemplateService.getMessage("RTA_LETTER_GENERATED_SMS"); 
			/*
			 * Customer notification
			 */
			UserLoginBean userLoginBeanObj = loginService.getUserLogin(caseModel.getUserId());
			ApplicationUserBean applicationUserBean = userLoginBeanObj.getApplicationUserBean();
			if(Objects.isNull(applicationUserBean)) {
				applicationUserBean = applicationUserService.getApplicationUserById(userLoginBeanObj.getApplicationUserId());
			}
			smsText = MessageFormat.format(smsText, caseModel.getReferenceNumber());
			sendSMSPostApplicationStatusChange(userLoginBean.getMobileNo(),smsText);
			//sendSMSAndEmailPostCaseStatusChange(applicationUserBean, userLoginBeanObj, caseModel.getReferenceNumber(), "RTA_LETTER_GENERATED_SMS");
			
					
			String eMailBoday = "";
			String website = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.WEBSITE);//"http://www.findmymoney.in";
			//UserLoginBean userLoginBeanObj = loginService.getUserLoginApplicationUserId(AppConstant.SYS_USER_ID);
			//String support_contactnumber = "91-8050570505";//userLoginBeanObj.getMobileNo(); //CUSTOMER_CARE_SUPPORT_NUMBER
			String support_contactnumber = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.CUSTOMER_CARE_SUPPORT_NUMBER);
			String team="Findmymoney";
//			if(mssageCode.equalsIgnoreCase("RTA_LETTER_GENERATED_SMS"))
//			{
				//smsText= MessageTemplateService.getMessage("RTA_LETTER_GENERATED_SMS");//AppConstant.APP_RESET_SMS_TEXT; //MessageTemplateService.getMessage("APP_RESET_SMS_TEXT"); // VALIDATION_SUBMISSION_SMS
				String subject = MessageTemplateService.getMessage("CASE_RTALETTER_GEN_EMAIL_SUB");//"Application approved";//
				eMailBoday = MessageTemplateService.getMessage("CASE_RTALETTER_GEN_EMAIL_BODY");
				eMailBoday = MessageFormat.format(eMailBoday, applicationUserBean.getFirstName(), caseModel.getReferenceNumber(),website);

//			}
				List<CaseDocumentModel> caseDocumentModels = caseDocumentDao.getByCaseId(caseModel.getId());
				List<Long> caseDocumentIds = new ArrayList(caseDocumentModels.size());
				if (ArgumentHelper.isNotEmpty(caseDocumentModels)) {
					caseDocumentModels.forEach(cd -> {
						if (cd.getType().equalsIgnoreCase(AppConstant.DocumentType.Aadhar.name())
								|| cd.getType().equalsIgnoreCase(AppConstant.DocumentType.Pan.name())
								|| cd.getType().equalsIgnoreCase(AppConstant.DocumentType.CancelCheque.name())
								|| cd.getType().equalsIgnoreCase(AppConstant.DocumentType.CancelCheque.label)
								|| cd.getType()
										.equalsIgnoreCase(AppConstant.DocumentType.ShareHolderDeathCertificate.name())
								|| cd.getType().equalsIgnoreCase(AppConstant.DocumentType.RtaLetter1.name())) {
							caseDocumentIds.add(cd.getDocumentId());
						}
					});

					List<String> emailCcIds = new ArrayList(10);

					ApplicationUserBean applicationAdminUserBean = applicationUserService
							.getApplicationUserById(AppConstant.SYS_USER_ID); // here assume Sys user Id is Admin.
					String ccEmailAdminAddress = applicationAdminUserBean.getEmailId();
					if (!Objects.isNull(ccEmailAdminAddress) && ccEmailAdminAddress.length() > 0) {
						emailCcIds.add(ccEmailAdminAddress);
					}

					ApplicationUserBean applicationFranchiseUserBean = applicationUserService
							.getApplicationUsersDtlForGivenFranchiseOwnerIdAndUserType(caseModel.getFranchiseId(),
									AppConstant.USER_TYPE_FRANCHISE);
					
					UserLoginBean franchiseUserLoginBeanObj = loginService.getUserLoginApplicationUserId(applicationFranchiseUserBean.getId());
					//smsText = MessageFormat.format(smsText, caseModel.getReferenceNumber());
					sendSMSPostApplicationStatusChange(franchiseUserLoginBeanObj.getMobileNo(),smsText);
					
					
					if (!Objects.isNull(applicationFranchiseUserBean)) {
						String ccEmailFranchiseAddress = applicationFranchiseUserBean.getEmailId();
						if (!Objects.isNull(ccEmailFranchiseAddress) && ccEmailFranchiseAddress.length() > 0) {
							emailCcIds.add(ccEmailFranchiseAddress);
						}
					}

					emailService.sendEmail(applicationUserBean, userLoginBeanObj, eMailBoday, subject, caseDocumentIds,
							emailCcIds);

				}
		}else if(caseDocumentBean.getTemplateType().equals(AppConstant.DocumentType.RtaLetter2.label) && !ArgumentHelper.isTrueFAlse(caseDocumentBean.getRtaLetter2PrviewOnly())) {
			String smsText= MessageTemplateService.getMessage("RTA_LETTER_GENERATED_SMS"); 
			/*
			 * Customer notification
			 */
			UserLoginBean userLoginBeanObj = loginService.getUserLogin(caseModel.getUserId());
			ApplicationUserBean applicationUserBean = userLoginBeanObj.getApplicationUserBean();
			if(Objects.isNull(applicationUserBean)) {
				applicationUserBean = applicationUserService.getApplicationUserById(userLoginBeanObj.getApplicationUserId());
			}
			smsText = MessageFormat.format(smsText, caseModel.getReferenceNumber());
			sendSMSPostApplicationStatusChange(userLoginBeanObj.getMobileNo(),smsText);
			//sendSMSAndEmailPostCaseStatusChange(applicationUserBean, userLoginBeanObj, caseModel.getReferenceNumber(), "RTA_LETTER_GENERATED_SMS");
			
			String eMailBoday = "";
			String website = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.WEBSITE);//"http://www.findmymoney.in";
			//smsText= MessageTemplateService.getMessage("RTA_LETTER_GENERATED_SMS");//AppConstant.APP_RESET_SMS_TEXT; //MessageTemplateService.getMessage("APP_RESET_SMS_TEXT"); // VALIDATION_SUBMISSION_SMS
			String subject = MessageTemplateService.getMessage("CASE_RTALETTER_GEN_EMAIL_SUB");//"Application approved";//
			eMailBoday = MessageTemplateService.getMessage("CASE_RTALETTER_GEN_EMAIL_BODY");
			eMailBoday = MessageFormat.format(eMailBoday, applicationUserBean.getFirstName(), caseModel.getReferenceNumber(),website);

			List<String> caseSignDoclst = caseModel.getSignedDocList();
			List<Long>  caseDocumentIds = new ArrayList(caseSignDoclst.size());
			//List<CaseDocumentModel>  caseDocumentModels = caseDocumentDao.getByCaseId(caseModel.getId());
			//List<Long>  caseDocumentIds = new ArrayList(caseDocumentModels.size());
			if(ArgumentHelper.isNotEmpty(caseSignDoclst)) {
				caseSignDoclst.forEach(cdoc->{
					caseDocumentIds.add(new Long(cdoc).longValue());
				});
			}
			List<String>  emailCcIds = new ArrayList(10);	
			
			ApplicationUserBean applicationAdminUserBean = applicationUserService.getApplicationUserById(AppConstant.SYS_USER_ID); // here assume Sys user Id is Admin. 
			String ccEmailAdminAddress = applicationAdminUserBean.getEmailId();
			if (!Objects.isNull(ccEmailAdminAddress) && ccEmailAdminAddress.length()>0) 
			{ 
				emailCcIds.add(ccEmailAdminAddress);
			}
			
			ApplicationUserBean applicationFranchiseUserBean = applicationUserService.getApplicationUsersDtlForGivenFranchiseOwnerIdAndUserType(caseModel.getFranchiseId(), AppConstant.USER_TYPE_FRANCHISE);
			UserLoginBean franchiseUserLoginBeanObj = loginService.getUserLoginApplicationUserId(applicationFranchiseUserBean.getId());
			//smsText = MessageFormat.format(smsText, caseModel.getReferenceNumber());
			sendSMSPostApplicationStatusChange(franchiseUserLoginBeanObj.getMobileNo(),smsText);
			
			if (!Objects.isNull(applicationFranchiseUserBean))
			{  String ccEmailFranchiseAddress = applicationFranchiseUserBean.getEmailId();
				if (!Objects.isNull(ccEmailFranchiseAddress) && ccEmailFranchiseAddress.length()>0) 
					{ 
						emailCcIds.add(ccEmailFranchiseAddress);
					}
			}
			
			
			emailService.sendEmail(applicationUserBean, userLoginBeanObj, eMailBoday, subject, caseDocumentIds,emailCcIds);
			
		}
			
		return fileTemplate;
	}
				


	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public Long intsertCaseDocument(Long caseId, DocumentModel documentModel, String uploadType) {
		CaseDocumentModel model = CaseDocumentModel.builder().build();
		model.setCaseId(caseId);
		model.setDocumentId(documentModel.getId());
		model.setType(documentModel.getType());
		model.setUploadType(uploadType);
		model.setCreatedDate(documentModel.getCreatedDate());
		model.setCreateBy(documentModel.getCreateBy());
		return caseDocumentDao.add(model);

	}

	public CaseDocumentTemplateBean getCaseDocumentTemplates(Long caseId, UserLoginBean userLoginBean) {
		CaseModel caseModel = caseRepo.getById(caseId, userLoginBean);
		//Get RTA Name by Company Name
		RtaDataModel rtaDataModel = rtaDataRepo.getRtaDataByCompanyName(caseModel.getCompanyName());
		List<RtaTemplateModel> rtaTemplateModels = rtaTemplatesDao.getRtaTemplateS(rtaDataModel.getRegistrarName());
		List<TemplateBean> templateTypes = new ArrayList<>();
		rtaTemplateModels.forEach(template -> {
			TemplateBean templateBean = TemplateBean.builder().templateType(template.getTemplateType())
					.rtaName(template.getName()).isFeeRequired(template.getIsFeeRequired()).build();
			if (template.getIsFeeRequired()) {
				CaseFeeModel caseFeeModel = caseFeeDao.getByCaseId(caseId, template.getTemplateType());
				if (Objects.nonNull(caseFeeModel)) {
					templateBean.setFeeValue(caseFeeModel.getFeeValue());
				}else {
					templateBean.setFeeValue(0D);
				}
			}
			templateTypes.add(templateBean);
		});
		return CaseDocumentTemplateBean.builder().caseId(caseId).templates(templateTypes).build();
	}

	


	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public CaseBean saveFranchiseDetails(CaseFranchiseBean caseFranchiseBean, UserLoginBean userLoginBean) {
		CaseModel caseModel = caseRepo.getById(caseFranchiseBean.getCaseId(), userLoginBean);
		caseModel.setFranchiseId(caseFranchiseBean.getFranchiseId());
		caseRepo.update(caseModel);
		return CaseConverter.convert(caseModel);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void update(CaseModel caseModel, UserLoginBean userLoginBean) {
		setDefaultNullValue(caseModel);
		caseRepo.update(caseModel);
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void updateCustomerAggrement(CaseModel caseModel, UserLoginBean userLoginBean) {
		caseRepo.updateCustomerAggrement(caseModel);
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void updateStatus(CaseStatusUpdateBean updateBean, UserLoginBean userLoginBean) {
		CaseModel caseModel = caseRepo.getById(updateBean.getCaseId(), userLoginBean);
		caseModel.setStatus(updateBean.getStatus());
		setDefaultNullValue(caseModel);
		caseRepo.update(caseModel);
		
		if(updateBean.getStatus().equalsIgnoreCase(AppConstant.CaseStatus.Closed.label))
		{
			String smsText= AppConstant.APP_CASE_CLOSED_SMS;     //MessageTemplateService.getMessage("CASE_CLOSED"); 
			
			/*
			 * Customer notification
			 */
			UserLoginBean userLoginBeanObj = loginService.getUserLogin(caseModel.getUserId());
			ApplicationUserBean applicationUserBean = userLoginBeanObj.getApplicationUserBean();
			if(Objects.isNull(applicationUserBean)) {
				applicationUserBean = applicationUserService.getApplicationUserById(userLoginBeanObj.getApplicationUserId());
			}
			smsText = MessageFormat.format(smsText, applicationUserBean.getFirstName(), caseModel.getReferenceNumber());
			sendSMSPostApplicationStatusChange(userLoginBeanObj.getMobileNo(),smsText);
		}
	}
	
	
	private ApplicationModel getByApplicationId(Long applicationId, UserLoginBean userLoginBean) {
		ApplicationModel model = applicationRepo.getById(applicationId, userLoginBean);
		model.setScripts(applicationScriptRepo.getByScriptByApplicationId(applicationId));
		return model;
	}

	private CaseModel createCaseModel(ApplicationModel applicationModel, UserLoginBean userLoginBean) {
		log.info("Preparing Case Object.");
		CaseModel caseModel = CaseModel.builder().build();
		BeanUtils.copyProperties(applicationModel, caseModel);
		caseModel.setStatus(AppConstant.CaseStatus.WaitingSubmission.label);
		caseModel.setAddressId(userLoginBean.getApplicationUserBean().getAddressId());
		caseModel.setFeeType(Objects.isNull(applicationModel.getFeeType())? AppConstant.FeeType.FixValue.name() :applicationModel.getFeeType());
		caseModel.setApplicationId(applicationModel.getId());
		List<CaseScriptModel> caseScripts = new ArrayList<>();
		applicationModel.getScripts().forEach(script -> {
			caseScripts.add(CaseScriptModel.builder().scriptId(script.getScriptId()).build());
		});
		caseModel.setScripts(caseScripts);
		caseModel.setReferenceNumber(generateReferenceNumber(caseModel));
		if(Objects.nonNull(userLoginBean.getApplicationUserBean().getReferalFranchiseId()) 
				&& userLoginBean.getApplicationUserBean().getReferalFranchiseId()>0) {
			caseModel.setFranchiseId(userLoginBean.getApplicationUserBean().getReferalFranchiseId());
		}else if(Objects.nonNull(userLoginBean.getApplicationUserBean().getReferalUserId()) 
					&& userLoginBean.getApplicationUserBean().getReferalUserId()>0) {
			UserLoginBean userLoginBeanRef = loginService.getUserLogin(userLoginBean.getApplicationUserBean().getReferalUserId());
				caseModel.setFranchiseId(userLoginBeanRef.getApplicationUserBean().getReferalPartnerFranchiseId());
		}else {
			caseModel.setFranchiseId(1L);
		}
		return caseModel;
	}
	
	private String generateReferenceNumber(CaseModel caseModel) {
		String referenceNumber;
		
		referenceNumber = caseModel.getFirstName().substring(0, 1);
//		if (ArgumentHelper.isValid(caseModel.getMiddleName())) {
//			referenceNumber = referenceNumber + "_" + caseModel.getMiddleName().substring(0, 1);
//		}
//		referenceNumber = referenceNumber + "_" + caseModel.getLastName().substring(0, 1);
		if(Objects.nonNull( caseModel.getLastName())) {
			referenceNumber = referenceNumber + caseModel.getLastName().substring(0, 1);
		}
		return referenceNumber;
	}
	
	public List<UploadCaseDocument> getUploadeSignedDocuments(Long caseId, UserLoginBean userLoginBean) {
		List<CaseDocumentModel>  caseDocumentModels = caseDocumentDao.getByCaseId(caseId);
		return getUploadeSignedDocuments(caseDocumentModels);
	}
	
	public List<UploadCaseDocument> getUploadRTAResponse(Long caseId, UserLoginBean userLoginBean) {
		List<CaseDocumentModel>  caseDocumentModels = caseDocumentDao.getByCaseId(caseId);
		return getUploadRTAResponse(caseDocumentModels);
	}
	

	private List<UploadCaseDocument> getUploadRTAResponse(List<CaseDocumentModel> caseDocumentModels) {
		List<CaseDocumentModel> ratDocumentModels = caseDocumentModels.stream()
				.filter(doc -> "RTA".equalsIgnoreCase(doc.getUploadType())).collect(Collectors.toList());
		List<UploadCaseDocument> rtaDocuments = new ArrayList<>();
		ratDocumentModels.forEach(doc->{
			rtaDocuments.add(UploadCaseDocument.builder().documentId(doc.getDocumentId()).documentType(doc.getType()).build());
		});
		return rtaDocuments;
	}

	private  List<UploadCaseDocument>  getUploadeSignedDocuments(List<CaseDocumentModel> caseDocumentModels) {
		List<CaseDocumentModel> signedDocumentModels = caseDocumentModels.stream()
				.filter(doc -> "Signed".equalsIgnoreCase(doc.getUploadType())).collect(Collectors.toList());
		List<UploadCaseDocument> signedDocuments = new ArrayList<>();
		signedDocumentModels.forEach(doc->{
			signedDocuments.add(UploadCaseDocument.builder().documentId(doc.getDocumentId()).documentType(doc.getType()).build());
		});
		return signedDocuments;
	}
	
	public void sendSMSPostApplicationStatusChange(String mobileNo, String smsText) {
		mobileNo = mobileNo.replaceAll("\\+", "");
		// "9921399990"
		smsText = smsText.replaceAll("<br/>", "");
		smsText = smsText.replaceAll("&nbsp;", "");
		smsService.sendSms(smsText, mobileNo);
	}
	
	private void sendSMSAndEmailPostCaseStatusChange(ApplicationUserBean applicationUserBean, UserLoginBean userLoginBean, String caseReference, String mssageCode)
	{
		log.info("Eending Case Status change..");
		// TOD Send SMS/Email to user.
		
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
		String website = "http://www.findmymoney.in";
		//UserLoginBean userLoginBeanObj = loginService.getUserLoginApplicationUserId(AppConstant.SYS_USER_ID);
		String support_contactnumber = "91-8050570505";//userLoginBeanObj.getMobileNo();
		String team="Findmymoney";
		if(mssageCode.equalsIgnoreCase("RTA_LETTER_GENERATED_SMS"))
		{
			smsText= MessageTemplateService.getMessage("RTA_LETTER_GENERATED_SMS");//AppConstant.APP_RESET_SMS_TEXT; //MessageTemplateService.getMessage("APP_RESET_SMS_TEXT"); // VALIDATION_SUBMISSION_SMS
			subject = MessageTemplateService.getMessage("CASE_RTALETTER_GEN_EMAIL_SUB");//"Application approved";//
			eMailBoday = MessageTemplateService.getMessage("CASE_RTALETTER_GEN_EMAIL_BODY");
			eMailBoday = MessageFormat.format(eMailBoday, applicationUserBean.getFirstName(), caseReference,website);

		}
		 smsText = MessageFormat.format(smsText,caseReference);
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

		EmailBodyModel emailBody = new EmailBodyModel();
		emailBody.seteMailBoday(eMailBoday.getBytes());
		EmailModel emailModel = new EmailModel();
		emailModel.setFromEmail(senderEmailAddress);
		emailModel.setSubject(subject);
		emailModel.setEmailBody(emailBody);
		emailModel.setEmailTos(emailTos);
		emailReposatory.addEmail(emailModel, userLoginBean);
		log.info("Email Application submission Validation sucessfull " + emailModel.toString());
		sendSMSPostApplicationStatusChange(userloginModel.getMobileNo(), smsText);
	}
	
	public List<DocumentMasterBean> getMasterDocumentList(String uploadedOrGenerated,Long caseId, UserLoginBean userLoginBean) {
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
		if(ArgumentHelper.isPositiveWithZero(caseId)) {
			CaseModel  model = caseRepo.getById(caseId, userLoginBean);
			List<StatusMasterModel> stage2Statues=  utilityRepo.getMasterStatusList("Stage 2");
			Optional<StatusMasterModel> statusFound = stage2Statues.stream().filter(st -> st.getStatus().trim().equalsIgnoreCase(model.getStatus().trim())).findFirst();
			if(statusFound.isPresent()){
				beans.addAll(getMasterDocumentListForRta2Preveiw(caseId));
			}
		}
		return beans;
	}
	
	public List<DocumentMasterBean> getMasterDocumentListForRta2Preveiw(Long caseId) {
		List<DocumentMasterBean> beans = new ArrayList<>();

		List<GeneratedDocumentsRequiredModel> generatedDocumentsRequiredModels = caseGeneratedDocumentsRequiredRepo
				.getByCaseId(caseId);
		generatedDocumentsRequiredModels = generatedDocumentsRequiredModels.stream()
				.filter(gd -> ArgumentHelper.isPositive(gd.getId())).collect(Collectors.toList());
		List<CustomerDocumentsRequiredModel> customerDocumentsRequiredModels = caseCustomerDocumentsRequiredRepo
				.getByCaseId(caseId);
		customerDocumentsRequiredModels = customerDocumentsRequiredModels.stream()
				.filter(cdr -> ArgumentHelper.isPositive(cdr.getId())).collect(Collectors.toList());
		generatedDocumentsRequiredModels.forEach(gd -> {
			Optional<DocumentMasterBean> found = beans.stream()
					.filter(d -> d.getParticulars().equalsIgnoreCase(gd.getDocumentMasterType())).findFirst();
			if (!found.isPresent()) {
				beans.add(DocumentMasterBean.builder().particulars(gd.getDocumentMasterType())
						.type(gd.getDocumentMasterType()).build());
			}
		});
		customerDocumentsRequiredModels.forEach(cdr -> {
			Optional<DocumentMasterBean> found = beans.stream()
					.filter(d -> d.getParticulars().equalsIgnoreCase(cdr.getDocumentMasterType())).findFirst();
			if (!found.isPresent()) {
				beans.add(DocumentMasterBean.builder().particulars(cdr.getDocumentMasterType())
						.type(cdr.getDocumentMasterType()).build());
			}
		});
		return beans;
	}
	
	
	public CaseTemplateBean getCaseTemplateBean(CaseModel caseModel, Long caseId, String templateName) {
		CaseTemplateBean caseTemplateBean = new CaseTemplateBean();
		BeanUtils.copyProperties(caseModel, caseTemplateBean);
		caseTemplateBean.setTemplateName(templateName);
//		UserLogin userLogin = userLoginRepo.getUserLogin(caseModel.getUserId());
		UserLoginBean appUserBean = loginService.getUserLogin(caseModel.getUserId());
		caseTemplateBean.setEMail(appUserBean.getApplicationUserBean().getEmailId());
		
		caseTemplateBean.setRtaLetter1PrviewOnly(caseModel.getRtaLetter1PrviewOnly());
		caseTemplateBean.setRtaLetter2PrviewOnly(caseModel.getRtaLetter2PrviewOnly());
		
		AddressModel communcationAddress =  addressRepo.getAddressById(caseTemplateBean.getCommAddressId());
		caseTemplateBean.setCommAddress(communcationAddress);
		
		RtaDataModel rtaDataModel = rtaDataRepo.getRtaDataByCompanyName(caseTemplateBean.getCompanyName());
		caseTemplateBean.setRtaDataModel(rtaDataModel);
		
		List<CaseScriptModel> caseScriptModels = caseScriptRepo.getByCaseId(caseModel.getId());
		List<Long> scriptIds = caseScriptModels.stream().map(script -> script.getScriptId()).distinct()
				.collect(Collectors.toList());
		List<ScriptModel> scriptModels = scriptRepo.getScripts(scriptIds);
		List<CaseShareCertificateDetailsModel> shareCertificateDetailsModels = caseShareCertificateDetailsRepo.getCaseShareCertificateDetailsByCaseId(caseId);
		List<CaseWarrantDetailsModel> caseWarrantDetailsModels = caseWarrantDetailsRepo.getCaseShareCertificateDetailsByCaseId(caseId);
		caseScriptModels.forEach(caseScript -> {
			Optional<ScriptModel> script = scriptModels.stream().filter(s -> s.getId().equals(caseScript.getScriptId()))
					.findFirst();
			caseScript.setScriptModel(script.orElse(null));
			List<CaseShareCertificateDetailsModel> shareCertificateModels = shareCertificateDetailsModels.stream().filter(s -> s.getScriptId().equals(caseScript.getScriptId())).collect(Collectors.toList());
			caseScript.setShareCertificateDetailsModels(shareCertificateModels);
			List<CaseWarrantDetailsModel> warrantDetailsModels = caseWarrantDetailsModels.stream().filter(s -> s.getScriptId().equals(caseScript.getScriptId())).collect(Collectors.toList());
			caseScript.setCaseWarrantDetailsModels(warrantDetailsModels);
		});
		caseTemplateBean.setScripts(caseScriptModels);
		
		 List<CaseDeathCertificateDtlModel> caseDeathCertificateDtlModels = caseDeathCertificateDtlRepo.getByCaseId(caseId);
		 caseTemplateBean.setCaseDeathCertificateDtlModels(caseDeathCertificateDtlModels);
		 List<WitnessModel> witnesses =  caseWitnessDao.getByCaseId(caseId);
		 caseTemplateBean.setWitnesses(witnesses);
		 List<SuretyInfoModel> suretyInfos = caseSuretyInfoDao.getByCaseId(caseId);
		 caseTemplateBean.setSuretyInfos(suretyInfos);
		return caseTemplateBean;
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void updateLawyerCaseDetails(LawyerCaseDetailsBean lawyerCaseDetailsBean, UserLoginBean userLoginBean) {
		
		/*
		 * No use for now.
		 * 
		 * 
		 * 
		 */
		
	}
	
	public List<CaseFieldsModel> getFieldsByCaseId(Long caseId) {
		return caseFieldsRepo.getByCaseId(caseId);
	}
	
	
	
	public LawyerCaseDetailsBean getLawyerCaseDtlByCaseId (Long caseId, UserLoginBean userLoginBean) {
		CaseBean bean = getById(caseId, userLoginBean);
		
		
		List<LawyerCaseCommentsDtlBean>  lawyerCaseCommentsDtlBeanlst = lawyerCaseCommentsDtlService.getAllLawyerCaseCommentsDtls(caseId);
		
		LawyerCaseDetailsBean lawyerCaseDetailsBean = new LawyerCaseDetailsBean();
		lawyerCaseDetailsBean.setCaseId(caseId);
		lawyerCaseDetailsBean.setLawyerCaseCommentsDtlBean(lawyerCaseCommentsDtlBeanlst);
	
		// Processing Payment Info
		// Only three Fee types copied from application
		Long userTypeId = new Long (utilityService.applicationUserType.get(userLoginBean.getApplicationUserBean().getUserType()));
		List<CaseFeeModel> feeModels = caseFeeDao.getByCaseIdAndUserTypeId(caseId,userTypeId);
		CaseFeesDetails paymentDetails = null;
		// Additional Fees Details
		CaseFeesDetails additionalFeesDetails = null;
		List<CaseFeeBean> fees = new ArrayList<>();
		feeModels.forEach(fee->{
			if(fee.getReceivedFeeValue()>0)
			{
				fees.add(CaseFeeConverter.convert(fee));
			}
		});
		paymentDetails = CaseFeesDetails.builder().caseId(caseId).fees(fees).build();
		lawyerCaseDetailsBean.setAdditionalFeesDetails(paymentDetails);

		
		
		//Customer Documents Required
		
		List<CustomerDocumentsRequiredModel> customerDocumentsRequiredModels = caseCustomerDocumentsRequiredRepo.getCaseCustRequireDocumentsByCaseIdAndUserTypeId(caseId, userTypeId);
		if(ArgumentHelper.isNotEmpty(customerDocumentsRequiredModels)) {
			
			List<CustomerDocumentsRequiredBean>  customerDocumentsRequiredBeans = CustomerDocumentsRequiredConverter.convert(customerDocumentsRequiredModels);
			customerDocumentsRequiredBeans.forEach(customerDocumentsRequiredBeanobj->{
				if(!(Objects.isNull(customerDocumentsRequiredBeanobj.getId()) || customerDocumentsRequiredBeanobj.getId() <=0))
				 {  customerDocumentsRequiredBeanobj.setIsCustomerDocumentRequire(Boolean.TRUE);
				 }
				else{
					customerDocumentsRequiredBeanobj.setIsCustomerDocumentRequire(Boolean.FALSE);
				}
				if(customerDocumentsRequiredBeanobj.getDoccumentName().equalsIgnoreCase(AppConstant.DocumentType.Aadhar.label)
						||(customerDocumentsRequiredBeanobj.getDoccumentName().equalsIgnoreCase(AppConstant.DocumentType.Pan.label))
						||(customerDocumentsRequiredBeanobj.getDoccumentName().equalsIgnoreCase(AppConstant.DocumentType.CancelCheque.label)))
						
				 {
					customerDocumentsRequiredBeanobj.setIsCustomerDocumentMandatory(Boolean.TRUE);
				 }
				else
				{
					customerDocumentsRequiredBeanobj.setIsCustomerDocumentMandatory(Boolean.FALSE);
				}
				
				if((customerDocumentsRequiredBeanobj.getDoccumentName().equalsIgnoreCase(AppConstant.DocumentType.CourierReceipt.label)))
				{
					customerDocumentsRequiredBeanobj.setIsCustomerDocumentRequire(Boolean.TRUE);
					customerDocumentsRequiredBeanobj.setIsCustomerDocumentMandatory(Boolean.TRUE);
				}
				if((customerDocumentsRequiredBeanobj.getDoccumentName().equalsIgnoreCase(AppConstant.DocumentType.ShareHolderDeathCertificate.label)) && (bean.getIsShareholderDeathCertificationRequire()))
				{
					customerDocumentsRequiredBeanobj.setIsCustomerDocumentRequire(Boolean.TRUE);
					customerDocumentsRequiredBeanobj.setIsCustomerDocumentMandatory(Boolean.TRUE);
				}
			});
			
//			bean.setCustomerDocuments(CaseCustomerDocumentsRequiredBean.builder().caseId(caseId)
//					.customerDocuments(customerDocumentsRequiredBeans).build());
			lawyerCaseDetailsBean.setLawyerUploadedDocuments(CaseCustomerDocumentsRequiredBean.builder().caseId(caseId)
					.customerDocuments(customerDocumentsRequiredBeans).build());
		}
		
//		lawyerCaseDetailsBean.setLawyerUploadedDocuments(bean.getCustomerDocuments());
		lawyerCaseDetailsBean.setReferenceNumber(bean.getReferenceNumber());
		lawyerCaseDetailsBean.setFirstName(bean.getFirstName());
		lawyerCaseDetailsBean.setMiddleName(bean.getMiddleName());
		lawyerCaseDetailsBean.setLastName(bean.getLastName());
		lawyerCaseDetailsBean.setDateOfBirth(bean.getDateOfBirth());
		lawyerCaseDetailsBean.setGender(bean.getGender());
		lawyerCaseDetailsBean.setCompanyName(bean.getCompanyName());
		lawyerCaseDetailsBean.setStatus(bean.getStatus());
		lawyerCaseDetailsBean.setUserId(bean.getUserId());
		lawyerCaseDetailsBean.setMobileNumber(bean.getMobileNumber());
		lawyerCaseDetailsBean.setEMail(bean.getEMail());
		
		
		
		lawyerCaseDetailsBean.setAddressId(bean.getAddressId());
		lawyerCaseDetailsBean.setCommAddressId(bean.getCommAddressId());
		lawyerCaseDetailsBean.setApplicationId(bean.getApplicationId());
		lawyerCaseDetailsBean.setAadharNumber(bean.getAadharNumber());
		lawyerCaseDetailsBean.setPanNumber(bean.getPanNumber());
		lawyerCaseDetailsBean.setCancelChequeDoccumentId(bean.getCancelChequeDoccumentId());
		lawyerCaseDetailsBean.setAadharDocId(bean.getAadharDocId());
		lawyerCaseDetailsBean.setPanDocId(bean.getPanDocId());
		lawyerCaseDetailsBean.setAggrmentDcoumentId(bean.getAggrmentDcoumentId());
		
		lawyerCaseDetailsBean.setAssignLawyerId(bean.getAssignLawyerId());
		lawyerCaseDetailsBean.setDhareHolderDeathCertificate1Id(bean.getDhareHolderDeathCertificate1Id());
		lawyerCaseDetailsBean.setAddress(bean.getAddress());
		lawyerCaseDetailsBean.setCommAddress(bean.getCommAddress());
		lawyerCaseDetailsBean.setStage(bean.getStage());
//		lawyerCaseDetailsBean.setAdditionalFeesDetails(bean.getAdditionalFeesDetails());
		lawyerCaseDetailsBean.setLawyer(bean.getLawyer());
		lawyerCaseDetailsBean.setUploadSignedDocuments(bean.getUploadSignedDocuments());

		return lawyerCaseDetailsBean;
	}
}
