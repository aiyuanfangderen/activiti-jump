package demo.controller;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Author:许清远
 * Data:2018/6/26
 * Description:
 */
@Controller
public class ManagerController {

    @Autowired
    IdentityService identityService;

    @PostMapping("/createMembership")
    @ResponseBody
    public String createMembership(String userId,String password,String groupId,String firstName,String lastName,String email){

        try {
            if (userId == null) return "NoUserId";
            User user = identityService.newUser(userId);
            if (password == null) return "NoPassword";
            else user.setPassword(password);
            if (email != null) user.setEmail(email);
            if (firstName != null) user.setFirstName(firstName);
            if (lastName != null) user.setLastName(lastName);
            identityService.saveUser(user);
            identityService.createMembership(userId,groupId);
        }catch (Exception e){
            e.printStackTrace();
            return "error";    //关系建立失败返回 error
        }
        return "success";   //关系建立成功返回 success
    }

}