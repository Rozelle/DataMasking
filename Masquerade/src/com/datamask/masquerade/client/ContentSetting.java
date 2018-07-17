package com.datamask.masquerade.client;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ContentSetting {
	private final SettingServiceAsync settingService = GWT.create(SettingService.class);

	public static void ContentProvider(VerticalPanel content) {
		
		Label admin = new Label();
	///	admin.setText("Logged in as : admin");
		admin.setStyleName("adminLabelLayout");
		content.add(admin);

		StackPanel panel = new StackPanel();
		panel.setWidth("200px");

		ContentSetting contentSetting = new ContentSetting();
		panel.add(contentSetting.getChangePasswordPanel(), "Change Password");
		panel.add(contentSetting.getNewUserPanel(), "Add New User");
		panel.add(contentSetting.deleteUserPanel(), "Delete Existing User");

		// Add it to the root panel.
		content.add(panel);
	}

	/// FOR PASSWORD CHANGE

	private Widget getChangePasswordPanel() {
		VerticalPanel changePasswordPanel = new VerticalPanel();
		changePasswordPanel.setWidth("450px");

		Grid grid;

		Label oldPasswordLabel = new Label();
		oldPasswordLabel.setText("Enter old Password  ");
		oldPasswordLabel.setStyleName("stackPanelLabel");

		Label newPasswordLabel = new Label();
		newPasswordLabel.setText("Enter new Password  ");
		newPasswordLabel.setStyleName("stackPanelLabel");

		Label reenterNewPasswordLabel = new Label();
		reenterNewPasswordLabel.setText("Renter new Password  ");
		reenterNewPasswordLabel.setStyleName("stackPanelLabel");

		final PasswordTextBox oldPasswordTextBox = new PasswordTextBox();
		oldPasswordTextBox.setStyleName("stackPanelTextBox");
		oldPasswordTextBox.getElement().setPropertyString("placeholder", "Old Password");

		final PasswordTextBox newPasswordTextBox = new PasswordTextBox();
		newPasswordTextBox.setStyleName("stackPanelTextBox");
		newPasswordTextBox.getElement().setPropertyString("placeholder", "New Password");

		final PasswordTextBox renterNewPasswordTextBox = new PasswordTextBox();
		renterNewPasswordTextBox.setStyleName("stackPanelTextBox");
		renterNewPasswordTextBox.getElement().setPropertyString("placeholder", "Re-enter Password");

		grid = new Grid(3, 2);
		grid.setWidget(0, 0, oldPasswordLabel);
		grid.setWidget(0, 1, oldPasswordTextBox);
		changePasswordPanel.add(grid);

		grid.setWidget(1, 0, newPasswordLabel);
		grid.setWidget(1, 1, newPasswordTextBox);
		changePasswordPanel.add(grid);

		grid.setWidget(2, 0, reenterNewPasswordLabel);
		grid.setWidget(2, 1, renterNewPasswordTextBox);
		changePasswordPanel.add(grid);

		DecoratorPanel decoratorPanel = new DecoratorPanel();
		decoratorPanel.add(grid);
		changePasswordPanel.add(decoratorPanel);

		Button applyButton = new Button("Apply changes");
		applyButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// handle the click event
				if (!newPasswordTextBox.getText().equals(renterNewPasswordTextBox.getText())) {
					Window.alert("Password does not match");
					newPasswordTextBox.setText("");
					newPasswordTextBox.setFocus(true);
					renterNewPasswordTextBox.setText("");

				}
				if (oldPasswordTextBox.getText().isEmpty() || newPasswordTextBox.getText().isEmpty()
						|| renterNewPasswordTextBox.getText().isEmpty()) {
					Window.alert("Fields cannot be left blank");
					oldPasswordTextBox.setText("");
					oldPasswordTextBox.setFocus(true);
					newPasswordTextBox.setText("");
					renterNewPasswordTextBox.setText("");
				}
				if (oldPasswordTextBox.getText().equals(newPasswordTextBox.getText())) {
					Window.alert("New Password cannot be same as the old password ");
					newPasswordTextBox.setText("");
					newPasswordTextBox.setFocus(true);
				}

				try {
					settingService.changePassword(oldPasswordTextBox.getText(), newPasswordTextBox.getText(),
							new AsyncCallback<Boolean>() {

								@Override
								public void onSuccess(Boolean flag) {
									oldPasswordTextBox.setText("");
									newPasswordTextBox.setText("");
									renterNewPasswordTextBox.setText("");
									if (flag == true) {
										Window.alert("Password has been successfully changed");
									} else {
										Window.alert("Password entered is incorrect. Please retry");
									}
								}

								@Override
								public void onFailure(Throwable arg0) {
									Window.alert("Something went wrong! Please retry");
								}
							});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		changePasswordPanel.add(applyButton);
		applyButton.setStyleName("stackPanelLogin");
		return changePasswordPanel;

	}

	// FOR ADDING NEW USER

	private Widget getNewUserPanel() {
		final VerticalPanel newUserPanel = new VerticalPanel();
		newUserPanel.setWidth("450px");

		Grid grid;

		Label newUserName = new Label();
		newUserName.setText("Enter the User Name");
		newUserName.setStyleName("stackPanelLabel");

		Label newUserPassword = new Label();
		newUserPassword.setText("Enter Password");
		newUserPassword.setStyleName("stackPanelLabel");

		final TextBox newUserNameTextBox = new TextBox();
		newUserName.setStyleName("stackPanelTextBox");
		newUserNameTextBox.getElement().setPropertyString("placeholder", "Username");

		final PasswordTextBox newUserPasswordTextBox = new PasswordTextBox();
		newUserPassword.setStyleName("stackPanelTextBox");
		newUserPasswordTextBox.getElement().setPropertyString("placeholder", "New Password");

		grid = new Grid(2, 2);
		grid.setWidget(0, 0, newUserName);
		grid.setWidget(0, 1, newUserNameTextBox);

		newUserPanel.add(grid);

		grid.setWidget(1, 0, newUserPassword);
		grid.setWidget(1, 1, newUserPasswordTextBox);
		DecoratorPanel decoratorPanel = new DecoratorPanel();
		decoratorPanel.add(grid);
		newUserPanel.add(decoratorPanel);

		Button applyButton = new Button("Create");
		applyButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// handle the click event
				if (newUserNameTextBox.getText().isEmpty() || newUserPasswordTextBox.getText().isEmpty()) {
					Window.alert("Fields cannot be left blank ");
					newUserNameTextBox.setText("");
					newUserNameTextBox.setFocus(true);
					newUserPasswordTextBox.setText("");
				}

				try {
					settingService.addUser(newUserNameTextBox.getText(), newUserPasswordTextBox.getText(),
							new AsyncCallback<String>() {

								@Override
								public void onSuccess(String result) {
									newUserNameTextBox.setText("");
									newUserPasswordTextBox.setText("");

									Window.alert(result);
								}

								@Override
								public void onFailure(Throwable arg0) {
									Window.alert("Something went wrong! Please retry");
								}
							});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		newUserPanel.add(applyButton);
		applyButton.setStyleName("stackPanelLogin");

		return newUserPanel;

	}

	// FOR DELETING USER

	private Widget deleteUserPanel() {

		VerticalPanel deleteUserPanel = new VerticalPanel();
		deleteUserPanel.setWidth("450px");

		Grid grid;

		Label deleteUserName = new Label();
		deleteUserName.setText("Enter Username: ");
		deleteUserName.setStyleName("stackPanelLabel");

		Label deleteUserPassword = new Label();
		deleteUserPassword.setText("Enter Password: ");
		deleteUserPassword.setStyleName("stackPasswordText");

		final TextBox deleteUserNameTextBox = new TextBox();
		deleteUserNameTextBox.setStyleName("stackPanelTextBox");
		deleteUserNameTextBox.getElement().setPropertyString("placeholder", "Username");

		final PasswordTextBox deleteUserPasswordTextBox = new PasswordTextBox();
		deleteUserPasswordTextBox.setStyleName("stackPasswordText");
		deleteUserPasswordTextBox.getElement().setPropertyString("placeholder", "Password Required");

		grid = new Grid(2, 2);

		grid.setWidget(0, 0, deleteUserName);
		grid.setWidget(0, 1, deleteUserNameTextBox);
		deleteUserPanel.add(grid);

		grid.setWidget(1, 0, deleteUserPassword);
		grid.setWidget(1, 1, deleteUserPasswordTextBox);
		deleteUserPanel.add(grid);

		DecoratorPanel decoratorPanel = new DecoratorPanel();
		decoratorPanel.add(grid);
		deleteUserPanel.add(decoratorPanel);

		Button applyButton = new Button("Delete User");
		applyButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				// handle the click event
				if (deleteUserNameTextBox.getText().isEmpty() || deleteUserPasswordTextBox.getText().isEmpty()) {
					Window.alert("Fields cannot be left blank ");
					deleteUserNameTextBox.setText("");
					deleteUserNameTextBox.setFocus(true);
					deleteUserPasswordTextBox.setText("");
				}

				try {
					settingService.deleteUser(deleteUserNameTextBox.getText(), deleteUserPasswordTextBox.getText(),
							new AsyncCallback<String>() {

								@Override
								public void onSuccess(String result) {
									deleteUserNameTextBox.setText("");
									deleteUserPasswordTextBox.setText("");

									Window.alert(result);
								}

								@Override
								public void onFailure(Throwable arg0) {
									Window.alert("Something went wrong! Please retry");
								}
							});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		deleteUserPanel.add(applyButton);

		applyButton.setStyleName("stackPanelLogin");

		return deleteUserPanel;

	}
}
