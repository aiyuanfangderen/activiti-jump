package demo.mapper;

import demo.entity.ActUser;
import demo.entity.Manager;
import org.apache.ibatis.annotations.Mapper;

/**
 * Author:许清远
 * Data:2018/6/26
 * Description:
 */
@Mapper
public interface ManagerMapper {

    public Manager queryManagerByPo(Manager manager) throws Exception;

}
