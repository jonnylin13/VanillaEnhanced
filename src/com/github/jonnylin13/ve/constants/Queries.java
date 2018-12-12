package com.github.jonnylin13.ve.constants;

public class Queries {
	
	public static String USERS = "Users";
	public static String USERS_SCHEMA = "name varchar(255) unique not null, uuid varchar(36) not null, groups varchar(255) not null, level int not null, xp int not null, primary key (uuid)";

	public static String SPAWNERS = "Spawners";
	public static String SPAWNERS_SCHEMA = "id varchar(255) unique not null, spawnedType varchar(255) not null, primary key (id)";
	
	public static String CREATE_TABLE = "create table if not exists %s (%s);";
	public static String INSERT = "insert into %s (%s) values (%s);";
	public static String SELECT = "select * from %s where %s;";
	public static String UPDATE = "update %s set %s where %s";
	public static String DELETE = "delete from %s where %s;";

}
