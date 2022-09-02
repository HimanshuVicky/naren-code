package com.assignsecurities.dm;


import java.util.Map;

import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.domain.dm.AdhocFilterBean;

public interface AdhocFilterService {
	
	public static final String FILTER_TYPE_CSV = "CSV";
	
	public static final String FILTER_TYPE_SQL = "SQL";
	
    public static final char COMMA = ',';
    
    public static final char COLON = ':';

	public AdhocFilterBean getAdhocFilter(String code, UserLoginBean userBean);
	
	public Map<String, String> getIdCode(String filterCode, UserLoginBean userBean);
}
