package org.mini.test;

import org.mini.beans.factory.annotation.Autowired;

public class BaseService {
    @Autowired
    private BaseBaseService baseBaseService;
    public BaseBaseService getBbs() {
        return baseBaseService;
    }
    public void setBbs(BaseBaseService bbs) {
        this.baseBaseService = bbs;
    }
    public BaseService() {
    }
    public void sayHello() {
        System.out.println("Base Service says Hello");
        baseBaseService.sayHello();
    }
}