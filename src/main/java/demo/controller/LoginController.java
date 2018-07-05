package demo.controller;

import demo.entity.ActUser;
import demo.entity.Manager;
import demo.service.ManagerService;
import demo.service.UserService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Author:许清远
 * Data:2018/6/26
 * Description:
 */
@Controller
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    ManagerService managerService;

    @Autowired
    IdentityService identityService;

    @PostMapping("/login")
    @ResponseBody
    public String login(HttpSession session,String username,String password) {
        try {
            ActUser actUser = new ActUser();
            actUser.setID_(username);
            actUser.setPWD_(password);
            ActUser user = userService.queryUserByPo(actUser);

            if(user!=null && user.getID_().equals(username)) {
                Group group = identityService.createGroupQuery()
                        .groupMember(username)
                        .singleResult();
                session.setAttribute("username",username);
                //将 username 置入 session

                return group.getId();   //group.getId()//如果存在User则返回组名，如 “业务员” 等。
            }

            Manager manager = new Manager();
            manager.setUsername(username);
            manager.setPassword(password);
            Manager mng = managerService.queryManagerByUsername(manager);

            if (mng!=null && mng.getUsername().equals(username)) return "manager";  //如果是管理人员则返回“manager”

            else return "error";   //未查到则返回“error”
        }catch (Exception e){
            e.printStackTrace();
            return "SystemError";  //异常返回"SystemError"
        }

    }

    @GetMapping("/exit")
    @ResponseBody
    public String exit(HttpSession session){
        try {
            session.invalidate();
            return "success";    //退出登录成功
        }catch (Exception e){
            e.printStackTrace();
            return "error";     //退出登录异常或失败
        }
    }

}
