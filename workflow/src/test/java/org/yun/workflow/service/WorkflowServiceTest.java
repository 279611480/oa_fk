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
public class WorkflowServiceTest {//继承无回滚事物【数据库会保存】extends AbstractJUnit4SpringContextTests
	
	//自动注入  服务层接口
	@Autowired
	private WorkflowService workflowService;
	//测试类   测试部署是否成功
	/**
	 * 加入  图名【就是   之前画图的类名】
	 * 
	 * 定义字节输出流
	 * 定义Zip输出流  添加文件
	 *  定义字节输入流（参数有输出）
	 *  调用服务层方法将文件名以及输入流传进去
	 * 断言，得到的结果编码是否为OK
	 * @throws IOException 
	 * @throws URISyntaxException 
	 * */
	@Test
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
	}
	/**被上面调用
	 * 添加方法  输出流以及名字传进去
	 * 写一个文件的标志
	 * 输出流传进去
	 *写入内容  URL  以及文件  然后COPY
	 *
	 * */
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
		/**定义关键字为Null   定义页码数为0【其实是第一页】  
		 * 调用工作流服务层方法，根据传进去的关键字以及页码数  查询流程定义
		 * 断言   页面是否不为空
		 * 断言总记录数是否大于0
		 * */
		String keyword = null;
		int pageNumber =0;//第一页
		Page<ProcessDefinition> page = this.workflowService.findDefinitions(keyword,pageNumber);
	}
	/************** -禁用和激活  **************************/
}
