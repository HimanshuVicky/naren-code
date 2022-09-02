package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.DocumentMasterBean;
import com.assignsecurities.domain.DocumentMasterModel;

public class DocumentMasterConverter {
	public static DocumentMasterBean convert(DocumentMasterModel model) {
		if (model == null) {
			return null;
		} else {
			DocumentMasterBean bean = new DocumentMasterBean();
			BeanUtils.copyProperties(model, bean);
			return bean;
		}
	}

	public static DocumentMasterModel convert(DocumentMasterBean bean) {
		if (bean == null) {
			return null;
		} else {
			DocumentMasterModel model = new DocumentMasterModel();
			BeanUtils.copyProperties(bean, model);
			return model;
		}
	}
	
	public static List<DocumentMasterBean> convert(List<DocumentMasterModel> models) {
		if (models == null) {
			return null;
		} else {
			List<DocumentMasterBean> beans = new ArrayList<>();
			models.forEach(model->{
				beans.add(convert(model));
			});
			return beans;
		}
	}
	
	public static List<DocumentMasterModel> convertL(List<DocumentMasterBean> beans) {
		if (beans == null) {
			return null;
		} else {
			List<DocumentMasterModel> models = new ArrayList<>();
			beans.forEach(bean->{
				models.add(convert(bean));
			});
			return models;
		}
	}
}
