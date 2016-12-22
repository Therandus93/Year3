package cw3.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;

import cw3.entities.Instance;
import cw3.entities.JsonClass;
import cw3.entities.MetaType;
import cw3.entities.PRIMITIVE_TYPE;
import cw3.entities.PrimitiveType;
import cw3.entities.Property;
import cw3.entities.UMTClass;
import javassist.NotFoundException;

public class ClassDAO {

	private Connection connection;
	
	public ClassDAO() {
		 try {
	            Class.forName("com.mysql.jdbc.Driver");
	            connection = DriverManager.getConnection("jdbc:mysql://" + System.getProperty("db_host") + ":"
	                    + System.getProperty("db_port") + "/" + System.getProperty("db_name") + "?useSSL=false",
	                    System.getProperty("db_user") , System.getProperty("db_pass"));

	        } catch (Exception e) {
	            System.err.println("ERROR: failed to get database connection.");
	            e.printStackTrace();
	        }
	}
	
	public Vector<String> getClasses() throws SQLException, NotFoundException {

    	String sql = "SELECT DISTINCT CLASSNAME FROM class_hierarchy;";
    	Vector<String> arr = new Vector<String>();
    	
    	PreparedStatement statement;
    	statement = connection.prepareStatement(sql);
    	ResultSet results = statement.executeQuery();
    	
    	while (results.next()) {
    		arr.add(results.getString(1));  
    	}

    return arr;
    }
	
	public Boolean addClass(UMTClass tempclass) throws SQLException, NotFoundException {

    	String sql = "INSERT INTO class_hierarchy (CLASSNAME,SUPER_CLASS) VALUES (?,?";
    	
    	PreparedStatement statement;

    	Vector<UMTClass> tmparr = new Vector<UMTClass>();
    	tmparr = tempclass.getSuperClasses();
    	
    	if (tmparr.size() > 1) {
    		for (int i = 0;i < tmparr.size();i++) {
    			sql += ",?";
    		}
    	}
    	sql += ");";

    	
    	statement = connection.prepareStatement(sql);
    	statement.setString(1, tempclass.getName());
    	
    	for (int i = 0;i < tmparr.size();i++) {
    		statement.setString(2+i, tmparr.get(i).getName());
    	}
    	
    	int result = statement.executeUpdate();
    	 
    	if (result == 0) {
    		return false;
    	}

    return true;
    }
	
	public Boolean deleteClass(String name) throws SQLException, NotFoundException {

    	String sql = "DELETE FROM property WHERE BELONG_TO_CLASS=?";
    	String sql2 = "DELETE FROM class_hierarchy WHERE CLASSNAME=? OR SUPER_CLASS=?";
    	int result = 0;
    	
    	PreparedStatement statement;
    	
    	statement = connection.prepareStatement(sql);
    	statement.setString(1, name);
    	statement.executeUpdate();
    	
    	statement = connection.prepareStatement(sql2);
    	statement.setString(1, name);
    	statement.setString(2, name);
    	statement.executeUpdate();
    	 
    	if (result == 0) {
    		return false;
    	}

    return true;
    }

	//Needs name chaning (for instances)
    public MetaType getTypeByClass(String instancename) throws SQLException, NotFoundException {

    	String sql = "SELECT * FROM `property` WHERE BELONG_TO_CLASS=?";
    	MetaType temptype = null;
    	
    	PreparedStatement statement;

    	statement = connection.prepareStatement(sql);
    	statement.setString(1, instancename);

    	ResultSet results = statement.executeQuery();

    
    

    return temptype;
    }
    
    public Vector<Property> getPropertiesByClass(String classname) throws SQLException, NotFoundException {

    	String sql = "SELECT * FROM `property` WHERE BELONG_TO_CLASS=?";
    	Vector<Property> arr = new Vector<Property>();
    	
    	PreparedStatement statement;

    	statement = connection.prepareStatement(sql);
    	statement.setString(1, classname);
    	
    	ResultSet results = statement.executeQuery();
    	
    	while (results.next()) {
    		switch (results.getString(2)) {
    	
    		case "String":
    			arr.add(new Property(results.getString(1),PRIMITIVE_TYPE.String));
    			break;
    		case "int":
    			arr.add(new Property(results.getString(1),PRIMITIVE_TYPE.Int));
    			break;
    		case "double":
    			arr.add(new Property(results.getString(1),PRIMITIVE_TYPE.Double));
    			break;
    		case "boolean":
    			arr.add(new Property(results.getString(1),PRIMITIVE_TYPE.Boolean));
    			break;
    	
    		}
    	}
    	
    return arr;
    }
    
    public Integer countProperties(String classname) throws SQLException, NotFoundException {

    	String sql = "SELECT Count(*) FROM `property` WHERE BELONG_TO_CLASS=?";
    	Integer count = 0;
    	
    	PreparedStatement statement;

    	statement = connection.prepareStatement(sql);
    	statement.setString(1, classname);

    	ResultSet results = statement.executeQuery();
    	results.next();
    	count = results.getInt(1);

    return count;
    }
    
    public Vector<UMTClass> getSuperClasses(String classname) throws SQLException, NotFoundException {

    	String sql = "SELECT SUPER_CLASS FROM class_hierarchy WHERE CLASSNAME=?;";
    	Vector<UMTClass> arr = new Vector<UMTClass>();
    	
    	PreparedStatement statement;

    	statement = connection.prepareStatement(sql);
    	statement.setString(1, classname);
    	
    	ResultSet results = statement.executeQuery();
    	
    	while(results.next()) {
    	arr.add(new UMTClass(results.getString(1)));    
    	}
    	
    return arr;
    }
    
    public Vector<JsonClass> getSubClasses(String classname) throws SQLException, NotFoundException {

    	String sql = "SELECT CLASSNAME FROM class_hierarchy WHERE SUPER_CLASS=?;";
    	Vector<JsonClass> arr = new Vector<JsonClass>();
    	
    	PreparedStatement statement;

    	statement = connection.prepareStatement(sql);
    	statement.setString(1, classname);
    	
    	ResultSet results = statement.executeQuery();
    	
    	while(results.next()) {
    	arr.add(new JsonClass(results.getString(1)));    
    	}
    	
    return arr;
    }
}
