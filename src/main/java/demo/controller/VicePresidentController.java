package demo.controller;

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
    public Result approvalByF(String taskId, String msg1, String text){
        try {
            if(msg1.equals("同意")){
                Map<String,Object> variables = new HashMap<String, Object>();
                variables.put("msg1","同意");
                variables.put("textF",text);   //副经理的审批意见
                taskService.setVariable(taskId,"display","待总经理审批");
                taskService.complete(taskId,variables);

                return new Result(ResultCode.SUCCESS);
            }

            else {
                Map<String,Object> variables = new HashMap<String, Object>();
                variables.put("msg1","不同意");
                variables.put("textF",text);   //副经理的审批意见
                taskService.setVariable(taskId,"display","审批未通过");
                taskService.complete(taskId,variables);

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
                    if (key.equals("fileMsg")) taskPo.setFileMsg((String) processVariables.get(key));

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

    //查实历史流程任务里的流程变量，需传入任务Id
    @PostMapping("/getFileFH")
    @ResponseBody
    public Result getFileFH(String taskId){
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
    public Result getFileF(String taskId){
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
