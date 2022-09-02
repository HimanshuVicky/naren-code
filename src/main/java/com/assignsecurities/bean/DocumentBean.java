package com.assignsecurities.bean;

import java.time.LocalDateTime;

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
public class DocumentBean implements java.io.Serializable {

	private static final long serialVersionUID = -723431997261020665L;
	private Long id;
	private String name;
	private String type;
	private LocalDateTime createdDate;
	private Long createBy;
	private String contentType;
	
	private FileBean file;

}
