package org.observis.medreminder.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
public interface LoginServiceAsync {
	void getUserSessionTimeout(AsyncCallback<Integer> callback);
	void isSessionAlive(AsyncCallback<Boolean> callback);
	void ping(AsyncCallback<Void> callback);
	void logOut(AsyncCallback<Void> callback);
	void isUserInSession(String session,AsyncCallback<Boolean> callback)throws IllegalArgumentException;
	void checkLoggedIn(AsyncCallback<Boolean> callback) throws IllegalArgumentException;
	void logIn(String username, String password, AsyncCallback<String> callback) throws IllegalArgumentException;
}
