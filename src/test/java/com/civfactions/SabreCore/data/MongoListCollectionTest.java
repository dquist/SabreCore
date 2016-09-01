package com.civfactions.SabreCore.data;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

import java.util.UUID;

import org.apache.commons.lang.NullArgumentException;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.civfactions.SabreApi.SabreLogger;
import com.mongodb.client.MongoCollection;
import org.bson.conversions.Bson;

@SuppressWarnings("unchecked")
public class MongoListCollectionTest {
	
	private MongoCollection<Document> collection;
	private String listName = "list";
	private MongoListCollection<DocumentableTestObject> list;
	private DocumentableTestObject root = new DocumentableTestObject(UUID.randomUUID(), "root");
	private SabreLogger logger;

	@Before
	public void setUp() throws Exception {
		collection = (MongoCollection<Document>)mock(MongoCollection.class);
		logger = mock(SabreLogger.class);
		list = new MongoListCollection<DocumentableTestObject>(collection, listName, logger);
	}

	@Test
	public void testMongoListCollection() {
		Throwable e = null;
		try { new MongoListCollection<DocumentableTestObject>(null, listName, logger); } catch (Throwable ex) { e = ex; }
		assertTrue(e instanceof NullArgumentException);
		
		try { new MongoListCollection<DocumentableTestObject>(collection, null, logger); } catch (Throwable ex) { e = ex; }
		assertTrue(e instanceof NullArgumentException);
		
		try { new MongoListCollection<DocumentableTestObject>(collection, listName, null); } catch (Throwable ex) { e = ex; }
		assertTrue(e instanceof NullArgumentException);
		
		try { new MongoListCollection<DocumentableTestObject>(collection, "", logger); } catch (Throwable ex) { e = ex; }
		assertTrue(e instanceof RuntimeException);
	}
	
	@Test
	public void testAdd() {
		ArgumentCaptor<Bson> filterArg = ArgumentCaptor.forClass(Bson.class);
		ArgumentCaptor<Bson> updateArg = ArgumentCaptor.forClass(Bson.class);
		
		DocumentableTestObject toAdd = new DocumentableTestObject(UUID.randomUUID(), "item");
		toAdd.setProperpty(10);
		
		list.add(root, toAdd);
		verify(collection).updateOne(filterArg.capture(), updateArg.capture());
		
		System.out.println("Filter Bson:");
		System.out.println(filterArg.getValue().toString());
		System.out.println("Update Bson:");
		System.out.println(updateArg.getValue().toString());
	}
	
	@Test
	public void testRemove() {
		
	}
	
	@Test
	public void testUpdate() {
		
	}

}
