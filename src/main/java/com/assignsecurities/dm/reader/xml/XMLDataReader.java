package com.assignsecurities.dm.reader.xml;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.app.util.MessageConstants;
import com.assignsecurities.app.util.StringUtil;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.dm.MetaDataConfigService;
import com.assignsecurities.dm.MetaDataConfigServiceImpl;
import com.assignsecurities.dm.PropertyMapperService;
import com.assignsecurities.dm.reader.DocumentParser;
import com.assignsecurities.dm.reader.validator.BasicValidator;
import com.assignsecurities.domain.dm.*;
import com.assignsecurities.domain.dm.excel.ExcelDataColumnModel;
import com.assignsecurities.domain.dm.xml.*;




/**
 * This class is responsible to read xml object based on configuration and
 * create object.
 * 
 * 
 */

public class XMLDataReader extends DefaultHandler implements DocumentParser {
	private BasicValidator basicValidator = new BasicValidator();

	private List<DataLoadObjectModel> dataLoadObjectModels;
	private String xmlFileName;
	private String tmpValue = "";
	private UserLoginBean user;
	private InputStream inputStream;
	private boolean isError = false;
	private HierarchyModel dataHierarchyModel;
	private HierarchyModel dataDefHierarchyModel;
	private int elementCount = 0;
	private Object boObject;
	private int recCount = 0;
	private Map<String, XMLErrorModel> errorMessageModelMap = new HashMap<String, XMLErrorModel>();
	@SuppressWarnings("unchecked")
	private Set availableAttributesSet = new HashSet();

	public Map<String, XMLErrorModel> getErrorMessageModelMap() {
		return errorMessageModelMap;
	}

	public void setErrorMessageModelMap(
			Map<String, XMLErrorModel> errorMessageModelMap) {
		this.errorMessageModelMap = errorMessageModelMap;
	}

	public XMLDataReader() {
	}

	public XMLDataReader(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public XMLDataReader(String xmlFileName) {
		this.xmlFileName = xmlFileName;
	}

	@SuppressWarnings("unchecked")
	public List<DataLoadObjectModel> readData(
			ObjectConfigBean objectConfigModel, Document doc,
			UserLoginBean user) throws ServiceException {
		this.user = user;
		this.inputStream = doc.getImputStream();

		dataLoadObjectModels = new ArrayList();
		dataDefHierarchyModel = new HierarchyModel();
		dataDefHierarchyModel.setCurrentObject(objectConfigModel);
		parseDocument();
		checkForMissingRequiredAttributes(dataLoadObjectModels,
				objectConfigModel);

		return dataLoadObjectModels;
	}

	/**
	 * update error message Model when required
	 * 
	 * @param dataLoadObjectModels2
	 * @param objectConfigModel
	 * @throws ServiceException
	 */
	private void checkForMissingRequiredAttributes(
			List<DataLoadObjectModel> dataLoadObjectModels2,
			ObjectConfigBean objectConfigModel) throws ServiceException {
		List<AttributeConfigBean> attributesConfigModels = objectConfigModel
				.getAttributeConfigModels();
		if (attributesConfigModels == null) {
			return;
		}
		if (dataLoadObjectModels2 == null) {
			return;
		}
		for (DataLoadObjectModel dataLoadObjectModel : dataLoadObjectModels2) {
			Object obObject = dataLoadObjectModel.getBusinessObjectModel();

			checkForMissingAttribute(attributesConfigModels, obObject, dataLoadObjectModel);

		}

	}

	/**
	 * check for missing required attribute.
	 * 
	 * @param attributesConfigModels
	 * @param obObject
	 * @throws ServiceException
	 */
	@SuppressWarnings( { "unchecked", "static-access" })
	private void checkForMissingAttribute(
			List<AttributeConfigBean> attributesConfigModels, Object obObject,DataLoadObjectModel dataLoadObjectModel)
			throws ServiceException {
		if (attributesConfigModels == null) {
			return;
		}
		if (obObject.getClass().isArray() || obObject instanceof List
				|| obObject instanceof Set
				|| obObject instanceof Map
				|| obObject instanceof Vector) {

			List list = new ArrayList();
			if (obObject.getClass().isArray()) {
				int length = Array.getLength(obObject);
				for (int index = 0; index < length; index++) {
					list.add(Array.get(obObject, index));
				}

			} else if (obObject instanceof List) {
				list = (List) obObject;
			} else if (obObject instanceof Set) {
				list.addAll((Set) obObject);
			} else if (obObject instanceof Vector) {
				list.addAll((Vector) obObject);
			}
			for (Object boObject : list) {
				checkForMissingAttribute(attributesConfigModels, boObject,dataLoadObjectModel);
			}
			return;
		}
		for (AttributeConfigBean attributeConfigModel : attributesConfigModels) {

			if (attributeConfigModel.isAttributeAvailableInBo()) {
				Object attributeValue = null;
				try {
					if (!attributeConfigModel.getFullyQualifiedName().contains(
							".")) {
						attributeValue = PropertyUtils.getProperty(obObject,
								attributeConfigModel.getName());
					} else {
						String[] multiLavelAttributeName = StringUtils.split(attributeConfigModel.getFullyQualifiedName(), ".");
						if(multiLavelAttributeName.length>1){
							Object parentObj = obObject;
							for(int index=0;index<multiLavelAttributeName.length-1;index++){
								String multiLavelAttributeNames = multiLavelAttributeName[index];
								Object parentLevelObject = PropertyUtils.getProperty(parentObj, multiLavelAttributeNames);
								if(parentLevelObject == null){
									Class parentLevelClass = PropertyUtils.getPropertyType(parentObj, multiLavelAttributeNames);
									parentLevelObject = PropertyMapperService.getPropertyMapper().getNewObjectInstance(parentLevelClass.getName());
								}
								PropertyUtils.setProperty(parentObj, multiLavelAttributeNames, parentLevelObject);
								parentObj = parentLevelObject;
							}
							attributeValue=parentObj;
							
						}
					}
				} catch (Exception e) {
					if (obObject != null) {
						throw new ServiceException(e);
					}
				}
				if (attributeConfigModel.isRequired() && obObject != null) {
					@SuppressWarnings("unused")
					String code = getErrorCode(attributeConfigModel,
							attributeValue, obObject);
					if (attributeValue == null) {
						updateErrorMessageMapForMissingAttribute(
								attributeConfigModel,
								attributeValue,
								obObject,
								basicValidator
										.getErrorMessage(
												MessageConstants.GLOBAL_CONSTRAINT_VALUE_REQUIRED_KEY,
												user),dataLoadObjectModel);
					} else {
						if (!availableAttributesSet.contains(boObject)) {
							boolean showError = true;
							if (attributeValue.getClass().isArray()
									|| attributeValue instanceof List
									|| attributeValue instanceof Set
									|| attributeValue instanceof Map
									|| attributeValue instanceof Vector) {
								int length = 0;
								if (attributeValue.getClass().isArray()) {
									length = Array.getLength(attributeValue);
								} else if (attributeValue instanceof List) {
									length = ((List) attributeValue).size();
								} else if (attributeValue instanceof Set) {
									length = ((Set) attributeValue).size();
								} else if (attributeValue instanceof Vector) {
									length = ((Vector) attributeValue).size();
								}
								if (length > 0) {
									showError = false;
								}
							}
							if (showError) {
								updateErrorMessageMapForMissingAttribute(
										attributeConfigModel,
										attributeValue,
										obObject,
										basicValidator
												.getErrorMessage(
														MessageConstants.GLOBAL_CONSTRAINT_VALUE_REQUIRED_KEY,
														user),dataLoadObjectModel);
							}
						}
					}

				}
				if (attributeConfigModel.getChildObjectId() != null
						&& attributeConfigModel.getChildObjectId() > 0) {
					ObjectConfigBean configModel = getObjectConfigModel(
							attributeConfigModel.getChildObjectId(), user);
					checkForMissingAttribute(configModel
							.getAttributeConfigModels(), attributeValue,dataLoadObjectModel);
				}
			}
		}
	}

	private void parseDocument() throws ServiceException {
		// parse
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			XMLReader xmlReader = parser.getXMLReader();
			xmlReader.setContentHandler(new XMLDataReader());
			if (StringUtil.isValidString(xmlFileName)) {
				parser.parse(xmlFileName, this);
			} else {
				parser.parse(inputStream, this);
			}
		} catch (ParserConfigurationException e) {
			isError = true;
			throw new ServiceException(e);
		} catch (SAXException e) {
			isError = true;
			throw new ServiceException(e);

		} catch (IOException e) {
			isError = true;
			throw new ServiceException(e);

		} catch (Exception e) {
			isError = true;
			throw new ServiceException(e);
		}
	}

	/*
	 * private void printDatas() { // System.out.println(bookL.size()); for
	 * (Book tmpB : bookL) { System.out.println(tmpB.toString()); } }
	 */

	@Override
	public void startDocument() {

	}

	public String getXMLHeaderName1(AttributeConfigBean attributeConfigModel,
			ObjectConfigBean objectConfigModel) {
		return (attributeConfigModel.getHeaderName() != null) ? attributeConfigModel
				.getHeaderName()
				: objectConfigModel.getTabOrFileName()
						+ ((attributeConfigModel.getCollectionDataType() != null) ? "S"
								: "");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void startElement(String s, String s1, String elementName,
			Attributes attributes) throws SAXException {
		if (StringUtil.isValidString(elementName)) {
			elementCount++;
		}
		if (dataDefHierarchyModel == null) {
			return;
		}
		ObjectConfigBean objectConfigModel = (ObjectConfigBean) dataDefHierarchyModel
				.getCurrentObject();
		if (objectConfigModel == null) {
			return;
		}
		List<AttributeConfigBean> attributesConfigModels = objectConfigModel
				.getAttributeConfigModels();
		if (attributesConfigModels == null) {
			return;
		}
		//
		// if (elementName.equalsIgnoreCase("esDetailsS")
		// ||elementName.equalsIgnoreCase("esDetails") ) {
		// int i = 0;
		// i++;
		//			  
		// }
		boolean isFound = false;
		for (AttributeConfigBean attributeConfigModel : attributesConfigModels) {

			if (elementName.equalsIgnoreCase(attributeConfigModel
					.getXmlTagName())) {

				isFound = true;
				if (attributeConfigModel.getChildObjectId() != null
						&& attributeConfigModel.getChildObjectId() != 0) {

					HierarchyModel tempDataHierarchyModel = new HierarchyModel();
					tempDataHierarchyModel.setParentObject(dataHierarchyModel);
					tempDataHierarchyModel
							.setAttributeConfigModel(attributeConfigModel);
					dataHierarchyModel = tempDataHierarchyModel;
					ObjectConfigBean tempObjectConfigModel;
					try {
						tempObjectConfigModel = getObjectConfigModel(
								attributeConfigModel.getChildObjectId(), user);
					} catch (ServiceException e1) {
						throw new SAXException(e1);
					}
					HierarchyModel tempDataDefHierarchyModel = new HierarchyModel();
					try {

						if (!StringUtil.isValidString(attributeConfigModel
								.getCollectionDataType())) {
							boObject = PropertyMapperService
									.getPropertyMapper()
									.getNewObjectInstance(
											tempObjectConfigModel
													.getBusinessObjectClassName());
							updateParentAttribute(attributeConfigModel);
						} else {
							boObject = PropertyMapperService
									.getDummyCollectionInstance(attributeConfigModel);
							PropertyUtils.setProperty(
									(Object) dataHierarchyModel
											.getParentObject()
											.getCurrentObject(),
									attributeConfigModel.getName(), boObject);
						}
						tempDataHierarchyModel.setCurrentObject(boObject);

						if (boObject != null) {
							availableAttributesSet.add(boObject);
						}
					} catch (Exception e) {
						throw new SAXException(e);
					}
					tempDataDefHierarchyModel
							.setParentObject(dataDefHierarchyModel);
					tempDataHierarchyModel
							.setAttributeConfigModel(attributeConfigModel);
					dataDefHierarchyModel = tempDataDefHierarchyModel;
					tempDataDefHierarchyModel
							.setCurrentObject(tempObjectConfigModel);
				} else {
					/*
					 * try { updateModelProperty(attributeConfigModel); } catch
					 * (Exception ex) {
					 * 
					 * // setError(true); // exceptionObject = ex; // ignoure
					 * this exception }
					 */

				}
				break;
			}
		}
		if (!isFound
				&& elementName.equalsIgnoreCase(objectConfigModel
						.getTabOrFileName().replace("#", ""))) {
			if (dataHierarchyModel == null
					|| dataHierarchyModel.getParentObject() == null) {
				dataHierarchyModel = new HierarchyModel();
			} else {
				HierarchyModel tempDataHierarchyModel = new HierarchyModel();
				tempDataHierarchyModel.setParentObject(dataHierarchyModel);
				dataHierarchyModel = tempDataHierarchyModel;

				HierarchyModel tempDataDefHierarchyModel = new HierarchyModel();
				tempDataDefHierarchyModel
						.setParentObject(dataDefHierarchyModel);
				tempDataDefHierarchyModel.setCurrentObject(objectConfigModel);
				dataDefHierarchyModel = tempDataDefHierarchyModel;
			}

			try {
				boObject = PropertyMapperService.getPropertyMapper()
						.getNewObjectInstance(
								objectConfigModel.getBusinessObjectClassName());
				dataHierarchyModel.setCurrentObject(boObject);
				DataLoadObjectModel dataLoadObjectModel = new XMLDataLoadObjectModel();
				dataLoadObjectModel.setBusinessObjectModel(boObject);
				if (dataHierarchyModel.getParentObject() == null) {
					dataLoadObjectModels.add(dataLoadObjectModel);
				}
			} catch (Exception e) {
				throw new SAXException(e);
			}

		}
		// Read from attribute
		if (attributes != null && attributes.getLength() > 0) {
			for (int i = 0; i < attributes.getLength(); i++) {
				for (AttributeConfigBean attributeConfigModel : attributesConfigModels) {
					String attValue = attributes.getValue(attributeConfigModel
							.getName());
					if (attValue != null) {
						if (attributeConfigModel.getChildObjectId() != null
								&& attributeConfigModel.getChildObjectId() != 0) {
							break;
						} else {
							try {
								tmpValue = attValue;
								updateModelProperty(attributeConfigModel);
							} catch (Exception ex) {

								setError(true,null);
								// exceptionObject = ex;
								// ignoure this exception
							}

						}
						break;
					}
				}
			}
		}

	}

	/**
	 * Update parent attribute values
	 * 
	 * @param attributeConfigModel
	 * @throws ServiceException
	 */
	private void updateParentAttribute(AttributeConfigBean attributeConfigModel)
			throws ServiceException {
		try {
			PropertyMapperService.getPropertyMapper()
					.updateParentObjectAttribute(
							(ObjectConfigBean) dataDefHierarchyModel
									.getCurrentObject(),
							attributeConfigModel,
							boObject,
							(Object) dataHierarchyModel.getParentObject()
									.getCurrentObject());
		} catch (ServiceException e) {
			ObjectConfigBean objectConfigModel2 = ((ObjectConfigBean) dataDefHierarchyModel
					.getCurrentObject());
			String oldObjectType = objectConfigModel2.getObjectType();
			if (StringUtil.isValidString(oldObjectType)) {
				objectConfigModel2.setObjectType(null);
				PropertyMapperService.getPropertyMapper()
						.updateParentObjectAttribute(
								(ObjectConfigBean) dataDefHierarchyModel
										.getCurrentObject(),
								attributeConfigModel,
								boObject,
								(Object) dataHierarchyModel.getParentObject()
										.getCurrentObject());
				objectConfigModel2.setObjectType(oldObjectType);
			}
		}
	}

	/**
	 * Update model property values based on attribute configuration
	 * 
	 * @param attributeConfigModel
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws ServiceException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void updateModelProperty(AttributeConfigBean attributeConfigModel)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, ServiceException, Exception {
		List boModels = new ArrayList();
		/*if (attributeConfigModel.getFullyQualifiedName().contains(".")) {
			int i = 1;
			i = 1 + 1;
		}*/
		boObject = PropertyMapperService.getPropertyMapper()
				.getNewObjectInstance(attributeConfigModel.getDataType());
		boModels.add(boObject);
		if (attributeConfigModel.getDelimitedKey() != null
				&& !attributeConfigModel.getDelimitedKey().isEmpty()) {
			updateModelPropertyFromDilimetedValue(attributeConfigModel,
					boModels);
			boObject = boModels.get(boModels.size() - 1);
		} else if (attributeConfigModel.getDataFormatKey() != null
				&& !attributeConfigModel.getDataFormatKey().isEmpty()) {
			updateModelPropertyWithFormatedValue(attributeConfigModel, boModels);
			boObject = boModels.get(boModels.size() - 1);
		} else {
			if (attributeConfigModel.getDataType().equalsIgnoreCase(
					attributeConfigModel.getDataType())) {
				updateSingleValueModelProperty(attributeConfigModel, boModels);
				boObject = boModels.get(boModels.size() - 1);
			} else {
				PropertyMapperService.getPropertyMapper().updatePropertyValue(
						attributeConfigModel, boObject, tmpValue,true);
				boObject = PropertyUtils.getProperty(boObject,
						attributeConfigModel.getName());
			}
			String oldObjectType = ((ObjectConfigBean) dataDefHierarchyModel
					.getCurrentObject()).getObjectType();
			try {
				PropertyMapperService.getPropertyMapper()
						.updateParentObjectAttribute(
								(ObjectConfigBean) dataDefHierarchyModel
										.getCurrentObject(),
								attributeConfigModel, boObject,
								(Object) dataHierarchyModel.getCurrentObject());
			} catch (Exception ex) {
				((ObjectConfigBean) dataDefHierarchyModel.getCurrentObject())
						.setObjectType(null);
				PropertyMapperService.getPropertyMapper()
						.updateParentObjectAttribute(
								(ObjectConfigBean) dataDefHierarchyModel
										.getCurrentObject(),
								attributeConfigModel, boObject,
								(Object) dataHierarchyModel.getCurrentObject());
				((ObjectConfigBean) dataDefHierarchyModel.getCurrentObject())
						.setObjectType(oldObjectType);
			}
		}
		if (boObject != null) {
			availableAttributesSet.add(boObject);
		}

		// }
	}

	/**
	 * create dummy column model and perform basic validation based on
	 * configuration
	 * 
	 * @param attributeConfigModel
	 * @param exceptionObj
	 * @throws SAXException
	 */
	@SuppressWarnings( { "unused", "static-access" })
	private void addNewColumnModel(XMLDataRowModel rowModel, String tmpValue,
								   AttributeConfigBean attributeConfigModel, Exception exceptionObj,
								   UserLoginBean uam) throws SAXException {
		DataColumnModel columnModel = new XMLDataColumnModel();
		columnModel.setColValue(tmpValue);
		rowModel.addColumns(columnModel);
		if (exceptionObj != null) {
			try {
				if (exceptionObj.getMessage() == null) {
					if (StringUtil.isValidString(tmpValue)) {
						basicValidator.setErrorMessage(columnModel,
								basicValidator.getErrorMessage(
										MessageConstants.INVALID_VALUE_DEFINED,
										uam));
					} else {
						if (attributeConfigModel != null
								&& attributeConfigModel.isRequired()) {
							basicValidator
									.setErrorMessage(
											columnModel,
											basicValidator
													.getErrorMessage(
															MessageConstants.GLOBAL_CONSTRAINT_VALUE_REQUIRED_KEY,
															uam));
						} else {
							// Ignore this exception -- do nothing
						}
					}
				} else if (exceptionObj.getMessage() != null
						&& exceptionObj.getMessage().contains(
								"Value not found for the key")) {
					basicValidator
							.setErrorMessage(
									columnModel,
									basicValidator
											.getErrorMessage(
													MessageConstants.GLOBAL_CONST_SS_FORMATTER_INVALID_VALUE_KEY,
													uam));
				} else {
					basicValidator
							.setErrorMessage(
									columnModel,
									basicValidator
											.getErrorMessage(
													MessageConstants.GLOBAL_CONSTRAINT_VALUE_REQUIRED_KEY,
													uam));
				}
			} catch (ServiceException e) {
				throw new SAXException(e);
			}
			rowModel.setRowIndex(recCount);
		}
		if (attributeConfigModel != null) {
			boolean isValidColValue;
			try {
				isValidColValue = basicValidator.processBasicValidation(
						columnModel, attributeConfigModel, uam);
			} catch (ServiceException e) {
				throw new SAXException(e);
			}
			if (!isValidColValue) {
				setError(true,null);
			}
		}
	}

	/**
	 * Update Single property value of business model
	 * 
	 * @param attributeConfigModel
	 * @param boModels
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws ParseException
	 */
	@SuppressWarnings( { "unchecked", "static-access" })
	private void updateSingleValueModelProperty(
			AttributeConfigBean attributeConfigModel, List boModels)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, ParseException {
		Class type = PropertyMapperService.getPropertyMapper().getClass(
				attributeConfigModel.getDataType());
		if ((tmpValue == null || StringUtils.isEmpty(tmpValue))
				&& !"java.lang.String"
						.equalsIgnoreCase((type.getName().trim()))) {
			// Ignore
		} else if ("int".equals(type.getName())) {
			boModels.remove(boModels.size() - 1);
			boObject = Integer.valueOf(tmpValue.toString());
			boModels.add(boObject);
		} else if ("long".equals(type.getName())) {
			boModels.remove(boModels.size() - 1);
			boObject = Long.valueOf(tmpValue.toString());
			boModels.add(boObject);
		} else if ("double".equals(type.getName())) {
			boModels.remove(boModels.size() - 1);
			boObject = Double.valueOf(tmpValue.toString());
			boModels.add(boObject);
		} else if ("float".equals(type.getName())) {
			boModels.remove(boModels.size() - 1);
			boObject = Float.valueOf(tmpValue.toString());
			boModels.add(boObject);
		} else if ("short".equals(type.getName())) {
			boModels.remove(boModels.size() - 1);
			boObject = Short.valueOf(tmpValue.toString());
			boModels.add(boObject);
		} else if ("byte".equals(type.getName())) {
			boModels.remove(boModels.size() - 1);
			boObject = Byte.valueOf(tmpValue.toString());
			boModels.add(boObject);
		} else if ("boolean".equals(type.getName())) {
			boModels.remove(boModels.size() - 1);
			boObject = Boolean.valueOf(tmpValue.toString());
			boModels.add(boObject);
		} else if ("char".equals(type.getName())) {
			if (tmpValue.toString().isEmpty()) {
				boModels.remove(boModels.size() - 1);
				boObject = Character.valueOf(tmpValue.toString().charAt(0));
				boModels.add(boObject);
			}
		} else if (type.getSuperclass() == Number.class) {
			boModels.remove(boModels.size() - 1);
			Class[] parameterTypes = { String.class };
			boObject = type.getConstructor(parameterTypes)
					.newInstance(tmpValue);
			boModels.add(boObject);
		} else {
			Object valueObject = tmpValue;
			boModels.remove(boModels.size() - 1);
			Class[] parameterTypes = { String.class };
			if ("java.util.Date".equals(type.getName())) {
				if (!tmpValue.toString().isEmpty()) {
					if (!boModels.isEmpty()) {
						boModels.remove(boModels.size() - 1);
					}
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"dd-MMM-yyyy");
					boObject = dateFormat.parse(String.valueOf(tmpValue));
					boModels.add(boObject);
				}
			} else {
				boObject = type.getConstructor(parameterTypes).newInstance(
						valueObject);
				boModels.add(boObject);
			}
		}
	}

	/**
	 * update model property after applying formating on it.
	 * 
	 * @param attributeConfigModel
	 * @throws ServiceException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void updateModelPropertyWithFormatedValue(
			AttributeConfigBean attributeConfigModel, List boModels)
			throws ServiceException, Exception {
		// populate formated object

		tmpValue = String.valueOf(PropertyMapperService.getPropertyMapper()
				.getFormatedValue(tmpValue, attributeConfigModel, user));
		PropertyMapperService.getPropertyMapper().updatePropertyValue(
				attributeConfigModel, dataHierarchyModel.getCurrentObject(),
				tmpValue,true);
		Object inputObject = dataHierarchyModel.getCurrentObject();
		String[] multiLavelAttributeName = StringUtils.split(
				attributeConfigModel.getFullyQualifiedName(), ".");
		if (multiLavelAttributeName.length > 1) {
			Object parentObj = inputObject;
			for (int index = 0; index < multiLavelAttributeName.length - 1; index++) {
				String multiLavelAttributeNames = multiLavelAttributeName[index];
				Object parentLevelObject = PropertyUtils.getProperty(parentObj,
						multiLavelAttributeNames);
				if (parentLevelObject == null) {
					Class parentLevelClass = PropertyUtils.getPropertyType(
							parentObj, multiLavelAttributeNames);
					parentLevelObject = PropertyMapperService
							.getPropertyMapper().getNewObjectInstance(
									parentLevelClass.getName());
				}
				PropertyUtils.setProperty(parentObj, multiLavelAttributeNames,
						parentLevelObject);
				parentObj = parentLevelObject;
			}
			inputObject = parentObj;

		}

		boModels.add(PropertyUtils.getProperty(inputObject,
				attributeConfigModel.getName()));
	}

	/**
	 * update model property form delimited values
	 * 
	 * @param attributeConfigModel
	 * @param boModels
	 * @throws ServiceException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 */
	@SuppressWarnings("unchecked")
	private void updateModelPropertyFromDilimetedValue(
			AttributeConfigBean attributeConfigModel, List boModels)
			throws ServiceException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			ClassNotFoundException, InstantiationException {
		List<Object> valueList = new ArrayList();
		// String[] populatedValueArray =
		// this.tmpValue.split(attributeConfigModel.getDelimitedKey());
		String[] populatedValueArray = StringUtils.split(this.tmpValue,
				attributeConfigModel.getDelimitedKey());
		if (populatedValueArray.length > 0) {
			for (String value : populatedValueArray) {
				if (attributeConfigModel.getDataFormatKey() != null
						&& !attributeConfigModel.getDataFormatKey().isEmpty()) {
					valueList
							.add(PropertyMapperService.getPropertyMapper()
									.getFormatedValue(value,
											attributeConfigModel, user));
				} else {
					valueList.add(value);
				}
			}
			Object inputObject = dataHierarchyModel.getCurrentObject();
			String[] multiLavelAttributeName = StringUtils.split(
					attributeConfigModel.getFullyQualifiedName(), ".");
			if (multiLavelAttributeName.length > 1) {
				Object parentObj = inputObject;
				for (int index = 0; index < multiLavelAttributeName.length - 1; index++) {
					String multiLavelAttributeNames = multiLavelAttributeName[index];
					Object parentLevelObject = PropertyUtils.getProperty(
							parentObj, multiLavelAttributeNames);
					if (parentLevelObject == null) {
						Class parentLevelClass = PropertyUtils.getPropertyType(
								parentObj, multiLavelAttributeNames);
						parentLevelObject = PropertyMapperService
								.getPropertyMapper().getNewObjectInstance(
										parentLevelClass.getName());
					}
					PropertyUtils.setProperty(parentObj,
							multiLavelAttributeNames, parentLevelObject);
					parentObj = parentLevelObject;
				}
				inputObject = parentObj;

			}
			PropertyMapperService.getPropertyMapper()
					.updateAttributeValueFromDeliminatedValue(
							attributeConfigModel, valueList, boModels,
							inputObject);
		}
	}

	/**
	 * get object config model
	 * 
	 * @param childObjectId
	 * @param user
	 * @return
	 * @throws ServiceException
	 */
	private ObjectConfigBean getObjectConfigModel(Long childObjectId,
			UserLoginBean user) throws ServiceException {

		MetaDataConfigService metaDataConfigService = new MetaDataConfigServiceImpl();
		return metaDataConfigService.getSpecificObjectMetaDataFromCache(
				childObjectId, user);
	}

	/**
	 * apply validate validation
	 * 
	 * @param lastContents2
	 * @param attributeConfigModel
	 * @param exceptionObj
	 * @throws SAXException
	 */
	@SuppressWarnings("static-access")
	private void validateValue(Object boObj, String lastContents2,
			AttributeConfigBean attributeConfigModel, Exception exceptionObj,
			UserLoginBean uam) throws SAXException {
		ExcelDataColumnModel columnModel = new ExcelDataColumnModel();
		columnModel.setColIndex(0);
		columnModel.setColValue(tmpValue);
		Object objToSetMsg = null;
		try {
			objToSetMsg = PropertyUtils.getProperty(boObj, attributeConfigModel
					.getName());
		} catch (Exception e1) {
			objToSetMsg = boObj;
		}
		if (exceptionObj != null) {
			try {
				if (exceptionObj.getMessage() == null) {
					if (StringUtil.isValidString(lastContents2)) {
						updateErrorMessageMap(attributeConfigModel,
								objToSetMsg, boObj,
								basicValidator.getErrorMessage(
										MessageConstants.INVALID_VALUE_DEFINED,
										uam), lastContents2);
					} else {
						if (attributeConfigModel != null
								&& attributeConfigModel.isRequired()) {
							updateErrorMessageMap(
									attributeConfigModel,
									objToSetMsg,
									boObj,
									basicValidator
											.getErrorMessage(
													MessageConstants.GLOBAL_CONSTRAINT_VALUE_REQUIRED_KEY,
													uam), lastContents2);
						} else {
							// Ignore this exception -- do nothing
						}
					}
				} else if (exceptionObj.getMessage() != null
						&& exceptionObj.getMessage().contains(
								"Value not found for the key")) {
					updateErrorMessageMap(
							attributeConfigModel,
							objToSetMsg,
							boObj,
							basicValidator
									.getErrorMessage(
											"ValidationKeys.GLOBAL_CONST_SS_FORMATTER_INVALID_VALUE_KEY",
											uam), lastContents2);
				} else {
					updateErrorMessageMap(
							attributeConfigModel,
							objToSetMsg,
							boObj,
							basicValidator
									.getErrorMessage(
											"ValidationKeys.GLOBAL_CONSTRAINT_VALUE_REQUIRED_KEY",
											uam), lastContents2);
				}
			} catch (ServiceException e) {
				throw new SAXException(e);
			}
		}
		if (attributeConfigModel != null) {
			boolean isValidColValue;
			try {
				isValidColValue = basicValidator.processBasicValidation(
						columnModel, attributeConfigModel, uam);
			} catch (ServiceException e) {
				throw new SAXException(e);
			}
			if (!isValidColValue) {
				setError(true,null);
				updateErrorMessageMap(attributeConfigModel, objToSetMsg, boObj,
						columnModel.getErrorMessage(), lastContents2);
			}
		}
	}

	/**
	 * update error message map
	 * 
	 * @param msg
	 */
	public void updateErrorMessageMap(
			AttributeConfigBean attributeConfigModel, Object boObject,
			Object parentObject, String msg, String lastContents2) {

		String code = getErrorCode(attributeConfigModel, boObject, parentObject);
		XMLErrorModel xmlErrorModel = errorMessageModelMap.get(String
				.valueOf(code));
		if (xmlErrorModel == null) {
			xmlErrorModel = new XMLErrorModel();
			errorMessageModelMap.put(code, xmlErrorModel);
			xmlErrorModel.setDataValue((lastContents2 == null) ? ""
					: lastContents2);
		}
		List<String> msgList = xmlErrorModel.getErrorMessages();
		if (msgList == null || msgList.isEmpty()) {
			msgList = new ArrayList<String>();
			xmlErrorModel.setErrorMessages(msgList);
		}
		if (StringUtil.isValidString(msg)) {			
				msgList.add(msg);	
			
		}
	}

	/**
	 * get error code
	 * 
	 * @param attributeConfigModel
	 * @param boObject
	 * @param parentObject
	 * @return
	 */
	public static String getErrorCode(
			AttributeConfigBean attributeConfigModel, Object boObject,
			Object parentObject) {
		String code = "";
		if (boObject == null) {
			if (parentObject != null) {
				code = "OBJ:" + parentObject.hashCode() + ":"
						+ attributeConfigModel.getId();
			} else {
				code = "ATT::" + attributeConfigModel.getId();
			}
		} else {
			if (parentObject != null) {
				code = "OBJ:" + parentObject.hashCode() + ":"
						+ boObject.hashCode() + ":"
						+ attributeConfigModel.getId();
			} else {
				code = "OBJ::" + boObject.hashCode() + ":"
						+ attributeConfigModel.getId();
			}
		}
		return code;
	}

	/**
	 * update error message map for missing attributes
	 * 
	 * @param msg
	 */
	public void updateErrorMessageMapForMissingAttribute(
			AttributeConfigBean attributeConfigModel, Object obObject,
			Object parentObject, String msg,DataLoadObjectModel dataLoadObjectModel) {
		setError(true,dataLoadObjectModel);
		String code = getErrorCode(attributeConfigModel, boObject, parentObject);

		XMLErrorModel xmlErrorModel = errorMessageModelMap.get(String
				.valueOf(code));

		if (xmlErrorModel == null) {			
				xmlErrorModel = new XMLErrorModel();
				errorMessageModelMap.put(code, xmlErrorModel);
			
		}
		List<String> msgList = xmlErrorModel.getErrorMessages();
		if (msgList == null || msgList.isEmpty()) {
			msgList = new ArrayList<String>();
			xmlErrorModel.setErrorMessages(msgList);
		}
		if (StringUtil.isValidString(msg) && !msgList.contains(msg)) {
			msgList.add(msg);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void endElement(String s, String s1, String element)
			throws SAXException {
		tmpValue = tmpValue.replace("\n", "").trim();

		// if (element.equalsIgnoreCase("esDetailsS") ||
		// element.equalsIgnoreCase("esDetails")) {
		// int i = 0;
		// i++;
		// }
		if (dataDefHierarchyModel == null) {
			return;
		}
		setError(false,null);
		ObjectConfigBean objectConfigModel = (ObjectConfigBean) dataDefHierarchyModel
				.getCurrentObject();
		if (objectConfigModel == null) {
			return;
		}
		List<AttributeConfigBean> attributesConfigModels = objectConfigModel
				.getAttributeConfigModels();
		if (attributesConfigModels == null) {
			return;
		}
		boolean isFound = false;
		for (AttributeConfigBean attributeConfigModel : attributesConfigModels) {
			if (element.equalsIgnoreCase(attributeConfigModel.getXmlTagName())) {
				/*
				 * if (attributeConfigModel.getName().equalsIgnoreCase(
				 * "rateCardPositionClasses")) { int i = 0; i--; }
				 */
				isFound = true;
				Exception exceptionObject = null;
				if (attributeConfigModel.getChildObjectId() != null
						&& attributeConfigModel.getChildObjectId() != 0) {
					dataHierarchyModel = dataHierarchyModel.getParentObject();
					dataDefHierarchyModel = dataDefHierarchyModel
							.getParentObject();
				} else {
					try {
						updateModelProperty(attributeConfigModel);
					} catch (Exception ex) {
						setError(true,null);
						exceptionObject = ex;
						// ignoure this exception
					}
					if (element.equalsIgnoreCase("action")) {
						dataLoadObjectModels.get(
								dataLoadObjectModels.size() - 1).setAction(
								tmpValue);
					} else {
						validateValue(((Object) dataHierarchyModel
								.getCurrentObject() == null) ? boObject
								: (Object) dataHierarchyModel
										.getCurrentObject(), tmpValue,
								attributeConfigModel, exceptionObject, user);
					}
				}
				break;
			}
		}
		if (!isFound
				&& (element.equalsIgnoreCase(objectConfigModel
						.getTabOrFileName().replace("#", "")) || element
						.equalsIgnoreCase(objectConfigModel.getTabOrFileName()
								.replace("#", "")
								+ "S"))) {
			if ((dataHierarchyModel != null && dataHierarchyModel
					.getParentObject() != null)) {
				Object parentObject = dataHierarchyModel.getParentObject()
						.getCurrentObject();
				if (parentObject != null) {
					if (parentObject.getClass().isArray()) {

						if (parentObject != null) {

							int length = Array.getLength(parentObject);
							// Array.set(parentObject,length-1,(Object)
							// dataHierarchyModel.getCurrentObject());
							Object newArray = null;
							try {
								newArray = Array
										.newInstance(
												PropertyMapperService
														.getClass(objectConfigModel
																.getBusinessObjectClassName()),
												length + 1);
							} catch (NegativeArraySizeException e) {
								throw new SAXException(e);
							} catch (ClassNotFoundException e) {
								throw new SAXException(e);
							}
							if (length != 0) {
								System.arraycopy(parentObject, 0, newArray, 0,
										length);
							}
							Array.set(newArray, length, dataHierarchyModel
									.getCurrentObject());
							dataHierarchyModel.getParentObject()
							.setCurrentObject(newArray);
							
							
							if (dataHierarchyModel.getParentObject()
									.getParentObject() != null
									&& dataHierarchyModel.getParentObject()
											.getAttributeConfigModel() != null) {
								try {
									PropertyUtils
											.setProperty(
													(Object) (dataHierarchyModel
															.getParentObject()
															.getParentObject())
															.getCurrentObject(),
													((AttributeConfigBean) (dataHierarchyModel
															.getParentObject()
															.getAttributeConfigModel()))
															.getName(),
													newArray);
								} catch (Exception e) {
									throw new SAXException(e);
								}
							}
						}
					} else if (parentObject instanceof List
							|| parentObject instanceof Set
							|| parentObject instanceof Vector) {
						updateCollectionWithChildObject(
								(Collection) (parentObject), dataHierarchyModel
										.getCurrentObject());
					}
				}
				dataHierarchyModel = dataHierarchyModel.getParentObject();
				dataDefHierarchyModel = dataDefHierarchyModel.getParentObject();
				// }
			}

		}
		tmpValue = "";
	}

	/**
	 * update collection child object in collection
	 * 
	 * @param object
	 * @param childObj
	 */
	@SuppressWarnings("unchecked")
	private void updateCollectionWithChildObject(Collection object,
			Object childObj) {

		if (childObj.getClass().isArray()) {
			int length = Array.getLength(childObj);
			for (int index = 0; index < length; index++) {
				object.add(Array.get(childObj, index));
			}
		} else if (childObj instanceof List) {
			object.addAll((List) childObj);
		} else if (childObj instanceof Set) {
			object.addAll(((Set) childObj));
		} else if (childObj instanceof Vector) {
			object.addAll((Vector) childObj);
		} else {
			object.add(childObj);
		}

	}

	public boolean isError() {
		return isError;
	}
	/**
	 * This method is responsible to mark the data load model as error
	 * when it is called during reading process it pass null as DataLoadObjectModel.
	 * in this case it will get set by last created data load model. 
	 * @param isError
	 * @param dataLoadObjectModel
	 */
	public void setError(boolean isError,DataLoadObjectModel dataLoadObjectModel) {
		if(dataLoadObjectModel==null){
			if (dataLoadObjectModels != null && !dataLoadObjectModels.isEmpty()) {
				DataLoadObjectModel currentDataLoadObjectModel = dataLoadObjectModels
						.get(dataLoadObjectModels.size() - 1);
				if (!currentDataLoadObjectModel.isError()) {
					currentDataLoadObjectModel.setError(isError);
				}
			}
		}else{
			dataLoadObjectModel.setError(isError);
		}
		this.isError = isError;
	}

	@Override
	public void characters(char[] ac, int i, int j) throws SAXException {
		tmpValue += new String(ac, i, j);
	}

	public static void test() {

		
	}

}
