/**
 * 
 */
package db;

/**
 * Exceção que indica que houve restrição de integridade. 
 *
 */
public class DbInegrityException extends RuntimeException {

	
	private static final long serialVersionUID = 1L;
	
	public DbInegrityException(String msg) {
		super(msg);
	}
}
