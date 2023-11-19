package model.dao.impl;

import db.DBConnection;
import db.DBException;
import model.dao.DepartmentDAO;
import model.entities.Department;
import model.entities.Seller;

import java.nio.channels.SeekableByteChannel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDAO {
	private final Connection connection;
	
	public DepartmentDaoJDBC(Connection connection){
		this.connection = connection;
	}
	
	@Override
	public void insert(Department obj) {
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement("INSERT INTO department (Name) VALUES (?)",Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, obj.getName());
			
			int rowsAffected = ps.executeUpdate();
			
			if (rowsAffected>0){
				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next()){
					int id = rs.getInt(1);
					obj.setId(id);
					System.err.print("Done, department registered: " + obj.getName() + "\n");
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
				DBConnection.closeResultSet(rs);
			} else {
				throw new DBException("Unexpected error! No rows affected.");
			}
			
		} catch (SQLException e){
			throw new DBException(e.getMessage());
		}
		finally {
			DBConnection.closeStatement(ps);
		}
	}
	
	@Override
	public void update(Department obj) {
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement("UPDATE department SET Name = ? WHERE Id = ?");
			ps.setString(1, obj.getName());
			ps.setInt(2, obj.getId());
			
			int rA = ps.executeUpdate();
			if (rA > 0){
				System.err.println("Done! Department updated: " + obj.getName());
			}
			
		} catch (SQLException e){
			throw new DBException(e.getMessage());
		}
		finally {
			DBConnection.closeStatement(ps);
		}
	}
	
	@Override
	public void deleteById(Integer id) {
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement("DELETE FROM department WHERE Id = ?", Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, id);
			
			int rA = ps.executeUpdate();
			
			if (rA > 0){
				ResultSet rs = ps.getGeneratedKeys();
				if(rs.next()){
					System.err.println("Department deleted: " + rs.getString("Name"));
				}
				DBConnection.closeResultSet(rs);
			} else {
				throw new DBException("Unexpected error, ID may not exist in DataBase!");
			}
		} catch (SQLException e){
			throw new DBException(e.getMessage());
		}
		finally {
			DBConnection.closeStatement(ps);
		}
	}
	
	@Override
	public Department findById(Integer id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement("SELECT * FROM department WHERE Id = ?");
			ps.setInt(1, id);
			
			rs = ps.executeQuery();
			
			if (rs.next()){
				return instantiateDepartment(rs);
			}
			if (!rs.next()){
				System.err.println("No department found with ID " + id);
			}
			return new Department();
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DBConnection.closeResultSet(rs);
			DBConnection.closeStatement(ps);
		}
	}
	
	@Override
	public List<Department> findAll() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement("SELECT * FROM department");
			rs = ps.executeQuery();
			List<Department> data = new ArrayList<>();
			while (rs.next()){
				Department department = instantiateDepartment(rs);
				data.add(department);
			}
			return data;
		} catch (SQLException e){
			throw new DBException(e.getMessage());
		} finally {
			DBConnection.closeResultSet(rs);
			DBConnection.closeStatement(ps);
		}
	}
	
	@Override
	public List<Seller> findAllSellersOnDepartmentById(Integer id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(
						"SELECT department.*, seller.* " +
								"FROM department " +
								"INNER JOIN seller " +
								"ON department.Id = seller.DepartmentId " +
								"WHERE seller.DepartmentId = ?;");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			List<Seller> sellers = new ArrayList<>();
 			while (rs.next()){
				Department department = instantiateDepartment(rs);
				Seller seller = instantiateSeller(rs, department);
				sellers.add(seller);
			}
			return sellers;
		} catch (SQLException e){
			throw new DBException(e.getMessage());
		} finally {
			DBConnection.closeResultSet(rs);
			DBConnection.closeStatement(ps);
		}
	}
	
	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("Id"));
		dep.setName(rs.getString("Name"));
		return dep;
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
	
}
