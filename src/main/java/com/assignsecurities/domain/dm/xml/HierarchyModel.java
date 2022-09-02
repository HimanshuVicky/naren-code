package com.assignsecurities.domain.dm.xml;

import com.assignsecurities.domain.dm.AttributeConfigBean;

public class HierarchyModel {
     private HierarchyModel parentObject;
     private Object currentObject;
     private AttributeConfigBean attributeConfigModel;
     private boolean isCollectionTypeData = false;
	
	public Object getCurrentObject() {
		return currentObject;
	}
	public void setCurrentObject(Object currentObject) {
		this.currentObject = currentObject;
	}
	public void setParentObject(HierarchyModel parentObject) {
		this.parentObject = parentObject;
	}
	public HierarchyModel getParentObject() {
		return parentObject;
	}
	public void setCollectionTypeData(boolean isCollectionTypeData) {
		this.isCollectionTypeData = isCollectionTypeData;
	}
	public boolean isCollectionTypeData() {
		return isCollectionTypeData;
	}
	public void setAttributeConfigModel(AttributeConfigBean attributeConfigModel) {
		this.attributeConfigModel = attributeConfigModel;
	}
	public AttributeConfigBean getAttributeConfigModel() {
		return attributeConfigModel;
	}
	
}
