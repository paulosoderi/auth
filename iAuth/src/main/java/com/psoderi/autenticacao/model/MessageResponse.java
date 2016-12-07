package com.psoderi.autenticacao.model;

public class MessageResponse<T> {
	
	private String status;
	private String message;
	private T result;
	
	public  MessageResponse(String st, String mb, T bc){
		this.status = st;
		this.message = mb;
		this.result = bc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String statusCode) {
		this.status = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}
}
