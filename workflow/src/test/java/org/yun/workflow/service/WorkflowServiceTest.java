package org.yun.workflow.service;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.activiti.bpmn.converter.util.BpmnXMLUtil;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import org.yun.common.data.domain.Result;
import org.yun.workflow.WorkflowConfig;


@RunWith(SpringRunner.class)//注解（表示  使用test测试框架）
@ContextConfiguration(classes= {WorkflowConfig.class})	//注解  加入配置信息
public class WorkflowServiceTest extends AbstractJUnit4SpringContextTests {//继承无回滚事物【数据库会保存】extends AbstractJUnit4SpringContextTests
	
	//自动注入  服务层接口
	@Autowired
	private WorkflowService workflowService;
	
	private String processDefinitionId;
	//测试类   测试部署是否成功
	//@Test
	// 把准备数据的代码，放入Before里面，这样保证每个@Test都有一份新的数据
	@Before
	public void testDeploySuccess() throws IOException, URISyntaxException {
		String name = "HelloWorld";
		//把测试文件压缩起来，方便测试
		//可以直接导出文件，方便生成zip文件，也可以直接在内存里面压缩
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try(ZipOutputStream out = new ZipOutputStream(outputStream);){
			this.addFile(out,name+".bpmn");
			this.addFile(out, name + ".png");
		}
		try(InputStream in = new ByteArrayInputStream(outputStream.toByteArray())){
			Result result = this.workflowService.deploy(name, in);
			Assert.assertEquals(Result.CODE_OK, result.getCode());
		}
		ProcessDefinition definition = this.workflowService.findDefinitionByKey(name);
		if(definition != null) {
			processDefinitionId = definition.getId();	
		}
	}

	private void addFile(ZipOutputStream out, String name) throws IOException, URISyntaxException {
		// 写一个文件的标志
		ZipEntry bpmn = new ZipEntry(name);
		out.putNextEntry(bpmn);
		//写入内容
		URL bpmnUrl = this.getClass().getResource("/diagrams/" + name);
		File bpmnFile = new File(bpmnUrl.toURI());
		Files.copy(bpmnFile.toPath(), out);
	}
	
	/**************查询流程定义列表  **************************/
	@Test
	public void testFindDefinitions() throws Exception {
		//测试流程定义查询		
		String keyword = null;
		int pageNumber =0;//第一页
		Page<ProcessDefinition> page = this.workflowService.findDefinitions(keyword,pageNumber);
		Assert.assertNotNull("必须要求返回一页数据", page);
		Assert.assertEquals("预期要有数据，总记录数要大于0", true, page.getTotalElements() > 0);
	}
	/************** -禁用和激活  **************************/
	//禁用及激活
	@Test
	public void disable() {
		//禁用流程定义
		//直接查询一个流程定义来模拟，检查是否禁用成功
		// String processDefinitionId = "";
		
		//执行禁用
		this.workflowService.disableProcessDefinition(processDefinitionId);
		
		//查询流程定义。检查是否禁用成功
		ProcessDefinition definition=this.workflowService.findDefinitionById(processDefinitionId);
		Assert.assertNotNull(definition);
		Assert.assertEquals(true, definition.isSuspended());
		
		//执行激活
		this.workflowService.activeProcessDefinition(processDefinitionId);
		//查询流程定义，检查使用激活成功
		definition = this.workflowService.findDefinitionById(processDefinitionId);
		Assert.assertNotNull(definition);
		Assert.assertEquals(false, definition.isSuspended());
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
