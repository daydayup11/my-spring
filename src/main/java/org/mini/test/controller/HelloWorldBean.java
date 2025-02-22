package org.mini.test.controller;

import org.mini.beans.factory.annotation.Autowired;
import org.mini.test.entity.User;
import org.mini.test.service.BaseService;
import org.mini.web.RequestMapping;

public class HelloWorldBean {
  @Autowired
  BaseService baseService;

  @RequestMapping("/test1")
  public String doTest1() {
    return "test 1, hello world!";
  }
  @RequestMapping("/test2")
  public String doTest2() {
    return "test 2, hello world!";
  }
  @RequestMapping("/test3")
  public String doTest3() {
    return baseService.getHello();
  }
  @RequestMapping("/test4")
  public String doTest4(User user) {
    return user.getId() +" "+user.getName() + " " + user.getBirthday();
  }

  @RequestMapping("/test5")
  public String doTest5(Integer id) {
    return id +"";
  }

}