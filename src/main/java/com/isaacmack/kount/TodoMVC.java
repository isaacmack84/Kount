package com.isaacmack.kount;

import java.util.logging.Logger;

import com.google.gson.Gson;

import spark.Spark;

public class TodoMVC {
	public static void main(String[] args) {
		
		final Gson gson = new Gson();
		
		Spark.init();
		
//		Spark.before((request,response) -> {
//			request.session();
//		});
		
		
		// tell angular we have an exposed api		
		Spark.get("/api", (request,response) -> {
			return "";
		});
		
		
		// list users 
		Spark.get("/api/users", (request,response) -> {
			return gson.toJson(UserDAO.get());
		});
		
		
		// add user
		Spark.post("/api/users", (request,response) -> {
			UserDAO.insert(request.body());
			return "";
		});
		
		
		// list Todos for user
		Spark.get("/api/todos", (request,response) -> {
			final String user = request.cookie("user");
			if(user == null) {
				response.status(401);
				return "";
			}
			
			return gson.toJson(TodoDAO.get(user)); 
		});
		
		
		// add Todo for user
		Spark.post("/api/todos", (request,response) -> {
			final String user = request.cookie("user");
			if(user == null) {
				response.status(401);
				return "";
			}
			
			final Todo todo = gson.fromJson(request.body(), Todo.class);
			
			return gson.toJson(TodoDAO.insert(user, todo));
		});
		
		
		
		// update Todo for user
		Spark.put("/api/todos/:id", (request,response) -> {
			final String user = request.cookie("user");
			if(user == null) {
				response.status(401);
				return "";
			}
			
			try {
				
				final int id = Integer.parseInt(request.params("id"));
			
				final Todo todo = gson.fromJson(request.body(), Todo.class);
				
				
				if(id != todo.id) {
					Logger.getAnonymousLogger().severe("Todo id in url doens't match todo in payload");
					response.status(500);
					return "";
				}
				
				
				TodoDAO.put(user, todo);
				
				return "";
			
			}
			catch(NumberFormatException e) {
				Logger.getAnonymousLogger().severe(e.getMessage());
				
				response.status(404);
				return "";
				
			}
		});
		
		
		// remove completed todos for user
		Spark.delete("/api/todos", (request,response) -> {
			
			final String user = request.cookie("user");
			if(user == null) {
				response.status(401);
				return "";
			}
			
			TodoDAO.clearCompleted(user);
			return "";
		});
		
		
		// remove a single Todo for a user
		Spark.delete("/api/todos/:id", (request,response) -> {
			final String user = request.cookie("user");
			if(user == null) {
				response.status(401);
				return "";
			}
			
			try {
				
				final int id = Integer.parseInt(request.params("id"));
				
				TodoDAO.delete(user, id);
				
				return "";
			
			}
			catch(NumberFormatException e) {
				Logger.getAnonymousLogger().severe(e.getMessage());
				
				response.status(404);
				return "";
				
			}
		});
		
		
		Spark.after((request,response) -> {
			response.header("Content-Type", "application/json");
			response.header("Content-Encoding", "gzip");
		});
		
	}
}