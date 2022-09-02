package com.assignsecurities.dm.handler;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.app.util.DMConstants;
import com.assignsecurities.app.util.MessageConstants;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.dm.reader.DocumentParser;
import com.assignsecurities.dm.reader.validator.BasicValidator;
import com.assignsecurities.domain.dm.DataLoadObjectModel;
import com.assignsecurities.domain.dm.DataRowModel;
import com.assignsecurities.domain.dm.Document;
import com.assignsecurities.domain.dm.ErrorMessageBean;
import com.assignsecurities.domain.dm.ObjectConfigBean;
import com.assignsecurities.domain.dm.ObjectTemplateBean;




/**
 * This interface has to be implemented by the different File types e.g.
 * Excel(xls,xlsx), CSV ,xml for handling the error in their own way.
 * 
 * This will be call to handle the Basic and Business Validation
 * 
 * 
 */
@Service
public abstract class ErrorHandler {
	protected static final String RETRY_CONSTANT = "Error_";
	
	protected @Autowired AutowireCapableBeanFactory beanFactory;
	
	/**
	 * This is used handle action error
	 * 
	 * @param doc
	 *            file document object
	 * @param objectConfigModel
	 *            Object configuration model
	 * @param dataLoadObjectModel
	 *            this contain business object,it's meta data object as per the
	 *            specific type and the list of error model.
	 * @param errorModels
	 *           list of errors
	 * @param user
	 *            current user model
	 * @throws ServiceException
	 *             for any error.
	 */
	protected abstract void handleActionDefinitionError(Document doc,
														DataLoadObjectModel dataLoadObjectModel,
														ObjectConfigBean objectConfigModel,
														List<ErrorMessageBean> errorModels, UserLoginBean user)
			throws ServiceException;

	/**
	 * This is used to check whether defined action are correct or not
	 * 
	 * @param doc
	 *            file document object
	 * @param objectConfigModel
	 *            Object configuration model
	 * @param dataLoadObjectModel
	 *            this contain business object,it's meta data object as per the
	 *            specific type and the list of error model.
	 * @param user
	 *            current user model
	 * @return true if action column are not having proper value
	 * @throws ServiceException
	 *             for any error.
	 */
	public boolean checkForActionDefinitionError(Document doc,
			ObjectConfigBean objectConfigModel,
			DataLoadObjectModel dataLoadObjectModel, UserLoginBean user)
			throws ServiceException {
		boolean isActionError = false;
		List<ErrorMessageBean> validationErrorModels = new ArrayList<ErrorMessageBean>();
		if (objectConfigModel.isActionColumnRequired()) {
			if (dataLoadObjectModel.getAction() == null
					|| (!DMConstants.ACTION_EDIT
							.equalsIgnoreCase(dataLoadObjectModel.getAction()) && !(DMConstants.ACTION_ADD
							.equalsIgnoreCase(dataLoadObjectModel.getAction())))) {
				ErrorMessageBean errorModel = new ErrorMessageBean();
				errorModel.setMessage(BasicValidator.getErrorMessage(
						MessageConstants.ACTION_NOT_DEFINED_MSG_KEY, user));
				validationErrorModels.add(errorModel);
				Map<String, DataRowModel> dataRowMap = dataLoadObjectModel
						.getDataRowsMap();
				for (String key : dataRowMap.keySet()) {
					DataRowModel dataRowModel = (DataRowModel) dataRowMap
							.get(key);
					dataRowModel.getColumns().get(0).setErrorMessage(
							BasicValidator.getErrorMessage(
									MessageConstants.ACTION_NOT_DEFINED_MSG_KEY, user));
				}
				this.handleActionDefinitionError(doc, dataLoadObjectModel,
						objectConfigModel, validationErrorModels, user);
				// retryFileCreationRequired = true;
				validationErrorModels.clear();
				// errorCounter++;
				isActionError = true;
			}
		}
		return isActionError;
	}

	/**
	 * This is used to check whether defined action are correct or not
	 * 
	 * @param doc
	 *            file document object
	 * @param objectConfigModel
	 *            Object configuration model
	 * @param dataLoadObjectModel
	 *            this contain business object,it's meta data object as per the
	 *            specific type and the list of error model.
	 * @param docParser document parser instance
	 * @param user
	 *            current user model
	 * @return true if action column are not having proper value
	 * @throws ServiceException
	 *             for any error.
	 */
	public abstract boolean checkForBasicValidationError(Document doc,
														 ObjectConfigBean objectConfigModel,
														 DataLoadObjectModel dataLoadObjectModel, DocumentParser docParser, UserLoginBean user)
			throws ServiceException;

	/**
	 * Handle business error
	 * 
	 * @param doc
	 *            file document object
	 * @param objectConfigModel
	 *            Object configuration model
	 * @param dataLoadObjectModel
	 *            this contain business object,it's meta data object as per the
	 *            specific type and the list of error model.
	 * @param user
	 *            current user model
	 * @param errorMessageModels
	 * @throws ServiceException
	 *             for any error.
	 */
	public abstract void handleBusinessError(Document doc,
			ObjectConfigBean objectConfigModel,
			DataLoadObjectModel dataLoadObjectModel,
			List<ErrorMessageBean> errorMessageModels, UserLoginBean user)
			throws ServiceException;

	/**
	 * Handle unknown errors
	 * 
	 * @param doc
	 *            file document object
	 * @param objectConfigModel
	 *            Object configuration model
	 * @param dataLoadObjectModel
	 *            this contain business object,it's meta data object as per the
	 *            specific type and the list of error model.
	 * @param user
	 *            current user model
	 * @param errorMessageModels
	 * @throws ServiceException
	 *             for any error.
	 */
	public abstract void handleUnknownError(Document doc,
			ObjectConfigBean objectConfigModel,
			DataLoadObjectModel dataLoadObjectModel,
			List<ErrorMessageBean> errorMessageModels, UserLoginBean user)
			throws ServiceException;

	/**
	 * 
	 * This method is used to write the retry File for the invalid/error object.
	 * 
	 * @param doc
	 *            file document object
	 * @param validationType
	 *            this will indicate the validation type (Basic and Business)
	 * 
	 * @param dataLoadObjectModel
	 *            this contain business object,it's meta data object as per the
	 *            specific type and the list of error model.
	 * @param errorModels
	 *            list of errors which include the Basic and Business
	 *            Validation. This can be null for Basic validation
	 * @param user
	 *            current user model
	 * @throws ServiceException
	 *             for any error.
	 */
	public abstract void handleErrors(Document doc, String validationType,
			DataLoadObjectModel dataLoadObjectModel,
			ObjectConfigBean objectConfigModel,
			List<ErrorMessageBean> errorModels, UserLoginBean user)
			throws ServiceException;

	/**
	 * This method is used to write the retry excel file to DB
	 * 
	 * @throws ServiceException
	 */
	public abstract void writeFile(String orgFielName) throws ServiceException;

	

//	protected ObjectServiceImpl getObjectService() throws ServiceException {
//		if(objectService==null) {
//			objectService= new ObjectServiceImpl();
//			beanFactory.autowireBean(objectService);
//		}
//		return objectService;
//	}

	
	/**
	 * Destroy resources after use
	 */
	public void distroy(){
		
	}
	/**
	 * This method used to get the template and gives the input stream
	 * 
	 * @param doc
	 *            current doc object
	 * @return input stream
	 * @throws ServiceException
	 * @throws RemoteException
	 */
	public InputStream getTheTemplateInputStream(Document doc,
			UserLoginBean user) throws ServiceException {
		// doc.getFileType();
		ObjectTemplateBean objectTemplateModel = null;
	
//			objectTemplateModel = ObjectFactory.get().getObjectTemplateModel(
//					doc.getObjectId(),doc.getFileType());
		
			//TODO
		InputStream ios = null;

		return ios;
	}
}
