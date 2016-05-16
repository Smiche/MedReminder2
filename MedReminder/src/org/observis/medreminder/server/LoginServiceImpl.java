package org.observis.medreminder.server;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.observis.medreminder.client.LoginService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

	private static final long serialVersionUID = 109090909L;
	private int timeout;
	@Override
	public boolean checkLoggedIn() throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String logIn(String username, String password) throws IllegalArgumentException {
		DataBaseConnection dbCon = new DataBaseConnection();
		if (dbCon.checkLogin(username, password)){
			UUID sessionID = UUID.randomUUID();
	        HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
	        HttpSession session = httpServletRequest.getSession(true);
	        session.setAttribute("user", username);
	        session.setAttribute("sid", sessionID);
	        session.setMaxInactiveInterval(900000);
	        //getThreadLocalRequest().getSession().setMaxInactiveInterval(10000);
			System.out.println("Successful login. SID: "+sessionID);	
			return sessionID.toString();
		}else{
			return "invalid";
		}
	}

	@Override
	public void logOut(){
		HttpServletRequest servReq = this.getThreadLocalRequest();
		HttpSession session = servReq.getSession(true);
		session.removeAttribute("sid");		
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
	public Boolean isUserInSession(String sid){
		HttpServletRequest req = this.getThreadLocalRequest();
		HttpSession session = req.getSession(true);
	
		UUID sidObj = (UUID) session.getAttribute("sid");
		String sidStr = "";
		if(sidObj!=null && sidObj instanceof UUID){
			sidStr = sidObj.toString();
		}	
		
		if(sidStr.equals(sid)){
			return true;
		}
		return false;
	}

	@Override
	public void ping() {

	}

}
