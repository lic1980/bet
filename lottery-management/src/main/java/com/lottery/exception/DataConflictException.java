package com.lottery.exception;

public class DataConflictException extends Exception{
	public DataConflictException() {
	}

	public DataConflictException(String message) {
		super(message);
	}
}
