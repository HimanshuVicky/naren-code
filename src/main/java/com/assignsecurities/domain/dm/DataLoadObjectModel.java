package com.assignsecurities.domain.dm;

import java.util.Map;

public interface DataLoadObjectModel {
	/**
	 * 
	 * @return
	 */
	public Object getBusinessObjectModel();

	/**
	 * 
	 * @param bo
	 */
	public void setBusinessObjectModel(Object bo);

	/**
	 * 
	 * @return
	 */
	public Map<String, DataRowModel> getDataRowsMap();

	/**
	 * 
	 * @return
	 */
	public void addDataRow(String key, DataRowModel row);

	/**
	 * 
	 * @return
	 */
	public DataRowModel getDataRow(String key);

	/**
	 * 
	 * @return
	 */
	public String getAction();
	/**
	 * 
	 * @return
	 */
	public void setAction(String action);
	/**
	 * 
	 * @return
	 */
	public boolean isError();
	/**
	 * 
	 * @return
	 */
	public void setError(Boolean isBoolean);
	/**
	 * 
	 * @param rowMap
	 */
	public void setDataRowsMap(Map<String, DataRowModel> rowMap);

}
