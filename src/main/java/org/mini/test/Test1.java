package org.mini.test;

import org.mini.beans.BeansException;
import org.mini.context.ClassPathXmlApplicationContext;

public class Test1 {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
        try {
            AService aService = (AService) context.getBean("aservice");
            aService.sayHello();
        } catch (BeansException e) {
            throw new RuntimeException(e);
        }


    } 
} 