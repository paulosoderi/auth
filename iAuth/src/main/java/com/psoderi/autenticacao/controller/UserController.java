package com.psoderi.autenticacao.controller;

import java.util.List;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.psoderi.autenticacao.enums.ErrorCode;
import com.psoderi.autenticacao.exception.ActivationCodeException;
import com.psoderi.autenticacao.exception.CreateUserExecption;
import com.psoderi.autenticacao.exception.EnableUserException;
import com.psoderi.autenticacao.exception.ForgotPasswordException;
import com.psoderi.autenticacao.exception.GenericException;
import com.psoderi.autenticacao.exception.UpdatePasswordException;
import com.psoderi.autenticacao.model.ActivationCodeResponse;
import com.psoderi.autenticacao.model.MessageResponse;
import com.psoderi.autenticacao.model.User;
import com.psoderi.autenticacao.response.ErroResponse;
import com.psoderi.autenticacao.service.UserService;

import io.swagger.annotations.ApiOperation;
import javassist.NotFoundException;


@RestController
public class UserController {
	
	@Autowired
	UserService userService;

	@ApiOperation(value = "Verify if server is alive")
	@RequestMapping(value = "/api/", method = RequestMethod.GET)
	public ResponseEntity<Object> index(@RequestParam(value="apiKey", required=true) String apiKey){
		return new ResponseEntity<Object>(new MessageResponse<String>("200", "SUCCESS", "Server is alive..."), HttpStatus.OK);		
	}
	
	
	@ApiOperation(value = "Get the user list")
	@RequestMapping(value = "/api/user", method = RequestMethod.GET)
	public ResponseEntity<Object> getUserList(@RequestParam(value="apiKey", required=true) String apiKey){
		return new ResponseEntity<Object>(new MessageResponse<List<User>>("200", "List of all users", (List<User>) userService.getUserList() ), HttpStatus.OK);
	}	
	
	
	@ApiOperation(value = "Add a new user to application")
	@RequestMapping(value = "/api/user", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> criarUsuario(@RequestParam(value="apiKey", required=true) String apiKey,
												@RequestParam(value="login", required=true) String login,
												@RequestParam(value="password", required=false) String password){
		String path = "/user";
		try{
			User user = new User();
			user.setLogin(login);
			user.setPassword(password);
			ActivationCodeResponse ac = userService.createUser(user);
			if (ac != null){
				return new ResponseEntity<Object>(new MessageResponse<ActivationCodeResponse>("200", "User created with success, for activate use activationCode", ac ), HttpStatus.OK);
			}else{
				return new ResponseEntity<Object>(new MessageResponse<String>("200", "User created and activated with success", null ), HttpStatus.OK);
			}			 
		}catch(CreateUserExecption e){
			return new ResponseEntity<Object>(new ErroResponse(e, HttpStatus.BAD_REQUEST, path), HttpStatus.BAD_REQUEST); 
		}catch (DataIntegrityViolationException e){
			return new ResponseEntity<Object>(new ErroResponse(new GenericException(ErrorCode.USER_ALREDY_EXISTS), HttpStatus.BAD_REQUEST, path), HttpStatus.BAD_REQUEST);
		}catch(Exception ex){
			return new ResponseEntity<Object>(new ErroResponse(new GenericException(ex), HttpStatus.INTERNAL_SERVER_ERROR, path), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@ApiOperation(value = "Delete an existent user")
	@RequestMapping(value = "/api/user/delete", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> excluirUsuario(@RequestParam(value="apiKey", required=true) String apiKey,
										@RequestParam(value="login", required=true) String login){
		String path = "/user/delete";		
		User user = new User();
		user.setLogin(login);
		try {
			userService.delete(user);
			return new ResponseEntity<Object>(new MessageResponse<ActivationCodeResponse>("200", "Success", null), HttpStatus.OK);			
		}   catch (NotFoundException e) {
			return new ResponseEntity<Object>(new ErroResponse(new GenericException(e), HttpStatus.NOT_FOUND, path ), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<Object>(new ErroResponse(new GenericException(e), HttpStatus.INTERNAL_SERVER_ERROR, path), HttpStatus.INTERNAL_SERVER_ERROR);			
		}
	}
	
	
	@ApiOperation(value = "Enable a user using it's activation code")
	@RequestMapping(value = "/api/user/enableUser", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> enableUser(@RequestParam(value="apiKey", required=true) String apiKey,
												@RequestParam(value="password", required=true) String password,
												@RequestParam(value="activation_code", required=true) String activationCode){
		
		String path = "/user/enableUser";
		
		User user = new User();
		user.setPassword(password);
		user.setActivationCode(activationCode);
		
		try{
			boolean isActivated = userService.enableUser(user);
			if(isActivated){				
				return new ResponseEntity<Object>(new MessageResponse<String>("200", "User activated with success", null ), HttpStatus.OK);
			}else{
				return new ResponseEntity<Object>(new ErroResponse(new GenericException("Ops! Something went wrong!"), HttpStatus.INTERNAL_SERVER_ERROR, path), HttpStatus.INTERNAL_SERVER_ERROR);
			}			
		}catch (EnableUserException e){
			return new ResponseEntity<Object>(new ErroResponse(e, HttpStatus.BAD_REQUEST, path), HttpStatus.BAD_REQUEST);
		}catch(Exception ex){
			return new ResponseEntity<Object>(new ErroResponse(new GenericException(ex), HttpStatus.INTERNAL_SERVER_ERROR, path), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
		
	
	@ApiOperation(value = "Updates the password of an existing User")
	@RequestMapping(value = "/api/user/updatePassword", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> updatePassword(@RequestParam(value="apiKey", required=true) String apiKey,
												  @RequestParam(value="login", required=true) String login,
												  @RequestParam(value="password", required=true) String password,
												  @RequestParam(value="old_password", required=true) String oldPassword){
	
		String path = "/api/user/updatePassword";
				
		User user = new User();
		user.setLogin(login);
		user.setPassword(password);
		user.setOldPassword(oldPassword);
		
		try{
			 userService.updatePassword(user);
			 return new ResponseEntity<Object>(new MessageResponse<String>("200", "User updated with success", null ), HttpStatus.OK);
			 
		}catch(UpdatePasswordException e){
			return new ResponseEntity<Object>(new ErroResponse(e, HttpStatus.BAD_REQUEST, path), HttpStatus.BAD_REQUEST);
		}catch(GenericException ex){
			return new ResponseEntity<Object>(new ErroResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, path), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	

	@ApiOperation(value = "Generete a new activation code for forgotten users")
	@RequestMapping(value = "/api/user/forgotPassword", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> forgotPassword(@RequestParam(value="apiKey", required=true) String apiKey,
												 @RequestParam(value="login", required=true) String login){
		String path = "/user/forgotPassword";
		
		User user = new User();
		user.setLogin(login);
		try{
			String ac = userService.forgotPassword(user);
			return new ResponseEntity<Object>(new MessageResponse<ActivationCodeResponse>("200", "User reseted with success. For activate this user, please use activationCode", new ActivationCodeResponse(ac)), HttpStatus.OK);
		}catch(ForgotPasswordException e){
			return new ResponseEntity<Object>(new ErroResponse(e, HttpStatus.BAD_REQUEST, path), HttpStatus.BAD_REQUEST);
		}catch(GenericException ex){
			return new ResponseEntity<Object>(new ErroResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, path), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@ApiOperation(value = "Get the activation code to specific User")
	@RequestMapping(value = "/api/user/activationCode", method = RequestMethod.GET,  produces = "application/json")
	public ResponseEntity<Object> getActivationCode(@RequestParam(value="apiKey", required=true) String apiKey,
													@RequestParam(value="login", required=true) String login){
		String path = "/user/activationCode";
		User user = new User();	
		user.setLogin(login);
		
		try{
			String ac = userService.getActivationCode(user);
			return new ResponseEntity<Object>(new MessageResponse<ActivationCodeResponse>("200", "Success to find user/code", new ActivationCodeResponse(ac)), HttpStatus.OK);
		}catch(ActivationCodeException e){
			return new ResponseEntity<Object>(new ErroResponse(e, HttpStatus.BAD_REQUEST, path), HttpStatus.BAD_REQUEST);
		}catch(GenericException ex){
			return new ResponseEntity<Object>(new ErroResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, path), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@ApiOperation(value = "Log user in application")
	@RequestMapping(value = "/api/user/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> login(@RequestParam(value="apiKey", required=true) String apiKey,
										@RequestParam(value="login", required=true) String login,
										@RequestParam(value="password", required=true) String password){
		String path = "/user/login";
		
		User user = new User();
		user.setLogin(login);
		user.setPassword(password);
		try {
			userService.login(user);
			return new ResponseEntity<Object>(new MessageResponse<ActivationCodeResponse>("200", "Success", null), HttpStatus.OK);			
		}  catch (AuthenticationException e) {
			return new ResponseEntity<Object>(new ErroResponse(new GenericException(e), HttpStatus.UNAUTHORIZED, path ), HttpStatus.UNAUTHORIZED);
		} catch (NotFoundException e) {
			return new ResponseEntity<Object>(new ErroResponse(new GenericException(e), HttpStatus.NOT_FOUND, path ), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<Object>(new ErroResponse(new GenericException(e), HttpStatus.INTERNAL_SERVER_ERROR, path), HttpStatus.INTERNAL_SERVER_ERROR);			
		}
	}	
}
