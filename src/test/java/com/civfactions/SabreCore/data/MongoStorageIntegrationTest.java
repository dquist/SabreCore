package com.civfactions.SabreCore.data;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.civfactions.SabreApi.SabreLogger;
import com.civfactions.SabreApi.data.DataCollection;
import com.civfactions.SabreApi.data.DataStorage;
import com.civfactions.SabreApi.data.ListCollection;
import com.civfactions.SabreApi.data.SabreDocument;

public class MongoStorageIntegrationTest {
	
	private DocumentableTestObjectFactory factory = new DocumentableTestObjectFactory();
	
	private SabreDocument doc;
	private DataStorage storage;
	
	@Before
	public void setUp() throws Exception {
		// Connects to 'test' database
		doc = new SabreDocument("name", "test");
		
		
		storage = new MongoStorage(mock(SabreLogger.class)).loadDocument(doc);
		storage.connect();
	}
	
	@After
	public void tearDown() throws Exception {
		storage.disconnect();
	}

	@Test
	public void testAddRead() {
		// Get the collection and drop it to clear all values
		DataCollection<DocumentableTestObject> collection = storage.getDataCollection("testObjects", factory);
		collection.drop();
		
		// Will have an empty collection at this point
		collection = storage.getDataCollection("testObjects", factory);
		Collection<?> read = collection.readAll();
		assertEquals(read.size(), 0);
		
		// Add some objects
		final int numObjects = 50;
		ArrayList<DocumentableTestObject> added = new ArrayList<>();
		
		for (Integer i = 0; i < numObjects; i++) {
			DocumentableTestObject insert = DocumentableTestObject.create("insertItem_" + i.toString());
			insert.setProperpty(i);
			added.add(insert);
			collection.insert(insert);
		}
		
		// Make sure they are all read back
		read = collection.readAll();
		assertEquals(read.size(), numObjects);
		assertTrue(read.containsAll(added));
	}
	
	@Test
	public void testUpdate() {
		// Get the collection and drop it to clear all values
		DataCollection<DocumentableTestObject> collection = storage.getDataCollection("testObjects", factory);
		collection.drop();
		collection = storage.getDataCollection("testObjects", factory);
		
		DocumentableTestObject item = DocumentableTestObject.create("item");
		item.setProperpty(50);
		assertEquals(item.getProperpty(), 50);
		collection.insert(item);
		
		DocumentableTestObject read = collection.readDocument("_id", item.getUniqueId().toString());
		assertEquals(item, read);
		
		// Change it
		collection.updateField(item, "property", 25);
		read = collection.readDocument("_id", item.getUniqueId().toString());
		assertEquals(read.getProperpty(), 25);
		assertNotSame(item, read);
		
		// Change it back
		collection.updateField(item, "property", 50);
		read = collection.readDocument("_id", item.getUniqueId().toString());
		assertEquals(item, read);
	}
	
	@Test
	public void testList() {
		// Get the collection and drop it to clear all values
		DataCollection<DocumentableTestObject> collection = storage.getDataCollection("testList", factory);
		collection.drop();
		
		collection = storage.getDataCollection("testList", factory);
		ListCollection<DocumentableTestObject> list = collection.getCollectionList("list");
		
		DocumentableTestObject baseObject = DocumentableTestObject.create("baseObject");
		collection.insert(baseObject);
		
		// Add some objects to the list
		final int numObjects = 5;
		ArrayList<DocumentableTestObject> added = new ArrayList<>();
		
		for (Integer i = 0; i < numObjects; i++) {
			DocumentableTestObject insert = DocumentableTestObject.create("listItem_" + i.toString());
			insert.setProperpty(i);
			added.add(insert);
			list.add(baseObject, insert);
		}
		
		Collection<?> readList = list.readAll(baseObject, factory);
		assertTrue(readList.containsAll(added));
		
		DocumentableTestObject modify = added.get(0);
		assertEquals(modify.getName(), "listItem_0");
		
		list.update(baseObject, modify, "name", "new_name");
		
		DocumentableTestObject readBack = list.read(baseObject, "_id", modify.getUniqueId(), factory);
		assertEquals(modify.getUniqueId(), readBack.getUniqueId());
		assertEquals(readBack.getName(), "new_name");
	}
}
