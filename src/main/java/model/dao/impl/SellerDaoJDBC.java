package model.dao.impl;

import db.DBConnection;
import db.DBException;

import model.dao.SellerDAO;
import model.entities.Department;
import model.entities.Seller;


import java.sql.*;
import java.util.*;

public class SellerDaoJDBC implements SellerDAO {
	private Connection connection;
	
	public SellerDaoJDBC(Connection connection){
		this.connection = connection;
	}
	
	@Override
	public void insert(Seller obj) {
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement("INSERT INTO seller (Name, Email, BirthDate, BaseSalary, DepartmentId) " +
					"VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, obj.getName());
			ps.setString(2, obj.getEmail());
			ps.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			ps.setDouble(4, obj.getBaseSalary());
			ps.setInt(5, obj.getDepartment().getId());
			
			int rowsAffected = ps.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next()){
					int id = rs.getInt(1);
					obj.setId(id);
					System.err.print("Done, seller registered: ");
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
					System.out.print(rs.getString("Name"));
				}
				DBConnection.closeResultSet(rs);
			} else {
				throw new DBException("Unexpected error! No rows affected.");
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
		finally {
			DBConnection.closeStatement(ps);
		}
	}
	
	@Override
	public void update(Seller obj) {
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement("UPDATE seller SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " +
											     "WHERE Id = ?");
			ps.setString(1, obj.getName());
			ps.setString(2, obj.getEmail());
			ps.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			ps.setDouble(4, obj.getBaseSalary());
			ps.setInt(5, obj.getDepartment().getId());
			ps.setInt(6, obj.getId());
			
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0){
				System.err.println("Done! Seller updated: " + obj.getName());
			}
			
		} catch (SQLException exception) {
			throw new DBException(exception.getMessage());
		}
	}
	
	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			Optional<Seller> seller = Optional.ofNullable(findById(id));
			if (seller.isEmpty()){
				System.err.println("None ID compatible found in DataBase!");
			} if (seller.isPresent()){
				st = connection.prepareStatement("DELETE FROM seller WHERE id = ?");
				st.setInt(1, id);
				
				int affectedRows = st.executeUpdate();
				System.out.println("Rows affected: " + affectedRows);
			}
		} catch (SQLException e){
			throw new DBException(e.getMessage());
		}
		finally {
			DBConnection.closeStatement(st);
		}
	}
	
	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = connection.prepareStatement(
					"SELECT seller.*, department.Name AS DepName " +
							"FROM seller " +
							"INNER JOIN department ON seller.DepartmentId = department.Id " +
							"WHERE seller.Id = ?");
			st.setInt(1, id);
			
			rs = st.executeQuery();
			
			if(rs.next()){
				Department dep = instantiateDepartment(rs);
				return instantiateSeller(rs, dep);
			}
			if (!rs.next()){
				System.err.println("No Seller found with ID " + id);
			}
			return new Seller();
		} catch (SQLException e){
			throw new DBException(e.getMessage());
		}
		finally {
			DBConnection.closeResultSet(rs);
			DBConnection.closeStatement(st);
		}
	}

	
	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = connection.prepareStatement("SELECT seller.*, department.Name AS DepName " +
					"FROM seller " +
					"INNER JOIN department " +
					"ON seller.DepartmentId = department.Id " +
					"ORDER BY seller.Id;");
			rs = st.executeQuery();
			List<Seller> data = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			while (rs.next()){
				Department dep = map.get(rs.getInt("DepartmentId"));
				if (dep == null){
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				Seller seller = instantiateSeller(rs, dep);
				data.add(seller);
			}
			return data;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
		finally {
			DBConnection.closeResultSet(rs);
			DBConnection.closeStatement(st);
		}
	}
	
	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = connection.prepareStatement("SELECT seller.*, department.Name AS DepName " +
					"FROM seller " +
					"INNER JOIN department ON seller.DepartmentId = department.Id " +
					"WHERE seller.DepartmentId = ? " +
					"ORDER BY seller.Name;");
			st.setInt(1, department.getId());
			
			rs = st.executeQuery();
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			//Can be 0, so while he has next it goes...
			while (rs.next()){
				//Create a test if the department instance already exists, so it doesn't create another one.
				Department dep = map.get(rs.getInt("DepartmentId"));
				if (dep == null){
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				Seller seller = instantiateSeller(rs, dep);
				list.add(seller);
			}
			return list;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
		finally {
			DBConnection.closeResultSet(rs);
			DBConnection.closeStatement(st);
		}
	}
	
	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep);
		return obj;
	}
	
	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}
}
