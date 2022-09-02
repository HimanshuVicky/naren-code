package com.assignsecurities.dm.handler.excel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.app.util.DMConstants;
import com.assignsecurities.app.util.StringUtil;
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
import com.assignsecurities.domain.dm.ObjectModel;
import com.assignsecurities.domain.dm.excel.ExcelDataColumnModel;
import com.assignsecurities.domain.dm.excel.ExcelDataLoadObjectModel;
import com.assignsecurities.domain.dm.excel.ExcelDataRowModel;



/**
 * This is the excel implementation for handling the error. this will create the
 * retry file by adding the 'retry' string in the file name.
 *
 * This acutely reads the Template for that object and type creates the retry
 * file with the errors. Basic validation error will as cell comments and the
 * business validation errors are consolidated a the last columns of their
 * respective models.
 *
 *
 */
@Service
public class ExcelErrorHandler extends ErrorHandler {
	private ObjectServiceImpl objectService;
	protected @Autowired AutowireCapableBeanFactory beanFactory;
	private CreationHelper createHelper = null;
	private InputStream inp = null;
	private Workbook wb = null;
	private Comment comment = null;
	private Document doc = null;
	private UserLoginBean user = null;
	private int errorColumnIndex = -1;
	private Map<String, Long> sheetCurreRow = new HashMap<String, Long>();
	private static final Logger logger = LogManager.getLogger(ExcelErrorHandler.class);

	public ExcelErrorHandler() {
		if (objectService == null) {
			objectService = new ObjectServiceImpl();
//			beanFactory.autowireBean(objectService);
		}
	}

	public ExcelErrorHandler(ObjectServiceImpl objectService) {

		this.objectService = objectService;
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
			handleChildRows(dataRowModel, validationErrorModels);
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

	/**
	 * This method is used to handle the row with in the rows for excle row date
	 * model.
	 *
	 * @param dataRowModel
	 * @param validationErrorModels
	 */
	private void handleChildRows(DataRowModel dataRowModel, List<ErrorMessageBean> validationErrorModels) {
		if (dataRowModel instanceof ExcelDataRowModel) {
			Map<String, List<ExcelDataRowModel>> dataRowMap = ((ExcelDataRowModel) dataRowModel).getChildRowMap();
			if (dataRowMap != null) {
				for (String key : dataRowMap.keySet()) {
					List<ExcelDataRowModel> dataRowModels = dataRowMap.get(key);
					for (ExcelDataRowModel excelDataRowModel : dataRowModels) {
						if (excelDataRowModel.getColumns() != null) {
							for (DataColumnModel dataColumnModel : excelDataRowModel.getColumns()) {
								if (dataColumnModel.getErrorMessage() != null
										&& !dataColumnModel.getErrorMessage().trim().isEmpty()) {
									ErrorMessageBean errorModel = new ErrorMessageBean();
									errorModel.setMessage(dataColumnModel.getErrorMessage());
									validationErrorModels.add(errorModel);

								}
							}
						}
						if (excelDataRowModel.getChildRowMap() != null) {
							handleChildRows(excelDataRowModel, validationErrorModels);
						}
					}
				}
			}
		}

	}

	@Override
	public void handleErrors(Document doc, String validationType, DataLoadObjectModel dataLoadObjectModel,
							 ObjectConfigBean objectConfigModel, List<ErrorMessageBean> errorModels, UserLoginBean user)
			throws ServiceException {
		this.user = user;
		if (DMConstants.VALIDATION_TYPE_UNKNOWN.equalsIgnoreCase(validationType) && doc != null) {
			ObjectModel objectModel = objectService.getObject(user, doc.getObjectId());
			inp = new ByteArrayInputStream(objectModel.getObjectTemplateModel().getTemplate());

			// TODO
			// inp = FileUploadDAOFactory.getDAO().getFileStream(user,
			// doc.getFileId());
		}
		if (inp == null) {
			ObjectModel objectModel = objectService.getObject(user, doc.getObjectId());
			inp = new ByteArrayInputStream(objectModel.getObjectTemplateModel().getTemplate());
		}
		if (wb == null) {
			try {
				wb = WorkbookFactory.create(inp);
				// setting the workbook to handle null
				wb.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK);
			} catch (InvalidFormatException e) {
				throw new ServiceException(e);
			} catch (IOException e) {
				throw new ServiceException(e);
			}
		}
		if (createHelper == null) {
			createHelper = wb.getCreationHelper();
		}
		if (this.doc == null) {
			this.doc = doc;
		}
		if (objectConfigModel != null) {
			errorColumnIndex = objectConfigModel.getErrorColumnIndex();
		}
		dataLoadObjectModel.setBusinessObjectModel(null);
		writeToRootTab(validationType, dataLoadObjectModel.getDataRowsMap(), errorModels, user);

	}

	@Override
	public void writeFile(String orgFileName) throws ServiceException {
		FileOutputStream fileOut = null;
		String fileName = RETRY_CONSTANT + orgFileName;
		File file = null;
		try {
			fileOut = new FileOutputStream(fileName);
			wb.write(fileOut);
			fileOut.close();
			String inputfileName = fileName;
			// Save file in SMP_FILE_STORE
			file = new File(fileName);
			// int reTryFileId = upload(inputfileName, file, "xlsx", user);
			// ObjectBean objectModel = new ObjectBean();
			// objectModel.setId(doc.getId());
			// objectModel.setReTryFileId(reTryFileId);
			objectService.updateRetryFile(doc.getId(), inputfileName, file, user);
		} catch (FileNotFoundException e) {
			throw new ServiceException(e);
		} catch (IOException e) {
			throw new ServiceException(e);
		} finally {
			try {
				if (fileOut != null) {
					fileOut.close();
				}
			} catch (IOException e) {
				// ignoure exception
			}

			if (file != null) {
				file.delete();
			}
		}
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
								List<ErrorMessageBean> errorModels, UserLoginBean user) throws ServiceException {

		if (dataRowMap == null || dataRowMap.isEmpty()) {
			if (DMConstants.VALIDATION_TYPE_UNKNOWN.equalsIgnoreCase(validationType) && errorModels != null) {

				Sheet sheet = wb.getSheetAt(1);
				String sheetName = sheet.getSheetName();
				Row currentRow = sheet.createRow(4);
				int errorCellNo = currentRow.getLastCellNum() + 1;
				String errorMessage = "Business Validation Messages";
				errorMessage = getAllErroMessageASString(errorModels);
				Cell currentCell = currentRow.createCell(errorCellNo);
				CellStyle cs = wb.createCellStyle();
				cs.setWrapText(true);
				currentCell.setCellStyle(cs);
				currentCell.setCellValue(errorMessage);
			}
			return;
		}
		int curRow = -1;
		for (String key : dataRowMap.keySet()) {
			ExcelDataRowModel excelDataRowModel = (ExcelDataRowModel) dataRowMap.get(key);
			Sheet sheet = wb.getSheetAt(excelDataRowModel.getTabIndex() - 1);
			String sheetName = sheet.getSheetName();
			if (curRow == -1) {
				if (sheetCurreRow.containsKey(sheetName)) {
					curRow = ((Long) sheetCurreRow.get(sheetName)).intValue() + 1;
				} else {
					curRow = excelDataRowModel.getHeaderRowNumber();
					setErrColHeader(excelDataRowModel, sheet);
				}
				deleteInValidRows(curRow, sheet);
			}

			Row currentRow = sheet.createRow(curRow);

			List<DataColumnModel> cols = excelDataRowModel.getColumns();
			for (DataColumnModel dataColumnModel : cols) {
				ExcelDataColumnModel excelDataColumnModel = (ExcelDataColumnModel) dataColumnModel;
				Cell currentCell = currentRow.createCell(excelDataColumnModel.getColIndex() - 1);
				CellStyle cs = wb.createCellStyle();
				cs.setWrapText(true);
				cs.setBorderBottom((short) 1);
				cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());
				cs.setBorderRight((short) 1);
				cs.setRightBorderColor(IndexedColors.BLACK.getIndex());
				currentCell.setCellStyle(cs);
				setCellValue(excelDataColumnModel, currentCell);
				setCellError(excelDataColumnModel, currentCell, null);
			}
			if (DMConstants.VALIDATION_TYPE_BUSINESS.equalsIgnoreCase(validationType) && errorModels != null) {
				int errorCellNo = currentRow.getLastCellNum() + 1;
				if (errorColumnIndex != -1) {
					errorCellNo = errorColumnIndex + 1;
				}
				String errorMessage = "Business Validation Messages";
				errorMessage = getAllErroMessageASString(errorModels);
				Cell currentCell = currentRow.createCell(errorCellNo);
				setErrorCellStyle(currentCell);
				currentCell.setCellValue(errorMessage);
			}
			Map<String, List<ExcelDataRowModel>> childRowMap = excelDataRowModel.getChildRowMap();
			if (childRowMap != null && !childRowMap.isEmpty()) {
				writeToTab(validationType, childRowMap, user);
			}
			sheetCurreRow.put(sheetName, new Long(curRow));
			curRow++;
		}

	}

	/**
	 * @param currentCell
	 */
	private void setErrorCellStyle(Cell currentCell) {
		CellStyle cs = wb.createCellStyle();
		cs.setWrapText(true);
		cs.setFillForegroundColor(HSSFColor.RED.index);
		Font font = wb.createFont();
		font.setColor(HSSFColor.RED.index);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		cs.setFont(font);
		cs.setBorderBottom((short) 1);
		cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		cs.setBorderRight((short) 1);
		cs.setRightBorderColor(IndexedColors.BLACK.getIndex());
		currentCell.setCellStyle(cs);
	}

	private void setErrColHeader(ExcelDataRowModel excelDataRowModel, Sheet sheet) {
		int headeRowNumber = excelDataRowModel.getHeaderRowNumber();
		Row currentRow = sheet.getRow(headeRowNumber - 2);
		int errorCellNo = currentRow.getLastCellNum() + 1;
		if (errorColumnIndex != -1) {
			errorCellNo = errorColumnIndex + 1;
		}
		Cell currentCell = currentRow.createCell(errorCellNo);
		CellStyle cs = wb.createCellStyle();
		cs.setWrapText(true);
		Font font = wb.createFont();
		font.setColor(HSSFColor.RED.index);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		cs.setFont(font);
		cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cs.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		currentCell.setCellStyle(cs);
		currentCell.setCellValue("Error");
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
	 * this method is used to create row and the columns for the child
	 * sheet(supports hierarchical).
	 *
	 * @param validationType
	 *            this indicates the it is Business Validation Or the Basic
	 *            Validation
	 * @param childRowMap
	 *            This contain all the Errors rows in hierarchical corresponding to
	 *            the root row
	 * @param user
	 *            user auth model
	 */
	private void writeToTab(String validationType, Map<String, List<ExcelDataRowModel>> childRowMap,
							UserLoginBean user) {
		String key = null;
		for (String compKey : childRowMap.keySet()) {
			logger.debug("Child Row Key -->" + compKey);
			int curRow = -1;
			key = compKey.substring(compKey.lastIndexOf(":") + 1);
			logger.debug("Child Tab Index -->" + key);
			List<ExcelDataRowModel> dataRowModels = childRowMap.get(compKey);
			Collections.sort(dataRowModels);
			Sheet sheet = null;
			if (dataRowModels != null) {
				for (ExcelDataRowModel excelDataRowModel : dataRowModels) {
					if (null == sheet) {
						sheet = wb.getSheetAt(excelDataRowModel.getTabIndex() - 1);
					}
					String sheetName = sheet.getSheetName();
					if (curRow == -1) {
						if (sheetCurreRow.containsKey(sheetName)) {
							curRow = ((Long) sheetCurreRow.get(sheetName)).intValue() + 1;
						} else {
							// AS row writing is starting from the row six so
							// reducing it
							curRow = excelDataRowModel.getHeaderRowNumber();
						}
						deleteInValidRows(curRow, sheet);
					}
					Row currentRow = sheet.createRow(curRow);
					List<DataColumnModel> cols = excelDataRowModel.getColumns();
					logger.debug(sheet.getSheetName() + "    curRow -->" + curRow);
					for (DataColumnModel dataColumnModel : cols) {
						ExcelDataColumnModel excelDataColumnModel = (ExcelDataColumnModel) dataColumnModel;
						logger.debug("excelDataColumnModel.getColIndex() -->" + excelDataColumnModel.getColIndex()
								+ "  ColValue " + excelDataColumnModel.getColValue());
						Cell currentCell = currentRow.createCell(excelDataColumnModel.getColIndex() - 1);
						CellStyle cs = wb.createCellStyle();
						cs.setWrapText(true);
						cs.setBorderBottom((short) 1);
						cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());
						cs.setBorderRight((short) 1);
						cs.setRightBorderColor(IndexedColors.BLACK.getIndex());
						currentCell.setCellStyle(cs);
						setCellValue(excelDataColumnModel, currentCell);
						setCellError(excelDataColumnModel, currentCell, null);
					}

					Map<String, List<ExcelDataRowModel>> childRowMap1 = excelDataRowModel.getChildRowMap();
					if (childRowMap1 != null && !childRowMap1.isEmpty()) {
						writeToTab(validationType, childRowMap1, user);
					}
					sheetCurreRow.put(sheetName, new Long(curRow));
					curRow++;
				}
			}
		}
	}

	/**
	 * This method is used to deleted invalid row from the Template.
	 *
	 * @param curRow
	 *            index or row number of the first row after the header.
	 * @param sheet
	 *            sheet from where need to delete the row
	 */
	private void deleteInValidRows(int curRow, Sheet sheet) {
		int lastRowNo = sheet.getLastRowNum();
		for (int rowCounter = curRow; rowCounter <= lastRowNo; rowCounter++) {
			if (sheet.getRow(rowCounter) != null) {
				sheet.removeRow(sheet.getRow(rowCounter));
			}
		}

	}

	/**
	 *
	 * @param excelDataColumnModel
	 * @param currentCell
	 */
	private void setCellError(ExcelDataColumnModel excelDataColumnModel, Cell currentCell, Drawing drawing1) {
		if (StringUtil.isValidString(excelDataColumnModel.getErrorMessage())) {
			Drawing drawing = currentCell.getSheet().createDrawingPatriarch();
			ClientAnchor anchor = createHelper.createClientAnchor();
			anchor.setCol1(currentCell.getColumnIndex());
			anchor.setCol2(currentCell.getColumnIndex() + 1);
			anchor.setRow1(currentCell.getRowIndex());
			anchor.setRow2(currentCell.getRowIndex() + 1);
			comment = drawing.createCellComment(anchor);
			RichTextString str1 = createHelper.createRichTextString(excelDataColumnModel.getErrorMessage());
			comment.setString(str1);
			comment.setAuthor("");
			currentCell.setCellComment(comment);
		}
	}

	/**
	 *
	 * @param excelDataColumnModel
	 * @param currentCell
	 */
	private void setCellValue(ExcelDataColumnModel excelDataColumnModel, Cell currentCell) {
		Object cellValue = excelDataColumnModel.getColValue();
		if (cellValue instanceof Number) {
			currentCell.setCellValue(Double.valueOf(cellValue.toString()));
		} else if (excelDataColumnModel.getColValue() instanceof String) {
			currentCell.setCellValue(cellValue.toString());
		} else if (excelDataColumnModel.getColValue() instanceof Boolean) {
			currentCell.setCellValue(Boolean.valueOf(cellValue.toString()));
		} else if (excelDataColumnModel.getColValue() instanceof Date) {
			currentCell.setCellValue((Date) cellValue);
		} else {
			currentCell.setCellValue(cellValue.toString());
		}
	}

	public static void main(String[] args) {
		ObjectServiceImpl objectService = new ObjectServiceImpl();
		ExcelErrorHandler excelErrorHandler = new ExcelErrorHandler(objectService);
		Document doc = new Document();
		doc.setFileName("C:\\SP\\9.6\\selfsufficiency\\30_CSM_MA.xlsx");
		String validationType = "Basic";
		DataLoadObjectModel dataLoadObjectModel = new ExcelDataLoadObjectModel();
		ObjectConfigBean objectConfigModel = new ObjectConfigBean();
		List<ErrorMessageBean> errorModels = new ArrayList<ErrorMessageBean>();

		ExcelDataRowModel maDataRowModel = new ExcelDataRowModel();
		dataLoadObjectModel.addDataRow("Row1code", maDataRowModel);
		// dataLoadObjectModel.addDataRow("Row2code", maDataRowModel);

		DataColumnModel dataColumnModel = new ExcelDataColumnModel();
		dataColumnModel.setColIndex(0);
		dataColumnModel.setColName("masterAgreementCode");
		dataColumnModel.setColValue("CARGAN_SERVICE_CORPORATION");
		// dataColumnModel.setErrorMessage("Invalid Code");
		maDataRowModel.addColumns(dataColumnModel);
		maDataRowModel.setRowIndex(2);
		maDataRowModel.setHeaderRowNumber(1);
		maDataRowModel.setTabIndex(1);
		dataColumnModel = new ExcelDataColumnModel();
		dataColumnModel.setColIndex(1);
		dataColumnModel.setColName("masterAgreementName");
		dataColumnModel.setColValue("Cargan Service Corporation");
		// dataColumnModel.setErrorMessage("Invalid Code");
		maDataRowModel.addColumns(dataColumnModel);
		maDataRowModel.setRowIndex(2);
		maDataRowModel.setHeaderRowNumber(1);

		// MACategory#
		// Row1
		List<ExcelDataRowModel> maCategoryChildRowDataModel = new ArrayList<ExcelDataRowModel>();
		ExcelDataRowModel catDataRowModel = new ExcelDataRowModel();
		catDataRowModel.setTabIndex(2);
		dataColumnModel = new ExcelDataColumnModel();
		dataColumnModel.setColIndex(0);
		dataColumnModel.setColName("masterAgreementCode");
		dataColumnModel.setColValue("CARGAN_SERVICE_CORPORATION");
		catDataRowModel.addColumns(dataColumnModel);
		catDataRowModel.setRowIndex(2);
		catDataRowModel.setHeaderRowNumber(1);
		dataColumnModel = new ExcelDataColumnModel();
		dataColumnModel.setColIndex(1);
		dataColumnModel.setColName("categoryCode");
		dataColumnModel.setColValue("TECHNICAL123");
		dataColumnModel.setErrorMessage("Invalid Code");
		catDataRowModel.addColumns(dataColumnModel);
		catDataRowModel.setRowIndex(2);
		catDataRowModel.setHeaderRowNumber(1);
		maCategoryChildRowDataModel.add(catDataRowModel);
		// Row2
		catDataRowModel = new ExcelDataRowModel();
		dataColumnModel = new ExcelDataColumnModel();
		dataColumnModel.setColIndex(0);
		dataColumnModel.setColName("masterAgreementCode");
		dataColumnModel.setColValue("CARGAN_SERVICE_CORPORATION");
		catDataRowModel.addColumns(dataColumnModel);
		catDataRowModel.setRowIndex(3);
		catDataRowModel.setHeaderRowNumber(1);
		dataColumnModel = new ExcelDataColumnModel();
		dataColumnModel.setColIndex(1);
		dataColumnModel.setColName("categoryCode");
		dataColumnModel.setColValue("TECHNICAL_ADMIN");
		// dataColumnModel.setErrorMessage("Invalid Code");
		catDataRowModel.addColumns(dataColumnModel);
		catDataRowModel.setRowIndex(3);
		catDataRowModel.setHeaderRowNumber(1);
		maCategoryChildRowDataModel.add(catDataRowModel);

		maDataRowModel.addChildRows(":2", maCategoryChildRowDataModel);

		// MAOrganisation#
		// Row1
		List<ExcelDataRowModel> maORGChildRowDataModel = new ArrayList<ExcelDataRowModel>();
		ExcelDataRowModel orgDataRowModel = new ExcelDataRowModel();
		orgDataRowModel.setTabIndex(3);
		dataColumnModel = new ExcelDataColumnModel();
		dataColumnModel.setColIndex(0);
		dataColumnModel.setColName("masterAgreementCode");
		dataColumnModel.setColValue("CARGAN_SERVICE_CORPORATION");
		orgDataRowModel.addColumns(dataColumnModel);
		orgDataRowModel.setRowIndex(2);
		orgDataRowModel.setHeaderRowNumber(1);
		dataColumnModel = new ExcelDataColumnModel();
		dataColumnModel.setColIndex(1);
		dataColumnModel.setColName("ORG_ADMIN");
		dataColumnModel.setColValue("ORG123");
		dataColumnModel.setErrorMessage("Invalid Code row1");
		orgDataRowModel.addColumns(dataColumnModel);
		orgDataRowModel.setRowIndex(2);
		orgDataRowModel.setHeaderRowNumber(1);
		maORGChildRowDataModel.add(orgDataRowModel);
		// Row2
		orgDataRowModel = new ExcelDataRowModel();

		dataColumnModel = new ExcelDataColumnModel();
		dataColumnModel.setColIndex(0);
		dataColumnModel.setColName("masterAgreementCode");
		dataColumnModel.setColValue("CARGAN_SERVICE_CORPORATION");
		orgDataRowModel.addColumns(dataColumnModel);
		orgDataRowModel.setRowIndex(3);
		orgDataRowModel.setHeaderRowNumber(1);
		dataColumnModel = new ExcelDataColumnModel();
		dataColumnModel.setColIndex(1);
		dataColumnModel.setColName("organizationCode");
		dataColumnModel.setColValue("ORG_ADMIN");
		dataColumnModel.setErrorMessage("Invalid Code row2");
		orgDataRowModel.addColumns(dataColumnModel);
		orgDataRowModel.setHeaderRowNumber(1);
		maORGChildRowDataModel.add(orgDataRowModel);
		maDataRowModel.addChildRows(":3", maORGChildRowDataModel);
		try {
			excelErrorHandler.handleErrors(doc, validationType, dataLoadObjectModel, objectConfigModel, errorModels,
					null);
			validationType = "Business";
			ErrorMessageBean errorMessageModel = new ErrorMessageBean();
			errorMessageModel.setMessage("Invalid object Message1");
			errorModels.add(errorMessageModel);

			errorMessageModel = new ErrorMessageBean();
			errorMessageModel.setMessage("Invalid object Message2");
			errorModels.add(errorMessageModel);

			errorMessageModel = new ErrorMessageBean();
			errorMessageModel.setMessage("Invalid object Message3");
			errorModels.add(errorMessageModel);
			excelErrorHandler.handleErrors(doc, validationType, dataLoadObjectModel, objectConfigModel, errorModels,
					null);
			excelErrorHandler.writeFile("30_CSM_MA.xlsx");
		} catch (ServiceException e) {
			e.printStackTrace();
		}
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

