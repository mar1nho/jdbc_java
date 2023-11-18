package db;


import java.io.Serial;

public class DBException extends RuntimeException{
	

	@Serial
	private static final long serialVersionUID = 1L;
	
	public DBException(String msg){
		super(msg);
	}
}
