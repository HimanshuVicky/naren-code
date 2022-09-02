package com.assignsecurities.repository.impl;

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

import com.assignsecurities.domain.CaseDeathCertificateDtlModel;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class CaseDeathCertificateDtlRepo {
	
	@Autowired
	private NamedParameterJdbcTemplate template;

	public CaseDeathCertificateDtlModel getById(Long id) {
		try {
			String query = "SELECT * FROM case_shareholder_death_certdtl WHERE id=:id";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("id", id);
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<CaseDeathCertificateDtlModel> getByCaseId(Long caseId) {
		try {
			String query = "SELECT * FROM case_shareholder_death_certdtl WHERE caseId=:caseId";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("caseId", caseId);
			return template.query(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	public Long add(CaseDeathCertificateDtlModel model) {
		String query = "insert into `case_shareholder_death_certdtl` (caseId,deceasedName,relation,documentId,dateOfDeath) "
				+ "values(:caseId,:deceasedName,:relation,:documentId,:dateOfDeath)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseId", model.getCaseId())
				.addValue("deceasedName", model.getDeceasedName()).addValue("relation", model.getRelation())
				.addValue("documentId", model.getDocumentId()).addValue("dateOfDeath", model.getDateOfDeath());

		template.update(query, sqlParameterSource, keyHolder);
		return keyHolder.getKey().longValue();
	}

	public int update(CaseDeathCertificateDtlModel model) {
		String query = "update `case_shareholder_death_certdtl` set caseId=:caseId=:,deceasedName=:deceasedName,relation=:relation,documentId=:documentId, dateOfDeath=:dateOfDeath"
				+ " where id = :id ";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseId", model.getCaseId())
				.addValue("deceasedName", model.getDeceasedName()).addValue("relation", model.getRelation())
				.addValue("documentId", model.getDocumentId()).addValue("id", model.getId()).addValue("dateOfDeath", model.getDateOfDeath());
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
		return updatCount;
	}

	private RowMapper<CaseDeathCertificateDtlModel> mapObject() {
		return (result, i) -> {
			return CaseDeathCertificateDtlModel.builder().id(result.getLong("Id")).caseId(result.getLong("CaseId"))
					.DeceasedName(result.getString("deceasedName")).relation(result.getString("relation"))
					.DocumentId(result.getLong("documentId"))
					.dateOfDeath(Objects.isNull(result.getDate("dateOfDeath")) ? null : result.getDate("dateOfDeath").toLocalDate()).build();
		};
	}

}
