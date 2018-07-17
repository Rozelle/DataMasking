package com.datamask.masquerade.client;

import com.google.gwt.core.client.EntryPoint;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Masquerade implements EntryPoint {
	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 **/
	private final LoginServiceAsync loginService = GWT.create(LoginService.class);

	/** This is the entry point method. **/
	public void onModuleLoad() {

		/** Host page root panel name it as container **/
		RootPanel rootPanel = RootPanel.get("container");
		
		/** Login message **/
		final Label loginMessage = new Label();
		loginMessage.setText("Enter your credentials:");
			
		/** Text box for login **/
		final HorizontalPanel loginPanel = new HorizontalPanel();
		final TextBox loginBox = new TextBox();
		loginBox.getElement().setPropertyString("placeholder", "Login");
		loginPanel.add(loginBox);

		/** Password text box for password **/
		final HorizontalPanel passwordPanel = new HorizontalPanel();
		final PasswordTextBox passwordBox = new PasswordTextBox();
		passwordBox.getElement().setPropertyString("placeholder", "Password");
		passwordPanel.add(passwordBox);

		/** Error message on wrong credentials **/
		final Label invalidCredMessage = new Label();
		invalidCredMessage.addStyleName("invalidCredLabel");

		/** login button **/
		final Button loginButton = new Button("login");

		/** add all panels to root panel **/
		rootPanel.add(loginMessage);
		rootPanel.add(loginPanel);
		rootPanel.add(passwordPanel);
		rootPanel.add(invalidCredMessage);
		rootPanel.add(loginButton);

		/** Add style names to widgets **/
		loginMessage.addStyleName("loginMessage");
		loginBox.addStyleName("loginBox");
		passwordBox.addStyleName("passwordBox");
		invalidCredMessage.addStyleName("invalidCredMessage");
		loginButton.addStyleName("loginButton");

		/** Create a handler for the sendButton and nameField **/
		class MyHandler implements ClickHandler, KeyUpHandler {
			public void onClick(ClickEvent event) {
				Widget sender = (Widget) event.getSource();

				if (sender == loginButton)
					login();
			}

			public void onKeyUp(KeyUpEvent event) {
				Widget sender = (Widget) event.getSource();

				if (sender == passwordBox && event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
					login();
			}

			private void login() {
				final String loginStr = loginBox.getText();
				String passwordStr = passwordBox.getText();

				loginService.logintoServer(loginStr, passwordStr, new AsyncCallback<Boolean>() {
					public void onFailure(Throwable caught) {
					}

					public void onSuccess(Boolean result) {
						if (result) {
							RootPanel.get("container").clear();
							DashboardView.setContent();
							
						}

						else {
							passwordBox.setFocus(true);
							loginButton.setEnabled(true);
							invalidCredMessage.setText("Incorrect login or password");
						}
					}

				});

			} // end of login() method

		}// end of class Myhandler

		/** Add a handler to send the login/password to the server **/
		MyHandler handler = new MyHandler();
		loginButton.addClickHandler(handler);
		passwordBox.addKeyUpHandler(handler);
	}// end of onModuleLoad
}
