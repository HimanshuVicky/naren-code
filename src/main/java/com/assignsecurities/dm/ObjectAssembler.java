
package com.assignsecurities.dm;


import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.bean.UserLoginBean;

/**
 * This class is used to assemble object. This class is responsible to populate
 * dependent data models value.
 * 
 * 
 */
public interface ObjectAssembler {
	/**
	 * responsible to assemble object before business validation. this will populate dependent properties values.
	 * @param obj
	 * @param uam
	 */
	public void assemble(Object obj, UserLoginBean uam) throws ServiceException;

}
