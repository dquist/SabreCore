package com.civfactions.SabreCore.data;

import java.util.HashSet;
import java.util.UUID;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.civfactions.SabreApi.data.Documentable;
import com.civfactions.SabreApi.data.SabreDocument;

/**
 * Documentable object for testing
 * @author Gordon
 *
 */
public class DocumentableTestObject implements Documentable {

	private final UUID uid;
	private final String name;

	private int someProperty;
	private HashSet<DocumentableTestObject> list = new HashSet<DocumentableTestObject>();

	public DocumentableTestObject(final UUID uid, final String name) {
		this.uid = uid;
		this.name = name;
		this.someProperty = 0;
	}

	public UUID getUniqueId() {
		return uid;
	}

	public String getName() {
		return name;
	}

	public int getProperpty() {
		return someProperty;
	}

	public void setProperpty(int someProperty) {
		this.someProperty = someProperty;
	}

	@Override
	public String getDocumentKey() {
		return uid.toString();
	}

	@Override
	public SabreDocument getDocument() {
		return new SabreDocument("_id", getDocumentKey())
				.append("name", name)
				.append("property", someProperty);
	}

	@Override
	public DocumentableTestObject loadDocument(SabreDocument doc) {
		this.someProperty = doc.getInteger("property", someProperty);
		return this;
	}
	
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31) // two randomly chosen prime numbers
            .append(uid)
            .append(name)
            .append(someProperty)
            .toHashCode();
    }

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DocumentableTestObject)) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		
		DocumentableTestObject other = (DocumentableTestObject)obj;
		
		return new EqualsBuilder()
				.append(uid, other.uid)
				.append(name, other.name)
				.append(someProperty, other.someProperty)
				.isEquals();
	}
	
	public static DocumentableTestObject create(String name) {
		return new DocumentableTestObject(UUID.randomUUID(), name);
	}
}
