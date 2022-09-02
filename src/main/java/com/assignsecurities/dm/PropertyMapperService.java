package com.assignsecurities.dm;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.app.util.DMConstants;
import com.assignsecurities.app.util.StringUtil;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.domain.dm.AttributeConfigBean;
import com.assignsecurities.domain.dm.ObjectConfigBean;
import com.assignsecurities.domain.dm.excel.ExcelDataRowModel;




public class PropertyMapperService {
	private static final Logger logger = LogManager.getLogger(PropertyMapperService.class);
	//MetaDataConfigService metaDataConfigService= new MetaDataConfigServiceImpl();
	private static final PropertyMapperService propertyMapper= new PropertyMapperService();
	private PropertyMapperService(){
		super();
	}
	public static PropertyMapperService getPropertyMapper(){
				return propertyMapper;
	}
	/*public void updatePropertyValue(AttributeConfigModel parentAttributeConfigModel,AttributeConfigModel attributeConfigModel,Object inputObject,Object valueObject) throws ServiceException{
		if(parentAttributeConfigModel!= null && parentAttributeConfigModel.getChildObjectId() != null ){
			UserLoginBean user = AuthDAOFactory.getDAO().loginUser(SPMSysConstants.SYSTEM_USER_LOGIN_NAME,
					Long.parseLong(ConfigService
							.getConfigProperty("elance_sys_owner_id")));
			ObjectConfigModel objectConfigModel = metaDataConfigService.getSpecificObjectMetaDataFromCache(parentAttributeConfigModel.getChildObjectId(), user);
		}
	}*/
	/**
	 * update property value of an object
	 * @param attributeConfigModel
	 * @param valueObject
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void updatePropertyValue(AttributeConfigBean attributeConfigModel, Object inputObject1, Object valueObject, boolean isHierarchyDrillDownReq) throws Exception{
			try {
				Object inputObject = inputObject1;
				if(valueObject==null || !StringUtil.isValidString(String.valueOf(valueObject)) ||  String.valueOf(valueObject.toString()).equalsIgnoreCase("null")){
					return; 
				}
				if(attributeConfigModel.isAttributeAvailableInBo() ){	
					@SuppressWarnings("unused")
					Class propertyType = PropertyUtils.getPropertyType(inputObject, attributeConfigModel.getName());
					if(isHierarchyDrillDownReq){
						String[] multiLavelAttributeName = StringUtils.split(attributeConfigModel.getFullyQualifiedName(), ".");
						if(multiLavelAttributeName.length>1){
							Object parentObj = inputObject1;
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
							inputObject=parentObj;
						}
					}
					if(attributeConfigModel.getDataType().equalsIgnoreCase("java.util.Date") && StringUtil.isValidString(valueObject.toString())){
						if(valueObject instanceof String){
							SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
							try {
								valueObject =dateFormat.parse(String.valueOf(valueObject));
							}catch(java.text.ParseException e1) {
								dateFormat = new SimpleDateFormat("dd-MM-yyyy");
								try {
									valueObject =dateFormat.parse(String.valueOf(valueObject));
								}catch(java.text.ParseException e2) {
									try {
									dateFormat = new SimpleDateFormat("dd/MM/yyyy");
									valueObject =dateFormat.parse(String.valueOf(valueObject));
									}catch(java.text.ParseException e3) {
										try {
											dateFormat = new SimpleDateFormat("MM/dd/yyyy");
											valueObject =dateFormat.parse(String.valueOf(valueObject));
										}catch(java.text.ParseException e4) {
											dateFormat = new SimpleDateFormat("MM/dd/yy");
											valueObject =dateFormat.parse(String.valueOf(valueObject));
										}
									}
								}
							}
						}
					}else {
						if(valueObject instanceof String){							
								valueObject=getNewObjectInstanceWithValue(attributeConfigModel.getDataType(), String.valueOf(valueObject));	
						}
					}
						PropertyUtils.setProperty(inputObject, attributeConfigModel.getName(), valueObject);
					
				}
			} catch (Exception e) {
				logger
				.log(
						null,
						"Error while loading propery value"+ inputObject1+" for"+attributeConfigModel.getName() +" of boejct "+valueObject,
						e);
				throw e;
			}
	       
		
	}
	@SuppressWarnings("unchecked")
	public  void updateParentObjectAttribute(
			ObjectConfigBean objectConfigModel,
			AttributeConfigBean parentAttributeConfigModel, Object model,
			ExcelDataRowModel parentDataRowModel) throws ServiceException {
		try{
			if(DMConstants.ARRAY_TYPE_OBJECT.equalsIgnoreCase(objectConfigModel.getObjectType())){
				Object existingArray = PropertyUtils.getProperty(parentDataRowModel.getRowBOModel(),parentAttributeConfigModel.getName());
				if(existingArray==null){
					existingArray = Array.newInstance(getClass(objectConfigModel.getBusinessObjectClassName()), 1);
					Array.set(existingArray,0,model);
					PropertyUtils.setProperty(parentDataRowModel.getRowBOModel(),parentAttributeConfigModel.getName(),existingArray);
				}else{
					int length = Array.getLength(existingArray);
					Object newArray = Array.newInstance(getClass(objectConfigModel.getBusinessObjectClassName()),length+1);					
					System.arraycopy(existingArray, 0, newArray, 0, length);
					Array.set(newArray,length,model);
					PropertyUtils.setProperty(parentDataRowModel.getRowBOModel(),parentAttributeConfigModel.getName(),newArray);
				}
				
			//	existingArray
				
			}else if(DMConstants.LIST_TYPE_OBJECT.equalsIgnoreCase(objectConfigModel.getObjectType())){
				Object existingListObj = PropertyUtils.getProperty(parentDataRowModel.getRowBOModel(),parentAttributeConfigModel.getName());
				List existingList = null;
				if(existingListObj==null){
					existingList= new ArrayList(0);
				}else{
					existingList =(List)existingListObj; 
				}
				existingList.add(model);	
				PropertyUtils.setProperty(parentDataRowModel.getRowBOModel(),parentAttributeConfigModel.getName(),existingList);
			}else if(DMConstants.MAP_TYPE_OBJECT.equalsIgnoreCase(objectConfigModel.getObjectType())){
				
			}else if(DMConstants.SET_TYPE_OBJECT.equalsIgnoreCase(objectConfigModel.getObjectType())){
				Object existingSetObj = PropertyUtils.getProperty(parentDataRowModel.getRowBOModel(),parentAttributeConfigModel.getName());
				Set existingSet = null;
				if(existingSetObj==null){
					existingSet =  new HashSet(0);				
				}else{
					existingSet =(Set)existingSetObj;
				}
				PropertyUtils.setProperty(parentDataRowModel.getRowBOModel(),parentAttributeConfigModel.getName(),existingSet);
				
			}else if(DMConstants.VECTOR_TYPE_OBJECT.equalsIgnoreCase(objectConfigModel.getObjectType())){
				Object existingVectorObj = PropertyUtils.getProperty(parentDataRowModel.getRowBOModel(),parentAttributeConfigModel.getName());
				Vector existingVector = null;
				if(existingVectorObj==null){
					existingVector = new Vector();					
				}else{
					existingVector = (Vector) existingVectorObj;
				}
				PropertyUtils.setProperty(parentDataRowModel.getRowBOModel(),parentAttributeConfigModel.getName(),existingVector);
				
			}else{
				updatePropertyValue(parentAttributeConfigModel, parentDataRowModel.getRowBOModel(), model,false);
			}
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Update property values
	 * @param objectConfigModel
	 * @param parentAttributeConfigModel
	 * @param model
	 * @param parentBOObject
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public  void updateParentObjectAttribute(
			ObjectConfigBean objectConfigModel,
			AttributeConfigBean parentAttributeConfigModel, Object model,
			Object parentBOObject) throws ServiceException {
		try{
			if(DMConstants.ARRAY_TYPE_OBJECT.equalsIgnoreCase(objectConfigModel.getObjectType())){
				Object existingArray = PropertyUtils.getProperty(parentBOObject,parentAttributeConfigModel.getName());
				if(existingArray==null){
					existingArray = Array.newInstance(getClass(objectConfigModel.getBusinessObjectClassName()), 1);
					Array.set(existingArray,0,model);
					PropertyUtils.setProperty(parentBOObject,parentAttributeConfigModel.getName(),existingArray);
				}else{
					int length = Array.getLength(existingArray);
					Object newArray = Array.newInstance(getClass(objectConfigModel.getBusinessObjectClassName()),length+1);					
					System.arraycopy(existingArray, 0, newArray, 0, length);
					Array.set(newArray,length,model);
					PropertyUtils.setProperty(parentBOObject,parentAttributeConfigModel.getName(),newArray);
				}
				
			//	existingArray
				
			}else if(DMConstants.LIST_TYPE_OBJECT.equalsIgnoreCase(objectConfigModel.getObjectType())){
				Object existingListObj = PropertyUtils.getProperty(parentBOObject,parentAttributeConfigModel.getName());
				List existingList = null;
				if(existingListObj==null){
					existingList= new ArrayList(0);
				}else{
					existingList =(List)existingListObj; 
				}
				existingList.add(model);	
				PropertyUtils.setProperty(parentBOObject,parentAttributeConfigModel.getName(),existingList);
			}else if(DMConstants.MAP_TYPE_OBJECT.equalsIgnoreCase(objectConfigModel.getObjectType())){
				
			}else if(DMConstants.SET_TYPE_OBJECT.equalsIgnoreCase(objectConfigModel.getObjectType())){
				Object existingSetObj = PropertyUtils.getProperty(parentBOObject,parentAttributeConfigModel.getName());
				Set existingSet = null;
				if(existingSetObj==null){
					existingSet =  new HashSet(0);				
				}else{
					existingSet =(Set)existingSetObj;
				}
				PropertyUtils.setProperty(parentBOObject,parentAttributeConfigModel.getName(),existingSet);
				
			}else if(DMConstants.VECTOR_TYPE_OBJECT.equalsIgnoreCase(objectConfigModel.getObjectType())){
				Object existingVectorObj = PropertyUtils.getProperty(parentBOObject,parentAttributeConfigModel.getName());
				Vector existingVector = null;
				if(existingVectorObj==null){
					existingVector = new Vector();					
				}else{
					existingVector = (Vector) existingVectorObj;
				}
				PropertyUtils.setProperty(parentBOObject,parentAttributeConfigModel.getName(),existingVector);
				
			}else{
				updatePropertyValue(parentAttributeConfigModel, parentBOObject, model,true);
			}
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}
	/**
	 * update models value from list
	 * @param attributeConfigModel
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws NegativeArraySizeException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public static Object getDummyCollectionInstance(AttributeConfigBean attributeConfigModel) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, NegativeArraySizeException, ClassNotFoundException{
		List  valueList = new ArrayList();
		if(DMConstants.ARRAY_TYPE_OBJECT.equalsIgnoreCase(attributeConfigModel.getCollectionDataType())){
			return Array.newInstance(getClass(attributeConfigModel.getDataType()), valueList.size());
		}else if(DMConstants.LIST_TYPE_OBJECT.equalsIgnoreCase(attributeConfigModel.getCollectionDataType())){
			return valueList;		
		}else if(DMConstants.MAP_TYPE_OBJECT.equalsIgnoreCase(attributeConfigModel.getCollectionDataType())){
			
		}else if(DMConstants.SET_TYPE_OBJECT.equalsIgnoreCase(attributeConfigModel.getCollectionDataType())){
			return new HashSet();			
			
		}else if(DMConstants.VECTOR_TYPE_OBJECT.equalsIgnoreCase(attributeConfigModel.getCollectionDataType())){
			return  new Vector(); 
		}else{
			return null;
		}
		return null;
		
	}
	/**
	 * update models value from list
	 * @param attributeConfigModel
	 * @param valueList
	 * @param boModels
	 * @param currentBoModel
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws NegativeArraySizeException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public void updateAttributeValueFromDeliminatedValue(AttributeConfigBean attributeConfigModel,List<Object> valueList,List<Object> boModels,Object currentBoModel) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, NegativeArraySizeException, ClassNotFoundException{
		boModels.remove(boModels.size()-1);
		if(DMConstants.ARRAY_TYPE_OBJECT.equalsIgnoreCase(attributeConfigModel.getCollectionDataType())){
			Object existingArray = Array.newInstance(getClass(attributeConfigModel.getDataType()), valueList.size());			
			for(int index=0;index<valueList.size();index++){
				try {
					Array.set(existingArray,index,getNewObjectInstanceWithValue(attributeConfigModel.getDataType(),String.valueOf(valueList.get(index))));
				} catch (Exception e) {
					// ignour this add proper log
					//e.printStackTrace();
				} 
			}
			PropertyUtils.setProperty(currentBoModel,attributeConfigModel.getName(),existingArray);	
			boModels.add(currentBoModel);
		}else if(DMConstants.LIST_TYPE_OBJECT.equalsIgnoreCase(attributeConfigModel.getCollectionDataType())){
			PropertyUtils.setProperty(currentBoModel,attributeConfigModel.getName(),valueList);	
			boModels.add(currentBoModel);		
		}else if(DMConstants.MAP_TYPE_OBJECT.equalsIgnoreCase(attributeConfigModel.getCollectionDataType())){
			
		}else if(DMConstants.SET_TYPE_OBJECT.equalsIgnoreCase(attributeConfigModel.getCollectionDataType())){
			Set valueSet = new HashSet();
			valueSet.addAll(valueList);
			PropertyUtils.setProperty(currentBoModel,attributeConfigModel.getName(),valueSet);	
			boModels.add(currentBoModel);	
			
		}else if(DMConstants.VECTOR_TYPE_OBJECT.equalsIgnoreCase(attributeConfigModel.getCollectionDataType())){
			Vector newVector = new Vector(); 
			newVector.addAll(valueList);
			PropertyUtils.setProperty(currentBoModel,attributeConfigModel.getName(),newVector);
			boModels.add(currentBoModel);	
		}else{
			String values ="";
			for(Object obj : valueList){
				values = values + obj+",";
			}
			if(values.indexOf(",")>0){
				values=values.substring(0,values.lastIndexOf(","));
			}
			PropertyUtils.setProperty(currentBoModel,attributeConfigModel.getName(),values);
			boModels.add(currentBoModel);	
		}
	}
	@SuppressWarnings("unchecked")
	public  Class getWrapperClass(String className) throws ClassNotFoundException{
		 if("int" .equals(className)) return Integer.class;
		 if("long".equals(className)) return Long.class;
		 if("double".equals(className)) return Double.class;
		 if("float".equals(className)) return Float.class;
		 if("short".equals(className)) return Short.class;
		 if("char".equals(className)) return Character.class;
		 if("boolean".equals(className)) return Boolean.class;
		 if("byte".equals(className)) return Byte.class;
		 return Class.forName(className);
	}
	
	@SuppressWarnings("unchecked")
	public static  Class getClass(String className) throws ClassNotFoundException{
		 if("int" .equals(className)) return int.class;
		 if("long".equals(className)) return long.class;
		 if("double".equals(className)) return double.class;
		 if("float".equals(className)) return float.class;
		 if("short".equals(className)) return short.class;
		 if("char".equals(className)) return char.class;
		 if("boolean".equals(className)) return boolean.class;
		 if("byte".equals(className)) return byte.class;
		 return Class.forName(className);
	}
	/**
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	@SuppressWarnings("unchecked")
	public Object getNewObjectInstance(String className) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		Object boObject = null;
		Class boClass = getClass(className);
		if(boClass.isPrimitive()){
			if("int" .equals(boClass.getName())){
				boObject = Integer.valueOf("0");
			}else if("long".equals(boClass.getName())){
				boObject = 0L;
			}else if("double".equals(boClass.getName())) {
				boObject = Double.valueOf("0");
			}else if("float".equals(boClass.getName())){
				boObject =  Float.valueOf("0");
			}else if("short".equals(boClass.getName())){
				boObject = Short.valueOf("0");
			}else if("byte".equals(boClass.getName())){
				boObject = Byte.valueOf("0");
			}else if("boolean".equals(boClass.getName())){
				boObject = Boolean.valueOf("false");
			}else if("char".equals(boClass.getName())){
					boObject = Character.valueOf(" ".charAt(0));
				
			}
		} else if(boClass.getSuperclass() == Number.class){
				Class[] parameterTypes ={String.class};
				boObject = boClass.getConstructor(parameterTypes).newInstance("0"); 
			}else{
			boObject = boClass.newInstance();
		}
		return boObject;
	}
	/**
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	@SuppressWarnings("unchecked")
	public Object getNewObjectInstanceWithValue(String className,String value) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		Object boObject = null;
		Class boClass = getClass(className);
		if(boClass.isPrimitive()){
			if("int" .equals(boClass.getName())){
				boObject = Integer.valueOf(value);
			}else if("long".equals(boClass.getName())){
				boObject = Long.parseLong(value);
			}else if("double".equals(boClass.getName())) {
				boObject = Double.valueOf(value);
			}else if("float".equals(boClass.getName())){
				boObject =  Float.valueOf(value);
			}else if("short".equals(boClass.getName())){
				boObject = Short.valueOf(value);
			}else if("byte".equals(boClass.getName())){
				boObject = Byte.valueOf(value);
			}else if("boolean".equals(boClass.getName())){
				boObject = Boolean.valueOf(value);
			}else if("char".equals(boClass.getName())){
					boObject = Character.valueOf(" ".charAt(0));
				
			}
		}else if(boClass.getSuperclass() == Number.class){
				Class[] parameterTypes ={String.class};
				boObject = boClass.getConstructor(parameterTypes).newInstance(value); 
		}else if("java.util.Date".equals(boClass.getName())){
			boObject = value; 
		}else{
			Class[] parameterTypes ={String.class};
			boObject = boClass.getConstructor(parameterTypes).newInstance(value);
		}
		return boObject;
	}
	/**
	 * Format objects
	 * @param value
	 * @param attributeConfigModel
	 * @return
	 * @throws ServiceException 
	 */
	@SuppressWarnings("unchecked")
	public Object getFormatedValue(String value,
			AttributeConfigBean attributeConfigModel, UserLoginBean uam)
			throws ServiceException {
		Object formatedValue = "";
		if (StringUtil.isValidString(value) && !"null".equalsIgnoreCase(value)) {
			// first apply value based resolver
			if (attributeConfigModel.getValueBasedResolver() != null
					&& !attributeConfigModel.getValueBasedResolver().isEmpty()
					&& attributeConfigModel.getValueBasedResolver()
							.containsKey(value)) {
				String className = attributeConfigModel.getValueBasedResolver()
						.get(value);
				try {
					Class resolverClass = Class.forName(className);
					Class[] parameterTypes = { String.class, Map.class,
							UserLoginBean.class };
					Object[] parameterValue = { value, null, uam };
					Method resolverMethod = resolverClass.getDeclaredMethod(
							"resolve", parameterTypes);
					return resolverMethod.invoke(resolverClass.newInstance(),
							parameterValue);
				} catch (Exception e) {
					throw new ServiceException(e);
				}
			}
			if (attributeConfigModel.getDataFormatKey() != null
					&& !attributeConfigModel.getDataFormatKey().isEmpty()) {
				// apply formatter if resolver does not resolve value
				formatedValue = SSFormater.getSSFormater().getCodeValue(
						attributeConfigModel.getDataFormatKey(), value, uam);
				try {
					formatedValue = getNewObjectInstanceWithValue(
							attributeConfigModel.getDataType(), formatedValue
									.toString());
				} catch (Exception e) {
					throw new ServiceException(e);
				}
			}else{
				formatedValue =value;
			}
		} else if ("".equalsIgnoreCase(value)
				&& attributeConfigModel.isRequired()) {
			throw new ServiceException(attributeConfigModel.getHeaderName()
					+ " is required cannot be set as null");

		}
		return formatedValue;
	}

	/**
	 * Format objects for output file
	 * 
	 * @param value
	 * @param attributeConfigModel
	 * @return
	 * @throws ServiceException
	 */
	public Object getCodeValue(String value,
			AttributeConfigBean attributeConfigModel, UserLoginBean uam)
			throws ServiceException {
		Object formatedValue = "";
		// first apply value based resolver
		if (attributeConfigModel.getValueBasedResolver() != null
				&& !attributeConfigModel.getValueBasedResolver().isEmpty()) {
			if (value.equalsIgnoreCase(String
					.valueOf(DMConstants.HASH_ALL_NUMBER))) {
				value = DMConstants.HASH_ALL;
				return value;
			} else if (value.equalsIgnoreCase(String
					.valueOf(DMConstants.HASH_DEFAULT_NUMBER))) {
				return DMConstants.HASH_DEFAULT;
			} else if (value.equalsIgnoreCase("01-Jan-1900")) {
				return DMConstants.HASH_DEFAULT;
			} else if (value.equalsIgnoreCase(String
					.valueOf(DMConstants.HASH_REMAINING_NUMBER))) {
				return DMConstants.HASH_REMAINING;
			}
		}
		// apply formatter if resolver does not resolve value
		formatedValue = SSFormater.getSSFormater().getIdValue(
				attributeConfigModel.getDataFormatKey(), value, uam);

		return formatedValue;
	}
	/**
	 * Reassemble object if required.This function is used to populate dependent values in Model
	 * @param objectToBeAssempled
	 * @param objectConfigModel
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public void assambleObject(Object objectToBeAssempled,ObjectConfigBean objectConfigModel,UserLoginBean uam)throws ServiceException {
		try{
			String objectAssemblerClassName = objectConfigModel.getObjectAssemplerClassName();
			if(StringUtil.isValidString(objectAssemblerClassName )){
				Class objectAssemblerClass = Class.forName(objectAssemblerClassName);
				ObjectAssembler assembler = (ObjectAssembler)objectAssemblerClass.newInstance();
				assembler.assemble(objectToBeAssempled,uam);
			}
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}
	/**
	 * Reassemble object if required.This function is used to populate dependent values in Model
	 * @param objectConfigModel
	 * @return Object
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public Object disAssambleObject(Object objectToBeDisAssempled,ObjectConfigBean objectConfigModel,UserLoginBean uam,AutowireCapableBeanFactory beanFactory)throws ServiceException {
		Object returnObject = objectToBeDisAssempled;
		try{
			String objectDisAssemblerClassName = objectConfigModel.getObjectDisAssemplerClassName();
			if(StringUtil.isValidString(objectDisAssemblerClassName)){
				Class objectDisAssemblerClass = Class.forName(objectDisAssemblerClassName);
				ObjectDisAssembler assembler = (ObjectDisAssembler)objectDisAssemblerClass.newInstance();
				beanFactory.autowireBean(assembler);
				returnObject = assembler.disAssemble(objectToBeDisAssempled,uam);
			}
		}catch(Exception e){
			throw new ServiceException(e);
		}
		return returnObject;
	}
	/**
	 * check whether it is numeric data or not
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str)
	{
	    return str.matches("[+-]?\\d*(\\.\\d+)?");
	}
}
