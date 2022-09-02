package com.assignsecurities.repository.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.domain.UserLogin;
import com.assignsecurities.domain.dm.ObjectModel;
import com.assignsecurities.domain.dm.ObjectTemplateModel;

@Repository
public class ObjectDao {
	@Autowired
	private NamedParameterJdbcTemplate template;


	/**
	 * 
	 */
	public List<ObjectModel> getObjectList(UserLogin uam) throws ServiceException {
		try {
			String query = "select * from dm_obj_def";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			return template.query(query, sqlParameterSource, mapObject(uam));
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	private RowMapper<ObjectModel> mapObject(UserLogin uam) {
		return (rs, i) -> {
			ObjectModel objectModel = new ObjectModel();
			objectModel.setId(rs.getLong("ID"));
			objectModel.setName(rs.getString("NAME"));
			objectModel.setCode(rs.getString("CODE"));
			objectModel.setMaxParentRows((rs.getLong("MAX_PARENT_ROWS")));
			objectModel.setObjectPerFile(rs.getLong("OBJECT_PER_FILE"));
			objectModel.setObjectTemplateModel(getObjectTemplate(objectModel.getId(), uam));

			return objectModel;
		};
	}

	public ObjectTemplateModel getObjectTemplate(Long objectId, UserLogin uam) throws ServiceException {
		try {
			String query = "select * from dm_obj_template where OBJ_ID=:objectId and LOCALE_CODE=:localeCode ";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("objectId", objectId);
			sqlParameterSource.addValue("localeCode", "en_US");
			return template.queryForObject(query, sqlParameterSource, mapObjectTemplate());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	private RowMapper<ObjectTemplateModel> mapObjectTemplate() {
		return (rs, i) -> {
			ObjectTemplateModel objectTemplateModel = new ObjectTemplateModel();
			objectTemplateModel.setId(rs.getLong("ID"));
			objectTemplateModel.setObjId(rs.getLong("OBJ_ID"));
			objectTemplateModel.setLocaleCode(rs.getString("LOCALE_CODE"));
			objectTemplateModel.setTemplate(rs.getBytes("TEMPLATE_BYTE"));

			return objectTemplateModel;

		};
	}

	/**
	 * 
	 * @param uam
	 * @param objectId
	 * @return
	 * @throws ServiceException
	 */
	public ObjectModel getObject(UserLogin uam, Long objectId) throws ServiceException {

		try {
			String query = "select * from dm_obj_def where id=:objectId";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("objectId", objectId);
			return template.queryForObject(query, sqlParameterSource, mapObject(uam));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public ObjectModel getObject(UserLogin uam, String typOfObject) throws ServiceException {
		try {
			String query = "select * from dm_obj_def where code=:typOfObject";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("typOfObject", typOfObject);
			return template.queryForObject(query, sqlParameterSource, mapObject(uam));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * 
	 * @param objectId
	 * @param fileFormat
	 * @param user
	 * @return
	 * @throws ServiceException
	 */
	public Long getParentId(Long objectId, String fileFormat, UserLogin user) throws ServiceException {

		try {
			String query = "SELECT MIN(ssObj.id) FROM dm_obj_conf ssObj,dm_obj_def odbDef\r\n"
					+ " WHERE upper(ssObj.object_code)=upper(odbDef.code) and odbDef.id=:objectId";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("objectId", objectId);
			return template.queryForObject(query, sqlParameterSource, Long.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public void updateTemplate(FileInputStream inputStream,Long objectId) {
		String query="Update dm_obj_template set TEMPLATE_BYTE= :template where OBJ_ID=:objectId";
		
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		try {
			sqlParameterSource.addValue("template",IOUtils.toByteArray(inputStream));
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		sqlParameterSource.addValue("objectId", objectId);
		template.update(query, sqlParameterSource);
		
	}

}
