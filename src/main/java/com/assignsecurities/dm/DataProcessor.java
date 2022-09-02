package com.assignsecurities.dm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.transaction.UserTransaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.app.util.DMConstants;
import com.assignsecurities.app.util.ResourceBundleUtil;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.dm.handler.ErrorHandler;
import com.assignsecurities.dm.handler.csv.CSVErrorHandler;
import com.assignsecurities.dm.handler.excel.ExcelErrorHandler;
import com.assignsecurities.dm.processor.BusinessDataProcessor;
import com.assignsecurities.dm.processor.BusinessDataProcessorFactory;
import com.assignsecurities.dm.reader.DocumentReader;
import com.assignsecurities.dm.reader.validator.BusinessValidator;
import com.assignsecurities.dm.reader.validator.BusinessValidatorFactory;
import com.assignsecurities.dm.reader.validator.TemplateValidator;
import com.assignsecurities.dm.reader.validator.TemplateValidatorFactory;
import com.assignsecurities.domain.dm.DataLoadObjectModel;
import com.assignsecurities.domain.dm.Document;
import com.assignsecurities.domain.dm.ErrorMessageBean;
import com.assignsecurities.domain.dm.ObjectConfigBean;
import com.assignsecurities.scheduler.AssignSecuritiesObjectService;



/**
 * 
 * 
 */
@Service("dataProcessor")
public class DataProcessor {

	private static final Logger logger = LogManager.getLogger(DataProcessor.class);
	
	private @Autowired AutowireCapableBeanFactory beanFactory;

	private MetaDataConfigService metaDataConfigService;


	@Autowired
	private ResourceBundleUtil resourceBundleUtil;
	
	@Autowired
	private AssignSecuritiesObjectService objectService;

	/**
	 * This is the main/important method of Self Sufficiency, this acutely work
	 * as a controller, controls the flow start form the reading the file,
	 * template validation, Basic validation, business validation and the
	 * business data processing
	 * 
	 * @throws ServiceException
	 *             for any error
	 */
	public void processData(Long importId, UserLoginBean user)
			throws ServiceException {

		DocumentReader documentReader = new DocumentReader();
		List<DataLoadObjectModel> dataLoadObjectModels = null;
		UserTransaction transaction = null;
		ErrorHandler errorHandler = null;
		Document doc = null;

		try {

			SSFormater.init();
			beanFactory.autowireBean(documentReader);
			beanFactory.autowireBean(SSFormater.getSSFormater());
			metaDataConfigService =new MetaDataConfigServiceImpl();
			beanFactory.autowireBean(metaDataConfigService);
//			objectService=SpringBeanFactory.getBean(ObjectServiceImpl.class);
//			if(Objects.nonNull(objectService)) {
//				objectService= new ObjectServiceImpl();
//				beanFactory.autowireBean(objectService);
//			}
			logger.info("Getting objectService.getDocumentObjectToProcess");
			try {
				doc = objectService.getDocumentObjectToProcess(importId, user);
			} catch (Throwable e) {
				e.printStackTrace();
				throw new ServiceException(e.getMessage(), e);
			}
//			errorHandler = new ExcelErrorHandler(objectService);
			if(doc.getObjectId().equals(1)) {
				errorHandler = new CSVErrorHandler(objectService);
			}else {
				errorHandler = new ExcelErrorHandler(objectService);
			}
			TemplateValidator templateValidator = TemplateValidatorFactory
					.getValidatorInstance(String.valueOf(doc.getObjectId()),
							doc.getFileType(), resourceBundleUtil);
			beanFactory.autowireBean(errorHandler);
			beanFactory.autowireBean(templateValidator);
			ObjectConfigBean objectConfigModel = metaDataConfigService
					.getSpecificObjectMetaDataFromCache(
							new Long(doc.getParentSheetId()), user);
			// Apply basic template validation
			String errorMessage = templateValidator.validate(doc,
					objectConfigModel, user);
			if (errorMessage != null) {
				objectService.updateImportStatus(doc.getId(),
						DMConstants.FILE_STATUS_INVALID_TEMPLATE, user);
				return;
			}
			boolean retryFileCreationRequired = false;
			long successFulCounter = 0;
			long errorCounter = 0;
			long totalRecordesToBeProcessed = 0;
			// Start reading document
			try {
				doc = objectService.getDocumentObjectToProcess(doc.getId(),
						user);
				// documentReader.setDocumentParser(documentParser);
				dataLoadObjectModels = documentReader.readDocument(doc, user);
			} catch (Throwable e) {
				 e.printStackTrace();
				throw new ServiceException(e.getMessage(), e);
			}
			if (doc.getImportedBy() > 0) {
				// user = this.getAuthRemote().getUser(
				// doc.getImportedBy(),-1);
			}
			if (dataLoadObjectModels != null && !dataLoadObjectModels.isEmpty()) {
				BusinessValidator businessValidator = BusinessValidatorFactory
						.getValidatorInstance(objectConfigModel);
				beanFactory.autowireBean(businessValidator);
				BusinessDataProcessor businessDataProcessor = BusinessDataProcessorFactory
						.getBusinessDataProcessor(objectConfigModel);
				beanFactory.autowireBean(businessDataProcessor);
				List<ErrorMessageBean> validationErrorModels = new ArrayList<ErrorMessageBean>();
				List<ErrorMessageBean> processorValidationErrorModels = new ArrayList<ErrorMessageBean>();
				int currentCounter =0;
				for (DataLoadObjectModel dataLoadObjectModel : dataLoadObjectModels) {
					try {
						if ("No Action".equalsIgnoreCase(dataLoadObjectModel
								.getAction()) || !ArgumentHelper.isValid(dataLoadObjectModel
								.getAction())) {
							continue;
						}
						boolean isActionDefError = errorHandler
								.checkForActionDefinitionError(doc,
										objectConfigModel, dataLoadObjectModel,
										user);
						if (isActionDefError) {
//							retryFileCreationRequired = true;
//							errorCounter++;
							continue;
						}
						currentCounter++;
						totalRecordesToBeProcessed++;
						if(currentCounter>=1000) {
							objectService.updateErrorStat(doc.getId(),
									successFulCounter, totalRecordesToBeProcessed,
									user);
							currentCounter=0;
//							logger.info("Getting objectService.getDocumentObjectToProcess");
						}
						boolean isBasicValidationError = errorHandler
								.checkForBasicValidationError(doc,
										objectConfigModel, dataLoadObjectModel,
										documentReader.getDocumentParser(),
										user);
						if (isBasicValidationError) {
							retryFileCreationRequired = true;
							errorCounter++;
							continue;
						}
						// make a call to assemble object to populate dependent
						// value
						PropertyMapperService
								.getPropertyMapper()
								.assambleObject(
										dataLoadObjectModel
												.getBusinessObjectModel(),
										objectConfigModel, user);
						// check for business validation
						List<ErrorMessageBean> errorMessageModels = businessValidator
								.validate(dataLoadObjectModel,
										objectConfigModel, user);
						if (errorMessageModels != null
								&& !errorMessageModels.isEmpty()) {
							errorHandler.handleBusinessError(doc,
									objectConfigModel, dataLoadObjectModel,
									errorMessageModels, user);
							retryFileCreationRequired = true;
							errorCounter++;
							validationErrorModels.clear();
							dataLoadObjectModel.setDataRowsMap(null);
							continue;
						}
						// start business process of data
						try {
							// transaction = JNDIService.getUserTransaction();
							// transaction.setTransactionTimeout(20 * 60 *
							// 1000);
							// transaction.begin();
							businessDataProcessor.processBusinessData(
									dataLoadObjectModel,
									processorValidationErrorModels, user);
							if (processorValidationErrorModels != null
									&& !processorValidationErrorModels
											.isEmpty()) {
								// We got an exception, rollback any changes
								try {
									if (transaction != null) {
										transaction.rollback();
									}
								} catch (Exception e1) {
									logger.error(
											"Error while rolling back transation",
											e1);
								}
								errorHandler.handleBusinessError(doc,
										objectConfigModel, dataLoadObjectModel,
										processorValidationErrorModels, user);
								retryFileCreationRequired = true;
								errorCounter++;
								validationErrorModels.clear();
								processorValidationErrorModels.clear();
								dataLoadObjectModel.setDataRowsMap(null);
								continue;
							} else {
								// transaction.commit();
								successFulCounter++;
							}
						} catch (Exception e) {
							 e.printStackTrace();
							logger.error(e);

							// We got an exception, rollback any changes
							try {
								if (transaction != null) {
									transaction.rollback();
								}
							} catch (Exception e1) {
								logger.error(
										"Error while rolling back transation",
										e1);
							}
							List<ErrorMessageBean> errorModels = new ArrayList<ErrorMessageBean>();
							ErrorMessageBean errorModel = new ErrorMessageBean();
							errorModel
									.setMessage(resourceBundleUtil.getLabel(
											"message.dm.generalError",
											user.getLocale())
											+ e.getMessage());
							errorModels.add(errorModel);
							errorHandler.handleBusinessError(doc,
									objectConfigModel, dataLoadObjectModel,
									errorModels, user);
							retryFileCreationRequired = true;
							errorCounter++;
							validationErrorModels.clear();
							dataLoadObjectModel.setDataRowsMap(null);
							continue;
						}
					} catch (Exception e) {
						 e.printStackTrace();
						logger.error(e);
						List<ErrorMessageBean> errorModels = new ArrayList<ErrorMessageBean>();
						ErrorMessageBean errorModel = new ErrorMessageBean();
						StringBuilder sb = new StringBuilder();
						for (StackTraceElement element : e.getStackTrace()) {
							sb.append(element.toString());
							sb.append("\n");
						}
						errorModel.setMessage(resourceBundleUtil.getLabel(
								"message.dm.unknownError", user.getLocale())
								+ e.getMessage() + sb.toString());
						errorModels.add(errorModel);
						errorHandler.handleBusinessError(doc,
								objectConfigModel, dataLoadObjectModel,
								errorModels, user);
						retryFileCreationRequired = true;
						validationErrorModels.clear();
						dataLoadObjectModel.setDataRowsMap(null);
						errorCounter++;
					}
				}
			}
			try {
				// transaction = JNDIService.getUserTransaction();
				// transaction.setTransactionTimeout(20 * 60 * 1000);
				// transaction.begin();
				if (retryFileCreationRequired) {
					errorHandler.writeFile(doc.getFileName());
					if (successFulCounter == 0
							|| errorCounter == totalRecordesToBeProcessed) {
						objectService.updateImportStatus(doc.getId(),
								DMConstants.FILE_STATUS_UNSUCCESSFUL, user);
						// totalRecordesToBeProcessed of
						// totalRecordesToBeProcessed
						objectService.updateErrorStat(doc.getId(),
								successFulCounter, totalRecordesToBeProcessed,
								user);
					} else {
						objectService.updateImportStatus(doc.getId(),
								DMConstants.FILE_STATUS_PARTIAL_SUCCESSFUL,
								user);
						// successFulCounter of totalRecordesToBeProcessed
						objectService.updateErrorStat(doc.getId(),
								successFulCounter, totalRecordesToBeProcessed,
								user);
					}
				} else {
					if (successFulCounter == totalRecordesToBeProcessed) {
						objectService.updateImportStatus(doc.getId(),
								DMConstants.FILE_STATUS_SUCCESSFUL, user);
					} else {
						objectService.updateImportStatus(doc.getId(),
								DMConstants.FILE_STATUS_UNSUCCESSFUL, user);
					}
					// successFulCounter of totalRecordesToBeProcessed
					objectService
							.updateErrorStat(doc.getId(), successFulCounter,
									totalRecordesToBeProcessed, user);
				}
				// transaction.commit();
			} catch (Exception e) {
				logger.error(e);
				// transaction.rollback();
				throw new ServiceException(e.getMessage(), e);
			}

		} catch (Exception e) {
			// e.printStackTrace();
			logger.error(e.getMessage(), e);
			List<ErrorMessageBean> errorModels = new ArrayList<ErrorMessageBean>();
			ErrorMessageBean errorModel = new ErrorMessageBean();
			StringBuilder sb = new StringBuilder();
			for (StackTraceElement element : e.getStackTrace()) {
				sb.append(element.toString());
				sb.append("\n");
			}
			errorModel.setMessage(resourceBundleUtil.getLabel(
					"message.dm.unknownError", user.getLocale())
					+ e.getMessage() + sb.toString());
			errorModels.add(errorModel);
			errorHandler.handleUnknownError(doc, null, null, errorModels, user);

			// transaction = JNDIService.getUserTransaction();

			try {
				// transaction.setTransactionTimeout(20 * 60 * 1000);
				// transaction.begin();

				errorHandler.writeFile(doc.getFileName());
				objectService.updateImportStatus(doc.getId(),
						DMConstants.FILE_STATUS_UNSUCCESSFUL, user);
				// transaction.commit();
			} catch (Exception e1) {
				try {
					// transaction.rollback();
				} catch (Exception e2) {
					// ignore eception
				}
				logger.error("Retry file creation failed", e1);
			}

			throw new ServiceException(e.getMessage(), e);
		} finally {
			if (doc != null && doc.getImputStream() != null) {
				try {
					doc.getImputStream().close();
				} catch (IOException e) {
					logger.error("Error while closing inputStream ", e);
				}
			}
			if(Objects.nonNull(errorHandler)) {
				errorHandler.distroy();
				SSFormater.destory();
			}
		}
	}

	

}
