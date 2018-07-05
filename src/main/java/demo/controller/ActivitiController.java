package demo.controller;

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
	public String deploy(){
		
		repositoryService.createDeployment()
			.addClasspathResource("processes/demo/demo.bpmn")
			.addClasspathResource("processes/demo/demo.png")
			.deploy();
		return "部署成功";

	}

	@GetMapping("/createGroup")
	@ResponseBody
	public String createGroup(){

		identityService.saveGroup(identityService.newGroup("业务员"));
		identityService.saveGroup(identityService.newGroup("副经理"));
		identityService.saveGroup(identityService.newGroup("总经理"));

		return "创建组成功";
	}
}
