package com.assignsecurities.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentMasterBean implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6374090061646746843L;
	private Long id;
	private String particulars;
	private Integer tempOrder;
	private String type;
	private String uploadedOrGenerated;
	private String action;
	private String process;
	private String link;
}
