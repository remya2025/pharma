/**
 * 
 */
package com.example.pharma.exception;

/**
 * 
 */
public class PharmaBusinessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int code;

	public PharmaBusinessException(String message, int errorCode) {
		super(message); // Pass the message to the parent Exception class
		this.code = errorCode;
	}

	public PharmaBusinessException(Throwable t) {

	}

	public PharmaBusinessException(String message, Throwable t) {

	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "PharmaBusinessException [code=" + code + ":" + getMessage() + "]";
	}

}
