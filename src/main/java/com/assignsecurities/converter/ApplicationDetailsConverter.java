package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.ApplicationDetailsBean;
import com.assignsecurities.domain.ApplicationModel;

public class ApplicationDetailsConverter {
	public static ApplicationDetailsBean convert(ApplicationModel model) {
		if (model == null) {
			return null;
		} else {
			ApplicationDetailsBean bean = new ApplicationDetailsBean();
			BeanUtils.copyProperties(model, bean);
			return bean;
		}
	}

	public static ApplicationModel convert(ApplicationDetailsBean bean) {
		if (bean == null) {
			return null;
		} else {
			ApplicationModel model = new ApplicationModel();
			BeanUtils.copyProperties(bean, model);
			return model;
		}
	}

	public static List<ApplicationModel> convert(List<ApplicationDetailsBean> beans) {
		if (beans == null) {
			return null;
		} else {
			List<ApplicationModel> models = new ArrayList<>();
			beans.forEach(bean -> {
				models.add(convert(bean));
			});
			return models;
		}
	}

	public static List<ApplicationDetailsBean> convertL(List<ApplicationModel> models) {
		if (models == null) {
			return null;
		} else {
			List<ApplicationDetailsBean> beans = new ArrayList<>();
			models.forEach(model -> {
				beans.add(convert(model));
			});
			return beans;
		}
	}
}
