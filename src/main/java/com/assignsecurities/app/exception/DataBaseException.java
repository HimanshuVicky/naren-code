package com.assignsecurities.app.exception;

public class DataBaseException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1150184073087037605L;

	private long exceptionCode;

	public static final Long EXCEL_MAX_ROW_LIMIT_EXCEED = 1L;

	public DataBaseException() {
		super();
	}

	public DataBaseException(String str) {
		super(str);
	}

	public DataBaseException(String str, Throwable e) {
		super(str, e);
	}

	public DataBaseException(Throwable e) {
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
