package com.psoderi.autenticacao.exception;

import com.psoderi.autenticacao.enums.ErrorCode;

public class CreateUserExecption extends GenericException{
	
	private static final long serialVersionUID = 5343433614735949292L;

	public CreateUserExecption(ErrorCode code) {
		super(code);
	}
	
	public CreateUserExecption(String msg) {
		super(msg);
	}

	public CreateUserExecption(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	@Override
	public Throwable getCause(){
		return super.getCause();
	}

}
