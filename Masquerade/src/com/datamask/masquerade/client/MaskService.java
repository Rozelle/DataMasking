package com.datamask.masquerade.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.datamask.masquerade.shared.MaskingDetails;
import com.datamask.masquerade.shared.SchemaConnection;
import com.datamask.masquerade.shared.TableDescription;

/**
 * The client-side stub for the RPC service.
 */

@RemoteServiceRelativePath("maskservice")
public interface MaskService extends RemoteService {
	ArrayList<SchemaConnection> getSchemaConnection() throws Exception;

	ArrayList<TableDescription> getTables(SchemaConnection schemaConnection) throws Exception;

	boolean maskSchema(MaskingDetails[] maskingDetailsArray)throws Exception;
}
