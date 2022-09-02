package com.assignsecurities.dm.writer.excel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.app.util.*;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.dm.MetaDataConfigService;
import com.assignsecurities.dm.MetaDataConfigServiceImpl;
import com.assignsecurities.dm.writer.DownLoadDataWriter;
import com.assignsecurities.domain.dm.*;
import com.assignsecurities.domain.dm.excel.ExcelDataRowModel;
import com.assignsecurities.domain.dm.excel.ExcelDataWriterModel;




/**
 * Responsible to write data in xml form.
 * @author pkumar
 *
 */
public class ExcelDataWriter extends DownLoadDataWriter {

	private CreationHelper createHelper = null;
	private final HashMap<String, Integer> sheetRowCountMap = new HashMap<String, Integer>();
	private CellStyle cs = null;
	private SXSSFSheet sheet =null;
	private static final Logger logger = LogManager.getLogger(ExcelDataWriter.class);
	/**
	 * Default constructor
	 */
	public ExcelDataWriter(ResourceBundleUtil resourceBundleUtil) {
		try {
			if (dateFormator == null) {
				dateFormator = DateFormatterUtil.getDateFormatForLocal(
						"dd-MMM-yyyy", new Locale("en_US"),resourceBundleUtil);
			}
		} catch (Exception pex) {
			
			throw new RuntimeException(pex);
		}
	}

	/**
	 * 
	 */
	public void writeDataToFile(Object obj, ObjectModel objectModel,
								ObjectConfigBean objectConfigModel, ObjectBean objectbean,
								DataWriterModel dataWriterModel, int rowCount, boolean isMainSheet,
								UserLoginBean user) throws ServiceException {
		// System.out.println("processing Record :" + rowCount);
		ExcelDataWriterModel excelDataWriterModel = (ExcelDataWriterModel) dataWriterModel;
//		try {
			if (excelDataWriterModel.getExcelWorkBook() == null) {
				InputStream ios =new ByteArrayInputStream( objectModel.getObjectTemplateModel().getTemplate());
//				try {
//					ObjectTemplateModel objectTemplateModel = ObjectFactory
//							.get()
//							.getObjectTemplateModel(
//									objectConfigModel.getId(),
//									SelfSufficiencyConstants.DOCUMENT_TYPE_FORMAT_XLSX);
//					excelDataWriterModel.initDataWriterObject(
//							objectConfigModel, FileUploadDAOFactory.getDAO()
//									.getFileStream(user,
//											objectTemplateModel.getFileID()));
//
//				} catch (InvalidFormatException e) {
//					throw new ServiceException(e);
//				}
			}
			SXSSFWorkbook wb = (SXSSFWorkbook) excelDataWriterModel.getExcelWorkBook();
			if (cs == null) {
				cs = wb.createCellStyle();
				Font font2 = wb.createFont();
				font2.setFontHeightInPoints((short) 10);
				font2.setFontName("Arial");
				font2.setColor(IndexedColors.BLACK.getIndex());

				cs.setWrapText(true);
				cs.setBorderBottom((short) 1);
				cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());
				cs.setBorderRight((short) 1);
				cs.setRightBorderColor(IndexedColors.BLACK.getIndex());
				cs.setFont(font2);
			}
			updateWorksSheet(obj, objectConfigModel, objectbean, null, wb,
					rowCount, isMainSheet, user);

//		} catch (IOException e) {
//			throw new ServiceException(e);
//		}

	}

	/**
	 * update excel worksheet with data
	 * 
	 * @param obj
	 * @param objectConfigModel
	 * @param objectModel
	 * @param wb
	 * @param rowCount
	 * @param isMainSheet
	 * @param user
	 * @throws ServiceException
	 */
	public void updateWorksSheet(Object obj,
			ObjectConfigBean objectConfigModel, ObjectBean objectModel,
			List<Object> parentRefKeys, SXSSFWorkbook wb, int rowCount,
			boolean isMainSheet, UserLoginBean user) throws ServiceException {
		wb.setCompressTempFiles(true); // temp files will be gzipped
		int tabIndex = objectConfigModel.getTabIndex();
		int colCount = -1;
		try{
		sheet = (SXSSFSheet)wb.getSheetAt(tabIndex - 1);
		sheet.setRandomAccessWindowSize(100);// keep 100 rows in memory, exceeding
		
		// rows will be flushed to disk
		int currentRowIndex = rowCount;
		if (sheetRowCountMap.containsKey(String.valueOf(tabIndex))) {
			currentRowIndex = Integer.valueOf(sheetRowCountMap.get(String
					.valueOf(tabIndex))) + 1;
			sheetRowCountMap.put(String.valueOf(tabIndex), new Integer(
					currentRowIndex));
		} else {
			currentRowIndex = objectConfigModel.getDataStartRowNo() - 1;
			sheetRowCountMap.put(String.valueOf(tabIndex), new Integer(
					currentRowIndex));
		}
		if (obj == null) {
			return;
		}
		Row row = null;
		try{
			row = sheet.createRow(currentRowIndex);
		}catch(IllegalArgumentException e1){
			ServiceException proservException= new ServiceException(e1);
			proservException.setExceptionCode(ServiceException.EXCEL_MAX_ROW_LIMIT_EXCEED);
			throw proservException;
		}
		
		List<AttributeConfigBean> attributeConfigModels = objectConfigModel
				.getAttributeConfigModels();
		if (attributeConfigModels == null || attributeConfigModels.isEmpty()) {
			return;
		}
		List<Object> primaryKeys = new ArrayList<Object>();

		List<Integer> parentKeyIndex = objectConfigModel.getParentRefKeyIndex();
		List<Integer> primaryKeyIndex = objectConfigModel.getPrimaryKeyIndex();
		int keyCount = 0;
		for (AttributeConfigBean attributeConfigModel : attributeConfigModels) {
			colCount++;
			if (!attributeConfigModel.isAttributeAvailableInBo()) {
				String keyValue = "";
				if (parentKeyIndex.contains(Integer.valueOf(colCount + 1))) {
					if (parentRefKeys != null
							&& parentRefKeys.size() > keyCount) {
						keyValue = String.valueOf(parentRefKeys.get(keyCount)
								.toString());
					}
					keyCount++;
				}
				if (isMainSheet
						&& "ACTION".equalsIgnoreCase(attributeConfigModel
								.getHeaderName())
						&& attributeConfigModel.getSequenceNo() == 0) {
					keyValue = "No Action";
				}
				if (primaryKeyIndex.contains(Integer.valueOf(colCount + 1))
						&& parentRefKeys != null
						&& parentRefKeys.size() > colCount) {
					primaryKeys.add(parentRefKeys
							.get(Integer.valueOf(colCount)));
				}
				Cell currentCell = row.createCell(colCount);
				if(attributeConfigModel.getDataType().equalsIgnoreCase("java.util.Date")){
					keyValue = getFormatedValueForDate(attributeConfigModel, keyValue);
				}else{
					keyValue = getFormatedValue(attributeConfigModel, keyValue);
				}
				currentCell.setCellValue(keyValue);
				currentCell.setCellType(Cell.CELL_TYPE_STRING);
				currentCell.setCellStyle(cs);
//				String address = new CellReference(currentCell).formatAsString();
//				cell.setCellValue(address);
				continue;
			}
			Object value = getValueFromModel(attributeConfigModel, obj,
					objectConfigModel);
			value=getFinalValueForPremitiveDataType(attributeConfigModel, value,
					objectConfigModel);
			if (primaryKeyIndex.contains(Integer.valueOf(colCount))) {
				primaryKeys.add((value != null) ? value.toString() : "");
			} else if (primaryKeyIndex.contains(Integer.valueOf(colCount + 1))) {
				primaryKeys.add((value != null) ? value.toString() : "");
			}
			if (attributeConfigModel.getChildObjectId() != null
					&& attributeConfigModel.getChildObjectId() != 0
					&& value != null) {
				updateChildRecords(objectModel, wb, rowCount, user,
						primaryKeys, attributeConfigModel, value);

			} else {
				value = getAttributeValue(user, attributeConfigModel, value);

				Cell currentCell = row.createCell(colCount);
				if (value != null) {
					value = getFormatedValue(attributeConfigModel, value
							.toString());
					currentCell.setCellValue(value.toString());
					currentCell.setCellType(Cell.CELL_TYPE_STRING);
					currentCell.setCellStyle(cs);
				} else {
					currentCell.setCellValue("");
					currentCell.setCellType(Cell.CELL_TYPE_STRING);
					currentCell.setCellStyle(cs);
				}
			}
		}
		}finally{
			//sheet.s
			//sheet=null;
		}
	}

	/**
	 * 
	 * @param attributeConfigModel
	 * @param keyValue
	 * @return
	 * @throws ServiceException
	 */
	private String getFormatedValueForDate(
			AttributeConfigBean attributeConfigModel, String keyValue)
			throws ServiceException {
		String valueObject = keyValue;
		if (StringUtil.isValidString(keyValue.toString())) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			String formatString = DateUtil.getDateFormat(keyValue);
			Date date = null;

			SimpleDateFormat formatter1 = new SimpleDateFormat(formatString);
			try {
				date = (Date) formatter1.parse(keyValue);
			} catch (ParseException e1) {
				throw new ServiceException(e1);
			}
			valueObject = dateFormat.format(date);
		}
		return valueObject;
	}

	/**
	 * 
	 * @param attributeConfigModel
	 * @param value
	 * @param objectConfigModel
	 * @return
	 */
	private Object getFinalValueForPremitiveDataType(
			AttributeConfigBean attributeConfigModel, Object value,
			ObjectConfigBean objectConfigModel) {
		String attClassName = attributeConfigModel.getDataType();
		Double doubleValue= -11d;
		try {
			doubleValue = Double.parseDouble(String.valueOf(value));
		} catch (NumberFormatException e1) {
			// ignore
			doubleValue = -11d;
		}
		boolean idZero=  doubleValue.doubleValue()==0d && !attributeConfigModel.isRequired();
		if (("int".equals(attClassName)
				|| "java.lang.Integer".equals(attClassName)) && idZero) {
			value="";
		} else if (("double".equals(attClassName)
				|| "java.lang.Double".equals(attClassName)) && idZero) {
			value="";
		} else if (("long".equals(attClassName)
				|| "java.lang.Long".equals(attClassName)) && idZero) {
			value="";
		} else if (("float".equals(attClassName)
				|| "java.lang.Float".equals(attClassName)) && idZero) {
			value="";
		} else if (("short".equals(attClassName)
				|| "java.lang.Short".equals(attClassName)) && idZero) {
			value="";
		}
		return value;
	}

	/**
	 * update workbook with child sheet data
	 * 
	 * @param objectModel
	 * @param wb
	 * @param rowCount
	 * @param user
	 * @param primaryKeys
	 * @param attributeConfigModel
	 * @param value
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	private void updateChildRecords(ObjectBean objectModel, SXSSFWorkbook wb,
			int rowCount, UserLoginBean user, List<Object> primaryKeys,
			AttributeConfigBean attributeConfigModel, Object value)
			throws ServiceException {
		ObjectConfigBean objectConfigModelChild = getMetaDataConfigService()
				.getSpecificObjectMetaDataFromCache(
						attributeConfigModel.getChildObjectId(), user);

		if (DMConstants.ARRAY_TYPE_OBJECT
				.equalsIgnoreCase(objectConfigModelChild.getObjectType())) {
			for (Object vObject : Arrays.asList(value)) {
				if (vObject != null) {
					updateWorksSheet(vObject, objectConfigModelChild,
							objectModel, primaryKeys, wb, rowCount, false, user);
				}
			}
		} else if (DMConstants.LIST_TYPE_OBJECT
				.equalsIgnoreCase(objectConfigModelChild.getObjectType())) {
			for (Object vObject : (List) value) {
				if (vObject != null) {
					updateWorksSheet(vObject, objectConfigModelChild,
							objectModel, primaryKeys, wb, rowCount, false, user);
				}
			}
		} else if (DMConstants.MAP_TYPE_OBJECT
				.equalsIgnoreCase(objectConfigModelChild.getObjectType())) {
			// TODO need to implement this
		} else if (DMConstants.SET_TYPE_OBJECT
				.equalsIgnoreCase(objectConfigModelChild.getObjectType())) {
			for (Object vObject : (Set) value) {
				if (vObject != null) {
					updateWorksSheet(vObject, objectConfigModelChild,
							objectModel, primaryKeys, wb, rowCount, false, user);
				}
			}
		} else if (DMConstants.VECTOR_TYPE_OBJECT
				.equalsIgnoreCase(objectConfigModelChild.getObjectType())) {
			for (Object vObject : (Vector) value) {
				if (vObject != null) {
					updateWorksSheet(vObject, objectConfigModelChild,
							objectModel, primaryKeys, wb, rowCount, false, user);
				}
			}
		} else {
			updateWorksSheet(value, objectConfigModelChild, objectModel,
					primaryKeys, wb, rowCount, false, user);
		}
	}

	/**
	 * This method creates the Excel Workbook file.
	 * 
	 *            <String, List>referenceMap- This map contains the populated
	 *            values for all model String represents the sheetName or model
	 *            name. list - Is the db list of that generic model.
	 * @throws IOException
	 * */
	public File generateFile(String inputfileName, UserLoginBean uam,
			ObjectBean objectModel, DataWriterModel dataWriterModel)
			throws ServiceException, IOException {
		logger.info("DataLoadHelper****generateSheetData() called ..");
		ExcelDataWriterModel excelDataWriterModel = (ExcelDataWriterModel) dataWriterModel;
		SXSSFWorkbook wb = (SXSSFWorkbook) excelDataWriterModel.getExcelWorkBook();
		FileOutputStream fileOut = null;
		File file = null;
		try {
			String fileName=this.fileName+ ".xlsx";
			fileOut = new FileOutputStream(fileName);
			wb.write(fileOut);
			fileOut.close();

			// Save file in SMP_FILE_STORE
			file = new File(fileName);
//			Long fileId = upload(inputfileName, file, "xlsx",objectModel, uam);
//			objectModel.setFileId(fileId);
			objectModel.setFileName(inputfileName);

		} finally {
			wb=null;
			excelDataWriterModel.setExcelWorkBook(null);
			excelDataWriterModel.setDataWriterObject(null);
			try {
				if (fileOut != null) {
					fileOut.close();
				}
			} catch (IOException e) {
				// ignoure exception
			}

//			if (file != null) {
//				file.delete();
//			}
		}
		return file;

	}

	

	/**
	 * This method is used to handle the row with in the rows for excle row date
	 * model.
	 * 
	 * @param dataRowModel
	 * @param validationErrorModels
	 */
	@SuppressWarnings("unused")
	private void handleChildRows(DataRowModel dataRowModel,
			List<ErrorMessageBean> validationErrorModels) {
		if (dataRowModel instanceof ExcelDataRowModel) {
			Map<String, List<ExcelDataRowModel>> dataRowMap = ((ExcelDataRowModel) dataRowModel)
					.getChildRowMap();
			for (String key : dataRowMap.keySet()) {
				List<ExcelDataRowModel> dataRowModels = dataRowMap.get(key);
				for (ExcelDataRowModel excelDataRowModel : dataRowModels) {
					if (excelDataRowModel.getColumns() != null) {
						for (DataColumnModel dataColumnModel : excelDataRowModel
								.getColumns()) {
							if (dataColumnModel.getErrorMessage() != null
									&& !dataColumnModel.getErrorMessage()
											.trim().isEmpty()) {
								ErrorMessageBean errorModel = new ErrorMessageBean();
								errorModel.setMessage(dataColumnModel
										.getErrorMessage());
								validationErrorModels.add(errorModel);

							}
						}
					}
					if (excelDataRowModel.getChildRowMap() != null) {
						handleChildRows(excelDataRowModel,
								validationErrorModels);
					}
				}
			}
		}

	}

	private MetaDataConfigService getMetaDataConfigService() {
		return new MetaDataConfigServiceImpl();
	}

	public CreationHelper getCreateHelper() {
		return createHelper;
	}

	public void setCreateHelper(CreationHelper createHelper) {
		this.createHelper = createHelper;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
