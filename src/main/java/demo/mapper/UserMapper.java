package demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import demo.dto.ForSelect;
import demo.entity.HiAct;
import demo.entity.HiTask;
import demo.entity.Log;
import demo.entity.RuExecution;
import demo.entity.RuTask;
import demo.entity.User;
@Mapper
public interface UserMapper
{
    public void insertUserInformation(User user);
    
    public void insertUserState(User user);
    
    public void updateUser(User user);
    
    
    //修改正在执行任务表里面的数据
    public void updateRuTask(RuTask ruTask);
    
    //修改执行对象表
    public void updateRuExecution(RuExecution ruExecution);
    
    //删除历史任务表的数据
    public void deleteHisTask(String ID_);
    
    //修改历史流程表里面的数据
    public void updateHisTask(HiTask hiTask);
    
    //删除历史节点表
    public void deleteHisAct(String ID_);
    
    //修改历史节点表信息
    public void updateHisAct(HiAct hiAct);    
    
    
    //批量处理的情况
    
    //查找当前任务的执行对象
    public String findRuExecutionId(String id);
    
    //查询上一个任务节点的信息
    public ForSelect findBeforeTaskInformation(ForSelect forSelect);
    
    //在流程启动时，插入用户的信息
    public void insertUser(User user);
    
    //查询业务员的所有业务
    public List<User> findAllOperation();
    
    //修改user业务表的state字段
    public void updateState(User user);
    
    //通过当前的任务id查找对应的实例id
    public String findProcessinstance(String ID_);
    
    
    //日志记录
    public void LogRecord(Log log);
    
    
    
    
}
