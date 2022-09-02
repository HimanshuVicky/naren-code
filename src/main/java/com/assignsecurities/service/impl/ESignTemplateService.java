package com.assignsecurities.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.AmazonClientException;
import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.AppConstant.DocumentType;
import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.app.util.FileCloudUtil;
import com.assignsecurities.bean.FileBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.dm.PropertyKeys;
import com.assignsecurities.domain.DocumentModel;
import com.assignsecurities.domain.ESignDocumentModel;
import com.assignsecurities.domain.RtaTemplateModel;
import com.assignsecurities.repository.impl.DocumentDao;
import com.assignsecurities.repository.impl.ESignDocumentRepo;
import com.assignsecurities.service.impl.doc.processor.DocProcessor;
import com.assignsecurities.service.impl.doc.processor.DocTemplateEnum;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
@Slf4j
public class ESignTemplateService {


	
	
	@Autowired
	private DocumentDao documentDao;
	
	@Autowired
	private ESignDocumentRepo eSignDocumentRepo; 
	
	@Autowired
	private Environment env;
	
	private @Autowired AutowireCapableBeanFactory beanFactory;
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public FileBean processTemplate(UserLoginBean userLoginBean) {
		
		RtaTemplateModel rtaTemplateModel = RtaTemplateModel.builder()
				.templateName(AppConstant.FRANCHISE_REFERRAL_AGREEMENT_TEMPLATE_NAME)
				.templateType(AppConstant.DocumentType.FranchiseReferralAgreement.name()).build();
		if(userLoginBean.getApplicationUserBean().getUserType().equalsIgnoreCase(AppConstant.USER_TYPE_REFERRAL_PARTNER)) {
//			REFERRAL_AGREEMENT_TEMPLATE_NAME
			rtaTemplateModel.setTemplateName(AppConstant.REFERRAL_AGREEMENT_TEMPLATE_NAME);
			rtaTemplateModel.setTemplateType(AppConstant.DocumentType.ReferralAgreement.name());
		}
		String docTemplatePath = loadTemplatePath(rtaTemplateModel);
		String finalPdfFileName = generateDestFileName(rtaTemplateModel, userLoginBean);
		
		

		//TODO get the required placeHolders
		Object objForTable=null;
		return storeAgreementAsPdf(docTemplatePath, rtaTemplateModel, userLoginBean, objForTable,
				finalPdfFileName,Boolean.TRUE,userLoginBean);
	}
	
	public String loadTemplatePath(RtaTemplateModel rtaTemplateModel) {
		String docTemplatePath = getTemplateRootPath();
		return docTemplatePath + "/" + rtaTemplateModel.getTemplateName();

	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public String generateDestFileName(RtaTemplateModel rtaTemplateModel, UserLoginBean userLoginBean) {
		String pdfTargetFileName = rtaTemplateModel.getTemplateName().replaceAll("docx", "pdf");
		pdfTargetFileName = "ReferralAgreement_" + userLoginBean.getId() + "_" + pdfTargetFileName;
		return pdfTargetFileName;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public FileBean storeAgreementAsPdf(String srcPath, RtaTemplateModel rtaTemplateModel, Object objPlaceHolder,
			Object objForTable, String finalPdfFileName, Boolean storeDoc,UserLoginBean userLoginBean) {
		String docTemplatePath = getTemplateRootPath();
		String destPath = null;
		String destPathPdf = null;
		FileBean agreementFile = new FileBean();
		try {
			Files.createDirectories(Paths.get(docTemplatePath + "/temp/"));
			String bucketName = ApplicationPropertiesService
					.getPropertyStringValue(PropertyKeys.AZ_S3_BUCKET_NAME);
			String dcoumentName = AppConstant.REFERRAL_AGREEMENT_TEMPLATE_NAME;
			DocTemplateEnum docTemplateEnum = DocTemplateEnum.ReferralAgreement;
			DocumentType documentType =  AppConstant.DocumentType.ReferralAgreement;
			if (AppConstant.USER_TYPE_FRANCHISE.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())
					|| AppConstant.USER_TYPE_FRANCHISE_USER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
				docTemplateEnum = DocTemplateEnum.FranchiseReferralAgreement;
				documentType =  AppConstant.DocumentType.FranchiseReferralAgreement;
				dcoumentName = AppConstant.FRANCHISE_REFERRAL_AGREEMENT_TEMPLATE_NAME;
			}
			
			
			DocProcessor docProcessor = new DocProcessor();
			destPath = docTemplatePath + "/temp/" + dcoumentName;
			destPathPdf = docTemplatePath + "/temp/"
					+ dcoumentName.replaceAll("docx", "pdf");
			
			
			//DocTemplateEnum.ReferralAgreement
			//public static final String  FRANCHISE_REFERRAL_AGREEMENT_TEMPLATE_NAME = "Franchise Referral Agreement With Adhyata.docx";
			
//			public static final String  REFERRAL_AGREEMENT_TEMPLATE_NAME = "Referral Agreement With Adhyata.docx";

			docProcessor.replaceTextInDoc(srcPath, destPath, destPathPdf, docTemplateEnum, objPlaceHolder, objForTable);

			agreementFile.setFileTitle(finalPdfFileName);
			agreementFile.setFileContentType(AppConstant.JAVA_SCRIPT_DATA_PDF_BASE64);

			File file = new File(destPathPdf);
			if (storeDoc) {
				saveFileToAS3(file, agreementFile, bucketName);
			}
			byte[] content = FileUtils.readFileToByteArray(file);
			agreementFile.setFileContent(Base64.getEncoder().encodeToString(content));
			
			DocumentModel documentModel = DocumentModel.builder().name(finalPdfFileName)
					.createdDate(LocalDateTime.now()).createBy(userLoginBean.getId())
					.type(documentType.name())
					.contentType(AppConstant.JAVA_SCRIPT_DATA_PDF_BASE64).bucketName(bucketName).build();
			
			Long eSignDcoumentId = documentDao.add(documentModel);
			
			eSignDocumentRepo.add(ESignDocumentModel.builder().userId(userLoginBean.getId()).documentId(eSignDcoumentId).build());
			
		} catch (IOException e) {
			throw new ServiceException(e);
		}

		try {
			Files.deleteIfExists(Paths.get(destPath));
			Files.deleteIfExists(Paths.get(destPathPdf));
		} catch (IOException e) {
			// ignore
		}
		return agreementFile;
	}



	public String getTemplateRootPath() {
		String docTemplatePath =ApplicationPropertiesService
				.getPropertyStringValue(PropertyKeys.DOC_TEMPLATE_PATH_ENV_VAR);
		if (Objects.isNull(docTemplatePath)) {
			docTemplatePath =   System.getenv(PropertyKeys.DOC_TEMPLATE_PATH_ENV_VAR);
		}
		return docTemplatePath;
	}
	

	public void saveFileToAS3(FileBean fileBean, String bucketName) {
		try{
			FileCloudUtil.delete(bucketName, fileBean.getFileTitle());
		} catch (Exception e) {
			//ignore
		}
//	        System.out.println("fileBean.getFileTitle()==>"+fileBean.getFileTitle());
		FileCloudUtil imageCloudUtil = new FileCloudUtil(fileBean.getFileTitle(), bucketName, fileBean.getFileContent());
		try {
//			byte[] fileBytes = Util.getBytes(fileBean.getFileContent());
//			 Path path = Paths.get("E:\\ShareProject\\testAgreement.pdf");
//			java.nio.file.Files.write(path, fileBytes);
			imageCloudUtil.upload();
		} catch (AmazonClientException | InterruptedException | IOException e) {
			throw new ServiceException(e);
		}
	}
	
	public void saveFileToAS3(File file, FileBean fileBean, String bucketName) {
		FileCloudUtil imageCloudUtil = new FileCloudUtil(fileBean.getFileTitle(), bucketName, file);
		try {
			imageCloudUtil.upload();
		} catch (AmazonClientException | InterruptedException | IOException e) {
			throw new ServiceException(e);
		}
	}
}
