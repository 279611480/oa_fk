package org.yun.workflow.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yun.common.data.domain.Result;
import org.yun.workflow.service.WorkflowService;


@Service
public class WorkflowServiceImpl implements WorkflowService {
	
	//日志记录器
	private Logger log = LoggerFactory.getLogger(WorkflowServiceImpl.class);
	
	//自动注入持久层服务
	@Autowired
	private RepositoryService repositoryService;
	
	@Override
	public Result deploy(String name, InputStream in) {
		// 解压缩，里面会包含bpmn、png，甚至有一些其他的文件
		//这些文件，全部打包成ZIP格式压缩文件，一并上传
		/**
		 * new一个ZipInputStream 参数里面有in
		 * */
		try (ZipInputStream zipInputStream = new ZipInputStream(in)){
			//（调用持久部署服务）创建部署构建器
			//里面有文件名，与文件ZipInputStream
			DeploymentBuilder builder = this.repositoryService.createDeployment();
			builder.name(name);
			builder.addZipInputStream(zipInputStream);
			//执行部署
			builder.deploy();
			//返回状态
			return Result.ok("流程定义部署成功");	
		} catch (IOException e) {
			log.error("流程定义部署失败："+ e.getLocalizedMessage(),e);
			return Result.error("流程定义部署失败："+ e.getLocalizedMessage());
		}

	}
	
	
	
	
}
