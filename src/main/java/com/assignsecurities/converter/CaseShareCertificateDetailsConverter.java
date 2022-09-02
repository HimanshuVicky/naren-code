package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.CaseShareCertificateDetailsBean;
import com.assignsecurities.domain.CaseShareCertificateDetailsModel;

public class CaseShareCertificateDetailsConverter {
	public static CaseShareCertificateDetailsBean convert(CaseShareCertificateDetailsModel model) {
		if (model == null) {
			return null;
		} else {
			CaseShareCertificateDetailsBean bean = new CaseShareCertificateDetailsBean();
			BeanUtils.copyProperties(model, bean);
			return bean;
		}
	}

	public static CaseShareCertificateDetailsModel convert(CaseShareCertificateDetailsBean bean) {
		if (bean == null) {
			return null;
		} else {
			CaseShareCertificateDetailsModel model = new CaseShareCertificateDetailsModel();
			BeanUtils.copyProperties(bean, model);
			return model;
		}
	}
	
	
	
	public static List<CaseShareCertificateDetailsBean> convert(List<CaseShareCertificateDetailsModel> models) {
		if (models == null) {
			return null;
		} else {
			List<CaseShareCertificateDetailsBean> beans = new ArrayList<>();
			models.forEach(model->{
				beans.add(convert(model));
			});
			return beans;
		}
	}
	
	public static List<CaseShareCertificateDetailsModel> convertL(List<CaseShareCertificateDetailsBean> beans) {
		if (beans == null) {
			return null;
		} else {
			List<CaseShareCertificateDetailsModel> models = new ArrayList<>();
			beans.forEach(bean->{
				models.add(convert(bean));
			});
			return models;
		}
	}
}
