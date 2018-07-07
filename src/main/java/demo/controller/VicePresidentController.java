package demo.controller;

import demo.dto.OpinionDto;
import demo.dto.TaskDto;
import demo.entity.TaskPo;
import demo.entity.TaskPoHi;
import demo.until.Result;
import demo.until.ResultCode;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:许清远
 * Data:2018/6/26
 * Description:
 */
@Controller
public class VicePresidentController {

    @Autowired
    TaskService taskService;

    @Autowired
    HistoryService historyService;

    @Autowired
    RuntimeService runtimeService;

    @PostMapping("/approvalByF")
    @ResponseBody
    public Result approvalByF(@RequestBody OpinionDto opinionDto){
        try {
            if(opinionDto.getMsg1().equals("同意")){
                Map<String,Object> variables = new HashMap<String, Object>();
                variables.put("msg1","同意");
                variables.put("textF",opinionDto.getText());   //副经理的审批意见
                taskService.setVariable(opinionDto.getTaskId(),"display","待总经理审批");
                taskService.complete(opinionDto.getTaskId(),variables);

                return new Result(ResultCode.SUCCESS);
            }

            else {
                String owner = (String) taskService.getVariable(opinionDto.getTaskId(), "owner");
                Task task = taskService.createTaskQuery()
                        .taskId(opinionDto.getTaskId())
                        .singleResult();
                Map<String,Object> variables = new HashMap<String, Object>();
                variables.put("msg1","不同意");
                variables.put("textF",opinionDto.getText());   //副经理的审批意见
                taskService.setVariable(opinionDto.getTaskId(),"display","审批未通过");
                String pi = task.getProcessInstanceId();
                taskService.complete(opinionDto.getTaskId(),variables);
                Task result = taskService.createTaskQuery()
                        .processInstanceId(pi)
                        .singleResult();
                taskService.claim(result.getId(),owner);

                return new Result(ResultCode.SUCCESS);            //流程正常返回 success
            }
        }catch (Exception e){
            return new Result(ResultCode.ERROR);
        }
    }

    @GetMapping("/queryTaskF")
    @ResponseBody
    public Result queryTaskF(){
        try {
            List<Task> list = taskService.createTaskQuery()
                    .taskCandidateGroup("副经理")
                    .list();
            List<TaskPo> taskPoList = new ArrayList<TaskPo>();
            for (Task task:
                    list) {
                TaskPo taskPo = new TaskPo();
                taskPo.setId(task.getId());
                Map<String, Object> processVariables = taskService.getVariables(task.getId());
                for (String key:
                        processVariables.keySet()) {
                    if (key.equals("display")) taskPo.setDisplay((String) processVariables.get(key));
                    if (key.equals("textF")) taskPo.setTextF((String) processVariables.get(key));
                    if (key.equals("textZ")) taskPo.setTextZ((String) processVariables.get(key));

                }
                taskPo.setStartTime(task.getCreateTime().toString());
                taskPo.setName(task.getName());
                taskPoList.add(taskPo);
            }
            return new Result(ResultCode.SUCCESS,taskPoList);

        }catch (Exception e){
            e.printStackTrace();
            return new Result(ResultCode.ERROR);
        }
    }

    //查询历史任务实例(无法返回流程变量)
    @GetMapping("/queryHistoryTaskF")
    @ResponseBody
    public Result queryHistoryTaskF(){

        try {

            List<HistoricTaskInstance> list = historyService.
                    createHistoricTaskInstanceQuery()
                    .taskCandidateGroup("副经理")
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

    //查实历史流程任务里的流程变量，需传入任务Id
    @PostMapping("/getFileFH")
    @ResponseBody
    public Result getFileFH(@RequestBody TaskDto taskDto){
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
            String rootName = "C:\\Users\\xuqingyuan\\Desktop\\downfjl\\history"
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

    @PostMapping("/getFileF")
    @ResponseBody
    public Result getFileF(@RequestBody TaskDto taskDto){
        String taskId = taskDto.getTaskId();
        try {
            String fileName = (String) taskService.getVariable(taskId, "file");
            File file = new File(fileName);
            String rootName = "C:\\Users\\xuqingyuan\\Desktop\\downfjl\\"+fileName.substring(fileName.lastIndexOf("\\"));
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
