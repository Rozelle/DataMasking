package com.datamask.masquerade.client;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class DashboardView 
{
	private static final int REFRESH_INTERVAL = 1000; // ms

	
	public static void setContent()
	{
	    RootPanel rootPanel = RootPanel.get("container");
	
		//This is the top blue bar
		final HorizontalPanel bluePanel = new HorizontalPanel();
		bluePanel.addStyleName("bluePanel");

		//Message on the blue bar
		final Label dashboardMessage = new Label();
		dashboardMessage.addStyleName("dashboardMessage");
		
		bluePanel.add(dashboardMessage);
		
		final Label dashboardTimer = new Label();
		dashboardTimer.addStyleName("dashboardTimer");
				
		bluePanel.add(dashboardTimer);
		
		 // Setup timer to refresh list automatically.
	      Timer refreshTimer = new Timer() 
							      {
							        @Override
							        public void run() 
							        {
							        	 DateTimeFormat dateFormat = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM);
							        	 dashboardTimer.setText(dateFormat.format(new Date()));
							        }
							      };
		refreshTimer.scheduleRepeating(REFRESH_INTERVAL);

		//Menu Panel
		HorizontalPanel menuPanel = new HorizontalPanel();
		menuPanel.addStyleName("menuPanel");
		
		//Configuration
		final HorizontalPanel configurationPanel =new HorizontalPanel();
		configurationPanel.addStyleName("configurationPanel");
		configurationPanel.add(new Image("images/configuration.png"));
		final Label configLable = new Label("Configuration");
		configLable.addStyleName("configLable");
		configurationPanel.add(configLable);

		//Data Masking
		final HorizontalPanel maskingPanel =new HorizontalPanel();
		maskingPanel.addStyleName("maskingPanel");
		maskingPanel.add(new Image("images/masking.png"));
		final Label maskingLable = new Label("Data Masking");
		maskingLable.addStyleName("maskingLable");
		maskingPanel.add(maskingLable);
		
		//Settings
		final HorizontalPanel settingPanel =new HorizontalPanel();
		settingPanel.addStyleName("settingPanel");
		settingPanel.add(new Image("images/settings.png"));
		final Label settingLable = new Label("Setting");
		settingLable.addStyleName("settingLable");
		settingPanel.add(settingLable);
		
		menuPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		menuPanel.add(configurationPanel);
		menuPanel.add(maskingPanel);
		menuPanel.add(settingPanel);
		
		menuPanel.setCellWidth(configurationPanel, "33%");
		menuPanel.setCellWidth(maskingPanel,"33%");
		menuPanel.setCellWidth(settingPanel, "34%");
		
		rootPanel.add(menuPanel);
		rootPanel.add(bluePanel);
		
		final VerticalPanel content = new VerticalPanel();
		rootPanel.add(content);
		
		/** Create a handler for the menuPanel **/
	    class menuPanelHandler implements ClickHandler
	    {
	    	public void onClick(ClickEvent event) 
		    {
	    		Widget sender = (Widget) event.getSource();
	    		
	    		configLable.setStyleName("configLable");
	    		maskingLable.setStyleName("maskingLable");
	    		settingLable.setStyleName("settingLable");
	    		
		    	if(sender==configLable)
		    	{
		    		content.clear();
		    		dashboardMessage.setText("Configuration");
		    		configLable.addStyleName("configLable2");
		    		ContentConfiguration.ContentProvider(content);
		    	}
		    	
		    	if(sender==maskingLable)
		    	{
		    		content.clear();
		    		dashboardMessage.setText("Data Masking");
		    		maskingLable.addStyleName("maskingLable2");
		    	  	ContentDataMasking.ContentProvider(content);
		    	}
		    	
		    	if(sender==settingLable)
		    	{
		    		content.clear();
		    		dashboardMessage.setText("Setting");
		    		settingLable.addStyleName("settingLable2");
		    	  	ContentSetting.ContentProvider(content);
		    	}
		    }
		    
		     
	    }//end of class menuPanelHandler     
		
	    /** Add a handler to send the login/password to the server **/
	    menuPanelHandler handler = new menuPanelHandler();
	    configLable.addClickHandler(handler);
	    maskingLable.addClickHandler(handler);
	    settingLable.addClickHandler(handler);
	    
		
	}
 }