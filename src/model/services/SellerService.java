/**
 * 
 */
package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

/**
 * Serviços relacionados ao Seller.
 *
 */
public class SellerService {
	
	private SellerDao dao = DaoFactory.createSellerDao();
	
	/**
	 * Devolve todos os Sellers.
	 * @return {@link List}< Seller >
	 */
	public List<Seller> findAll(){
		
		return dao.findAll();
		
	}
	
	/**
	 * Salva ou autaliza um Seller dept passado. Salva, caso o id seja, nulo. 
	 * Do contrário, ele atualiza com os novos dados.
	 * @param dept Seller
	 */
	public void saveOrUpdate(Seller dept) {
		if (dept.getId() == null) {
			dao.insert(dept);
		} else {
			dao.update(dept);
		}
	}
	
	/**
	 * Remove um {@link Seller} dept do DB.
	 * @param dept {@link Seller}
	 */
	public void remove(Seller dept) {
		dao.deleteById(dept.getId());
	}

}
