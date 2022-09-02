
package com.assignsecurities.domain.dm;

import java.io.Serializable;

public interface DataColumnModel extends Serializable{
	public Object getColValue();
	public void setColValue(Object colValue);
	public int getColIndex();
	public void setColIndex(int colIndex);
	public String getColName();
	public void setColName(String colName);
	public String getErrorMessage();
	public void setErrorMessage(String errorMessage);
}
