package com.github.jonnylin13.ve.objects.generic;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.github.jonnylin13.ve.VEPlugin;
import com.github.jonnylin13.ve.constants.Queries;
import com.github.jonnylin13.ve.tools.QueryParser;
import com.google.gson.Gson;

public abstract class SQLObject extends JSONObject {
	
	private transient Map<String, Boolean> updateInfo;
	
	public SQLObject() {
		this.updateInfo = new HashMap<String, Boolean>();
	}
	
	public abstract String matchCondition();
	
	public String wrap(String token) {
		return "'" + token + "'";
	}
	
	public <T> void insert() {
		try {
			Statement statement = VEPlugin.getInstance().getDb().getConnection().createStatement();
			String columns = "";
			String values = "";
			try {
				for (Field field : this.getClass().getDeclaredFields()) {
					field.setAccessible(true);
					boolean isTransient = Modifier.isTransient(field.getModifiers());
					if (isTransient) continue;
					String col = field.getName();
					Object valObj = field.get(this);
					String val;
					
						if (field.get(this) instanceof String[]) {
							Gson gson = new Gson();
							val = "'" + gson.toJson(valObj) + "'";
						} else {
							val = "'" + String.valueOf(valObj)+ "'";
						}
					
					if (columns == "") {
						columns += col;
						values += val;
					} else {
						columns += ", " + col;
						values += ", " + val;
					}
					this.updateInfo.put(col, false);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			statement.execute(QueryParser.parseInsert(Queries.USERS, columns, values));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
