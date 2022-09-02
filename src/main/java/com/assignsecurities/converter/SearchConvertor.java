package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.ReportBean;
import com.assignsecurities.domain.ReportModel;

public class SearchConvertor {
	public static ReportBean convert(ReportModel model) {
		if (model == null) {
			return null;
		} else {
			ReportBean bean = new ReportBean();
			BeanUtils.copyProperties(model, bean);
			return bean;
		}
	}

	public static ReportModel convert(ReportBean bean) {
		if (bean == null) {
			return null;
		} else {
			ReportModel model = new ReportModel();
			BeanUtils.copyProperties(bean, model);
			return model;
		}
	}
	
	
	
	public static List<ReportBean> convert(List<ReportModel> models) {
		if (models == null) {
			return null;
		} else {
			List<ReportBean> beans = new ArrayList<>();
			models.forEach(model->{
				beans.add(convert(model));
			});
			return beans;
		}
	}
	
	public static List<ReportModel> convertL(List<ReportBean> beans) {
		if (beans == null) {
			return null;
		} else {
			List<ReportModel> models = new ArrayList<>();
			beans.forEach(bean->{
				models.add(convert(bean));
			});
			return models;
		}
	}
}
