/**
 * 
 */
package model.dao.implementacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

/**
 * @author cristovao
 *
 */
public class DepartmentDaoJDBC implements DepartmentDao {
	
	
	private Connection connection;
	
	
	public DepartmentDaoJDBC(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void insert(Department dept) {
		
		PreparedStatement ps = null;
		
		try {
			
			ps = connection.prepareStatement("INSERT INTO department "
					+ "(name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, dept.getName());
			
			int rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				
				ResultSet rs = ps.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					dept.setId(id);
				}
				
				DB.closeResultSet(rs);
				
			} else {
				
				throw new DbException("Erro inesperado: nenhuma linha inserida!");
	
			}
			
		} catch (SQLException e) {
			
			throw new DbException(e.getMessage());
		} finally {
			
			DB.closeStatement(ps);
		}

	}

	@Override
	public void update(Department dept) {
		
		PreparedStatement ps = null;
		
		try {
			
			ps = connection.prepareStatement("UPDATE department "
					+ "SET name = ? "
					+ "WHERE Id = ?", Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, dept.getName());
			ps.setInt(2, dept.getId());
			
			ps.executeUpdate();
			
			
		} catch (SQLException e) {
			
			throw new DbException(e.getMessage());
			
		} finally {
			
			DB.closeStatement(ps);
		}
	}

	@Override
	public void deleteById(Integer id) {
		
		PreparedStatement ps = null;
		try {
			
			ps = connection.prepareStatement("DELETE FROM department WHERE id = ? ");
			
			ps.setInt(1, id);
			
			ps.executeUpdate();
		
		} catch (SQLException e) {
			
			throw new DbException(e.getMessage());

		} finally {
			
			DB.closeStatement(ps);
		}

	}

	@Override
	public Department findById(Integer id) {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			ps = connection.prepareStatement("SELECT dept.* "
					+ "FROM department dept "
					+ "WHERE dept.Id = ?");
			
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				
				Department dep = this.instantiateSeller(rs);
				
				return dep;
			}
			
			return null;
			
		} catch(SQLException e) {
			
			throw new DbException(e.getMessage());
		} finally {
			
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Department> findAll() {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			ps = connection.prepareStatement("SELECT depto.* FROM department AS depto ORDER BY name");
			
			rs = ps.executeQuery();
			
			List<Department> depts = new ArrayList<>();
			Department dep = null;
			
			while(rs.next()) {
				
				dep = this.instantiateSeller(rs);
				depts.add(dep);
			}
			
			return depts.size() > 0 ? depts : null;
		
		} catch (SQLException e) {
		
			throw new DbException(e.getMessage());
			
		} finally {

			DB.closeStatement(ps);
			DB.closeResultSet(rs);;
		}
	}
	
	/**
	 * Método auxiliar que devolve um Department preenchidos com os campos de
	 * ResultSet rs.
	 * @param rs {@link ResultSet}
	 * @return {@link Department}
	 * @throws SQLException
	 */
	private Department instantiateSeller(ResultSet rs) throws SQLException{
		
		Department dept = new Department();
		dept.setId(rs.getInt("Id"));
		dept.setName(rs.getString("Name"));
		
		return dept;
	}
}
