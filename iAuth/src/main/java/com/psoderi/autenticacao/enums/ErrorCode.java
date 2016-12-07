package com.psoderi.autenticacao.enums;

public enum ErrorCode  {
	
	VALIDATION_EXCEPTION_PASSWORD(1, "Password is required"),
	USER_DOES_NOT_EXISTS(2, "User doesn't exist"),
	UPDATEPASSWORD_USER_INACTIVE(3,"User is not active, please active it before change it's password"),
	UPDATEPASSWORD_OLDPASSWORD_WRONG(4,"old_password is wrong"),
	USER_ALREDY_EXISTS(5,"User Alredy Exists"),
	ENABLEUSER_ACTIVATION_CODE_DOESNOT_EXISTS(6,"This activation_code doesn't exist"),
	ENABLEUSER_ACTIVATION_CODE_ALREDY_USED(7,"activation_code alredy used");
	
	
	private final int code;
	private final String message;
	
	ErrorCode (int code, String message){
        this.code = code;
        this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}