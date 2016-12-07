package com.psoderi.autenticacao.exception;

import com.psoderi.autenticacao.enums.ErrorCode;

public class UpdatePasswordException extends GenericException {

	private static final long serialVersionUID = 1149241039409861914L;

	public UpdatePasswordException(ErrorCode code) {
		super(code);
	}

	public UpdatePasswordException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	@Override
	public Throwable getCause(){
		return super.getCause();
	}
}
