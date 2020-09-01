package com.isaacmack.kount;

import static com.isaacmack.kount.DB.getConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 * API for Todos
 * 
 * @author isaac
 *
 */
public class TodoDAO {
	private TodoDAO() {};
	
	
	/**
	 * Remove completed {@linkplain Todo}s from user
	 * 
	 * @param user
	 */
	public static void clearCompleted(String user) throws SQLException {
		
		final String sql = "DELETE FROM todos WHERE username = ? AND completed = true ";
		
		try(final Connection conn = getConnection()) {
			final PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user);
			
			pstmt.executeUpdate();
		}
	}
	
	
	/**
	 * Remove a single {@linkplain Todo}
	 * 
	 * @param user
	 * @param id
	 */
	public static void delete(String user, int id) throws SQLException {
		final String sql = "DELETE FROM todos WHERE username = ? AND id = ? ";
		
		try(final Connection conn = getConnection()) {
			final PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user);
			pstmt.setLong(2, id);
			
			pstmt.executeUpdate();
		}
	}
	
	
	/**
	 * Get a list of {@linkplain Todo} for the user
	 * 
	 * @param user
	 * @return
	 */
	public static List<Todo> get(String user) throws SQLException {
		
		final String sql = "SELECT id, title, completed FROM todos WHERE username = ? ";
		
		try(final Connection conn = getConnection()) {
			final PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user);
			
			final ResultSet rs = pstmt.executeQuery();
			
			final List<Todo> toReturn = new LinkedList<>();
			
			while(rs.next()) {
				toReturn.add(new Todo(
						  rs.getInt("id")
						, rs.getString("title")
						, rs.getBoolean("completed")
				));
			}
			
			return toReturn;
		}
	}
	
	
	/**
	 * Add a new {@linkplain Todo}
	 * 
	 * @param user
	 * @param todo
	 * @return
	 */
	public static Todo insert(String user, Todo todo) throws SQLException {
		final String sql = "INSERT INTO todos (title,completed,username) VALUES (?,?,?) ";
		
		try(final Connection conn = getConnection()) {
			final PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, todo.title);
			pstmt.setBoolean(2, todo.completed);
			pstmt.setString(3, user);
			
			pstmt.executeUpdate();
			
			final ResultSet rs = pstmt.getGeneratedKeys();
			rs.next();
			final int id = rs.getInt(1);
			
			todo.id = id;
			
			return todo;
		}
	}
	
	
	/**
	 * Update an existing {@linkplain Todo} if it exists
	 * 
	 * @param user
	 * @param todo
	 */
	public static void put(String user, Todo todo) throws SQLException {
		final String sql = "UPDATE todos SET title = ?, completed = ? WHERE username = ? AND id = ? ";
		
		try(final Connection conn = getConnection()) {
			final PreparedStatement pstmt = conn.prepareStatement(sql);
			
			int i=1;
			pstmt.setString(i++, todo.title);
			pstmt.setBoolean(i++, todo.completed);
			pstmt.setString(i++, user);
			pstmt.setInt(i++, todo.id);
			
			pstmt.executeUpdate();
			
		}
	}
	
}
