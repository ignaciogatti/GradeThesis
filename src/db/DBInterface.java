package db;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DBInterface {

	public void connect(String dbFilepath, String dbPort, String user, String password) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException;

	public ResultSet executeQuery(String query) throws SQLException;

	public int executeUpdate(String sql) throws SQLException;

	public void disconnect() throws SQLException;

	public void commit() throws SQLException;

}
