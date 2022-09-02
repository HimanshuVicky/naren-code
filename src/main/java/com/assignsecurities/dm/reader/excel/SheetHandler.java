package com.assignsecurities.dm.reader.excel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.app.util.*;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.dm.PropertyMapperService;
import com.assignsecurities.dm.reader.validator.BasicValidator;
import com.assignsecurities.domain.dm.AttributeConfigBean;
import com.assignsecurities.domain.dm.ObjectConfigBean;
import com.assignsecurities.domain.dm.excel.ExcelDataColumnModel;
import com.assignsecurities.domain.dm.excel.ExcelDataRowModel;

public class SheetHandler extends DefaultHandler {
	public enum xssfDataType {
		BOOL, ERROR, FORMULA, INLINESTR, SSTINDEX, NUMBER,
	}

	private StylesTable stylesTable;
	private BasicValidator basicValidator;
	private xssfDataType nextDataType;
	private short formatIndex;
	private String formatString;
	private final DataFormatter formatter = new DataFormatter();

	AttributeConfigBean parentAttributeConfigModel;
	private Map<String, ExcelDataRowModel> parentDataRowModels;
	private SharedStringsTable sst;
	private String lastContents;
	private String orgLastContents;
	private boolean nextIsString;
	private List<AttributeConfigBean> attributeConfigModels;
	private Map<String, AttributeConfigBean> attributeConfigModelsMap;
	private List<Object> boModels;
	private ExcelDataRowModel dataRowModel;
	private List<String> excelDataRowKeys = new ArrayList<String>();
	private Map<String, ExcelDataRowModel> inputMetaDataMap = new LinkedHashMap<String, ExcelDataRowModel>();
	private String sheetName;
	private String keyPrefix;
	private String boClassName;
	private int countrows = 0;
	private int countColumn = 0;
	private int dataStartRow = 2;
	private Object boObject;
	private ObjectConfigBean objDefModels;
	private boolean isError = false;
	private boolean isEmptyCell = false;
	private boolean isMainSheet = false;
	String rowKey;
	private UserLoginBean uam;
	private String currentColRef = "A";
	private StringBuilder value = new StringBuilder();

	private static final Logger logger = LogManager.getLogger(SheetHandler.class);

	public SharedStringsTable getSst() {
		return sst;
	}

	public void setSst(SharedStringsTable sst) {
		this.sst = sst;
	}

	public String getLastContents() {
		return lastContents;
	}

	public void setLastContents(String lastContents) {
		this.lastContents = lastContents;
	}

	public boolean isNextIsString() {
		return nextIsString;
	}

	public void setNextIsString(boolean nextIsString) {
		this.nextIsString = nextIsString;
	}

	public List<Object> getBoModels() {
		return boModels;
	}

	public void setBoModels(List<Object> boModels) {
		this.boModels = boModels;
	}

	public Map<String, ExcelDataRowModel> getInputMetaDataMap() {
		return inputMetaDataMap;
	}

	public void setInputMetaDataMap(Map<String, ExcelDataRowModel> inputMetaDataMap) {
		this.inputMetaDataMap = inputMetaDataMap;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public String getKeyPrefix() {
		return keyPrefix;
	}

	public void setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}

	public String getBoClassName() {
		return boClassName;
	}

	public void setBoClassName(String boClassName) {
		this.boClassName = boClassName;
	}

	public int getCountrows() {
		return countrows;
	}

	public void setCountrows(int countrows) {
		this.countrows = countrows;
	}

	public int getCountColumn() {
		return countColumn;
	}

	public void setCountColumn(int countColumn) {
		this.countColumn = countColumn;
	}

	public Object getBoObject() {
		return boObject;
	}

	public void setBoObject(Object boObject) {
		this.boObject = boObject;
	}

	public StringBuilder getValue() {
		return value;
	}

	public void setValue(StringBuilder value) {
		this.value = value;
	}

	/**
	 * 
	 * @param styles
	 * @param sst
	 * @param objectConfigModel
	 * @param parentDataRowModels
	 * @param parentAttributeConfigModel
	 * @param uam
	 */
	public SheetHandler(StylesTable styles, SharedStringsTable sst, ObjectConfigBean objectConfigModel,
			Map<String, ExcelDataRowModel> parentDataRowModels, AttributeConfigBean parentAttributeConfigModel,
			boolean isMainSheet, UserLoginBean uam) {
		this.stylesTable = styles;
		this.sst = sst;
		this.attributeConfigModels = objectConfigModel.getAttributeConfigModels();
		if (this.attributeConfigModels != null) {
			this.attributeConfigModelsMap = new HashMap<String, AttributeConfigBean>();
			for (AttributeConfigBean attributeConfigModel : this.attributeConfigModels) {
				attributeConfigModelsMap.put(String.valueOf(attributeConfigModel.getSequenceNo()),
						attributeConfigModel);
			}
		}
		this.keyPrefix = "";
		this.boClassName = objectConfigModel.getBusinessObjectClassName();
		boModels = new ArrayList<Object>();
		this.sheetName = objectConfigModel.getObjectName();
		this.dataStartRow = objectConfigModel.getDataStartRowNo();
		this.objDefModels = objectConfigModel;
		this.parentDataRowModels = parentDataRowModels;
		countrows = 0;
		countColumn = 0;
		this.parentAttributeConfigModel = parentAttributeConfigModel;
		this.basicValidator = new BasicValidator();
		this.uam = uam;
		this.isMainSheet = isMainSheet;
	}

	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
		if (("inlineStr".equals(name)) || ("v".equals(name))) {
			this.nextIsString = true;

			this.value.setLength(0);

		} else {
			nextIsString = false;
		}
		if ("c".equals(name) && countrows + 1 >= dataStartRow) {
			String newColRef = attributes.getValue("r");
			coverColumnDistanceWithDummyNullObjects(currentColRef, newColRef);
			currentColRef = newColRef;
			this.isEmptyCell = false;
			this.nextDataType = xssfDataType.NUMBER;
			this.formatIndex = -1;
			this.formatString = null;
			String cellType = attributes.getValue("t");
			String cellStyleStr = attributes.getValue("s");
			if ("b".equals(cellType)) {
				this.nextDataType = xssfDataType.BOOL;
			} else if ("e".equals(cellType)) {
				this.nextDataType = xssfDataType.ERROR;
			} else if ("inlineStr".equals(cellType)) {
				this.nextDataType = xssfDataType.INLINESTR;
			} else if ("s".equals(cellType)) {
				this.nextDataType = xssfDataType.SSTINDEX;
			} else if ("str".equals(cellType)) {
				this.nextDataType = xssfDataType.FORMULA;
			} else if (cellStyleStr != null) {
				int styleIndex = Integer.parseInt(cellStyleStr);
				XSSFCellStyle style = this.stylesTable.getStyleAt(styleIndex);
				this.formatIndex = style.getDataFormat();
				this.formatString = style.getDataFormatString();
				if (this.formatString == null)
					this.formatString = BuiltinFormats.getBuiltinFormat(this.formatIndex);
			}
		}
		if ("row".equals(name)) {
			currentColRef = "A";
			if (countrows + 1 >= dataStartRow) {
				try {
					boObject = PropertyMapperService.getPropertyMapper().getNewObjectInstance(this.boClassName);
				} catch (Exception e) {
					throw new SAXException(e);
				}
				boModels.add(boObject);
				dataRowModel = new ExcelDataRowModel();
				dataRowModel.setRowIndex(countrows);
				dataRowModel.setTabIndex(objDefModels.getTabIndex());
				dataRowModel.setRowBOModel(boObject);
				dataRowModel.setHeaderRowNumber(objDefModels.getHeaderRowNumber());
			}
			countrows++;
			countColumn = 0;

		}
		this.value.setLength(0);
	}

	private void coverColumnDistanceWithDummyNullObjects(String fromColRefString, String newColRef)
			throws SAXException {
		int colRefDistance = getDistance(fromColRefString, newColRef);
		while ((countColumn == 0 && colRefDistance == 1 && dataRowModel != null) || (colRefDistance > 1)) {
//			logger.info("Covering distance with null");
			Exception exceptionObject = null;
			createModelWithEmptyValue(exceptionObject);
			--colRefDistance;
			int i = 0;
			i--;
		}

	}

	private int getDistance(String fromColRefString, String toColRefString) {
		if (fromColRefString == null || toColRefString == null) {
			return 1;
		}
		String fromColRef = getExcelCellRef(fromColRefString);
		String toColRef = getExcelCellRef(toColRefString);
		return CellReference.convertColStringToIndex(toColRef) - CellReference.convertColStringToIndex(fromColRef);
	}

	private String getExcelCellRef(String fromColRef) {
		if (fromColRef != null) {
			int i = 0;
			for (; i < fromColRef.toCharArray().length; i++) {
				if (Character.isDigit(fromColRef.charAt(i))) {
					break;
				}
			}
			if (i == 0) {
				return fromColRef;
			} else {
				return fromColRef.substring(0, i);
			}
		}
		return null;
	}

	@SuppressWarnings({ "static-access", "unchecked" })
	public void endElement(String uri, String localName, String name) throws SAXException {
		Exception exceptionObject = null;
		if ("c".equals(name) && countrows >= dataStartRow) {
			if (!this.isEmptyCell) {
				createModelWithEmptyValue(exceptionObject);
			}
		} else if ("v".equals(name) && countrows >= dataStartRow) {
			this.isEmptyCell = true;

			AttributeConfigBean attributeConfigModel = getAttributeConfigModel(countColumn);
			if (attributeConfigModel == null) {
				countColumn++;
				lastContents = "";
				return;
			}
			/*
			 * try{ int idx = Integer.parseInt(this.value.toString()); this.lastContents =
			 * new XSSFRichTextString(this.sst.getEntryAt(idx)).toString(); }catch
			 * (Exception e){ lastContents = this.value.toString(); }
			 */
			switch (this.nextDataType) {
			case BOOL:
				char first = this.value.charAt(0);
				lastContents = first == '0' ? "FALSE" : "TRUE";
				break;
			case ERROR:
				lastContents = this.value.toString();
				break;
			case FORMULA:
				lastContents = this.value.toString();
				break;
			case INLINESTR:
				XSSFRichTextString rtsi = new XSSFRichTextString(this.value.toString());
				lastContents = rtsi.toString();
				break;
			case SSTINDEX:
				String sstIndex = this.value.toString();
				try {
					int idx = Integer.parseInt(sstIndex);
					XSSFRichTextString rtss = new XSSFRichTextString(this.sst.getEntryAt(idx));
					lastContents = rtss.toString();
				} catch (NumberFormatException ex) {
					throw new SAXException(ex);
				}
				break;
			case NUMBER:
				String n = this.value.toString();
				if (this.formatString != null)
					lastContents = this.formatter.formatRawCellContents(Double.parseDouble(n), this.formatIndex,
							this.formatString);
				else
					lastContents = n;
				break;
			default:
				lastContents = this.value.toString();
			}
			orgLastContents = lastContents;
			Object bkBoObject = null;
//			if(countColumn==1) {
//				System.out.println("ID===>"+lastContents);
//			}
			try {
				if (attributeConfigModel != null) {
					String[] multiLavelAttributeName = StringUtils.split(attributeConfigModel.getFullyQualifiedName(),
							".");
					if (multiLavelAttributeName.length > 1) {
						Object parentObj = boObject;
						for (int index = 0; index < multiLavelAttributeName.length - 1; index++) {
							String multiLavelAttributeNames = multiLavelAttributeName[index];
							Object parentLevelObject = PropertyUtils.getProperty(parentObj, multiLavelAttributeNames);
							if (parentLevelObject == null) {
								Class parentLevelClass = PropertyUtils.getPropertyType(parentObj,
										multiLavelAttributeNames);
								parentLevelObject = PropertyMapperService.getPropertyMapper()
										.getNewObjectInstance(parentLevelClass.getName());
							}
							PropertyUtils.setProperty(parentObj, multiLavelAttributeNames, parentLevelObject);
							parentObj = parentLevelObject;
						}
						bkBoObject = boObject;
						boObject = parentObj;

					}
					if (attributeConfigModel.getDelimitedKey() != null
							&& !attributeConfigModel.getDelimitedKey().isEmpty()) {
						List<Object> valueList = new ArrayList<Object>();
						// String[] populatedValueArray =
						// this.lastContents.split(attributeConfigModel.getDelimitedKey());
						String[] populatedValueArray = StringUtils.split(this.lastContents,
								attributeConfigModel.getDelimitedKey());
						if (populatedValueArray.length > 0) {
							for (String value : populatedValueArray) {
								if (attributeConfigModel.getDataFormatKey() != null
										&& !attributeConfigModel.getDataFormatKey().isEmpty()) {
									valueList.add(PropertyMapperService.getPropertyMapper().getFormatedValue(value,
											attributeConfigModel, this.uam));
								} else {
									valueList.add(value);
								}
							}
							PropertyMapperService.getPropertyMapper().updateAttributeValueFromDeliminatedValue(
									attributeConfigModel, valueList, boModels, boObject);
						}
					} else if (attributeConfigModel.getDataFormatKey() != null
							&& !attributeConfigModel.getDataFormatKey().isEmpty()) {
						// populate formated object
//						System.out.println("lastContents===>" + lastContents);
						logger.debug("lastContents===>" + lastContents);
						lastContents = String.valueOf(PropertyMapperService.getPropertyMapper()
								.getFormatedValue(lastContents, attributeConfigModel, uam));
						PropertyMapperService.getPropertyMapper().updatePropertyValue(attributeConfigModel, boObject,
								lastContents, false);
					} else if (attributeConfigModel.getDataType().equalsIgnoreCase(boClassName)) {
						Class type = PropertyMapperService.getPropertyMapper().getClass(boClassName);
//						System.out.println("type.getName()===>" + type.getName());
						logger.debug("type.getName()===>" + type.getName());
						if ((lastContents == null || StringUtils.isEmpty(lastContents))
								&& !"java.lang.String".equalsIgnoreCase((type.getName().trim()))) {
							// Ignore
						} else if ("int".equals(type.getName())) {
							boModels.remove(boModels.size() - 1);
							boObject = Integer.valueOf(lastContents.toString());
							boModels.add(boObject);
						} else if ("long".equals(type.getName())) {
							boModels.remove(boModels.size() - 1);
							boObject = Long.valueOf(lastContents.toString());
							boModels.add(boObject);
						} else if ("double".equals(type.getName())) {
							boModels.remove(boModels.size() - 1);
							boObject = Double.valueOf(lastContents.toString());
							boModels.add(boObject);
						} else if ("float".equals(type.getName())) {
							boModels.remove(boModels.size() - 1);
							boObject = Float.valueOf(lastContents.toString());
							boModels.add(boObject);
						} else if ("short".equals(type.getName())) {
							boModels.remove(boModels.size() - 1);
							boObject = Short.valueOf(lastContents.toString());
							boModels.add(boObject);
						} else if ("byte".equals(type.getName())) {
							boModels.remove(boModels.size() - 1);
							boObject = Byte.valueOf(lastContents.toString());
							boModels.add(boObject);
						} else if ("boolean".equals(type.getName())) {
							boModels.remove(boModels.size() - 1);
							boObject = Boolean.valueOf(lastContents.toString());
							boModels.add(boObject);
						} else if ("char".equals(type.getName())) {
							if (lastContents.toString().isEmpty()) {
								boModels.remove(boModels.size() - 1);
								boObject = Character.valueOf(lastContents.toString().charAt(0));
								boModels.add(boObject);
							}
						} else if (type.getSuperclass() == Number.class) {
							boModels.remove(boModels.size() - 1);
							Class[] parameterTypes = { String.class };
							boObject = type.getConstructor(parameterTypes).newInstance(lastContents);
							boModels.add(boObject);
						} else {
							boModels.remove(boModels.size() - 1);
							Class[] parameterTypes = { String.class };
							boObject = type.getConstructor(parameterTypes).newInstance(lastContents);
							boModels.add(boObject);
						}
						dataRowModel.setRowBOModel(boObject);
					} else {
						lastContents = String.valueOf(PropertyMapperService.getPropertyMapper()
								.getFormatedValue(lastContents, attributeConfigModel, uam));
						PropertyMapperService.getPropertyMapper().updatePropertyValue(attributeConfigModel, boObject,
								lastContents, false);
					}
				} else {
					logger.info("attributeConfigModel is null for sheet index : " + this.objDefModels.getTabIndex()
							+ " column index :" + countColumn);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				setError(true);
				exceptionObject = ex;
				// ignoure this exception
			}
			int countColumnTemp = countColumn;
			if (this.isMainSheet && !objDefModels.getPrimaryKeyIndex().contains(1)) {
				countColumnTemp = countColumnTemp + 1;
			}
			if ((!this.isMainSheet && objDefModels.getPrimaryKeyIndex().contains((countColumn + 1)))
					|| (this.isMainSheet && objDefModels.getPrimaryKeyIndex().contains((countColumnTemp)))) {
				rowKey = ((rowKey == null) ? "" : rowKey) + lastContents + ":";
				// System.out.println("rowKey===>"+rowKey +
				// "<===PrimaryKeyIndex--->"+objDefModels.getPrimaryKeyIndex() +
				// "<===countColumn index Considered===>"+( countColumn+1));
			}
			addNewExcelColumnModel(dataRowModel, lastContents, attributeConfigModel, exceptionObject);

			countColumn++;
			lastContents = "";
			if (bkBoObject != null) {
				boObject = bkBoObject;
			}
		} else if ("row".equals(name)) {
			if (countrows >= dataStartRow) {
				if (rowKey != null) {
					String key = rowKey.substring(0, rowKey.length() - 1);
					excelDataRowKeys.add(key);
					inputMetaDataMap.put(key, dataRowModel);
					rowKey = null;
					if (!dataRowModel.getRowBOModel().getClass().equals(boModels.get(boModels.size() - 1).getClass())) {
						boModels.remove(boModels.size() - 1);
						boModels.add(dataRowModel.getRowBOModel());
					}
				} else {
					boModels.remove(boModels.size() - 1);
				}
				for (int emptyColCounter = countColumn; emptyColCounter < attributeConfigModelsMap.size()
						- 1; emptyColCounter++) {
					AttributeConfigBean attributeConfigModel = getAttributeConfigModel(countColumn);
					if (attributeConfigModel == null) {
						lastContents = "";
						break;
					} else if (StringUtil.isValidString(attributeConfigModel.getHeaderName())) {
						createModelWithEmptyValue(exceptionObject);
					}
				}
			}
		}
	}

	/**
	 * handle empty cell value
	 * 
	 * @param exceptionObject
	 * @throws SAXException
	 */
	private void createModelWithEmptyValue(Exception exceptionObject) throws SAXException {
		AttributeConfigBean attributeConfigModel = getAttributeConfigModel(countColumn);
		try {
			lastContents = "";
			orgLastContents = "";
			if (!(lastContents == null || StringUtils.isEmpty(lastContents))
					&& "java.lang.String".equalsIgnoreCase((attributeConfigModel.getDataType().trim()))) {
				PropertyMapperService.getPropertyMapper().updatePropertyValue(attributeConfigModel, boObject,
						lastContents, false);
			}
		} catch (Exception e) {
			setError(true);
			exceptionObject = e;
		}
		if (objDefModels.getPrimaryKeyIndex().contains((countColumn + 1))) {
			rowKey = ((rowKey == null) ? "" : rowKey) + lastContents + ":";
		}
		addNewExcelColumnModel(dataRowModel, orgLastContents, attributeConfigModel, exceptionObject);
		countColumn++;
	}

	/**
	 * get attribute config model based on index
	 * 
	 * @param index
	 * @return
	 */
	private AttributeConfigBean getAttributeConfigModel(int index) {
		AttributeConfigBean attributeConfigModel = null;
		if (this.isMainSheet) {
			attributeConfigModel = attributeConfigModelsMap.get(String.valueOf(index));
		}
		if (attributeConfigModel == null) {
			attributeConfigModel = attributeConfigModelsMap.get(String.valueOf(index + 1));
		}
		return attributeConfigModel;
	}

	/**
	 * 
	 * @param dataRowModel2
	 * @param lastContents2
	 * @param attributeConfigModel
	 * @param exceptionObj
	 * @throws SAXException
	 */
	@SuppressWarnings("static-access")
	private void addNewExcelColumnModel(ExcelDataRowModel dataRowModel2, String lastContents2,
			AttributeConfigBean attributeConfigModel, Exception exceptionObj) throws SAXException {
		if (Objects.nonNull(dataRowModel2)) {
			ExcelDataColumnModel columnModel = new ExcelDataColumnModel();
			columnModel.setColIndex(countColumn + 1);
			columnModel.setColValue(lastContents2);
			dataRowModel2.addColumns(columnModel);
			if (exceptionObj != null) {
				try {
					if (exceptionObj.getMessage() == null) {
						if (StringUtil.isValidString(lastContents2)) {
							basicValidator.setErrorMessage(columnModel,
									basicValidator.getErrorMessage(MessageConstants.INVALID_VALUE_DEFINED, uam));
						} else {
							if (attributeConfigModel != null && attributeConfigModel.isRequired()) {
								basicValidator.setErrorMessage(columnModel, basicValidator
										.getErrorMessage(MessageConstants.GLOBAL_CONSTRAINT_VALUE_REQUIRED_KEY, uam));
							} else {
								// Ignore this exception -- do nothing
							}
						}
					} else if (exceptionObj.getMessage() != null
							&& exceptionObj.getMessage().contains("Value not found for the key")) {
						basicValidator.setErrorMessage(columnModel, basicValidator
								.getErrorMessage(MessageConstants.GLOBAL_CONST_SS_FORMATTER_INVALID_VALUE_KEY, uam));
					} else {
						basicValidator.setErrorMessage(columnModel, basicValidator
								.getErrorMessage(MessageConstants.GLOBAL_CONSTRAINT_VALUE_REQUIRED_KEY, uam));
					}
				} catch (ServiceException e) {
					throw new SAXException(e);
				}

				dataRowModel2.setRowIndex(countrows);
			}
			if (attributeConfigModel != null) {
				boolean isValidColValue;
				try {
					isValidColValue = basicValidator.processBasicValidation(columnModel, attributeConfigModel, uam);
				} catch (ServiceException e) {
					throw new SAXException(e);
				}
				if (!isValidColValue) {
					setError(true);
				}
			}
		} else {
//			basicValidator.setErrorMessage(columnModel,  basicValidator.getErrorMessage(MessageConstants.GLOBAL_CONSTRAINT_VALUE_REQUIRED_KEY,uam));
		}
	}

	/**
	 * 
	 * @param currentIndex
	 * @return
	 */
	boolean isPrimaryKeyIndex(int currentIndex) {

		if (objDefModels.getPrimaryColumnIndex() != null) {
			String[] pkColIndex = objDefModels.getPrimaryColumnIndex().split(",");
			return Arrays.asList(pkColIndex).contains(String.valueOf(currentIndex));
		}
		return false;
	}

	/**
	 * Captures characters only if a suitable element is open. Originally was just
	 * "v"; extended for inlineStr also.
	 */
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (isNextIsString()) {
			// ch[799];
			this.value.append(ch, start, length);
		}
	}

	/**
	 * Converts an Excel column name like "C" to a zero-based index.
	 *
	 * @param name
	 * @return Index corresponding to the specified name
	 */
	@SuppressWarnings("unused")
	private int nameToColumn(String name) {
		int column = -1;
		for (int i = 0; i < name.length(); ++i) {
			int c = name.charAt(i);
			column = (column + 1) * 26 + c - 'A';
		}
		return column;
	}

	public List<AttributeConfigBean> getAttributeConfigModels() {
		return attributeConfigModels;
	}

	public void setAttributeConfigModels(List<AttributeConfigBean> attributeConfigModels) {
		this.attributeConfigModels = attributeConfigModels;
	}

	/**
	 * @return the objDefModels
	 */
	public ObjectConfigBean getObjDefModels() {
		return objDefModels;
	}

	/**
	 * @param objDefModels the objDefModels to set
	 */
	public void setObjDefModels(ObjectConfigBean objDefModels) {
		this.objDefModels = objDefModels;
	}

	/**
	 * @return the parentDataRowModels
	 */
	public Map<String, ExcelDataRowModel> getParentDataRowModels() {
		return parentDataRowModels;
	}

	/**
	 * @param parentDataRowModels the parentDataRowModels to set
	 */
	public void setParentDataRowModels(Map<String, ExcelDataRowModel> parentDataRowModels) {
		this.parentDataRowModels = parentDataRowModels;
	}

	/**
	 * @return the dataRowModel
	 */
	public ExcelDataRowModel getDataRowModel() {
		return dataRowModel;
	}

	/**
	 * @param dataRowModel the dataRowModel to set
	 */
	public void setDataRowModel(ExcelDataRowModel dataRowModel) {
		this.dataRowModel = dataRowModel;
	}

	/**
	 * @return the excelDataRowKeys
	 */
	public List<String> getExcelDataRowKeys() {
		return excelDataRowKeys;
	}

	/**
	 * @param excelDataRowKeys the excelDataRowKeys to set
	 */
	public void setExcelDataRowKeys(List<String> excelDataRowKeys) {
		this.excelDataRowKeys = excelDataRowKeys;
	}

	/**
	 * @return the parentAttributeConfigModel
	 */
	public AttributeConfigBean getParentAttributeConfigModel() {
		return parentAttributeConfigModel;
	}

	/**
	 * @param parentAttributeConfigModel the parentAttributeConfigModel to set
	 */
	public void setParentAttributeConfigModel(AttributeConfigBean parentAttributeConfigModel) {
		this.parentAttributeConfigModel = parentAttributeConfigModel;
	}

	public boolean isError() {
		return isError;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

	public Map<String, AttributeConfigBean> getAttributeConfigModelsMap() {
		return attributeConfigModelsMap;
	}

	public void setAttributeConfigModelsMap(Map<String, AttributeConfigBean> attributeConfigModelsMap) {
		this.attributeConfigModelsMap = attributeConfigModelsMap;
	}

}
