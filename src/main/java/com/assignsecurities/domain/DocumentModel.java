package com.assignsecurities.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -723431997261020665L;
	private Long id;
	private String name;
	private String type;
	private LocalDateTime createdDate;
	private Long createBy;
	private String bucketName;
	private String contentType;

}
