package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.ApplicationFeatureBean;
import com.assignsecurities.domain.ApplicationFeatureModel;

public class ApplicationFeatureConverter {
	
	public static ApplicationFeatureBean convert(ApplicationFeatureModel model) {
        if (model == null) {
            return null;
        } else {
        	ApplicationFeatureBean bean = new ApplicationFeatureBean();
            BeanUtils.copyProperties(model, bean);
            return bean;
        }
    }

    public static ApplicationFeatureModel convert(ApplicationFeatureBean bean) {
        if (bean == null) {
            return null;
        } else {
        	ApplicationFeatureModel model = new ApplicationFeatureModel();
            BeanUtils.copyProperties(bean, model);
            return model;
        }
    }
    
    public static List<ApplicationFeatureBean> convert(List<ApplicationFeatureModel> models) {
		if (models == null) {
			return null;
		} else {
			List<ApplicationFeatureBean> beans = new ArrayList<>();
			models.forEach(model->{
				beans.add(convert(model));
			});
			return beans;
		}
	}

}
