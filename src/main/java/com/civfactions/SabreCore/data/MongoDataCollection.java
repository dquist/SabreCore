package com.civfactions.SabreCore.data;

import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Level;

import org.bson.Document;

import com.civfactions.SabreApi.SabreLogger;
import com.civfactions.SabreApi.data.DataCollection;
import com.civfactions.SabreApi.data.Documentable;
import com.civfactions.SabreApi.data.SabreDocument;
import com.civfactions.SabreApi.data.SabreObjectFactory;
import com.civfactions.SabreApi.util.Guard;
import com.mongodb.client.MongoCollection;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

/**
 * Wrapper class for a MongoDB collection
 * @author Gordon
 *
 * @param <T> The object type
 */
class MongoDataCollection<T extends Documentable> implements DataCollection<T> {
	
	private final MongoCollection<Document> collection;
	private final SabreObjectFactory<T> factory;
	private final SabreLogger logger;
	
	/**
	 * Creates a new MongoDataCollection instance
	 * @param collection The document collection
	 * @param factory The factory for creating new object instances
	 * @param logger The logger instance
	 */
	MongoDataCollection(final MongoCollection<Document> collection, final SabreObjectFactory<T> factory, SabreLogger logger) {
		Guard.ArgumentNotNull(collection, "collection");
		Guard.ArgumentNotNull(factory, "factory");
		Guard.ArgumentNotNull(logger, "logger");
		
		this.collection = collection;
		this.factory = factory;
		this.logger = logger;
	}

	@Override
	public Collection<T> readAll() {
		HashSet<T> values = new HashSet<T>();
		
		for(Document d : collection.find()) {
			try {
				values.add(factory.createInstance(new SabreDocument(d)));
			} catch (Exception ex) {
				logger.log(Level.WARNING, "Failed to read collection record %s", d.toJson());
				ex.printStackTrace();
			}
		}
		
		return values;
	}

	@Override
	public T readDocument(String key, Object value) {
		Document first = collection.find(eq(key, value)).first();
		if (first == null) {
			return null;
		}
		return factory.createInstance(new SabreDocument(first));
	}

	@Override
	public void insert(final T doc) {		
		collection.insertOne(new Document(doc.getDocument()));
		
	}

	@Override
	public void updateField(final T doc, final String key, final Object value) {
		collection.updateOne(eq("_id", doc.getDocumentKey()), set(key, value));
	}

	@Override
	public void remove(final T doc) {
		collection.deleteOne(eq("_id", doc.getDocumentKey()));
	}

	@Override
	public void addListItem(T doc, String key, Object value) {
		collection.updateOne(eq("_id", doc.getDocumentKey()), push(key, value));
		
	}

	@Override
	public void removeListItem(T doc, String key, Object value) {
		collection.updateOne(eq("_id", doc.getDocumentKey()), pull(key, value));
	}
}
