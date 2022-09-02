package com.assignsecurities.service.impl;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.app.exception.ValidationException;
import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.bean.CaseCancelChequeBean;
import com.assignsecurities.bean.FileBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.bean.ValidationError;
import com.assignsecurities.bean.surepass.ChequeDetails;
import com.assignsecurities.bean.surepass.ChequeDetailsExtended;
import com.assignsecurities.dm.PropertyKeys;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
@Slf4j
public class CaseKycChequeScanService {

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void scanChequeDetails(CaseCancelChequeBean caseCancelChequeBean, UserLoginBean userLoginBean) {
		String baseUrl = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.SURE_PASS_URL);
		String authorizationToken = ApplicationPropertiesService
				.getPropertyStringValue(PropertyKeys.SURE_PASS_AUTH_TOKEN);
		boolean isIntegrationRequired = ApplicationPropertiesService
				.getPropertyBooleanValue(PropertyKeys.SURE_PASS_INTEGRATION_REQUIRED);
		try {
			FileBean cancelChequeImage = caseCancelChequeBean.getCancelChequeImage();
			byte[] idCardByteArray = Base64.getDecoder().decode(cancelChequeImage.getFileContent().getBytes());
			if(!isIntegrationRequired) {
				return;
			}

			OkHttpClient client = new OkHttpClient().newBuilder().build();
			String requestUrl = baseUrl + "/api/v1/ocr/cheque";
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
			if (response.isSuccessful()) {
				String responseString = response.body().source().readByteString().utf8();
				log.info("responseString===>" + responseString);
				// Creating a Gson Object
				Gson gson = new Gson();
				ChequeDetails chequeDetails = gson.fromJson(responseString, ChequeDetails.class);
				log.info("chequeDetails===>" + chequeDetails);
				if(ArgumentHelper.isValid(chequeDetails.getData().getAccount_number().getValue())) {
	//				String clientId = aadharData.getData().getClient_id();
					String accountNumber = chequeDetails.getData().getAccount_number().getValue();
					String ifscCode = chequeDetails.getData().getIfsc_code().getValue();
					caseCancelChequeBean.setAccountNumber(accountNumber);
					caseCancelChequeBean.setIfscCode(ifscCode);
					try {
						OkHttpClient clientRazorpay = new OkHttpClient().newBuilder()
							  .build();
							Request requestRazorpay = new Request.Builder()
							  .url("https://ifsc.razorpay.com/"+ifscCode)
							  .method("GET", null)
							  .build();
							Response responseRazorpay = clientRazorpay.newCall(requestRazorpay).execute();
							String responseStringRazorpay = responseRazorpay.body().source().readByteString().utf8();
							log.info("responseStringRazorpay===>" + responseStringRazorpay);
							ChequeDetailsExtended chequeDetailsExtended = gson.fromJson(responseStringRazorpay, ChequeDetailsExtended.class);
//							caseCancelChequeBean.setChequeNumber(chequeDetailsExtended.get);
							caseCancelChequeBean.setBankName(chequeDetailsExtended.getBANK());
							caseCancelChequeBean.setBankAddress(chequeDetailsExtended.getADDRESS());
							
					}catch (Exception e) {
						//ignore
					}
				}else {
					
					List<ValidationError> errorList = new ArrayList<>();
					errorList.add(ValidationError.builder().message("Cheque not found").build());

					if (ArgumentHelper.isNotEmpty(errorList)) {
						throw new ValidationException(errorList);
					}
				}

			} else {
				String responseString = response.body().source().readByteString().utf8();
				log.info("responseString===>" + responseString);
				List<ValidationError> errorList = new ArrayList<>();
				errorList.add(ValidationError.builder().message(response.message()).build());

				if (ArgumentHelper.isNotEmpty(errorList)) {
					throw new ValidationException(errorList);
				}
			}
		} catch (ValidationException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
//		return kycResponseBean;
	}

}
