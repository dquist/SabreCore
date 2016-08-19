package com.civfactions.SabreCore.data;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.commons.lang.NullArgumentException;
import org.junit.Test;

import com.civfactions.SabreApi.chat.ChatPlayer;

@SuppressWarnings("unchecked")
public class StoredValueTest {

	@Test
	public void testCoreStoredValue() {
		Throwable e = null;
		try { new StoredValue<String>(null, null); } catch (Throwable ex) { e = ex; }
		assertTrue(e instanceof NullArgumentException);
		
		try { new StoredValue<String>("", null); } catch (Throwable ex) { e = ex; }
		assertTrue(e instanceof RuntimeException);
		
		StoredValue<?> v = new StoredValue<String>("testVar", "value");
		assertNotNull(v);
	}
	
	@Test
	public void testGetSetString() {
		String varName = "testVariable";
		String testString = "testValue";
		StoredValue<?> v1 = new StoredValue<String>(varName, testString);
		assertNotNull(v1);
		assertEquals(v1.getValue(), testString);
	
		((StoredValue<String>)v1).setValue("newValue");
		assertEquals(v1.getValue(), "newValue");
	}
	
	@Test
	public void testGetSetChatPlayer() {
		String varName = "testVariable";
		ChatPlayer chatPlayer = mock(ChatPlayer.class);
		StoredValue<?> v1 = new StoredValue<ChatPlayer>(varName, chatPlayer);
		assertNotNull(v1);
		assertEquals(v1.getValue(), chatPlayer);
	
		ChatPlayer chatPlayer2 = mock(ChatPlayer.class);
		((StoredValue<ChatPlayer>)v1).setValue(chatPlayer2);
		assertEquals(v1.getValue(), chatPlayer2);
	}
}
