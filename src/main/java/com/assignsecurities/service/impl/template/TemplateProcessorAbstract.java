package com.assignsecurities.service.impl.template;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.AmazonClientException;
import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.app.util.FileCloudUtil;
import com.assignsecurities.bean.FileBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.dm.PropertyKeys;
import com.assignsecurities.domain.CaseModel;
import com.assignsecurities.domain.RtaTemplateModel;
import com.assignsecurities.repository.impl.RtaTemplatesDao;
import com.assignsecurities.service.impl.ApplicationPropertiesService;
import com.assignsecurities.service.impl.doc.processor.DocProcessor;
import com.assignsecurities.service.impl.doc.processor.DocTemplateEnum;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
@Slf4j
public abstract class TemplateProcessorAbstract {

	@Autowired
	private RtaTemplatesDao rtaTemplatesDao;

	@Autowired
	private Environment env;

	private static Map<String, DocTemplateEnum> docTypeEnumMapping = new HashMap<>();
	static {
		docTypeEnumMapping.put("RTA Letter 1", DocTemplateEnum.RTALetter1);
		docTypeEnumMapping.put("RTA Letter 2", DocTemplateEnum.RTALetter2);
		docTypeEnumMapping.put("RTA Letter", DocTemplateEnum.RTALetter1);
//		docTypeEnumMapping.put("FIR Copy", DocTemplateEnum.FIRCopy);
//		docTypeEnumMapping.put("Indemnity Bond", DocTemplateEnum.IndemnityBond);
//		docTypeEnumMapping.put("Name Change Affidavit", DocTemplateEnum.NameChangeAffidavit);
//		docTypeEnumMapping.put("Address Change Affidavit", DocTemplateEnum.AddressChangeAffidavit);
//		docTypeEnumMapping.put("Specimen Signature Affidavit", DocTemplateEnum.SpecimenSignatureAffidavit);
//		docTypeEnumMapping.put("Duplicate Share Certitficate Affidavit", DocTemplateEnum.DuplicateShareCertitficateAffidavit);
//		docTypeEnumMapping.put("Transmission Of Shares Affidavit", DocTemplateEnum.TransmissionOfSharesAffidavit);
//		docTypeEnumMapping.put("Paper Advertisment", DocTemplateEnum.PaperAdvertisment);
	}

	public RtaTemplateModel getRtaTemplate(String rtaName, String templateType) {
		return rtaTemplatesDao.getRtaTemplate(rtaName, templateType);
	}

//	public FileBean loadTemplate(RtaTemplateModel rtaTemplateModel, UserLoginBean userLoginBean, CaseModel caseModel) {
//		String docTemplatePath = getTemplateRootPath();
//		String srcPath = docTemplatePath + "/" + rtaTemplateModel.getTemplateName();
//		FileBean fileTemplate = new FileBean();
//		fileTemplate.setFileContentType(AppConstant.JAVA_SCRIPT_DATA_PDF_BASE64);
//		File file = new File(srcPath);
//		try {
//			byte[] content = FileUtils.readFileToByteArray(file);
//			fileTemplate.setFileContent(Base64.getEncoder().encodeToString(content));
//		} catch (IOException e) {
//			throw new ServiceException(e);
//		}
//
//		return fileTemplate;
//	}

	public String loadTemplatePath(RtaTemplateModel rtaTemplateModel) {
		String docTemplatePath = getTemplateRootPath();
		return docTemplatePath + "/" + rtaTemplateModel.getTemplateName();

	}

	public String generateDestFileName(RtaTemplateModel rtaTemplateModel, UserLoginBean userLoginBean,
			CaseModel caseModel) {
		String pdfTargetFileName = rtaTemplateModel.getTemplateName().replaceAll("docx", "pdf");
		pdfTargetFileName = "Case Ref No_" + caseModel.getReferenceNumber() + "_" + pdfTargetFileName;
		return pdfTargetFileName;
	}

	public FileBean storeAgreementAsPdf(String srcPath, RtaTemplateModel rtaTemplateModel, Object objPlaceHolder,
			Object objForTable, String finalPdfFileName, Boolean storeDoc) {
		String docTemplatePath = getTemplateRootPath();
		String destPath = null;
		String destPathPdf = null;
		FileBean agreementFile = new FileBean();
		try {
			Files.createDirectories(Paths.get(docTemplatePath + "/temp/"));

			DocProcessor docProcessor = new DocProcessor();
			destPath = docTemplatePath + "/temp/" + AppConstant.CUSTOMER_AGREEMENT_TEMPLATE_NAME;
			destPathPdf = docTemplatePath + "/temp/"
					+ AppConstant.CUSTOMER_AGREEMENT_TEMPLATE_NAME.replaceAll("docx", "pdf");
			DocTemplateEnum docTemplateEnum = docTypeEnumMapping.get(rtaTemplateModel.getTemplateType());
			if (Objects.isNull(docTemplateEnum)) {
				docTemplateEnum = DocTemplateEnum.valueOf(rtaTemplateModel.getTemplateType().replaceAll(" ", ""));
			}

			docProcessor.replaceTextInDoc(srcPath, destPath, destPathPdf, docTemplateEnum, objPlaceHolder, objForTable);

			agreementFile.setFileTitle(finalPdfFileName);
			agreementFile.setFileContentType(AppConstant.JAVA_SCRIPT_DATA_PDF_BASE64);

			File file = new File(destPathPdf);
			if (storeDoc) {
				String bucketName = ApplicationPropertiesService
						.getPropertyStringValue(PropertyKeys.AZ_S3_BUCKET_NAME);
				saveFileToAS3(file, agreementFile, bucketName);
			}
			byte[] content = FileUtils.readFileToByteArray(file);
			agreementFile.setFileContent(Base64.getEncoder().encodeToString(content));
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

	public void saveFileToAS3(File file, FileBean fileBean, String bucketName) {
		FileCloudUtil imageCloudUtil = new FileCloudUtil(fileBean.getFileTitle(), bucketName, file);
		try {
			imageCloudUtil.upload();
		} catch (AmazonClientException | InterruptedException | IOException e) {
			throw new ServiceException(e);
		}
	}

	public String getTemplateRootPath() {
		String docTemplatePath =ApplicationPropertiesService
				.getPropertyStringValue(PropertyKeys.DOC_TEMPLATE_PATH_ENV_VAR);
		if (Objects.isNull(docTemplatePath)) {
			docTemplatePath =   System.getenv(PropertyKeys.DOC_TEMPLATE_PATH_ENV_VAR);
		}
		return docTemplatePath;
	}
}
