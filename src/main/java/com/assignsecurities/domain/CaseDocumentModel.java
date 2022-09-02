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
public class CaseDocumentModel {
//	Id,CaseId,DocumentId,Type,CreatedDate,CreateBy
	private Long id;
	private Long caseId;
	private Long documentId;
	private String type;
	private String uploadType;
	private LocalDateTime createdDate;
	private Long createBy;
}
