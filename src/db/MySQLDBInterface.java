package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

import constantes.Constantes;

public class MySQLDBInterface implements DBInterface{
	
	private Connection connection = null;
	private final String driverName = Constantes.DB_DRIVER_NAME;

	public MySQLDBInterface() {
		// TODO Auto-generated constructor stub
	}
	
	public void connect(String dbFilepath, String dbPort, String user, String password) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Class.forName(driverName).newInstance();
		String databaseURL = Constantes.DATA_BASE_URL;
		connection = java.sql.DriverManager.getConnection(databaseURL, user, password);
		System.out.println("Connection established.");
		connection.setAutoCommit(false);
		System.out.println("Auto-commit is disabled.");
	}

	public ResultSet executeQuery(String query) throws SQLException {
		Statement statement = (Statement) connection.createStatement();
		return statement.executeQuery(query);
	}

	public int executeUpdate(String sql) throws SQLException {
		Statement statement = (Statement) connection.createStatement();
		return statement.executeUpdate(sql);
	}

	public void disconnect() throws SQLException {
		connection.close();
	}

	public void commit() throws SQLException {
		connection.commit();
	}


}
