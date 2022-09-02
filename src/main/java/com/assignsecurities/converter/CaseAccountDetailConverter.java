package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.CaseAccountDetailBean;
import com.assignsecurities.domain.CaseAccountDetailModel;

public class CaseAccountDetailConverter {
	
	public static CaseAccountDetailBean convert(CaseAccountDetailModel model) {
		if (model == null) {
			return null;
		} else {
			CaseAccountDetailBean bean = new CaseAccountDetailBean();
			BeanUtils.copyProperties(model, bean);
			return bean;
		}
	}

	public static CaseAccountDetailModel convert(CaseAccountDetailBean bean) {
		if (bean == null) {
			return null;
		} else {
			CaseAccountDetailModel model = new CaseAccountDetailModel();
			BeanUtils.copyProperties(bean, model);
			return model;
		}
	}
	
	public static List<CaseAccountDetailBean> convert(List<CaseAccountDetailModel> models) {
		if (models == null) {
			return null;
		} else {
			List<CaseAccountDetailBean> beans = new ArrayList<>();
			models.forEach(model->{
				beans.add(convert(model));
			});
			return beans;
		}
	}

}
