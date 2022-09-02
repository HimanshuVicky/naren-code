package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.SuretyInfoBean;
import com.assignsecurities.domain.SuretyInfoModel;

public class SuretyInfoConverter {
	
	public static SuretyInfoBean convert(SuretyInfoModel model) {
		if (model == null) {
			return null;
		} else {
			SuretyInfoBean bean = new SuretyInfoBean();
			BeanUtils.copyProperties(model, bean);
			return bean;
		}
	}

	public static SuretyInfoModel convert(SuretyInfoBean bean) {
		if (bean == null) {
			return null;
		} else {
			SuretyInfoModel model = new SuretyInfoModel();
			BeanUtils.copyProperties(bean, model);
			return model;
		}
	}
	
	public static List<SuretyInfoBean> convert(List<SuretyInfoModel> models) {
		if (models == null) {
			return null;
		} else {
			List<SuretyInfoBean> beans = new ArrayList<>();
			models.forEach(model->{
				beans.add(convert(model));
			});
			return beans;
		}
	}
	public static List<SuretyInfoModel> convertL(List<SuretyInfoBean> beans) {
		if (beans == null) {
			return null;
		} else {
			List<SuretyInfoModel> models = new ArrayList<>();
			beans.forEach(bean->{
				models.add(convert(bean));
			});
			return models;
		}
	}

}
