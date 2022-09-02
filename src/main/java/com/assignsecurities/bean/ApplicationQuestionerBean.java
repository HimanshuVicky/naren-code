package com.assignsecurities.bean;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationQuestionerBean {
	private Long applicationScriptId;
	private List<QuestionerBean> questioners;
	private ScriptBean script;
}
