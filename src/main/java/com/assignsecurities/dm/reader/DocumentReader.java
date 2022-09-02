package com.assignsecurities.dm.reader;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.app.util.DMConstants;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.dm.MetaDataConfigService;
import com.assignsecurities.dm.MetaDataConfigServiceImpl;
import com.assignsecurities.domain.dm.DataLoadObjectModel;
import com.assignsecurities.domain.dm.Document;
import com.assignsecurities.domain.dm.ObjectConfigBean;




/**
 * This class is responsible to read all supported document.
 * 
 * 
 */
public class DocumentReader {
	private @Autowired
	AutowireCapableBeanFactory beanFactory;
	
	private MetaDataConfigService metaDataConfigService;
	private static final Logger logger = LogManager.getLogger(DocumentReader.class);
	
	
	private DocumentParser documentParser;

	/**
	 * This API is used to read any supported document like excel,csv
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public List<DataLoadObjectModel> readDocument(Document doc, UserLoginBean user)
			throws ServiceException {
		List<DataLoadObjectModel> objectDataModel = new ArrayList<DataLoadObjectModel>();
		try {
			metaDataConfigService = new MetaDataConfigServiceImpl();
			beanFactory.autowireBean(metaDataConfigService);
			if (documentParser == null) {
				documentParser = DocumentParserFactory.getDocumentParser(doc
						.getFileType());
				
				if (documentParser == null) {
					if (DMConstants.DOCUMENT_TYPE_FORMAT_XML
							.equalsIgnoreCase(doc.getFileType())) {
						documentParser = DocumentParserFactory
								.getDocumentParser(DMConstants.DOCUMENT_TYPE_FORMAT_XML);
					}
				}
			}
			beanFactory.autowireBean(documentParser);
			ObjectConfigBean objectConfigModel = metaDataConfigService
					.getSpecificObjectMetaDataFromCache(
							new Long(doc.getParentSheetId()), user);
			objectDataModel = documentParser.readData(objectConfigModel, doc,
					user);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e);
		}
		return objectDataModel;

	}

	public void setDocumentParser(DocumentParser documentParser) {
		this.documentParser = documentParser;
	}

	public DocumentParser getDocumentParser() {
		return documentParser;
	}
}
