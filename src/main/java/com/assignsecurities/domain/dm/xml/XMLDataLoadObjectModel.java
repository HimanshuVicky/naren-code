package com.assignsecurities.domain.dm.xml;

import java.util.HashMap;
import java.util.Map;

import com.assignsecurities.domain.dm.DataLoadObjectModel;
import com.assignsecurities.domain.dm.DataRowModel;


public class XMLDataLoadObjectModel implements DataLoadObjectModel {

	Object businessObjectModel;
    String action;
    Map<String, DataRowModel> dataRowMap;
    private boolean isError = false;
    /**
	 * @param dataRowMap the dataRowMap to set
	 */
	public void setDataRowMap(Map<String, DataRowModel> dataRowMap) {
		this.dataRowMap = dataRowMap;
	}
	
	/**
     * 
     */
    public XMLDataLoadObjectModel(){
   	 super();
   	 dataRowMap = new HashMap<String, DataRowModel>();
    }
	
	public Object getBusinessObjectModel() {		
		return businessObjectModel;
	}
	
	public void setBusinessObjectModel(Object bo) {
		this.businessObjectModel = bo;
	}
	
	public Map<String, DataRowModel> getDataRowsMap() {
		return dataRowMap;
	}
	
	public void addDataRow(String key, DataRowModel row) {
		dataRowMap.put(key, row);
		
	}
	
	public DataRowModel getDataRow(String key) {
		return dataRowMap.get(key);
	}

	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	public boolean isError() {
		return isError;
	}

	
	public void setError(Boolean isError) {
		this.isError = isError;	 	
	}
	/**
	 * 
	 * @param rowMap
	 */
	public void setDataRowsMap(Map<String, DataRowModel> rowMap){
		this.dataRowMap=rowMap;
	}
}
