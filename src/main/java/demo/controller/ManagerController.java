package demo.controller;

import demo.dto.MembershipDto;
import demo.until.Result;
import demo.until.ResultCode;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public Result createMembership(@RequestBody MembershipDto membershipDto){

        try {
            if (membershipDto.getGroupId() == null) return new Result(ResultCode.ERROR,"NoGroupId");
            if (membershipDto.getUserId() == null) return new Result(ResultCode.ERROR,"NoUsername");
            User user = identityService.newUser(membershipDto.getUserId());
            if (membershipDto.getPassword() == null) return new Result(ResultCode.ERROR,"NoPassword");
            else user.setPassword(membershipDto.getPassword());
            if (membershipDto.getEmail() != null) user.setEmail(membershipDto.getEmail());
            if (membershipDto.getFirstName() != null) user.setFirstName(membershipDto.getFirstName());
            if (membershipDto.getLastName() != null) user.setLastName(membershipDto.getLastName());
            identityService.saveUser(user);
            identityService.createMembership(membershipDto.getUserId(),membershipDto.getGroupId());
        }catch (Exception e){
            e.printStackTrace();
            return new Result(ResultCode.ERROR);    //关系建立失败返回 error
        }
        return new Result(ResultCode.SUCCESS);   //关系建立成功返回 success
    }

}
