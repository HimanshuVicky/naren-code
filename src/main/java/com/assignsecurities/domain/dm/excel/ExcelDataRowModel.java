package com.assignsecurities.domain.dm.excel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.assignsecurities.domain.dm.DataColumnModel;
import com.assignsecurities.domain.dm.DataRowModel;

public class ExcelDataRowModel implements DataRowModel,Comparable<ExcelDataRowModel> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5361844571025775096L;

	private int rowIndex = 0;
	
	private int tabIndex;
	
	private int headerRowNumber;
	private Object rowBOModel; 
	
	private String key;
	private String errorMessage;
	
	private List<DataColumnModel> columnModels = new ArrayList<DataColumnModel>(
			0);
	private Map<String, List<ExcelDataRowModel>> childRowMap = new LinkedHashMap<String, List<ExcelDataRowModel>>(
			0);

	
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the columnModels
	 */
	public List<DataColumnModel> getColumnModels() {
		return columnModels;
	}

	/**
	 * @param columnModels
	 *            the columnModels to set
	 */
	public void setColumnModels(List<DataColumnModel> columnModels) {
		this.columnModels = columnModels;
	}

	/**
	 * @return the childRowMap
	 */
	public Map<String, List<ExcelDataRowModel>> getChildRowMap() {
		return childRowMap;
	}

	/**
	 * @param childRowMap
	 *            the childRowMap to set
	 */
	public void setChildRowMap(Map<String, List<ExcelDataRowModel>> childRowMap) {
		this.childRowMap = childRowMap;
	}

	/**
	 * 
	 * @param tabNameOrIndex
	 * @param dataRowModel
	 */
	public void addChildRow(String tabNameOrIndex,
			ExcelDataRowModel dataRowModel) {
		if (this.childRowMap.containsKey(tabNameOrIndex)) {
			this.childRowMap.get(tabNameOrIndex).add(dataRowModel);
		} else {
			List<ExcelDataRowModel> newDataRowModels = new ArrayList<ExcelDataRowModel>(
					0);
			newDataRowModels.add(dataRowModel);
			this.childRowMap.put(tabNameOrIndex, newDataRowModels);
		}
	}

	/**
	 * 
	 * @param tabNameOrIndex
	 * @param dataRowModels
	 */
	public void addChildRows(String tabNameOrIndex,
			List<ExcelDataRowModel> dataRowModels) {
		if (this.childRowMap.containsKey(tabNameOrIndex)) {
			this.childRowMap.get(tabNameOrIndex).addAll(dataRowModels);
		} else {
			this.childRowMap.put(tabNameOrIndex, dataRowModels);
		}
	}

	
	public int getRowIndex() {
		return rowIndex;
	}

	
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	
	public void setColumns(List<DataColumnModel> dataColModels) {
		this.columnModels = dataColModels;

	}

	
	public void addColumns(DataColumnModel dataColModels) {
		columnModels.add(dataColModels);
	}

	
	public List<DataColumnModel> getColumns() {
		return columnModels;
	}

	/**
	 * @return the tabIndex
	 */
	public int getTabIndex() {
		return tabIndex;
	}

	/**
	 * @param tabIndex the tabIndex to set
	 */
	public void setTabIndex(int tabIndex) {
		this.tabIndex = tabIndex;
	}

	/**
	 * @return the headerRowNumber
	 */
	public int getHeaderRowNumber() {
		return headerRowNumber;
	}

	/**
	 * @param headerRowNumber the headerRowNumber to set
	 */
	public void setHeaderRowNumber(int headerRowNumber) {
		this.headerRowNumber = headerRowNumber;
	}

	public Object getRowBOModel() {
		return rowBOModel;
	}

	public void setRowBOModel(Object rowBOModel) {
		this.rowBOModel = rowBOModel;
	}
	
	public int compareTo(ExcelDataRowModel excelDataRowModel) {
		return new Integer(rowIndex).compareTo(new Integer(excelDataRowModel.getRowIndex()));
	}
	
}
