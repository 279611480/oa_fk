package org.yun.workflow;

import javax.sql.DataSource;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.PlatformTransactionManager;


//@Configuration//如果要改为SpringBoot,只需要修改此行，然后加个main方法
@SpringBootApplication
@ComponentScan("org.yun")
public class WorkflowConfig {
	
	public static void main(String[] args) {
		SpringApplication.run(WorkflowConfig.class, args);
	}

//	 <bean id="processEngineConfiguration" class="org.activiti.spring.SpringProcessEngineConfiguration">
//    <property name="dataSource" ref="dataSource" />
//    <property name="transactionManager" ref="transactionManager" />
//    <property name="databaseSchemaUpdate" value="true" />
//    <property name="jobExecutorActivate" value="false" />
//  </bean>
	// 配置引擎
	@Bean
	public SpringProcessEngineConfiguration processEngineConfiguration(
			@Autowired DataSource dataSource,
			@Autowired PlatformTransactionManager transactionManager
			) {
		SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();
		config.setDataSource(dataSource);
		config.setTransactionManager(transactionManager);
		//自动更新数据库表，如果表不存在会自动创建
		config.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
		config.setJobExecutorActivate(false);//是否自动激活任务
		return config;
	}
	
//	  <bean id="processEngine" class="org.activiti.spring.ProcessEngineFactoryBean">
//    <property name="processEngineConfiguration" ref="processEngineConfiguration" />
//  </bean>
	// 配置引擎实例的工厂Bean，用于产生Bean（ProcessEngine）
	@Bean
	public ProcessEngineFactoryBean processEngine(@Autowired SpringProcessEngineConfiguration config) {
		ProcessEngineFactoryBean bean = new ProcessEngineFactoryBean();
		bean.setProcessEngineConfiguration(config);
		return bean;
	}
	
//	<bean id="repositoryService" factory-bean="processEngine" factory-method="getRepositoryService" />
	// 获取流程引擎暴露出来的各种服务，包括存储、运行时、任务、历史、表单、用户权限、管理等

	// 存储服务：负责流程定义的部署、查询、禁用、激活等功能
	// 这些服务，都直接当做DAO使用，因为它们是【框架级的服务】。我们自己的服务层，指的是【应用级服务】。
	@Bean
	public RepositoryService repositoryService(@Autowired ProcessEngine processEngine) {
		return processEngine.getRepositoryService();
	}
	
	// 运行时服务：负责流程的流转，是核心
	@Bean
	public RuntimeService runtimeService (@Autowired ProcessEngine processEngine) {
		return processEngine.getRuntimeService();
	}
	
	// 任务服务：负责待办任务的查询、完成任务、转派任务等，任务其实也是运行时数据
	@Bean
	public TaskService taskService(@Autowired ProcessEngine processEngine ) {
		return processEngine.getTaskService();
	}
	
	// 所有的运行时数据，在使用完成以后，都直接删除，根本没有update语句，运行时数据越少、运行效率越高
	// 为了保证数据的完整性、可跟踪性，于是必须有历史数据
	// 历史服务：管理所有的历史数据，而且历史数据比运行时数据要完整得多，历史数据通常只是用主键查询
	@Bean 
	public HistoryService historyService(@Autowired ProcessEngine processEngine) {
		return processEngine.getHistoryService();	
	}
	
	// 表单服务：负责处理所有跟表单有关的数据，比如业务表单、表单属性等
	@Bean
	public FormService formService(@Autowired ProcessEngine processEngine) {
		return processEngine.getFormService();
	}
}
