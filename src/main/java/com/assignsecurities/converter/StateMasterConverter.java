package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.StateMasterBean;
import com.assignsecurities.domain.StateMasterModel;

public class StateMasterConverter {
	public static StateMasterBean convert(StateMasterModel model) {
		if (model == null) {
			return null;
		} else {
			StateMasterBean bean = new StateMasterBean();
			BeanUtils.copyProperties(model, bean);
			return bean;
		}
	}

	public static StateMasterModel convert(StateMasterBean bean) {
		if (bean == null) {
			return null;
		} else {
			StateMasterModel model = new StateMasterModel();
			BeanUtils.copyProperties(bean, model);
			return model;
		}
	}
	
	public static List<StateMasterBean> convert(List<StateMasterModel> models) {
		if (models == null) {
			return null;
		} else {
			List<StateMasterBean> beans = new ArrayList<>();
			models.forEach(model->{
				beans.add(convert(model));
			});
			return beans;
		}
	}
	
	public static List<StateMasterModel> convertL(List<StateMasterBean> beans) {
		if (beans == null) {
			return null;
		} else {
			List<StateMasterModel> models = new ArrayList<>();
			beans.forEach(bean->{
				models.add(convert(bean));
			});
			return models;
		}
	}
}
