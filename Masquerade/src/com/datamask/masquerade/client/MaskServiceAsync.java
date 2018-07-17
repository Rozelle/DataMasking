package com.datamask.masquerade.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.datamask.masquerade.shared.MaskingDetails;
import com.datamask.masquerade.shared.SchemaConnection;
import com.datamask.masquerade.shared.TableDescription;

/**
 * The async counterpart of <code>MaskService</code>.
 */
public interface MaskServiceAsync {
	void getTables(SchemaConnection schemaConnection, AsyncCallback<ArrayList<TableDescription>> callback)
			throws Exception;

	void getSchemaConnection(AsyncCallback<ArrayList<SchemaConnection>> callback) throws Exception;

	void maskSchema(MaskingDetails[] maskingDetailsArray, AsyncCallback<Boolean> asyncCallback) throws Exception;
}
