package com.assignsecurities.dm;

import java.util.List;
import java.util.Map;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.domain.dm.ObjectConfigBean;
import com.assignsecurities.domain.dm.ObjectExportModel;


/**
 * This class is responsible to read all meta data.
 *
 */
public interface MetaDataConfigService {
	/**
	 * This will populate all available data load meta data. This api will get used to cached all required meta data
	 * @return
	 * @throws ServiceException
	 */ 
	public Map<Long, ObjectConfigBean> getAllMetaData()throws ServiceException;
	
	/**
	 * This will populate meta data for specific object. 
	 * @param objectId Object id available in SelfSufficiencyConstants file 
	 * @return
	 * @throws ServiceException
	 */
	public ObjectConfigBean getSpecificObjectMetaData( Long objectId)throws ServiceException;
	
	/**
	 * This will populate meta data for specific list of object. 
	 * @param objectIds Object id available in SelfSufficiencyConstants file
	 * @return
	 * @throws ServiceException
	 */
	public List<ObjectConfigBean> getSpecificObjectMetaData( List<Long> objectIds)throws ServiceException;
	
	/**
	 * This will populate meta data for specific object from cache. No any db call required. 
	 * @param objectId Object id available in SelfSufficiencyConstants file
	 * @param user 
	 * @return
	 * @throws ServiceException
	 */
	public ObjectConfigBean getSpecificObjectMetaDataFromCache(Long objectId, UserLoginBean user)throws ServiceException;

	/**
	 * 
	 * @param objectModel
	 * @return
	 * @throws ServiceException
	 */
	public Long getParentSheetConfigId(ObjectExportModel exportModel) throws ServiceException;
	
}
