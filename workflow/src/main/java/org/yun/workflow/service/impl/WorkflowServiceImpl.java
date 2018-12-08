package org.yun.workflow.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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
	/**实现方法
	 *设置每页查询多少数据  Pageable
	 * */
	@Override
	public Page<ProcessDefinition> findDefinitions(String keyword, int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, 10);// 每页读取10条数据
		//虽然Page和Pageable是Spring Data 里面，但是Activiti没有SpringData的支持，需要自己查询数据
		//1.创建流程定义查询对象 【调用持久服务层方法层 创建】
		ProcessDefinitionQuery query = this.repositoryService.createProcessDefinitionQuery();
		//2.设置 查询条件【判断  关键字为null的时候  不理  表明没搜索，
		//当关键字不为null的时候，模糊查询关键字，查询对象调用流程查询名字模糊  将关键字传进去】
		if(StringUtils.isEmpty(keyword)) {
			//为空  表示 压根就没有 搜索  不理
		}{
			//不然的话，有在关键字查询，模糊查询
			keyword = "%" + keyword +"%";
			query.processDefinitionNameLike(keyword);
		}
		//只查询最后一个版本 流程定义（每个KEY对应的最后一个版本的流程定义）
		query.latestVersion();
		//3.设置排序条件   升序排序
		query.orderByProcessDefinitionKey().asc();
		//4.查询总记录数
		long totalRows = query.count();
		//5.查询一页的数据
		List<ProcessDefinition> content = query.listPage((int) pageable.getOffset(),pageable.getPageSize());
		//6.构建Page对象
		Page<ProcessDefinition> page =new PageImpl<>(content,pageable,totalRows);
		//返回page
		return page;
	}
	
	
	
	
}
