package com.psoderi.autenticacao.response;

public class Erro {
	
	private int code;
	private String message;
	
	
	public Erro(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	
	
	public Erro() {
	}


	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
