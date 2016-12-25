package cw3.entities;

import java.util.Vector;

public class JsonClass {

	private String name;
	private Vector<JsonClass> subclasses;
	private Vector<Property> properties;
	private int level = 0;
	
	public JsonClass(String n) {
		name = n;
		subclasses = new Vector<JsonClass>();
		properties = new Vector<Property>();
	}
	
	public JsonClass(String n,Vector<JsonClass> sc) {
		name = n;
		subclasses = sc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vector<JsonClass> getSubClasses() {
		return subclasses;
	}

	public void setSubClasses(Vector<JsonClass> subclasses) {
		this.subclasses = subclasses;
	}
	
	public void addSubClass(JsonClass jsonclass) {
		subclasses.addElement(jsonclass);
	}

	public Vector<Property> getProperties() {
		return properties;
	}

	public void setProperties(Vector<Property> properties) {
		this.properties = properties;
	}
	
	public void addProperty(Property p) {
		properties.addElement(p);
	}
	
	public void deleteProperty(Property p) {
		properties.remove(p);
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
