package demo.controller;

import demo.entity.ActUser;
import demo.entity.Manager;
import demo.service.ManagerService;
import demo.service.UserService;
import demo.until.Result;
import demo.until.ResultCode;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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
    public Result login(HttpSession session,String username,String password) {
        try {
            Result result = new Result();
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

                result.setResultCodeAndData(ResultCode.LOGIN_SUCCESS,group.getId());
                return result;
            }

            Manager manager = new Manager();
            manager.setUsername(username);
            manager.setPassword(password);
            Manager mng = managerService.queryManagerByUsername(manager);

            if (mng!=null && mng.getUsername().equals(username)){
                result.setResultCodeAndData(ResultCode.LOGIN_SUCCESS,"manager");
                return result;
            }

            result.setResultCode(ResultCode.LOGIN_FAIL);

            return result;
        }catch (Exception e){
            e.printStackTrace();
            return new Result(ResultCode.LOGIN_FAIL);
        }

    }

    @GetMapping("/exit")
    @ResponseBody
    public Result exit(HttpSession session){
        try {
            session.invalidate();
            return new Result(ResultCode.SUCCESS);    //退出登录成功
        }catch (Exception e){
            e.printStackTrace();
            return new Result(ResultCode.ERROR);     //退出登录异常或失败
        }
    }

}
