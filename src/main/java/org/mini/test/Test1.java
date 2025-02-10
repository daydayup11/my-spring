package org.mini.test;

import org.mini.beans.factory.support.BeansException;
import org.mini.context.ClassPathXmlApplicationContext;

public class Test1 {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
        try {
            AService aService = (AService) context.getBean("aservice");
            aService.sayHello();
            BaseService baseService = (BaseService) context.getBean("baseService");
            baseService.sayHello();
        } catch (BeansException e) {
            throw new RuntimeException(e);
        }


    } 
} 