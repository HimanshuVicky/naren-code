package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.ScriptBean;
import com.assignsecurities.domain.ScriptModel;

public class ScriptConvertor {
	public static ScriptBean convert(ScriptModel model) {
		if (model == null) {
			return null;
		} else {
			ScriptBean bean = new ScriptBean();
			BeanUtils.copyProperties(model, bean);
			return bean;
		}
	}

	public static ScriptModel convert(ScriptBean bean) {
		if (bean == null) {
			return null;
		} else {
			ScriptModel model = new ScriptModel();
			BeanUtils.copyProperties(bean, model);
			return model;
		}
	}
	
	
	
	public static List<ScriptBean> convert(List<ScriptModel> models) {
		if (models == null) {
			return null;
		} else {
			List<ScriptBean> beans = new ArrayList<>();
			models.forEach(model->{
				beans.add(convert(model));
			});
			return beans;
		}
	}
	
	public static List<ScriptModel> convertL(List<ScriptBean> beans) {
		if (beans == null) {
			return null;
		} else {
			List<ScriptModel> models = new ArrayList<>();
			beans.forEach(bean->{
				models.add(convert(bean));
			});
			return models;
		}
	}
}
