package com.assignsecurities.repository.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.assignsecurities.domain.CaseLogModel;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class CaseLogRepo {
	@Autowired
	private NamedParameterJdbcTemplate template;

	public CaseLogModel getById(Long id) {
		try {
			String query = "SELECT * FROM caselog WHERE id=:id";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("id", id);
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<CaseLogModel> getByCaseId(Long caseId) {
		try {
			String query = "SELECT * FROM caselog WHERE CaseId=:caseId order by ScriptId";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("caseId", caseId);
			return template.query(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	public Long add(CaseLogModel model) {
		String query = "insert into `caselog` (CaseId,DocumentId,`Action`,CreatedDate,CreateBy,Remarks) "
				+ "values(:caseId,:documentId,:action,:createdDate,:createBy,:remarks)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseId", model.getCaseId())
				.addValue("documentId", model.getDocumentId()).addValue("action", model.getAction())
				.addValue("createdDate",
						Objects.isNull(model.getCreatedDate()) ? LocalDateTime.now() : model.getCreatedDate())
				.addValue("createBy", model.getCreateBy()).addValue("remarks", model.getRemarks());
		template.update(query, sqlParameterSource, keyHolder);
		return keyHolder.getKey().longValue();
	}

	private RowMapper<CaseLogModel> mapObject() {
		return (result, i) -> {
			return CaseLogModel.builder().id(result.getLong("Id")).caseId(result.getLong("CaseId"))
					.documentId(result.getLong("DocumentId"))
					.createdDate(result.getTimestamp("CreatedDate").toLocalDateTime())
					.createBy(result.getString("CreateBy")).remarks(result.getString("remarks")).build();
		};
	}
}
