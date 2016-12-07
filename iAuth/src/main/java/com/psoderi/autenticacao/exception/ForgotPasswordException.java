package com.psoderi.autenticacao.exception;

import com.psoderi.autenticacao.enums.ErrorCode;

public class ForgotPasswordException extends GenericException {

	private static final long serialVersionUID = 1149241039409861914L;

	public ForgotPasswordException(ErrorCode code) {
		super(code);
	}
	
	@Override
	public Throwable getCause(){
		return super.getCause();
	}
}
