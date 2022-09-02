package com.assignsecurities.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.exception.ValidationException;
import com.assignsecurities.app.util.DMConstants;
import com.assignsecurities.bean.FileBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.bean.ValidationError;
import com.assignsecurities.dm.ObjectServiceImpl;
import com.assignsecurities.dm.PropertyKeys;
import com.assignsecurities.domain.Pagination;
import com.assignsecurities.domain.dm.ObjectImportModel;
import com.assignsecurities.domain.dm.ObjectModel;
import com.assignsecurities.service.impl.ApplicationPropertiesService;
import com.assignsecurities.service.impl.DataLoadService;

@RestController
public class DataManagementController extends BaseController {

	private @Autowired AutowireCapableBeanFactory beanFactory;

	@Autowired
	private DataLoadService dataLoadService;

	private ObjectServiceImpl objectService;

	@RequestMapping(value = "/getShowImports/objectId/{objectId}/currentPageNo/{currentPageNo}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> getShowImports(@PathVariable("objectId") Long objectId,
			@PathVariable("currentPageNo") Integer currentPageNo) {
		Map<String, Object> jsonResponse = new HashMap<String, Object>();
		Map<String, String> errorMessages = new HashMap<String, String>();
		UserLoginBean userLoginBean = getUser();
		if (!(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN))) {
			return null;
		}
		
		if (!Objects.isNull(errorMessages) && !errorMessages.isEmpty()) {
			jsonResponse.put("Result", "NotOK");
			jsonResponse.put("errorMessages", errorMessages.values());
			ResponseEntity<Void> responseEntity = getResponseEntity(jsonResponse);
			return responseEntity;
		}
//		Integer pageSize = Pagination.DEFAULT_PAGE_SIZE.intValue();
		Integer pageSize = 100;
		if (currentPageNo > 1) {

			currentPageNo = (currentPageNo - 1) * pageSize + 1;
		}

		Pagination<ObjectImportModel> pagination = new Pagination<ObjectImportModel>();
		pagination.setCurrPageNumber(currentPageNo.longValue());
		pagination.setPageSize(pageSize.longValue());
		pagination = dataLoadService.getLatestImports(userLoginBean, objectId, pagination);
		jsonResponse.put("Result", "OK");
		jsonResponse.put("totalRecords", pagination.getTotalRecords());
		jsonResponse.put("Records", pagination.getList());
		ResponseEntity<Void> responseEntity = getResponseEntity(jsonResponse);
		return responseEntity;
	}

	@RequestMapping(value = "/downloadFile/type/{type}/importId/{importId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public FileBean downloadImport(@PathVariable("type") String type, @PathVariable("importId") Long importId) {
		UserLoginBean userLoginBean = getUser();
		if (!(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN))) {
			return null;
		}
		ObjectImportModel objectImportModel = dataLoadService.getImport(userLoginBean, importId);
		String fileName = "a.xlsx";
		byte[] fileContent = null;
		if (Objects.isNull(type) || type.equalsIgnoreCase("File")) {
			fileName = objectImportModel.getFileName();
			fileContent = objectImportModel.getFileByte();
		} else {
			if (Objects.nonNull(objectImportModel.getRetryByte())) {
				fileContent = objectImportModel.getRetryByte();
			}
			if(Objects.nonNull(objectImportModel.getRetryFileName())) {
				fileName = objectImportModel.getRetryFileName();
			}else {
				fileName = objectImportModel.getObjId() + "_ReTry.xlsx";
			}
			
		}
		return FileBean.builder().fileTitle(fileName).fileContent(Base64.encodeBase64String(fileContent))
				.fileContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet").build();
	}

	@RequestMapping(value = "/importObject", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> importObject(@Validated @RequestBody ObjectImportModel objectImportModel, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
		Map<String, Object> jsonResponse = new HashMap<String, Object>();
		Map<String, String> warnningMessages = new HashMap<String, String>();

		UserLoginBean user = getUser();
		if (!(user.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN))) {
			return null;
		}
		
		objectImportModel.setImportedBy(user.getId().longValue());
		objectImportModel.setModifiedBy(user.getId().longValue());
		java.util.Date utilDate = new java.util.Date();
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
		objectImportModel.setDateCreated(sqlDate);
		objectImportModel.setDateModified(sqlDate);
//		objectImportModel.setFileName(multipartFile.getOriginalFilename());

//		byte[] bFile = Base64.decodeBase64(objectImportModel.getFileContent());
//		objectImportModel.setFileByte(bFile);
		String toReplace = AppConstant.JAVA_SCRIPT_DATA_EXCEL_SHEET_BASE64;
		String fileContent = objectImportModel.getFileContent();
//		toReplace = toReplace.replace(AppConstant.FILE_TYPE_JPEG, objectImportModel.getFileExt().toLowerCase());
		fileContent = fileContent.replaceAll(toReplace, "");
		byte[] bFile = Base64.decodeBase64(fileContent);
		objectImportModel.setFileByte(bFile);
		objectImportModel.setLocaleCode(user.getLocalCode());
		objectImportModel.setStatusId(DMConstants.FILE_STATUS_SCHEDULED);
		objectImportModel.setFileFormat(objectImportModel.getFileExt());

		getObjectService().addImport(objectImportModel);

		jsonResponse.put("Result", "OK");
		jsonResponse.put("warnningMessages", warnningMessages);
		ResponseEntity<Void> responseEntity = getResponseEntity(jsonResponse);
		return responseEntity;
	}

	private ObjectServiceImpl getObjectService() {
		if (Objects.isNull(objectService)) {
			objectService = new ObjectServiceImpl();
			beanFactory.autowireBean(objectService);
		}
		return objectService;
	}

	@RequestMapping(value = "/downloadTemplate/objectId/{objectId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public FileBean downloadTemplate(@PathVariable("objectId") Long objectId) {
		UserLoginBean userLoginBean = getUser();
		if (Objects.isNull(userLoginBean)) {
			userLoginBean = new UserLoginBean();
		}
		if (!(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN))) {
			return null;
		}
		ObjectModel objectModel = getObjectService().getObject(userLoginBean, objectId);
		String fileName = objectModel.getName() + ".xlsx";
		byte[] fileContent = objectModel.getObjectTemplateModel().getTemplate();
		return FileBean.builder().fileTitle(fileName).fileContent(Base64.encodeBase64String(fileContent))
				.fileContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet").build();
	}

	@RequestMapping(value = "/downloadTemplate/{ratName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public FileBean downloadTemplateByRTA(@PathVariable("ratName") String ratName) {
		UserLoginBean userLoginBean = getUser();
		if (Objects.isNull(userLoginBean)) {
			userLoginBean = new UserLoginBean();
		}
		if (!(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN))) {
			return null;
		}
		String templateRooPath = getTemplateRootPath();
		String fileName="";
		if("Linkintime".equals(ratName)) {
			fileName = "CaseFields_en_US_Template-LinkedinTime  V 1.0.xlsx";
		}else if("TSR".equals(ratName)) {
			fileName = "CaseFields_en_US_Template-TSR  V 1.0.xlsx";
		}else if("Bigshare".equals(ratName)) {
			fileName = "CaseFields_en_US_Template-Bigshare  V 1.0.xlsx";
		}else if("Kfintech".equals(ratName)) {
			fileName = "CaseFields_en_US_Template-Kfintech  V 1.0.xlsx";
		}else if("OneMore".equals(ratName)) {
			fileName = "CaseFields_en_US_Template-OneMore  V 1.0.xlsx";
		}else {
			ObjectModel objectModel = getObjectService().getObject(userLoginBean, 3L);
			fileName = objectModel.getName() + ".xlsx";
			byte[] fileContent = objectModel.getObjectTemplateModel().getTemplate();
			return FileBean.builder().fileTitle(fileName).fileContent(Base64.encodeBase64String(fileContent))
					.fileContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet").build();
		}
		File file = new File(templateRooPath+fileName);
		
		byte[] fileContent=null;
		try {
			fileContent = FileUtils.readFileToByteArray(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return FileBean.builder().fileTitle(fileName).fileContent(Base64.encodeBase64String(fileContent))
				.fileContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet").build();
	}
	
	private String getTemplateRootPath() {
		String docTemplatePath =ApplicationPropertiesService
				.getPropertyStringValue(PropertyKeys.DOC_TEMPLATE_PATH_ENV_VAR);
		if (Objects.isNull(docTemplatePath)) {
			docTemplatePath =   System.getenv(PropertyKeys.DOC_TEMPLATE_PATH_ENV_VAR);
		}
		return docTemplatePath;
	}
	
}
