package com.civfactions.SabreCore;

import com.civfactions.SabreApi.data.DataAccess;
import com.civfactions.SabreApi.data.DataCollection;
import com.civfactions.SabreApi.data.Documentable;
import com.civfactions.SabreApi.data.SabreObjectFactory;

public interface DataStorage extends DataAccess, Documentable {
	
	boolean connect();
	void disconnect();
	boolean isConnected();
	<T extends Documentable> DataCollection<T> getDataCollection(String name, SabreObjectFactory<T> factory);
}
