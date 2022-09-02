package com.assignsecurities.service.impl;

import java.time.ZoneId;
import java.util.Objects;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.bean.ScriptBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.dm.ObjectAssembler;

public class ScriptDataAssembler implements ObjectAssembler{

	@Override
	public void assemble(Object obj, UserLoginBean uam) throws ServiceException {
		ScriptBean scriptBean = (ScriptBean) obj;
		if(Objects.nonNull(scriptBean.getActualDateTransferDl())) {
			scriptBean.setActualDateTransferIEPF(scriptBean.getActualDateTransferDl().toInstant().atZone(ZoneId.systemDefault())
			      .toLocalDateTime());
		}
	}

}
