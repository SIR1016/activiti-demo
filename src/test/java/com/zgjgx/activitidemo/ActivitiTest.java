package com.zgjgx.activitidemo;

import org.activiti.engine.*;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

/**
 * @author 张子成
 * @since 2019年8月25日
 */
public class ActivitiTest {

    // 会默认按照Resources目录下的activiti.cfg.xml创建流程引擎
    ProcessEngine processEngine = ProcessEngineConfiguration
            .createProcessEngineConfigurationFromResource("processes/activiti.cfg.xml").buildProcessEngine();

    @Test
    // 根据 activiti.cfg.xml 初始化流程引擎
    public void activitiTable() {
        ProcessEngineConfiguration cfg = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("processes/activiti.cfg.xml");
        ProcessEngine processEngine = cfg.buildProcessEngine();

    }

    // 部署流程
    @Test
    public void deployProcess() {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder builder = repositoryService.createDeployment();
        builder.addClasspathResource("leave_staff.bpmn");//bpmn文件的名称
        builder.deploy();
    }

    // 启动流程
    @Test
    public void startProcess() {
        RuntimeService runtimeService = processEngine.getRuntimeService();

        // 流程的名称，也可以使用ByID来启动流程
        runtimeService.startProcessInstanceByKey("leave_staff");
    }

    // 流程查询
    @Test
    public void queryTask() {
        TaskService taskService = processEngine.getTaskService();

        // 根据assignee(代理人)查询任务
        String assignee = "sub_leader";
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(assignee).list();
        if (tasks.size() == 0){
            System.out.println("当前代理人没有待办任务");
        }

        //首次运行的时候这个没有输出，因为第一次运行的时候扫描act_ru_task的表里面是空的，但第一次运行完成之后里面会添加一条记录，之后每次运行里面都会添加一条记录
        for (Task task : tasks) {
            System.out.println("taskId:" + task.getId());
            System.out.println("taskName:" + task.getName());
            System.out.println("assignee:" + task.getAssignee());
            System.out.println("createTime:" + task.getCreateTime());
        }
    }

    // 办理流程
    @Test
    public void handleTask() {
        TaskService taskService = processEngine.getTaskService();

        // 根据上一步生成的taskId执行任务
        String taskId = "2505";
        taskService.complete(taskId);
    }


}
