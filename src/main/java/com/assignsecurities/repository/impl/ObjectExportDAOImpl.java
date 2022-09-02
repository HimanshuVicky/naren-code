package com.assignsecurities.repository.impl;

import org.apache.catalina.mbeans.UserMBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.domain.UserLogin;
import com.assignsecurities.domain.dm.ObjectExportModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



@Repository
public class ObjectExportDAOImpl{
	@Autowired
	private NamedParameterJdbcTemplate template;
	public List<ObjectExportModel> getExports(UserLogin user, Long statusId) {
		
		try {
			String query = "select * from dm_dl_exp_files";
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			return template.query(query, sqlParameterSource, mapObjectImportModel());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
//		Criteria criteria = getSession()
//				.createCriteria(ObjectExportModel.class);
//		// criteria.add(Restrictions.eq("localeCode", user.getLocalCode()));
//		criteria.add(Restrictions.eq("statusId", statusId.longValue()));
//		return (List<ObjectExportModel>) criteria.list();
		
	}
	private RowMapper<ObjectExportModel> mapObjectImportModel() {
		return (rs, i) -> {
			ObjectExportModel exportModel = new ObjectExportModel();
			exportModel.setId(rs.getLong("ID"));  
			exportModel.setCompletionDate(rs.getDate("COMPLETION_DATE"));
			exportModel.setDateCreated(rs.getDate("DATE_CREATED"));
			exportModel.setDateModified(rs.getDate("DATE_MODIFIED"));
			exportModel.setFile(rs.getBytes("FILE"));
			exportModel.setFileFormat(rs.getString("FILE_FORMAT"));
			exportModel.setFileName(rs.getString("FILE_NAME"));
			exportModel.setStatusId(rs.getLong("STATUS_ID"));
			exportModel.setCreatedBy(rs.getLong("CREATED_BY"));
			exportModel.setModifiedBy(rs.getLong("MODIFIED_BY"));
			
			return exportModel;

		};
	}
	public ObjectExportModel find(UserLogin user, Long statusId, Long objectId) {

//		String sql = "select expFile.* from dm_dl_exp_files expFile, pdms_application_users usr, pdms_security_user sUsr "
//				+ " where sUsr.user_id=usr.id and  expFile.CREATED_BY=sUsr.id and  usr.Org_Group_Id=:orgGroupId and expFile.STATUS_ID=:statusId and expFile.TYPE_ID=:objectId";
//		SQLQuery  query = getSession().createSQLQuery(sql);
//		// query.set
//		query.addEntity(ObjectExportModel.class);
//		Object objectExportModel = query.setLong("orgGroupId", user.getOrgId())
//				.setLong("statusId", statusId).setLong("objectId", objectId)
//				.uniqueResult();
//		return (ObjectExportModel) objectExportModel;
		return null;
	
	}
	public ObjectExportModel findLatest(UserLogin user, Long objectId) throws ServiceException {

//		String sql = "select max(expFile.id) from dm_dl_exp_files expFile, pdms_application_users usr, pdms_security_user sUsr "
//				+ " where sUsr.user_id=usr.id and  expFile.CREATED_BY=sUsr.id and  usr.Org_Group_Id=:orgGroupId and expFile.STATUS_ID=:statusId and expFile.TYPE_ID=:objectId";
//		SQLQuery  query = getSession().createSQLQuery(sql);
//		// query.set
////		query.addEntity(Lo.class);
//		Object maxExportObject = query.setLong("orgGroupId", user.getOrgId())
//				.setLong("statusId", DMConstants.EXPORT_FILE_STATUS_COMPLETED).setLong("objectId", objectId)
//				.uniqueResult();
//		Long maxExportId = Long.parseLong(maxExportObject.toString());
//		return find(maxExportId);
		return null;
		// Criteria criteria = getSession()
		// .createCriteria(ObjectImportModel.class);
		// // criteria.add(Restrictions.eq("localeCode", user.getLocalCode()));
		// criteria.add(Restrictions.eq("objId", objectId.longValue()));
		// criteria.add(Restrictions.eq("statusId", statusId.longValue()));
		// criteria.add(Restrictions.eq("createdBy.id", user.getId()));
		// return (ObjectExportModel) criteria.uniqueResult();
	}

	public ObjectExportModel create(ObjectExportModel objectImportModel) {
		// TODO Auto-generated method stub
		return null;
	}

	public ObjectExportModel update(ObjectExportModel objectImportModel) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
