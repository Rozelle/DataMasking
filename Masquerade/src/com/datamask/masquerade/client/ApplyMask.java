package com.datamask.masquerade.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TreeItem;

import java.util.ArrayList;

import com.datamask.masquerade.shared.MaskingDetails;
import com.datamask.masquerade.shared.SchemaConnection;

public class ApplyMask implements ClickHandler {
	TreeItem schema;
	SchemaConnection schemaConnection;
	boolean flag = false;
	private static final MaskServiceAsync maskService = GWT.create(MaskService.class);
	MaskingDetails maskingDetailsArray[];

	public ApplyMask(TreeItem schema, SchemaConnection schemaConnection) {
		this.schema = schema;
		this.schemaConnection = schemaConnection;
	}

	@Override
	public void onClick(ClickEvent event) {
		// Get all subtrees but not the apply button
		maskingDetailsArray = new MaskingDetails[schema.getChildCount()];
		for (int i = 0; i < schema.getChildCount() - 1; i++) {
			TreeItem table = schema.getChild(i);
			Grid grid = (Grid) table.getChild(0).getWidget();
			ArrayList<ArrayList<String>> maskingTableColumns = new ArrayList<ArrayList<String>>();
			// Get each column name and rule selected
			for (int j = 0; j < grid.getRowCount(); j++) {
				ArrayList<String> columnDetails = new ArrayList<String>();
				
				Label columnNameLabel = (Label) grid.getWidget(j, 0);
				String columnName = columnNameLabel.getText();
				columnDetails.add(columnName);
				
				Label dataTypeLabel = (Label) grid.getWidget(j, 1);
				String dataType = dataTypeLabel.getText();
				columnDetails.add(dataType);
				
				ListBox listBox = (ListBox) grid.getWidget(j, 2);
				String rule = listBox.getSelectedValue();
				columnDetails.add(rule);
				
				maskingTableColumns.add(columnDetails);
			}//inner for loop ends
			maskingDetailsArray[i] = new MaskingDetails(schemaConnection, table.getText(), maskingTableColumns);
		}//outer for loop ends
		try {
			maskService.maskSchema(maskingDetailsArray, new AsyncCallback<Boolean>() {
						@Override
						public void onFailure(Throwable caught) {
							Window.alert(caught.getCause().toString());
						}

						@Override
						public void onSuccess(Boolean result) { 
							flag = result;
						}
					});
		} catch (Exception e) {}
		Timer timer = new Timer() {
			
			@Override
			public void run() {
				Button button = (Button) schema.getChild(schema.getChildCount() - 1).getWidget();
				button.setEnabled(true);
				Window.alert("Successful");
			}
		};
		timer.schedule(2000);
	}
}
