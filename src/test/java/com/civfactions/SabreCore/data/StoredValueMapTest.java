package com.civfactions.SabreCore.data;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.civfactions.SabreApi.SabrePlayer;

public class StoredValueMapTest {

	private StoredValueMap storage;
	
	@Before
	public void setUp() throws Exception {
		storage = new StoredValueMap();
		assertNotNull(storage);
	}

	
	@Test
	public void test() {
		String varName1 = "intVar";
		String varName2 = "stringVar";
		String varName3 = "playerVar";
		String varName4 = "listVar";
		
		Integer intValue = 55;
		storage.register(varName1, intValue);
		assertEquals(storage.getDataValue(varName1), (Integer)intValue);
		
		String strValue = "test value";
		storage.register(varName2, strValue);
		assertEquals(storage.getDataValue(varName2), strValue);
		assertNotSame(storage.getDataValue(varName2), intValue);
		
		SabrePlayer player = mock(SabrePlayer.class);
		storage.register(varName3, player);
		assertEquals(storage.getDataValue(varName3), player);
		
		ArrayList<String> listVar = new ArrayList<String>();
		listVar.add("String1");
		listVar.add("String2");
		storage.register(varName4, listVar);
		assertEquals(storage.getDataValue(varName4), listVar);
	}
}
