package model.dao;

import db.DBConnection;
import model.dao.impl.SellerDaoJDBC;

public class DAO_Factory {
	public static SellerDAO createSellerDAO(){
		return new SellerDaoJDBC(DBConnection.getConnection());
	}
}
