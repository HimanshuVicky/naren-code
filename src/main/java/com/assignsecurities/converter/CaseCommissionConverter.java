package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.CaseCommissionBean;
import com.assignsecurities.domain.CaseCommissionModel;

public class CaseCommissionConverter {
	
	public static CaseCommissionBean convert(CaseCommissionModel model) {
		if (model == null) {
			return null;
		} else {
			CaseCommissionBean bean = new CaseCommissionBean();
			BeanUtils.copyProperties(model, bean);
			return bean;
		}
	}

	public static CaseCommissionModel convert(CaseCommissionBean bean) {
		if (bean == null) {
			return null;
		} else {
			CaseCommissionModel model = new CaseCommissionModel();
			BeanUtils.copyProperties(bean, model);
			return model;
		}
	}
	
	public static List<CaseCommissionBean> convert(List<CaseCommissionModel> models) {
		if (models == null) {
			return null;
		} else {
			List<CaseCommissionBean> beans = new ArrayList<>();
			models.forEach(model->{
				beans.add(convert(model));
			});
			return beans;
		}
	}

}
