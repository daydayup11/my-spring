package org.mini.test.service;

import org.mini.beans.factory.annotation.Required;

public class BaseBaseService {

    private AServiceImpl as;
    public BaseBaseService() {
    }
    public AServiceImpl getAs() {
        return as;
    }

    @Required
    public void setAs(AServiceImpl as) {
        this.as = as;
    }

    public void sayHello() {
        System.out.println("BaseBaseService sayHello");
    }
}