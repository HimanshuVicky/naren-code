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

import com.assignsecurities.domain.CourierDetailModel;

@Repository
public class CourierDetailRepo {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	public CourierDetailModel getCourierDetailById(Long id) {
		String query = "SELECT * FROM courier_dtl WHERE Id=:id";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("id", id);
		try {
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public CourierDetailModel getCourierServiceByName(String name) {
		String query = "SELECT * FROM courier_dtl WHERE name=:name";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("name", name);
		try {
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	

	private RowMapper<CourierDetailModel> mapObject() {
		return (rs, i) -> {
			return CourierDetailModel.builder().id(rs.getLong("id")).name(rs.getString("name"))
					.city(rs.getString("city")).url(rs.getString("url")).build();
		};
	}

	public Long add(CourierDetailModel model) {
		String query = "insert into courier_dtl (name,city,url) "
				+ "values (:name,:city,:url)";
		KeyHolder keyHolderAddress = new GeneratedKeyHolder();
		MapSqlParameterSource sqlParameterSourceaddress = new MapSqlParameterSource();
		sqlParameterSourceaddress.addValue("name", model.getName());
		sqlParameterSourceaddress.addValue("city", model.getCity());
		sqlParameterSourceaddress.addValue("url", model.getUrl());
		template.update(query, sqlParameterSourceaddress, keyHolderAddress);
		Long roleId = keyHolderAddress.getKey().longValue();
		return roleId;
	}

	public void update(CourierDetailModel model) {
		String query = "update courier_dtl set name=:name,city=:city,url=:url where id=:id ";
		MapSqlParameterSource sqlParameterSourceaddress = new MapSqlParameterSource();
		sqlParameterSourceaddress.addValue("name", model.getName());
		sqlParameterSourceaddress.addValue("city", model.getCity());
		sqlParameterSourceaddress.addValue("url", model.getUrl());
		sqlParameterSourceaddress.addValue("id", model.getId());
		template.update(query, sqlParameterSourceaddress);

	}
	
	public List<CourierDetailModel> getAllCourierServiceDetails() {
		try {

			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			String query = "SELECT r.* from courier_dtl r order by r.id asc";
			List<CourierDetailModel> courierDetailModels = template.query(query, sqlParameterSource, mapObject());
			return courierDetailModels;
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

}
