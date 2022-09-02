package com.assignsecurities.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.assignsecurities.domain.CaseQuestionerModel;
import com.assignsecurities.domain.QuestionerModel;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class CaseQuestionerRepo {
	@Autowired
	private NamedParameterJdbcTemplate template;

	public void add(CaseQuestionerModel model) {
		String query = "insert into `casescriptques` (CaseScriptId,QuestionId,Answer) "
				+ "values(:caseScriptId, :questionId, :answer)";
		model.getQuestioners().forEach(q -> {
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseScriptId",
					model.getCaseScriptId()).addValue("questionId", q.getId()).addValue("answer",
							q.getAnswer());
			template.update(query, sqlParameterSource);
		});
	}
	
	public void addAll(List<CaseQuestionerModel> models) {
		String query = "insert into `casescriptques` (CaseScriptId,QuestionId,Answer) "
				+ "values(:caseScriptId, :questionId, :answer)";
		
		List<Map<String, Object>> batchValues = new ArrayList<>();
		models.stream().forEach(model -> {
			model.getQuestioners().forEach(q -> {
				batchValues.add( new MapSqlParameterSource("caseScriptId",
						model.getCaseScriptId()).addValue("questionId", q.getId()).addValue("answer",
								q.getAnswer()).getValues());
			});
		});
		template.batchUpdate(query, batchValues.toArray(new Map[batchValues.size()]));
		
	}

	public void deleteCaseScriptQues(Long caseScriptId) {
		String query = "delete from `casescriptques` where caseScriptId =:caseScriptId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseScriptId",
				caseScriptId);
		template.update(query, sqlParameterSource);
	}
	
	public void deleteCaseScriptQuesCaseAndScriptId(Long caseId, Long scriptId) {
		String query = "delete from `casescriptques` "
				+ " where CaseScriptId in(select Id from casescript where caseId=:caseId and ScriptId=:scriptId)";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseId",
				caseId).addValue("scriptId", scriptId);
		template.update(query, sqlParameterSource);
	}


	public CaseQuestionerModel getCaseQuestioner(Long caseScriptId) {
		try {
			String query = "SELECT * FROM casescriptques asq inner join questioner q  on asq.QuestionId = q.ID WHERE CaseScriptId=:caseScriptId";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("caseScriptId", caseScriptId);
			List<QuestionerModel> questioners = template.query(query, sqlParameterSource,
					mapObjectCaseQuestione());
			return CaseQuestionerModel.builder().caseScriptId(caseScriptId)
					.questioners(questioners).build();
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	
	public CaseQuestionerModel getCaseQuestionerAns(Long caseScriptId) {
		try {
			String query = "SELECT q.id as Id, q.*,asq.*,asq.CaseScriptId  FROM casescriptques asq\r\n" + 
					" inner join questioner q  on asq.QuestionId = q.ID \r\n" + 
					"inner join casescript apps on apps.id =asq.CaseScriptId\r\n" + 
					"inner join `case` a on a.id=apps.CaseId\r\n" + 
					"where CaseScriptId =:caseScriptId";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("caseScriptId", caseScriptId);
			List<QuestionerModel> questioners = template.query(query, sqlParameterSource,
					mapObjectCaseQuestioneWithScript());
			return CaseQuestionerModel.builder().caseScriptId(caseScriptId)
					.questioners(questioners).build();
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	

	public CaseQuestionerModel getCaseQuestionerAnsByCaseId(Long caseId) {
		try {
			String query = "SELECT q.id as Id, q.*,asq.*,asq.CaseScriptId,apps.ScriptId as ScriptId  FROM casescriptques asq " + 
					" inner join questioner q  on asq.QuestionId = q.ID  " + 
					" inner join casescript apps on apps.id =asq.CaseScriptId " + 
					" inner join `case` a on a.id=apps.CaseId " + 
					" where a.id =:caseId";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("caseId", caseId);
			List<QuestionerModel> questioners = template.query(query, sqlParameterSource,
					mapObjectCaseQuestioneWithScript());
			return CaseQuestionerModel.builder()//.caseScriptId(caseScriptId)
					.questioners(questioners).build();
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public CaseQuestionerModel getDistinctQuestionerAnsByCaseId(Long caseId) {
		try {
			String query = "SELECT distinct q.Question,asq.Answer FROM casescriptques asq  " + 
					"	 inner join questioner q  on asq.QuestionId = q.ID   " + 
					"	 inner join casescript apps on apps.id =asq.CaseScriptId   " + 
					"	 inner join `case` a on a.id=apps.CaseId " + 
					" where a.id =:caseId";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("caseId", caseId);
			List<QuestionerModel> questioners = template.query(query, sqlParameterSource,
					mapObjectDistincCaseQuestione());
			return CaseQuestionerModel.builder()//.caseScriptId(caseScriptId)
					.questioners(questioners).build();
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	private RowMapper<QuestionerModel> mapObjectDistincCaseQuestione() {
		return (result, i) -> {
			return QuestionerModel.builder().question(result.getString("Question"))
					.answer(result.getString("Answer")).build();
		};
	}
	
	private RowMapper<QuestionerModel> mapObjectCaseQuestioneWithScript() {
		return (result, i) -> {
			return QuestionerModel.builder().id(result.getLong("id")).question(result.getString("Question"))
					.answer(result.getString("Answer")).parentScriptId(result.getLong("asq.CaseScriptId"))
					.scriptId(result.getLong("ScriptId")).build();
		};
	}

	private RowMapper<QuestionerModel> mapObjectCaseQuestione() {
		return (result, i) -> {
			return QuestionerModel.builder().id(result.getLong("id"))
					.question(result.getString("Question")).answer(result.getString("Answer")).build();
		};
	}
}
