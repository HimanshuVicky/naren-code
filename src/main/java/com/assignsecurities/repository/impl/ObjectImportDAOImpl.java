package com.assignsecurities.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.util.DMConstants;
import com.assignsecurities.domain.Pagination;
import com.assignsecurities.domain.UserLogin;
import com.assignsecurities.domain.dm.ObjectImportModel;



@Repository
public class ObjectImportDAOImpl {

	@Autowired
	private NamedParameterJdbcTemplate template;

	public List<ObjectImportModel> getImports(UserLogin user, Long statusId) {
		try {
			String query = "select * from dm_obj_import where STATUS_ID in("+DMConstants.FILE_STATUS_SCHEDULED +","+DMConstants.FILE_STATUS_IN_PROCESS +")";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("statusId", statusId);
			return template.query(query, sqlParameterSource, mapObjectImportModel());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	private RowMapper<ObjectImportModel> mapObjectImportModel() {
		return (rs, i) -> {
			ObjectImportModel importModel = new ObjectImportModel();
			importModel.setId(rs.getLong("ID"));
			importModel.setDateCreated(rs.getDate("DATE_CREATED"));
			importModel.setDateModified(rs.getDate("DATE_MODIFIED"));
			importModel.setErrorRecordCount(rs.getLong("ERROR_REC_COUNT"));
			importModel.setFileByte(rs.getBytes("FILE_BYTE"));
			importModel.setFileFormat(rs.getString("FILE_FORMAT"));
			importModel.setFileName(rs.getString("FILE_NAME"));
			importModel.setLocaleCode(rs.getString("LOCALE_CODE"));
			importModel.setObjId(rs.getLong("OBJ_ID"));
			importModel.setRetryByte(rs.getBytes("RETRY_FILE"));
			importModel.setRetryFileName(rs.getString("RETRY_FILE_NAME"));
			importModel.setStatusId(rs.getLong("STATUS_ID"));
			importModel.setTotalRecordCount(rs.getLong("TOTAL_REC_COUNT"));
			importModel.setImportedBy(rs.getLong("IMPORTED_BY"));
			importModel.setModifiedBy(rs.getLong("MODIFIED_BY"));
			return importModel;
		};
	}

	public ObjectImportModel getImport(UserLogin user, Long importId) {

		try {
			String query = "select * from dm_obj_import where id=:importId";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("importId", importId);
			return template.queryForObject(query, sqlParameterSource, mapObjectImportModel());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public Boolean existsByFileName(UserLogin user, String fileName) {

		try {
			String query = "select count(FILE_NAME) from dm_obj_import where FILE_NAME=:fileName;";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("fileName", fileName);
//			String fileCount = template.queryForObject(query, sqlParameterSource,
//					new BeanPropertyRowMapper<>(String.class));
			Long fileCount = template.queryForObject(query,sqlParameterSource, Long.class);
			
			if (Objects.nonNull(fileCount) && fileCount>1) {
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		} catch (EmptyResultDataAccessException e) {
			return Boolean.TRUE;
		}
	}

	public void addImport(ObjectImportModel objectImportModel) {
		String query = "insert into `dm_obj_import` (DATE_CREATED,DATE_MODIFIED,ERROR_REC_COUNT,FILE_BYTE,FILE_FORMAT,FILE_NAME,LOCALE_CODE,OBJ_ID,"
				+ "RETRY_FILE,RETRY_FILE_NAME,STATUS_ID,TOTAL_REC_COUNT,IMPORTED_BY,MODIFIED_BY) "
				+ " values(CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP(),:errorRecCount,:fileByte,:fileFormat,:fileName,:localeCode,:objId,:retryFile,:retryFileName,:statusId,:totalRecCount,:inportedBy,:modifiedBy)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		Date dateCreated = Objects.isNull(objectImportModel.getDateCreated()) ? new Date()
				: objectImportModel.getDateCreated();
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("dateCreated", dateCreated);
		sqlParameterSource.addValue("dateModified", dateCreated);
		sqlParameterSource.addValue("errorRecCount", objectImportModel.getErrorRecordCount());
		sqlParameterSource.addValue("fileByte", objectImportModel.getFileByte());
		sqlParameterSource.addValue("fileFormat", objectImportModel.getFileFormat());
		sqlParameterSource.addValue("fileName", objectImportModel.getFileName());
		sqlParameterSource.addValue("localeCode", objectImportModel.getLocaleCode());
		sqlParameterSource.addValue("objId", objectImportModel.getObjId());
		sqlParameterSource.addValue("retryFile", objectImportModel.getRetryByte());
		sqlParameterSource.addValue("retryFileName", objectImportModel.getRetryFileName());
		sqlParameterSource.addValue("statusId", objectImportModel.getStatusId());
		sqlParameterSource.addValue("totalRecCount", objectImportModel.getTotalRecordCount());
		sqlParameterSource.addValue("inportedBy", objectImportModel.getImportedBy());
		sqlParameterSource.addValue("modifiedBy", objectImportModel.getModifiedBy());
		template.update(query, sqlParameterSource, keyHolder);
		Long importId = keyHolder.getKey().longValue();
		objectImportModel.setId(importId);
	}

	public void updateImport(ObjectImportModel objectImportModel) {
		String query = "update dm_obj_import set" + " DATE_MODIFIED=CURRENT_TIMESTAMP()" + " ,ERROR_REC_COUNT=:errorRecCount"
				+ " ,RETRY_FILE=:retryFile" + " ,RETRY_FILE_NAME=:retryFileName" + " ,STATUS_ID=:statusId"
				+ " ,TOTAL_REC_COUNT=:totalRecCount" + " ,MODIFIED_BY=:modifiedBy" + " Where id=:importId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("dateModified", objectImportModel.getDateModified());
		sqlParameterSource.addValue("errorRecCount", objectImportModel.getErrorRecordCount());
		sqlParameterSource.addValue("retryFile", objectImportModel.getRetryByte());
		sqlParameterSource.addValue("retryFileName", objectImportModel.getRetryFileName());
		sqlParameterSource.addValue("statusId", objectImportModel.getStatusId());
		sqlParameterSource.addValue("totalRecCount", objectImportModel.getTotalRecordCount());
		sqlParameterSource.addValue("inportedBy", objectImportModel.getImportedBy());
		sqlParameterSource.addValue("modifiedBy", objectImportModel.getModifiedBy());
		sqlParameterSource.addValue("importId", objectImportModel.getId());
		template.update(query, sqlParameterSource);
	}

	public Pagination<ObjectImportModel> getLatestImports(UserLogin user, Long objectId,
														  Pagination<ObjectImportModel> pagination) {
		List<ObjectImportModel> imports;
		try {
			String query = "select * from dm_obj_import where OBJ_ID=:objectId order by ID desc limit " + (pagination.getCurrPageNumber() - 1)
					+ "," + pagination.getPageSize();
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("objectId", objectId);
			imports = template.query(query, sqlParameterSource, mapObjectImportModel());
		} catch (EmptyResultDataAccessException e) {
			imports = new ArrayList<>();
		}
		pagination.setList(imports);
		return pagination;
	}

}
