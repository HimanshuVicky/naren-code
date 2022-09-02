package com.assignsecurities.app.exception;

/**
 * FormatExceptionException
 *
 */
public class FormatException extends ServiceException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2514251091475195530L;

	public FormatException() {
        super();
    }

    public FormatException(Throwable t) {
        super(t);
    }

    public FormatException(String str) {
        super(str);
    }

    public FormatException(String str, Throwable e) {
        super(str, e);
    }
}
