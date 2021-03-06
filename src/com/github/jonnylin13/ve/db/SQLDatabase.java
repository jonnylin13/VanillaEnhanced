package com.github.jonnylin13.ve.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.github.jonnylin13.ve.VEPlugin;
import com.github.jonnylin13.ve.constants.Queries;
import com.github.jonnylin13.ve.tools.QueryParser;

public class SQLDatabase {

	private VEPlugin vep;
	private Connection connection;

	public SQLDatabase(VEPlugin instance) {
		this.vep = instance;
		this.openConnection();
		this.provisionTables();
	}
	
	private void provisionTables() {
		Statement statement;
		try {
			statement = this.connection.createStatement();
			statement.execute(QueryParser.parseCreate(Queries.USERS, Queries.USERS_SCHEMA));
			statement.execute(QueryParser.parseCreate(Queries.SPAWNERS, Queries.SPAWNERS_SCHEMA));
		} catch (SQLException e) {
			// TODO: Handle exception
			e.printStackTrace();
		}
	}

	private void openConnection() {
		try {
			if (open()) {
				return;
			}
			synchronized (this) {
				this.connection = DriverManager.getConnection(
						"jdbc:mysql://" + this.vep.getCfg().getHost() + "/" + this.vep.getCfg().getDatabase(),
						this.vep.getCfg().getUsername(), this.vep.getCfg().getPassword());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void closeConnection() {
		try {
			if (open()) {
				this.connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean open() {
		try {
			return (this.connection != null && !this.connection.isClosed());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Connection getConnection() {
		if (!open()) {
			openConnection();
		}
		return this.connection;
	}
	
	public ResultSet query(String query) throws SQLException {
		Statement statement = this.getConnection().createStatement();
		return statement.executeQuery(query);
	}
	
	public boolean execute(String query) throws SQLException {
		Statement statement = this.getConnection().createStatement();
		return statement.execute(query);
	}
	

}
