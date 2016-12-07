package com.psoderi.autenticacao.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.psoderi.autenticacao.model.User;

@Repository
public class UserDAO  {
	
	@PersistenceContext
	private EntityManager manager;
	
	private final static String SQL_CONSULTA_SEQ_CHAVE = "SELECT NEXTVAL('seq_dashboard_chave_ativacao')";
	
	public List<User> findAll(){
		return manager.createQuery("select u from com.psoderi.autenticacao.model.User u", com.psoderi.autenticacao.model.User.class).getResultList();
	}

	public User createUser(User user) {
		manager.persist(user);
		return user;
	}

	public String findSequenceActivationCode() {
		javax.persistence.Query query = manager.createNativeQuery(SQL_CONSULTA_SEQ_CHAVE);
		String sequence = query.getSingleResult().toString();
		return sequence;
	}
	
	public User findUserByID(String id){
		return manager.find(User.class, id);
	}
	  
	public User findUserByActivationCode(String activationCode) {
		 User user = null;
		 Query query = manager.createQuery("SELECT obj FROM com.psoderi.autenticacao.model.User obj WHERE UPPER(obj.activationCode) = :CHAVE", com.psoderi.autenticacao.model.User.class);
    	 query.setParameter("CHAVE", activationCode);
    	  
    	 try{
    		 user = (User) query.getSingleResult();
    	 }catch(NoResultException nre){
    		 user = null;
    	 }    	 
         return user;
	}

	public void delete(User user) {
		manager.remove(user);
	}


}
