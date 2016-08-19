package com.civfactions.SabreCore.data;

import java.util.HashMap;

public final class StoredValueMap {

	private final HashMap<String, StoredValue<?>> values;
	
	public StoredValueMap() {
		this.values = new HashMap<String, StoredValue<?>>();
	}
	
	public <T> void register(String name, T value) {
		values.put(name, new StoredValue<T>(name, value));
	}
	
	public void unregisterValue(String name) {
		values.remove(name);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getDataValue(String key) {
		return (T)values.get(key).getValue();
		
	}
	
	@SuppressWarnings("unchecked")
	public <T> void setDataValue(String key, T value) {
		((StoredValue<T>)values.get(key)).setValue(value);
	}
}
