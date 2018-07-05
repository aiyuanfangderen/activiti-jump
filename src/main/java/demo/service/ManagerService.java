package demo.service;

import demo.entity.Manager;
import demo.mapper.ManagerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author:许清远
 * Data:2018/6/26
 * Description:
 */
@Service
public class ManagerService {

    @Autowired
    ManagerMapper managerMapper;

    public Manager queryManagerByUsername(Manager manager) throws Exception {
        return managerMapper.queryManagerByPo(manager);
    }

}
