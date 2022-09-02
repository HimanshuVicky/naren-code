package com.assignsecurities.dm.handler.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.app.util.DMConstants;
import com.assignsecurities.app.util.ResourceBundleUtil;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.dm.ObjectServiceImpl;
import com.assignsecurities.dm.handler.ErrorHandler;
import com.assignsecurities.dm.reader.DocumentParser;
import com.assignsecurities.dm.reader.xml.XMLDataReader;
import com.assignsecurities.dm.writer.xml.XMLDataWriter;
import com.assignsecurities.domain.dm.DataLoadObjectModel;
import com.assignsecurities.domain.dm.Document;
import com.assignsecurities.domain.dm.ErrorMessageBean;
import com.assignsecurities.domain.dm.ObjectConfigBean;
import com.assignsecurities.domain.dm.xml.XMLErrorModel;





/**
 * This is the xml implementation for handling the error. this will create the
 * retry file by adding the 'retry' string in the file name.
 * 
 * This acutely creates the retry file with the errors. Basic validation error
 * will as cell comments and the business validation errors are consolidated a
 * the last columns of their respective models.
 * 
 * @author Narendra
 * 
 */
@Service
public class XMLErrorHandler extends ErrorHandler {
	private Document doc = null;
	private ObjectServiceImpl objectService;
	private FileOutputStream fileOutputStream;
	private File file;
	private String inputfileName;
	private UserLoginBean user;
	private XMLDataWriter dataWriter;
	private ObjectConfigBean objectConfigModel;
	
	@Autowired
	private ResourceBundleUtil resourceBundleUtil;

	private static final Logger logger = LogManager.getLogger(XMLErrorHandler.class);
	public XMLErrorHandler() {

	}

	/**
	 * this method initialized the current object with all necessary
	 * information. This need to be call before all locale methods.
	 * 
	 * @param objectConfigModel
	 * @param doc
	 * @param user
	 * @throws ServiceException
	 */
	private void init(ObjectConfigBean objectConfigModel, Document doc,
			UserLoginBean user) throws ServiceException {
		if (dataWriter == null) {
			dataWriter = new XMLDataWriter(resourceBundleUtil);
			this.objectConfigModel = objectConfigModel;
		}
		if (fileOutputStream == null) {
			this.user = user;
			this.doc = doc;
			Random randomGenerator = new Random();
			String fileName = RETRY_CONSTANT
					+ objectConfigModel.getObjectName() + "_"
					+ randomGenerator.nextInt(100) + "-"
					+ System.currentTimeMillis() + ".xml";
			inputfileName = fileName;
			file = new File(fileName);
			try {
				fileOutputStream = new FileOutputStream(fileName);

			} catch (Exception e) {
				try {
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					if (file != null) {
						file.deleteOnExit();
					}
				} catch (IOException e1) {
					// ignoure exception
				}
				throw new ServiceException(e);
			}
			try {
				fileOutputStream
						.write(("<"
								+ objectConfigModel.getTabOrFileName().replace(
										"#", "") + "S>").getBytes());

			} catch (IOException e) {
				throw new ServiceException(e);
			}
		}
	}

	protected void handleActionDefinitionError(Document doc,
											   DataLoadObjectModel dataLoadObjectModel,
											   ObjectConfigBean objectConfigModel,
											   List<ErrorMessageBean> errorModels, UserLoginBean user)
			throws ServiceException {
		if (errorModels == null || errorModels.isEmpty()) {
			return;
		}
		init(objectConfigModel, doc, user);
		XMLErrorModel xmlErrorModel = new XMLErrorModel();
		List<String> msgList = new ArrayList<>();
		msgList.add(errorModels.get(0).getMessage());
		xmlErrorModel.setErrorMessages(msgList);
		xmlErrorModel.setDataValue(dataLoadObjectModel.getAction());
		Map<String, XMLErrorModel> errorMap = new HashMap<String, XMLErrorModel>();
		errorMap.put(DMConstants.ACTION_TEXT, xmlErrorModel);
		try {
			fileOutputStream.write(dataWriter.NEXT_LINE.getBytes());
			dataWriter.setActionValue(dataLoadObjectModel.getAction());
			fileOutputStream.write(dataWriter.getXML(objectConfigModel,
					dataLoadObjectModel.getBusinessObjectModel(), "", errorMap,
					user, true,false).getBytes());
			fileOutputStream.write(dataWriter.NEXT_LINE.getBytes());
			dataWriter.setActionValue(null);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public boolean checkForBasicValidationError(Document doc,
			ObjectConfigBean objectConfigModel,
			DataLoadObjectModel dataLoadObjectModel, DocumentParser docParser,
			UserLoginBean user) throws ServiceException {
		init(objectConfigModel, doc, user);
		boolean isBasicValidationError = false;
		Map<String, XMLErrorModel> errorMap = new HashMap<String, XMLErrorModel>();
		if (dataLoadObjectModel == null || docParser != null) {
			errorMap = ((XMLDataReader) docParser).getErrorMessageModelMap();
			if (errorMap == null || errorMap.isEmpty()) {
				return false;
			}
			if (dataLoadObjectModel.isError()) {
				isBasicValidationError = true;
				try {
					fileOutputStream.write(dataWriter.NEXT_LINE.getBytes());
					dataWriter.setActionValue(dataLoadObjectModel.getAction());
					fileOutputStream.write(dataWriter.getXML(objectConfigModel,
							dataLoadObjectModel.getBusinessObjectModel(), "",
							errorMap, user, true,false).getBytes());
					fileOutputStream.write(dataWriter.NEXT_LINE.getBytes());
					dataWriter.setActionValue(null);
				} catch (IOException e) {
					throw new ServiceException(e);
				}
			}
		}
		return isBasicValidationError;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleBusinessError(Document doc,
			ObjectConfigBean objectConfigModel,
			DataLoadObjectModel dataLoadObjectModel,
			List<ErrorMessageBean> errorMessageModels, UserLoginBean user)
			throws ServiceException {
		init(objectConfigModel, doc, user);
		if (errorMessageModels == null || errorMessageModels.isEmpty()) {
			return;
		}
		XMLErrorModel xmlErrorModel = new XMLErrorModel();
		List msgList = new ArrayList<String>();
		for (ErrorMessageBean errorMessageModel : errorMessageModels) {
			msgList.add(errorMessageModel.getMessage());
		}
		xmlErrorModel.setErrorMessages(msgList);
		Map<String, XMLErrorModel> errorMap = new HashMap<String, XMLErrorModel>();
		errorMap.put(DMConstants.BUSINESS_ERROR, xmlErrorModel);
		try {
			fileOutputStream.write(dataWriter.NEXT_LINE.getBytes());
			dataWriter.setActionValue(dataLoadObjectModel.getAction());
			fileOutputStream.write(dataWriter.getXML(objectConfigModel,
					dataLoadObjectModel.getBusinessObjectModel(), "", errorMap,
					user, true,false).getBytes());
			fileOutputStream.write(dataWriter.NEXT_LINE.getBytes());
			dataWriter.setActionValue(null);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void handleErrors(Document doc, String validationType,
			DataLoadObjectModel dataLoadObjectModel,
			ObjectConfigBean objectConfigModel,
			List<ErrorMessageBean> errorModels, UserLoginBean user)
			throws ServiceException {
		init(objectConfigModel, doc, user);

	}

	@SuppressWarnings("unchecked")
	public void handleUnknownError(Document doc,
			ObjectConfigBean objectConfigModel,
			DataLoadObjectModel dataLoadObjectModel,
			List<ErrorMessageBean> errorModels, UserLoginBean user)
			throws ServiceException {
		if (errorModels == null || errorModels.isEmpty()) {
			return;
		}
		XMLErrorModel xmlErrorModel = new XMLErrorModel();
		List msgList = new ArrayList<String>();
		msgList.add(errorModels.get(0).getMessage());
		xmlErrorModel.setErrorMessages(msgList);
		xmlErrorModel.setDataValue(dataLoadObjectModel.getAction());
		Map<String, XMLErrorModel> errorMap = new HashMap<String, XMLErrorModel>();
		errorMap.put(DMConstants.VALIDATION_TYPE_UNKNOWN,
				xmlErrorModel);
		try {
			fileOutputStream.write(dataWriter.NEXT_LINE.getBytes());
			dataWriter.setActionValue(dataLoadObjectModel.getAction());
			fileOutputStream.write(dataWriter.getXML(objectConfigModel,
					dataLoadObjectModel.getBusinessObjectModel(), "", errorMap,
					user, true,false).getBytes());
			fileOutputStream.write(dataWriter.NEXT_LINE.getBytes());
			dataWriter.setActionValue(null);
		} catch (IOException e) {
			throw new ServiceException(e);
		}

	}

	@Override
	public void writeFile(String orgFileName) throws ServiceException {
		try {
			fileOutputStream
					.write(("</"
							+ objectConfigModel.getTabOrFileName().replace("#",
									"") + "S>").getBytes());
			fileOutputStream.close();
//			int reTryFileId = upload(inputfileName, file, "xml", user);
//			ObjectBean objectModel = new ObjectBean();
//			objectModel.setId(doc.getId());
//			objectModel.setReTryFileId(reTryFileId);
//			getObjectService().updateRetryFileId(objectModel, user);
			objectService.updateRetryFile(doc.getId(), inputfileName, file, user);
		} catch (FileNotFoundException e) {
			throw new ServiceException(e);
		} catch (IOException e) {
			throw new ServiceException(e);
		} finally {
			try {
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
			} catch (IOException e) {
				// ignoure exception
			}

			if (file != null) {
				file.deleteOnExit();
			}
		}

	}

	@Override
	public void distroy() {
		try {
			if (fileOutputStream != null) {
				fileOutputStream.close();
			}
		} catch (IOException e) {
			// ignoure exception
		}
		if (dataWriter == null) {
			dataWriter = null;
		}
		if (objectConfigModel == null) {
			objectConfigModel = null;
		}
		if (file != null) {
			file.deleteOnExit();
		}

	}

}
