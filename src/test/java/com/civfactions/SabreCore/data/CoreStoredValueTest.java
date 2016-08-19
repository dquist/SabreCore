package com.civfactions.SabreCore.data;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.commons.lang.NullArgumentException;
import org.junit.Test;

import com.civfactions.SabreApi.chat.ChatPlayer;
import com.civfactions.SabreApi.data.StoredValue;

@SuppressWarnings("unchecked")
public class CoreStoredValueTest {

	@Test
	public void testCoreStoredValue() {
		Throwable e = null;
		try { new CoreStoredValue<String>(null, null); } catch (Throwable ex) { e = ex; }
		assertTrue(e instanceof NullArgumentException);
		
		try { new CoreStoredValue<String>("", null); } catch (Throwable ex) { e = ex; }
		assertTrue(e instanceof RuntimeException);
		
		CoreStoredValue<?> v = new CoreStoredValue<String>("testVar", "value");
		assertNotNull(v);
		assertFalse(v.getPersist());
		
		v = new CoreStoredValue<String>("testVar", "value", false);
		assertNotNull(v);
		assertFalse(v.getPersist());
		
		v = new CoreStoredValue<String>("testVar", "value", true);
		assertNotNull(v);
		assertTrue(v.getPersist());
	}
	
	@Test
	public void testGetSetString() {
		String varName = "testVariable";
		String testString = "testValue";
		CoreStoredValue<?> v1 = new CoreStoredValue<String>(varName, testString);
		assertNotNull(v1);
		assertEquals(v1.getValue(), testString);
	
		((StoredValue<String>)v1).setValue("newValue");
		assertEquals(v1.getValue(), "newValue");
		
		CoreStoredValue<?> v2 = v1.cloneDefault();
		assertNotNull(v2);
		assertNotSame(v1, v2);
		assertEquals(v2.getValue(), testString);
		
		((StoredValue<String>)v2).setValue("newValue");
		assertEquals(v2.getValue(), "newValue");
		
		// Changing value of original does not change value of cloned object
		((StoredValue<String>)v1).setValue("newValue2");
		assertNotSame(v1.getValue(), v2.getValue());
	}
	
	@Test
	public void testGetSetChatPlayer() {
		String varName = "testVariable";
		ChatPlayer chatPlayer = mock(ChatPlayer.class);
		CoreStoredValue<?> v1 = new CoreStoredValue<ChatPlayer>(varName, chatPlayer);
		assertNotNull(v1);
		assertEquals(v1.getValue(), chatPlayer);
	
		ChatPlayer chatPlayer2 = mock(ChatPlayer.class);
		((StoredValue<ChatPlayer>)v1).setValue(chatPlayer2);
		assertEquals(v1.getValue(), chatPlayer2);
		
		CoreStoredValue<?> v2 = v1.cloneDefault();
		assertNotNull(v2);
		assertNotSame(v1, v2);
		assertEquals(v2.getValue(), chatPlayer);
		
		((StoredValue<ChatPlayer>)v2).setValue(chatPlayer2);
		assertEquals(v2.getValue(), chatPlayer2);
		
		// Changing value of original does not change value of cloned object
		((StoredValue<ChatPlayer>)v1).setValue(chatPlayer);
		assertNotSame(v1.getValue(), v2.getValue());
	}
}
