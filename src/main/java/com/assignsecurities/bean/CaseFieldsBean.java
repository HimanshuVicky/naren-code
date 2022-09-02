package com.assignsecurities.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaseFieldsBean {
	@JsonIgnore
	private String dlAction;
	private String referenceNumber;
	private String srNo;
	private String fieldName;
	private String templateName;
	private String fieldNameHeader;
	private String fieldValue;
}
