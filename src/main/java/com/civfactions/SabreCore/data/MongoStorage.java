package com.civfactions.SabreCore.data;

import com.civfactions.SabreCore.DataStorage;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoDatabase;

import java.util.Arrays;
import java.util.logging.Level;

import com.civfactions.SabreApi.SabreLogger;
import com.civfactions.SabreApi.data.DataCollection;
import com.civfactions.SabreApi.data.Documentable;
import com.civfactions.SabreApi.data.SabreDocument;
import com.civfactions.SabreApi.data.SabreObjectFactory;
import com.civfactions.SabreApi.util.Guard;

public class MongoStorage implements DataStorage {
	
	private final SabreLogger logger;
	
	private String DbAddress;
	private int DbPort;
	private String DbName;
	private String DbUser;
	private String DbPassword;
	
	private boolean connected;
	private MongoClient mongoClient;
	private MongoDatabase db;
	
	public MongoStorage(SabreLogger logger) {
		Guard.ArgumentNotNull(logger, "logger");
		
		this.logger = logger;
	}

	@Override
	public boolean connect() {
		try {
			if (DbUser.isEmpty()) {
				mongoClient = new MongoClient(new ServerAddress(DbAddress, DbPort));
			} else {
				MongoCredential credential = MongoCredential.createCredential(DbUser, DbName, DbPassword.toCharArray());
				mongoClient = new MongoClient(new ServerAddress(DbAddress, DbPort), Arrays.asList(credential));
			}
			
			db = mongoClient.getDatabase(DbName).withWriteConcern(WriteConcern.UNACKNOWLEDGED);
			
			connected = true;
			return true;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Failed to connect to MongoDB database.");
			ex.printStackTrace();
			return false;
		}
	}

	@Override
	public void disconnect() {
		try {
			if (mongoClient != null) {
				mongoClient.close();
				mongoClient = null;
			}
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Failed to close MongoDB database.");
			ex.printStackTrace();
		} finally {
			connected = false;
		}
	}

	@Override
	public boolean isConnected() {
		return connected;
	}

	@Override
	public <T extends Documentable> DataCollection<T> getDataCollection(String name, SabreObjectFactory<T> factory) {
		if (!connected) {
			throw new RuntimeException("The MongoDB database isn't connected.");
		}
		
		return new MongoDataCollection<T>(db.getCollection(name), factory, logger);
	}

	@Override
	public Object getUniqueId() {
		return "database";
	}

	@Override
	public SabreDocument asDocument() {
		return new SabreDocument()
				.append("address", DbAddress)
				.append("port", DbPort)
				.append("name", DbName)
				.append("user", DbUser)
				.append("pass", DbPassword);
	}

	@Override
	public Documentable fromDocument(SabreDocument doc) {
		DbAddress = doc.getString("address", "localhost");
		DbPort = doc.getInteger("port", 27017);
		DbName = doc.getString("name", "sabre");
		DbUser = doc.getString("user", "");
		DbPassword = doc.getString("pass", "");
		return this;
	}
}
