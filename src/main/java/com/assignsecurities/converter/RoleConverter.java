package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.RoleBean;
import com.assignsecurities.domain.RoleModel;

public class RoleConverter {
	
	public static RoleBean convert(RoleModel model) {
        if (model == null) {
            return null;
        } else {
        	RoleBean bean = new RoleBean();
            BeanUtils.copyProperties(model, bean);
            return bean;
        }
    }

    public static RoleModel convert(RoleBean bean) {
        if (bean == null) {
            return null;
        } else {
        	RoleModel model = new RoleModel();
            BeanUtils.copyProperties(bean, model);
            return model;
        }
    }
    
    public static List<RoleBean> convert(List<RoleModel> models) {
		if (models == null) {
			return null;
		} else {
			List<RoleBean> beans = new ArrayList<>();
			models.forEach(model->{
				beans.add(convert(model));
			});
			return beans;
		}
	}

}
