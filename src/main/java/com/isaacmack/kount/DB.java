package com.isaacmack.kount;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DB {
	
	private DB() {};
	
	public static Connection getConnection() throws SQLException {
		
		try {
			final Properties p = new Properties();
			p.load(new FileInputStream("config.properties"));
			
			final String host = p.getProperty("DB_HOST");
			final String schema = p.getProperty("DB_SCHEMA");
			final String user = p.getProperty("DB_USER");
			final String password = p.getProperty("DB_PASS");
			
			return DriverManager.getConnection("jdbc:postgresql://" + host + "/" + schema, user, password);
		
		}
		catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
