package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DBException;
import model.dao.DepartmentDAO;
import model.entities.Department;
import model.entities.Seller;

public class DepartmentDAOJDBC implements DepartmentDAO {
	
	private Connection conn;
	
	public DepartmentDAOJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Department department) {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement(
				"INSERT INTO department "
				+ "(Name) "
				+ "VALUES (?) ",
				Statement.RETURN_GENERATED_KEYS
			);
			
			st.setString(1, department.getName());
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					department.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DBException("Unexpected error! No rows affected.");
			}
			
		} catch(SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void update(Department department) {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement(
				"UPDATE department "
				+ "SET Name = ? "
				+ "WHERE Id = ?"
			);
			
			st.setString(1, department.getName());
			st.setInt(2, department.getId());
			
			st.executeUpdate();
			
		} catch(SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement(
				"DELETE FROM department WHERE Id = ?"
			);
			
			st.setInt(1,  id);
			
			st.executeUpdate();
			
		} catch(SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public Department findById(Integer id) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = conn.prepareStatement(
				"SELECT * FROM department "
				+ "WHERE department.Id = ?"
			);
			
			st.setInt(1, id);
			
			rs = st.executeQuery();
			
			if(rs.next()) {
				Department department = instantiateDepartment(rs);
				return department;
			}
			
			return null;
			
		} catch(SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}

	@Override
	public List<Department> findAll() {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = conn.prepareStatement(
				"SELECT * FROM department "
				+ "ORDER BY Name"
			);
			
			rs = st.executeQuery();
			
			List<Department> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			while(rs.next()) {
				Department dep = instantiateDepartment(rs);
				list.add(dep);
			}
			
			return list;
			
		} catch(SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}
	
	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department department = new Department();
		department.setId(rs.getInt("Id"));
		department.setName(rs.getString("Name"));
		return department;
	}

}
