package com.assignsecurities.dm.handler.csv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.app.util.DMConstants;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.dm.ObjectServiceImpl;
import com.assignsecurities.dm.handler.ErrorHandler;
import com.assignsecurities.dm.reader.DocumentParser;
import com.assignsecurities.domain.dm.DataColumnModel;
import com.assignsecurities.domain.dm.DataLoadObjectModel;
import com.assignsecurities.domain.dm.DataRowModel;
import com.assignsecurities.domain.dm.Document;
import com.assignsecurities.domain.dm.ErrorMessageBean;
import com.assignsecurities.domain.dm.ObjectConfigBean;
import com.assignsecurities.domain.dm.excel.ExcelDataLoadObjectModel;
import com.assignsecurities.domain.dm.excel.ExcelDataRowModel;
import com.opencsv.CSVWriter;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CSVErrorHandler  extends ErrorHandler {
	private ObjectServiceImpl objectService;
	protected @Autowired AutowireCapableBeanFactory beanFactory;
	private Document doc = null;
	private UserLoginBean user = null;
	private static List<String[]> cSVRows;

	public CSVErrorHandler() {
		cSVRows = new ArrayList<String[]>();
		if (objectService == null) {
			objectService = new ObjectServiceImpl();
		}
	}

	public CSVErrorHandler(ObjectServiceImpl objectService) {
		this.objectService = objectService;
		cSVRows = new ArrayList<String[]>();
	}

	protected void handleActionDefinitionError(Document doc, DataLoadObjectModel dataLoadObjectModel,
											   ObjectConfigBean objectConfigModel, List<ErrorMessageBean> errorModels, UserLoginBean user)
			throws ServiceException {
		this.handleErrors(doc, DMConstants.VALIDATION_TYPE_BASIC, dataLoadObjectModel, objectConfigModel, errorModels,
				user);

	}

	@Override
	public boolean checkForBasicValidationError(Document doc, ObjectConfigBean objectConfigModel,
												DataLoadObjectModel dataLoadObjectModel, DocumentParser docParser, UserLoginBean user)
			throws ServiceException {
		boolean isBasicValidationError = false;
		List<ErrorMessageBean> validationErrorModels = new ArrayList<ErrorMessageBean>();
		// check for basic validation error message
		for (DataRowModel dataRowModel : dataLoadObjectModel.getDataRowsMap().values()) {
			if (dataRowModel.getColumns() != null) {
				for (DataColumnModel dataColumnModel : dataRowModel.getColumns()) {
					if (dataColumnModel.getErrorMessage() != null
							&& !dataColumnModel.getErrorMessage().trim().isEmpty()) {
						ErrorMessageBean errorModel = new ErrorMessageBean();
						errorModel.setMessage(dataColumnModel.getErrorMessage());
						validationErrorModels.add(errorModel);
					}
				}
			}
		}

		if (!validationErrorModels.isEmpty()) {
			this.handleErrors(doc, DMConstants.VALIDATION_TYPE_BASIC, dataLoadObjectModel, objectConfigModel,
					validationErrorModels, user);
			isBasicValidationError = true;
			validationErrorModels.clear();
			// errorCounter++;
			// continue;
		}
		return isBasicValidationError;
	}

	@Override
	public void handleBusinessError(Document doc, ObjectConfigBean objectConfigModel,
									DataLoadObjectModel dataLoadObjectModel, List<ErrorMessageBean> errorMessageModels, UserLoginBean user)
			throws ServiceException {
		this.handleErrors(doc, DMConstants.VALIDATION_TYPE_BUSINESS, dataLoadObjectModel, objectConfigModel,
				errorMessageModels, user);
	}

	@Override
	public void handleUnknownError(Document doc, ObjectConfigBean objectConfigModel,
								   DataLoadObjectModel dataLoadObjectModel, List<ErrorMessageBean> errorMessageModels, UserLoginBean user)
			throws ServiceException {
		dataLoadObjectModel = new ExcelDataLoadObjectModel();
		this.handleErrors(doc, DMConstants.VALIDATION_TYPE_UNKNOWN, dataLoadObjectModel, null,
				errorMessageModels, user);
	}

	

	@Override
	public void handleErrors(Document doc, String validationType, DataLoadObjectModel dataLoadObjectModel,
							 ObjectConfigBean objectConfigModel, List<ErrorMessageBean> errorModels, UserLoginBean user)
			throws ServiceException {
		this.user = user;
		if (this.doc == null) {
			this.doc = doc;
		}
		dataLoadObjectModel.setBusinessObjectModel(null);
		writeToRootTab(validationType, dataLoadObjectModel.getDataRowsMap(), errorModels,objectConfigModel,  user);

	}

	

	/**
	 * This method is used to create the root sheet from the RowData Model
	 *
	 * @param validationType
	 *            this indicates the it is Business Validation Or the Basic
	 *            Validation
	 * @param dataRowMap
	 *            This contain all the Errors rows in hierarchical corresponding to
	 *            the root row
	 * @param errorModels
	 *            contains the error model if any business error occurred
	 * @param user
	 *            current user
	 * @throws ServiceException
	 *             for any error
	 */
	private void writeToRootTab(String validationType, Map<String, DataRowModel> dataRowMap,
								List<ErrorMessageBean> errorModels, ObjectConfigBean objectConfigModel, UserLoginBean user) throws ServiceException {
		

		List<String> headerColumns = new ArrayList<String>();
		if(ArgumentHelper.isNotEmpty(objectConfigModel.getAttributeConfigModels())) {
			headerColumns = objectConfigModel.getAttributeConfigModels().stream().map(conf->conf.getHeaderName()).collect(Collectors.toList());
		}
		headerColumns.add("Error");
		if (dataRowMap == null || dataRowMap.isEmpty()) {
			if (DMConstants.VALIDATION_TYPE_UNKNOWN.equalsIgnoreCase(validationType) && errorModels != null) {
				String errorMessage = getAllErroMessageASString(errorModels);
				headerColumns.add(errorMessage);
				String[] arr = new String[headerColumns.size()];
				cSVRows.add(headerColumns.toArray(arr));
			}
			return;
		}
		if(ArgumentHelper.isEmpty(cSVRows)) {
			String[] arr = new String[headerColumns.size()];
			cSVRows.add(headerColumns.toArray(arr));
		}
		for (String key : dataRowMap.keySet()) {
			ExcelDataRowModel excelDataRowModel = (ExcelDataRowModel) dataRowMap.get(key);
			List<String> currColumns = new ArrayList<String>();
			String errorMessage = "";
			int colIndex = 0;
			for(DataColumnModel colmn  :excelDataRowModel.getColumns()) {
//				System.out.println("colIndex==>"+colIndex + "<===>"+headerColumns.size());
				if(colIndex<(headerColumns.size()-1)) {
					if(Objects.isNull(colmn.getColValue())) {
						currColumns.add("");
					}else {
						currColumns.add(colmn.getColValue().toString());
					}
					if(Objects.nonNull(colmn.getErrorMessage())) {
						String headerName = objectConfigModel.getAttributeConfigModels().get(colIndex).getHeaderName();
						errorMessage = errorMessage + headerName +":" +colmn.getErrorMessage() + "\n";
					}
				}
				colIndex++;
			}
			
			excelDataRowModel.setKey(key);
			if (DMConstants.VALIDATION_TYPE_BUSINESS.equalsIgnoreCase(validationType) && errorModels != null) {
				errorMessage = errorMessage + getAllErroMessageASString(errorModels);
			}
			
			errorMessage = errorMessage.replace("\"\",", "");
			currColumns.add(errorMessage);
			String[] arr = new String[currColumns.size()];
			cSVRows.add(currColumns.toArray(arr));
		}

	}

	@Override
	public void writeFile(String orgFileName) throws ServiceException {
		orgFileName = orgFileName.replace("xlsx", "csv");
		String fileName = RETRY_CONSTANT + orgFileName;
		File file = null;
		try {
			try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
	            writer.writeAll(cSVRows);
	        } catch (IOException e) {
				e.printStackTrace();
			}
			file = new File(fileName);
			objectService.updateRetryFile(doc.getId(), fileName, file, user);
			
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			if (file != null) {
				file.delete();
			}
		}
	}

	/**
	 * This method return the all the error message in a String
	 *
	 * @param errorModels
	 *            error models.
	 * @return error string.
	 */
	private String getAllErroMessageASString(List<ErrorMessageBean> errorModels) {
		String errorMessage = "";
		for (ErrorMessageBean errorMessageModel : errorModels) {
			errorMessage = errorMessage + errorMessageModel.getMessage() + "\n";
		}
		return errorMessage;
	}

	/**
	 * Gets the fileExt attribute of the FileTransporter object
	 *
	 * @param fileName
	 *            Description of the Parameter
	 * @return The fileExt value
	 */
	protected String getFileExt(String fileName) {
		String value = new String();
		int start = 0;
		int end = 0;
		if (fileName == null) {
			return "";
		}
		start = fileName.lastIndexOf('.') + 1;
		end = fileName.length();
		value = fileName.substring(start, end);
		if (fileName.lastIndexOf('.') > 0) {
			return value;
		} else {
			return "";
		}
	}
}
