package com.psoderi.autenticacao.model;

import java.security.MessageDigest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;


@Entity(name = "usuario")
public class User {
	
	
	
	@Id
	@Column(name = "login_user")
	private String login;	
	
	@Column(name = "activation_code")
	private String activationCode;
	
	@Column(name = "md5_user")
	private String md5;	
	
	@Column(name="status")
	private int status;
	
	@Transient
	private String password;
	
	@Transient
	private static final String CHAVE_SHA = "IndraInovation";
	
	@Transient
	private String oldPassword;
	
	@Transient
	public static int sequenciaChaveAtivcao = 0;
	
	public User(){
		
	}
		
	public User(String login, String activationCode, int status, String password) {
		super();
		this.login = login;
		this.activationCode = activationCode;
		this.status = status;
		this.password = password;
	}

	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String senhaUsuario) {
		this.password = senhaUsuario;
	}
	
	public String getMd5() {
		return md5;
	}
	
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	
	public String getActivationCode() {
		return activationCode != null ? activationCode.toUpperCase() : null;
	}
	
	public void setActivationCode(String chave_ativacao_usuario) {
		this.activationCode = chave_ativacao_usuario.toUpperCase();
	}
	
	public void criptografarSenhaUsuario(){		
		if(this.password != null){
			this.md5 = geraCriptografia(this.password);
		}
	}

	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setStatus() {
		if(this.password == null){
			this.status = 0;
		}else{
			this.status = 1;
		}		
	}
	
	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String geraCriptografia(String senha){
		MessageDigest algorithm = null;
    	String senhaCriptografada = null;
		try {
			algorithm = MessageDigest.getInstance("SHA-256");
			byte messageDigest[] = algorithm.digest(senha.concat(CHAVE_SHA).getBytes("UTF-8"));
			StringBuilder hexString = new StringBuilder();
	    	for (byte b : messageDigest) {
	    	  hexString.append(String.format("%02X", 0xFF & b));
	    	}
	    	senhaCriptografada = hexString.toString();
		} catch (Exception e) {
			senhaCriptografada = senha;
			e.printStackTrace();
		}
		return senhaCriptografada;
	}
	

	public String validateRequiredFieldsCreateUser(){
		StringBuilder sb = new StringBuilder();
		if(this.getLogin() == null){
			sb.append("Login is required");
		}			
		return sb.toString();
	}

	public String validateRequiredFieldsIncluirSenha() {
		StringBuilder sb = new StringBuilder();
		if(this.getLogin() == null){
			sb.append("login is required");
		}		
		if(this.getPassword() == null){
			sb.append("password is required");
		}
		if(this.getOldPassword() == null){
			sb.append("oldPassword is required");
		}
		return sb.toString();
	}

	public String validateRequiredFieldsEnableUser() {
		StringBuilder sb = new StringBuilder();
		if(this.password == null){
			sb.append("password is required");
		}
		if(this.activationCode == null){
			sb.append("activationCode is required");
		}
		return sb.toString();
	}

	public String validateRequiredFieldsForgotPassword() {
		StringBuilder sb = new StringBuilder();
		if(this.login == null){
			sb.append("login is required");
		}
		return sb.toString();
	}

	public String validateRequiredFieldsGetActivationCode() {
		StringBuilder sb = new StringBuilder();
		if(this.login == null){
			sb.append("login is required");
		}
		return sb.toString();
	}

	public String validateRequiredFieldsLogin() {
		StringBuilder sb = new StringBuilder();
		if(this.getLogin() == null){
			sb.append("login is required");
		}		
		if(this.getPassword() == null){
			sb.append("password is required");
		}

		return sb.toString();
	}
}