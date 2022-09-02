package com.assignsecurities.dm;


import com.assignsecurities.app.exception.ValidationException;

/**
 * This enforces an "alpha numeric characters only" constraint.
 *
 * <p>Permitted characters
 * <ul>
 * <li>A-Z
 * <li>a-z
 * <li>0-9
 * <li>space
 * </ul>
 *
 */

public class AlphaNumericConstraint extends AbstractConstraint {

  private static AlphaNumericConstraint alphaNumericConstraint = new AlphaNumericConstraint();

  private AlphaNumericConstraint() {
//	String errMsg = this.getErrorMessage(ValidationKeys.GLOBAL_CONSTRAINT_ALPHANUMERIC_KEY);
//	setErrorMessage(errMsg);
    //setErrorMessage("The value should consist of letters and digits");
  }

  public static AlphaNumericConstraint getInstance() {
    return new AlphaNumericConstraint();
  }

  public boolean validate(String value) throws ValidationException {
      for(int i = 0; i < value.length(); i++) {
      char ch = value.charAt(i);
      if (!(Character.isLetter(ch) || Character.isDigit(ch) ||
      			(Character.isWhitespace(ch)))) {
          return false;
      }
      }
      return true;
  }

    /* (non-Javadoc)
     * @see com.elance.proserv.framework.util.validation.AbstractConstraint#getErrorKey()
     */
    public String getErrorKey() {
        return "ValidationKeys.GLOBAL_CONSTRAINT_ALPHANUMERIC_KEY";
    }

}//end-class
