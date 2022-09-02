package com.assignsecurities.dm;

import java.io.UnsupportedEncodingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.assignsecurities.app.exception.ValidationException;



/**
 * Enforces a maximum string length.
 *
 */

public class MaxLengthConstraint extends AbstractConstraint {
	private static final Logger logger = LogManager.getLogger(MaxLengthConstraint.class);
  private int maxLength;
  private static final int BYTE_LENGTH_CHECK_LIMIT = 10;
  private static final int MAX_BYTES_ALLOWED = 4000;
  private static final String ENCODING_FORMAT = "utf-8";
  

  public MaxLengthConstraint(int max) {
    this.maxLength = max;	
//	String errMsg = this.getErrorMessage(ValidationKeys.GLOBAL_CONSTRAINT_EXCEED_STORAGE_LIMIT_KEY);
//	setErrorMessage(errMsg);
	//setErrorMessage("The Entered value exceeds the storage limit");
  }

  public boolean validate(String s) throws ValidationException {
    int length = s.length();
    if(length < BYTE_LENGTH_CHECK_LIMIT){
		return checkCharLength(s);
    }else{
    	try{
    		logger.info("s.getBytes(ENCODING_FORMAT).length max length check ="+s.getBytes(ENCODING_FORMAT).length);
			return checkCharLength(s) && s.getBytes(ENCODING_FORMAT).length < MAX_BYTES_ALLOWED;
    	}catch(UnsupportedEncodingException encodingException){
    		logger.info("Encoding excpetion"+encodingException.getMessage());
    		return false;
    	}
    }
    
    /*setErrorMessage("Value entered should not be greater than " + maxLength +
      " characters");*/
  }
  
  private boolean checkCharLength(String s){
	  return s.length() <= this.maxLength;
  }

    /* (non-Javadoc)
     * @see com.elance.proserv.framework.web.validation.AbstractConstraint#getErrorKey()
     */
    public String getErrorKey() {
        return "ValidationKeys.GLOBAL_CONSTRAINT_EXCEED_STORAGE_LIMIT_KEY";
    }
    
    public int getMaxLength()
    {
        return maxLength;
    }

}//end-class
