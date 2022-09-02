package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.StatusMasterBean;
import com.assignsecurities.domain.StatusMasterModel;

public class StatusMasterConverter {
	public static StatusMasterBean convert(StatusMasterModel model) {
		if (model == null) {
			return null;
		} else {
			StatusMasterBean bean = new StatusMasterBean();
			BeanUtils.copyProperties(model, bean);
			return bean;
		}
	}

	public static StatusMasterModel convert(StatusMasterBean bean) {
		if (bean == null) {
			return null;
		} else {
			StatusMasterModel model = new StatusMasterModel();
			BeanUtils.copyProperties(bean, model);
			return model;
		}
	}
	
	public static List<StatusMasterBean> convert(List<StatusMasterModel> models) {
		if (models == null) {
			return null;
		} else {
			List<StatusMasterBean> beans = new ArrayList<>();
			models.forEach(model->{
				beans.add(convert(model));
			});
			return beans;
		}
	}
	
	public static List<StatusMasterModel> convertL(List<StatusMasterBean> beans) {
		if (beans == null) {
			return null;
		} else {
			List<StatusMasterModel> models = new ArrayList<>();
			beans.forEach(bean->{
				models.add(convert(bean));
			});
			return models;
		}
	}
}
