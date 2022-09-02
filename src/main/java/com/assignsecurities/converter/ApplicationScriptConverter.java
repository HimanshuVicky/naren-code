package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.ApplicationScriptBean;
import com.assignsecurities.domain.ApplicationScriptModel;

public class ApplicationScriptConverter {
	public static ApplicationScriptBean convert(ApplicationScriptModel model) {
		if (model == null) {
			return null;
		} else {
			ApplicationScriptBean bean = new ApplicationScriptBean();
			BeanUtils.copyProperties(model, bean);
			bean.setScripts(ScriptConvertor.convert(model.getScripts()));
			return bean;
		}
	}

	public static ApplicationScriptModel convert(ApplicationScriptBean bean) {
		if (bean == null) {
			return null;
		} else {
			ApplicationScriptModel model = new ApplicationScriptModel();
			BeanUtils.copyProperties(bean, model);
			model.setScripts(ScriptConvertor.convertL(bean.getScripts()));
			return model;
		}
	}
	
	public static List<ApplicationScriptBean> convert(List<ApplicationScriptModel> models) {
		if (models == null) {
			return null;
		} else {
			List<ApplicationScriptBean> beans = new ArrayList<>();
			models.forEach(model->{
				beans.add(convert(model));
			});
			return beans;
		}
	}
	
	public static List<ApplicationScriptModel> convertL(List<ApplicationScriptBean> beans) {
		if (beans == null) {
			return null;
		} else {
			List<ApplicationScriptModel> models = new ArrayList<>();
			beans.forEach(bean->{
				models.add(convert(bean));
			});
			return models;
		}
	}
}
