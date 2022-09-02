package com.assignsecurities.service.impl;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.assignsecurities.bean.surepass.AadharData;
import com.google.gson.Gson;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TestSurePassClient {
	public static void main(String[] args) {
		String baseUrl = "https://sandbox.aadhaarkyc.io";
		String authorizationToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MTI2MDEwNzcsIm5iZiI6MTYxMjYwMTA3NywianRpIjoiMmY3NjUzZjQtNjcxYS00MDI2LWJiNGQtZTU3MDI3NGUxOGVmIiwiZXhwIjoxNjE1MTkzMDc3LCJpZGVudGl0eSI6ImRldi50cmFja3NvZnRzb2x1dGlvbnNAYWFkaGFhcmFwaS5pbyIsImZyZXNoIjpmYWxzZSwidHlwZSI6ImFjY2VzcyIsInVzZXJfY2xhaW1zIjp7InNjb3BlcyI6WyJyZWFkIl19fQ.hRL-rTyiAnGQrTTJ6rZblQgQe3IpaTTp_dg_cPaMPN0";
		String aadharNumber = "515715043335";
//		String authorizationToken = "<Kept actula Token while testing>";
//		String aadharNumber = "<Provided Actual Adhar Name>";

		try {
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			String requestUrl = baseUrl + "/api/v1/ocr/aadhaar";
			RequestBody reqBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
					.addFormDataPart("file", "file", RequestBody
							.create(okhttp3.MediaType.parse("application/octet-stream"), new File("E:\\Personal\\NC\\myAdhar2.jpg")))
					.build();
			Request request = new Request.Builder().url(requestUrl)
					.method("POST", reqBody)
					.addHeader("Authorization",
							"Bearer "+authorizationToken)
					.addHeader("accept", MediaType.APPLICATION_JSON_VALUE)
					.build();
			Response response = client.newCall(request).execute();
			String responseString = response.body().source().readByteString().utf8();
			System.out.println("responseString===>" + responseString);
			
			 // Creating a Gson Object 
	        Gson gson = new Gson(); 

	        AadharData aadharData 
	            = gson.fromJson(responseString, 
	            		AadharData.class); 
	        System.out.println("aadharData===>" + aadharData);

		    
		} catch (Exception e) {

			e.printStackTrace();

		}
		if(true) {
			return;
		}
		try {
//			byte[] idCardByteArray = Base64.getDecoder().decode(aadharFrontImage.getFileContent().getBytes());
			File file = new File("E:\\Personal\\NC\\myAdhar2.jpg");
			byte[] idCardByteArray = FileUtils.readFileToByteArray(file);
			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//			body.add("file", new ByteArrayResource(idCardByteArray));
			body.add("file", idCardByteArray);
			String requestUrl = baseUrl + "/api/v1/ocr/aadhaar";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.add("Authorization", "Bearer " + authorizationToken);
			headers.add("Content-Type", MediaType.MULTIPART_FORM_DATA.toString());
			headers.setAccept(Arrays.asList(MediaType.ALL));

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
			RestTemplate restTemplate = new RestTemplate();
//			
			ResponseEntity<String> responseEntity = restTemplate.postForEntity(requestUrl, requestEntity, String.class);
			System.out.println("Response code: " + responseEntity.getStatusCode());
			System.out.println("Response Body: " + responseEntity.getBody());
//			ResponseEntity<String> result = restTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity, String.class);
//			System.out.println("result===>" + result);
		} catch (Exception e) {

			e.printStackTrace();

		}

		// Working Coded
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			headers.setContentType(MediaType.APPLICATION_JSON);
//			headers.set("Authorization", "Bearer "+token);
			headers.add("Authorization", "Bearer " + authorizationToken);
			String requestUrl = baseUrl + "/api/v1/aadhaar-v2/generate-otp";
			MultiValueMap<String, String> postParams = new LinkedMultiValueMap<String, String>();
			postParams.add("id_number", aadharNumber);
			System.out.println("Post parameters are" + postParams);
			HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(
					postParams, headers);
			System.out.println("request entities  are" + requestEntity);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> result = restTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity,
					String.class);
			System.out.println("result===>" + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
