package com.assignsecurities.app.exception;

public class ServiceException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6590614303361127446L;
	
	private long exceptionCode;
	
	public static final Long EXCEL_MAX_ROW_LIMIT_EXCEED=1L;

	public ServiceException() {
		super();
	}
	
	public ServiceException(String str) {
		super(str);
	}

	public ServiceException(String str, Throwable e) {
		super(str, e);
	}

	public ServiceException(Throwable e) {
		super(e);
	}

	/**
	 * @return the exceptionCode
	 */
	public long getExceptionCode() {
		return exceptionCode;
	}

	/**
	 * @param exceptionCode the exceptionCode to set
	 */
	public void setExceptionCode(long exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
	
}
