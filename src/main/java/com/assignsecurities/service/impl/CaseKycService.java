package com.assignsecurities.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
import com.assignsecurities.bean.AddressBean;
import com.assignsecurities.bean.ApplicationUserBean;
import com.assignsecurities.bean.CaseAadharBean;
import com.assignsecurities.bean.CaseAadharGenerateOtpBean;
import com.assignsecurities.bean.CasePanBean;
import com.assignsecurities.bean.CasePanVerifyBean;
import com.assignsecurities.bean.ESigningResponseBean;
import com.assignsecurities.bean.FileBean;
import com.assignsecurities.bean.GetDocumentRequestBean;
import com.assignsecurities.bean.KycOtpRequestBean;
import com.assignsecurities.bean.KycResponseBean;
import com.assignsecurities.bean.PersonName;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.bean.ValidationError;
import com.assignsecurities.bean.surepass.AadharData;
import com.assignsecurities.bean.surepass.AadharData.OcrField;
import com.assignsecurities.bean.surepass.AadharSignResponse;
import com.assignsecurities.bean.surepass.AadharSignResponse.NameMatch;
import com.assignsecurities.bean.surepass.EsignInitializeData;
import com.assignsecurities.bean.surepass.FinalAadharData;
import com.assignsecurities.bean.surepass.FinalAadharData.Address;
import com.assignsecurities.bean.surepass.FinalPanData;
import com.assignsecurities.bean.surepass.GetDocumentResponse;
import com.assignsecurities.bean.surepass.GetUploadLinkResponse;
import com.assignsecurities.bean.surepass.GetUploadLinkResponse.Fields;
import com.assignsecurities.bean.surepass.OtpResponse;
import com.assignsecurities.dm.PropertyKeys;
import com.assignsecurities.domain.CaseClientIdModel;
import com.assignsecurities.domain.CaseDocumentModel;
import com.assignsecurities.domain.CaseLogModel;
import com.assignsecurities.domain.CaseModel;
import com.assignsecurities.domain.DocumentModel;
import com.assignsecurities.repository.impl.CaseClientIdDao;
import com.assignsecurities.repository.impl.CaseDocumentDao;
import com.assignsecurities.repository.impl.CaseGeneratedDocumentsRequiredRepo;
import com.assignsecurities.repository.impl.CaseLogRepo;
import com.assignsecurities.repository.impl.CaseRepo;
import com.assignsecurities.repository.impl.DocumentDao;
import com.assignsecurities.repository.impl.EmailReposatory;
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
public class CaseKycService {
	@Autowired
	private CaseService caseService;
	
	@Autowired
	private CaseRepo caseRepo;

	@Autowired
	private DocumentDao documentDao;
	
	@Autowired
	private CaseDocumentDao caseDocumentDao;
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private CaseTemplateService caseTemplateService;
	
	@Autowired
	private CaseClientIdDao caseClientIdDao;
	
	@Autowired
	private CaseGeneratedDocumentsRequiredRepo caseGeneratedDocumentsRequiredRepo;
	
	@Autowired
	private CaseSurepassClientidService caseSurepassClientidService;
	
	@Autowired
	private CaseLogRepo caseLogRepo; 
	
	@Autowired
	private ApplicationUserService applicationUserService;
	
	@Autowired 
	private EmailReposatory emailReposatory;
	
	@Autowired
	private EmailService emailService;

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public KycResponseBean generateOtp(CaseAadharGenerateOtpBean aadharGenerateOtpBean, UserLoginBean userLoginBean) {
		if (Objects.isNull(aadharGenerateOtpBean) || Objects.isNull(aadharGenerateOtpBean.getCaseId())
				|| aadharGenerateOtpBean.getCaseId() <= 0) {
			log.error("CaseAadharGenerateOtpBean===>" + aadharGenerateOtpBean);
			String msg = "Invalid Request.... Please try after some time.";
			List<ValidationError> errorList = new ArrayList<>();
			errorList.add(ValidationError.builder().message(msg).build());
			if (ArgumentHelper.isNotEmpty(errorList)) {
				throw new ValidationException(errorList);
			}
		}
		String baseUrl = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.SURE_PASS_URL);
		KycResponseBean kycResponseBean = KycResponseBean.builder().build();
		String authorizationToken = ApplicationPropertiesService
				.getPropertyStringValue(PropertyKeys.SURE_PASS_AUTH_TOKEN);
		boolean isIntegrationRequired = ApplicationPropertiesService
				.getPropertyBooleanValue(PropertyKeys.SURE_PASS_INTEGRATION_REQUIRED);
		try {
			FileBean aadharFrontImage = aadharGenerateOtpBean.getAadharFrontImage();
			byte[] idCardByteArray = Base64.getDecoder().decode(aadharFrontImage.getFileContent().getBytes());
			if(isIntegrationRequired) {
				OkHttpClient client = new OkHttpClient().newBuilder().build();
				String requestUrl = baseUrl + "/api/v1/ocr/aadhaar";
				log.info("requestUrl===>" + requestUrl);
				RequestBody reqBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
						.addFormDataPart("file", "file",
								RequestBody.create(okhttp3.MediaType.parse("application/octet-stream"), idCardByteArray))
						.build();
				Request request = new Request.Builder().url(requestUrl).method("POST", reqBody)
						.addHeader("Authorization", "Bearer " + authorizationToken)
						.addHeader("accept", MediaType.APPLICATION_JSON_VALUE).build();
				Response response = client.newCall(request).execute();
				Gson gson = new Gson();
				AadharData aadharData;
				String clientId;
				if (response.isSuccessful()) {
					String responseString = response.body().source().readByteString().utf8();
					log.info("ocr/aadhaar::responseString===>" + responseString);
	
					// Creating a Gson Object
					aadharData = gson.fromJson(responseString, AadharData.class);
					log.info("aadharData===>" + aadharData);
					String aaDharNumber = null;
					for (OcrField field : aadharData.getData().getOcr_fields()) {
						aaDharNumber = field.getAadhaar_number().getValue();
						if(ArgumentHelper.isValid(aaDharNumber)) {
							break;
						}
					}
					caseSurepassClientidService.storeSurepassClientId(aadharGenerateOtpBean.getCaseId(), aadharData.getData().getClient_id(),
							"AadharOcr", "Success", "");
					clientId = generateOptFromAadharNumber(aaDharNumber);
					caseSurepassClientidService.storeSurepassClientId(aadharGenerateOtpBean.getCaseId(), clientId,
							"AadharOptGeneration", "Success", "");
					kycResponseBean.setClientId(clientId);
					caseService.saveAadharImage(aadharGenerateOtpBean.getCaseId(), aadharFrontImage, userLoginBean);
				} else {
					String responseString = response.body().source().readByteString().utf8();
					log.info("ocr/aadhaar::responseString===>" + responseString);
					aadharData = gson.fromJson(responseString, AadharData.class);
					clientId = aadharData.getData().getClient_id();
					String msg = Objects.isNull(aadharData.getMessage()) ?"": aadharData.getMessage().toString();
					caseSurepassClientidService.storeSurepassClientId(aadharGenerateOtpBean.getCaseId(), clientId,
							"AadharOcr", "Faulure", msg);
					List<ValidationError> errorList = new ArrayList<>();
					errorList.add(ValidationError.builder().message(msg).build());
					if (ArgumentHelper.isNotEmpty(errorList)) {
						throw new ValidationException(errorList);
					}
				}
			}else {
				caseService.saveAadharImage(aadharGenerateOtpBean.getCaseId(), aadharFrontImage, userLoginBean);
				caseLogRepo.add(CaseLogModel.builder().caseId(aadharGenerateOtpBean.getCaseId())
						.action("Aadhar Varification Not Required").createBy(userLoginBean.getDisplayName()).build());
			}
			kycResponseBean.setOtpRequired(isIntegrationRequired);
		}catch(ValidationException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return kycResponseBean;
	}

	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void submitAadharOtp(KycOtpRequestBean kycOtpRequestBean, UserLoginBean userLoginBean) {
		try {
			boolean isIntegrationRequired = ApplicationPropertiesService
					.getPropertyBooleanValue(PropertyKeys.SURE_PASS_INTEGRATION_REQUIRED);
			if(!isIntegrationRequired) {
				return;
			}
			
			HttpHeaders headers = new HttpHeaders();
			String requestUrl = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.SURE_PASS_URL);
			requestUrl = requestUrl + "/api/v1/aadhaar-v2/submit-otp";
			log.info("requestUrl===>" + requestUrl);
			String authorizationToken = ApplicationPropertiesService
					.getPropertyStringValue(PropertyKeys.SURE_PASS_AUTH_TOKEN);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("Authorization", "Bearer " + authorizationToken);
	
//			MultiValueMap<String, String> postParams = new LinkedMultiValueMap<String, String>();
//			postParams.add("client_id", kycOtpRequestBean.getClientId());
//			postParams.add("otp", kycOtpRequestBean.getOtp());
	//		log.info("Post parameters are" + postParams);
	//		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(
	//				postParams, headers);
	//		RestTemplate restTemplate = new RestTemplate();
	//		ResponseEntity<String> result = restTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity, String.class);
			OkHttpClient client = new OkHttpClient().newBuilder()
					  .build();
			okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
//					RequestBody reqBody = RequestBody.create(mediaType, "{\"client_id\": \"{{"+kycOtpRequestBean.getClientId()+"}}\",\"otp\": \"{{"+kycOtpRequestBean.getOtp()+"}}\"}");
			String requestString = "{\n\t\"client_id\": \""+kycOtpRequestBean.getClientId()+"\",\n\t\"otp\": \""+kycOtpRequestBean.getOtp()+"\"\n}";
//			requestString.replaceAll("client_idData", kycOtpRequestBean.getClientId());
//			requestString.replaceAll("otpData", kycOtpRequestBean.getOtp());
			log.info("generateOtp::requestString===>" + requestString);
			RequestBody body = RequestBody.create(mediaType, requestString);
					Request request = new Request.Builder()
					  .url(requestUrl)
					  .method("POST", body)
					  .addHeader("Content-Type", "application/json")
					  .addHeader("Authorization", "Bearer " + authorizationToken)
					  .build();
	//				Response response = client.newCall(request).execute();
//					Request request = new Request.Builder().url(requestUrl).method("POST", body)
//							.addHeader("Authorization", "Bearer " + authorizationToken)
//							.addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE).build();
					Response response = client.newCall(request).execute();
					
			Gson gson = new Gson();
			String clientId;
			if (response.isSuccessful()) {
	//			log.info("result.getBody()" + result.getBody());
				String responseString = response.body().source().readByteString().utf8();
				log.info("generateOtp::responseString===>" + responseString);
				// Creating a Gson Object
				FinalAadharData otpResponse = gson.fromJson(responseString, FinalAadharData.class);
				log.info("FinalAadharData" + otpResponse);
				clientId = otpResponse.getData().getClient_id();
				caseSurepassClientidService.storeSurepassClientId(kycOtpRequestBean.getCaseId(), clientId,
						"AadharOcrOptVirificationReq", "Success", "");
				CaseAadharBean caseAadharBean = CaseAadharBean.builder().build();
				caseAadharBean.setId(kycOtpRequestBean.getCaseId());
				caseAadharBean.setAadharNumber(otpResponse.getData().getAadhaar_number());
				PersonName personName = PersonNameUtil.splitFullName(otpResponse.getData().getFull_name());
				caseAadharBean.setAadharFirstName(personName.getFirstName());
				caseAadharBean.setAadharMiddleName(personName.getMiddleName());
				caseAadharBean.setAadharLastName(personName.getLastName());
				AddressBean address = AddressBean.builder().build();
				Address aadharAddress = otpResponse.getData().getAddress();
				String generateAddress = Objects.isNull(aadharAddress.getHouse()) ? "" : aadharAddress.getHouse();
				generateAddress = generateAddress
						+ (Objects.isNull(aadharAddress.getLandmark()) ? "" : " " + aadharAddress.getLandmark());
				generateAddress = generateAddress
						+ (Objects.isNull(aadharAddress.getStreet()) ? "" : " " + aadharAddress.getStreet());
				generateAddress = generateAddress
						+ (Objects.isNull(aadharAddress.getLoc()) ? "" : " " + aadharAddress.getLoc());
				generateAddress = generateAddress
						+ (Objects.isNull(aadharAddress.getPo()) ? "" : " Post Office :" + aadharAddress.getPo());
	
				address.setAddress(generateAddress);
				address.setCity(aadharAddress.getVtc());
				address.setPinCode(otpResponse.getData().getZip());
				address.setState(aadharAddress.getState());
				address.setCountry(aadharAddress.getCountry());
				caseAadharBean.setDateOfBirth(
						LocalDate.parse(otpResponse.getData().getDob(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
				caseAadharBean.setGender(otpResponse.getData().getGender());
				caseAadharBean.setAddress(address);
				caseService.saveAadharDetails(caseAadharBean, userLoginBean);
				caseLogRepo.add(CaseLogModel.builder().caseId(kycOtpRequestBean.getCaseId())
						.action("Aadhar Varification Done").createBy(userLoginBean.getDisplayName()).build());
			} else {
				List<ValidationError> errorList = new ArrayList<>();
				String responseString = response.body().source().readByteString().utf8();
				log.info("generateOtp::responseString===>" + responseString);
				// Creating a Gson Object
				FinalAadharData otpResponse = gson.fromJson(responseString, FinalAadharData.class);
				String msg = Objects.isNull(otpResponse.getMessage()) ?"": otpResponse.getMessage().toString();
				caseSurepassClientidService.storeSurepassClientId(kycOtpRequestBean.getCaseId(),kycOtpRequestBean.getClientId(),
						"AadharOcrOptVirification", "Faulure", msg);
				errorList.add(ValidationError.builder().message(msg).build());
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
	public String generateOptFromAadharNumber(String aaDharNumber) {
		try {
			String requestUrl = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.SURE_PASS_URL);
			String authorizationToken = ApplicationPropertiesService
					.getPropertyStringValue(PropertyKeys.SURE_PASS_AUTH_TOKEN);
			requestUrl = requestUrl + "/api/v1/aadhaar-v2/generate-otp";
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
			String requestString = "{\"id_number\": \"" + aaDharNumber + "\"}";
			log.info("requestString===>" + requestString);
			RequestBody body = RequestBody.create(mediaType, requestString);
			Request request = new Request.Builder().url(requestUrl).method("POST", body)
					.addHeader("Content-Type", "application/json")
					.addHeader("Authorization", "Bearer " + authorizationToken).build();
			log.info("requestUrl===>" + requestUrl);
			Response response = client.newCall(request).execute();
			Gson gson = new Gson();
			if (response.isSuccessful()) {
				String responseString = response.body().source().readByteString().utf8();
				log.info("responseString===>" + responseString);
				
				OtpResponse otpResponse = gson.fromJson(responseString, OtpResponse.class);
				return otpResponse.getData().getClient_id();
			}else {
				String responseString = response.body().source().readByteString().utf8();
				log.info("responseString===>" + responseString);
				List<ValidationError> errorList = new ArrayList<>();
				OtpResponse otpResponse = gson.fromJson(responseString, OtpResponse.class);
				String msg = Objects.isNull(otpResponse.getMessage()) ?"": otpResponse.getMessage();
				if(otpResponse.getMessage().contains("None or invalid mobile number")) {
					errorList.add(ValidationError.builder().message(msg).inputType("NoMobileLink").build());
				}else {
					errorList.add(ValidationError.builder().message(msg).build());
				}

				if (ArgumentHelper.isNotEmpty(errorList)) {
					throw new ValidationException(errorList);
				}
			}
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		return null;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void panVerify(CasePanVerifyBean casePanVerifyBean, UserLoginBean userLoginBean) {
		String baseUrl = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.SURE_PASS_URL);
		String authorizationToken = ApplicationPropertiesService
				.getPropertyStringValue(PropertyKeys.SURE_PASS_AUTH_TOKEN);
		boolean isIntegrationRequired = ApplicationPropertiesService
				.getPropertyBooleanValue(PropertyKeys.SURE_PASS_INTEGRATION_REQUIRED);
		try {
			if(Objects.isNull(casePanVerifyBean.getCaseId()) || "null".equalsIgnoreCase(casePanVerifyBean.getCaseId().toString())) {
				List<ValidationError> errorList = new ArrayList<>();
				String msg = "Please do the Aadhar vefrifcation First.";
				errorList.add(ValidationError.builder().message(msg).build());
				if (ArgumentHelper.isNotEmpty(errorList)) {
					throw new ValidationException(errorList);
				}	
			}
			FileBean panImage = casePanVerifyBean.getPanImage();
			byte[] idCardByteArray = Base64.getDecoder().decode(panImage.getFileContent().getBytes());
			if(isIntegrationRequired) {
				OkHttpClient client = new OkHttpClient().newBuilder().build();
				String requestUrl = baseUrl + "/api/v1/ocr/pan";
				log.info("requestUrl===>" + requestUrl);
				RequestBody reqBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
						.addFormDataPart("file", "file",
	//							RequestBody.create(okhttp3.MediaType.parse("application/octet-stream"),
	//									new File("E:\\Personal\\NC\\myAdhar2.jpg")))
								RequestBody.create(okhttp3.MediaType.parse("application/octet-stream"), idCardByteArray))
						.build();
				Request request = new Request.Builder().url(requestUrl).method("POST", reqBody)
						.addHeader("Authorization", "Bearer " + authorizationToken)
						.addHeader("accept", MediaType.APPLICATION_JSON_VALUE).build();
				Response response = client.newCall(request).execute();
				Gson gson = new Gson();
				if (response.isSuccessful()) {
					String responseString = response.body().source().readByteString().utf8();
					log.info("responseString===>" + responseString);
					// Creating a Gson Object
					FinalPanData panData = gson.fromJson(responseString, FinalPanData.class);
					log.info("panData===>" + panData);
					caseSurepassClientidService.storeSurepassClientId(casePanVerifyBean.getCaseId(),panData.getData().getClient_id(),
							"PanOcrReq", "Success", "");
					String panNumber = null;
					String fullName = null;
					String dob = null;
					for (com.assignsecurities.bean.surepass.FinalPanData.OcrField field : panData.getData()
							.getOcr_fields()) {
						panNumber = field.getPan_number().getValue();
						fullName = field.getFull_name().getValue();
						dob = field.getDob().getValue();
					}
					CaseModel caseModel = caseRepo.getById(casePanVerifyBean.getCaseId(),userLoginBean);
					fullName = PersonNameUtil.getFullName(caseModel.getFirstName(), caseModel.getMiddleName(), caseModel.getLastName());
					boolean isSuccess = panVerifyWithSurePass(casePanVerifyBean, panNumber, fullName, dob, userLoginBean,caseModel.getAadharNumber());
					CasePanBean casePanBean = CasePanBean.builder().build();
					casePanBean.setPanNumber(panNumber);
					casePanBean.setId(casePanVerifyBean.getCaseId());
					casePanBean.setPanImage(panImage);
					casePanBean.setPanVerified(isSuccess);
					caseService.savePanDetails(casePanBean, userLoginBean);
				} else {
					String responseString = response.body().source().readByteString().utf8();
					log.info("responseString===>" + responseString);
					List<ValidationError> errorList = new ArrayList<>();
					FinalPanData panData = gson.fromJson(responseString, FinalPanData.class);
					String msg = Objects.isNull(panData.getMessage()) ?"": panData.getMessage().toString();
					caseSurepassClientidService.storeSurepassClientId(casePanVerifyBean.getCaseId(),panData.getData().getClient_id(),
							"PanOcrReq", "Faulure", msg);
					errorList.add(ValidationError.builder().message(msg).build());
	
					if (ArgumentHelper.isNotEmpty(errorList)) {
						throw new ValidationException(errorList);
					}
				}
			}else {
				CasePanBean casePanBean = CasePanBean.builder().build();
				casePanBean.setId(casePanVerifyBean.getCaseId());
				casePanBean.setPanImage(panImage);
				caseService.savePanDetails(casePanBean, userLoginBean);
			}
		}catch(ValidationException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
//		return kycResponseBean;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	private boolean panVerifyWithSurePass(CasePanVerifyBean casePanVerifyBean, String panNumber, String fullName, String dob, UserLoginBean userLoginBean
			,String aadharNumber) {
//		fullName="Sanjeev P Kalal";
		try {
			String requestUrl = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.SURE_PASS_URL);
			String authorizationToken = ApplicationPropertiesService
					.getPropertyStringValue(PropertyKeys.SURE_PASS_AUTH_TOKEN);
			requestUrl = requestUrl + "/api/v1/pan/pan-verify";
//			requestUrl = requestUrl + "v1/pan/pan";
//			requestUrl = requestUrl + "/api/v1/pan/pan-aadhaar-link-check";
			log.info("requestUrl===>" + requestUrl);
			OkHttpClient client = new OkHttpClient().newBuilder().build();

			LocalDate tempDob = LocalDate.parse(dob, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

			dob = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(tempDob);

			okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
			String resuestString = "{\"id_number\": \"" + panNumber + "\", \"full_name\": \"" + fullName
					+ "\",  \"dob\": \"" + dob + "\"}";
//			resuestString = "{\"id_number\": \"" + panNumber +  "\"}";
//			resuestString = "{\"pan_number\": \"" + panNumber + "\", \"aadhaar_number\": \"" + aadharNumber + "\"}";
			
			log.info("resuestString==>" + resuestString);
			RequestBody body = RequestBody.create(mediaType, resuestString);
			log.info("Post body are" + body);
			Request request = new Request.Builder().url(requestUrl).method("POST", body)
					.addHeader("Content-Type", "application/json")
					.addHeader("Authorization", "Bearer " + authorizationToken).build();
			Response response = client.newCall(request).execute();
			String responseString = response.body().source().readByteString().utf8();
			log.info("responseString===>" + responseString);
			if (!response.isSuccessful()) {
//				List<ValidationError> errorList = new ArrayList<>();
//				errorList.add(ValidationError.builder().message("Pan verification failure, please check your pan and aadhar details.").build());
				caseSurepassClientidService.storeSurepassClientId(casePanVerifyBean.getCaseId(),"",
						"PanOcrVerify", "Failure", response.message());
//				if (ArgumentHelper.isNotEmpty(errorList)) {
//					throw new ValidationException(errorList);
//				}
				log.warn("Pan verification failure, please check your pan and aadhar details.");
				return false;
			}else {
				caseSurepassClientidService.storeSurepassClientId(casePanVerifyBean.getCaseId(),"",
						"PanOcrVerify", "Success", "");
				caseLogRepo.add(CaseLogModel.builder().caseId(casePanVerifyBean.getCaseId())
						.action("Pan Varification Done").createBy(userLoginBean.getDisplayName()).build());
				return true;
			}
		} catch (IOException e) {
			throw new ServiceException(e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public ESigningResponseBean initializeAndUploadAgreement(Long caseId,
			UserLoginBean userLoginBean) {
		CaseModel caseModel = caseRepo.getById(caseId, userLoginBean);
		boolean isIntegrationRequired = ApplicationPropertiesService
				.getPropertyBooleanValue(PropertyKeys.SURE_PASS_INTEGRATION_REQUIRED);
		ESigningResponseBean eSigningResponseBean = ESigningResponseBean.builder().caseId(caseId).build();
		if(!isIntegrationRequired) {
			caseModel.setStatus(AppConstant.CaseStatus.WaitingFranchiseAssigment.label);
			caseModel.setIseAdharComplete(Boolean.TRUE);
			caseService.update(caseModel, userLoginBean);
			caseLogRepo.add(CaseLogModel.builder().caseId(caseId)
					.action("eSigning Not Required").createBy(userLoginBean.getDisplayName()).build());
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
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
			String fullNAme = PersonNameUtil.getFullName(caseModel.getFirstName(), caseModel.getMiddleName(), caseModel.getLastName());
			UserLoginBean appUserBean = null;
			if (AppConstant.USER_TYPE_END_USER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
				appUserBean = loginService.getUserLogin(caseModel.getUserId());
			}else {
				appUserBean = userLoginBean;
				fullNAme  = PersonNameUtil.getFullName(appUserBean.getApplicationUserBean().getFirstName(), 
						appUserBean.getApplicationUserBean().getMiddleName(),
						appUserBean.getApplicationUserBean().getLastName());
			}
			String mobileNo = appUserBean.getMobileNo();
//			mobileNo = "9890960765";
			String emialId = appUserBean.getApplicationUserBean().getEmailId();
			if(Objects.isNull(emialId)) {
				List<ValidationError> errorList = new ArrayList<>();
				errorList.add(ValidationError.builder().message("Please update the email in profile to processed on this step.").build());
				if (ArgumentHelper.isNotEmpty(errorList)) {
					throw new ValidationException(errorList);
				}
			}
//					"narendra.chouhan123@gmail.com"; 
			String profileJsonString =  
					"    ,\"prefill_options\": {" + 
					"        \"full_name\": \""+fullNAme+"\"," + 
					"        \"mobile_number\": \""+mobileNo+"\"," + 
					"        \"user_email\": \""+emialId+"\"" + 
					"    }" ;
			
			DocumentModel documentModel = documentDao.getById(caseModel.getAggrmentDcoumentId());
			byte[] idCardByteArray = FileCloudUtil.downloadFile(documentModel.getBucketName(), documentModel.getName());
			PdfReader reader = new PdfReader(idCardByteArray);
			int noOfPages =reader.getNumberOfPages();
			log.info("noOfPages===>" + noOfPages);
			String pagePossitionString =null;
			if (AppConstant.USER_TYPE_END_USER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
				pagePossitionString = Util.getPossitionJson(noOfPages, surePassSign1XCoOrdinate, surePassSign1YCoOrdinate);				
			}else {
				pagePossitionString = Util.getPossitionJson(noOfPages, surePassSign2XCoOrdinate, surePassSign2YCoOrdinate);		
			}
			
			String jsonString = "{" 
								+ "    \"pdf_pre_uploaded\": true," 
								+ "    \"config\": {"
								+ "    \"name_match\":{\"match\": true,\"score\": \"60\"},"
								+ "        \"auth_mode\": \"1\","
								+ "        \"reason\": \"Contract\"," 
								+ "        \"positions\": "
								//{
//								+ "            \"1\": [" 
//								+ "                {" 
//								+ "                    \"x\": "+surePassSign1XCoOrdinate+","
//								+ "                    \"y\": "+surePassSign1YCoOrdinate+"" 
//								+ "                }" 
//								+ "            ]," 
//								+ "            \"2\": ["
//								+ "                {" 
//								+ "                    \"x\": "+surePassSign2XCoOrdinate+"," 
//								+ "                    \"y\": "+surePassSign2YCoOrdinate+""
//								+ "                }" 
//								+ "            ]" 
//								+ "        }" 
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
				
				caseSurepassClientidService.storeSurepassClientId(caseId,esignInitializeData.getData().getClient_id(),
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
					caseSurepassClientidService.storeSurepassClientId(caseId,clientId,
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
						CaseClientIdModel model = null;
						//TODO for 2nd wards client Id
						if (AppConstant.USER_TYPE_END_USER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
							 model = CaseClientIdModel.builder().caseId(caseId).clientId1(clientId).build();
						}else {
							 model = CaseClientIdModel.builder().caseId(caseId).clientId2(clientId).build();
						}
						caseClientIdDao.saveOrUpdate(model);
						caseSurepassClientidService.storeSurepassClientId(caseId,clientId,
								"eSigningGet-upload-Agreement", "Sucess", "");
					}else  {
						responseString = response.body().source().readByteString().utf8();
						log.info("Upload Agreement responseString===>" + responseString);
						List<ValidationError> errorList = new ArrayList<>();
						errorList.add(ValidationError.builder().message(response.message()).build());
						caseSurepassClientidService.storeSurepassClientId(caseId,clientId,
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
						caseSurepassClientidService.storeSurepassClientId(caseId,clientId,
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
				caseSurepassClientidService.storeSurepassClientId(caseId,esignInitializeData.getData().getClient_id(),
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
	public Long saveCustomerAgreement(GetDocumentRequestBean getDocumentRequestBean, UserLoginBean userLoginBean) {
		Long oldDocId = 0L;
		try {
			CaseModel caseModel = caseRepo.getById(getDocumentRequestBean.getCaseId(), userLoginBean);
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
				caseSurepassClientidService.storeSurepassClientId(getDocumentRequestBean.getCaseId(),getDocumentRequestBean.getClientId(),
						"eSigningGet-Get Status", "Success", response.message());
				NameMatch nameMatch =aadharSignResponse.getData().getReports().getName_match();
				if(nameMatch.isShould_name_match() && !nameMatch.isName_matched()) {
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
				caseSurepassClientidService.storeSurepassClientId(getDocumentRequestBean.getCaseId(),getDocumentRequestBean.getClientId(),
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
				caseSurepassClientidService.storeSurepassClientId(getDocumentRequestBean.getCaseId(),getDocumentRequestBean.getClientId(),
						"eSigning-get-signed-document", "Success", "");
				
				
				URL toDownload = new URL(getUploadLinkResponse.getData().getUrl());
				byte[] idCardByteArray = downloadFromUrl(toDownload);
				String content64Bit  =Base64.getEncoder().encodeToString(idCardByteArray);
				
//				 Path path = Paths.get("E:\\ShareProject\\testAgreement.pdf");
//			        java.nio.file.Files.write(path, idCardByteArray);
				
				DocumentModel documentModel = documentDao.getById(caseModel.getAggrmentDcoumentId());
				FileBean agreementFile = FileBean.builder().build();
				agreementFile.setFileTitle(documentModel.getName());
				agreementFile.setFileContentType(AppConstant.JAVA_SCRIPT_DATA_PDF_BASE64);
				agreementFile.setFileContent(content64Bit);
				String bucketName = ApplicationPropertiesService
				.getPropertyStringValue(PropertyKeys.AZ_S3_BUCKET_NAME);
				documentModel = DocumentModel.builder().name(documentModel.getName())
						.createdDate(LocalDateTime.now()).createBy(userLoginBean.getId())
						.type(AppConstant.DocumentType.CustomerAgreement.name())
						.contentType(AppConstant.JAVA_SCRIPT_DATA_PDF_BASE64).bucketName(bucketName).build();
				oldDocId = caseModel.getAggrmentDcoumentId();
				Long newAgrmentDcoumentId = documentDao.add(documentModel);
				caseModel.setAggrmentDcoumentId(newAgrmentDcoumentId);
				documentModel.setId(newAgrmentDcoumentId);
				caseService.intsertCaseDocument(caseModel.getId(), documentModel, null);
				//TODO for the 2nd ownwards status update
				if (AppConstant.USER_TYPE_END_USER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
					caseModel.setStatus(AppConstant.CaseStatus.WaitingAdminAadhar.label);
					caseLogRepo.add(CaseLogModel.builder().caseId(getDocumentRequestBean.getCaseId())
							.action("eSigning Done").createBy(userLoginBean.getDisplayName()).build());
					/*
					 * notification for eAdhar complete by customer SMS
					 */
					String website = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.WEBSITE);//"http://www.findmymoney.in";//MessageTemplateService.getMessage("WEBSITE"); //"http://www.findmymoney.in";
					String smsText= MessageTemplateService.getMessage("EADHAR_COMPLETE_ADMIN_SMS"); 
					ApplicationUserBean applicationUserBean = applicationUserService.getApplicationUserById(AppConstant.SYS_USER_ID);
					UserLoginBean userLoginBeanObj = loginService.getUserLoginApplicationUserId(applicationUserBean.getId());
					smsText = MessageFormat.format(smsText, applicationUserBean.getFirstName(), caseModel.getReferenceNumber(), website);
					caseService.sendSMSPostApplicationStatusChange(userLoginBeanObj.getMobileNo(),smsText);
				}else {
					//Commented as verifyAdmin esigning flag not yet updated.
					List<CaseDocumentModel> caseDocumentModels = caseDocumentDao
							.getByCaseIdAndDocType(getDocumentRequestBean.getCaseId(), AppConstant.DocumentType.RtaLetter1.name());
					if(ArgumentHelper.isEmpty(caseDocumentModels)) {
						caseModel.setStatus(AppConstant.CaseStatus.WaitingFranchiseAssigment.label);
					}else {
						caseModel.setStatus(AppConstant.CaseStatus.WaitingRequiredDocumentList.label);//TODO need to check the which status to update
					}
					caseModel.setIseAdharComplete(Boolean.TRUE);
					
					caseLogRepo.add(CaseLogModel.builder().caseId(getDocumentRequestBean.getCaseId())
							.action("eSigning Done").createBy(userLoginBean.getDisplayName()).build());
					/*
					 * notification for eAdhar complete by Admin SMS to end user.
					 */
					String website = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.WEBSITE);//"http://www.findmymoney.in";//MessageTemplateService.getMessage("WEBSITE"); //"http://www.findmymoney.in";
					String smsText= MessageTemplateService.getMessage("EADHAR_COMPLETE_SMS"); 
//					ApplicationUserBean applicationUserBean = applicationUserService.getApplicationUserById(caseModel.getUserId());
//					UserLoginBean userLoginBeanObj = loginService.getUserLoginApplicationUserId(applicationUserBean.getId());
					UserLoginBean userLoginBeanObj = loginService.getUserLogin(caseModel.getUserId());
					ApplicationUserBean applicationUserBean = userLoginBeanObj.getApplicationUserBean();
					if(Objects.isNull(applicationUserBean)) {
						applicationUserBean = applicationUserService.getApplicationUserById(userLoginBeanObj.getApplicationUserId());
					}
					smsText = MessageFormat.format(smsText, applicationUserBean.getFirstName(), caseModel.getReferenceNumber(), website);
					caseService.sendSMSPostApplicationStatusChange(userLoginBeanObj.getMobileNo(),smsText);
					sendEmail(userLoginBeanObj, caseModel.getAggrmentDcoumentId());
				}
				caseService.updateCustomerAggrement(caseModel, userLoginBean);
//				deleteOldDoc(oldDocId);
				caseTemplateService.saveFileToAS3(agreementFile,bucketName);
				
			} else {
				List<ValidationError> errorList = new ArrayList<>();
				errorList.add(ValidationError.builder().message(response.message()).build());
				caseSurepassClientidService.storeSurepassClientId(getDocumentRequestBean.getCaseId(),getDocumentRequestBean.getClientId(),
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
		return oldDocId;
	}




	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void deleteOldDoc(Long oldDocId) {
		caseDocumentDao.deleteByDocumentId(oldDocId);
		caseGeneratedDocumentsRequiredRepo.deleteDocId(oldDocId);
		emailReposatory.deleteByDocId(oldDocId);
		documentDao.deleteById(oldDocId);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void sendEmail(UserLoginBean userLoginBeanObj,Long aggrmentDcoumentId) {
		String eMailBoday = "";
		String website = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.WEBSITE);//"http://www.findmymoney.in";
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

}
