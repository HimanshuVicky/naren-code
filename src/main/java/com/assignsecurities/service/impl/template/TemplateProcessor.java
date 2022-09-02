package com.assignsecurities.service.impl.template;

import com.assignsecurities.bean.CaseDocumentBean;
import com.assignsecurities.bean.FileBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.domain.CaseModel;

public interface TemplateProcessor {
	public FileBean processTemplate(CaseDocumentBean caseDocumentBean, UserLoginBean userLoginBean,
			CaseModel caseModel);
}