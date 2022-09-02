package com.assignsecurities.dm.writer;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.app.util.DMConstants;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.dm.PropertyMapperService;
import com.assignsecurities.domain.dm.*;




/**
 * This class cantains API to support download current data in system.
 * @author pkumar
 *
 */
public abstract class DownLoadDataWriter {
	protected static SimpleDateFormat dateFormator = null;
	protected String fileName;
	private static final Logger logger = LogManager.getLogger(DownLoadDataWriter.class);
	/**
	 * use to write data one by one on file
	 * @param obj
	 * @param objectConfigModel
	 * @param objectModel
	 * @param dataWriterModel
	 * @param rowCount
	 * @param isMainSheet
	 * @param user
	 * @throws ServiceException
	 */
	public abstract void writeDataToFile(Object obj, ObjectModel objectModel,
										 ObjectConfigBean objectConfigModel, ObjectBean objectBean,
										 DataWriterModel dataWriterModel, int rowCount, boolean isMainSheet,
										 UserLoginBean user) throws ServiceException;
	/**
	 * responsible to generate complete file
	 * @param inputfileName
	 * @param uam
	 * @param objectModel
	 * @param dataWriterModel
	 * @throws ServiceException
	 * @throws IOException
	 */
	public abstract File generateFile(String inputfileName, UserLoginBean uam,
			ObjectBean objectModel,DataWriterModel dataWriterModel) throws ServiceException, IOException;
	/**
	 * retrive property value from model
	 * @param attributeConfigModel
	 * @param obj
	 * @param objectConfigModel
	 * @return
	 * @throws ServiceException 
	 */
	protected Object getValueFromModel(AttributeConfigBean attributeConfigModel,
									   Object obj, ObjectConfigBean objectConfigModel) throws ServiceException {
		try {
			String[] multiLavelAttributeName = StringUtils.split(attributeConfigModel.getFullyQualifiedName(), ".");
			if(multiLavelAttributeName.length>1){
				Object parentObj = obj;
				for(int index=0;index<multiLavelAttributeName.length-1;index++){
					String multiLavelAttributeNames = multiLavelAttributeName[index];
					Object parentLevelObject = PropertyUtils.getProperty(parentObj, multiLavelAttributeNames);
					if(parentLevelObject == null){
						return null;
					}
					parentObj = parentLevelObject;
				}
				Class type = PropertyUtils.getPropertyType(parentObj,
						attributeConfigModel.getName());
				String attributeName=multiLavelAttributeName[multiLavelAttributeName.length-1];
				if("java.lang.Boolean".equalsIgnoreCase(type.getName() )  || "boolean".equalsIgnoreCase(type.getName() ) && attributeConfigModel.getName().startsWith("is")){
					return PropertyUtils.getProperty(parentObj,
							attributeName.substring(2,3).toLowerCase()+attributeName.substring(3));
				}else{
					return PropertyUtils.getProperty(parentObj, multiLavelAttributeName[multiLavelAttributeName.length-1]);
				}
			}
			Class type = PropertyUtils.getPropertyType(obj,
					attributeConfigModel.getName());
			if("java.lang.Boolean".equalsIgnoreCase(type.getName() )  || "boolean".equalsIgnoreCase(type.getName() ) && attributeConfigModel.getName().startsWith("is")){
				return PropertyUtils.getProperty(obj,
						attributeConfigModel.getName().substring(2,3).toLowerCase()+attributeConfigModel.getName().substring(3));
			}else{
				return PropertyUtils.getProperty(obj, attributeConfigModel.getName());
			}
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * Uploads the file.
	 * 
	 * @throws ServiceException
	 * */
	public Long upload(String fileName, File file, String fileExt,ObjectBean objectModel,
			UserLoginBean uam) throws ServiceException {
		/*
		Long fileID = 0L;
		UploadFile newFile = new UploadFile();
		int fileLength = (int) file.length();
		newFile.setFileName(file.getName());
		newFile.setFileExt(fileExt);
		newFile.setSize(fileLength);
		newFile.setRelativeDestFilePathName(file.getAbsolutePath());
		try {
			InputStream inp = new FileInputStream(file);
//			if(getDBType().equalsIgnoreCase("db2")) {
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				int nRead;
				byte[] data = new byte[16384];
				while ((nRead = inp.read(data, 0, data.length)) != -1) {
				  buffer.write(data, 0, nRead);
				}
				buffer.flush();
				byte[] byteStream =buffer.toByteArray();
				newFile.setFileID(FilePersisterUtil.getInstance().persist(newFile,
						byteStream));
//			}else{
//				newFile.setFileID(FilePersisterUtil.getInstance().persist(newFile,
//						inp, fileLength));	
//			}
			//
			getObjectDAO().addDownloadPartIds(objectModel, newFile, uam);	
			fileID = newFile.getFileID();
			inp.close();
			
		} catch (Exception e1) {
			LogService.logError(this.getClass(), "Upload operation failed", e1);
			throw new ServiceException(e1);
		}

		return fileID;
		*/
		return -1L;
	}
	/**
	 * get formated value
	 * @param attributeConfigModel
	 * @param keyValue
	 * @return
	 */
	protected String getFormatedValue(
			AttributeConfigBean attributeConfigModel, String keyValue) {
		if (DMConstants.HASH_ALL.equals(keyValue)
				|| DMConstants.HASH_DEFAULT.equals(keyValue)
				|| DMConstants.HASH_REMAINING.equals(keyValue)) {
			return keyValue;
		}
		if (attributeConfigModel.getDecimalPlaces() > -1
				&& !keyValue.contains("|") && keyValue != null
				&& keyValue.trim().length() > 1) {
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

	/**
	 * return formatted value
	 * 
	 * @param user
	 * @param attributeConfigModel
	 * @param value
	 * @return
	 */
	protected Object getFormattedValue(UserLoginBean user,
			AttributeConfigBean attributeConfigModel, Object value) {
		try {
			if(value==null){
				return "";
			}
			value = String
					.valueOf(PropertyMapperService.getPropertyMapper()
							.getCodeValue(value.toString(),
									attributeConfigModel, user));
		} catch (Exception e) {
			logger.error( "", e);
			// ignoure this exception reset to original value
			if (!(attributeConfigModel.getDataFormatKey() != null && !attributeConfigModel
					.getDataFormatKey().isEmpty() && value!=null)
					&& attributeConfigModel.getValueBasedResolver() != null
					&& !attributeConfigModel.getValueBasedResolver().isEmpty()) {
			   return getResolverString(user, attributeConfigModel, value,"");
			}
			value = "";
		}
		return value;
	}

	/**
	 * handle all delimited values
	 * 
	 * @param user
	 * @param attributeConfigModel
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Object handleDelimitedValue(UserLoginBean user,
			AttributeConfigBean attributeConfigModel, Object value) {
		List<Object> valueList = new ArrayList<Object>();
		
		if (value == null) {
			value = "";
		} else if (value instanceof Collection) {
			valueList.addAll((Collection) value);			
		} else if (DMConstants.ARRAY_TYPE_OBJECT
				.equalsIgnoreCase(attributeConfigModel.getCollectionDataType())) {
			int length = Array.getLength(value);
			for (int i = 0; i < length; i++) {
				valueList.add(Array.get(value, i));
			}
		} else {
			valueList.addAll(Arrays.asList(StringUtils.split(value.toString(),
					attributeConfigModel.getDelimitedKey())));
		}
		if(attributeConfigModel.getValueBasedResolver() != null && !attributeConfigModel.getValueBasedResolver().isEmpty() && !valueList.isEmpty()){
			if(valueList.contains(DMConstants.HASH_ALL_NUMBER)){
				valueList.clear();
				valueList.add(DMConstants.HASH_ALL_NUMBER);
			}
			if(valueList.contains(DMConstants.HASH_DEFAULT_NUMBER)){
				valueList.clear();
				valueList.add(DMConstants.HASH_DEFAULT_NUMBER);
			}
		}
		StringBuffer delimatedValue = new StringBuffer();
		if (valueList.size() > 0) {
			for (Object valueId : valueList) {
				if (valueId == null) {
					continue;
				}
				if (attributeConfigModel.getDataFormatKey() != null
						&& !attributeConfigModel.getDataFormatKey().isEmpty()) {
					try {
						delimatedValue.append(
								PropertyMapperService.getPropertyMapper()
										.getCodeValue(valueId.toString(),
												attributeConfigModel, user))
								.append(attributeConfigModel.getDelimitedKey());
					} catch (Exception e) {
						// ignoure Exception
					}
				} else {
					delimatedValue.append(valueId).append(
							attributeConfigModel.getDelimitedKey());
				}
			}
		}
		if (delimatedValue.length() > 1) {
			value = delimatedValue.substring(0, delimatedValue.length() - 1);
		} else {
			value = "";
		}
		return value;
	}
	/**
	 * get Attribute value 
	 * 
	 * @param user
	 * @param attributeConfigModel
	 * @param value
	 * @return
	 */
	protected Object getAttributeValue(UserLoginBean user, Map errorMap,
			AttributeConfigBean attributeConfigModel, Object value) {
		if (attributeConfigModel.getDelimitedKey() != null
				&& !attributeConfigModel.getDelimitedKey().isEmpty()) {
			value = handleDelimitedValue(user, attributeConfigModel,
					value);
		} else if (attributeConfigModel.getDataFormatKey() != null
				&& !attributeConfigModel.getDataFormatKey().isEmpty()) {
			if(errorMap != null && !errorMap.isEmpty()){
				if("java.lang.Boolean".equalsIgnoreCase(attributeConfigModel.getDataType() )  || "boolean".equalsIgnoreCase(attributeConfigModel.getDataType() ) ){
					value = !Boolean.valueOf(value.toString());
				}else if("YES_NO_TO_Y_N".equalsIgnoreCase(attributeConfigModel.getDataFormatKey())){
					if("Y".equalsIgnoreCase(String.valueOf(value))){
						value="N";
					}else{
						value="Y";
					}
				}
			}
			// populate formated object
			value = getFormattedValue(user, attributeConfigModel, value);
		} else if (value instanceof Date && value != null) {
			value = dateFormator.format(value);
		}
		return value;
	}

	/**
	 * get Attribute value
	 * 
	 * @param user
	 * @param attributeConfigModel
	 * @param value
	 * @return
	 */
	protected Object getAttributeValue(UserLoginBean user,
			AttributeConfigBean attributeConfigModel, Object value) {
		if (attributeConfigModel.getDelimitedKey() != null
				&& !attributeConfigModel.getDelimitedKey().isEmpty()) {
			value = handleDelimitedValue(user, attributeConfigModel, value);
		} else if (attributeConfigModel.getDataFormatKey() != null
				&& !attributeConfigModel.getDataFormatKey().isEmpty()) {
			// populate formated object
			value = getFormattedValue(user, attributeConfigModel, value);
		} else if (value instanceof Date && value != null) {
			value = dateFormator.format(value);
		} else if (!(attributeConfigModel.getDataFormatKey() != null && !attributeConfigModel
				.getDataFormatKey().isEmpty())
				&& attributeConfigModel.getValueBasedResolver() != null
				&& !attributeConfigModel.getValueBasedResolver().isEmpty()) {
			return getResolverString(user, attributeConfigModel, value,String.valueOf(value));
		}
		return value;
	}
	/**
	 * 
	 * @param user
	 * @param attributeConfigModel
	 * @param value
	 * @return
	 */
	private String getResolverString(UserLoginBean user,
			AttributeConfigBean attributeConfigModel, Object value,String returnValue) {
		String value1 = String.valueOf(value);
		double doubleValue = 0;
		try {
			doubleValue = Double.parseDouble(value1);
		} catch (NumberFormatException e1) {
			// ignore
			doubleValue = 0;
		}
		if (attributeConfigModel.getValueBasedResolver() != null
				&& !attributeConfigModel.getValueBasedResolver().isEmpty()) {
			if (value1.equalsIgnoreCase(String
					.valueOf(DMConstants.HASH_ALL_NUMBER))
					|| doubleValue == DMConstants.HASH_ALL_NUMBER) {
				return DMConstants.HASH_ALL;
			} else if (value1.equalsIgnoreCase(String
					.valueOf(DMConstants.HASH_DEFAULT_NUMBER))
					|| doubleValue == DMConstants.HASH_DEFAULT_NUMBER) {
				return DMConstants.HASH_DEFAULT;
			} else if (value1.equalsIgnoreCase("01-Jan-1900")) {
				return DMConstants.HASH_DEFAULT;
			} else if (value1.equalsIgnoreCase(String
					.valueOf(DMConstants.HASH_REMAINING_NUMBER))
					|| doubleValue == DMConstants.HASH_REMAINING_NUMBER) {
				return DMConstants.HASH_REMAINING;
			}
		}
		return returnValue;
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
