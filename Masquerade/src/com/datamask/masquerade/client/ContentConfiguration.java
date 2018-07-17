package com.datamask.masquerade.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.datamask.masquerade.client.ConfigurationService;
import com.datamask.masquerade.client.ConfigurationServiceAsync;

import java.util.ArrayList;


public class ContentConfiguration 
{
	/** Create a remote service proxy to talk to the server-side Greeting service. **/
	private static final ConfigurationServiceAsync greetingService = GWT.create(ConfigurationService.class);
	 
    final static Label TestResultLable = new Label();
    static int dialog;
	
	public static void ContentProvider(final VerticalPanel content)
	{
		/****************************** Database adding Panel ******************************/
		final HorizontalPanel addConnectionPanel = new HorizontalPanel();
		final Button addConnectionButton = new Button("Databse +");
		addConnectionButton.addStyleName("addSchemaButton");
		addConnectionButton.setEnabled(true);
		addConnectionButton.setFocus(true);
		
		addConnectionPanel.add(addConnectionButton);
		
		/****************************** Database connection(s) Table ******************************/
		
		final FlexTable connectionTable = new FlexTable();
		
		connectionTable.setText(0, 0, "Name");
		connectionTable.setText(0, 1, "Edit");
		connectionTable.setText(0, 2, "Remove");
		
		connectionTable.getColumnFormatter().setWidth(0, "300px");
		connectionTable.getColumnFormatter().setWidth(1, "70px");
		connectionTable.getColumnFormatter().setWidth(2, "70px");
		
		connectionTable.setCellPadding(6);
		connectionTable.getRowFormatter().addStyleName(0,"schemaListHeader");
		connectionTable.addStyleName("ListOfSchemas");

		content.add(addConnectionPanel);
		content.add(connectionTable);
				
		getConList(connectionTable);		

		/****************************** Destination adding Panel ******************************/
		final HorizontalPanel addDestinationPanel = new HorizontalPanel();
		
		final Button addDestinationButton = new Button("Destination +");
		addDestinationButton.addStyleName("addSchemaButton");
		addDestinationButton.setEnabled(true);
		addDestinationButton.setFocus(true);
		
		addDestinationPanel.add(addDestinationButton);
		
		/****************************** Destination connection Table ******************************/
		final FlexTable destinationTable = new FlexTable();
	
		destinationTable.setText(0, 0, "Name");
		destinationTable.setText(0, 1, "Edit");
		destinationTable.setText(0, 2, "Remove");
		
		destinationTable.getColumnFormatter().setWidth(0, "300px");
		destinationTable.getColumnFormatter().setWidth(1, "70px");
		destinationTable.getColumnFormatter().setWidth(2, "70px");
		
		destinationTable.setCellPadding(6);
		destinationTable.getRowFormatter().addStyleName(0,"schemaListHeader");
		destinationTable.addStyleName("ListOfSchemas");
		

		content.add(addDestinationPanel);
		content.add(destinationTable);
		
		getDestList(destinationTable);
		
		
		/****************************** Pop-up for New connection/Destination ******************************/
		
		final DialogBox conDialogBox = new DialogBox();
		conDialogBox.setAnimationEnabled(true);
		
		VerticalPanel dialogVPanel = new VerticalPanel();
		
	    final HorizontalPanel connectionNamePanel = new HorizontalPanel();
	    final Label conNameLabel = new Label("Name");
	    conNameLabel.addStyleName("conNameLabel");
		final TextBox conNameBox = new TextBox();
		conNameBox.addStyleName("conNameBox");
    	conNameBox.setReadOnly(false);

		connectionNamePanel.add(conNameLabel);
		connectionNamePanel.add(conNameBox);
			
		
		final HorizontalPanel hostportPanel = new HorizontalPanel();
	    final Label hostLabel = new Label("Host");
	    hostLabel.addStyleName("hostLabel");
		final TextBox hostBox = new TextBox();
		hostBox.addStyleName("hostBox");
	    final Label portLabel = new Label("Port");
	    portLabel.addStyleName("portLabel");
		final TextBox portBox = new TextBox();
		portBox.addStyleName("portBox");
		
		hostportPanel.add(hostLabel);
		hostportPanel.add(hostBox);
		hostportPanel.add(portLabel);
		hostportPanel.add(portBox);
		
		final HorizontalPanel mysqlloginPanel = new HorizontalPanel();
	    final Label mysqlloginLabel = new Label("Login");
	    mysqlloginLabel.addStyleName("mysqlloginLabel");
		final TextBox mysqlloginBox = new TextBox();
		mysqlloginBox.addStyleName("mysqlloginBox");
	    final Label mysqlpassLabel = new Label("Password");
	    mysqlpassLabel.addStyleName("mysqlpassLabel");
		final PasswordTextBox mysqlpassBox = new PasswordTextBox();
		mysqlpassBox.addStyleName("mysqlpassBox");

		mysqlloginPanel.add(mysqlloginLabel);
		mysqlloginPanel.add(mysqlloginBox);
		mysqlloginPanel.add(mysqlpassLabel);
		mysqlloginPanel.add(mysqlpassBox);
		
		final HorizontalPanel testsavePanel = new HorizontalPanel();
		testsavePanel.addStyleName("testsavePanel");
		testsavePanel.setSpacing(10);
		final Button testConButton = new Button("Test");
		testConButton.setStyleName("testConButton");
		
		final Button saveConButton = new Button("Save");
		//saveConButton.setEnabled(false);
		saveConButton.setStyleName("saveConButton");
		testsavePanel.add(testConButton);
		testsavePanel.add(saveConButton);
		
		final HorizontalPanel TestResultPanel = new HorizontalPanel();
	    TestResultPanel.add(TestResultLable);
	    TestResultLable.addStyleName("TestResultLable");
	    
		final Button closeConButton = new Button("Close");
		closeConButton.setStyleName("closeConButton");

		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_LEFT);

		dialogVPanel.add(connectionNamePanel);
		connectionNamePanel.addStyleName("connectionNamePanel");
		dialogVPanel.add(hostportPanel);
		hostportPanel.addStyleName("hostportPanel");
		dialogVPanel.add(mysqlloginPanel);
		mysqlloginPanel.addStyleName("mysqlloginPanel");
		
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);

		dialogVPanel.add(testsavePanel);
		dialogVPanel.add(TestResultPanel);
		TestResultPanel.addStyleName("TestResultPanel");


		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeConButton);
		
		/********** Add all to the dialog box **********/
		conDialogBox.setWidget(dialogVPanel);
		conDialogBox.addStyleName("conDialogBox");
		
			
		/****************************** Click Handler ******************************/
		
	    class ConfigurationClickHandler implements ClickHandler
	    {
	    	public void onClick(ClickEvent event) 
		    {
	    		Widget sender = (Widget) event.getSource();
	    		
	    		/*************** ConnectionButton & DestinationButton ***************/
	    		if(sender == addConnectionButton || sender == addDestinationButton )
		    	{	   			
    	        	conNameBox.setReadOnly(false);
    	        	
    	        	addConnectionButton.setEnabled(false);
		    		addDestinationButton.setEnabled(false);
		    		
		    		saveConButton.setEnabled(false);
		    		testConButton.setEnabled(true);

    	        	
    	        	TestResultLable.setText("");
					conNameBox.setText("");
		    		hostBox.setText("");
		    		portBox.setText("");
		    		mysqlloginBox.setText("");
		    		mysqlpassBox.setText("");

		    		if(sender == addConnectionButton) 
		    		{
		    			dialog = 1;
		    			conDialogBox.setText("New Connection");
		    		}
		    		
		    		else
		    		{
		    			dialog = 2;
		    			conDialogBox.setText("New Destination");
		    			
		    			if(destinationTable.getRowCount()==2)
		    			{
		    				Window.alert("There can be only one destination!");
		    				
		    				addConnectionButton.setEnabled(true);
				    		addDestinationButton.setEnabled(true);
				    		
		    				return;
		    			}
		    		}		    		
					
					conDialogBox.center();
		    		conDialogBox.show();		    		
		    	}
	    		
	    		/*************** connectionTable &  destinationTable ***************/
	    		
	    	    if(sender == connectionTable || sender == destinationTable)
	    		{
	    	    	FlexTable thisTable = (FlexTable) sender; 
	    	    	
	    	    	if(thisTable == connectionTable) dialog = 1;
	    	    	else							 dialog = 2;
	    	    	
	    	    	final int cellIndex = thisTable.getCellForEvent(event).getCellIndex();
	    	        final int rowIndex  = thisTable.getCellForEvent(event).getRowIndex();
	    	    	        
	    	        	    	        
	    	        /*************** Remove Icon ***************/ 
	    	        
	    	        if(cellIndex==2 && rowIndex!=0)
	    	        {
			    		
	    	        	String removeName = thisTable.getFlexCellFormatter().getElement(rowIndex, 0).getInnerText();
	    	        	
	    	        	if(sender == connectionTable) removeCon(thisTable,removeName,rowIndex);
	    	        	else 						  removeDest(thisTable,removeName,rowIndex);
	    	        	        	    	        	
	    	        	addConnectionButton.setEnabled(true);
			    		addDestinationButton.setEnabled(true);
	    	        }	
	    	        
	    	        
	    	        /*************** Edit Icon ***************/ 
	    	        
	    	        if(cellIndex==1 && rowIndex!=0)
	    	        {
	    	        	conNameBox.setReadOnly(true);
	    	        	
	    	        	addConnectionButton.setEnabled(false);
			    		addDestinationButton.setEnabled(false);
			    		
			    		saveConButton.setEnabled(false);  		
			    		testConButton.setEnabled(true);
			    		
	    	        	conDialogBox.setText("Edit Connection");
	    	        	
				    	TestResultLable.setText("");
			    		
				    	if(sender == connectionTable)
			    		{
			    		   greetingService.getConList(
					  				new AsyncCallback<ArrayList<ArrayList<String>>>() 
					  				{
					  					public void onFailure(Throwable caught) {
					  					}
					  	
					  					public void onSuccess(ArrayList<ArrayList<String>> result) 
					  					{ 
					  						//ipaddress, port, schemaname, username, password
					  						if(result!=null)
					  						{
					  							ArrayList <String> al = result.get(rowIndex-1);
					  							conNameBox.setText(al.get(2));
					  				    		hostBox.setText(al.get(0));
					  							portBox.setText(al.get(1));
					  				    		mysqlloginBox.setText(al.get(3));
					  				    		mysqlpassBox.setText(al.get(4));
					  				    		
					  				    		conDialogBox.center();
					  				    		conDialogBox.show();
					  						}
					  					}
					  				});	
			    		}
				    	
			    		else
			    		{			    			
			    			greetingService.getDestList(
					  				new AsyncCallback<ArrayList<String>>() 
					  				{
					  					public void onFailure(Throwable caught) {
					  					}
					  	
					  					public void onSuccess(ArrayList<String> result) 
					  					{ 
					  						//ipaddress, port, schemaname, username, password
					  						if(result!=null)
					  						{					  							
					  							conNameBox.setText(result.get(2));
					  				    		hostBox.setText(result.get(0));
					  							portBox.setText(result.get(1));
					  				    		mysqlloginBox.setText(result.get(3));
					  				    		mysqlpassBox.setText(result.get(4));
					  				    		
					  				    		conDialogBox.center();
					  				    		conDialogBox.show();
					  						}
					  					}
					  				});	
			    		}
				    	
			    		testConButton.setEnabled(true);

	    	        }
	    	        				    		
	    		}
	    		
	    		/*************** Close Button ***************/

	    		if(sender==closeConButton)
		    	{
    				addConnectionButton.setEnabled(true);
		    		addDestinationButton.setEnabled(true);
		    		
	    			conDialogBox.hide();
		    		
					TestResultLable.setText("");
					conNameBox.setText("");
		    		hostBox.setText("");
		    		portBox.setText("");
		    		mysqlloginBox.setText("");
		    		mysqlpassBox.setText("");
		    	}

	    		/*************** Test Button ***************/

		    	if(sender == testConButton)
		    	{
            		saveConButton.setEnabled(false); 

		    		String Con[] = new String[5];
		    		
		    		Con[2]=conNameBox.getText();
		    		Con[0]=hostBox.getText();
		    		Con[1]=portBox.getText();
		    		Con[3]=mysqlloginBox.getText();
		    		Con[4]=mysqlpassBox.getText();
		    		
		    		for(int i=0; i<5;i++)
		    			if(Con[i].isEmpty())
		    			{
				    	  TestResultLable.setText("Enter all the Details!");
				    	  return;
		    			}
		    		
		    		if(dialog==1) //Test of New Connection
	    	    	{
			    		testNewCon(Con);
			    	}
	    	    
	    	    	else		 //Test of New Destination
	    	    	{
	    	    		testNewDest(Con);
	    	    	} 
		    		    		
		    		Timer timer = new Timer()
	    	        {
	    	            @Override
	    	            public void run()
	    	            { 
	    	            	if(TestResultLable.getText().equals("Connected Successfully"))
	    	            		saveConButton.setEnabled(true); 
	    	            }
	    	        };
	    	        
	    	    	timer.schedule(1000);
		    		testConButton.setEnabled(true);

		    	}

	    		/*************** Save Button ***************/

		    	if(sender == saveConButton)
		    	{
		    		Timer timer = new Timer()
	    	        {
	    	            @Override
	    	            public void run()
	    	            {
				    		conDialogBox.hide(true);
				    		
				    		getConList(connectionTable);
				    		getDestList(destinationTable);
				    		
				    		addConnectionButton.setEnabled(true);				    		   
				    		addDestinationButton.setEnabled(true);
	    	            }
	    	        };

		    		String newCon[] = new String[5];
		    		newCon[2]=conNameBox.getText();
		    		newCon[0]=hostBox.getText();
		    		newCon[1]=portBox.getText();
		    		newCon[3]=mysqlloginBox.getText();
		    		newCon[4]=mysqlpassBox.getText();
		    		
		    		for(int i=0; i<5;i++)
		    			if(newCon[i].isEmpty())
		    			{
				    	  TestResultLable.setText("Enter all the details!");
				    	  return;
		    			}
		    		    			
		    	    /************ Save Button for new Connection/Destination ********/
	
		    	    if(!conNameBox.isReadOnly() && TestResultLable.getText().equals("Connected Successfully"))
		    		{	    	   		
		    	    	
		    	    	if(dialog==1) //New Connection
		    	    	{
		    	    		addNewCon(newCon);		    	    		
				    	}
		    	    
		    	    	else		 //New Destination
		    	    	{
		    	    		addDestCon(newCon);
		    	    	} 
		    	    	
		    	    	timer.schedule(1000);
		    		}
		    	    
		    	    /************ Save Button for edit Connection/Destination ********/
	    			
		    	    else if(conNameBox.isReadOnly() && TestResultLable.getText().equals("Connected Successfully"))
		    	    {
		    	    	
		    	    	if(dialog == 1) //New Connection
		    	    	{
		    	    		updateCon(newCon);				    		
		    	    	}
		    	    
		    	    	else		 //New Destination
		    	    	{
		    	    		updateDest(newCon);
		    	        }	
		    	    	
		    	    	timer.schedule(1000);
		    	    }
		    	    	
		        }//End of SaveButton handler
		    	
		    }// End Of Click Handler
    		
	    }//end of class ConfigurationHandler     
		
		/*************** handler handler ***************/
	    ConfigurationClickHandler handler = new ConfigurationClickHandler();
	    
	    addConnectionButton.addClickHandler(handler);
	    addDestinationButton.addClickHandler(handler);	
	    
	    connectionTable.addClickHandler(handler);	
	    destinationTable.addClickHandler(handler);		

	    testConButton.addClickHandler(handler);	       
	    saveConButton.addClickHandler(handler);	
	    closeConButton.addClickHandler(handler);
	}
	
	/*********************************** Methods ***********************************/
	
	private static void getConList(final FlexTable connectionTable) {
		greetingService.getConList(
  				new AsyncCallback<ArrayList<ArrayList<String>>>() 
  				{
  					public void onFailure(Throwable caught) { }
  	
  					public void onSuccess(ArrayList<ArrayList<String>> result) 
  					{ 
  						//ipaddress, port, schemaname, username, password
  						if(result!=null)
  						{
  							  							
  							int i=1;
  	  						for(ArrayList<String> row : result)
  	  						{
  	  							connectionTable.setText(i, 0, row.get(2));
  	  							connectionTable.setWidget(i, 1, new Image("images/edit2.png"));
  	  							connectionTable.setWidget(i, 2, new Image("images/delete2.png"));
								i++;
  							}
  						}
  					}

  				});
	}
	
	private static void getDestList(final FlexTable destinationTable)	{
		greetingService.getDestList(
  				new AsyncCallback<ArrayList<String>>() 
  				{
  					public void onFailure(Throwable caught) { }
  	
  					public void onSuccess(ArrayList<String> result) 
  					{ 
  						//ipaddress, port, schemaname, username, password
  						if(result!=null)
  						{
  							
  							
  							int i=1;
  	  						destinationTable.setText(i, 0, result.get(2));
  	  						destinationTable.setWidget(i, 1, new Image("images/edit2.png"));
  	  						destinationTable.setWidget(i, 2, new Image("images/delete2.png"));
							
  						}
  					}

  				});
	}

	private static void removeCon(final FlexTable connectionTable, String removeName, final int rowIndex) {

		greetingService.removeCon(removeName,
  				new AsyncCallback<Boolean>() 
  				{
  					public void onFailure(Throwable caught) { }
  	
  					public void onSuccess(Boolean flag) 
  					{ 	  							
  						if(flag)
  		    	           connectionTable.removeRow(rowIndex);
  					}

  				});		
	}
	
	private static void removeDest(final FlexTable destinationTable, String removeName, final int rowIndex) {

		greetingService.removeDest(removeName,
  				new AsyncCallback<Boolean>() 
  				{
  					public void onFailure(Throwable caught) { }
  	
  					public void onSuccess(Boolean flag) 
  					{ 	  							
  						if(flag)
  							destinationTable.removeRow(rowIndex);
  					}

  				});		
	}

	private static void testNewCon(String[] Con) 
	{
		greetingService.testNewCon(Con,
				new AsyncCallback<String>() 
  				{
  					public void onFailure(Throwable caught) { 
  						TestResultLable.setText(caught.toString());
  					}
  	
  					public void onSuccess(String result) 
  					{ 
  						//ipaddress, port, schemaname, username, password
  						if(result.equals("successful"))
  	  						TestResultLable.setText("Connected Successfully");
  						else
  	  						TestResultLable.setText(result.toString());
  					}

  				});
	}
	
	private static void testNewDest(String[] Con) 
	{
		greetingService.testNewDest(Con,
				new AsyncCallback<String>() 
  				{
  					public void onFailure(Throwable caught) { 
  						TestResultLable.setText(caught.toString());
  					}
  	
  					public void onSuccess(String result) 
  					{ 
  						//ipaddress, port, schemaname, username, password
  						if(result.equals("successful"))
  	  						TestResultLable.setText("Connected Successfully");
  						else
  	  						TestResultLable.setText(result.toString());
  					}

  				});
	}

	private static void addNewCon(String[] newCon) {

	    TestResultLable.setText(""); 

		greetingService.addNewCon(newCon,
  				new AsyncCallback<String>() 
  				{
  					public void onFailure(Throwable caught) { }
  	
  					public void onSuccess(String result) 
  					{ 
  						TestResultLable.setText(result); 
  					}
  				});
	}
	
	private static void addDestCon(String[] newCon) {
		
		greetingService.addDestCon(newCon,
  				new AsyncCallback<String>() 
  				{
  					public void onFailure(Throwable caught) { }
  	
  					public void onSuccess(String result) 
  					{ 
  						TestResultLable.setText(result); 
  					}
  				});				
	}

	
	private static void updateCon(String[] newCon) {
		
		greetingService.updateCon(newCon,
  				new AsyncCallback<String>() 
  				{
  					public void onFailure(Throwable caught) { }
  	
  					public void onSuccess(String result) 
  					{ 
  						TestResultLable.setText(result); 
  					}
  				});	
		
	}	
	private static void updateDest(String[] newCon) {
		
		greetingService.updateDest(newCon,
  				new AsyncCallback<String>() 
  				{
  					public void onFailure(Throwable caught) { }
  	
  					public void onSuccess(String result) 
  					{ 
  						TestResultLable.setText(result); 
  					}
  				});	
	}  

}
