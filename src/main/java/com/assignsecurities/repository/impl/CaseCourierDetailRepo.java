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

import com.assignsecurities.domain.CaseCourierDetailModel;

@Repository
public class CaseCourierDetailRepo {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	public CaseCourierDetailModel getCseCourierDetailById(Long id) {
		String query = "SELECT * FROM case_courierdtl WHERE Id=:id";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("id", id);
		try {
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public List<CaseCourierDetailModel> getAllCourierServiceDetailByGivenCaseId(Long caseId) {
		String query = "SELECT * FROM case_courierdtl WHERE caseId=:caseId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("caseId", caseId);
		try {
			List<CaseCourierDetailModel> caseCourierDetailModels = template.query(query, sqlParameterSource, mapObject());
			return caseCourierDetailModels;
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}
	
	public List<CaseCourierDetailModel> getAllCaseCourierServiceDetailByGivenCourierId(Long courierId) {
		String query = "SELECT * FROM case_courierdtl WHERE courierId=:courierId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("courierId", courierId);
		try {
//			return template.queryForObject(query, sqlParameterSource, mapObject());
			List<CaseCourierDetailModel> caseCourierDetailModels = template.query(query, sqlParameterSource, mapObject());
			return caseCourierDetailModels;
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}
	
	

	private RowMapper<CaseCourierDetailModel> mapObject() {
		return (rs, i) -> {
			return CaseCourierDetailModel.builder().id(rs.getLong("id")).caseId(rs.getLong("caseId")).courierId(rs.getLong("courierId"))
					.referncenumber(rs.getString("referncenumber"))
					.receiptDate(Objects.isNull(rs.getDate("receiptDate")) ? null : rs.getDate("receiptDate").toLocalDate()).build();
		};
	}

	public Long add(CaseCourierDetailModel model) {
		String query = "insert into case_courierdtl (caseId,courierId,referncenumber,receiptDate) "
				+ "values (:caseId,:courierId,:referncenumber,:receiptDate)";
		KeyHolder keyHolderAddress = new GeneratedKeyHolder();
		MapSqlParameterSource sqlParameterSourceaddress = new MapSqlParameterSource();
		sqlParameterSourceaddress.addValue("caseId", model.getCaseId());
		sqlParameterSourceaddress.addValue("courierId", model.getCourierId());
		sqlParameterSourceaddress.addValue("referncenumber", model.getReferncenumber());
		sqlParameterSourceaddress.addValue("receiptDate", model.getReceiptDate());
		template.update(query, sqlParameterSourceaddress, keyHolderAddress);
		Long roleId = keyHolderAddress.getKey().longValue();
		return roleId;
	}

	public void update(CaseCourierDetailModel model) {
		String query = "update case_courierdtl set caseId=:caseId,courierId=:courierId,referncenumber=:referncenumber,receiptDate=:receiptDate where id=:id ";
		MapSqlParameterSource sqlParameterSourceaddress = new MapSqlParameterSource();
		sqlParameterSourceaddress.addValue("caseId", model.getCaseId());
		sqlParameterSourceaddress.addValue("courierId", model.getCourierId());
		sqlParameterSourceaddress.addValue("referncenumber", model.getReferncenumber());
		sqlParameterSourceaddress.addValue("receiptDate", model.getReceiptDate());
		sqlParameterSourceaddress.addValue("id", model.getId());
		template.update(query, sqlParameterSourceaddress);

	}
	
	public List<CaseCourierDetailModel> getAllCourierServiceDetailsByCaseId() {
		try {

			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			String query = "SELECT r.* from case_courierdtl r order by r.id asc";
			List<CaseCourierDetailModel> caseCourierDetailModels = template.query(query, sqlParameterSource, mapObject());
			return caseCourierDetailModels;
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

}
