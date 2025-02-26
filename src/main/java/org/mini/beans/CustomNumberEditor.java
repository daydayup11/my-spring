package org.mini.beans;

import org.mini.util.NumberUtils;
import org.mini.util.StringUtils;

import java.text.NumberFormat;

public class CustomNumberEditor implements PropertyEditor {
	private Class<? extends Number> numberClass;
	private NumberFormat numberFormat;
	private boolean allowEmpty;
	private Object value;
	
	public CustomNumberEditor(Class<? extends Number> numberClass,
			  boolean allowEmpty) throws IllegalArgumentException {
		this(numberClass, null, allowEmpty);
	}
	
	public CustomNumberEditor(Class<? extends Number> numberClass,
			 NumberFormat numberFormat, boolean allowEmpty) throws IllegalArgumentException {
		this.numberClass = numberClass;
		this.numberFormat = numberFormat;
		this.allowEmpty = allowEmpty;
	}
	
	@Override
	public void setAsText(String text) {
		if (this.allowEmpty && !StringUtils.hasText(text)) {
			// Treat empty String as null value.
			setValue(null);
		}
		else if (this.numberFormat != null) {
			// 使用自定义 NumberFormat for parsing text.
			setValue(NumberUtils.parseNumber(text, this.numberClass, this.numberFormat));
		}
		else {
			//使用默认NumberFormat for parsing text.
			setValue(NumberUtils.parseNumber(text, this.numberClass));
		}
	}

	@Override
	public void setValue(Object value) {
		if (value instanceof Number) {
			this.value = (NumberUtils.convertNumberToTargetClass((Number) value, this.numberClass));
		}
		else {
			this.value = value;
		}
	}

	@Override
	public String getAsText() {
		Object value = this.value;
		if (value == null) {
			return "";
		}
		if (this.numberFormat != null) {
			// Use NumberFormat for rendering value.
			return this.numberFormat.format(value);
		}
		else {
			// Use toString method for rendering value.
			return value.toString();
		}
	}

	@Override
	public Object getValue() {
		return this.value;
	}

}
