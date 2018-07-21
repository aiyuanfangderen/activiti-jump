package demo.controller;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.intThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.ibatis.annotations.Update;
import org.apache.tomcat.util.buf.C2BConverter;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysql.fabric.xmlrpc.base.Data;

import demo.dto.ForSelect;
import demo.entity.HiAct;
import demo.entity.HiTask;
import demo.entity.Log;
import demo.entity.RuExecution;
import demo.entity.RuTask;
import demo.entity.User;
import demo.service.UserService;
import demo.task.until.TaskComplete;
import demo.task.until.TaskReturn;


@Controller
public class ActivitiController {

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
	
	@Autowired
	private TaskReturn taskReturn;
	
//	@Autowired 
//	private TaskComplete taskcomplete;
	
	
	
	//部署一个流程
	@RequestMapping("/addDeploy")
	public void addDeploy()
	{
		
		repositoryService.createDeployment()
		   .name("测试批量跳转")
			.addClasspathResource("processes/demo/JumpTest.bpmn")
			.addClasspathResource("processes/demo/JumpTest.png")
			.deploy();  
		System.out.println("成功");
	}
	
	//删除一个流程定义
	@RequestMapping("/deleteDeploy")
	public void deleteDeploy()
	{
		String deployment="67501";	
		repositoryService.deleteDeployment(deployment, true);		
		System.out.println("删除部署的流程成功");
	}
	
	//启动一个流程
	@RequestMapping("/startProcessInstance")
	public void startProcessInstance()
	{
		//将除流程实例以外的信息传过来
		
		User user=new User();
		user.setUserid("张三");
		user.setTitle("请假");
		user.setReason("腿不好");
		user.setState("未提交");
		//流程定义的key
		String processDefinitionKey = "jumptest";
		ProcessInstance pi =runtimeService.startProcessInstanceByKey(processDefinitionKey);//使用流程定义的key启动流程实例，key对应helloworld.bpmn文件中id的属性值，使用key值启动，默认是按照最新版本的流程定义启动
		
		user.setProcessinstance(pi.getId());
		userService.insertUser(user);
		//日志
		
		
		System.out.println("流程实例ID:"+pi.getId());//流程实例ID    101
		System.out.println("流程定义ID:"+pi.getProcessDefinitionId());//流程定义ID   helloworld:1:4
	}
	
//	//业务员完成他的任务
//	@RequestMapping("/completeTask")
//	public void completeTask()
//	{
//	
//		String taskId = "67509";
//		taskService.complete(taskId);
//		System.out.println("完成任务：任务ID："+taskId);
//	}
//	
//	
//	
//
//	//经理审核任务
//	@RequestMapping("/CompleteTaskManager")
//	public void CompleteTaskManager()
//	{
//		
//		
//		String taskId = "22502";
//		String userId="003";
//		taskService.claim(taskId, userId);
//		
//	    //批注信息
//		String processInstanceId="17505";
//		Authentication.setAuthenticatedUserId("003"); 
//		taskService.addComment(taskId, processInstanceId, "不行，现在公司赶项目");
//		
//		//设置流程变量，判断流程得流向
//		Map<String, Object> variables = new HashMap<String, Object>();
//		variables.put("msg", "不通过");
//		//与正在执行的任务管理相关的Service
//		taskService.complete(taskId,variables);
//		
//		System.out.println("完成任务：任务ID："+taskId);
//	}
//	
	
    //业务员查询他的所有业务
	@RequestMapping("/findAllOperation")
	public void findAllOperation()
	{
		List<User> users= userService.findAllOperation();
		for(User user:users)
		{
			System.out.println(user.getId());
			System.out.println(user.getProcessinstance());
			System.out.println(user.getUserid());	
			System.out.println(user.getReason());
			System.out.println(user.getTitle());
			System.out.println(user.getState());
		}
	}
	
	
	
	
	
	
	//修改正在执行任务表
	@RequestMapping("/updateMyRuTask")
	public void updateMyRuTask()
	{
		RuTask rutask=new RuTask();
		rutask.setID_("70002");
		rutask.setNAME_("业务员");
		rutask.setTASK_DEF_KEY_("usertask6");
		rutask.setASSIGNEE_("张三");
		rutask.setBeforeId("67509");
		
		String s="2018-07-20 18:11:25.221";
		//DateTime time = DateTime.parse(s);
		
		Timestamp timestamp=Timestamp.valueOf(s);
		rutask.setCREATE_TIME_(timestamp);
		
		userService.updatemyRuTask(rutask);
		
		 System.out.println("更新正在运行任务表成功");
	}
	
	
	//修改执行对象表
	@RequestMapping("updateRuExecution")
	public void updateRuExecution()
	{
		RuExecution ruExecution=new RuExecution();
		ruExecution.setID_("67506");
		ruExecution.setREV_(1);
		ruExecution.setACT_ID_("usertask6");
		
		userService.updateRuExecution(ruExecution);
		System.out.println("修改成功");
		
	}
	
	
	//删除历史任务表中的数据
	@RequestMapping("/deleteHisTask")
	public void deleteHisTask()
	{
		String id="70002";
		userService.deleteHisTask(id);
		System.out.println("删除成功");
	}
	
	//修改历史任务表中的数据
	@RequestMapping("/updateHisTask")
	public void updateHisTask()
	{
		HiTask hiTask=new HiTask();
		hiTask.setID_("67509");
		//将字符串转化为大数字型
		BigInteger bigInteger=new BigInteger(String.valueOf(0));
		hiTask.setDURATION_(bigInteger);
		// s="2018-07-20 18:11:25.221";
		String s="1900-01-01 12:00:00.000";
	
		Timestamp timestamp=Timestamp.valueOf(s);
		hiTask.setEND_TIME_(timestamp);
		
		userService.updateHisTask(hiTask);
		System.out.println("修改成功");
		
	}
	
	
	//删除历史节点表
	@RequestMapping("/deleteHisAct")
	public void deleteHisAct()
	{
		String id="70001";
		userService.deleteHisAct(id);
		System.out.println("删除成功");
	}
	
	
	//修改历史节点的信息
	@RequestMapping("/updateHisAct")
	public void updateHisAct()
	{
		HiAct hiAct=new HiAct();
		
		hiAct.setID_("67508");
		
		BigInteger bigInteger=new BigInteger(String.valueOf(0));
		hiAct.setDURATION_(bigInteger);
		
		String s="1900-01-01 12:00:00.000";
		Timestamp timestamp=Timestamp.valueOf(s);
		hiAct.setEND_TIME_(timestamp);
		userService.updateHisAct(hiAct);
		
		System.out.println("修改历史节点表成功");
		
		
	}
	
	
	
	//业务员批量提交业务
	@RequestMapping("/completeLot")
    public void completeLot()
    {
		//前台将选取的任务里面的user表里面所有的字段信息都提交到后台(其中包括 processInstanceId字段)
		//再完成任务
//		for(String processInstanceId:processInstanceIds)
//		{
//          通过前台传过来的list集合的实例id查找对应的任务
//			Task tasks1=taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
//		       将业务中的未提交改为副经理正在审核  
//			taskService.complete(task.getId());
//		}
		
		String processInstanceId="110001";
		//通过用户传送过来的实例id，查询正在执行的任务
		Task task1=taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
		System.out.println(task1.getId());
		//修改业务里面的state字段
		User user=new User();
		user.setProcessinstance(processInstanceId);
		user.setState("待副经理审批");
		//更新数据
		userService.updateState(user);
	//批量提交所有的任务
		taskService.complete(task1.getId());
	
		//直接调用该方法即可
	//taskcomplete.CompleteTask(processInstanceId);
   }

	
	//副经理批量回退当前任务
	    //该回退是在经理没有对业务员的业务做出同意和不同意的评价，即没有流程变量
	@RequestMapping("/returnLo")
	public void returnLot()
	{
		//副经理查询他的所有任务
		List<Task> tasks=taskService.createTaskQuery().taskAssignee("李四").list();
		
		
		//副经理查询他任务的上一个任务
		for(Task task:tasks)
		{
			//抛异常后后面继续执行的情况
			try {
				taskReturn.ReturnTask(task);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
														
		}
		
		
		//抛异常后后面全部不执行情况
//		try {
//			for(Task task:tasks)
//			{		
//					taskReturn.ReturnTask(task);
//																	
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		
		
		
		
		
			
	}
	
	
	
	
	
	
	
	
	//批量回退任务
	   //该情况是在副经理对业务员提交的任务做出不同的评价后做出前进，回退的不同结果，如果业务员存在流程变量则需要去清空业务员里面的流程变量
	   //另外如果进行回退时，中间跳过一个任务节点，而跳过的节点存在流程变量，也需要去清空流程变量
	
	
}
