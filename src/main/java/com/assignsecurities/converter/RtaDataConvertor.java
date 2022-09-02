package com.assignsecurities.converter;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.RtaDataBean;
import com.assignsecurities.domain.RtaDataModel;

public class RtaDataConvertor {

	public static RtaDataBean convert(RtaDataModel model) {
		if (model == null) {
			return null;
		} else {
			RtaDataBean bean = new RtaDataBean();
			BeanUtils.copyProperties(model, bean);
			return bean;
		}
	}

	public static RtaDataModel convert(RtaDataBean bean) {
		if (bean == null) {
			return null;
		} else {
			RtaDataModel model = new RtaDataModel();
			BeanUtils.copyProperties(bean, model);
			return model;
		}
	}
}
