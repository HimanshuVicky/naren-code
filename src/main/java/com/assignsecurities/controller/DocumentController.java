package com.assignsecurities.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.assignsecurities.bean.DocumentBean;
import com.assignsecurities.service.impl.DocumentService;

@RestController
public class DocumentController extends BaseController {

	@Autowired
	private DocumentService documentService;

//	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<Void> uploadFile(@Validated @RequestBody FileBean fileBean) {
//		UserLoginBean userLoginBean = getUser();
//		byte[] fileBytes = Util.getBytes(fileBean.getFileContent());
//		FileCloudUtil imageCloudUtil = new FileCloudUtil(fileBean.getFileTitle(), FileCloudUtil.BUCKET_NAME, fileBytes);
//		try {
//			imageCloudUtil.upload();
//		} catch (AmazonClientException | InterruptedException | IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return ResponseEntity.ok().build();
//	}

	@RequestMapping(value = "/getDocumentById/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public DocumentBean getById(@PathVariable("id") Long id) {
		return documentService.getById(id);
	}
	
//	@RequestMapping(value = "/getDocumentByName/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//	public DocumentBean getByName(@PathVariable("name") String name) {
//		return documentService.getByName(name);
//	}
}
