package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.SearchReferralBean;
import com.assignsecurities.domain.SearchReferralModel;

public class SearchReferaalConverter {
	public static SearchReferralBean convert(SearchReferralModel model) {
        if (model == null) {
            return null;
        } else {
        	SearchReferralBean bean = new SearchReferralBean();
            BeanUtils.copyProperties(model, bean);
            return bean;
        }
    }

    public static SearchReferralModel convert(SearchReferralBean bean) {
        if (bean == null) {
            return null;
        } else {
        	SearchReferralModel model = new SearchReferralModel();
            BeanUtils.copyProperties(bean, model);
            return model;
        }
    }
    
    public static List<SearchReferralBean> convert(List<SearchReferralModel> models) {
		if (models == null) {
			return null;
		} else {
			List<SearchReferralBean> beans = new ArrayList<>();
			models.forEach(model -> {
				beans.add(convert(model));
			});
			return beans;
		}
	}
}
