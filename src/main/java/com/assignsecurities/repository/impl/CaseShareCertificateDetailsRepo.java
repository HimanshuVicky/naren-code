package com.assignsecurities.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.assignsecurities.domain.CaseShareCertificateDetailsModel;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class CaseShareCertificateDetailsRepo {
	@Autowired
	private NamedParameterJdbcTemplate template;

	public void add(CaseShareCertificateDetailsModel model) {
		String query = "insert into `case_script_share_cert_detls` (CaseScriptId,CertificateNo,DistinctiveNo,Quantity) "
				+ "values(:caseScriptId, :certificateNo, :distinctiveNo, :quantity)";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseScriptId", model.getCaseScriptId())
				.addValue("certificateNo", model.getCertificateNo()).addValue("distinctiveNo", model.getDistinctiveNo())
				.addValue("quantity", model.getQuantity());
		template.update(query, sqlParameterSource);
	}

	public void addAll(List<CaseShareCertificateDetailsModel> models) {
		String query = "insert into `case_script_share_cert_detls` (CaseScriptId,CertificateNo,DistinctiveNo,Quantity) "
				+ "values(:caseScriptId, :certificateNo, :distinctiveNo, :quantity)";

		List<Map<String, Object>> batchValues = new ArrayList<>();
		models.stream().forEach(model -> {
			batchValues.add(new MapSqlParameterSource("caseScriptId", model.getCaseScriptId())
					.addValue("certificateNo", model.getCertificateNo())
					.addValue("distinctiveNo", model.getDistinctiveNo()).addValue("quantity", model.getQuantity())
					.getValues());
		});
		template.batchUpdate(query, batchValues.toArray(new Map[batchValues.size()]));

	}

	public void delete(Long caseScriptId) {
		String query = "delete from `case_script_share_cert_detls` where caseScriptId =:caseScriptId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseScriptId", caseScriptId);
		template.update(query, sqlParameterSource);
	}
	public void deleteByCaseIdAndScriptId(Long caseId, Long scriptId) {
		String query = "delete from `case_script_share_cert_detls`"
				+ " where CaseScriptId in(select Id from casescript where caseId=:caseId and ScriptId=:scriptId)";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("caseId", caseId)
				.addValue("scriptId", scriptId);
		template.update(query, sqlParameterSource);
	}

	public CaseShareCertificateDetailsModel getCaseShareCertificateDetails(Long caseScriptId) {
		try {
			String query = "SELECT * FROM case_script_share_cert_detls WHERE CaseScriptId=:caseScriptId";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("caseScriptId", caseScriptId);
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<CaseShareCertificateDetailsModel> getCaseShareCertificateDetailsByCaseId(Long caseId) {
		try {
			String query = "SELECT csscd.*,sc.ScriptId ScriptId FROM case_script_share_cert_detls  csscd " + 
					"inner join casescript sc on csscd.CaseScriptId=sc.id WHERE sc.CaseId=:caseId";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("caseId", caseId);
			return template.query(query, sqlParameterSource, mapObjectWithScriptId());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	private RowMapper<CaseShareCertificateDetailsModel> mapObject() {
		return (result, i) -> {
			return CaseShareCertificateDetailsModel.builder().id(result.getLong("id"))
					.caseScriptId(result.getLong("CaseScriptId")).certificateNo(result.getString("CertificateNo"))
					.distinctiveNo(result.getString("DistinctiveNo")).quantity(result.getDouble("Quantity")).build();
		};
	}
	
	private RowMapper<CaseShareCertificateDetailsModel> mapObjectWithScriptId() {
		return (result, i) -> {
			return CaseShareCertificateDetailsModel.builder().id(result.getLong("id"))
					.caseScriptId(result.getLong("CaseScriptId")).certificateNo(result.getString("CertificateNo"))
					.distinctiveNo(result.getString("DistinctiveNo")).quantity(result.getDouble("Quantity"))
					.scriptId(result.getLong("ScriptId")).build();
		};
	}
}
