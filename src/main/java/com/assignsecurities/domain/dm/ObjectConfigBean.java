package com.assignsecurities.domain.dm;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class ObjectConfigBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String objectName;
	private String businessObjectClassName;
	private String tabOrFileName;
	private int tabIndex;
	private boolean isRequired;
	private boolean isActionColumnRequired;
	private String inputFileFormat;
	private int headerRowNumber = 2;
	private List<AttributeConfigBean> attributeConfigModels;
//	private List<ObjectConfigBean> childObjectList;
	private String primaryColumnIndex="1";
	private String parentReferanceColumnIndex="1";
	private List<Integer> pks ;
	private List<Integer> refKeys;
	private String objectType;
	private String businessValidatorClassName;
	private String businessProcesserClassName;
	private int errorColumnIndex;
	private String objectAssemplerClassName;
	private String objectDisAssemplerClassName;
	private int dataStartRowNo;
	private String dataDownLoadProcessorClassName;
	public int getErrorColumnIndex() {
		return errorColumnIndex;
	}
	public void setErrorColumnIndex(int errorColumnIndex) {
		this.errorColumnIndex = errorColumnIndex;
	}
	/**
	 * @return the primaryColumnIndex
	 */
	public String getPrimaryColumnIndex() {
		return primaryColumnIndex;
	}
	/**
	 * @param primaryColumnIndex the primaryColumnIndex to set
	 */
	public void setPrimaryColumnIndex(String primaryColumnIndex) {
		this.primaryColumnIndex = primaryColumnIndex;
	}
//	public List<ObjectConfigBean> getChildObjectList() {
//		return childObjectList;
//	}
//	public void setChildObjectList(List<ObjectConfigBean> childObjectList) {
//		this.childObjectList = childObjectList;
//	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	public String getBusinessObjectClassName() {
		return businessObjectClassName;
	}
	public void setBusinessObjectClassName(String businessObjectClassName) {
		this.businessObjectClassName = businessObjectClassName;
	}
	public String getTabOrFileName() {
		return tabOrFileName;
	}
	public void setTabOrFileName(String tabOrFileName) {
		this.tabOrFileName = tabOrFileName;
	}
	public boolean isRequired() {
		return isRequired;
	}
	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}
	public String getInputFileFormat() {
		return inputFileFormat;
	}
	public void setInputFileFormat(String inputFileFormat) {
		this.inputFileFormat = inputFileFormat;
	}
	public List<AttributeConfigBean> getAttributeConfigModels() {
		return attributeConfigModels;
	}
	public void setAttributeConfigModels(List<AttributeConfigBean> attributeConfigModels) {
		this.attributeConfigModels = attributeConfigModels;
	}
	public int getHeaderRowNumber() {
		return headerRowNumber;
	}
	public void setHeaderRowNumber(int headerRowNumber) {
		this.headerRowNumber = headerRowNumber;
	}
	public int getTabIndex() {
		return tabIndex;
	}
	public void setTabIndex(int tabIndex) {
		this.tabIndex = tabIndex;
	}
	/**
	 * @return the parentReferanceColumnIndex
	 */
	public String getParentReferanceColumnIndex() {
		return parentReferanceColumnIndex;
	}
	/**
	 * @param parentReferanceColumnIndex the parentReferanceColumnIndex to set
	 */
	public void setParentReferanceColumnIndex(String parentReferanceColumnIndex) {
		this.parentReferanceColumnIndex = parentReferanceColumnIndex;
	}
	
	public List<Integer> getPrimaryKeyIndex(){
		if(pks !=null){
			return pks;
		}
		pks = new ArrayList<Integer>();
		String[] pkIndex = primaryColumnIndex.split(",");
		for(String pk:pkIndex){
			pks.add(Integer.valueOf(pk));
		}
		return pks;
	}
	public List<Integer> getParentRefKeyIndex(){
		if(refKeys !=null){
			return refKeys;
		}
		refKeys = new ArrayList<Integer>();
		String[] refIndex = parentReferanceColumnIndex.split(",");
		for(String refKey:refIndex){
			refKeys.add(Integer.valueOf(refKey));
		}
		return refKeys;
	}
	/**
	 * @return the objectType
	 */
	public String getObjectType() {
		return objectType;
	}
	/**
	 * @param objectType the objectType to set
	 */
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	public String getBusinessValidatorClassName() {
		return businessValidatorClassName;
	}
	public void setBusinessValidatorClassName(String businessValidatorClassName) {
		this.businessValidatorClassName = businessValidatorClassName;
	}
	public String getBusinessProcesserClassName() {
		return businessProcesserClassName;
	}
	public void setBusinessProcesserClassName(String businessProcesserClassName) {
		this.businessProcesserClassName = businessProcesserClassName;
	}
	public String getObjectAssemplerClassName() {
		return objectAssemplerClassName;
	}
	public void setObjectAssemplerClassName(String objectAssemplerClassName) {
		this.objectAssemplerClassName = objectAssemplerClassName;
	}

	/**
	 * @return the dataStartRowNo
	 */
	public int getDataStartRowNo() {
		return dataStartRowNo;
	}
	/**
	 * @param dataStartRowNo the dataStartRowNo to set
	 */
	public void setDataStartRowNo(int dataStartRowNo) {
		this.dataStartRowNo = dataStartRowNo;
	}
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
	    String newLine = System.getProperty("line.separator");

	    result.append( this.getClass().getName() );
	    result.append( " Object {" );
	    result.append(newLine);

	    //determine fields declared in this class only (no fields of superclass)
	    Field[] fields = this.getClass().getDeclaredFields();

	    //print field names paired with their values
	    for ( Field field : fields  ) {
	      result.append("  ");
	      try {
	        result.append( field.getName() );
	        result.append(": ");
	        //requires access to private field:
	        result.append( field.get(this) );
	      }
	      catch ( IllegalAccessException ex ) {
	        System.out.println(ex);//TODO
	      }
	      result.append(newLine);
	    }
	    result.append("}");

	    return result.toString();

	}
	public String getObjectDisAssemplerClassName() {
		return objectDisAssemplerClassName;
	}
	public void setObjectDisAssemplerClassName(
			String objectDisAssemplerClassName) {
		this.objectDisAssemplerClassName = objectDisAssemplerClassName;
	}
	public String getDataDownLoadProcessorClassName() {
		return dataDownLoadProcessorClassName;
	}
	public void setDataDownLoadProcessorClassName(
			String dataDownLoadProcessorClassName) {
		this.dataDownLoadProcessorClassName = dataDownLoadProcessorClassName;
	}
	public boolean isActionColumnRequired() {
		return isActionColumnRequired;
	}
	public void setActionColumnRequired(boolean isActionColumnRequired) {
		this.isActionColumnRequired = isActionColumnRequired;
	}
}
