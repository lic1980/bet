package com.lottery.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.BAD_REQUEST)
public class InvalidParameter extends Exception{
	public InvalidParameter() {
	}

	public InvalidParameter(String message) {
		super(message);
	}
}
