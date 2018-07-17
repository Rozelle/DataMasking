package com.datamask.masquerade.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */

@RemoteServiceRelativePath("settingservice")
public interface SettingService extends RemoteService {
	boolean changePassword(String oldPassword, String newPassword) throws IllegalArgumentException;
	
	String addUser(String newUserName, String newUserPassword) throws Exception;
	String deleteUser(String deleteUserName, String deleteUserPassword) throws Exception;
}
