package com.isaacmack.kount;

import static com.isaacmack.kount.DB.getConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * API for users
 * 
 * @author isaac
 *
 */
public class UserDAO {
	private UserDAO() {};
	
	/**
	 * Get a list of users
	 * 
	 * @return the list of users
	 * @throws SQLException 
	 */
	public static List<String> get() throws SQLException {
		try(final Connection conn = getConnection()) {
			final Statement stmt = conn.createStatement();
			final ResultSet rs = stmt.executeQuery("SELECT username FROM users ");
			
			final List<String> users = new LinkedList<>();
			
			while(rs.next()) {
				users.add(rs.getString(1));
			}
			
			return users;
		}
	}
	
	/**
	 * Add a new user
	 * 
	 * @param user
	 */
	public static void insert(String user) throws SQLException {
		if(user.length() > 2) {
			
			try(final Connection conn = getConnection()) {
				final PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users (username) VALUES (?) ");
				pstmt.setString(1, user);
				pstmt.executeUpdate();
			}
			catch(SQLException e) { // to catch UNIQUE/PRIMARY violation
				
				if(!e.getSQLState().equals("23505")) { // ignore unique constraint violations
				
					Logger.getAnonymousLogger().severe(e.getMessage());
					throw e;
				
				}
			}
			
		}	
	}
}
