package com.assignsecurities.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.app.util.FileCloudUtil;
import com.assignsecurities.bean.DocumentBean;
import com.assignsecurities.bean.FileBean;
import com.assignsecurities.converter.DocumentConverter;
import com.assignsecurities.dm.PropertyKeys;
import com.assignsecurities.repository.impl.DocumentDao;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
@Slf4j
public class DocumentService {
	@Autowired
	private DocumentDao documentDao;

	public DocumentBean getById(Long id) {
		DocumentBean bean = DocumentConverter.convert(documentDao.getById(id));
		ArgumentHelper.requiredObjectNonNull(bean, String.format("Document Not found for Id : %d",id));
		setFileDetails(bean);
		return bean;
	}

	public DocumentBean getByName(String name) {
		DocumentBean bean = DocumentConverter.convert(documentDao.getByName(name));
		ArgumentHelper.requiredObjectNonNull(bean, String.format("Document Not found for name : %s",name));
		setFileDetails(bean);
		return bean;
	}

	private void setFileDetails(DocumentBean bean) {
		String bucketName = ApplicationPropertiesService
				.getPropertyStringValue(PropertyKeys.AZ_S3_BUCKET_NAME);
		String fileContent = FileCloudUtil.downloadFileBase64(bucketName, bean.getName());
		bean.setFile(FileBean.builder().fileTitle(bean.getName()).fileContent(fileContent)
				.fileContentType(bean.getContentType()).build());
	}
}
