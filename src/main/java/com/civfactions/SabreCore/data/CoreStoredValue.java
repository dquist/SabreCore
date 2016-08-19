package com.civfactions.SabreCore.data;

import com.civfactions.SabreApi.data.StoredValue;
import com.civfactions.SabreApi.util.Guard;

public class CoreStoredValue<T> implements StoredValue<T> {
	
	private final String name;
	private final T defValue;
	private T value;
	private final boolean persist;
	
	public CoreStoredValue(String name, T defValue, boolean persist) {
		Guard.ArgumentNotNullOrEmpty(name, "name");
		
		this.name = name;
		this.defValue = defValue;
		this.value = defValue;
		this.persist = persist;
	}
	
	public CoreStoredValue(String name, T value) {
		this(name, value, false);
	}
	
	public String getName() {
		return this.name;
	}
	
	public T getValue() {
		return this.value;
	}
	
	public T getDefaultValue() {
		return this.defValue;
	}
	
	public void setValue(T value) {
		this.value = value;
	}
	
	public boolean getPersist() {
		return this.persist;
	}

	@Override
	public CoreStoredValue<T> cloneDefault() {
		return new CoreStoredValue<T>(name, defValue);
	}
}
