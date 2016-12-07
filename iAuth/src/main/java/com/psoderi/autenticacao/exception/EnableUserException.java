package com.psoderi.autenticacao.exception;

import com.psoderi.autenticacao.enums.ErrorCode;

public class EnableUserException extends GenericException {

	private static final long serialVersionUID = 1149241039409861914L;

	public EnableUserException(ErrorCode code) {
		super(code);
	}

	public EnableUserException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	@Override
	public Throwable getCause(){
		return super.getCause();
	}
}
