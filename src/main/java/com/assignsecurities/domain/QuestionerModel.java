package com.assignsecurities.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionerModel {
	private Long id;
	private String question;
	private Boolean isActive;
	private String answer;
	private Long parentScriptId;
	private Long scriptId;
}
