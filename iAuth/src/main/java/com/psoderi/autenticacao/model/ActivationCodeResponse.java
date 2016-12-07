package com.psoderi.autenticacao.model;

public class ActivationCodeResponse {
	
	private String activationCode;
	
	public ActivationCodeResponse(){}
	
	public ActivationCodeResponse(String activationCode){
		this.activationCode = activationCode;
	}
	
 
	public String getActivationCode() {
		return activationCode;
	}

	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}
	
	
	

}
