package demo.task.until;

import java.math.BigInteger;
import java.sql.Timestamp;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import demo.dto.ForSelect;
import demo.entity.HiAct;
import demo.entity.HiTask;
import demo.entity.RuExecution;
import demo.entity.RuTask;
import demo.entity.User;
import demo.service.UserService;

@Component
@Transactional
public class TaskReturn
{
	
	@Autowired
	RepositoryService repositoryService;

	@Autowired
	IdentityService identityService;

	@Autowired
	TaskService taskService;
	
	@Autowired
	RuntimeService runtimeService;
	
	@Autowired
	HistoryService historyService;
	
	@Autowired
	private UserService userService;
	
	
	
   
	public void ReturnTask(Task task)
	{
		
		 //0回退前将原来的user业务表的state字段改变为未提交
	    //通过当前的任务查找实例id
	 String processInstanceId=userService.findProcessinstance(task.getId());
	    //通过实例id找到业务表对应的行进行更新数据
	  System.out.println("实例id为:"+processInstanceId);
	    User user=new User();
		user.setProcessinstance(processInstanceId);
		user.setState("未提交");
		userService.updateState(user);
		
		
	
	//1查询当前任务的执行对象id
	String executionId=userService.findRuExecutionId(task.getId());
	System.out.println(executionId);
	
	
	//2通过执行对象id去历史任务表查询相同的历史任务id和表的NAME_属性去判断是不是上一个任务（此属性为业务员，即副经理的上一级），历史任务id（此id即为上一个任务节点的id）
	//和TASK_DEF_KEY_（后面需要用到）
	ForSelect forSelect=new ForSelect();
	forSelect.setEXECUTION_ID_(executionId);
	forSelect.setNAME_("业务员");
	ForSelect forSelect2=userService.findBeforeTaskInformation(forSelect);
	System.out.println(forSelect2.getID_());
	System.out.println(forSelect2.getNAME_());
	System.out.println(forSelect2.getEXECUTION_ID_());
	System.out.println(forSelect2.getTASK_DEF_KEY_());
	System.out.println(forSelect2.getASSIGNEE_());
	
	//3修改正在执行的任务表
	   //3.1通过当前当前任务id查找要修改的正在执行的任务表
	   //3.2再通过当前任务对应的前一个任务id去查找上一个任务的ID_，NAME_，TASK_DEF_KEY_，ASSIGNEE_这些字段
	   //3.3将这些字段对应的数据保存到正在执行的任务表的对应的字段
	RuTask rutask=new RuTask();
	rutask.setID_(task.getId());
	rutask.setNAME_(forSelect2.getNAME_());
	rutask.setTASK_DEF_KEY_(forSelect2.getTASK_DEF_KEY_());
	rutask.setASSIGNEE_(forSelect2.getASSIGNEE_());
	rutask.setBeforeId(forSelect2.getID_());
	
	String s="2018-07-20 18:11:25.221";
	//DateTime time = DateTime.parse(s);
	
	Timestamp timestamp=Timestamp.valueOf(s);
	rutask.setCREATE_TIME_(timestamp);
	
	userService.updatemyRuTask(rutask);
	
	 System.out.println("更新正在运行任务表成功");
	   
	
	
	//4修改执行对象表
	       //4.1通过执行对象id查找对应的执行对象表
	        
	       //4.2修改这个执行对象表，将里面的REV_字段改为1，将上面查到的数据TASK_DEF_KEY_修改到该数据表
	    RuExecution ruExecution=new RuExecution();
		ruExecution.setID_(executionId);
		ruExecution.setREV_(1);
		ruExecution.setACT_ID_(forSelect2.getTASK_DEF_KEY_());
		
		userService.updateRuExecution(ruExecution);
		System.out.println("修改成功");
	
		
	//5删除历史任务表
	     //5.1通过当前的任务id去删除历史任务表
		String id=task.getId();
		userService.deleteHisTask(id);
		System.out.println("删除成功");
	
		
		
		
		
	//6修改历史任务表信息
	     //6.1将上一个任务的DURATION_设置为0，END_TIME_设置为未结束
		HiTask hiTask=new HiTask();
		hiTask.setID_(forSelect2.getID_());
		//将字符串转化为大数字型
		BigInteger bigInteger=new BigInteger(String.valueOf(0));
		hiTask.setDURATION_(bigInteger);
		// s="2018-07-20 18:11:25.221";
		String s2="1900-01-01 12:00:00.000";
	
		Timestamp timestamp2=Timestamp.valueOf(s2);
		hiTask.setEND_TIME_(timestamp2);
		
		userService.updateHisTask(hiTask);
		System.out.println("修改成功");
		
		
		
		
	
	//7删除历史节点表
	    //7.1通过当前任务id，删除历史节点表对应的信息
	
		String id2=task.getId();
		userService.deleteHisAct(id2);
		System.out.println("删除成功");
	
	//8修改历史节点表
	    //8.1将上一个任务的DURATION_设置为0，END_TIME_设置为未结束
		HiAct hiAct=new HiAct();
		
		hiAct.setID_(forSelect2.getID_());
		
		BigInteger bigInteger2=new BigInteger(String.valueOf(0));
		hiAct.setDURATION_(bigInteger2);
		
		String s3="1900-01-01 12:00:00.000";
		Timestamp timestamp3=Timestamp.valueOf(s3);
		hiAct.setEND_TIME_(timestamp3);
		userService.updateHisAct(hiAct);
		
		System.out.println("修改历史节点表成功");
		
		
	}
	
	
}
