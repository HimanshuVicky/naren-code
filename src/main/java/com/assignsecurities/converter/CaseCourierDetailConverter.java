package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.CaseCourierDetailBean;
import com.assignsecurities.domain.CaseCourierDetailModel;

public class CaseCourierDetailConverter {
	
	public static CaseCourierDetailBean convert(CaseCourierDetailModel model) {
        if (model == null) {
            return null;
        } else {
        	CaseCourierDetailBean bean = new CaseCourierDetailBean();
            BeanUtils.copyProperties(model, bean);
            return bean;
        }
    }

    public static CaseCourierDetailModel convert(CaseCourierDetailBean bean) {
        if (bean == null) {
            return null;
        } else {
        	CaseCourierDetailModel model = new CaseCourierDetailModel();
            BeanUtils.copyProperties(bean, model);
            return model;
        }
    }
    
    public static List<CaseCourierDetailBean> convert(List<CaseCourierDetailModel> models) {
		if (models == null) {
			return null;
		} else {
			List<CaseCourierDetailBean> beans = new ArrayList<>();
			models.forEach(model->{
				beans.add(convert(model));
			});
			return beans;
		}
	}

}
