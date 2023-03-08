package m09uf3.impl.bancapi;

import java.sql.Connection;
import java.sql.SQLException;

import com.mysql.cj.jdbc.MysqlDataSource;

import lib.db.ConnectionFactory;
import lib.db.DAOException;

public class ConnectionFactoryImpl implements ConnectionFactory {

	private MysqlDataSource ds;
	
	public ConnectionFactoryImpl() {
		
		ds = new MysqlDataSource();
		ds.setURL("jdbc:mysql://localhost:3306/dam2db");
		ds.setUser("myuser");
		ds.setPassword("mypassword");
		
		try {
			ds.setServerTimezone("UTC");
			ds.setDumpQueriesOnException(true);
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		return ds.getConnection();
	}

	
}
