package demo.controller;

import demo.dto.FileDto;
import demo.dto.TaskDto;
import demo.entity.TaskPo;
import demo.entity.TaskPoHi;
import demo.service.UserService;
import demo.until.Result;
import demo.until.ResultCode;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Author:许清远
 * Data:2018/6/26
 * Description:
 */
@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    HistoryService historyService;


    //提交申请（不能用于修改申请）
    @PostMapping("/userSubmit")
    @ResponseBody
    public Result userSubmit(HttpSession session, @RequestBody FileDto fileDto){

        try {

            MultipartFile file = fileDto.getFile();
            String fileMsg = fileDto.getFileMsg();

            String processDefinitionKey = "demo";
            String username = (String) session.getAttribute("username");

            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);

            List<Task> list = taskService.createTaskQuery()
                    .processInstanceId(processInstance.getId())
                    .orderByTaskCreateTime().desc()
                    .list();
            Task task = list.get(0);

            //分配任务
            taskService.claim(task.getId(),username);
            Map<String,Object> variables = new HashMap<String, Object>();

            //上传文件
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
            String res = sdf.format(new Date());
            String originalFileName = file.getOriginalFilename();
            String newFileName = username+"-"+res+originalFileName.substring(originalFileName.lastIndexOf("."));
            file.transferTo(new File("C:\\Users\\xuqingyuan\\Desktop\\demo",newFileName));

            variables.put("file","C:\\Users\\xuqingyuan\\Desktop\\demo\\"+newFileName);
            variables.put("display","待副经理审批");
            variables.put("fileMsg",fileMsg);
            variables.put("owner",username);
            taskService.complete(task.getId(),variables);

            return new Result(ResultCode.SUCCESS);                //提交成功返回 success

        }catch (Exception e){
            e.printStackTrace();
            return new Result(ResultCode.ERROR);
        }
    }

    //修改申请（不能用于提交申请）
    @PostMapping("/adjust")
    @ResponseBody
    public Result adjust(HttpSession session,String taskId,MultipartFile file,String fileMsg){
        try {
            String username = (String) session.getAttribute("username");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
            String res = sdf.format(new Date());
            String originalFileName = file.getOriginalFilename();
            String newFileName = username+"-"+res+originalFileName.substring(originalFileName.lastIndexOf("."));
            file.transferTo(new File("C:\\Users\\xuqingyuan\\Desktop\\demo",newFileName));

            //更换文件
            taskService.removeVariable(taskId,"textF");   //删除副经理审批意见
            taskService.removeVariable(taskId,"textZ");   //删除总经理审批意见
            taskService.removeVariable(taskId,"choice");  //删除总经理选择流程
            taskService.setVariable(taskId,"display","待副经理审批");
            taskService.setVariable(taskId,"fileMsg",fileMsg);
            taskService.setVariable(taskId,"file","C:\\Users\\xuqingyuan\\Desktop\\demo\\"+newFileName);

            taskService.complete(taskId);
            return new Result(ResultCode.SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(ResultCode.ERROR);
        }
    }

    //用于查询业务员查询他的申请，返回的是他所有的任务
    @GetMapping("/queryTask")
    @ResponseBody
    public Result queryTask(HttpSession session){

        try {
            String username = (String) session.getAttribute("username");
            List<Task> list = taskService.createTaskQuery()
                    .taskAssignee(username)
                    .list();
            List<TaskPo> taskPoList = new ArrayList<TaskPo>();
            for (Task task:
                 list) {
                TaskPo taskPo = new TaskPo();
                taskPo.setId(task.getId());     //设置任务Id
                Map<String, Object> processVariables = taskService.getVariables(task.getId());
                for (String key:
                     processVariables.keySet()) {
                    if (key.equals("display")) taskPo.setDisplay((String) processVariables.get(key));
                    if (key.equals("textF")) taskPo.setTextF((String) processVariables.get(key));
                    if (key.equals("textZ")) taskPo.setTextZ((String) processVariables.get(key));
                    if (key.equals("fileMsg")) taskPo.setFileMsg((String) processVariables.get(key));
                }
                taskPo.setStartTime(task.getCreateTime().toString());
                taskPo.setName(task.getName());
                taskPoList.add(taskPo);
            }
            return new Result(ResultCode.SUCCESS,taskPoList);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(ResultCode.ERROR);   //异常则返回 null
        }
    }

    @PostMapping("/getFileU")
    @ResponseBody
    public Result getFileU(@RequestBody TaskDto taskDto){
        try {
            String taskId =taskDto.getTaskId();
            String fileName = (String) taskService.getVariable(taskId, "file");
            File file = new File(fileName);
            String rootName = "C:\\Users\\xuqingyuan\\Desktop\\downywy\\"+fileName.substring(fileName.lastIndexOf("\\"));
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(rootName,true));
            int len = 0;
            while((len = inputStream.read()) != -1){
                out.write(len);
                out.flush();
            }
            out.close();
            return new Result(ResultCode.SUCCESS,rootName);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(ResultCode.ERROR);
        }
    }

    //用于放弃流程实例（不再提交申请）
    @PostMapping("/quitTask")
    @ResponseBody
    public Result quitTask(@RequestBody TaskDto taskDto){
        String taskId = taskDto.getTaskId();
        try {
            Task task = taskService.createTaskQuery()
                    .taskId(taskId)
                    .singleResult();
            taskService.setVariable(taskId,"display","已放弃");
            String processInstanceId = task.getProcessInstanceId();
            runtimeService.deleteProcessInstance(processInstanceId,"无");
            return new Result(ResultCode.SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(ResultCode.ERROR);
        }
    }

    //查询历史任务实例(不进行文件下载操作)
    @GetMapping("/queryHistoryTaskU")
    @ResponseBody
    public Result queryHistoryTaskU(HttpSession session){

        try {
            String username = (String) session.getAttribute("username");

            List<HistoricTaskInstance> list = historyService.
                    createHistoricTaskInstanceQuery()
                    .taskAssignee(username)
                    .list();

            List<TaskPoHi> taskPoHiList = new ArrayList<TaskPoHi>();

            for (HistoricTaskInstance task:
                 list) {
                if(task.getEndTime()!=null){
                    TaskPoHi taskPoHi = new TaskPoHi();
                    taskPoHi.setId(task.getId());
                    HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery()
                            .taskId(task.getId())
                            .singleResult();
                    List<HistoricVariableInstance> variableInstanceList = historyService.createHistoricVariableInstanceQuery()
                            .processInstanceId(historicTaskInstance.getProcessInstanceId())
                            .list();
                    for (HistoricVariableInstance v:
                         variableInstanceList) {
                        if (v.getVariableName().equals("textF")) taskPoHi.setTextF((String) v.getValue());
                        if (v.getVariableName().equals("textF")) taskPoHi.setTextZ((String) v.getValue());
                        if (v.getVariableName().equals("display")) taskPoHi.setDisplay((String) v.getValue());
                        if (v.getVariableName().equals("fileMsg")) taskPoHi.setFileMsg((String) v.getValue());
                    }
                    taskPoHi.setStartTime(task.getStartTime().toString());
                    taskPoHi.setEndtime(task.getEndTime().toString());
                    taskPoHi.setName(task.getName());
                    taskPoHiList.add(taskPoHi);
                }
            }

            return new Result(ResultCode.SUCCESS,taskPoHiList);

        }catch (Exception e){
            e.printStackTrace();
            return new Result(ResultCode.ERROR);
        }
    }

    //查看文件
    @PostMapping("/getFileUH")
    @ResponseBody
    public Result getFileUH(@RequestBody TaskDto taskDto){
        String taskId = taskDto.getTaskId();
        try {
            HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery()
                    .taskId(taskId)
                    .singleResult();
            HistoricVariableInstance variableInstance = historyService.createHistoricVariableInstanceQuery()
                    .processInstanceId(historicTaskInstance.getProcessInstanceId())
                    .variableName("file")
                    .singleResult();
            String fileName = (String) variableInstance.getValue();
            File file = new File(fileName);
            String rootName = "C:\\Users\\xuqingyuan\\Desktop\\downywy\\history"
                    +fileName.substring(fileName.lastIndexOf("\\"));
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(rootName,true));
            int len = 0;
            while((len = inputStream.read()) != -1){
                out.write(len);
                out.flush();
            }
            out.close();
            return new Result(ResultCode.SUCCESS,rootName);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(ResultCode.ERROR);
        }
    }

}
