package com.assignsecurities.repository.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.assignsecurities.domain.RtaDataModel;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class RtaDataRepo {
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	public RtaDataModel getRtaDataBySecurityCode(String securityCode) {
		try {
			String query = "SELECT * FROM rtadata WHERE securityCode=:securityCode";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("securityCode", securityCode);
			return template.queryForObject(query, sqlParameterSource,
					new BeanPropertyRowMapper<>(RtaDataModel.class));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public RtaDataModel getRtaDataByIsinNumber(String isinNumber) {
		try {
			String query = "SELECT * FROM rtadata WHERE IsinNumber=:isinNumber";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("isinNumber", isinNumber);
			return template.queryForObject(query, sqlParameterSource,
					new BeanPropertyRowMapper<>(RtaDataModel.class));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public RtaDataModel getRtaDataByCompanyName(String companyName) {
		try {
			String query = "SELECT * FROM rtadata WHERE companyName=:companyName";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("companyName", companyName);
			return template.queryForObject(query, sqlParameterSource,
					new BeanPropertyRowMapper<>(RtaDataModel.class));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public Boolean isRtaDataByCompanyName(String companyName) {
		try {
			String query = "SELECT id FROM rtadata WHERE companyName=:companyName limit 1";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("companyName", companyName);
//			String fileCount = template.queryForObject(query, sqlParameterSource,
//					new BeanPropertyRowMapper<>(String.class));
			Long fileCount = template.queryForObject(query,sqlParameterSource, Long.class);
			if (Objects.nonNull(fileCount) && fileCount>1) {
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		} catch (EmptyResultDataAccessException e) {
			return Boolean.FALSE;
		}
	}
	
	public void add(RtaDataModel model) {
		String query = "insert into `rtadata` (`CompanyName`,`RegistrarName`,`RegistrarAddress`,`Address`, `State`,`City`,`ContactNumber`, `Email`,`DDAmount`, `SecurityCode`,`IsinNumber`,`SecurityId`,`MPS`) "
				+ "values(:companyName,:registrarName,:registrarAddress,:address,:state,:city,:contactNumber,:email,:ddAmount,:securityCode,:isinNumber,:securityId,:mps)";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource()
					.addValue("companyName", model.getCompanyName())
					.addValue("registrarName", model.getRegistrarName())
					.addValue("registrarAddress", model.getRegistrarAddress())
					.addValue("address", model.getAddress())
					.addValue("state", model.getState())
					.addValue("city", model.getCity())
					.addValue("contactNumber", model.getContactNumber())
					.addValue("email", model.getEmail())
					.addValue("ddAmount", model.getDdAmount())
					.addValue("securityCode", model.getSecurityCode())
					.addValue("isinNumber", model.getIsinNumber())
					.addValue("securityId", model.getSecurityId())
					.addValue("mps", model.getMps());
					
		int insertCount = template.update(query, sqlParameterSource);
		log.info("insertCount===>"+insertCount);
	}
	
	public void update(RtaDataModel model) {
		String query = "update `rtadata` set `CompanyName`=:companyName,`RegistrarName`=:registrarName,RegistrarAddress=:registrarAddress,`Address`=:address, `State`=:state,"
				+ "`City`=:city,`ContactNumber`=:contactNumber, `Email`=:email,`DDAmount`=:ddAmount, `SecurityCode`=:securityCode,`IsinNumber`=:isinNumber,`SecurityId`=:securityId,"
				+ "`MPS`=:mps where Id=:id ";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource()
				.addValue("companyName", model.getCompanyName())
				.addValue("registrarName", model.getRegistrarName())
				.addValue("registrarAddress", model.getRegistrarAddress())
				.addValue("address", model.getAddress())
				.addValue("state", model.getState())
				.addValue("contactNumber", model.getContactNumber())
				.addValue("email", model.getEmail())
				.addValue("ddAmount", model.getDdAmount())
				.addValue("securityCode", model.getSecurityCode())
				.addValue("isinNumber", model.getIsinNumber())
				.addValue("securityId", model.getSecurityId())
				.addValue("mps", model.getMps())
				.addValue("id", model.getId());
		int updatCount = template.update(query, sqlParameterSource);
		log.info("updatCount===>"+updatCount);
	}
}
