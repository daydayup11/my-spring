package org.mini.test;

import org.mini.web.WebBindingInitializer;
import org.mini.web.WebDataBinder;

import java.util.Date;

public class DateInitializer implements WebBindingInitializer {
	@Override
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(Date.class,"yyyy-MM-dd", false));
		System.out.println("DateInitializer");
	}
}
