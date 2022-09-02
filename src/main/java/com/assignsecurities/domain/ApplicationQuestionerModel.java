package com.assignsecurities.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationQuestionerModel {
	private Long applicationScriptId;
	private List<QuestionerModel> questioners;
}
