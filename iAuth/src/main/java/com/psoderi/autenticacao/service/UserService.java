package com.psoderi.autenticacao.service;

import java.util.List;
import java.util.Random;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.psoderi.autenticacao.dao.UserDAO;
import com.psoderi.autenticacao.enums.ErrorCode;
import com.psoderi.autenticacao.exception.ActivationCodeException;
import com.psoderi.autenticacao.exception.CreateUserExecption;
import com.psoderi.autenticacao.exception.EnableUserException;
import com.psoderi.autenticacao.exception.ForgotPasswordException;
import com.psoderi.autenticacao.exception.UpdatePasswordException;
import com.psoderi.autenticacao.model.ActivationCodeResponse;
import com.psoderi.autenticacao.model.User;

import javassist.NotFoundException;

@Service
public class UserService {

	@Autowired
	private UserDAO	userDAO;

	public List<User> getUserList(){
		return (List<User>) userDAO.findAll();
	}
	
	@Transactional
	public ActivationCodeResponse createUser(User user) throws Exception{
		String validation = user.validateRequiredFieldsCreateUser();
		if(validation.length() > 0){
			throw new CreateUserExecption(validation); 
		}
		User usuarioAtualizado = null;
		user.setActivationCode(this.gerarChaveAtivacao());
		user.criptografarSenhaUsuario();
		user.setStatus();
			
		usuarioAtualizado = userDAO.createUser(user);
		if(user.getStatus() == 0 ){
			return new ActivationCodeResponse(usuarioAtualizado.getActivationCode());
		}else{
			return null;
		}

	}
	
	@Transactional
	public void updatePassword(User user) throws UpdatePasswordException {
		/*
		String validation = user.validateRequiredFieldsIncluirSenha();
		if(validation.length() > 0){
			return validation;
		}*/
		User userAux = user;
		user = userDAO.findUserByID(user.getLogin());
		
		//verifica se o usuario existe
		if(user == null){
			throw new UpdatePasswordException(ErrorCode.USER_DOES_NOT_EXISTS);
		}
		
		//se o usuario ainda nao estiver ativo, nao pode atualizar a senha
		if(user.getStatus() == 0){
			throw new UpdatePasswordException(ErrorCode.UPDATEPASSWORD_USER_INACTIVE);
		}
		
		//verifica se a senha antiga digitada confere com o que tem na base
		if(!user.geraCriptografia(userAux.getOldPassword()).equals(user.getMd5())){
			throw new UpdatePasswordException(ErrorCode.UPDATEPASSWORD_OLDPASSWORD_WRONG);
		}		
		
		user.setPassword(userAux.getPassword());
		user.criptografarSenhaUsuario();
		
		userDAO.createUser(user);
	}
	
	@Transactional
	public boolean enableUser(User user) {
		User userAux = user;
		user = userDAO.findUserByActivationCode(user.getActivationCode());
				
		//verifica se o usuario existe
		if(user == null){
			throw new EnableUserException(ErrorCode.ENABLEUSER_ACTIVATION_CODE_DOESNOT_EXISTS);
		}
		
		//verifica se o usuario ja foi ativado por essa chave
		if(user.getStatus() == 1){
			throw new EnableUserException(ErrorCode.ENABLEUSER_ACTIVATION_CODE_ALREDY_USED);
		}

		user.setPassword(userAux.getPassword());
		user.criptografarSenhaUsuario();
		user.setStatus();
		userDAO.createUser(user);
		return true;
	}
	
	@Transactional
	public String forgotPassword(User user) throws ForgotPasswordException{
		user = userDAO.findUserByID(user.getLogin());
		
		//verifica se o usuario existe
		if(user == null){
			throw new ForgotPasswordException(ErrorCode.USER_DOES_NOT_EXISTS);
		}
		
		//Usuario tem que reativar a conta, seta status = 0
		user.setPassword(null);
		user.setActivationCode(this.gerarChaveAtivacao());
		user.setStatus(0);
		user.setMd5(null);
		
		user = userDAO.createUser(user);
		return user.getActivationCode();
	}	
	

	public String getActivationCode(User user) {
		user = userDAO.findUserByID(user.getLogin());
		
		//verifica se o usuario existe
		if(user == null){
			throw new ActivationCodeException(ErrorCode.USER_DOES_NOT_EXISTS);
		}
		
		return user.getActivationCode();
	}
	
	public void login(User user) throws Exception{
		User userAux = user;
		try{    	
	    	user = userDAO.findUserByID(user.getLogin());	    	
    	}catch(Exception ex){  	    		
    		throw new Exception("Something goes wrong");
    	}  
		if(user != null){
    		String senhaInformada = userAux.geraCriptografia(userAux.getPassword());
    		String senhaGravada = user.getMd5();
    		if(!senhaInformada.equals(senhaGravada)){
    			throw new AuthenticationException("Password is wrong");
    		}
    	} else{
    		throw new NotFoundException("User not found");
    	}
	}
	
	@Transactional
	public void delete(User user) throws Exception{
		try{    	
	    	user = userDAO.findUserByID(user.getLogin());	    	
    	}catch(Exception ex){  	    		
    		throw new Exception("Something goes wrong");
    	}  
		if(user == null){
    		throw new NotFoundException("User not found");
    	} else{
    		userDAO.delete(user);
    	}
	}


	
	public String gerarChaveAtivacao(){
		String chave = userDAO.findSequenceActivationCode();
		
		chave = chave.trim();
		StringBuffer resp = new StringBuffer();
		int fim = 6 - chave.length();
		final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		final int N = alphabet.length();
		Random r = new Random();			    
		    
		for (int x = 0; x < fim; x++){
			resp.append(alphabet.charAt(r.nextInt(N)));
		}
		chave = resp + chave;
		
		StringBuilder chaveAtivacao = new StringBuilder();
		chaveAtivacao.append(chave);
		return chaveAtivacao.toString();
	}
}
