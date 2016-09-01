package com.civfactions.SabreCore.data;

import java.util.UUID;

import com.civfactions.SabreApi.data.SabreDocument;
import com.civfactions.SabreApi.data.SabreObjectFactory;

public class DocumentableTestObjectFactory implements SabreObjectFactory<DocumentableTestObject> {

	@Override
	public DocumentableTestObject createInstance(SabreDocument doc) {
		UUID uid = UUID.fromString(doc.getString("_id"));
		String name = doc.getString("name");
		
		return new DocumentableTestObject(uid, name).loadDocument(doc);
	}
}
