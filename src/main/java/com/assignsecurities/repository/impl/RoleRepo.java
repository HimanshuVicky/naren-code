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

import com.assignsecurities.domain.RoleModel;

@Repository
public class RoleRepo  {

	@Autowired
	private NamedParameterJdbcTemplate template;
	
	public RoleModel getRoleById(Long id) {
		String query = "SELECT * FROM role WHERE Id=:id";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("id", id);
		try {
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public RoleModel getRoleByName(String roleName) {
		String query = "SELECT * FROM role WHERE name=:roleName";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("roleName", roleName);
		try {
			return template.queryForObject(query, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	

	private RowMapper<RoleModel> mapObject() {
		return (rs, i) -> {
			return RoleModel.builder().id(rs.getLong("id")).name(rs.getString("name"))
					.code(rs.getString("code")).isActive(rs.getBoolean("isActive")).build();
		};
	}

	public Long add(RoleModel model) {
		String query = "insert into role (name,code,IsActive) "
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

	public void update(RoleModel model) {
		String query = "update role set name=:name,code=:code,IsActive=:isActive where id=:id ";
		MapSqlParameterSource sqlParameterSourceaddress = new MapSqlParameterSource();
		sqlParameterSourceaddress.addValue("name", model.getName());
		sqlParameterSourceaddress.addValue("code", model.getCode());
		sqlParameterSourceaddress.addValue("isActive", model.getIsActive());
		sqlParameterSourceaddress.addValue("id", model.getId());
		template.update(query, sqlParameterSourceaddress);

	}
	
	public List<RoleModel> getAllRoles() {
		try {

			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			String query = "SELECT r.* from role r order by r.id asc";
			List<RoleModel> roles = template.query(query, sqlParameterSource, mapObject());
			return roles;
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}
}
