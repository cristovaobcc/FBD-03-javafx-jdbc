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
	
	/**
	 * Salva ou autaliza um Department dept passado. Salva, caso o id seja, nulo. 
	 * Do contrário, ele atualiza com os novos dados.
	 * @param dept Department
	 */
	public void saveOrUpdate(Department dept) {
		if (dept.getId() == null) {
			dao.insert(dept);
		} else {
			dao.update(dept);
		}
	}
	
	/**
	 * Remove um {@link Department} dept do DB.
	 * @param dept {@link Department}
	 */
	public void remove(Department dept) {
		dao.deleteById(dept.getId());
	}

}
