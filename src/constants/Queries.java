package constants;

public class Queries {
	
	public static String USERS = "Users";
	public static String USERS_SCHEMA = "uuid varchar(36) not null, groups varchar(255) not null, primary key (uuid)";
	public static String CREATE_USERS_TABLE = "create table if not exists " + USERS + " (" + USERS_SCHEMA + ");";
	public static String INSERT = "insert into %s (%s) values (%s);";
	public static String SELECT = "select * from %s where %s;";

}
