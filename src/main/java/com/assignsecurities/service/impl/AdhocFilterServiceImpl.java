package com.assignsecurities.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.assignsecurities.app.util.StringUtil;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.converter.AdhocFilterConvertor;
import com.assignsecurities.dm.AdhocFilterService;
import com.assignsecurities.domain.dm.AdhocFilterBean;
import com.assignsecurities.domain.dm.AdhocFilterModel;
import com.assignsecurities.domain.dm.KeyValueModel;
import com.assignsecurities.repository.impl.AdhocFilterDAOImpl;





@Service("AdhocFilterService")
public class AdhocFilterServiceImpl implements AdhocFilterService {

	
	@Autowired
	private AdhocFilterDAOImpl adhocFilterDAO;

	/**
	 * 
	 */
	@Transactional
	public AdhocFilterBean getAdhocFilter(String filterCode, UserLoginBean user) {

		AdhocFilterModel adhocFilterModel = adhocFilterDAO.getAdhocFilter(
				filterCode, user);
		AdhocFilterBean adhocFilter = AdhocFilterConvertor
				.convert(adhocFilterModel);
		return adhocFilter;
	}
	@Transactional
	public Map<String, String> getIdCode(String filterCode, UserLoginBean user) {
		Map<String, String> idCode = new HashMap<String, String>();
		AdhocFilterModel adhocFilterModel = adhocFilterDAO.getAdhocFilter(
				filterCode, user);

		if (adhocFilterModel.getFilterType().equals(FILTER_TYPE_CSV)) {
			String[] nameValuePairs = StringUtil.split(
					adhocFilterModel.getQuery(), COMMA);
			for (int i = 0; i < nameValuePairs.length; i++) {
				String[] nameAndValue = StringUtil.split(nameValuePairs[i],
						COLON);
				idCode.put(nameAndValue[0], nameAndValue[1]);
			}

		} else if (adhocFilterModel.getFilterType().equals(FILTER_TYPE_SQL)) {
			List<KeyValueModel> keyValueModels = adhocFilterDAO.getIdCode(
					adhocFilterModel, user);
			for (KeyValueModel keyValueModel : keyValueModels) {
				idCode.put(keyValueModel.getIdKey(), keyValueModel.getIdValue());
			}
		}
		return idCode;
	}
}
