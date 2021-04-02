/**
 * 
 */
package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Classe com metodos estáticos auxiliares para obter e fechar uma conexão com um DB.
 *
 */
public class DB {
	
	// Atributo que aponta para objeto de realiza a conexão com o DB. 
	private static Connection connection = null;

	
	/**
	 * Carrega os dados de arquivo "db.properties", devolvendo-os
	 * em um objeto Properties.
	 * @return {@link Properties}
	 */
	private static Properties loadProperties() {
		
		Properties props = null;
		try(FileInputStream fs = new FileInputStream("db.properties")){
			props = new Properties();
			props.load(fs);
			
		} catch(IOException e) {
			throw new DbException(e.getMessage());
		}
		
		return props;
	}

	
	/**
	 * Obtém uma conexão com o BD através de uma Connection
	 * @return Connection
	 */
	public static Connection getConnection() {
		if (connection == null) {
			try {
				Properties props = loadProperties();
				// devolve a url do db.
				String url = props.getProperty("dburl");
				// obtém conexao com db. Instancia-se objeto do tipo Connection.
				connection = DriverManager.getConnection(url, props); 
				
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
			
		}
		return connection;
	}
	
	
	/**
	 * Fecha a conexão com o DB.
	 */
	public static void closeConnection() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
	/**
	 * Fecha um objeto Statement.
	 * @param statement Statement.
	 */
	public static void closeStatement(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				
				throw new DbException(e.getMessage());	
				
			}
		}
	}
	
	/**
	 * Fecha um objeto ResultSet.
	 * @param resultSet ResultSet
	 */
	public static void closeResultSet(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				
				throw new DbException(e.getMessage());	
				
			}
		}
	}
	
	
}
