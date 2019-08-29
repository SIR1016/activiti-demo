package com.zgjgx.activitidemo;

import org.activiti.engine.*;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态代理人流程
 * @author 张子成
 * @since 2019年8月29日
 */
public class Activiti2WithAssigneeVariableTest {

    ProcessEngine processEngine = ProcessEngineConfiguration
            .createProcessEngineConfigurationFromResource("processes/activiti.cfg.xml")
            .buildProcessEngine();

    RuntimeService runtimeService = processEngine.getRuntimeService();

    TaskService taskService = processEngine.getTaskService();

    // 部署流程
    @Test
    public void deployProcess(){
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder builder = repositoryService.createDeployment();
        builder.addClasspathResource("workflow/reimburse.bpmn")
                .deploy();
    }

    // 带变量启动流程
    @Test
    public void startProcess(){
        Map<String,Object> map = new HashMap<>();
        map.put("staff","007");
        map.put("money",20001);
        runtimeService.startProcessInstanceByKey("myProcess_1",map);
    }

    // 查询007是否成为流程代理人
    @Test
    public void queryTaskByAssignee(){
        String Assignee = "007main_leader";
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(Assignee).list();
        if (0 == tasks.size()){
            System.out.println(Assignee + " 没有需要办理的任务");
        }

        for (Task task : tasks){
            String money = taskService.getVariable(task.getId(),"money").toString();
            System.out.println("money：" + money);
            System.out.println("办理人：" + task.getAssignee());
            System.out.println("流程实例Id：" + task.getProcessInstanceId());
            System.out.println("任务Id：" + task.getId());
            System.out.println("任务名：" + task.getName());
            System.out.println("创建时间：" + task.getCreateTime());
            System.out.println("===================================");
        }
    }

    // 完成任务并指定下一节点办理人
    @Test
    public void handleTask(){
        Map<String,Object> map = new HashMap<>();
//        map.put("mian_leader","007main_leader");
//        map.put("vice_secretary","007vice_secretary");
        taskService.complete("62505",map);
    }
}
