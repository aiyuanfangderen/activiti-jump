package demo.mapper;

import demo.entity.ActUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * Author:许清远
 * Data:2018/6/26
 * Description:
 */
@Mapper
public interface UserMapper {

    public ActUser queryUserByPo(ActUser user) throws Exception;

}
