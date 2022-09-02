package com.assignsecurities.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.domain.ApplicationModel;
import com.assignsecurities.domain.ApplicationQuestionerModel;
import com.assignsecurities.domain.QuestionerModel;

@Repository
public class ApplicationRepo {

	@Autowired
	private NamedParameterJdbcTemplate template;

	public ApplicationModel getById(Long id, UserLoginBean userLoginBean) {
		try {
			String query = "SELECT distinct a.* FROM application a "
					+ " inner join applicationscript ascript on a.id=ascript.ApplicationId "
					+ " inner join script s on ascript.ScriptId=s.id  WHERE a.id=:id";
			if (AppConstant.USER_TYPE_END_USER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
				query = query +" and a.UserId =:userId ";
			}else if (AppConstant.USER_TYPE_ADMIN.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())
					|| AppConstant.USER_TYPE_CC.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
				//
			}else {
				return null;
			}
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("id", id);
			sqlParameterSource.addValue("userId", userLoginBean.getId());
//			System.out.println("ApplicationRepo.getById(Long, UserLoginBean)==>"+query);
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<ApplicationModel> getApplicationByStatus(String status, UserLoginBean userLoginBean) {
		try {
			String query = "SELECT a.*,sum(s.MarketPrice) totalValue FROM application a "
					+ " inner join applicationscript ascript on a.id=ascript.ApplicationId "
					+ " inner join script s on ascript.ScriptId=s.id "
					+ " WHERE a.Status=:status ";
			if (AppConstant.USER_TYPE_END_USER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
				query = query +" and a.UserId =:userId ";
			}
			query = query + " group by a.Id,a.FirstName,MiddleName,LastName,CompanyName,Status,UserId,FeeType,FeeValue,CreatedDate,Remarks "
					+ " order by id desc";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("status", status);
			sqlParameterSource.addValue("userId", userLoginBean.getId());
			return template.query(query, sqlParameterSource, mapObjectWithTotalValue());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	public void addAll(List<ApplicationModel> applicationModels) {
		String query = "insert into `application` (FirstName,MiddleName,LastName,CompanyName,Status,UserId,FeeType,FeeValue,CreatedDate,Remarks) "
				+ "values(:firstName,:middleName,:lastName,:companyName,:status,:userId,feeType,:feeValue,:createdDate,:remarks)";

		List<Map<String, Object>> batchValues = new ArrayList<>(applicationModels.size());
		applicationModels.stream().forEach(model -> {
			batchValues.add(new MapSqlParameterSource("firstName", model.getFirstName())
					.addValue("middleName", model.getMiddleName()).addValue("lastName", model.getLastName())
					.addValue("companyName", model.getCompanyName()).addValue("status", model.getStatus())
					.addValue("userId", model.getUserId()).addValue("feeType", model.getFeeType())
					.addValue("feeValue", model.getFeeValue()).addValue("createdDate", model.getCreatedDate())
					.addValue("remarks", model.getRemarks()).getValues());
		});
		template.batchUpdate(query, batchValues.toArray(new Map[applicationModels.size()]));
	}

	public Long add(ApplicationModel model) {
		String query = "insert into `application` (FirstName,MiddleName,LastName,CompanyName,Status,UserId,FeeType,FeeValue,CreatedDate,Remarks) "
				+ "values(:firstName,:middleName,:lastName,:companyName,:status,:userId,feeType,:feeValue,:createdDate,:remarks)";
		KeyHolder keyHolder = new GeneratedKeyHolder();

		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("firstName", model.getFirstName())
				.addValue("middleName", model.getMiddleName()).addValue("lastName", model.getLastName())
				.addValue("companyName", model.getCompanyName()).addValue("status", model.getStatus())
				.addValue("userId", model.getUserId()).addValue("feeType", model.getFeeType())
				.addValue("feeValue", model.getFeeValue()).addValue("createdDate", model.getCreatedDate())
				.addValue("remarks", model.getRemarks());

		template.update(query, sqlParameterSource, keyHolder);
		return keyHolder.getKey().longValue();
	}

	public void update(ApplicationModel model) {
		String query = "update `application` set FirstName=:firstName,MiddleName=:middleName,LastName=:lastName,CompanyName=:companyName,"
				+ "Status=:status,UserId=:userId,FeeType=:feeType,FeeValue=:feeValue,Remarks=:remarks where Id=:id ";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("firstName", model.getFirstName())
				.addValue("middleName", model.getMiddleName()).addValue("lastName", model.getLastName())
				.addValue("companyName", model.getCompanyName()).addValue("status", model.getStatus())
				.addValue("userId", model.getUserId()).addValue("feeType", model.getFeeType())
				.addValue("feeValue", model.getFeeValue()).addValue("remarks", model.getRemarks())
				.addValue("id", model.getId());
		int updatCount = template.update(query, sqlParameterSource);
		System.out.println("updatCount===>" + updatCount);
	}

	public void add(ApplicationQuestionerModel model) {
		String query = "insert into `applicationscriptques` (ApplicationScriptId,QuestionId,Answer) "
				+ "values(:applicationScriptId, :questionId, :answer)";
		model.getQuestioners().forEach(q -> {
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("applicationScriptId",
					model.getApplicationScriptId()).addValue("questionId", q.getId()).addValue("answer",
							q.getAnswer());
			template.update(query, sqlParameterSource);
		});
	}

	public void deleteApplicationscriptques(Long applicationScriptId) {
		String query = "delete from `applicationscriptques` where applicationScriptId =:applicationScriptId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("applicationScriptId",
				applicationScriptId);
		template.update(query, sqlParameterSource);
	}

	public ApplicationQuestionerModel getApplicationQuestioner(Long applicationScriptId) {
		try {
			String query = "SELECT * FROM applicationscriptques asq inner join questioner q  on asq.QuestionId = q.ID WHERE ApplicationScriptId=:applicationScriptId";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("applicationScriptId", applicationScriptId);
			List<QuestionerModel> questioners = template.query(query, sqlParameterSource,
					mapObjectApplicationQuestione());
			return ApplicationQuestionerModel.builder().applicationScriptId(applicationScriptId)
					.questioners(questioners).build();
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	
	public ApplicationQuestionerModel getApplicationQuestionerAns(Long applicationScriptId) {
		try {
			String query = "SELECT q.id as Id, q.*,asq.*,apps.ApplicationId FROM applicationscriptques asq\r\n" + 
					" inner join questioner q  on asq.QuestionId = q.ID \r\n" + 
					"inner join applicationscript apps on apps.id =asq.ApplicationScriptId\r\n" + 
					"inner join application a on a.id=apps.ApplicationId\r\n" + 
					"where ApplicationScriptId =:applicationScriptId";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("applicationScriptId", applicationScriptId);
			List<QuestionerModel> questioners = template.query(query, sqlParameterSource,
					mapObjectApplicationQuestione());
			return ApplicationQuestionerModel.builder().applicationScriptId(applicationScriptId)
					.questioners(questioners).build();
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	

	public ApplicationQuestionerModel getApplicationQuestionerAnsByApplicationId(Long applicationId) {
		try {
			String query = "SELECT q.id as Id, q.*,asq.*,apps.id ParentScriptId FROM applicationscriptques asq " + 
					" inner join questioner q  on asq.QuestionId = q.ID  " + 
					" inner join applicationscript apps on apps.id =asq.ApplicationScriptId " + 
					" inner join application a on a.id=apps.ApplicationId " + 
					" where a.id =:applicationId";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("applicationId", applicationId);
			List<QuestionerModel> questioners = template.query(query, sqlParameterSource,
					mapObjectApplicationQuestioneWithAppScriptId());
			return ApplicationQuestionerModel.builder()//.applicationScriptId(applicationScriptId)
					.questioners(questioners).build();
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public Boolean hasAllQuestionairForScripts(List<Long> scriptIds) {
		try {
			String query = "select count(distinct appsc.ScriptId) from applicationscript appsc \r\n" + 
					"	inner join applicationscriptques asq \r\n" + 
					"	on appsc.id = asq.ApplicationScriptId\r\n" + 
					"	where appsc.ScriptId in(:scriptIds)";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("scriptIds", scriptIds);
			Long count = template.queryForObject(query,sqlParameterSource, Long.class);
			return count== scriptIds.size();
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	private RowMapper<QuestionerModel> mapObjectApplicationQuestioneWithAppScriptId() {
		return (result, i) -> {
			return QuestionerModel.builder().id(result.getLong("id"))
					.question(result.getString("Question")).answer(result.getString("Answer")).parentScriptId(result.getLong("ParentScriptId")).build();
		};
	}

	private RowMapper<QuestionerModel> mapObjectApplicationQuestione() {
		return (result, i) -> {
			return QuestionerModel.builder().id(result.getLong("id"))
					.question(result.getString("Question")).answer(result.getString("Answer")).build();
		};
	}

	private RowMapper<ApplicationModel> mapObject() {
		return (result, i) -> {
			return ApplicationModel.builder().id(result.getLong("id")).firstName(result.getString("FirstName"))
					.middleName(result.getString("MiddleName")).lastName(result.getString("LastName"))
					.companyName(result.getString("CompanyName")).status(result.getString("Status"))
					.userId(result.getLong("UserId")).feeType(result.getString("FeeType"))
					.feeValue(result.getDouble("FeeValue"))
					.createdDate(Objects.isNull(result.getDate("CreatedDate")) ? null
							: result.getTimestamp("CreatedDate").toLocalDateTime())
					.remarks(result.getString("Remarks")).build();
		};
	}
	
	private RowMapper<ApplicationModel> mapObjectWithTotalValue() {
		return (result, i) -> {
			return ApplicationModel.builder().id(result.getLong("id")).firstName(result.getString("FirstName"))
					.middleName(result.getString("MiddleName")).lastName(result.getString("LastName"))
					.companyName(result.getString("CompanyName")).status(result.getString("Status"))
					.userId(result.getLong("UserId")).feeType(result.getString("FeeType"))
					.feeValue(result.getDouble("FeeValue"))
					.createdDate(Objects.isNull(result.getDate("CreatedDate")) ? null
							: result.getTimestamp("CreatedDate").toLocalDateTime())
					.remarks(result.getString("Remarks")).totalValue(result.getDouble("totalValue")).build();
		};
	}
	
}
