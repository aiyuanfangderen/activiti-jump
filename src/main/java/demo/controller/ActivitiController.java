package demo.controller;

import demo.until.Result;
import demo.until.ResultCode;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class ActivitiController {

	@Autowired
	RepositoryService repositoryService;

	@Autowired
	IdentityService identityService;

	@Autowired
	TaskService taskService;

	@GetMapping("/deploy")
	@ResponseBody
	public Result deploy(){
		
		repositoryService.createDeployment()
			.addClasspathResource("processes/demo/demo.bpmn")
			.addClasspathResource("processes/demo/demo.png")
			.deploy();
		return new Result(ResultCode.SUCCESS,"部署成功");

	}

	@GetMapping("/createGroup")
	@ResponseBody
	public Result createGroup(){

		identityService.saveGroup(identityService.newGroup("业务员"));
		identityService.saveGroup(identityService.newGroup("副经理"));
		identityService.saveGroup(identityService.newGroup("总经理"));

		return new Result(ResultCode.SUCCESS,"创建组成功");
	}
}
