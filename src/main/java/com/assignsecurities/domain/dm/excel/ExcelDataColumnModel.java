package com.assignsecurities.domain.dm.excel;

import com.assignsecurities.domain.dm.DataColumnModel;

public class ExcelDataColumnModel implements DataColumnModel {
	private Object colValue;
	private int colIndex;
	private String colName;
	private String errorMessage;

	public Object getColValue() {
		return colValue;
	}

	public void setColValue(Object colValue) {
		this.colValue = colValue;
	}

	public int getColIndex() {
		return colIndex;
	}

	public void setColIndex(int colIndex) {
		this.colIndex = colIndex;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
