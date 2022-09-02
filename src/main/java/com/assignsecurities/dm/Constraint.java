package com.assignsecurities.dm;


import com.assignsecurities.app.exception.ValidationException;

/**
 * Validates a piece of data. Serves as a public interface for all constraints.
 * The implementors need to specify a error code and error message
 * associated with the constraint.
 *
 */

public interface Constraint {

  /**
   * Check whether the value meets the implementors criteria testing
   *
   */
  public boolean validate(String value) throws ValidationException;

  /**
   * Get the error message associated with this constraint
   *
   * @return the error message
   */
  public String getErrorMessage();

  /**
   * Set the error message associated with this constraint.
   *
   * @param String the error message
   */
//  public void setErrorMessage(String errMsg);

  /**
   * Get the error code associated with this constraint
   *
   * @return the error code
   */
//  public String getErrorCode();

  /**
   * Set the error code associated with this constraint.
   *
   * @param String the error code
   */
//  public void setErrorCode(String errCode);

}//end-interface
