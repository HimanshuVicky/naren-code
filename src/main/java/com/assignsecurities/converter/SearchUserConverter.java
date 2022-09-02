package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.SearchUserBean;
import com.assignsecurities.domain.SearchUserModel;

public class SearchUserConverter {
	public static SearchUserBean convert(SearchUserModel model) {
        if (model == null) {
            return null;
        } else {
        	SearchUserBean bean = new SearchUserBean();
            BeanUtils.copyProperties(model, bean);
            return bean;
        }
    }

    public static SearchUserModel convert(SearchUserBean bean) {
        if (bean == null) {
            return null;
        } else {
        	SearchUserModel model = new SearchUserModel();
            BeanUtils.copyProperties(bean, model);
            return model;
        }
    }
    
    public static List<SearchUserBean> convert(List<SearchUserModel> models) {
		if (models == null) {
			return null;
		} else {
			List<SearchUserBean> beans = new ArrayList<>();
			models.forEach(model -> {
				beans.add(convert(model));
			});
			return beans;
		}
	}
}
