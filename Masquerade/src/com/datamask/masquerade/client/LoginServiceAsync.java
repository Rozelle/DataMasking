package com.datamask.masquerade.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface LoginServiceAsync {
	
  void logintoServer(String login, String password, AsyncCallback<Boolean> callback)
      throws IllegalArgumentException;
}
