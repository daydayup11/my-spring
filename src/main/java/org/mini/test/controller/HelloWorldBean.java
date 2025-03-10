package org.mini.test.controller;

import org.mini.beans.factory.annotation.Autowired;
import org.mini.test.entity.User;
import org.mini.test.service.BaseService;
import org.mini.test.service.IAction;
import org.mini.test.service.UserService;
import org.mini.web.bind.annotation.RequestMapping;
import org.mini.web.bind.annotation.ResponseBody;
import org.mini.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class HelloWorldBean {
  @Autowired
  BaseService baseService;
  @Autowired
  UserService userService;
  @Autowired
  IAction action1;
  @Autowired
  IAction action2;

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
  @RequestMapping("/test8")
  @ResponseBody
  public User doTest8(User user) {
    User users = userService.getUserInfo(user.getId());
    return users;
  }
  @RequestMapping("/test9")
  @ResponseBody
  public User doTest9(User user) {
    User users = userService.getUserInfoBatis(user.getId());
    return users;
  }
  @RequestMapping("/test10")
  @ResponseBody
  public int doTest10(User user) {
    return userService.deleteUserInfoBatis(user.getId());
  }
  @RequestMapping("/test11")
  @ResponseBody
  public int doTest11(User user) {
    return userService.updateUserInfoBatis(user.getId(), user.getName());
  }
  @RequestMapping("/testaop")
  public void doTestAop() {

    //DynamicProxy proxy = new DynamicProxy(action);
    //IAction p = (IAction)proxy.getProxy();

    action1.doAction();
    action2.doAction();
    action1.doSomething();
    action2.doSomething();

  }
}