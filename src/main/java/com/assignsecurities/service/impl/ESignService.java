package com.assignsecurities.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.MessageTemplateService;
import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.app.exception.ValidationException;
import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.app.util.FileCloudUtil;
import com.assignsecurities.app.util.PersonNameUtil;
import com.assignsecurities.app.util.Util;
import com.assignsecurities.bean.ApplicationUserBean;
import com.assignsecurities.bean.ESigningResponseBean;
import com.assignsecurities.bean.FileBean;
import com.assignsecurities.bean.GetDocumentRequestBean;
import com.assignsecurities.bean.ReferralsCommisionDtlBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.bean.ValidationError;
import com.assignsecurities.bean.surepass.AadharSignResponse;
import com.assignsecurities.bean.surepass.AadharSignResponse.NameMatch;
import com.assignsecurities.bean.surepass.EsignInitializeData;
import com.assignsecurities.bean.surepass.GetDocumentResponse;
import com.assignsecurities.bean.surepass.GetUploadLinkResponse;
import com.assignsecurities.bean.surepass.GetUploadLinkResponse.Fields;
import com.assignsecurities.converter.ReferralsCommisionDtlConverter;
import com.assignsecurities.dm.PropertyKeys;
import com.assignsecurities.domain.DocumentModel;
import com.assignsecurities.domain.ESignDocumentModel;
import com.assignsecurities.repository.impl.DocumentDao;
import com.assignsecurities.repository.impl.ESignDocumentRepo;
import com.assignsecurities.repository.impl.ReferralsCommisionDtlRepo;
import com.google.gson.Gson;
import com.lowagie.text.pdf.PdfReader;

import lombok.extern.slf4j.Slf4j;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
@Slf4j
public class ESignService {
	
	@Autowired
	private DocumentDao documentDao;
	
	@Autowired
	private ESignSurepassClientidService eSignSurepassClientidService;
	
	@Autowired
	private ApplicationUserService applicationUserService;
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private CaseTemplateService caseTemplateService;
	
	@Autowired
	private FranchiseService franchiseSErvice;
	
	@Autowired
	private ESignTemplateService eSignTemplateService;
	
	@Autowired
	private ESignDocumentRepo eSignDocumentRepo; 
	
	@Autowired
	private ReferralsCommisionDtlRepo referralsCommisionDtlRepo;
	
	@Autowired
	private EmailService emailService;
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public ESigningResponseBean initializeAndUploadAgreement(UserLoginBean userLoginBean, Long referralUserId) {
		if (AppConstant.USER_TYPE_ADMIN.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())
				&& (Objects .isNull(referralUserId) || referralUserId<0)) {
			List<ValidationError> errorList = new ArrayList<>();
			errorList.add(ValidationError.builder().message("Please do the referral agriment with customer first.").build());
			if (ArgumentHelper.isNotEmpty(errorList)) {
				throw new ValidationException(errorList);
			}
		}
		boolean isIntegrationRequired = ApplicationPropertiesService
				.getPropertyBooleanValue(PropertyKeys.SURE_PASS_INTEGRATION_REQUIRED);
		ESigningResponseBean eSigningResponseBean = ESigningResponseBean.builder().build();
		
		UserLoginBean referralUserBean = userLoginBean;
		
		// if Document exist then details from referralUserId
		if(Objects.nonNull(referralUserId) && referralUserId>0) {
			referralUserBean = loginService.getUserLogin(referralUserId);
		}
		
//		if (Objects.nonNull(referralUserId) && referralUserId>0 &&
//				AppConstant.USER_TYPE_ADMIN.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())){
//			referralUserBean = loginService.getUserLogin(referralUserId);
//		}
		eSigningResponseBean.setReferralUserId(referralUserBean.getId());
		if(!isIntegrationRequired) {
			//Update Status as eSign complete based on user type (Referral or franchise referral)
			if (AppConstant.USER_TYPE_REFERRAL_PARTNER.equalsIgnoreCase(referralUserBean.getApplicationUserBean().getUserType())){
				referralsCommisionDtlRepo.updateESignAgreementStatusForReferralUser(AppConstant.FRANCHISE_ESIGN_AGREEMENT_STATUS_COMPLETED, referralUserBean.getId());
			}else {
				referralsCommisionDtlRepo.updateESignAgreementStatusForReferralFranchise(AppConstant.FRANCHISE_ESIGN_AGREEMENT_STATUS_COMPLETED,
						referralUserBean.getApplicationUserBean().getFranchiseId());
			}
			eSigningResponseBean.setESigningRequired(Boolean.FALSE);
			     		
			return eSigningResponseBean;
		}
		eSigningResponseBean.setESigningRequired(Boolean.TRUE);
				
		
		String baseUrl = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.SURE_PASS_URL);
//		baseUrl = "https://esign-api.surepass.io";
		String authorizationToken = ApplicationPropertiesService
				.getPropertyStringValue(PropertyKeys.SURE_PASS_AUTH_TOKEN);
		String surePassSign1XCoOrdinate = ApplicationPropertiesService
				.getPropertyStringValue(PropertyKeys.SURE_PASS_SIGN_1_X_COORDINATE);
		String surePassSign1YCoOrdinate = ApplicationPropertiesService
				.getPropertyStringValue(PropertyKeys.SURE_PASS_SIGN_1_Y_COORDINATE);
		String surePassSign2XCoOrdinate = ApplicationPropertiesService
				.getPropertyStringValue(PropertyKeys.SURE_PASS_SIGN_2_X_COORDINATE);
		String surePassSign2YCoOrdinate = ApplicationPropertiesService
				.getPropertyStringValue(PropertyKeys.SURE_PASS_SIGN_2_Y_COORDINATE);
		

		try {
			//generate and store PDF agreement first time only if generated then take that
			ESignDocumentModel eSignDocumentModel =  eSignDocumentRepo.getByUserIdAndType(referralUserBean.getId(), referralUserBean.getApplicationUserBean().getUserType());
			
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
			String fullNAme = PersonNameUtil.getFullName(referralUserBean.getApplicationUserBean().getFirstName(),
					referralUserBean.getApplicationUserBean().getMiddleName(), referralUserBean.getApplicationUserBean().getLastName());
//			UserLoginBean appUserBean = null;
//			Long franchiseId = null;
//			if (AppConstant.USER_TYPE_FRANCHISE.equalsIgnoreCase(referralUserBean.getApplicationUserBean().getUserType())
//					|| AppConstant.USER_TYPE_FRANCHISE_USER.equalsIgnoreCase(referralUserBean.getApplicationUserBean().getUserType())) {
//				franchiseId = referralUserBean.getApplicationUserBean().getFranchiseId();
//				FranchiseBean franchiseBean=  franchiseSErvice.getFranchiseById(franchiseId);
//				fullNAme = franchiseBean.getName();
//			}
			String mobileNo = referralUserBean.getMobileNo();
//			mobileNo = "9890960765";
			String emialId = referralUserBean.getApplicationUserBean().getEmailId();
			if (AppConstant.USER_TYPE_ADMIN.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
				fullNAme = PersonNameUtil.getFullName(userLoginBean.getApplicationUserBean().getFirstName(),
						userLoginBean.getApplicationUserBean().getMiddleName(), userLoginBean.getApplicationUserBean().getLastName());
				mobileNo = userLoginBean.getMobileNo();
				emialId = userLoginBean.getApplicationUserBean().getEmailId();
			}

			if(Objects.isNull(emialId)) {
				List<ValidationError> errorList = new ArrayList<>();
				errorList.add(ValidationError.builder().message("Please update the email in profile to processed on this step.").build());
				if (ArgumentHelper.isNotEmpty(errorList)) {
					throw new ValidationException(errorList);
				}
			}
//					"narendra.chouhan129@gmail.com"; 
			String profileJsonString =  
					"    ,\"prefill_options\": {" + 
					"        \"full_name\": \""+fullNAme+"\"," + 
					"        \"mobile_number\": \""+mobileNo+"\"," + 
					"        \"user_email\": \""+emialId+"\"" + 
					"    }" ;
			
			
			DocumentModel documentModel = null;
			byte[] idCardByteArray;
//			if(Objects.isNull(eSignDocumentModel)) {
//				FileBean fileBean= eSignTemplateService.processTemplate(referralUserBean);
//				String documentName = fileBean.getFileTitle();
//				documentModel = DocumentModel.builder().name(documentName).build();
//				idCardByteArray = Base64.getDecoder().decode(fileBean.getFileContent());
//			}else {
//				documentModel = documentDao.getById(eSignDocumentModel.getDocumentId());
//				idCardByteArray = FileCloudUtil.downloadFile(documentModel.getBucketName(), documentModel.getName());
//			}

			documentModel = documentDao.getById(eSignDocumentModel.getDocumentId());
			idCardByteArray = FileCloudUtil.downloadFile(documentModel.getBucketName(), documentModel.getName());
			
			PdfReader reader = new PdfReader(idCardByteArray);
			int noOfPages =reader.getNumberOfPages();
			log.info("noOfPages===>" + noOfPages);
			String pagePossitionString =null;
			if (AppConstant.USER_TYPE_ADMIN.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
				pagePossitionString = Util.getPossitionJson(noOfPages, surePassSign2XCoOrdinate, surePassSign2YCoOrdinate);
			}else {
				pagePossitionString = Util.getPossitionJson(noOfPages, surePassSign1XCoOrdinate, surePassSign1YCoOrdinate);	
			}
			
			String jsonString = "{" 
								+ "    \"pdf_pre_uploaded\": true," 
								+ "    \"config\": {"
								+ "    \"name_match\":{\"match\": true,\"score\": \"60\"},"
								+ "        \"auth_mode\": \"1\","
								+ "        \"reason\": \"Contract\"," 
								+ "        \"positions\": "
								+ pagePossitionString
								+ "    }"
								+profileJsonString
								+ "}";
			log.info("/api/v1/esign/initialize=============jsonString===>" + jsonString);
			RequestBody body = RequestBody.create(mediaType, jsonString);
			String requestUrl = baseUrl + "/api/v1/esign/initialize";
//			log.info("requestUrl===>" + requestUrl);
			Request request = new Request.Builder().url(requestUrl).method("POST", body)
					.addHeader("Content-Type", "application/json")
					.addHeader("Authorization", "Bearer " + authorizationToken).build();
			Response response = client.newCall(request).execute();
			Gson gson;
			if (response.isSuccessful()) {
				String responseString = response.body().source().readByteString().utf8();
				log.info("initialize::responseString===>" + responseString);

				// Creating a Gson Object
				gson = new Gson();
				EsignInitializeData esignInitializeData = gson.fromJson(responseString, EsignInitializeData.class);
//				log.info("esignInitializeData===>" + esignInitializeData);

				String clientId = esignInitializeData.getData().getClient_id();
				eSigningResponseBean.setClientId(clientId);
				eSigningResponseBean.setUrl(esignInitializeData.getData().getUrl());
				eSigningResponseBean.setToken(esignInitializeData.getData().getToken());
				
				eSignSurepassClientidService.storeSurepassClientId(referralUserBean.getId(),esignInitializeData.getData().getClient_id(),
						"eSigning-initialize", "Success", "");
				
				// Get Upload Link
				requestUrl = baseUrl + "/api/v1/esign/get-upload-link";
				client = new OkHttpClient().newBuilder().build();
				mediaType = okhttp3.MediaType.parse("application/json");
				jsonString = "{\"client_id\":\"" + clientId + "\"}";
				log.info("/api/v1/esign/get-upload-link==========>jsonString===>" + jsonString);
				body = RequestBody.create(mediaType, jsonString);
//				log.info("requestUrlk==========>===>" + requestUrl);
				request = new Request.Builder().url(requestUrl).method("POST", body)
						.addHeader("Content-Type", "application/json")
						.addHeader("Authorization", "Bearer " + authorizationToken).build();
				
				response = client.newCall(request).execute();

//						String uploadUrl = esignInitializeData.getData().getUrl();
				if (response.isSuccessful()) {
					responseString = response.body().source().readByteString().utf8();
					log.info("esignInitializeData::responseString===>" + responseString);
				   gson = new Gson();
					GetUploadLinkResponse getUploadLinkResponse = gson.fromJson(responseString,
							GetUploadLinkResponse.class);
					eSignSurepassClientidService.storeSurepassClientId(referralUserBean.getId(),clientId,
							"eSigningGet-upload-link", "Success", "");
					
					client = new OkHttpClient().newBuilder().build();
//					mediaType = okhttp3.MediaType.parse("text/plain");
					Fields fields= getUploadLinkResponse.getData().getFields();
					body = new MultipartBody.Builder().setType(MultipartBody.FORM)
							.addFormDataPart("x-amz-signature",
									fields.getxAmzSignature())
							.addFormDataPart("x-amz-date", fields.getxAmzDate())
							.addFormDataPart("x-amz-credential",fields.getxAmzCredential())
							.addFormDataPart("key", fields.getKey())
							.addFormDataPart("policy",fields.getPolicy())
							.addFormDataPart("x-amz-algorithm", fields.getxAmzAlgorithm()).addFormDataPart("file",
									documentModel.getName(), RequestBody.create(
											okhttp3.MediaType.parse("application/octet-stream"), idCardByteArray))
							.build();
					request = new Request.Builder().url(getUploadLinkResponse.getData().getUrl()).method("POST", body)
							.build();
					response = client.newCall(request).execute();
					if (response.isSuccessful()) {
						log.info("Upload Agreement responseString===>" + responseString);
//						CaseClientIdModel model = null;
//						if (AppConstant.USER_TYPE_END_USER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//							 model = CaseClientIdModel.builder().caseId(caseId).clientId1(clientId).build();
//						}else {
//							 model = CaseClientIdModel.builder().caseId(caseId).clientId2(clientId).build();
//						}
//						caseClientIdDao.saveOrUpdate(model);
						eSignSurepassClientidService.storeSurepassClientId(referralUserBean.getId(),clientId,
								"eSigningGet-upload-Agreement", "Sucess", "");
					}else  {
						responseString = response.body().source().readByteString().utf8();
						log.info("Upload Agreement responseString===>" + responseString);
						List<ValidationError> errorList = new ArrayList<>();
						errorList.add(ValidationError.builder().message(response.message()).build());
						eSignSurepassClientidService.storeSurepassClientId(referralUserBean.getId(),clientId,
								"eSigningGet-upload-Agreement", "Failure", response.message());
						if (ArgumentHelper.isNotEmpty(errorList)) {
							throw new ValidationException(errorList);
						}
					}
				} else {
					responseString = response.body().source().readByteString().utf8();
					log.info("esignInitializeData ::responseString===>" + responseString);
					 gson = new Gson();
						GetUploadLinkResponse getUploadLinkResponse = gson.fromJson(responseString,
								GetUploadLinkResponse.class);
						String msg = Objects.nonNull(getUploadLinkResponse.getMessage()) ?"": getUploadLinkResponse.getMessage();
						eSignSurepassClientidService.storeSurepassClientId(referralUserBean.getId(),clientId,
								"eSigningGet-upload-link", "Faulure", msg);
					List<ValidationError> errorList = new ArrayList<>();
					errorList.add(ValidationError.builder().message(msg).build());

					if (ArgumentHelper.isNotEmpty(errorList)) {
						throw new ValidationException(errorList);
					}
				}

			} else {
				String responseString = response.body().source().readByteString().utf8();
				log.info("initialize::responseString===>" + responseString);
				gson = new Gson();
				EsignInitializeData esignInitializeData = gson.fromJson(responseString, EsignInitializeData.class);
				String msg = Objects.nonNull(esignInitializeData.getMessage()) ?"": esignInitializeData.getMessage();
				List<ValidationError> errorList = new ArrayList<>();
				errorList.add(ValidationError.builder().message(msg).build());
				eSignSurepassClientidService.storeSurepassClientId(referralUserBean.getId(),esignInitializeData.getData().getClient_id(),
						"eSigning-initialize", "Faulure", msg);
				if (ArgumentHelper.isNotEmpty(errorList)) {
					throw new ValidationException(errorList);
				}
			}
		}catch(ValidationException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return eSigningResponseBean;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void saveCustomerAgreement(GetDocumentRequestBean getDocumentRequestBean, UserLoginBean userLoginBean) {
		try {
			UserLoginBean referralUserBean = userLoginBean;
			
			// if Document exist then details from referralUserId
			if(Objects.nonNull(getDocumentRequestBean.getReferralUserId()) && getDocumentRequestBean.getReferralUserId()>0) {
				referralUserBean = loginService.getUserLogin(getDocumentRequestBean.getReferralUserId());
			}
			
//			if (Objects.nonNull(getDocumentRequestBean.getReferralUserId()) && getDocumentRequestBean.getReferralUserId()>0 &&
//					AppConstant.USER_TYPE_ADMIN.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())){
//				referralUserBean = loginService.getUserLogin(getDocumentRequestBean.getReferralUserId());
//			}
			
			//get the eSign Document Id
			ESignDocumentModel eSignDocumentModel =  eSignDocumentRepo.getByUserIdAndType(referralUserBean.getId(),
					referralUserBean.getApplicationUserBean().getUserType());
			
			String baseUrl = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.SURE_PASS_URL);
			String authorizationToken = ApplicationPropertiesService
					.getPropertyStringValue(PropertyKeys.SURE_PASS_AUTH_TOKEN);
			Gson gson = new Gson();
			OkHttpClient client = new OkHttpClient().newBuilder().build();
//			String requestUrl = baseUrl + "/api/v1/esign/status/";
			String requestUrl = baseUrl + "/api/v1/esign/report/";
			Request request;
			Response response;
			log.info("Verify Aadhar Name Match:::::::requestUrl===>" + requestUrl);
			okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
			String requestString = "{\r\n" + 
					"    \"categories\": [\"name_match\"]\r\n" + 
					"}";
			log.info("Verify Aadhar Name Match:::::::requestString===>" + requestString);
			RequestBody body = RequestBody.create(mediaType, requestString);
			request = new Request.Builder().url(requestUrl+getDocumentRequestBean.getClientId()).method("POST", body)
					.addHeader("Authorization", "Bearer " + authorizationToken).build();
			response = client.newCall(request).execute();
			if (response.isSuccessful()) {
				String responseString = response.body().source().readByteString().utf8();
				log.info("Verify Aadhar Name Match:::::::responseString===>" + responseString);
				AadharSignResponse aadharSignResponse = gson.fromJson(responseString, AadharSignResponse.class);
				eSignSurepassClientidService.storeSurepassClientId(referralUserBean.getId(),getDocumentRequestBean.getClientId(),
						"eSigningGet-Get Status", "Success", response.message());
				NameMatch nameMatch =aadharSignResponse.getData().getReports().getName_match();
				if(nameMatch.isShould_name_match() && Objects.nonNull(nameMatch.isName_matched()) &&  !nameMatch.isName_matched()) {
					List<ValidationError> errorList = new ArrayList<>();
					errorList.add(ValidationError.builder().message("User's provided name and name on the Aadhar not match. Please try eSigning with valid aadhar.").build());
					if (ArgumentHelper.isNotEmpty(errorList)) {
						throw new ValidationException(errorList);
					}
				}
			}else {
				String responseString = response.body().source().readByteString().utf8();
				log.info("Verify Aadhar Name Match:::::::responseString===>" + responseString);
				AadharSignResponse aadharSignResponse = gson.fromJson(responseString, AadharSignResponse.class);
				List<ValidationError> errorList = new ArrayList<>();
				errorList.add(ValidationError.builder().message(aadharSignResponse.getMessage()).build());
				eSignSurepassClientidService.storeSurepassClientId(referralUserBean.getId(),getDocumentRequestBean.getClientId(),
						"eSigningGet-Get Status", "Failure", aadharSignResponse.getMessage());
				if (ArgumentHelper.isNotEmpty(errorList)) {
					throw new ValidationException(errorList);
				}
			}
			requestUrl = baseUrl + "/api/v1/esign/get-signed-document/";
			request = new Request.Builder().url(requestUrl+getDocumentRequestBean.getClientId()).method("GET", null)
					.addHeader("Authorization", "Bearer " + authorizationToken).build();
			response = client.newCall(request).execute();
			if (response.isSuccessful()) {
				String responseString = response.body().source().readByteString().utf8();
				log.info("saveCustomerAgreement:::::::responseString===>" + responseString);
				gson = new Gson();
				GetDocumentResponse getUploadLinkResponse = gson.fromJson(responseString,
						GetDocumentResponse.class);
				log.info("getUploadLinkResponse.getData().getUrl()===>" + getUploadLinkResponse.getData().getUrl());
				eSignSurepassClientidService.storeSurepassClientId(referralUserBean.getId(),getDocumentRequestBean.getClientId(),
						"eSigning-get-signed-document", "Success", "");
				
				
				URL toDownload = new URL(getUploadLinkResponse.getData().getUrl());
				byte[] idCardByteArray = downloadFromUrl(toDownload);
				String content64Bit  =Base64.getEncoder().encodeToString(idCardByteArray);
				
//				 Path path = Paths.get("E:\\ShareProject\\testAgreement.pdf");
//			        java.nio.file.Files.write(path, idCardByteArray);
				//get the document Id
				DocumentModel documentModel = documentDao.getById(eSignDocumentModel.getDocumentId());
				FileBean agreementFile = FileBean.builder().build();
				agreementFile.setFileTitle(documentModel.getName());
				agreementFile.setFileContentType(AppConstant.JAVA_SCRIPT_DATA_PDF_BASE64);
				agreementFile.setFileContent(content64Bit);
				String bucketName = ApplicationPropertiesService
				.getPropertyStringValue(PropertyKeys.AZ_S3_BUCKET_NAME);
				AppConstant.DocumentType documentType =AppConstant.DocumentType.ReferralAgreement;
				if (AppConstant.USER_TYPE_FRANCHISE.equalsIgnoreCase(referralUserBean.getApplicationUserBean().getUserType())
						|| AppConstant.USER_TYPE_FRANCHISE_USER.equalsIgnoreCase(referralUserBean.getApplicationUserBean().getUserType())) {
					documentType =AppConstant.DocumentType.FranchiseReferralAgreement;
				}
				documentModel = DocumentModel.builder().name(documentModel.getName())
						.createdDate(LocalDateTime.now()).createBy(referralUserBean.getId())
						.type(documentType.name())
						.contentType(AppConstant.JAVA_SCRIPT_DATA_PDF_BASE64).bucketName(bucketName).build();
				//update the old id;
				Long oldDocId = eSignDocumentModel.getDocumentId();
				eSignDocumentRepo.deleteByDocumentId(oldDocId);
				documentDao.deleteById(oldDocId);
				Long newAgrmentDcoumentId = documentDao.add(documentModel);
				documentModel.setId(newAgrmentDcoumentId);
				eSignDocumentRepo.add(ESignDocumentModel.builder().userId(referralUserBean.getId()).documentId(newAgrmentDcoumentId).build());
				if (AppConstant.USER_TYPE_ADMIN.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
					//Update eSign Status referralscommissiondtl.eSignAgreementStatus
					//Commented as verifyAdmin esigning flag not yet updated.

					if (AppConstant.USER_TYPE_REFERRAL_PARTNER.equalsIgnoreCase(referralUserBean.getApplicationUserBean().getUserType())){
						referralsCommisionDtlRepo.updateESignAgreementStatusForReferralUser(AppConstant.FRANCHISE_ESIGN_AGREEMENT_STATUS_COMPLETED, referralUserBean.getId());
					}else {
						referralsCommisionDtlRepo.updateESignAgreementStatusForReferralFranchise(AppConstant.FRANCHISE_ESIGN_AGREEMENT_STATUS_COMPLETED,
								referralUserBean.getApplicationUserBean().getFranchiseId());
					}
					/*
					 * notification for eAdhar complete by Admin SMS to end user.
					 */
					String website = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.WEBSITE);
					String smsText= MessageTemplateService.getMessage("EADHAR_COMPLETE_SMS"); 
					ApplicationUserBean applicationUserBean = referralUserBean.getApplicationUserBean();
					if(Objects.isNull(applicationUserBean)) {
						applicationUserBean = applicationUserService.getApplicationUserById(referralUserBean.getApplicationUserId());
					}
					//TODO SEnd SMS 
					UserLoginBean userLoginBeanobj = loginService.getUserLoginApplicationUserId(applicationUserBean.getId());
					loginService.sendAdmineSignCompletedSMS(applicationUserBean.getFirstName(), userLoginBeanobj.getMobileNo(), applicationUserBean.getNonCustomerPin());
					sendEmail(userLoginBeanobj, newAgrmentDcoumentId);
				}else {
					//TODO Update eSign Status referralscommissiondtl.eSignAgreementStatus
					if (AppConstant.USER_TYPE_REFERRAL_PARTNER.equalsIgnoreCase(referralUserBean.getApplicationUserBean().getUserType())){
						referralsCommisionDtlRepo.updateESignAgreementStatusForReferralUser(AppConstant.FRANCHISE_ESIGN_AGREEMENT_STATUS_PENDING_FOR_ADMIN, referralUserBean.getId());
					}else {
						referralsCommisionDtlRepo.updateESignAgreementStatusForReferralFranchise(AppConstant.FRANCHISE_ESIGN_AGREEMENT_STATUS_PENDING_FOR_ADMIN,
								referralUserBean.getApplicationUserBean().getFranchiseId());
					}
					/*
					 * notification for eAdhar complete by customer SMS
					 */
					String website = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.WEBSITE);
					String smsText= MessageTemplateService.getMessage("EADHAR_COMPLETE_ADMIN_SMS"); 
					UserLoginBean userLoginBeanObj = loginService.getUserLoginApplicationUserId(AppConstant.SYS_USER_ID);
					//TODO SEnd SMS 
					loginService.sendReferralFranchiseUsereSignCompletedSMS(userLoginBeanObj.getApplicationUserBean().getFirstName(), userLoginBeanObj.getMobileNo(), "", referralUserBean.getApplicationUserBean().getUserType());
				}
			
				caseTemplateService.saveFileToAS3(agreementFile,bucketName);
				
			} else {
				List<ValidationError> errorList = new ArrayList<>();
				errorList.add(ValidationError.builder().message(response.message()).build());
				eSignSurepassClientidService.storeSurepassClientId(userLoginBean.getId(),getDocumentRequestBean.getClientId(),
						"eSigning-get-signed-document", "Failure", "");
				if (ArgumentHelper.isNotEmpty(errorList)) {
					throw new ValidationException(errorList);
				}
			}
		}catch(ValidationException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void sendEmail(UserLoginBean userLoginBeanObj,Long aggrmentDcoumentId) {
		String eMailBoday = "";
		String website = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.WEBSITE);//"http://www.findmymoney.in";
		//smsText= MessageTemplateService.getMessage("RTA_LETTER_GENERATED_SMS");//AppConstant.APP_RESET_SMS_TEXT; //MessageTemplateService.getMessage("APP_RESET_SMS_TEXT"); // VALIDATION_SUBMISSION_SMS
		String subject = AppConstant.E_SIGN_EMAIL_SUBJECT;
		eMailBoday = AppConstant.E_SINGN_EMAIL;
		eMailBoday = MessageFormat.format(eMailBoday, userLoginBeanObj.getApplicationUserBean().getFirstName(),website);

		List<Long> eSignDocIds = new ArrayList<Long>(1);
		eSignDocIds.add(aggrmentDcoumentId);
		
		List<String>  emailCcIds = new ArrayList<String>(1);	
		
//		ApplicationUserBean applicationAdminUserBean = applicationUserService.getApplicationUserById(AppConstant.SYS_USER_ID); // here assume Sys user Id is Admin. 
//		String ccEmailAdminAddress = applicationAdminUserBean.getEmailId();
//		if (!Objects.isNull(ccEmailAdminAddress) && ccEmailAdminAddress.length()>0) 
//		{ 
//			emailCcIds.add(ccEmailAdminAddress);
//		}
		
		emailService.sendEmail(userLoginBeanObj.getApplicationUserBean(), userLoginBeanObj, eMailBoday, subject, eSignDocIds,emailCcIds);
		
	}
	private byte[] downloadFromUrl(URL toDownload) {
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

	    try {
	        byte[] chunk = new byte[4096];
	        int bytesRead;
	        InputStream stream = toDownload.openStream();

	        while ((bytesRead = stream.read(chunk)) > 0) {
	            outputStream.write(chunk, 0, bytesRead);
	        }

	    } catch (IOException e) {
	    	throw new ServiceException(e);
	    }

	    return outputStream.toByteArray();
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void generateCustomerAgreement(UserLoginBean userLoginBean) {
		ESignDocumentModel eSignDocumentModel = eSignDocumentRepo.getByUserIdAndType(userLoginBean.getId(),
				userLoginBean.getApplicationUserBean().getUserType());
		if (Objects.isNull(eSignDocumentModel)) {
			eSignTemplateService.processTemplate(userLoginBean);
		}
	}
	
	public ReferralsCommisionDtlBean getReferralPartnerCommisionDtlforGivenAppUserId(Long userId) {
		ReferralsCommisionDtlBean bean = ReferralsCommisionDtlConverter.convert(referralsCommisionDtlRepo.getReferralPartnerCommisionDtlforGivenAppUserId(userId));
		UserLoginBean 	userLoginBean =	loginService.getUserLogin(userId);
		ESignDocumentModel eSignDocumentModel = eSignDocumentRepo.getByUserIdAndType(userLoginBean.getId(),
				userLoginBean.getApplicationUserBean().getUserType());
		if (Objects.nonNull(eSignDocumentModel)) {
			bean.setDocumentId(eSignDocumentModel.getDocumentId());
		}
		return bean;
	} 
	
	public ReferralsCommisionDtlBean getReferralPartnerCommisionDtlforGivenFranchiseId(Long franchiseId) {
		return  ReferralsCommisionDtlConverter.convert(referralsCommisionDtlRepo.getReferralPartnerCommisionDtlforGivenFranchiseId(franchiseId));
	}
	public ReferralsCommisionDtlBean getReferralPartnerCommisionDtlforGivenFranchiseUserId(Long franchiseUserId) {
		return  ReferralsCommisionDtlConverter.convert(referralsCommisionDtlRepo.getReferralPartnerCommisionDtlforGivenFranchiseUserId(franchiseUserId));
	}
	
}
