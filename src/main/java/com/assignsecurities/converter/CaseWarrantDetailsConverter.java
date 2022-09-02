package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.CaseWarrantDetailsBean;
import com.assignsecurities.domain.CaseWarrantDetailsModel;

public class CaseWarrantDetailsConverter {
	public static CaseWarrantDetailsBean convert(CaseWarrantDetailsModel model) {
		if (model == null) {
			return null;
		} else {
			CaseWarrantDetailsBean bean = new CaseWarrantDetailsBean();
			BeanUtils.copyProperties(model, bean);
			return bean;
		}
	}

	public static CaseWarrantDetailsModel convert(CaseWarrantDetailsBean bean) {
		if (bean == null) {
			return null;
		} else {
			CaseWarrantDetailsModel model = new CaseWarrantDetailsModel();
			BeanUtils.copyProperties(bean, model);
			return model;
		}
	}
	
	
	
	public static List<CaseWarrantDetailsBean> convert(List<CaseWarrantDetailsModel> models) {
		if (models == null) {
			return null;
		} else {
			List<CaseWarrantDetailsBean> beans = new ArrayList<>();
			models.forEach(model->{
				beans.add(convert(model));
			});
			return beans;
		}
	}
	
	public static List<CaseWarrantDetailsModel> convertL(List<CaseWarrantDetailsBean> beans) {
		if (beans == null) {
			return null;
		} else {
			List<CaseWarrantDetailsModel> models = new ArrayList<>();
			beans.forEach(bean->{
				models.add(convert(bean));
			});
			return models;
		}
	}
}
