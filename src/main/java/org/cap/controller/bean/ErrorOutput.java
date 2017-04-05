package org.cap.controller.bean;

public class ErrorOutput {

	private String message;

	public ErrorOutput() {
	}
	
	public ErrorOutput(String msg) {
		this.message = msg;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
