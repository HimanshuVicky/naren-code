package com.assignsecurities.dm;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.app.util.DMConstants;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.converter.UserLoginConverter;
import com.assignsecurities.domain.UserLogin;
import com.assignsecurities.domain.dm.Document;
import com.assignsecurities.domain.dm.ObjectBean;
import com.assignsecurities.domain.dm.ObjectExportModel;
import com.assignsecurities.domain.dm.ObjectImportModel;
import com.assignsecurities.domain.dm.ObjectModel;
import com.assignsecurities.domain.dm.ObjectTemplateBean;
import com.assignsecurities.repository.impl.ObjectDao;
import com.assignsecurities.repository.impl.ObjectExportDAOImpl;
import com.assignsecurities.repository.impl.ObjectImportDAOImpl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;



@Service("objectServiceImpl")
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class ObjectServiceImpl{
	private static final Logger logger = LogManager.getLogger(ObjectServiceImpl.class);

	@Autowired
	private ObjectDao objectDAO;

	@Autowired
	private ObjectImportDAOImpl objectImportDAO;
	
	
	@Autowired
	private ObjectExportDAOImpl objectExportDAO;
	/**
	 * This method is used to display the index page list for the the respective
	 * Data Load Objects.
	 * 
	 * @param uam
	 * @throws ServiceException
	 */
	public List<ObjectBean> getObjectList(UserLoginBean uam) throws ServiceException {
		UserLogin userLogin = new UserLogin();
		UserLoginConverter.convert(uam,userLogin);
		List<ObjectModel> objectModels = objectDAO.getObjectList(userLogin);
		List<ObjectBean> result = new ArrayList<ObjectBean>();
		ObjectBean objectBean = null;
		for (ObjectModel objectModel : objectModels) {
			objectBean = new ObjectBean();
			BeanUtils.copyProperties(objectModel, objectBean, "objectTemplateModel");
			if (objectModel.getObjectTemplateModel() != null) {
				ObjectTemplateBean objectTemplateBean = new ObjectTemplateBean();
				BeanUtils.copyProperties(objectModel.getObjectTemplateModel(), objectTemplateBean);
				objectBean.setObjectTemplateBean(objectTemplateBean);
			}
			result.add(objectBean);
		}
		return result;
	}

	public Document getDocumentObjectToProcess(long importId, UserLoginBean user) throws ServiceException {
		UserLogin userLogin = new UserLogin();
		UserLoginConverter.convert(user,userLogin);
		ObjectImportModel objectImportModel = objectImportDAO.getImport(userLogin, importId);
		Document doc = new Document();
		doc.setId(objectImportModel.getId());
		doc.setObjectId(objectImportModel.getObjId());
		String fileName = objectImportModel.getFileName();
		doc.setFileName(fileName);
		doc.setFileType(fileName.substring(fileName.lastIndexOf(".") + 1));
		logger.debug("objectImportModel==>"+objectImportModel);
//		logger.debug("objectImportModel.getFileByte())==>"+objectImportModel.getFileByte());
		InputStream stream = new ByteArrayInputStream(objectImportModel.getFileByte());
//		logger.debug("stream==>"+stream);
		doc.setImputStream(stream);
		doc.setImportedBy(objectImportModel.getImportedBy());
		Long parentId = objectDAO.getParentId(objectImportModel.getObjId(), objectImportModel.getFileFormat(), userLogin);
		doc.setParentSheetId(parentId.intValue());
		return doc;
	}

	public Document getDocumentObjectToDownload(long importId, UserLoginBean user) throws ServiceException {
		UserLogin userLogin = new UserLogin();
		UserLoginConverter.convert(user,userLogin);
		ObjectImportModel objectImportModel = objectImportDAO.getImport(userLogin, importId);
		Document doc = new Document();
		doc.setId(objectImportModel.getId());
		doc.setObjectId(objectImportModel.getObjId());
		String fileName = objectImportModel.getFileName();
		doc.setFileName(fileName);
		doc.setFileType(fileName.substring(fileName.lastIndexOf(".") + 1));
		InputStream stream = new ByteArrayInputStream(objectImportModel.getFileByte());
		doc.setImputStream(stream);
		doc.setImportedBy(objectImportModel.getImportedBy());
		return doc;
	}
	
	public Boolean existsByFileName(UserLoginBean user, String fielName) throws ServiceException {
		UserLogin userLogin = new UserLogin();
		UserLoginConverter.convert(user,userLogin);
		return  objectImportDAO.existsByFileName(userLogin, fielName);
	}
	

	/**
	 * 
	 * @param objectId
	 * @return
	 * @throws ServiceException
	 */
	public ObjectModel getObject(UserLoginBean user, Long objectId) throws ServiceException {
		UserLogin userLogin = new UserLogin();
		UserLoginConverter.convert(user,userLogin);
		return objectDAO.getObject(userLogin, objectId);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void updateRetryFile(long importId, String retryFileName, File reTryFile, UserLoginBean user)
			throws ServiceException {
		// ObjectImportModel objectImportModel= objectImportDAO.getImport(user,
		// objectModel.getId());
		// objectDAO.updateRetryFileId(objectModel, user);
		// TODO
		// throw new ServiceException("Imimplemented method need to implement");
		UserLogin userLogin = new UserLogin();
		UserLoginConverter.convert(user,userLogin);
		ObjectImportModel objectImportModel = objectImportDAO.getImport(userLogin, importId);
		objectImportModel.setRetryFileName(retryFileName);

		try {
			objectImportModel.setRetryByte(FileUtils.readFileToByteArray(reTryFile));
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		// objectImportModel.setRetryByte(Files.readAllBytes(reTryFile));
		objectImportModel.setDateModified(new java.sql.Date(System.currentTimeMillis()));
		objectImportDAO.updateImport(objectImportModel);
	}

	/**
	 * This method is used to update the import status.
	 * 
	 * @param user
	 * @throws ServiceException
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void updateImportStatus(long importId, long statusId, UserLoginBean user) throws ServiceException {
		UserLogin userLogin = new UserLogin();
		UserLoginConverter.convert(user,userLogin);
		ObjectImportModel objectImportModel = objectImportDAO.getImport(userLogin, importId);
		objectImportModel.setStatusId(statusId);
		objectImportModel.setDateModified(new java.sql.Date(System.currentTimeMillis()));
		objectImportDAO.updateImport(objectImportModel);
	}

	/**
	 * This method is used to update the import statistics.
	 * 
	 * @param user
	 * @throws ServiceException
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void updateErrorStat(long importId, long sucessRecCount, long totalRecCount, UserLoginBean user)
			throws ServiceException {
		UserLogin userLogin = new UserLogin();
		UserLoginConverter.convert(user,userLogin);
		ObjectImportModel objectImportModel = objectImportDAO.getImport(userLogin, importId);
		objectImportModel.setTotalRecordCount(totalRecCount);
		objectImportModel.setErrorRecordCount(totalRecCount-sucessRecCount);
		objectImportModel.setDateModified(new java.sql.Date(System.currentTimeMillis()));
		objectImportDAO.updateImport(objectImportModel);
	}

	/**
	 * To update export failure status
	 * 
	 * @param objectModel
	 * @param user
	 * @throws ServiceException
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void updateFailedStatus(ObjectBean objectModel, UserLoginBean user) throws ServiceException {
		UserLogin userLogin = new UserLogin();
		UserLoginConverter.convert(user,userLogin);
		ObjectImportModel objectImportModel = objectImportDAO.getImport(userLogin, objectModel.getId());
		objectImportModel.setStatusId(DMConstants.EXPORT_FILE_STATUS_FAILED);
		objectImportModel.setDateModified(new java.sql.Date(System.currentTimeMillis()));
		objectImportDAO.updateImport(objectImportModel);
	}

	/**
	 * 
	 * @param objectModel
	 * @param user
	 * @throws ServiceException
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void deletePattialPartIds(ObjectBean objectModel, UserLoginBean user) throws ServiceException {
//		objectDAO.deletePattialPartIds(null, user);
		// TODO
		throw new ServiceException("Imimplemented method need to implement");
	}

	public List<ObjectImportModel> getImports(UserLoginBean user, Long statusId) {
		UserLogin userLogin = new UserLogin();
		UserLoginConverter.convert(user,userLogin);
		return objectImportDAO.getImports(userLogin, statusId);
	}
	
	public List<ObjectExportModel> getExports(UserLoginBean user, Long statusId) {
		UserLogin userLogin = new UserLogin();
		UserLoginConverter.convert(user,userLogin);
		return objectExportDAO.getExports(userLogin, statusId);
	}

//	public Pagination<ObjectImportModel> getLatestImports(UserLoginBean user, Long objectId, Pagination<ObjectImportModel> pagination,List<SearchCriteria> searchCriterias) {
//		// dateModifiedFormated
//		pagination = objectImportDAO.getLatestImports(user, objectId, pagination,searchCriterias);
//		SimpleDateFormat formatter;
//		Locale locale = new Locale(user.getLocalCode());
//		formatter = new SimpleDateFormat("yyyy.MMMMM.dd GGG hh:mm aaa", locale);
//		for (ObjectImportModel objectImportModel : pagination.getList()) {
//			if (objectImportModel.getStatusId() == DMConstants.FILE_STATUS_SCHEDULED) {
//				objectImportModel.setStatusString("Scheduled");
//			} else if (objectImportModel.getStatusId() == DMConstants.FILE_STATUS_SUCCESSFUL) {
//				objectImportModel.setStatusString("Sucessful");
//			} else if (objectImportModel.getStatusId() == DMConstants.FILE_STATUS_UNSUCCESSFUL) {
//				objectImportModel.setStatusString("Un Successful");
//			} else if (objectImportModel.getStatusId() == DMConstants.FILE_STATUS_PARTIAL_SUCCESSFUL) {
//				objectImportModel.setStatusString("Partial Successful");
//			} else if (objectImportModel.getStatusId() == DMConstants.FILE_STATUS_INVALID_TEMPLATE) {
//				objectImportModel.setStatusString("Invalid Template");
//			}
//	
//			String dateModifiedFormated = formatter.format(objectImportModel.getDateModified());
//			objectImportModel.setDateModifiedFormated(dateModifiedFormated);
//
//		}
//		return pagination;
//	}

	public ObjectImportModel getImport(UserLoginBean user, Long importId) {
		UserLogin userLogin = new UserLogin();
		UserLoginConverter.convert(user,userLogin);
		return objectImportDAO.getImport(userLogin, importId);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void addImport(ObjectImportModel objectImportModel) {
		objectImportDAO.addImport(objectImportModel);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public ObjectExportModel addExport(ObjectExportModel objectImportModel) {
			return objectExportDAO.create(objectImportModel);
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public ObjectExportModel updateExport(ObjectExportModel objectImportModel) {
			objectImportModel.setDateModified(new java.sql.Date(System.currentTimeMillis()));
			return objectExportDAO.update(objectImportModel);
	}
	/**
	 * 
	 * @param uam
	 * @param type_of_object
	 * @return
	 * @throws ServiceException
	 */
	public ObjectModel getObject(UserLoginBean uam, String type_of_object) throws ServiceException {
		UserLogin userLogin = new UserLogin();
		UserLoginConverter.convert(uam,userLogin);
		return objectDAO.getObject(userLogin, type_of_object);
	}
	
	public ObjectExportModel findLatest(UserLoginBean user, Long objectId) {
		UserLogin userLogin = new UserLogin();
		UserLoginConverter.convert(user,userLogin);
		return objectExportDAO.findLatest(userLogin,  objectId);
	}
	public ObjectExportModel find(UserLoginBean user, Long statusId,Long objectId) {
		UserLogin userLogin = new UserLogin();
		UserLoginConverter.convert(user,userLogin);
		return objectExportDAO.find(userLogin, statusId, objectId);
	}
}
