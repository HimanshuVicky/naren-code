package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.ApplicationBean;
import com.assignsecurities.domain.ApplicationModel;

public class ApplicationConverter {
	public static ApplicationBean convert(ApplicationModel model) {
		if (model == null) {
			return null;
		} else {
			ApplicationBean bean = new ApplicationBean();
			BeanUtils.copyProperties(model, bean);
			bean.setScripts(ApplicationScriptConverter.convert(model.getScripts()));
			return bean;
		}
	}

	public static ApplicationModel convert(ApplicationBean bean) {
		if (bean == null) {
			return null;
		} else {
			ApplicationModel model = new ApplicationModel();
			BeanUtils.copyProperties(bean, model);
			model.setScripts(ApplicationScriptConverter.convertL(bean.getScripts()));
			return model;
		}
	}

	public static List<ApplicationModel> convert(List<ApplicationBean> beans) {
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

	public static List<ApplicationBean> convertL(List<ApplicationModel> models) {
		if (models == null) {
			return null;
		} else {
			List<ApplicationBean> beans = new ArrayList<>();
			models.forEach(model -> {
				beans.add(convert(model));
			});
			return beans;
		}
	}
}
