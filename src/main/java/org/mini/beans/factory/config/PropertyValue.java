package org.mini.beans.factory.config;

public class PropertyValue {

	private final String type;
	private final String name;
	private final Object value;
	private final boolean isRef;

	public PropertyValue(String type, String name, Object value, boolean isRef) {
		this.type = type;
		this.name = name;
		this.value = value;
		this.isRef = isRef;
	}
	public PropertyValue(String name, Object value) {
		this("", name, value, false);
	}

	public String getType() {
		return this.type;
	}

	public String getName() {
		return this.name;
	}

	public Object getValue() {
		return this.value;
	}

	public boolean getIsRef() {
		return isRef;
	}

}

