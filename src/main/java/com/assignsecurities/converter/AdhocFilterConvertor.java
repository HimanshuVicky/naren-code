package com.assignsecurities.converter;

import org.springframework.beans.BeanUtils;

import com.assignsecurities.domain.dm.AdhocFilterBean;
import com.assignsecurities.domain.dm.AdhocFilterModel;


public class AdhocFilterConvertor {

	public static AdhocFilterBean convert(AdhocFilterModel adhocFilterModel) {
		if (adhocFilterModel == null) {
			return null;
		}
		AdhocFilterBean adhocFilterBean = new AdhocFilterBean();
		BeanUtils.copyProperties(adhocFilterModel, adhocFilterBean);
		return adhocFilterBean;
	}

	public static AdhocFilterModel convert(AdhocFilterBean adhocFilterBean) {
		if (adhocFilterBean == null) {
			return null;
		}
		AdhocFilterModel adhocFilterModel = new AdhocFilterModel();
		BeanUtils.copyProperties(adhocFilterBean, adhocFilterModel);
		return adhocFilterModel;
	}
}
