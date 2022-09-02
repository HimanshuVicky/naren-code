package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.CaseScriptBean;
import com.assignsecurities.domain.CaseScriptModel;

public class CaseScriptConverter {
	public static CaseScriptBean convert(CaseScriptModel model) {
		if (model == null) {
			return null;
		} else {
			CaseScriptBean bean = new CaseScriptBean();
			BeanUtils.copyProperties(model, bean);
			return bean;
		}
	}

	public static CaseScriptModel convert(CaseScriptBean bean) {
		if (bean == null) {
			return null;
		} else {
			CaseScriptModel model = new CaseScriptModel();
			BeanUtils.copyProperties(bean, model);
			return model;
		}
	}
	
	
	
	public static List<CaseScriptBean> convert(List<CaseScriptModel> models) {
		if (models == null) {
			return null;
		} else {
			List<CaseScriptBean> beans = new ArrayList<>();
			models.forEach(model->{
				beans.add(convert(model));
			});
			return beans;
		}
	}
	
	public static List<CaseScriptModel> convertL(List<CaseScriptBean> beans) {
		if (beans == null) {
			return null;
		} else {
			List<CaseScriptModel> models = new ArrayList<>();
			beans.forEach(bean->{
				models.add(convert(bean));
			});
			return models;
		}
	}
}
