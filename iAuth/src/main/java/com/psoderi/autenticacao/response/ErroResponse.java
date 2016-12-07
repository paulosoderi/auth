package com.psoderi.autenticacao.response;

import java.util.Date;

import org.springframework.http.HttpStatus;

import com.psoderi.autenticacao.exception.GenericException;

public class ErroResponse {
	
	
	 private long timestamp;
	 private int status;
	 private String error;
	 private String exception;
	 private int errorCode;
	 private String message;
	 private String path;
	 
	public ErroResponse(){}
	
	public ErroResponse(GenericException ex, HttpStatus status, String path){
		this.timestamp = new Date().getTime();
		this.status = status.value();
		this.error = status.getReasonPhrase();
		this.exception = ex.toString();
		this.message = ex.getMessage();
		this.path = path;
		if(ex.getErrorCode() != null){
			this.errorCode = ex.getErrorCode().getCode();
		}
		
	}
	 
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getException() {
		return exception;
	}
	public void setException(String exception) {
		this.exception = exception;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
}
