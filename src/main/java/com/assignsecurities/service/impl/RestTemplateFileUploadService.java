package com.assignsecurities.service.impl;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class RestTemplateFileUploadService {
	public static void main(String[] args) {
		String baseUrl = "https://sandbox.aadhaarkyc.io";
		String authorizationToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MTI2MDEwNzcsIm5iZiI6MTYxMjYwMTA3NywianRpIjoiMmY3NjUzZjQtNjcxYS00MDI2LWJiNGQtZTU3MDI3NGUxOGVmIiwiZXhwIjoxNjE1MTkzMDc3LCJpZGVudGl0eSI6ImRldi50cmFja3NvZnRzb2x1dGlvbnNAYWFkaGFhcmFwaS5pbyIsImZyZXNoIjpmYWxzZSwidHlwZSI6ImFjY2VzcyIsInVzZXJfY2xhaW1zIjp7InNjb3BlcyI6WyJyZWFkIl19fQ.hRL-rTyiAnGQrTTJ6rZblQgQe3IpaTTp_dg_cPaMPN0";
		String aadharNumber = "515715043335";
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.add("Authorization", "Bearer " + authorizationToken);
			String requestUrl = baseUrl + "/api/v1/aadhaar-v2/generate-otp";
			// This nested HttpEntiy is important to create the correct
			// Content-Disposition entry with metadata "name" and "filename"
			MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
//	        ContentDisposition contentDisposition = ContentDisposition
//	                .builder("form-data")
//	                .name("file")
//	                .filename(filename)
//	                .build();
//	        fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
			File file = new File("E:\\Personal\\NC\\myAdhar2.jpg");
			byte[] idCardByteArray = FileUtils.readFileToByteArray(file);
			HttpEntity<byte[]> fileEntity = new HttpEntity<>(idCardByteArray, fileMap);

			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("file", fileEntity);

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity,
					String.class);
			System.out.println("response==>"+response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
