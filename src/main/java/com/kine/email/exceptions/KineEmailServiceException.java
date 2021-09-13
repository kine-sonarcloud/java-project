/**
 * @author mallesh
 * @email mallesh@kine.ai
 * @create date 2021-02-04 16:01:48
 * @modify date 2021-02-04 16:01:48
 * @desc [description]
 */

package com.kine.email.exceptions;
public class KineEmailServiceException extends RuntimeException  {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private final int errorCode;

	public KineEmailServiceException(int errorCode, String errorMessage, Throwable e) {
		super(errorMessage, e);
		this.errorCode = errorCode;
	}

	public KineEmailServiceException(int errorCode, String errorMessage) {
		super(errorMessage);
		this.errorCode = errorCode;
	}

		@Override
	public synchronized Throwable fillInStackTrace() {
		return this.getCause();
	}

}