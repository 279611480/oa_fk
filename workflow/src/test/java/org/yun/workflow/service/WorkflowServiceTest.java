package org.yun.workflow.service;

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
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.yun.common.data.domain.Result;
import org.yun.workflow.WorkflowConfig;


@RunWith(SpringRunner.class)//注解（表示  使用test测试框架）
@ContextConfiguration(classes= {WorkflowConfig.class})	//注解  加入配置信息
public class WorkflowServiceTest {
	
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
	private void addFile(ZipOutputStream out, String name) throws IOException, URISyntaxException {
		// 写一个文件的标志
		ZipEntry bpmn = new ZipEntry(name);
		out.putNextEntry(bpmn);
		//写入内容
		URL bpmnUrl = this.getClass().getResource("/diagrams/" + name);
		File bpmnFile = new File(bpmnUrl.toURI());
		Files.copy(bpmnFile.toPath(), out);
	}
	
	/**
	 * 添加方法  输出流以及名字传进去
	 * 写一个文件的标志
	 * 输出流传进去
	 *写入内容  URL  以及文件  然后COPY
	 *
	 * */
	
	
}
