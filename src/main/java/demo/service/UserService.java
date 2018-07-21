package demo.service;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demo.dto.ForSelect;
import demo.entity.HiAct;
import demo.entity.HiTask;
import demo.entity.Log;
import demo.entity.RuExecution;
import demo.entity.RuTask;
import demo.entity.User;
import demo.mapper.UserMapper;

@Service
public class UserService 
{
   @Autowired
	private UserMapper userMapper;
	
	public void insertUserInformation(User user)
	
	{
		 userMapper.insertUserInformation(user);
	}
	
	public void insertUserState(User user)
	{
		userMapper.insertUserState(user);
	}
	
	
	public void updateUser(User user)
	{
		userMapper.updateUser(user);
	}
	
	
	//修改正在执行任务的表
	
	public void updatemyRuTask(RuTask ruTask)
	{
		userMapper.updateRuTask(ruTask);
	}
	
	//修改执行对象表
	public void updateRuExecution(RuExecution ruExecution)
	{
		userMapper.updateRuExecution(ruExecution);
	}
	
	//删除历史任务表数据
	public void deleteHisTask(String ID_)
	{
		userMapper.deleteHisTask(ID_);
	}
	
	//修改历史流程表里面的数据
	public void updateHisTask(HiTask hiTask)
	{
		userMapper.updateHisTask(hiTask);
	}
	
	
	//删除历史节点表里面的数据
	public void deleteHisAct(String ID_)
	{
		userMapper.deleteHisAct(ID_);
	}
	
	//修改历史节点表里面的数据
	public void updateHisAct(HiAct hiAct)
	{
		userMapper.updateHisAct(hiAct);
	}
	
	
	//批量处理的情况
	
	//查找当前任务的执行对象
	public String findRuExecutionId(String id)
	{
		return userMapper.findRuExecutionId(id);
	}
	
	
	//查询上一个任务节点的信息
	public ForSelect findBeforeTaskInformation(ForSelect forSelect)
	{
		return userMapper.findBeforeTaskInformation(forSelect);
	}
	
	//在启动流程时插入用户的相关信息
	public void insertUser(User user)
	{
		 userMapper.insertUser(user);
	}
	
	//查询业务员的所有业务
	public List<User> findAllOperation()
	{
		 return userMapper.findAllOperation();
	}
	
	//修改user表的state字段
	public void updateState(User user)
	{
		userMapper.updateState(user);
	}
	
	//通过当前的任务id查找对应的实例id
	public String findProcessinstance(String ID_)
	{
		 return userMapper.findProcessinstance(ID_);
	}
	
	
	//用于日志记录
	public void LogRecord(Log log)
	{
		userMapper.LogRecord(log);
	}
	
}
