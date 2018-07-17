package com.datamask.masquerade.client;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.datamask.masquerade.client.ApplyMask;
import com.datamask.masquerade.client.MaskService;
import com.datamask.masquerade.client.MaskServiceAsync;
import com.datamask.masquerade.shared.SchemaConnection;
import com.datamask.masquerade.shared.TableDescription;

public class ContentDataMasking {

	static Tree tree;

	/**
	 * Create a remote service proxy to talk to the server-side MySQLConnection
	 * service.
	 **/
	private static final MaskServiceAsync maskService = GWT.create(MaskService.class);

	public static void ContentProvider(VerticalPanel content) {
		// create the tree
		tree = new Tree();

		try {
			maskService.getSchemaConnection(new AsyncCallback<ArrayList<SchemaConnection>>() {
				@Override
				public void onFailure(Throwable caught) {
				}

				@Override
				public void onSuccess(ArrayList<SchemaConnection> result) {
					try {
						createTree(result);

					} catch (Exception e) {
					}
				}
			});

		} catch (Exception e) {
		}
		tree.setAnimationEnabled(true);
		// Add text boxes to the root panel.
		VerticalPanel panel = new VerticalPanel();
		panel.add(tree);

		// add the tree to the root panel
		content.add(panel);
	}

	protected static void createTree(ArrayList<SchemaConnection> listOfSchemaConnections) throws Exception {
		for (final SchemaConnection schemaConnection : listOfSchemaConnections) {
			final TreeItem schema;
			schema = new TreeItem();
			schema.setText(schemaConnection.getSchemaName());
			maskService.getTables(schemaConnection, new AsyncCallback<ArrayList<TableDescription>>() {

				@Override
				public void onFailure(Throwable caught) {
				}

				@Override
				public void onSuccess(ArrayList<TableDescription> result) {
					createSubtrees(schema, result);
					addApplyButton(schema, schemaConnection);
				}
			});

			// add root item to the tree
			tree.addItem(schema);
		}
	}

	protected static void addApplyButton(TreeItem schema, SchemaConnection schemaConnection) {
		Button applyButton = new Button();
		applyButton.setText("Apply Mask");
		applyButton.setWidth("500px");
		applyButton.addClickHandler(new ApplyMask(schema, schemaConnection));
		schema.addItem(applyButton);
	}

	protected static void createSubtrees(TreeItem schema, ArrayList<TableDescription> tableDescriptionList) {
		for (TableDescription tableDescription : tableDescriptionList) {
			TreeItem table = new TreeItem();
			table.setText(tableDescription.getTableName());
			Grid columnGrid = new Grid(tableDescription.getColumns().size(), 3);
			columnGrid.setCellSpacing(15);
			int columnCount = 0;
			for (String[] columnDescription : tableDescription.getColumns()) {
				Label columnName = new Label();
				columnName.setText(columnDescription[0]);
				columnGrid.setWidget(columnCount, 0, columnName);

				Label columnType = new Label();
				columnType.setText(columnDescription[1]);
				columnGrid.setWidget(columnCount, 1, columnType);

				ListBox ruleListBox = new ListBox();
				ruleListBox.setWidth("300px");
				for (String rule : tableDescription.getRuleSuggestionList().get(columnCount)) {
					ruleListBox.addItem(rule);
				}
				ruleListBox.setVisibleItemCount(1);
				ruleListBox.setStyleName("treeListBox");
				columnGrid.setWidget(columnCount, 2, ruleListBox);
				columnCount++;
			}
			table.addItem(columnGrid);
			schema.addItem(table);
		}
	}
}
