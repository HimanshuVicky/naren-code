package com.assignsecurities.app.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.bean.ValidationError;

public class ValidationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5392734698436236742L;

	public ValidationException(List<ValidationError> errorList)
	{
	    this.errorList=errorList;
	}
	public ValidationException(String message, ValidationError singleError) {
	    super(message);
	    this.errorList= Arrays.asList(new ValidationError[]{singleError});
	}
	public ValidationException(ValidationError singleError) {
	    this.errorList= Arrays.asList(new ValidationError[]{singleError});
	}
	public ValidationException(String message, List<ValidationError> errorList) {super(message);this.errorList=errorList;}

	private transient List<ValidationError> errorList = new ArrayList<>();

	public List<ValidationError> getErrorList() {
	    return errorList;
	}

	public void setErrorList(List<ValidationError> errorList) {
	    this.errorList = errorList;
	}

	public Boolean hasErrorList(){
	    return ArgumentHelper.isNotEmpty(errorList);
	}
}
