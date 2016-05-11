package org.observis.medreminder.server;
import java.sql.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.observis.medreminder.client.LoginService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

	private static final long serialVersionUID = 1L;
	private int timeout;
	private boolean loggedIn = false;
	@Override
	public boolean checkLoggedIn() throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean logIn(String username, String password) throws IllegalArgumentException {
		if (DatabaseConnector.checkLogin(username, password)){
			
	        HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
	        HttpSession session = httpServletRequest.getSession(true);
	        session.setAttribute("user", username);
			System.out.println("Successful login.");	
			return true;
		}else{
			loggedIn = false;
			return false;
		}
		
		// TODO Auto-generated method stub

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

	@Override
	public void ping() {

	}

}
