package com.assignsecurities.dm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.domain.dm.*;
import com.assignsecurities.repository.impl.MetaDataConfigDAOImpl;


/**
 * This class contains api for populating self sufficiency meta data
 * 
 */
@Service("MetaDataConfigService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class MetaDataConfigServiceImpl implements MetaDataConfigService {

	@Autowired
	MetaDataConfigDAOImpl metaDataConfigDAO;

	public Map<Long, ObjectConfigBean> getAllMetaData() throws ServiceException {
		Map<Long, ObjectConfigBean> objectConfigBeans = new HashMap<Long, ObjectConfigBean>();
		Map<Long, ObjectConfigModel> objectConfigModels = metaDataConfigDAO
				.getAllMetaData();
		ObjectConfigModel objectConfigModel = null;
		ObjectConfigBean objectConfigBean = null;
		for (Long objId : objectConfigModels.keySet()) {
			objectConfigModel = objectConfigModels.get(objId);
			objectConfigBean = converObjectConfigModelToBean(objectConfigModel);
			objectConfigBeans.put(objId, objectConfigBean);
		}
		return objectConfigBeans;
	}

	private ObjectConfigBean converObjectConfigModelToBean(
			ObjectConfigModel objectConfigModel) {
		ObjectConfigBean objectConfigBean = new ObjectConfigBean();
		BeanUtils.copyProperties(objectConfigModel, objectConfigBean,
				"attributeConfigModels");
		if (objectConfigModel.getAttributeConfigModels() != null) {
//			System.out
//					.println("objectConfigModel.getAttributeConfigModels():Not null");
			Set<AttributeConfigModel> attributeConfigModels = objectConfigModel
					.getAttributeConfigModels();
			AttributeConfigBean attributeConfigBean = null;
			List<AttributeConfigBean> attributeConfigBeans = new ArrayList<AttributeConfigBean>();
			for (AttributeConfigModel attributeConfigModel : attributeConfigModels) {
//				System.out.println(attributeConfigModel.getName() + "<==attributeConfigModel==>"
//						+ attributeConfigModel.getHeaderName());
				attributeConfigBean = convertAttributeConfigModelToBean(attributeConfigModel);
				attributeConfigBeans.add(attributeConfigBean);
			}
			sort(attributeConfigBeans, "sequenceNo", true);
			objectConfigBean.setAttributeConfigModels(attributeConfigBeans);
		}
		return objectConfigBean;
	}

	
	private void sort(List<AttributeConfigBean> ps, String property, final boolean asc) {

		  if (property.equals("sequenceNo")) {
		    Collections.sort(ps, new Comparator<AttributeConfigBean>() {

		        public int compare(AttributeConfigBean o1, AttributeConfigBean o2) {
		            // pls use appropriate compareTo I always get confused
		            // which one to call when it is asc or desc.
		            if (asc)
		                return o1.getSequenceNo().compareTo(o2.getSequenceNo());
		            else
		                return o2.getSequenceNo().compareTo(o1.getSequenceNo());
		        }
		    });
		  }

		  
		}
	private AttributeConfigBean convertAttributeConfigModelToBean(
			AttributeConfigModel attributeConfigModel) {
		AttributeConfigBean attributeConfigBean = new AttributeConfigBean();
		BeanUtils.copyProperties(attributeConfigModel, attributeConfigBean,
				"valueBasedResolvers", "validationConFigModels");
		if (attributeConfigModel.getValueBasedResolvers() != null) {
			Map<String, String> valueBasedResolver = new HashMap<String, String>();
			for (AttributeValueResolverModel attributeValueResolverModel : attributeConfigModel
					.getValueBasedResolvers()) {
				// CODE_TO_RESOLVE,RESOLVER_CLASS_NAME
				valueBasedResolver.put(
						attributeValueResolverModel.getCodeToResolve(),
						attributeValueResolverModel.getResolverClassName());
			}
			attributeConfigBean.setValueBasedResolver(valueBasedResolver);
		}
		if (attributeConfigModel.getValidationConFigModels() != null) {
			List<AttributeValidationConfigBean> attributeValidationConfigBeans = new ArrayList<AttributeValidationConfigBean>();
			AttributeValidationConfigBean attributeValidationConfigBean = null;
			for (AttributeValidationConfigModel attributeValidationConfigModel : attributeConfigModel
					.getValidationConFigModels()) {
				attributeValidationConfigBean = new AttributeValidationConfigBean();
				BeanUtils.copyProperties(attributeValidationConfigModel,
						attributeValidationConfigBean);
			}
			attributeValidationConfigBeans.add(attributeValidationConfigBean);
		}
		return attributeConfigBean;
	}

	public ObjectConfigBean getSpecificObjectMetaData(Long objectId)
			throws ServiceException {
		ObjectConfigModel objectConfigModel = metaDataConfigDAO
				.getSpecificObjectMetaData(objectId);
		ObjectConfigBean objectConfigBean = converObjectConfigModelToBean(objectConfigModel);
		return objectConfigBean;
	}

	public List<ObjectConfigBean> getSpecificObjectMetaData(List<Long> objectIds)
			throws ServiceException {
		List<ObjectConfigModel> objectConfigModels = metaDataConfigDAO
				.getSpecificObjectMetaData(objectIds);
		List<ObjectConfigBean> objectConfigBeans = new ArrayList<ObjectConfigBean>();
		for (ObjectConfigModel objectConfigModel : objectConfigModels) {
			objectConfigBeans
					.add(converObjectConfigModelToBean(objectConfigModel));
		}
		return objectConfigBeans;
	}

	public ObjectConfigBean getSpecificObjectMetaDataFromCache(Long parentSheetId,
			UserLoginBean user) throws ServiceException {

		ObjectConfigModel objectConfigModel = metaDataConfigDAO
				.getSpecificObjectMetaData(parentSheetId);
		if (objectConfigModel == null) {
			throw new ServiceException("config object not found.");
		}
		return converObjectConfigModelToBean(objectConfigModel);
	}

	/**
	 * 
	 * @param exportModel
	 * @return
	 * @throws ServiceException
	 */
	public Long getParentSheetConfigId(ObjectExportModel exportModel)
			throws ServiceException {
		return metaDataConfigDAO.getParentSheetConfigId(exportModel);
	}

}
