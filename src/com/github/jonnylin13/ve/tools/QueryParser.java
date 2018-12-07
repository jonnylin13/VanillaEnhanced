package com.github.jonnylin13.ve.tools;

import com.github.jonnylin13.ve.constants.Queries;

public class QueryParser {
	
	public static String parseInsert(String table, String columns, String values) {
		return String.format(Queries.INSERT, table, columns, values);
	}
	
	public static String parseSelect(String table, String condition) {
		return String.format(Queries.SELECT, table, condition);
	}
	
	public static String parseUpdate(String table, String update, String condition) {
		return String.format(Queries.UPDATE, table, update, condition);
	}
	
	public static String parseCreate(String table, String schema) {
		return String.format(Queries.CREATE_TABLE, table, schema);
	}
	
	public static String parseDelete(String table, String condition) {
		return String.format(Queries.DELETE, table, condition);
	}

}
