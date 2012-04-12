package com.dreamcather.bicycle.exception;

public class NetworkException extends Exception {
	private static final long serialVersionUID = 1639042918413298333L;

	public NetworkException() {
		super();
	}

	public NetworkException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public NetworkException(String detailMessage) {
		super(detailMessage);
	}

	public NetworkException(Throwable throwable) {
		super(throwable);
	}	
	
}
