package com.civfactions.SabreCore.data;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;

import org.bson.Document;

import com.civfactions.SabreApi.SabreLogger;
import com.civfactions.SabreApi.data.Documentable;
import com.civfactions.SabreApi.data.ListCollection;
import com.civfactions.SabreApi.data.SabreDocument;
import com.civfactions.SabreApi.data.SabreObjectFactory;
import com.civfactions.SabreApi.util.Guard;
import com.mongodb.client.MongoCollection;

class MongoListCollection<T extends Documentable> implements ListCollection<T> {

	private final MongoCollection<Document> base;
	private final String listName;
	private final SabreLogger logger;
	
	/**
	 * Creates a new MongoListCollection instance
	 * @param base The base collection
	 * @param listName The name of the list
	 */
	MongoListCollection(final MongoCollection<Document> base, final String listName, final SabreLogger logger) {
		Guard.ArgumentNotNull(base, "base");
		Guard.ArgumentNotNullOrEmpty(listName, "listName");
		Guard.ArgumentNotNull(logger, "logger");
		
		this.base = base;
		this.listName = listName;
		this.logger = logger;
	}

	@Override
	public Collection<T> readAll(Documentable doc, SabreObjectFactory<T> factory) {
		HashSet<T> values = new HashSet<>();
		
		Document d = base.find(eq("_id", doc.getDocumentKey())).first();
		if (d != null) {
			Object o = d.get(listName);
			if (o instanceof List<?>) {
				@SuppressWarnings("unchecked")
				List<Document> list = (List<Document>)o;
				for(Document l : list) {
					try {
						values.add(factory.createInstance(new SabreDocument(l)));
					} catch (Exception ex) {
						logger.log(Level.WARNING, "Failed to read collection record %s", d.toJson());
						ex.printStackTrace();
					}
				}
			}
		}
		
		return values;
	}

	@Override
	public T read(Documentable doc, String key, Object value, SabreObjectFactory<T> factory) {
		Document d = base.find(eq("_id", doc.getDocumentKey())).first();
		if (d != null) {
			Object o = d.get(listName);
			if (o instanceof List<?>) {
				@SuppressWarnings("unchecked")
				List<Document> list = (List<Document>)o;
				for(Document l : list) {					
					try {
						return factory.createInstance(new SabreDocument(l));
					} catch (Exception ex) {
						logger.log(Level.WARNING, "Failed to read collection record %s", d.toJson());
						return null;
					}
				}
			}
		}
		return null;
	}

	@Override
	public void add(final Documentable doc, final T item) {
		base.updateOne(eq("_id", doc.getDocumentKey()), push(listName, new Document(item.getDocument())));
	}

	@Override
	public void remove(final Documentable doc, final T item) {
		base.updateOne(eq("_id", doc.getDocumentKey()), pull(listName, eq("_id", item.getDocumentKey())));
	}

	@Override
	public void update(Documentable doc, T item, String key, Object value) {
		base.updateOne(and(eq("_id", doc.getDocumentKey()), eq(listName + "._id", item.getDocumentKey())), 
				set(listName + ".$." + key, value));
	}
}