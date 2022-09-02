package com.assignsecurities.repository.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.domain.LawyerCaseCommentsDtlModel;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class LawyerCaseCommentsDtlRepo {
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	public LawyerCaseCommentsDtlModel getLawyerCommentDtlById(Long id) {
		String query = "SELECT * FROM lawyerCaseCommentsDtl WHERE Id=:id";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("id", id);
		try {
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	private RowMapper<LawyerCaseCommentsDtlModel> mapObject() {
		return (rs, i) -> {
			return LawyerCaseCommentsDtlModel.builder().id(rs.getLong("id")).caseId(rs.getLong("caseId"))
					.comment(rs.getString("comment")).lawyerName(rs.getString("lawyerName")).createdDate(Objects.isNull(rs.getDate("CreatedDate")) ? null
							: rs.getTimestamp("CreatedDate").toLocalDateTime())
					.caseCommentDate(getCaseCommentDate(Objects.isNull(rs.getDate("CreatedDate")) ? null : rs.getTimestamp("CreatedDate").toLocalDateTime())).build();
		};
	}
	
	private String getCaseCommentDate(LocalDateTime createdDate) {
		if(Objects.nonNull(createdDate)) {
			return DateTimeFormatter.ofPattern(AppConstant.DD_MMM_YYYY_HH_MM_SS).format(createdDate);
		}
		return null;
		
//		
	}
	


	public Long add(LawyerCaseCommentsDtlModel model) {
		String query = "insert into lawyerCaseCommentsDtl (caseId,comment,createdDate,lawyerName) "
				+ "values (:caseId,:comment,:createdDate,:lawyerName)";
		KeyHolder keyHolderAddress = new GeneratedKeyHolder();
		MapSqlParameterSource sqlParameterSourceaddress = new MapSqlParameterSource();
		sqlParameterSourceaddress.addValue("caseId", model.getCaseId());
		sqlParameterSourceaddress.addValue("comment", model.getComment());
		sqlParameterSourceaddress.addValue("createdDate", model.getCreatedDate());
		sqlParameterSourceaddress.addValue("lawyerName", model.getLawyerName());
		template.update(query, sqlParameterSourceaddress, keyHolderAddress);
		Long lawyercommnetsdtlId = keyHolderAddress.getKey().longValue();
		return lawyercommnetsdtlId;
	}

	public void update(LawyerCaseCommentsDtlModel model) {
		String query = "update lawyerCaseCommentsDtl set caseId=:caseId,comment=:comment,createdDate=:createdDate,lawyerName=:lawyerName where id=:id ";
		MapSqlParameterSource sqlParameterSourceaddress = new MapSqlParameterSource();
		sqlParameterSourceaddress.addValue("caseId", model.getCaseId());
		sqlParameterSourceaddress.addValue("comment", model.getComment());
		sqlParameterSourceaddress.addValue("createdDate", model.getCreatedDate());
		sqlParameterSourceaddress.addValue("lawyerName", model.getLawyerName());
		sqlParameterSourceaddress.addValue("id", model.getId());
		template.update(query, sqlParameterSourceaddress);

	}
	
	public List<LawyerCaseCommentsDtlModel> getAllLawyerCaseCommentsDtls(Long caseId) {
		try {

			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			String query = "SELECT r.* from lawyerCaseCommentsDtl r where r.caseId=:caseId order by r.id asc";
			
			sqlParameterSource.addValue("caseId", caseId);
			
			List<LawyerCaseCommentsDtlModel> lawyerCaseCommentsDtlModel = template.query(query, sqlParameterSource, mapObject());
			return lawyerCaseCommentsDtlModel;
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}
	

}
