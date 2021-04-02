package db;

public class DbException extends RuntimeException {

	private static final long serialVersionUID = 5035379979276647580L;
	
	public DbException(String msg) {
		super(msg);
	}

}
