package org.mini.beans;

import org.mini.beans.factory.config.PropertyValue;
import org.mini.beans.factory.config.PropertyValues;

public abstract class AbstractPropertyAccessor extends PropertyEditorRegistrySupport{

	PropertyValues pvs;
	
	public AbstractPropertyAccessor() {
		super();

	}

	
	public void setPropertyValues(PropertyValues pvs) {
		this.pvs = pvs;
		for (PropertyValue pv : this.pvs.getPropertyValues()) {
			setPropertyValue(pv);
		}
	}
	
	public abstract void setPropertyValue(PropertyValue pv) ;

}
