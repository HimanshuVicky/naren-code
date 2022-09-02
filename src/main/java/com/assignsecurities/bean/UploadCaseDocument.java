package com.assignsecurities.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class UploadCaseDocument implements java.io.Serializable {

	private static final long serialVersionUID = -4713365044122761843L;
	private Long id;
	private String documentType;
	private FileBean document;
	private String uploadType;
	private Long documentId;
}
