package com.cabin.common.exception;

public class UserAlreadyActiveException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserAlreadyActiveException(String message) {
		super(message);
	}

}
