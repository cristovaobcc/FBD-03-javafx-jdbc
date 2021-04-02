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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

/**
 * Classe que representa a implementação JDBC de SellerDao.
 *
 */
public class SellerDaoJDBC implements SellerDao{

	private Connection connection;
	
	
	public SellerDaoJDBC(Connection connection) {
		this.connection = connection;
	}
	
	
	@Override
	public void insert(Seller seller) {
		
		PreparedStatement ps = null;
		
		try {
			ps = connection.prepareStatement("INSERT INTO seller "
											+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
											+ "VALUES "
											+ "(?, ?, ?, ?, ?)",
											Statement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, seller.getName());
			ps.setString(2, seller.getEmail());
			ps.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
			ps.setDouble(4, seller.getBaseSalary());
			ps.setInt(5, seller.getDepartment().getId());
			
			int  rowsAffected = ps.executeUpdate();
			
			if (rowsAffected > 0) {
				
				ResultSet rs  = ps.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					seller.setId(id);
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
	public void update(Seller seller) {
		
		PreparedStatement ps = null;
		
		try {
			ps = connection.prepareStatement("UPDATE seller "
											+ "SET Name = ?,  Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId  = ? "
											+ "WHERE Id = ?"
											, Statement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, seller.getName());
			ps.setString(2, seller.getEmail());
			ps.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
			ps.setDouble(4, seller.getBaseSalary());
			ps.setInt(5, seller.getDepartment().getId());
			ps.setInt(6, seller.getId());
			
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
			ps = connection.prepareStatement("DELETE FROM seller WHERE Id = ?");
			
			ps.setInt(1, id);
			
			ps.executeUpdate();
			
			// Pode-se lançar uma exceção caso se queira deletar um id que não existe.
			
		} catch (SQLException e) {
			
			throw new DbException(e.getMessage());
			
		} finally {
			
			DB.closeStatement(ps);
		}
		
	}

	@Override
	public Seller findById(Integer id) {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = connection.prepareStatement(
								"SELECT seller.*, department.Name as DepName " 
								+ "FROM seller INNER JOIN department "
								+ "ON seller.DepartmentId = department.Id "
								+ "WHERE seller.Id = ?");
			
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) { // devolve true se há um vendedor com o id passado.
				
				Department dep = instantiateDepartment(rs);
				
				Seller seller = instantiateSeller(rs, dep);
				
				return seller;
				
			}
			return null;
					
		} catch(SQLException e) {
			
			throw new DbException(e.getMessage());
			
		} finally {
			
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
		
	}
	
	
	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		
		Seller seller = new Seller();
		seller.setId(rs.getInt("Id"));
		seller.setName(rs.getString("Name"));
		seller.setEmail(rs.getString("Email"));
		seller.setBaseSalary(rs.getDouble("BaseSalary"));
		seller.setBirthDate(rs.getDate("BirthDate"));
		seller.setDepartment(dep);
		
		return seller;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId")); // nome conforme a tabela do BD ou alias inserido na consulta.
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = connection.prepareStatement(
								"SELECT seller.*, department.Name as DepName " 
								+ "FROM seller INNER JOIN department "
								+ "ON seller.DepartmentId = department.Id "
								+ "ORDER BY Name");
			
			rs = ps.executeQuery();
			List<Seller> sellers = new ArrayList<>();
			Department dep = null;
			Seller seller = null;
			Map<Integer, Department> mapDept = new HashMap<>(); 
			
			while (rs.next()) { // devolve true se há um departamento com o id passado.
				
				dep = mapDept.get(rs.getInt("DepartmentId"));
				if (dep == null) {
					dep = instantiateDepartment(rs);
					mapDept.put(dep.getId(), dep);
				}			
				
				seller = instantiateSeller(rs, dep);
				
				sellers.add(seller);
				
			}
			
			return sellers.size() == 0 ? null : sellers;
					
		} catch(SQLException e) {
			
			throw new DbException(e.getMessage());
			
		} finally {
			
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = connection.prepareStatement(
								"SELECT seller.*, department.Name as DepName " 
								+ "FROM seller INNER JOIN department "
								+ "ON seller.DepartmentId = department.Id "
								+ "WHERE department.Id = ? "
								+ "ORDER BY Name");
			
			ps.setInt(1, department.getId());
			rs = ps.executeQuery();
			List<Seller> sellers = new ArrayList<>();
			Department dep = null;
			Seller seller = null;
			Map<Integer, Department> mapDept = new HashMap<>(); 
			
			while (rs.next()) { // devolve true se há um departamento com o id passado.
				
				dep = mapDept.get(rs.getInt("DepartmentId"));
				if (dep == null) {
					dep = instantiateDepartment(rs);
					mapDept.put(dep.getId(), dep);
				}			
				
				seller = instantiateSeller(rs, dep);
				
				sellers.add(seller);
				
				
			}
			
			return sellers.size() == 0 ? null : sellers;
					
		} catch(SQLException e) {
			
			throw new DbException(e.getMessage());
			
		} finally {
			
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
		
	}
	

}
