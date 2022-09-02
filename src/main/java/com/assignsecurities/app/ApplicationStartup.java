package com.assignsecurities.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.assignsecurities.app.util.DMConstants;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.domain.dm.ObjectImportModel;
import com.assignsecurities.repository.impl.ObjectDao;
import com.assignsecurities.repository.impl.ObjectImportDAOImpl;
import com.assignsecurities.service.impl.ApplicationPropertiesService;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	private ApplicationPropertiesService applicationPropertiesService;

	@Autowired
	private MessageTemplateService messageTemplateService;

	@Autowired
	private ObjectDao objectDao;

	@Autowired
	private ObjectImportDAOImpl objectImportDao;

	/**
	 * This event is executed as late as conceivably possible to indicate that the
	 * application is ready to service requests.
	 */
	@Override
	public void onApplicationEvent(final ApplicationReadyEvent event) {
//		System.out.println("##################***************************************");
		// TODO get System Login
		applicationPropertiesService.loadApplicationProperties();
		messageTemplateService.loadMessageTemplate();
		event.getSpringApplication().getSources().forEach(System.out::println);

		Resource resource = new ClassPathResource("templates");
		try {
			Map<String, Long> objectCodeId = new HashMap<>();
			objectCodeId.put("Script", 1L);
			objectCodeId.put("RtaData", 2L);
			objectCodeId.put("CaseFields", 3L);
//			System.out.println("resource--->" + resource);
			File templates = resource.getFile();
//			System.out.println("templates--->" + templates);
			Files.list(Paths.get(templates.getAbsolutePath())).forEach(file -> {
				String fileName = file.getFileName().toFile().getName();
//				System.out.println("fileName==>" + fileName);
				String objectCode = fileName.substring(0, fileName.indexOf("_"));
//				System.out.println("objectCode==>" + objectCode);
				Long objectId = objectCodeId.get(objectCode);
//				System.out.println("objectId==>" + objectId);
				try {
					objectDao.updateTemplate(new FileInputStream(file.toFile().getAbsoluteFile()), objectId);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			});
//			/*
			// TODO TEmp Code to create TestData
			ObjectImportModel objectImportModel = new ObjectImportModel();
			objectImportModel.setId(1L);
			objectImportModel.setObjId(2L);
			objectImportModel.setStatusId(DMConstants.FILE_STATUS_SCHEDULED);
			java.sql.Date dt = java.sql.Date.valueOf(LocalDate.now());
			objectImportModel.setDateCreated(dt);
			objectImportModel.setDateModified(dt);
			objectImportModel.setModifiedBy(1L);
			objectImportModel.setImportedBy(1L);
////			#--insert into `dm_obj_import` (`ID`, `DATE_CREATED`, `DATE_MODIFIED`, `ERROR_REC_COUNT`, `FILE_BYTE`, `FILE_FORMAT`, `FILE_NAME`, `LOCALE_CODE`, `OBJ_ID`, `RETRY_FILE`, `RETRY_FILE_NAME`, `STATUS_ID`, `TOTAL_REC_COUNT`, `IMPORTED_BY`, `MODIFIED_BY`)
////			#--values('2',now(),now(),'0','test','xlsx','Script.xlsx','en_US','1',NULL,NULL,'90796',NULL,'1','1');
			objectImportModel.setFileName("RtaData.xlsx");
			objectImportModel.setRetryFileName("");
			objectImportModel.setErrorRecordCount(0L);
			objectImportModel.setFileFormat("xlsx");
			objectImportModel.setFileExt("xlsx");
			objectImportModel.setLocaleCode("en_US");
//			String tempPath = templates.getAbsolutePath() + "/../testData/RtaData.xlsx";
//			System.out.println("tempPath===>"+tempPath);
//			try {
//				objectImportModel.setFileByte(Files.readAllBytes(Paths.get(tempPath)));
//				objectImportDao.addImport(objectImportModel);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}

			objectImportModel = new ObjectImportModel();
			objectImportModel.setId(2L);
			objectImportModel.setObjId(1L);
			objectImportModel.setDateCreated(dt);
			objectImportModel.setDateModified(dt);
			objectImportModel.setModifiedBy(1L);
			objectImportModel.setImportedBy(1L);
			objectImportModel.setStatusId(DMConstants.FILE_STATUS_SCHEDULED);
			objectImportModel.setFileName("Script.xlsx");
			objectImportModel.setRetryFileName("");
			objectImportModel.setErrorRecordCount(0L);
			objectImportModel.setFileFormat("xlsx");
			objectImportModel.setFileExt("xlsx");
			objectImportModel.setLocaleCode("en_US");
//			tempPath = templates.getAbsolutePath() + "/../testData/Script.xlsx";
//			try {
//				objectImportModel.setFileByte(Files.readAllBytes(Paths.get(tempPath)));
//				objectImportDao.addImport(objectImportModel);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

}