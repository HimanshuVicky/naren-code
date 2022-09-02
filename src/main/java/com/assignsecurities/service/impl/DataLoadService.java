package com.assignsecurities.service.impl;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.assignsecurities.app.util.DMConstants;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.converter.UserLoginConverter;
import com.assignsecurities.domain.Pagination;
import com.assignsecurities.domain.UserLogin;
import com.assignsecurities.domain.dm.ObjectImportModel;
import com.assignsecurities.repository.impl.ObjectImportDAOImpl;



@Service("dataLoadService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class DataLoadService {
	@Autowired
	private ObjectImportDAOImpl objectImportDAO;

	public Pagination<ObjectImportModel> getLatestImports(UserLoginBean user, Long objectId,
														  Pagination<ObjectImportModel> pagination) {
		UserLogin userLogin = new UserLogin();
		UserLoginConverter.convert(user,userLogin);
		// dateModifiedFormated
		pagination = objectImportDAO.getLatestImports(userLogin, objectId, pagination);
		SimpleDateFormat formatter;
		Locale locale = new Locale(user.getLocalCode());
		formatter = new SimpleDateFormat("yyyy.MMMMM.dd GGG hh:mm aaa", locale);
		for (ObjectImportModel objectImportModel : pagination.getList()) {
			if (objectImportModel.getStatusId() == DMConstants.FILE_STATUS_SCHEDULED) {
				objectImportModel.setStatusString("Scheduled");
			} else if (objectImportModel.getStatusId() == DMConstants.FILE_STATUS_SUCCESSFUL) {
				objectImportModel.setStatusString("Sucessful");
			} else if (objectImportModel.getStatusId() == DMConstants.FILE_STATUS_UNSUCCESSFUL) {
				objectImportModel.setStatusString("Un Successful");
			} else if (objectImportModel.getStatusId() == DMConstants.FILE_STATUS_PARTIAL_SUCCESSFUL) {
				objectImportModel.setStatusString("Partial Successful");
			} else if (objectImportModel.getStatusId() == DMConstants.FILE_STATUS_INVALID_TEMPLATE) {
				objectImportModel.setStatusString("Invalid Template");
			} else if (objectImportModel.getStatusId() == DMConstants.FILE_STATUS_IN_PROCESS) {
				objectImportModel.setStatusString("In Process");
			} else if (objectImportModel.getStatusId() == DMConstants.FILE_STATUS_FAILD_DUPLICATE) {
				objectImportModel.setStatusString("Faild Duplicate");
			}
			objectImportModel.setFileByte(null);
			objectImportModel.setRetryByte(null);
			String dateModifiedFormated = formatter.format(objectImportModel.getDateModified());
			objectImportModel.setDateModifiedFormated(dateModifiedFormated);

		}
		return pagination;
	}
	public ObjectImportModel getImport(UserLoginBean user, Long importId) {
		UserLogin userLogin = new UserLogin();
		UserLoginConverter.convert(user,userLogin);
		return objectImportDAO.getImport(userLogin, importId);
	}
	
}
