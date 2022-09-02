package com.assignsecurities.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8679671118393613488L;
	private String fileContent;
	private String fileTitle;
	private String fileContentType;
}
