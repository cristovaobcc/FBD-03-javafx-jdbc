/**
 * 
 */
package model.dao;

import java.util.ArrayList;
import java.util.List;

import model.entities.Department;

/**
 * Interface que contém os métodos para serem implementados seguindo
 * o padrão DAO.
 *
 */
public interface DepartmentDao {
	
	/**
	 * Insere um Departamento no BD.
	 * @param dept {@link Department}
	 */
	void insert(Department dept);
	
	/**
	 * Atualiza, no BD, os dados de um {@link Department} conforme o dept
	 * passado.
	 * @param dept Department
	 */
	void update(Department dept);
	
	/**
	 * Remove, do BD, o {@link Department} conforme o id passado.
	 * @param id
	 */
	void deleteById(Integer id);
	
	/**
	 * Devolve um {@link Department} com o id passado.
	 * @param id Integer
	 * @return {@link Department}
	 */
	Department findById(Integer id);
	
	/**
	 * Devolve um {@link ArrayList} de {@link Department} contidos no BD.
	 * @return List<Department>
	 */
	List<Department> findAll();
}
