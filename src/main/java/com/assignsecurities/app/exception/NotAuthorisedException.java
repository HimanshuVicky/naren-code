package com.assignsecurities.app.exception;

/**
 * FormatExceptionException
 *
 */
public class NotAuthorisedException extends ServiceException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2514251091475195530L;

	public NotAuthorisedException() {
        super();
    }

    public NotAuthorisedException(Throwable t) {
        super(t);
    }

    public NotAuthorisedException(String str) {
        super(str);
    }

    public NotAuthorisedException(String str, Throwable e) {
        super(str, e);
    }
}
