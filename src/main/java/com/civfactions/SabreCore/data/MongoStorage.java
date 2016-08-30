package com.civfactions.SabreCore.data;

import com.civfactions.SabreCore.DataStorage;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoDatabase;

import java.util.logging.Level;
import java.util.logging.Logger;

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
	private int dbTimeoutms;
	
	private boolean connected;
	private MongoClient mongoClient;
	private MongoDatabase db;
	
	public MongoStorage(SabreLogger logger) {
		Guard.ArgumentNotNull(logger, "logger");
		
		this.logger = logger;
		
		// Only log severe messages
		Logger.getLogger( "org.mongodb.driver" ).setLevel(Level.SEVERE);
	}

	@Override
	public boolean connect() {
		try {
			logger.log("Connecting to database...");
			
			String connectionString = "mongodb://";
			
			if (!DbUser.isEmpty()) {
				connectionString += String.format("%s:%s@", DbUser, DbPassword);
			}
			
			connectionString += String.format("%s:%d/?connectTimeoutMS=%d&socketTimeoutMS=%d", DbAddress, DbPort, dbTimeoutms, dbTimeoutms);
			
			mongoClient = new MongoClient(new MongoClientURI(connectionString));
			
			db = mongoClient.getDatabase(DbName).withWriteConcern(WriteConcern.UNACKNOWLEDGED);
			
			logger.log("Database connected");
			
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
	public String getDocumentKey() {
		return "database";
	}

	@Override
	public SabreDocument getDocument() {
		return new SabreDocument()
				.append("address", DbAddress)
				.append("port", DbPort)
				.append("name", DbName)
				.append("user", DbUser)
				.append("pass", DbPassword)
				.append("timeout_ms", dbTimeoutms);
	}

	@Override
	public MongoStorage loadDocument(SabreDocument doc) {
		DbAddress = doc.getString("address", "localhost");
		DbPort = doc.getInteger("port", 27017);
		DbName = doc.getString("name", "sabre");
		DbUser = doc.getString("user", "");
		DbPassword = doc.getString("pass", "");
		dbTimeoutms = doc.getInteger("timeout_ms", 3000);
		return this;
	}
}
