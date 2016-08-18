package com.civfactions.SabreCore.data;

import java.util.HashMap;

import com.civfactions.SabreApi.data.StoredValue;

public final class ClassStorage {

	private final HashMap<String, StoredValue<?>> values;
	
	public ClassStorage() {
		this.values = new HashMap<String, StoredValue<?>>();
	}
	
	private ClassStorage(HashMap<String, StoredValue<?>> values) {
		this.values = new HashMap<String, StoredValue<?>>(values);
	}
	
	public void register(StoredValue<?> value) {
		values.put(value.getName(), value);
	}
	
	public void unregisterValue(String name) {
		values.remove(name);
	}
	
	public void unregisterValue(StoredValue<?> value) {
		unregisterValue(value.getName());
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getDataValue(String key) {
		return (T)values.get(key).getValue();
		
	}
	
	@SuppressWarnings("unchecked")
	public <T> void setDataValue(String key, T value) {
		((StoredValue<T>)values.get(key)).setValue(value);
	}
	
	@Override
	public ClassStorage clone() {
		return new ClassStorage(values);
	}
	
	
}
