package com.assignsecurities.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.domain.dm.AttributeConfigModel;
import com.assignsecurities.domain.dm.ObjectConfigModel;
import com.assignsecurities.domain.dm.ObjectExportModel;


/**
 * This class contains API to communicate with database
 * 
 */
@Repository
public class MetaDataConfigDAOImpl {
	@Autowired
	private NamedParameterJdbcTemplate template;

	public List<ObjectConfigModel> listFildMatrix() {
		try {
			String query = "select * from dm_obj_conf";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			return template.query(query, sqlParameterSource, mapObjectConfigModel());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	private RowMapper<ObjectConfigModel> mapObjectConfigModel() {
		return (rs, i) -> {
			ObjectConfigModel objectConfigModel = new ObjectConfigModel();
			objectConfigModel.setId(rs.getLong("ID"));
			objectConfigModel.setBusinessObjectClassName(rs.getString("BO_MODEL_NAME"));
			objectConfigModel.setBusinessProcesserClassName(rs.getString("BUS_DATA_PROC_CLASS"));
			objectConfigModel.setBusinessValidatorClassName(rs.getString("BUS_DATA_VALIDATOR_CLASS"));
			objectConfigModel.setDataDownLoadProcessorClassName(rs.getString("DL_DATA_PROC_CLASS"));
			objectConfigModel.setDataStartRowNo(rs.getInt("DATA_START_ROW_NO"));
			objectConfigModel.setErrorColumnIndex(rs.getInt("ERROR_INDEX_NO"));
			objectConfigModel.setHeaderRowNumber(rs.getInt("HEADER_ROW_INDEX"));
			objectConfigModel.setInputFileFormat(rs.getString("INPUT_FILE_FORMAT"));
			objectConfigModel.setActionColumnRequired(rs.getBoolean("IS_ACTION_COLUMN_REQUIRED"));
			objectConfigModel.setRequired(rs.getBoolean("IS_REQUIRED"));
			objectConfigModel.setObjectAssemplerClassName(rs.getString("OBJECT_ASSEMBLER_CLASS"));
			objectConfigModel.setObjectDisAssemplerClassName(rs.getString("DIS_ASSEMBLER_CLASS_NAME"));
			objectConfigModel.setObjectName(rs.getString("OBJECT_CODE"));
			objectConfigModel.setObjectType(rs.getString("OBJECT_TYPE"));
			objectConfigModel.setParentReferanceColumnIndex(rs.getString("PARENT_REF_KEY_COL_IDX"));
			objectConfigModel.setPrimaryColumnIndex(rs.getString("PRIMARY_KEY_COL_IDX"));
			objectConfigModel.setTabIndex(rs.getInt("SEQ_NO"));
			objectConfigModel.setTabOrFileName(rs.getString("TAB_OR_FILE_NAME"));
			List<AttributeConfigModel> attributeConfigModelList=loadAttributeConfigModel(objectConfigModel.getId());
			Set<AttributeConfigModel> attributeConfigModels = new HashSet<>(attributeConfigModelList);
			objectConfigModel.setAttributeConfigModels(attributeConfigModels);
			
			return objectConfigModel;

		};
	}

	public Map<Long, ObjectConfigModel> getAllMetaData() throws ServiceException {
		Map<Long, ObjectConfigModel> allObjectConfigModelMap = new HashMap<Long, ObjectConfigModel>();
		List<ObjectConfigModel> objectConfigModels = listFildMatrix();
		if (Objects.nonNull(objectConfigModels)) {
			objectConfigModels.stream().forEach(
					objectConfigModel -> allObjectConfigModelMap.put(objectConfigModel.getId(), objectConfigModel));
		}
		return allObjectConfigModelMap;
	}
	public List<AttributeConfigModel> loadAttributeConfigModel(Long parentObjectId){
		try {
			String query = "select * from dm_obj_attribute_dtl where PARENT_OBJECT_ID=:parentObjectId";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("parentObjectId", parentObjectId);
			return template.query(query, sqlParameterSource, mapAttributeConfigModel());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}
	private RowMapper<AttributeConfigModel> mapAttributeConfigModel() {
		return (rs, i) -> {
			AttributeConfigModel attributeConfigModel = new AttributeConfigModel();
			attributeConfigModel.setId(rs.getLong("ID"));
			attributeConfigModel.setChildObjectId(rs.getLong("CURRENT_OBJECT_ID"));
			attributeConfigModel.setCollectionDataType(rs.getString("COLLECTION_DATA_TYPE")); 
			attributeConfigModel.setDataFormatKey(rs.getString("DATA_FORMATOR_KEY")); 
			attributeConfigModel.setDataType(rs.getString("TYPE")); 
			attributeConfigModel.setDecimalPlaces(rs.getInt("DECIMAL_PLACES")); 
			attributeConfigModel.setDelimitedKey(rs.getString("DELIMITER_KEY"));
			attributeConfigModel.setHeaderName(rs.getString("ATTRIBUTE_HEADER_NAME"));
			attributeConfigModel.setAttributeAvailableInBo(rs.getBoolean("IS_BO_ATTRIBUTE")); 
			attributeConfigModel.setRequired(rs.getBoolean("IS_REQUIRED"));
			attributeConfigModel.setReverseValueUsed(rs.getBoolean("IS_REVERSE_VALUE_USED"));
			attributeConfigModel.setXMLAttribute(rs.getBoolean("isXMLAttribute"));
			attributeConfigModel.setLength(rs.getInt("MAX_LENGTH"));
			attributeConfigModel.setMaxValue(rs.getLong("MAX_VALUE"));
			attributeConfigModel.setMinValue(rs.getLong("MIN_VALUE"));
			attributeConfigModel.setName(rs.getString("ATTRIBUTE_NAME"));
			attributeConfigModel.setParentObjectId(rs.getLong("PARENT_OBJECT_ID"));
			attributeConfigModel.setSampleValue(rs.getString("SAMPLE_VALUE"));
			attributeConfigModel.setSequenceNo(rs.getLong("SEQUENCE_NUMBER"));
			attributeConfigModel.setXmlTagName(rs.getString("XML_TAG_NAME"));

			return attributeConfigModel;


		};
	}
	public ObjectConfigModel getSpecificObjectMetaData(Long parentSheetId) throws ServiceException {
		try {
			String query = "select * from dm_obj_conf where id=:parentSheetId";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("parentSheetId", parentSheetId);
			return template.queryForObject(query, sqlParameterSource, mapObjectConfigModel());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<ObjectConfigModel> getSpecificObjectMetaData(List<Long> objectIds) throws ServiceException {

		try {
			String query = "select * from dm_obj_conf where id in (:objectIds)";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("objectIds", objectIds);
			return template.query(query, sqlParameterSource, mapObjectConfigModel());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	/**
	 * Return parent sheet id.
	 * 
	 * @param exportModel
	 * @return
	 * @throws ServiceException
	 */
	public Long getParentSheetConfigId(ObjectExportModel exportModel) throws ServiceException {
		Long parentSheetId = -1L;
		try {
			String query = "SELECT MIN(id) FROM dm_obj_conf WHERE upper(Object_code)"
					+ " in (SELECT upper(SSDLDEF.code) FROM dm_dl_exp_files SSDLIMP,  dm_obj_def SSDLDEF \"\r\n"
					+ "WHERE SSDLIMP.TYPE_ID =SSDLDEF.id and SSDLIMP.ID=:objectId)";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("objectId", exportModel.getId());
			parentSheetId = template.queryForObject(query, sqlParameterSource, Long.class);
		} catch (EmptyResultDataAccessException e) {
			parentSheetId = -1L;
		}
		return parentSheetId;
	}

}
