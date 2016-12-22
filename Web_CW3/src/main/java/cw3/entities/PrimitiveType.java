package cw3.entities;

public class PrimitiveType extends MetaType{
	
	public PrimitiveType(String name, PRIMITIVE_TYPE type) {
		super(type.toString());
		this.type = type;
		
	}

	PRIMITIVE_TYPE type;

}
