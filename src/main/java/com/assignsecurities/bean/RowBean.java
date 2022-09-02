package com.assignsecurities.bean;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RowBean {
	
	private int rowNumber;
	Integer fromCell;
	Integer toCell;
	private int lastColWidht;
	
	private List<ColumnBean> columns;
}
