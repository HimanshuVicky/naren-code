package com.assignsecurities.dm;


import com.assignsecurities.app.exception.ValidationException;

/**
 * This enforces the supplied email address is of the pattern xxx@xxx.xxx
 * It basically checks whether the string has a "@" and a "."
 *
 */

public class EmailAddressConstraint extends AbstractConstraint {

  private static EmailAddressConstraint emailAddressConstraint = new EmailAddressConstraint();

  private EmailAddressConstraint() {	
//	String errMsg = this.getErrorMessage(ValidationKeys.GLOBAL_CONSTRAINT_EMAIL_KEY);	
//	setErrorMessage(errMsg);
    //setErrorMessage("Please enter a valid email address.");
  }

  public static EmailAddressConstraint getInstance() {
    return emailAddressConstraint;
  }

  public boolean validate(String email) throws ValidationException {

    int atCount = 0;
    int dotCount = 0;
    int emailLength = email.length();

    for(int i = 0; i < emailLength; i++) {

        /* commented by Ramesh
        if((email.charAt(i) == '@') || (email.charAt(i) == '.'))
        END */

        if (email.charAt(i) == '@')  {
			atCount++;
		} else if (email.charAt(i) == '.') {
			dotCount++;
		}
    }

    /* commented by Ramesh
    //modified check to allow email addresses of the form abc@xyz.co.in
    ////System.out.println("In Email Address Constraint, atCount :" + count);
    if(count == 2 || count == 3){
    return true;
    }else{

    return false;
    }
    END */

    if (atCount == 0 || atCount > 1 || dotCount == 0) {
        return false;
    } else if (email.charAt(0) == '@' || email.charAt(0) == '.' ||
               email.charAt(emailLength-1) == '@' || email.charAt(emailLength-1) == '.') {
        return false;
    }

    return true;

  }

    /* (non-Javadoc)
     * @see com.elance.proserv.framework.web.validation.AbstractConstraint#getErrorKey()
     */
    public String getErrorKey() {
        return "ValidationKeys.GLOBAL_CONSTRAINT_EMAIL_KEY";
    }

}//end-class
