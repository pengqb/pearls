package com.vela.iot.active.resteasy;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.Assert;


public class Main {
	static Logger log = LoggerFactory.getLogger(Main.class);	
	public static void main(String[] args)throws Exception {
		log.info("系统正在启动....");
		Properties props = springUtil();
        int port = Integer.parseInt(props.getProperty("port")); 
        int ioWorkerCount = Integer.parseInt(props.getProperty("ioWorkerCount"));
    	int executorThreadCount = Integer.parseInt(props.getProperty("executorThreadCount"));    	
    	int maxRequestSize = Integer.parseInt(props.getProperty("maxRequestSize"));
    	
    	ApplicationContext ac = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});			
		Assert.notNull(ac);

		NettyServer netty = ac.getBean(NettyServer.class);
		netty.init(port,ioWorkerCount,executorThreadCount,maxRequestSize).start();
		log.info("系统启动成功....");        

	}
	
	private static Properties springUtil(){  
	    Properties props = new Properties();  
	    try {  
            props=PropertiesLoaderUtils.loadAllProperties("nettyServer.properties");  
        } catch (IOException e) {  
            System.out.println(e.getMessage());  
            log.error("读写nettyServer.properties文件出错", e);
        } 
	    return props;
	}  
}
