package com.civfactions.SabreCore.data;

import com.civfactions.SabreApi.util.Guard;

public class StoredValue<T> {
	
	private final String name;
	private T value;
	
	public StoredValue(String name, T value) {
		Guard.ArgumentNotNullOrEmpty(name, "name");
		
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return this.name;
	}
	
	public T getValue() {
		return this.value;
	}
	
	public void setValue(T value) {
		this.value = value;
	}
}
