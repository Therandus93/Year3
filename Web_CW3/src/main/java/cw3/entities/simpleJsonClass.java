package cw3.entities;

import java.util.Vector;

public class simpleJsonClass {

	private String name;
	private Vector<simpleJsonClass> subclasses;
	private Vector<simpleProperty> properties;
	private int level;
	
	public simpleJsonClass(String n, int l) {
		name = n;
		subclasses = new Vector<simpleJsonClass>();
		properties = new Vector<simpleProperty>();
		level = l;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Vector<simpleJsonClass> getSubclasses() {
		return subclasses;
	}
	public void setSubclasses(Vector<simpleJsonClass> subclasses) {
		this.subclasses = subclasses;
	}
	public void addSubClass(simpleJsonClass subclass) {
		this.subclasses.add(subclass);
	}
	public Vector<simpleProperty> getProperties() {
		return properties;
	}
	public void setProperties(Vector<simpleProperty> properties) {
		this.properties = properties;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
}
