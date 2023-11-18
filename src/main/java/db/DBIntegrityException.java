package db;


import java.io.Serial;

public class DBIntegrityException extends RuntimeException{

	@Serial
	private static final long serialVersionUID = 1L;
	public DBIntegrityException(String msg){
		super(msg);
	}
}
