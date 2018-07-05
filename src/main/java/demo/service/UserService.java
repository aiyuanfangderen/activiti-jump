package demo.service;

import demo.entity.ActUser;
import demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author:许清远
 * Data:2018/6/26
 * Description:
 */
@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public ActUser queryUserByPo(ActUser user) throws Exception {
        return userMapper.queryUserByPo(user);
    }

}
