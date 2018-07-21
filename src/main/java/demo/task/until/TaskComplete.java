package demo.task.until;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;

import demo.entity.User;
import demo.service.UserService;

public class TaskComplete 
{
   
	
	@Autowired
	private TaskService taskService;
	@Autowired
	private UserService userService;
	
	public void CompleteTask(String processInstanceId)
	{
//		String processInstanceId="100001";
//		//通过用户传送过来的实例id，查询正在执行的任务
//		Task task1=taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
//		System.out.println(task1.getId());
//		//修改业务里面的state字段
//		User user=new User();
//		user.setProcessinstance(processInstanceId);
//		user.setState("待副经理审批");
//		//更新数据
//		userService.updateState(user);
//	//批量提交所有的任务
//		taskService.complete(task1.getId());
	}
	
}
