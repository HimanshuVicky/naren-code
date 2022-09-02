package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.ApplicationUserBean;
import com.assignsecurities.domain.ApplicationUserModel;

public class ApplicationUserConverter {
	public static ApplicationUserBean convert(ApplicationUserModel model) {
        if (model == null) {
            return null;
        } else {
        	ApplicationUserBean bean = new ApplicationUserBean();
            BeanUtils.copyProperties(model, bean);
            return bean;
        }
    }

    public static ApplicationUserModel convert(ApplicationUserBean bean) {
        if (bean == null) {
            return null;
        } else {
        	ApplicationUserModel model = new ApplicationUserModel();
            BeanUtils.copyProperties(bean, model);
            return model;
        }
    }
    
    public static List<ApplicationUserBean> convert(List<ApplicationUserModel> models) {
		if (models == null) {
			return null;
		} else {
			List<ApplicationUserBean> beans = new ArrayList<>();
			models.forEach(model->{
				beans.add(convert(model));
			});
			return beans;
		}
	}
}
