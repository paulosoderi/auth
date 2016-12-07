package com.psoderi.autenticacao.exception;

import com.psoderi.autenticacao.enums.ErrorCode;

public class ActivationCodeException extends GenericException {

	private static final long serialVersionUID = 1149241039409861914L;

	public ActivationCodeException(ErrorCode code) {
		super(code);
	}

	public ActivationCodeException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	@Override
	public Throwable getCause(){
		return super.getCause();
	}
}
