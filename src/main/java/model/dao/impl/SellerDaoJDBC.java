package model.dao.impl;

import db.DBException;
import model.dao.SellerDAO;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SellerDaoJDBC implements SellerDAO {
	private Connection connection;
	
	public SellerDaoJDBC(Connection connection){
		this.connection = connection;
	}
	
	@Override
	public void insert(Seller obj) {
	
	}
	
	@Override
	public void update(Seller obj) {
	
	}
	
	@Override
	public void deleteById(Integer id) {
	
	}
	
	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = connection.prepareStatement(
					"SELECT seller.*, department.Name AS department " +
							"FROM seller " +
							"INNER JOIN department ON seller.DepartmentId = department.Id " +
							"WHERE seller.Id = ?");
			st.setInt(1, id);
			
			rs = st.executeQuery();
			
			if(rs.next()){
				Department dep = new Department();
				dep.setId(rs.getInt("DepartmentId"));
				dep.setName(rs.getString("Name"));
				
				Seller seller = new Seller();
				seller.setId(rs.getInt("Id"));
				seller.setName(rs.getString("Name"));
				seller.setBaseSalary(rs.getDouble("BaseSalary"));
				seller.setEmail(rs.getString("Email"));
				seller.setBirthDate(rs.getDate("BirthDate"));
				seller.setDepartment(dep);
				return seller;
			}
			return null;
		} catch (SQLException e){
			throw new DBException(e.getMessage());
		}
	}
	
	@Override
	public List<Seller> findAll() {
		return null;
	}
}
