/**
 * 
 */
package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Department;

/**
 * Serviços relacionados ao Department.
 *
 */
public class DepartmentService {
	
	/**
	 * Devolve todos os Departments.
	 * @return {@link List}< Department >
	 */
	public List<Department> findAll(){
		// Dados mockados.
		List<Department> list = new ArrayList<>();
		list.add(new Department(1, "Books"));
		list.add(new Department(2, "Computers"));
		list.add(new Department(3, "Eletronics"));
		return list;
	}

}
