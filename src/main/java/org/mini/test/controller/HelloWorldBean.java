package org.mini.test.controller;

import org.mini.beans.factory.annotation.Autowired;
import org.mini.test.entity.User;
import org.mini.test.service.BaseService;
import org.mini.web.bind.annotation.RequestMapping;
import org.mini.web.bind.annotation.ResponseBody;
import org.mini.web.servlet.ModelAndView;

import java.util.Date;

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
  public ModelAndView doTest5(User user) {
    ModelAndView mav = new ModelAndView("test","msg",user.getName());
    return mav;
  }
  @RequestMapping("/test6")
  public String doTest6(User user) {
    return "error";
  }

  @RequestMapping("/test7")
  @ResponseBody
  public User doTest7(User user) {
    user.setName(user.getName() + "---");
    user.setBirthday(new Date());
    return user;
  }

}