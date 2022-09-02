package com.assignsecurities.repository.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.assignsecurities.domain.CaseAccountDetailModel;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class CaseAccountDetailRepo {
	@Autowired
	private NamedParameterJdbcTemplate template;

	public CaseAccountDetailModel getById(Long id) {
		try {
			String query = "SELECT * FROM caseaccountdetails WHERE id=:id";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("id", id);
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}


	public CaseAccountDetailModel getByCaseId(Long caseId) {
		try {
			String query = "SELECT * FROM caseaccountdetails WHERE CaseId=:caseId";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("caseId", caseId);
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Long add(CaseAccountDetailModel model) {
		String query = "insert into `caseaccountdetails` (CaseId,DematAccountNumber,IEPFUserName,IEPFPassword,RTAContact,RtaRefNo) "
				+ "values(:caseId,:dematAccountNumber,:iepfUserName,:iepfPassword,:rtaContact,:rtaRefNo)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseId", model.getCaseId())
				.addValue("dematAccountNumber", model.getDematAccountNumber()).addValue("iepfUserName", model.getIepfUserName())
				.addValue("iepfPassword", model.getIepfPassword()).addValue("rtaContact", model.getRtaContact()).addValue("rtaRefNo", model.getRtaRefNo());

		template.update(query, sqlParameterSource, keyHolder);
		return keyHolder.getKey().longValue();
	}


	public int update(CaseAccountDetailModel model) {
		String query = "update `caseaccountdetails` set CaseId=:caseId=:,DematAccountNumber=:dematAccountNumber,"
				+ "IEPFUserName=:iepfUserName,IEPFPassword=:iepfPassword,RTAContact=:rtaContact,RtaRefNo=:rtaRefNo where id = :id ";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseId", model.getCaseId())
				.addValue("dematAccountNumber", model.getDematAccountNumber()).addValue("iepfUserName", model.getIepfUserName())
				.addValue("iepfPassword", model.getIepfPassword()).addValue("rtaContact", model.getRtaContact())
				.addValue("rtaRefNo", model.getRtaRefNo())
				.addValue("id", model.getId());
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
		return updatCount;
	}

	public int deleteByCaseId(Long caseId) {
		String query = "Delete FROM caseaccountdetails WHERE CaseId=:caseId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("caseId", caseId);
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>" + updatCount);
		return updatCount;
	}
	
	private RowMapper<CaseAccountDetailModel> mapObject() {
		return (result, i) -> {
			return CaseAccountDetailModel.builder().id(result.getLong("Id")).caseId(result.getLong("CaseId"))
					.dematAccountNumber(result.getString("DematAccountNumber")).iepfPassword(result.getString("IEPFPassword"))
					.rtaContact(result.getString("RTAContact")).rtaRefNo(result.getString("RtaRefNo")).build();
		};
	}
}
