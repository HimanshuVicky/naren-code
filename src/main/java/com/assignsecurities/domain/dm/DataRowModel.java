package com.assignsecurities.domain.dm;

import java.io.Serializable;
import java.util.List;

public interface DataRowModel extends Serializable{
	public int getRowIndex();
	public void setRowIndex(int rowIndex);
	public void setColumns(List<DataColumnModel> dataColModels );
	public void addColumns(DataColumnModel dataColModels );
	public List<DataColumnModel> getColumns( );

}
