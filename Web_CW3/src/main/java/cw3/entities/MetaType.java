package cw3.entities;

public abstract class MetaType {


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MetaType(String name) {
		super();
		this.name = name;
	}
	
	String name;

	
}
