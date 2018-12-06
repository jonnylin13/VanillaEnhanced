package com.github.jonnylin13.ve.db;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.github.jonnylin13.ve.VEPlugin;
import java.sql.Connection;

public class SQLDatabase {

	private VEPlugin vep;
	private Connection connection;

	public SQLDatabase(VEPlugin instance) {
		this.vep = instance;
		this.openConnection();
	}

	public void openConnection() {
		try {
			if (this.connection != null && !this.connection.isClosed()) {
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
			if (this.connection != null && !this.connection.isClosed()) {
				this.connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
