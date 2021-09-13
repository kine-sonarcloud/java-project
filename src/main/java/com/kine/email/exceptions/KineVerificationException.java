

package com.kine.email.exceptions;
public class KineVerificationException extends RuntimeException  {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private final int errorCode;

	public KineVerificationException(int errorCode, String errorMessage, Throwable e) {
		super(errorMessage, e);
		this.errorCode = errorCode;
	}

	public KineVerificationException(int errorCode, String errorMessage) {
		super(errorMessage);
		this.errorCode = errorCode;
	}

		@Override
	public synchronized Throwable fillInStackTrace() {
		return this.getCause();
	}

}