package com.assignsecurities.repository.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.assignsecurities.domain.ApplicationFeatureModel;

@Repository
public class ApplicationFeatureRepo {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	public ApplicationFeatureModel getApplicationFeatureById(Long id) {
		String query = "SELECT * FROM applicationfeature WHERE Id=:id";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("id", id);
		try {
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public ApplicationFeatureModel getApplicationFeatureByName(String name) {
		String query = "SELECT * FROM applicationfeature WHERE name=:name";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("name", name);
		try {
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	

	private RowMapper<ApplicationFeatureModel> mapObject() {
		return (rs, i) -> {
			return ApplicationFeatureModel.builder().id(rs.getLong("id")).name(rs.getString("name"))
					.code(rs.getString("code")).isActive(rs.getBoolean("isActive")).build();
		};
	}

	public Long add(ApplicationFeatureModel model) {
		String query = "insert into applicationfeature (name,code,IsActive) "
				+ "values (:name,:code,:isActive)";
		KeyHolder keyHolderAddress = new GeneratedKeyHolder();
		MapSqlParameterSource sqlParameterSourceaddress = new MapSqlParameterSource();
		sqlParameterSourceaddress.addValue("name", model.getName());
		sqlParameterSourceaddress.addValue("code", model.getCode());
		sqlParameterSourceaddress.addValue("isActive", model.getIsActive());
		template.update(query, sqlParameterSourceaddress, keyHolderAddress);
		Long roleId = keyHolderAddress.getKey().longValue();
		return roleId;
	}

	public void update(ApplicationFeatureModel model) {
		String query = "update applicationfeature set name=:name,code=:code,IsActive=:isActive where id=:id ";
		MapSqlParameterSource sqlParameterSourceaddress = new MapSqlParameterSource();
		sqlParameterSourceaddress.addValue("name", model.getName());
		sqlParameterSourceaddress.addValue("code", model.getCode());
		sqlParameterSourceaddress.addValue("isActive", model.getIsActive());
		sqlParameterSourceaddress.addValue("id", model.getId());
		template.update(query, sqlParameterSourceaddress);

	}
	
	public List<ApplicationFeatureModel> getAllApplicationFeature() {
		try {

			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			String query = "SELECT r.* from applicationfeature r order by r.id asc";
			List<ApplicationFeatureModel> appfeatures = template.query(query, sqlParameterSource, mapObject());
			return appfeatures;
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

}
