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
import cw3.entities.simpleJsonClass;
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
	
	public Boolean addClass(UMTClass tempclass,Integer level) throws SQLException, NotFoundException {

    	String sql = "INSERT INTO class_hierarchy (CLASSNAME,SUPER_CLASS,LEVEL) VALUES (?,?,?)";
    	
    	PreparedStatement statement;

    	Vector<UMTClass> tmparr = new Vector<UMTClass>();
    	tmparr = tempclass.getSuperClasses();
    	
    	statement = connection.prepareStatement(sql);
    	statement.setString(1, tempclass.getName());
    	statement.setString(2, tmparr.get(0).getName());
    	statement.setInt(3, level);
    	
    	int result = statement.executeUpdate();
    	 
    	if (result == 0) {
    		return false;
    	}

    return true;
    }
	
	public Integer getLevel(String classname) {
		
		String sql = "SELECT LEVEL FROM class_hierarchy WHERE CLASSNAME=?;";
    	int level = 0;
    	
    	PreparedStatement statement;
    	try {
    	statement = connection.prepareStatement(sql);
    	statement.setString(1, classname);
    	ResultSet results;
		results = statement.executeQuery();
		
		if (results.next()) {
    	level = results.getInt(1);  
		}
    	
    	} catch (SQLException e) {
			e.printStackTrace();
		}
		return level;
	}
	
	public Boolean deleteClass(String name) throws SQLException, NotFoundException {

    	String sql = "DELETE FROM property WHERE BELONG_TO_CLASS=?";
    	String sql2 = "DELETE FROM class_hierarchy WHERE CLASSNAME=?";
    	int result = 0;
    	Vector<JsonClass> subclasses = this.getSubClasses(name);
    	
    	PreparedStatement statement;
    	
    	statement = connection.prepareStatement(sql);
    	statement.setString(1, name);
    	result = statement.executeUpdate();
    	
    	statement = connection.prepareStatement(sql2);
    	statement.setString(1, name);
    	result = statement.executeUpdate();
    	 
    	if (result == 0) {
    		return false;
    	}
    	
    	subclasses = this.getSubClasses(name);
    	
    	for (int i = 0;i < subclasses.size();i++) {
    		this.deleteClass(subclasses.get(i).getName());
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
    		switch (results.getString(3)) {
    	
    		case "String":
    			arr.add(new Property(results.getString(2),PRIMITIVE_TYPE.String));
    			break;
    		case "int":
    			arr.add(new Property(results.getString(2),PRIMITIVE_TYPE.Int));
    			break;
    		case "double":
    			arr.add(new Property(results.getString(2),PRIMITIVE_TYPE.Double));
    			break;
    		case "boolean":
    			arr.add(new Property(results.getString(2),PRIMITIVE_TYPE.Boolean));
    			break;
    		default: 
    			arr.add(new Property(results.getString(2),new UMTClass(results.getString(3))));
        	break;
    		}
    	}
    	
    return arr;
    }
    
    public Boolean addPropertyByClass(Property p,String classname) throws SQLException, NotFoundException {

    	String sql = "INSERT INTO `property`(PROPERTY_NAME,TYPE,BELONG_TO_CLASS) VALUES (?,?,?)";
    	Vector<Property> arr = new Vector<Property>();
    	
    	PreparedStatement statement;

    	statement = connection.prepareStatement(sql);
    	statement.setString(1, p.getPropertyName());
    	statement.setString(2, p.getPropertyType().getName());
    	statement.setString(3, classname);
    	
    	int results = statement.executeUpdate();
    	
    	if (results != 0) {
    		return true;
    	}
    	
    return false;
    }
    
    public Boolean deletePropertyByClass(String pname,String classname) throws SQLException, NotFoundException {

    	String sql = "DELETE FROM `property` WHERE PROPERTY_NAME=? AND BELONG_TO_CLASS=?";
    	Vector<Property> arr = new Vector<Property>();
    	
    	PreparedStatement statement;

    	statement = connection.prepareStatement(sql);
    	statement.setString(1, pname);
    	statement.setString(2, classname);
    	
    	int results = statement.executeUpdate();
    	
    	if (results != 0) {
    		return true;
    	}
    	
    return false;
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
    
    public Vector<Vector<String>> getTreeMap() throws SQLException, NotFoundException{
    	
    	String sql = "SELECT * FROM class_hierarchy;";
    	Vector<Vector<String>> arr = new Vector<Vector<String>>();
    	
    	PreparedStatement statement;

    	statement = connection.prepareStatement(sql);
    	
    	ResultSet results = statement.executeQuery();
    	Vector<String> tmp = new Vector<String>();
    	
    	tmp.add("Location");
    	tmp.add("Parents");
    	arr.add(tmp); 
    	
    	while(results.next()) {
    		tmp = new Vector<String>();
    		tmp.add(results.getString(2));
    		tmp.add(results.getString(3));
    		arr.add(tmp);    
    	}
    	
    return arr;
    	
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
    
    public Vector<simpleJsonClass> getSimpleSubClasses(String classname) throws SQLException, NotFoundException {

    	String sql = "SELECT CLASSNAME,LEVEL FROM class_hierarchy WHERE SUPER_CLASS=?;";
    	Vector<simpleJsonClass> arr = new Vector<simpleJsonClass>();
    	
    	PreparedStatement statement;

    	statement = connection.prepareStatement(sql);
    	statement.setString(1, classname);
    	
    	ResultSet results = statement.executeQuery();
    	
    	while(results.next()) {
    	arr.add(new simpleJsonClass(results.getString(1),results.getInt(2)));    
    	}
    	
    return arr;
    }
}
