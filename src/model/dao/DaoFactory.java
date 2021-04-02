/**
 * 
 */
package model.dao;

import db.DB;
import model.dao.implementacao.DepartmentDaoJDBC;
import model.dao.implementacao.SellerDaoJDBC;

/**
 * Classe com operações estáticas para instanciar os daos.
 *
 */
public class DaoFactory {
	
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.getConnection());
	}
	
	public static DepartmentDao createDepartmentDao() {
		return new DepartmentDaoJDBC(DB.getConnection());
	}

}
