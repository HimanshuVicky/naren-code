package com.assignsecurities.dm.writer.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.app.util.DMConstants;
import com.assignsecurities.app.util.DateFormatterUtil;
import com.assignsecurities.app.util.ResourceBundleUtil;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.dm.MetaDataConfigService;
import com.assignsecurities.dm.MetaDataConfigServiceImpl;
import com.assignsecurities.dm.reader.xml.XMLDataReader;
import com.assignsecurities.dm.writer.DownLoadDataWriter;
import com.assignsecurities.domain.dm.*;
import com.assignsecurities.domain.dm.xml.XMLDataWriterModel;
import com.assignsecurities.domain.dm.xml.XMLErrorModel;





/**
 * This is the xml implementation for writing data in form of xml.
 * 
 * This is used to download existing data in xml form. this read configuration
 * and generate xml.
 * 
 * 
 * 
 */
public class XMLDataWriter extends DownLoadDataWriter {
	private final String LT_TAG = "<";
	private final String GT_TAG = ">";
	private final String SPACE = " ";
	private final String EQUAL = "=";
	private final String DOUBLE_QUOTE = "\"";
	private final String START_OF_END_TAG = "</";
	public final String NEXT_LINE = "\n";
	private ObjectConfigBean parentObjectConfigModel;
	private String actionValue;
	private static final Logger logger = LogManager.getLogger(XMLDataWriter.class);

	public String getActionValue() {
		return actionValue;
	}

	public void setActionValue(String actionValue) {
		this.actionValue = actionValue;
	}

	/**
	 * Default constructor
	 */
	public XMLDataWriter(ResourceBundleUtil resourceBundleUtil) {
		try {
			if (dateFormator == null) {
				dateFormator = DateFormatterUtil.getDateFormatForLocal(
						"dd-MMM-yyyy", new Locale("en_US"),resourceBundleUtil);
			}
		} catch (Exception pex) {
			pex.printStackTrace();
		}
	}

	@Override
	public File generateFile(String inputfileName, UserLoginBean uam,
							 ObjectBean objectModel, DataWriterModel dataWriterModel)
			throws ServiceException, IOException {

		XMLDataWriterModel xmlDataWriterModel = (XMLDataWriterModel) dataWriterModel;
//		Random randomGenerator = new Random();
		StringBuilder xml = new StringBuilder("");
		if(parentObjectConfigModel!=null){
			xml.append(NEXT_LINE).append(LT_TAG).append(
					parentObjectConfigModel.getTabOrFileName().replace('#', ' ')
							.trim()).append("S").append(GT_TAG);
			xml.append(xmlDataWriterModel.getXml());
			xmlDataWriterModel.getXml().delete(0,
					xmlDataWriterModel.getXml().length());
			xml.append(NEXT_LINE).append(START_OF_END_TAG).append(
					parentObjectConfigModel.getTabOrFileName().replace('#', ' ')
							.trim()).append("S").append(GT_TAG);
		}
		FileOutputStream fileOut = null;
//		String fileName = "downloadma" + randomGenerator.nextInt(100) + "-"
//				+ System.currentTimeMillis() + ".xml";
		String fileName=this.fileName+ ".xml";
		File file = null;
		try {
			fileOut = new FileOutputStream(fileName);
			fileOut.write(xml.toString().getBytes());
			fileOut.close();

			// Save file in SMP_FILE_STORE
			file = new File(fileName);
//			Long fileId = upload(inputfileName, file, "xml",objectModel, uam);
//			objectModel.setFileId(fileId);
			objectModel.setFileName(inputfileName);

		} finally {
			try {
				if (fileOut != null) {
					fileOut.close();
				}
			} catch (IOException e) {
				// ignoure exception
			}

//			if (file != null) {
//				file.deleteOnExit();
//			}
		}
		return file;

	}

	@Override
	public void writeDataToFile(Object obj, ObjectModel objectModel,
								ObjectConfigBean objectConfigModel, ObjectBean objectBean,
								DataWriterModel dataWriterModel, int rowCount, boolean isMainSheet,
								UserLoginBean user) throws ServiceException {
		if (parentObjectConfigModel == null) {
			parentObjectConfigModel = objectConfigModel;
		}
		XMLDataWriterModel writerModel = (XMLDataWriterModel) dataWriterModel;
		writerModel.getXml().append(NEXT_LINE);
		
		try {
			if(obj!=null){
				writerModel.getXml().append(
						this.getXML(objectConfigModel, obj, "", null, user, true,false));
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		writerModel.getXml().append(NEXT_LINE);
		//System.out.println(writerModel.getXml());
	}

	public void getXMLFromListOfDataLoadModels(
			ObjectConfigBean objectConfigModel,
			List<DataLoadObjectModel> dataLoadObjectModels, UserLoginBean user)
			throws ServiceException {
		StringBuilder xml = new StringBuilder("");
		xml.append(NEXT_LINE).append(LT_TAG).append(
				objectConfigModel.getTabOrFileName().replace('#', ' ').trim())
				.append("S").append(GT_TAG);
		for (DataLoadObjectModel dataLoadObjectModel : dataLoadObjectModels) {

			xml.append(this.getXML(objectConfigModel, dataLoadObjectModel
					.getBusinessObjectModel(), "", null, user, true,false));
			xml.append(NEXT_LINE);

		}

		xml.append(START_OF_END_TAG).append(
				objectConfigModel.getTabOrFileName().replace('#', ' ').trim())
				.append("S").append(GT_TAG);
		xml.append("");
	}

	/**
	 * create xml file for record
	 * 
	 * @param user
	 * @param isRootObject
	 * @param showSampleValue
	 * @return
	 * @throws ServiceException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	public String getXML(ObjectConfigBean objectConfigModel, Object boModel,
			String indentation, Map<String, XMLErrorModel> errorMessageMap,
			UserLoginBean user, boolean isRootObject,boolean showSampleValue) throws ServiceException {
		StringBuilder xml = new StringBuilder(indentation);
		
		if (boModel == null) {
			return getXML(objectConfigModel, indentation,showSampleValue, user);
		}

		if (boModel.getClass().isArray()
				|| boModel instanceof List
				|| boModel instanceof java.util.Set
				|| boModel instanceof Map
				|| boModel instanceof java.util.Vector) {

			List list = new ArrayList();
			if (boModel.getClass().isArray()) {
				int length = Array.getLength(boModel);				
				for(int index=0;index<length;index++){
					list.add(Array.get(boModel, index));
				}
			} else if (boModel instanceof List) {
				list = (List) boModel;
			} else if (boModel instanceof java.util.Set) {
				list.addAll((java.util.Set) boModel);
			} else if (boModel instanceof java.util.Vector) {
				list.addAll((java.util.Vector) boModel);
			}
			for (Object boObject : list) {
				xml.append(NEXT_LINE);
				indentation = indentation + "    ";
				xml.append(getXML(objectConfigModel, boObject, indentation,
						errorMessageMap, user, false,false));
				indentation = indentation.substring(indentation.length() - 4);
				xml.append(NEXT_LINE);
			}

			return xml.toString();
		}

		xml.append(LT_TAG).append(
				objectConfigModel.getTabOrFileName().replace('#', ' ').trim());
		if (errorMessageMap != null
				&& isRootObject
				&& errorMessageMap
						.containsKey(DMConstants.BUSINESS_ERROR)) {
			XMLErrorModel errorModel = errorMessageMap
					.get(DMConstants.BUSINESS_ERROR);
			xml.append(SPACE).append("Error=").append(DOUBLE_QUOTE);
			for (String msg : errorModel.getErrorMessages()) {
				xml.append(getValueWithoutEscapeChar(msg));
			}
			xml.append(DOUBLE_QUOTE).append(SPACE);
		}
		List<AttributeConfigBean> attributeConfigModels = objectConfigModel
				.getAttributeConfigModels();
		if (attributeConfigModels != null) {
			for (AttributeConfigBean attributeConfigModel : attributeConfigModels) {
				if (attributeConfigModel.isXMLAttribute()
						&& attributeConfigModel.isAttributeAvailableInBo()) {
					xml.append(SPACE);
					xml.append(attributeConfigModel.getXmlTagName()).append(
							EQUAL).append(DOUBLE_QUOTE).append(
							getAttributeValueWithoutEscapeChar(
									attributeConfigModels, boModel)).append(
							DOUBLE_QUOTE);
					xml.append(SPACE);
				}

			}
		}
		xml.append(GT_TAG);
		// }
		// List<AttributeConfigModel> attributeConfigModels =
		// objectConfigModel.getAttributeConfigModels();
		if (attributeConfigModels != null) {
			for (AttributeConfigBean attributeConfigModel : attributeConfigModels) {
				
			/*	if("supplierSkillsSpecializations".equalsIgnoreCase(objectConfigModel
						.getTabOrFileName())){
					int ik=1;
					ik++;
					if("supplierCode".equalsIgnoreCase(attributeConfigModel
							.getName())){
						int ijk=1;
						ijk++;
					}
				}

				if("skillId".equalsIgnoreCase(attributeConfigModel
						.getName())){
					int ik=1;
					ik++;
				}*/
				if (!attributeConfigModel.isXMLAttribute()
						&& (("Action".equalsIgnoreCase(attributeConfigModel
								.getXmlTagName()) && !attributeConfigModel
								.isAttributeAvailableInBo()) || attributeConfigModel
								.isAttributeAvailableInBo())) {
					/*
					 * if("btCatAssociation".equalsIgnoreCase(attributeConfigModel
					 * .getName())){ int i =0; i++; }
					 */
//					if (attributeConfigModel.getId()==87){
//						int i=0;
//						i++;
//					}
					Object boModelTemp = boModel;
					if (attributeConfigModel.getChildObjectId() == null) {
						Object attObject = null;
						boolean isAction = false;
						try {
							String[] multiLavelAttributeName = StringUtils.split(attributeConfigModel.getFullyQualifiedName(), ".");
							if(multiLavelAttributeName.length>1){ 
								Object parentObj = boModel;
								boolean isNull = false;
								for(int index=0;index<multiLavelAttributeName.length-1;index++){
									String multiLavelAttributeNames = multiLavelAttributeName[index];
									Object parentLevelObject = PropertyUtils.getProperty(parentObj, multiLavelAttributeNames);
									if(parentLevelObject == null){
										attObject = null;
										isNull = true;
										break;
									}
									parentObj = parentLevelObject;
								}
								if(isNull){
									boModel = boModelTemp;
									boModelTemp = null;
									continue;
								}
								//attObject = PropertyUtils.getProperty(parentObj, multiLavelAttributeName[multiLavelAttributeName.length-1]);
								String attName = multiLavelAttributeName[multiLavelAttributeName.length - 1];
								Class type = PropertyUtils.getPropertyType(parentObj, attName);
								if (type != null &&("java.lang.Boolean".equalsIgnoreCase(type.getName())
										|| "boolean".equalsIgnoreCase(type.getName()))
										&& attributeConfigModel.getName().startsWith("is")) {
									attObject = PropertyUtils.getProperty(parentObj, attName.substring(
											2, 3).toLowerCase()
											+ attName.substring(3));
								} else {
									try{
										attObject = PropertyUtils.getProperty(parentObj, attName);
									}catch(Exception e){
										e.printStackTrace();
									}
								}
							}else{
								Class type = PropertyUtils.getPropertyType(boModel,
										attributeConfigModel.getName());
								if(type != null && ( "java.lang.Boolean".equalsIgnoreCase(type.getName() )  || "boolean".equalsIgnoreCase(type.getName() ) ) && attributeConfigModel!= null && attributeConfigModel.getName()!= null && attributeConfigModel.getName().startsWith("is")){
									attObject = PropertyUtils.getProperty(boModel,
											attributeConfigModel.getName().substring(2,3).toLowerCase()+attributeConfigModel.getName().substring(3));
								}else{
									attObject = PropertyUtils.getProperty(boModel,
									attributeConfigModel.getName());
								}
							}
						} catch (Exception e) {
							if ("Action".equalsIgnoreCase(attributeConfigModel
									.getXmlTagName())) {
								attObject = "No Action";
								isAction = true;
							} else if (attributeConfigModel
									.isAttributeAvailableInBo()) {
								throw new ServiceException(e);
							} else {
								continue;
							}
						}
						String value = "";
						xml.append(NEXT_LINE).append(indentation)
								.append("    ");
						String errorCode = XMLDataReader.getErrorCode(
								attributeConfigModel, attObject, boModel);

						if (isAction) {
							xml.append(LT_TAG).append(
									attributeConfigModel.getXmlTagName());
							if (errorMessageMap != null
									&& errorMessageMap
											.containsKey(DMConstants.ACTION_TEXT)) {
								XMLErrorModel errorModel = errorMessageMap
										.get(DMConstants.ACTION_TEXT);
								xml.append(SPACE).append("Error=").append(
										DOUBLE_QUOTE);
								for (String msg : errorModel.getErrorMessages()) {
									xml.append(getValueWithoutEscapeChar(msg));
								}
								xml.append(DOUBLE_QUOTE).append(SPACE);
							}
							xml.append(GT_TAG);
							value = "No Action";
							if (actionValue != null) {
								value = actionValue;
							}
						} else if (errorMessageMap == null
								|| (!errorMessageMap.containsKey(errorCode))) {
							xml.append(LT_TAG).append(
									attributeConfigModel.getXmlTagName())
									.append(GT_TAG);
							try {
								value = getAttributeValue(attributeConfigModel,
										boModel,errorMessageMap, user);
								if (value != null) {
									value = getFormatedValue(attributeConfigModel, value
											.toString());
								}
							} catch (IllegalAccessException e) {
								throw new ServiceException(e);
							} catch (InvocationTargetException e) {
								throw new ServiceException(e);
							} catch (NoSuchMethodException e) {
								throw new ServiceException(e);
							}
						} else {
							xml.append(LT_TAG).append(
									attributeConfigModel.getXmlTagName());
							if (errorMessageMap != null) {
								XMLErrorModel errorModel = errorMessageMap
										.get(errorCode);
								if (errorModel.getErrorMessages() != null
										&& !errorModel.getErrorMessages()
												.isEmpty()) {
									xml.append(SPACE).append("Error=").append(
											DOUBLE_QUOTE);
									for (String msg : errorModel
											.getErrorMessages()) {
										xml
												.append(getValueWithoutEscapeChar(msg));
									}
									xml.append(DOUBLE_QUOTE).append(SPACE);
								}
								xml.append(GT_TAG);
								value = (errorModel.getDataValue() == null) ? ""
										: errorModel.getDataValue();
							}
						}
						if(value.contains(">")||value.contains("<")||value.contains("&")||value.contains(";")||value.contains("#")){
							value="<![CDATA["+value+"]]>";
						}
						xml.append(value);
						// xml.append(getAttributeValueWithoutEscapeChar(attributeConfigModels,boModel));
						xml.append(START_OF_END_TAG).append(
								attributeConfigModel.getXmlTagName()).append(
								GT_TAG);
						if(boModelTemp!=null){
							boModel = boModelTemp;
							boModelTemp = null;
						}
					} else {
						ObjectConfigBean childObjectConfigModel = getObjectConfigModel(
								attributeConfigModel.getChildObjectId(), user);
						Object childObject;
						try {
							childObject = PropertyUtils.getProperty(boModel,
									attributeConfigModel.getName());
						} catch (IllegalAccessException e) {
							throw new ServiceException(e);
						} catch (InvocationTargetException e) {
							throw new ServiceException(e);
						} catch (NoSuchMethodException e) {
							throw new ServiceException(e);
						}
						if (childObject != null) {
							xml.append(NEXT_LINE);
							if (childObject.getClass().isArray()
									|| childObject instanceof List
									|| childObject instanceof java.util.Set
									|| childObject instanceof Map
									|| childObject instanceof java.util.Vector) {
								xml.append(LT_TAG).append(
										attributeConfigModel.getXmlTagName())
										.append(GT_TAG);
								List list = new ArrayList();
								if (childObject.getClass().isArray()) {
									int length = Array.getLength(childObject);				
									for(int index=0;index<length;index++){
										list.add(Array.get(childObject, index));
									}
								} else if (childObject instanceof List) {
									list = (List) childObject;
								} else if (childObject instanceof java.util.Set) {
									list.addAll((java.util.Set) childObject);
								} else if (childObject instanceof java.util.Vector) {
									list.addAll((java.util.Vector) childObject);
								}
								list.remove("NEW_SUPPLIER");
								for (Object boObject : list) {
									xml.append(NEXT_LINE);
									indentation = indentation + "    ";
									xml.append(getXML(childObjectConfigModel,
											boObject, indentation,
											errorMessageMap, user, false,false));
									indentation = indentation
											.substring(indentation.length() - 4);
									xml.append(NEXT_LINE);
								}
								xml.append(NEXT_LINE).append(START_OF_END_TAG)
										.append(
												attributeConfigModel
														.getXmlTagName())
										.append(GT_TAG);
							} else {
								indentation = indentation + "    ";
								xml.append(getXML(childObjectConfigModel,
										childObject, indentation,
										errorMessageMap, user, false,false));
								indentation = indentation.substring(indentation
										.length() - 4);
							}
							xml.append(NEXT_LINE);
						}
					}
				}

			}
		}

		xml.append(NEXT_LINE).append(indentation).append(START_OF_END_TAG)
				.append(
						objectConfigModel.getTabOrFileName().replace('#', ' ')
								.trim());
		xml.append(GT_TAG);
		// }
		return xml.toString();

	}

	/**
	 * return dummy xml based on Object configuration
	 * 
	 * @param objectConfigModel
	 * @param indentation
	 * @param showSampleValue
	 * @param user
	 * @return
	 * @throws ServiceException
	 */
	public String getXML(ObjectConfigBean objectConfigModel,
			String indentation,boolean showSampleValue, UserLoginBean user) throws ServiceException {
		StringBuilder xml = new StringBuilder(indentation);
		List<AttributeConfigBean> attributeConfigModels = objectConfigModel
				.getAttributeConfigModels();
		if (attributeConfigModels == null) {
			return xml.toString();
		}
		for (AttributeConfigBean attributeConfigModel : attributeConfigModels) {
			if (attributeConfigModel.isAttributeAvailableInBo()) {
				if (attributeConfigModel.getChildObjectId() == null) {
					xml.append(NEXT_LINE).append(indentation).append(LT_TAG).append(
							attributeConfigModel.getXmlTagName())
							.append(GT_TAG);
					if(showSampleValue){
						xml.append(getSampleValue(attributeConfigModel,user));
					}
					xml.append(START_OF_END_TAG).append(
							attributeConfigModel.getXmlTagName())
							.append(GT_TAG);
				} else {
					ObjectConfigBean tempObjectConfigModel = getObjectConfigModel(
							attributeConfigModel.getChildObjectId(), user);
					if(attributeConfigModel.getCollectionDataType()!=null){
						xml.append(NEXT_LINE).append(indentation).append(LT_TAG).append(
								tempObjectConfigModel.getTabOrFileName().replaceAll("#", "")+"S")
								.append(GT_TAG);
					}
					xml.append(NEXT_LINE).append(indentation+ "    ").append(LT_TAG).append(
							tempObjectConfigModel.getTabOrFileName().replaceAll("#", ""))
							.append(GT_TAG);
					xml.append(
							getXML(tempObjectConfigModel, new String(
									indentation + "    	"),showSampleValue, user));
					xml.append(NEXT_LINE).append(indentation+ "    ").append(START_OF_END_TAG).append(
							tempObjectConfigModel.getTabOrFileName().replaceAll("#", ""))
							.append(GT_TAG);
					if(attributeConfigModel.getCollectionDataType()!=null){
						xml.append(NEXT_LINE).append(indentation).append(START_OF_END_TAG).append(
								tempObjectConfigModel.getTabOrFileName().replaceAll("#", "")+"S")
								.append(GT_TAG);
					}
				}
			}else if("dlAction".equalsIgnoreCase(attributeConfigModel.getName())){
				xml.append(NEXT_LINE).append(indentation).append(LT_TAG).append(
						"action")
						.append(GT_TAG);
				if(showSampleValue){
					xml.append(getSampleValue(attributeConfigModel,user));
				}
				xml.append(START_OF_END_TAG).append("action")
						.append(GT_TAG);
			}
		}
		return xml.toString();

	}
	/**
	 * get Sample values for XML
	 * @param attributeConfigModel
	 * @param user
	 * @return
	 * @throws ServiceException
	 */
	private String getSampleValue(AttributeConfigBean attributeConfigModel,UserLoginBean user) throws ServiceException {
		String value = "";
		if(attributeConfigModel.getSampleValue()!=null){
			value=attributeConfigModel.getSampleValue();
		}else if(attributeConfigModel.getName().equalsIgnoreCase("dlAction")){
			if(attributeConfigModel.isRequired()){
				value="Add";
			}
		}else if(attributeConfigModel.getDataFormatKey()!=null){
			//TODO for id and value
			/*
			AdhocCache adhocCache = AdhocCacheImpl.getInstance();
			AdhocFilterModel adhocFilterModel= null;
			Map<String, Map<String,String>> pulldown =null;
			
			try {
				adhocFilterModel = adhocCache.getFilterDefinition(
						user, attributeConfigModel.getDataFormatKey());
				pulldown =SSFormater.getSSFormater().getPulldown(user, null,
						adhocFilterModel,attributeConfigModel.getDataFormatKey());
			} catch (RemoteException e) {
				throw new ServiceException(e);
			}
			if(pulldown!=null && pulldown.size()>0){
				if(attributeConfigModel.getDelimitedKey()== null ){
					value=pulldown.getPulldownItem(0).getId();
				}else if(pulldown.size()>=1){
					value=pulldown.getPulldownItem(0).getId()+attributeConfigModel.getDelimitedKey()+pulldown.getPulldownItem(1).getId();					
				}
			}
			*/
		}else{
			String attClassName = attributeConfigModel.getDataType();
			
			if ("int".equals(attClassName)
					|| "java.lang.Integer".equals(attClassName)) {
				value="0";
			} else if ("double".equals(attClassName)
					|| "java.lang.Double".equals(attClassName)) {
				value="0.00";
			} else if ("long".equals(attClassName)
					|| "java.lang.Long".equals(attClassName)) {
				value="999";
			} else if ("float".equals(attClassName)
					|| "java.lang.Float".equals(attClassName)) {
				value="0.00";
			} else if ("char".equals(attClassName)
					|| "java.lang.Character".equals(attClassName)) {
				value="";
			} else if ("boolean".equals(attClassName)
					|| "java.lang.Boolean".equals(attClassName)) {

				value="Yes";
			} else if ("short".equals(attClassName)
					|| "java.lang.Short".equals(attClassName)) {
				value="0";
			} else if ("java.lang.StringBuffer".equals(attClassName)
					|| "java.lang.StringBuilder".equals(attClassName)
					|| "java.lang.String".equals(attClassName)) {
				if(attributeConfigModel.getHeaderName() !=null && attributeConfigModel.getHeaderName().toLowerCase().contains("currency")){
					value="USD";
				}else if(attributeConfigModel.getName().toLowerCase().contains("code") || attributeConfigModel.getHeaderName().toLowerCase().contains("code") ){
						value="Some Unique Code ";
					}else if("btTimesheetTemplateDefStartTime".equalsIgnoreCase(attributeConfigModel.getHeaderName())||"btTimesheetTemplateDefEndTime".equalsIgnoreCase(attributeConfigModel.getHeaderName())){
						value="00:00";
					}else if(attributeConfigModel.getName().toLowerCase().contains("rate")){
						value="0.00";
					}else{
						value="Some Text Value";
					}
			} else if ("java.util.Date".equals(attClassName)) {
				value="21-Jul-2013";
			} 
		}
		if(value.contains(">")||value.contains("<")||value.contains("&")||value.contains(";")||value.contains("#")){
			value="<![CDATA["+value+"]]>";
		}
		return value;
	}

	/**
	 * 
	 * @param objectConfigModel
	 * @param indentation
	 * @param user
	 * @return
	 * @throws ServiceException
	 */
	public String getXSD(ObjectConfigBean objectConfigModel,
			String indentation,boolean isForInternalUse, UserLoginBean user) throws ServiceException {
		StringBuilder xml = new StringBuilder(indentation);
		StringBuilder xmlType = new StringBuilder(indentation);
		List<ObjectConfigBean> objConfigModels = new ArrayList<ObjectConfigBean>();
		List<AttributeConfigBean> attributeConfigModels = objectConfigModel
				.getAttributeConfigModels();
		if (attributeConfigModels == null) {
			return xml.toString();
		}
		xml.append(NEXT_LINE).append(LT_TAG).append("xs:element ").append(" name=\"").append(
				objectConfigModel.getTabOrFileName().replace("#", "")+"S")
		.append("\"").append(">");
		xml.append(NEXT_LINE).append("	<xs:complexType>");
		xml.append(NEXT_LINE).append("		<xs:sequence>");
		xml.append(NEXT_LINE).append("			").append(LT_TAG).append("xs:element ").append(" name=\"").append(
				objectConfigModel.getTabOrFileName().replace("#", ""))
		.append("\" ").append(" minOccurs=\"0\" maxOccurs=\"unbounded\" ").append(">");
		xml.append(NEXT_LINE).append("				").append("<xs:complexType>");
		xml.append(NEXT_LINE).append("					").append("<xs:sequence>");
		// xml.append(NEXT_LINE).append(LT_TAG).append(value)
		for (AttributeConfigBean attributeConfigModel : attributeConfigModels) {
			if (attributeConfigModel.isAttributeAvailableInBo()|| attributeConfigModel.getName().equalsIgnoreCase("dlAction")) {
				if (attributeConfigModel.getChildObjectId() == null) {
					String typeText = getXSDTypeText(attributeConfigModel,
							xmlType,isForInternalUse,user);
					xml.append(NEXT_LINE).append("						").append(LT_TAG).append("xs:element ")
							.append(typeText).append(" name=\"").append(
									attributeConfigModel.getXmlTagName())
							.append("\" ");
					appendAdditionalXSDAttribute(isForInternalUse, xml,
							attributeConfigModel);
					xml.append("/>");
					// xml.append(NEXT_LINE).append(LT_TAG).append("xs:complexType")

				} else {
					ObjectConfigBean tempObjectConfigModel = getObjectConfigModel(
							attributeConfigModel.getChildObjectId(), user);
					if (attributeConfigModel.getCollectionDataType() == null) {
						xml.append(NEXT_LINE).append("						").append("<xs:element name=\"")
								.append(
										tempObjectConfigModel
												.getTabOrFileName().replace("#", "")).append(
										"\" type=\"").append(
										tempObjectConfigModel
												.getTabOrFileName().replace("#", "")).append(
										"type\" ");
						appendAdditionalXSDAttribute(isForInternalUse, xml,
								attributeConfigModel);
						xml.append("/>");
					} else {
						xml.append(NEXT_LINE).append("						").append("<xs:element name=\"")
								.append(
										tempObjectConfigModel
												.getTabOrFileName().replace("#", "")).append(
										"S\" ");
						appendAdditionalXSDAttribute(isForInternalUse, xml,
								attributeConfigModel);
						xml.append(" >");
						xml.append(NEXT_LINE).append("							").append("<xs:complexType>");
						xml.append(NEXT_LINE).append("								").append("	<xs:sequence>");
						xml.append(NEXT_LINE).append("									").append("<xs:element name=\"")
								.append(
										tempObjectConfigModel
												.getTabOrFileName().replace("#", "")).append(
										"\" type=\"").append(
										tempObjectConfigModel
												.getTabOrFileName().replace("#", "")).append(
										"type\" ");
						
						appendAdditionalXSDAttribute(isForInternalUse, xml,
								attributeConfigModel);
						xml.append("/>");
						xml.append(NEXT_LINE).append("								").append(" </xs:sequence> ");
						xml.append(NEXT_LINE).append("							").append("</xs:complexType>");
						xml.append(NEXT_LINE).append("						").append("</xs:element>");
						
					}
					objConfigModels.add(tempObjectConfigModel);

				}
			}
		}
		
		xml.append(NEXT_LINE).append("					").append("</xs:sequence>");
		xml.append(NEXT_LINE).append("				").append("</xs:complexType>");
		xml.append(NEXT_LINE).append("			").append("</xs:element>");
		xml.append(NEXT_LINE).append("		").append("</xs:sequence>");
		xml.append(NEXT_LINE).append("	").append("</xs:complexType>");
		xml.append(NEXT_LINE).append("</xs:element>");
		for (ObjectConfigBean tempObjectConfigModel : objConfigModels) {
			updateTypeDetails(xmlType, tempObjectConfigModel,isForInternalUse, user);
		}

		return "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">"
				+ NEXT_LINE + xmlType.toString() + NEXT_LINE + xml.toString()
				+ NEXT_LINE + "</xs:schema>";

	}
	/**
	 * append additional attribute like min and max occurs
	 * @param isForInternalUse
	 * @param xml
	 * @param attributeConfigModel
	 */
	private void appendAdditionalXSDAttribute(boolean isForInternalUse,
			StringBuilder xml, AttributeConfigBean attributeConfigModel) {
		if(!isForInternalUse){
			if(attributeConfigModel.isRequired()){
				xml.append(" minOccurs=\"1\" ");
			}else{
				xml.append(" minOccurs=\"0\" ");
			}
			if(attributeConfigModel.getCollectionDataType() == null){
				xml.append(" maxOccurs=\"1\" ");
			}else{
				if(attributeConfigModel.getDelimitedKey()==null){
					xml.append(" maxOccurs=\"unbounded\" ");
				}else{
					xml.append(" maxOccurs=\"1\" ");
				}
			}
		}else{
			xml.append(" minOccurs=\"0\" ");
			xml.append("maxOccurs=\"unbounded\" ");
		}
	}
	/**
	 * update type details in xsd
	 * @param xmlType
	 * @param objectConfigModel
	 * @param isForInternalUse
	 * @param user
	 * @throws ServiceException
	 */
	private void updateTypeDetails(StringBuilder xmlType,
			ObjectConfigBean objectConfigModel,boolean isForInternalUse, UserLoginBean user)
			throws ServiceException {
		List<ObjectConfigBean> objConfigModels = new ArrayList<ObjectConfigBean>();
		List<AttributeConfigBean> attributeConfigModels = objectConfigModel
				.getAttributeConfigModels();
		if (attributeConfigModels == null) {
			return;
		}

		xmlType.append(NEXT_LINE).append("	<xs:complexType name=\"").append(
				objectConfigModel.getTabOrFileName().replace("#", "")).append("type\">");
		xmlType.append(NEXT_LINE).append("		<xs:sequence>");
		// xml.append(NEXT_LINE).append(LT_TAG).append(value)
		for (AttributeConfigBean attributeConfigModel : attributeConfigModels) {
			if (attributeConfigModel.isAttributeAvailableInBo()) {
				if (attributeConfigModel.getChildObjectId() == null) {
					String typeText = getXSDTypeText(attributeConfigModel,
							xmlType,isForInternalUse,user);
					xmlType.append(NEXT_LINE).append("			").append(LT_TAG).append(
							"xs:element ").append(typeText).append(" name=\"")
							.append(attributeConfigModel.getXmlTagName())
							.append("\"");
					appendAdditionalXSDAttribute(isForInternalUse, xmlType,
							attributeConfigModel);
					xmlType.append("/>");
					// xml.append(NEXT_LINE).append(LT_TAG).append("xs:complexType")

				} else {
					ObjectConfigBean tempObjectConfigModel = getObjectConfigModel(
							attributeConfigModel.getChildObjectId(), user);
					if (attributeConfigModel.getCollectionDataType() == null) {
						xmlType.append(NEXT_LINE).append("			").append("<xs:element name=\"")
								.append(
										tempObjectConfigModel
												.getTabOrFileName().replace("#", "")).append(
										"\" type=\"").append(
										tempObjectConfigModel
												.getTabOrFileName().replace("#", "")).append(
										"type\" ");
						appendAdditionalXSDAttribute(isForInternalUse, xmlType,
								attributeConfigModel);
						xmlType.append("/>");
					} else {
						xmlType.append(NEXT_LINE).append("			").append("<xs:element name=\"")
								.append(
										tempObjectConfigModel
												.getTabOrFileName().replace("#", "")).append(
										"S\" ");
						appendAdditionalXSDAttribute(isForInternalUse, xmlType,
								attributeConfigModel);
						xmlType.append(" >");
						xmlType.append(NEXT_LINE).append("				").append("<xs:complexType>");
						xmlType.append(NEXT_LINE).append("					").append("<xs:sequence>");
						xmlType.append(NEXT_LINE).append("						").append("<xs:element name=\"")
								.append(
										tempObjectConfigModel
												.getTabOrFileName().replace("#", "")).append(
										"\" type=\"").append(
										tempObjectConfigModel
												.getTabOrFileName().replace("#", "")).append(
										"type\" ");
						appendAdditionalXSDAttribute(isForInternalUse, xmlType,
								attributeConfigModel);
						xmlType.append("/>");
						xmlType.append(NEXT_LINE).append("					").append("</xs:sequence> ");
						xmlType.append(NEXT_LINE).append("				").append("</xs:complexType>");
						xmlType.append(NEXT_LINE).append("			").append("</xs:element>");
					}
					objConfigModels.add(tempObjectConfigModel);

				}
			}
		}
		xmlType.append(NEXT_LINE).append("		</xs:sequence>");
		xmlType.append(NEXT_LINE).append("	</xs:complexType>");
		for (ObjectConfigBean tempObjectConfigModel : objConfigModels) {
			updateTypeDetails(xmlType, tempObjectConfigModel,isForInternalUse, user);
		}

	}
	/**
	 * get XSD Data Type
	 * @param attributeConfigModel
	 * @param xmlTypeMaster
	 * @param isForInternalUse
	 * @param user
	 * @return
	 * @throws ServiceException
	 */
	private String getXSDTypeText(AttributeConfigBean attributeConfigModel,
			StringBuilder xmlTypeMaster,boolean isForInternalUse,UserLoginBean user) throws  ServiceException {
		String attClassName = attributeConfigModel.getDataType();
		StringBuilder typetext = new StringBuilder(" type=\"");
		int length = attributeConfigModel.getLength();
		int decimalLength = attributeConfigModel.getDecimalPlaces();
		StringBuilder xmlType = new StringBuilder();
// Need to discuss whether we need to provide code based validation Rules in XSD or not		
		if(!isForInternalUse && attributeConfigModel.getDataFormatKey() != null && attributeConfigModel.getDelimitedKey() == null ){
			getXMLTypeForCodeBasedValue(attributeConfigModel, xmlTypeMaster,
					user, typetext, length, xmlType);
		}else if ("int".equals(attClassName)
				|| "java.lang.Integer".equals(attClassName)) {
			if (attributeConfigModel.getDataFormatKey() != null || attributeConfigModel.getDelimitedKey() != null) {
				typetext.append("xs:string\" ");
			} else {
				typetext.append("xs:integer\" ");
			}
		} else if ("double".equals(attClassName)
				|| "java.lang.Double".equals(attClassName)) {
			if (attributeConfigModel.getDataFormatKey() != null ||attributeConfigModel.getDelimitedKey() != null) {
				typetext.append("xs:string\" ");
			}else if(isForInternalUse ){
				typetext.append("xs:double\" ");
			} else if (decimalLength > 0 || length > 0) {
				String code = "double_" + length + "_" + decimalLength;
				if (xmlTypeMaster.indexOf("<xs:simpleType name=\"" + code
						+ "\"") < 0) {
					xmlType.append("<xs:simpleType name=\"");
					xmlType.append(code).append("\">\n");
					xmlType.append("		 <xs:restriction base=\"xs:decimal\">\n");
					// xmlType.append("		   <xs:minLength value=\"5\"/>");
					if (length > 0) {
						xmlType.append("		   <xs:maxLength value=\"").append(
								length).append("\"/>\n");
					}
					if (decimalLength > 0) {
					//	xmlType
					//			.append("		    <xs:minExclusive value=\"0\"/>\n");
						xmlType.append("		    <xs:fractionDigits value=\"")
								.append(decimalLength).append("\"/>\n");
					}
					xmlType.append("		 </xs:restriction>\n");
					xmlType.append("</xs:simpleType>\n");
					xmlTypeMaster.insert(0, xmlType);
				}
				typetext.append(code).append("\" ");

			} else {
				typetext.append("xs:double\" ");
			}
		} else if ("long".equals(attClassName)
				|| "java.lang.Long".equals(attClassName)) {
			if (attributeConfigModel.getDataFormatKey() != null || attributeConfigModel.getDelimitedKey() != null) {
				typetext.append("xs:string\" ");
			} else if(isForInternalUse ){
				typetext.append("xs:long\" ");
			} else if (decimalLength > 0 || length > 0) {
				String code = "long_" + length + "_" + decimalLength;
				if (xmlTypeMaster.indexOf("<xs:simpleType name=\"" + code + "\"") < 0) {
					xmlType.append("<xs:simpleType name=\"");
					xmlType.append(code).append("\">\n");
					xmlType.append("		 <xs:restriction base=\"xs:long\">\n");
					// xmlType.append("		   <xs:minLength value=\"5\"/>");
					if (length > 0) {
						xmlType.append("		   <xs:maxLength value=\"").append(
								length).append("\"/>\n");
					}
					
					xmlType.append("		 </xs:restriction>\n");
					xmlType.append("</xs:simpleType>\n");
					xmlTypeMaster.insert(0, xmlType);
				}
				typetext.append(code).append("\" ");

			} else {
				typetext.append("xs:long\" ");
			}
		} else if ("float".equals(attClassName)
				|| "java.lang.Float".equals(attClassName)) {
			if (attributeConfigModel.getDataFormatKey() != null ||attributeConfigModel.getDelimitedKey() != null) {
				typetext.append("xs:string\" ");
			} else if(isForInternalUse ){
				typetext.append("xs:float\" ");
			} else	if (decimalLength > 0 || length > 0) {
				String code = "float_" + length + "_" + decimalLength;
				if (xmlTypeMaster.indexOf("<xs:simpleType name=\"" + code + "\"") < 0) {
					xmlType.append("<xs:simpleType name=\"");
					xmlType.append(code).append("\">\n");
					xmlType.append("		 <xs:restriction base=\"xs:decimal\">\n");
					// xmlType.append("		   <xs:minLength value=\"5\"/>");
					if (length > 0) {
						xmlType.append("		   <xs:maxLength value=\"").append(
								length).append("\"/>\n");
					}
					if (decimalLength > 0) {
						//xmlType
						//		.append("		    <xs:minExclusive value=\"0\"/>\n");
						xmlType.append("		    <xs:fractionDigits value=\"")
								.append(decimalLength).append("\"/>\n");
					}
					xmlType.append("		 </xs:restriction>\n");
					xmlType.append("</xs:simpleType>\n");
					xmlTypeMaster.insert(0, xmlType);
				}
				typetext.append(code).append("\" ");

			} else {
				typetext.append("xs:float\" ");
			}
		} else if ("char".equals(attClassName)
				|| "java.lang.Character".equals(attClassName)) {
			typetext.append("xs:string\" ");
		} else if ("boolean".equals(attClassName)
				|| "java.lang.Boolean".equals(attClassName)) {

			String code = "spboolean";
			if(isForInternalUse ){
				typetext.append("xs:string\" ");
			} else if (xmlTypeMaster.indexOf("<xs:simpleType name=\"" + code + "\"") < 0) {
				xmlType.append("<xs:simpleType name=\"");
				xmlType.append(code).append("\">\n");
				xmlType.append("		 <xs:restriction base=\"xs:string\">\n");
				// xmlType.append("		   <xs:minLength value=\"5\"/>");
				if (length > 0) {
					xmlType.append("		   <xs:maxLength value=\"")
							.append(length).append("\"/>\n");
				}
				xmlType
						.append(" 		<xs:enumeration value=\"Yes\"/>\n 		<xs:enumeration value=\"No\"/>\n");

				xmlType.append("		 </xs:restriction>\n");
				xmlType.append("</xs:simpleType>\n");
				xmlTypeMaster.insert(0, xmlType);
				typetext.append(code).append("\" ");
			}
			
		} else if ("short".equals(attClassName)
				|| "java.lang.Short".equals(attClassName)) {
			typetext.append("xs:short\" ");
		} else if ("java.lang.StringBuffer".equals(attClassName)
				|| "java.lang.StringBuilder".equals(attClassName)
				|| "java.lang.String".equals(attClassName)) {
			if(isForInternalUse ){
				typetext.append("xs:string\" ");
			}else if (length > 0) {
				String code = "string_" + length ;
				if (xmlTypeMaster.indexOf("<xs:simpleType name=\"" + code + "\"") < 0) {
					xmlType.append("<xs:simpleType name=\"");
					xmlType.append(code).append("\">\n");
					xmlType.append("		 <xs:restriction base=\"xs:string\">\n");
					// xmlType.append("		   <xs:minLength value=\"5\"/>");
					if (length > 0) {
						xmlType.append("		   <xs:maxLength value=\"").append(
								length).append("\"/>\n");
					}
					xmlType.append("		 </xs:restriction>\n");
					xmlType.append("</xs:simpleType>\n");
					xmlTypeMaster.insert(0, xmlType);
				}
				typetext.append(code).append("\" ");

			} else {
				typetext.append("xs:string\" ");
			}
		} else if ("java.util.Date".equals(attClassName)) {
			String code = "SPDate";
			/*if (xmlTypeMaster.indexOf("<xs:simpleType name=\"" + code + "\"") < 0) {
				xmlType.append("<xs:simpleType name=\"");
				xmlType.append(code).append("\">\n");
				xmlType.append("		 <xs:restriction base=\"xs:string\">\n");
				xmlType.append("		   <xs:pattern value=\"\\d{2}-(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)-\\d{4}$\"/>\n");
				xmlType.append("		 </xs:restriction>\n");
				xmlType.append("</xs:simpleType>\n");
				xmlTypeMaster.insert(0, xmlType);
			}
			typetext.append(code).append("\" ");*/
			typetext.append("xs:string\" ");
		} else {
			typetext.append("xs:byte\" ");
		}

		return typetext.toString();

	}

	private boolean isStaticData(String dataFormatKey) {
		// TODO Auto-generated method stub
		return false;
	}

	private void getXMLTypeForCodeBasedValue(
			AttributeConfigBean attributeConfigModel,
			StringBuilder xmlTypeMaster, UserLoginBean user,
			StringBuilder typetext, int length, StringBuilder xmlType)
			throws ServiceException {
		String code = attributeConfigModel.getDataFormatKey();
		if (xmlTypeMaster.indexOf("<xs:simpleType name=\"" + code
				+ "\"") < 0) {
			xmlType.append("<xs:simpleType name=\"");
			xmlType.append(code).append("\">\n");
			xmlType.append("		 <xs:restriction base=\"xs:string\">\n");
			// xmlType.append("		   <xs:minLength value=\"5\"/>");
			if (length > 0) {
				xmlType.append("		   <xs:maxLength value=\"").append(
						length).append("\"/>\n");
			}
			//TODO ID and value
			
			/*
			AdhocCache adhocCache = AdhocCacheImpl.getInstance();
			AdhocFilterModel adhocFilterModel= null;
			Pulldown pulldown =null;
			
			try {
				adhocFilterModel = adhocCache.getFilterDefinition(
						user, attributeConfigModel.getDataFormatKey());
				pulldown =SSFormater.getSSFormater().getPulldown(user, null,
						adhocFilterModel,attributeConfigModel.getDataFormatKey());
			} catch (ProservServiceException e) {
				throw new ServiceException(e);
			} catch (RemoteException e) {
				throw new ServiceException(e);
			}
		
			if(pulldown != null){
				for(String availableCode :pulldown.getItemIdsArray()){
					String tempCode =(availableCode+"");
					tempCode = tempCode.replaceAll("&", "&amp;");
					xmlType
					.append(" 			<xs:enumeration value=\"").append(tempCode).append("\"/>\n ");
				}
				if(attributeConfigModel.getValueBasedResolver()!=null && attributeConfigModel.getValueBasedResolver().containsKey("#ALL")){
					xmlType
					.append(" 			<xs:enumeration value=\"").append("#ALL").append("\"/>\n ");
				}
				xmlType
				.append(" 			<xs:enumeration value=\"").append("").append("\"/>\n ");
			}
			xmlType.append("		 </xs:restriction>\n");
			xmlType.append("</xs:simpleType>\n");
			xmlTypeMaster.insert(0, xmlType);
				*/
		}
		typetext.append(code).append("\" ");
	}

	private String getValueWithoutEscapeChar(String msg) {
		// TODO Auto-generated method stub
		return (msg == null) ? "" : msg;
	}

	private String getAttributeValue(AttributeConfigBean attributeConfigModel,
			Object boModel,Map errorMap, UserLoginBean user) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		String[] multiLavelAttributeName = StringUtils.split(attributeConfigModel.getFullyQualifiedName(), ".");
		Object value="";
		if(multiLavelAttributeName.length>1){ 
			Object parentObj = boModel;
			for(int index=0;index<multiLavelAttributeName.length-1;index++){
				String multiLavelAttributeNames = multiLavelAttributeName[index];
				Object parentLevelObject = PropertyUtils.getProperty(parentObj, multiLavelAttributeNames);
				if(parentLevelObject == null){
					break;
				}
				parentObj = parentLevelObject;
			}
			//value = PropertyUtils.getProperty(parentObj, multiLavelAttributeName[multiLavelAttributeName.length-1]);
			String attName = multiLavelAttributeName[multiLavelAttributeName.length - 1];
			Class type = PropertyUtils.getPropertyType(parentObj, attName);
			if ("java.lang.Boolean".equalsIgnoreCase(type.getName())
					|| "boolean".equalsIgnoreCase(type.getName())
					&& attributeConfigModel.getName().startsWith("is")) {
				value = PropertyUtils.getProperty(parentObj, attName.substring(
						2, 3).toLowerCase()
						+ attName.substring(3));
			} else {
				value = PropertyUtils.getProperty(parentObj, attName);
			}
		}else{
			Class type = PropertyUtils.getPropertyType(boModel,
					attributeConfigModel.getName());
			if(type != null && ("java.lang.Boolean".equalsIgnoreCase(type.getName() )  || "boolean".equalsIgnoreCase(type.getName() )) && attributeConfigModel.getName()!= null && attributeConfigModel.getName().startsWith("is")){
				value = PropertyUtils.getProperty(boModel,
						attributeConfigModel.getName().substring(2,3).toLowerCase()+attributeConfigModel.getName().substring(3));
			}else{
			value = PropertyUtils.getProperty(boModel, attributeConfigModel
				.getName());
			}
		}
		value = getAttributeValue(user,errorMap, attributeConfigModel, value);
		return (value == null) ? "" : value.toString();
	}

	private ObjectConfigBean getObjectConfigModel(Long childObjectId,
			UserLoginBean user) throws ServiceException {

		MetaDataConfigService metaDataConfigService = new MetaDataConfigServiceImpl();
		return metaDataConfigService.getSpecificObjectMetaDataFromCache(
				childObjectId, user);
	}

	private String getAttributeValueWithoutEscapeChar(
			List<AttributeConfigBean> attributeConfigModels, Object boModel) {
		
		return " ";
	}
public static void main(String[] args) {
	String str="12-Jun-0000";
	boolean s =str.matches("\\d{2}-(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)-\\d{4}$");
	System.out.println(s);
}

/**
 * get formated value
 * @param attributeConfigModel
 * @param keyValue
 * @return
 */
protected String getFormatedValue(AttributeConfigBean attributeConfigModel,
		String keyValue) {
	if (attributeConfigModel.getDecimalPlaces() > -1 && !keyValue.contains("|")) {
		String decimalFormat = "";
		for (int decimalFormatCounter = 0; decimalFormatCounter < attributeConfigModel
				.getDecimalPlaces(); decimalFormatCounter++) {
			decimalFormat = decimalFormat + "#";
		}
		if (decimalFormat.equals("")) {
			decimalFormat = "###";
		} else {
			decimalFormat = "###." + decimalFormat;
		}
		double d = Double.parseDouble(keyValue);
		NumberFormat formatter = new DecimalFormat(decimalFormat);
		keyValue = formatter.format(d);
	}
	return keyValue;
}
}
