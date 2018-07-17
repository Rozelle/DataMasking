package com.datamask.masquerade.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>PasswordChangeService</code>.
 */
public interface SettingServiceAsync {
	void changePassword(String oldPassword, String newPassword, AsyncCallback<Boolean> callback) throws IllegalArgumentException;
	void addUser(String newUserName, String newUserPassword,  AsyncCallback<String> asyncCallback) throws Exception;
	void deleteUser(String deleteUserName, String deleteUserPassword,  AsyncCallback<String> asyncCallback) throws Exception;
	
}
