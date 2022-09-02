package com.assignsecurities.dm;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Abstract implementation of the Constraint. Subclasses should implement the
 * validate() method.
 * 
 */

public abstract class AbstractConstraint implements Constraint {

	// private String errorMsg;
	// private String errorCode;

	/**
	 * This method needs to be implemented by every constraint.
	 * 
	 * @return
	 */
	public abstract String getErrorKey();

	/**
	 * This method needs to be overridden by constraints that have dynamic
	 * substitution of values in error messages
	 * 
	 * @return
	 */
	public Map getErrorMap() {
		return null;
	}

	public String getErrorMessage() {
		// for constraints which do not have dynamic messages.
		// get the resource key for the error message from the sub-class.
		// call the getErrorMessage(key) API internally to get the message.
		//
		// for constraints which have dynamic messages,
		// get the resource key and the value substitution map from the
		// instance.
		// call the getErrorMessage(key,map) API internally to get the error
		// message.
		if (getErrorMap() == null) {
			return getErrorMessage(getErrorKey());
		} else {
			Map errMap = getErrorMap();
			// begin format dynamic data that goes into the error msgs
			Set keySet = errMap.keySet();
			Iterator itr = keySet.iterator();
			while (itr.hasNext()) {
				Object key = itr.next();
				Object value = errMap.get(key);
				// TODO formatter value
				String formattedValue = value.toString();
				// put fmtted data in error map
				errMap.put(key, formattedValue);

			}
			return getErrorMessage(getErrorKey(), errMap);
		}
	}

	// public void setErrorMessage(String errMsg) {
	// this.errorMsg = errMsg;
	// }
	//
	// public String getErrorCode() {
	// return errorCode;
	// }
	//
	// public void setErrorCode(String errCode) {
	// this.errorCode = errCode;
	// }

	public String getErrorMessage(Object key) {
		String errMsg = null;

		errMsg = String.valueOf(key);
		return errMsg;
	}

	public String getErrorMessage(Object key, Map params) {
		String errMsg = null;
		errMsg = String.valueOf(key);
		return errMsg;
	}

}
