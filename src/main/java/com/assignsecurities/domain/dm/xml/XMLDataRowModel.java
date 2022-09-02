package com.assignsecurities.domain.dm.xml;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.assignsecurities.domain.dm.DataColumnModel;
import com.assignsecurities.domain.dm.DataRowModel;
import com.assignsecurities.domain.dm.excel.ExcelDataRowModel;


public class XMLDataRowModel implements DataRowModel,Comparable<XMLDataRowModel> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int rowIndex = 0;	
	private Object rowBOModel; 
	private List<DataColumnModel> columnModels = new ArrayList<DataColumnModel>(
			0);
	private Map<String, List<ExcelDataRowModel>> childRowMap = new LinkedHashMap<String, List<ExcelDataRowModel>>(
			0);

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

	public Object getRowBOModel() {
		return rowBOModel;
	}

	public void setRowBOModel(Object rowBOModel) {
		this.rowBOModel = rowBOModel;
	}
	
	public int compareTo(XMLDataRowModel xmlDataRowModel) {
		return new Integer(rowIndex).compareTo(new Integer(xmlDataRowModel.getRowIndex()));
	}

	
}
