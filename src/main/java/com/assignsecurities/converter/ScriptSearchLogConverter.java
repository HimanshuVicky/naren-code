package com.assignsecurities.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.bean.ScriptSearchLogBean;
import com.assignsecurities.domain.ScriptSearchLogModel;

public class ScriptSearchLogConverter {
	public static ScriptSearchLogBean convert(ScriptSearchLogModel model) {
        if (model == null) {
            return null;
        } else {
        	ScriptSearchLogBean bean = new ScriptSearchLogBean();
            BeanUtils.copyProperties(model, bean);
            return bean;
        }
    }

    public static ScriptSearchLogModel convert(ScriptSearchLogBean bean) {
        if (bean == null) {
            return null;
        } else {
        	ScriptSearchLogModel model = new ScriptSearchLogModel();
            BeanUtils.copyProperties(bean, model);
            return model;
        }
    }
    
    public static List<ScriptSearchLogBean> convert(List<ScriptSearchLogModel> models) {
		if (models == null) {
			return null;
		} else {
			List<ScriptSearchLogBean> beans = new ArrayList<>();
			models.forEach(model -> {
				beans.add(convert(model));
			});
			return beans;
		}
	}
}
