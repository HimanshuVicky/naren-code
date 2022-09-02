package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.GeneratedDocumentsRequiredBean;
import com.assignsecurities.domain.GeneratedDocumentsRequiredModel;

public class GeneratedDocumentsRequiredConverter {
	public static GeneratedDocumentsRequiredBean convert(GeneratedDocumentsRequiredModel model) {
		if (model == null) {
			return null;
		} else {
			GeneratedDocumentsRequiredBean bean = new GeneratedDocumentsRequiredBean();
			BeanUtils.copyProperties(model, bean);
			return bean;
		}
	}

	public static GeneratedDocumentsRequiredModel convert(GeneratedDocumentsRequiredBean bean) {
		if (bean == null) {
			return null;
		} else {
			GeneratedDocumentsRequiredModel model = new GeneratedDocumentsRequiredModel();
			BeanUtils.copyProperties(bean, model);
			return model;
		}
	}
	
	public static List<GeneratedDocumentsRequiredBean> convert(List<GeneratedDocumentsRequiredModel> models) {
		if (models == null) {
			return null;
		} else {
			List<GeneratedDocumentsRequiredBean> beans = new ArrayList<>();
			models.forEach(model->{
				beans.add(convert(model));
			});
			return beans;
		}
	}
	
	public static List<GeneratedDocumentsRequiredModel> convertL(List<GeneratedDocumentsRequiredBean> beans) {
		if (beans == null) {
			return null;
		} else {
			List<GeneratedDocumentsRequiredModel> models = new ArrayList<>();
			beans.forEach(bean->{
				models.add(convert(bean));
			});
			return models;
		}
	}
}
