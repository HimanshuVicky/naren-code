package com.assignsecurities.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ESignDocumentModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3198202995288413522L;
	private Long Id;
	private Long userId;
	private Long documentId;
	private LocalDateTime createdDate;
}
