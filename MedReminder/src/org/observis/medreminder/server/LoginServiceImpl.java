package org.observis.medreminder.server;
import java.sql.*;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.observis.medreminder.client.LoginService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

	private static final long serialVersionUID = 109090909L;
	private int timeout;
	private boolean loggedIn = false;
	@Override
	public boolean checkLoggedIn() throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String logIn(String username, String password) throws IllegalArgumentException {
		if (DatabaseConnector.checkLogin(username, password)){
			UUID sessionID = UUID.randomUUID();
	        HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
	        HttpSession session = httpServletRequest.getSession(true);
	        session.setAttribute("user", username);
	        session.setAttribute("sid", sessionID);
	        getThreadLocalRequest().getSession().setMaxInactiveInterval(10000);
			System.out.println("Successful login. SID: "+sessionID);	
			return sessionID.toString();
		}else{
			loggedIn = false;
			return "invalid";
		}
	}

	@Override
	public Integer getUserSessionTimeout() {
		timeout = getThreadLocalRequest().getSession().getMaxInactiveInterval() * 1000;
		return timeout;
	}

	@Override
	public Boolean isSessionAlive() {
		return new Boolean(
				(System.currentTimeMillis() - getThreadLocalRequest().getSession().getLastAccessedTime()) < timeout);
	}
	
	private Boolean isUserInSession(String sid){
		HttpServletRequest req = this.getThreadLocalRequest();
		HttpSession session = req.getSession(true);
		Object sidObj = session.getAttribute("sid");
		if(sidObj!=null && sidObj instanceof UUID){
			return true;
		}
		return false;
	}

	@Override
	public void ping() {

	}

}
