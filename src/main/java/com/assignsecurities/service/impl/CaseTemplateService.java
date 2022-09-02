package com.assignsecurities.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.AmazonClientException;
import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.app.util.FileCloudUtil;
import com.assignsecurities.app.util.Util;
import com.assignsecurities.bean.CaseDocumentBean;
import com.assignsecurities.bean.FileBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.domain.CaseModel;
import com.assignsecurities.domain.CaseScriptModel;
import com.assignsecurities.domain.ScriptModel;
import com.assignsecurities.repository.impl.CaseScriptRepo;
import com.assignsecurities.repository.impl.DocumentDao;
import com.assignsecurities.repository.impl.RtaTemplatesDao;
import com.assignsecurities.repository.impl.ScriptRepo;
import com.assignsecurities.service.impl.doc.processor.DocProcessor;
import com.assignsecurities.service.impl.doc.processor.DocTemplateEnum;
import com.assignsecurities.service.impl.template.AddressChangeAffidavitTemplateProcessor;
import com.assignsecurities.service.impl.template.CustomerAgreementTemplateProcessor;
import com.assignsecurities.service.impl.template.DuplicateShareCertitficateAffidavitTemplateProcessor;
import com.assignsecurities.service.impl.template.FIRCopyTemplateProcessor;
import com.assignsecurities.service.impl.template.IndemnityBondTemplateProcessor;
import com.assignsecurities.service.impl.template.NameChangeAffidavitTemplateProcessor;
import com.assignsecurities.service.impl.template.OtherTemplateProcessor;
import com.assignsecurities.service.impl.template.PaperAdvertismentTemplateProcessor;
import com.assignsecurities.service.impl.template.RtaLetter2TemplateProcessor;
import com.assignsecurities.service.impl.template.RtaLetterTemplateProcessor;
import com.assignsecurities.service.impl.template.SpecimenSignatureAffidavitTemplateProcessor;
import com.assignsecurities.service.impl.template.TemplateProcessor;
import com.assignsecurities.service.impl.template.TransmissionOfSharesAffidavitTemplateProcessor;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
@Slf4j
public class CaseTemplateService {

	@Autowired
	private CaseScriptRepo caseScriptRepo;


	@Autowired
	private ScriptRepo scriptRepo;
	
	@Autowired
	private RtaTemplatesDao rtaTemplatesDao;
	
	@Autowired
	private DocumentDao documentDao;
	
	@Autowired
	private Environment env;
	
	private @Autowired AutowireCapableBeanFactory beanFactory;
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public FileBean processTemplate(CaseDocumentBean caseDocumentBean, UserLoginBean userLoginBean,
			CaseModel caseModel) {
		
		TemplateProcessor templateProcessor = null;
		
		if(caseDocumentBean.getTemplateType().equals(DocTemplateEnum.CustomerAgreement.name())) {
			templateProcessor =  new CustomerAgreementTemplateProcessor();
		} else if(caseDocumentBean.getTemplateType().equals(AppConstant.DocumentType.RtaLetter1.label)) {
			templateProcessor =  new RtaLetterTemplateProcessor();
		} else if(caseDocumentBean.getTemplateType().equals(AppConstant.DocumentType.RtaLetter2.label)) {
			templateProcessor =  new RtaLetter2TemplateProcessor();
		}else {
			templateProcessor =  new  OtherTemplateProcessor();
		}
		beanFactory.autowireBean(templateProcessor);
		return templateProcessor.processTemplate(caseDocumentBean, userLoginBean, caseModel);
	}
	
	
	public void storeAgreementAsPdf(UserLoginBean userLoginBean, CaseModel caseModel, String docTemplatePath,
			String srcPath, String bucketName, FileBean agreementFile, boolean maskFolio) {
		DocProcessor docProcessor = new DocProcessor();
		String destPath = docTemplatePath + "/temp/" + AppConstant.CUSTOMER_AGREEMENT_TEMPLATE_NAME;
		String destPathPdf = docTemplatePath + "/temp/"
				+ AppConstant.CUSTOMER_AGREEMENT_TEMPLATE_NAME.replaceAll("docx", "pdf");

		try {
			Files.createDirectories(Paths.get(docTemplatePath + "/temp/"));
		} catch (IOException e1) {
			// ignore
		}

		DocTemplateEnum docTemplateEnum = DocTemplateEnum.CustomerAgreement;
		List<CaseScriptModel> caseScriptModels = caseScriptRepo.getByCaseId(caseModel.getId());
		List<Long> scriptIds = caseScriptModels.stream().map(script -> script.getScriptId()).distinct()
				.collect(Collectors.toList());
		List<ScriptModel> scriptModels = scriptRepo.getScripts(scriptIds);
		caseScriptModels.forEach(caseScript -> {
			Optional<ScriptModel> script = scriptModels.stream().filter(s -> s.getId().equals(caseScript.getScriptId()))
					.findFirst();
			caseScript.setScriptModel(script.orElse(null));
		});
		caseModel.setScripts(caseScriptModels);
		caseModel.setMaskFolio(maskFolio);
		docProcessor.replaceTextInDoc(srcPath, destPath, destPathPdf, docTemplateEnum, userLoginBean, caseModel);

		File file = new File(destPathPdf);
		saveFileToAS3(file, agreementFile, bucketName);

		try {
			byte[] content = FileUtils.readFileToByteArray(file);
			agreementFile.setFileContent(Base64.getEncoder().encodeToString(content));
			Files.deleteIfExists(Paths.get(destPath));
			Files.deleteIfExists(Paths.get(destPathPdf));
		} catch (IOException e) {
			// ignore
		}
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
