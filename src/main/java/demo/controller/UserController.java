package demo.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mysql.fabric.xmlrpc.base.Data;

import demo.entity.User;
import demo.service.UserService;

@Controller
public class UserController 
{
    @Autowired
	private UserService userService;
   
    @RequestMapping("/insertInformation")
    //将我的要提交信息保存到数据库
   public void insertInformation()
   {
	   User user=new User();
	   user.setTitle("请假");
	   user.setReason("生病了");
	  // user.setTime(new Date());
	   user.setUserid("001");
	   user.setState("未提交");
	   userService.insertUserInformation(user);
	   System.out.println("保存成功");
   }
   
   
   //修改未审核过得任务
   @RequestMapping("updateInformation")
   public void updateInformation()
   {
	   User user=new User();
	 
	   user.setId(1);
	   user.setReason("生大病了");
	   user.setState("未提交");
	   userService.updateUser(user);
	   System.out.println("修改成功");
   }
   
   
   
	
	
}
