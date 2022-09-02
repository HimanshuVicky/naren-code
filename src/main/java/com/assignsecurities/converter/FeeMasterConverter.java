package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.FeeMasterBean;
import com.assignsecurities.domain.FeeMasterModel;

public class FeeMasterConverter {
	public static FeeMasterBean convert(FeeMasterModel model) {
		if (model == null) {
			return null;
		} else {
			FeeMasterBean bean = new FeeMasterBean();
			BeanUtils.copyProperties(model, bean);
			return bean;
		}
	}

	public static FeeMasterModel convert(FeeMasterBean bean) {
		if (bean == null) {
			return null;
		} else {
			FeeMasterModel model = new FeeMasterModel();
			BeanUtils.copyProperties(bean, model);
			return model;
		}
	}
	
	public static List<FeeMasterBean> convert(List<FeeMasterModel> models) {
		if (models == null) {
			return null;
		} else {
			List<FeeMasterBean> beans = new ArrayList<>();
			models.forEach(model->{
				beans.add(convert(model));
			});
			return beans;
		}
	}
	
	public static List<FeeMasterModel> convertL(List<FeeMasterBean> beans) {
		if (beans == null) {
			return null;
		} else {
			List<FeeMasterModel> models = new ArrayList<>();
			beans.forEach(bean->{
				models.add(convert(bean));
			});
			return models;
		}
	}
}
