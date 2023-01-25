package model.dao;

import db.DB;
import model.dao.impl.DepartmentDAOJDBC;
import model.dao.impl.SellerDAOJDBC;

public class DaoFactory {
	
	public static SellerDAO createSellerDAO() {
		return new SellerDAOJDBC(DB.getConnection());
	}
	
	public static DepartmentDAO createDepartmentDAO() {
		return new DepartmentDAOJDBC(DB.getConnection());
	}

}
