/**
 * 
 */
package model.services;

import java.util.ArrayList;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

/**
 * Serviços relacionados ao Department.
 *
 */
public class DepartmentService {
	
	private DepartmentDao dao = DaoFactory.createDepartmentDao();
	
	/**
	 * Devolve todos os Departments.
	 * @return {@link List}< Department >
	 */
	public List<Department> findAll(){
		
		return dao.findAll();
		
	}

}
