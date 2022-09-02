package com.assignsecurities.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ColumnBean {
	private String colValue;
	
	private String hAlognment;
	
	private int colWidht;
}
