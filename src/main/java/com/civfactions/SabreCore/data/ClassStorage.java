package com.civfactions.SabreCore.data;

import java.util.HashMap;

import com.civfactions.SabreApi.data.StoredValue;
import com.civfactions.SabreApi.util.Guard;

public final class ClassStorage {

	private final HashMap<String, StoredValue<?>> values;
	
	public ClassStorage() {
		this.values = new HashMap<String, StoredValue<?>>();
	}
	
	private ClassStorage(HashMap<String, StoredValue<?>> values) {
		Guard.ArgumentNotNull(values, "values");
		
		this.values = new HashMap<String, StoredValue<?>>();
		
		for (StoredValue<?> v : values.values()) {
			values.put(v.getName(), v.cloneDefault());
		}
	}
	
	public <T> void register(String name, T value, boolean persist) {
		values.put(name, new CoreStoredValue<T>(name, value, persist));
	}
	
	public <T> void register(String name, T value) {
		register(name, value, false);
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
	
	public ClassStorage cloneDefault() {
		return new ClassStorage(values);
	}
}
