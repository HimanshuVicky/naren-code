package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.CaseBean;
import com.assignsecurities.domain.CaseModel;

public class CaseConverter {
	public static CaseBean convert(CaseModel model) {
		if (model == null) {
			return null;
		} else {
			CaseBean bean = new CaseBean();
			BeanUtils.copyProperties(model, bean);
			bean.setScripts(CaseScriptConverter.convert(model.getScripts()));
			return bean;
		}
	}

	public static CaseModel convert(CaseBean bean) {
		if (bean == null) {
			return null;
		} else {
			CaseModel model = new CaseModel();
			BeanUtils.copyProperties(bean, model);
			model.setScripts(CaseScriptConverter.convertL(bean.getScripts()));
			return model;
		}
	}
	
	
	
	public static List<CaseBean> convert(List<CaseModel> models) {
		if (models == null) {
			return null;
		} else {
			List<CaseBean> beans = new ArrayList<>();
			models.forEach(model->{
				beans.add(convert(model));
			});
			return beans;
		}
	}
	
	public static List<CaseModel> convertL(List<CaseBean> beans) {
		if (beans == null) {
			return null;
		} else {
			List<CaseModel> models = new ArrayList<>();
			beans.forEach(bean->{
				models.add(convert(bean));
			});
			return models;
		}
	}
}
