package com.nfv.mail;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class MailContent {
	
	private static final Logger logger = LoggerFactory.getLogger(MailContent.class);
	
	@Resource
    private Environment environment;
	
	@Autowired
    private VelocityEngine velocityEngine;
	
	public String emailNotificationDetails(ArrayList<Map<String, String>> list,String userName, String template){
		StringWriter writer = new StringWriter();
		try {
			velocityEngine.init();
			VelocityContext context = new VelocityContext();
			context.put("dataList", list);
			context.put("projectName", environment.getProperty("project.name"));
			context.put("userName", userName);
			Template t = velocityEngine.getTemplate(template);
			t.merge( context, writer );
		} catch (Exception e) {
			logger.error("",e);
		}
		return writer.toString();
	}
	
	public String emailNotificationDetails(Map<String, String> map,String userName, String template){
		StringWriter writer = new StringWriter();
		try {
			velocityEngine.init();
			VelocityContext context = new VelocityContext();
			context.put("dataMap", map);
			context.put("projectName", environment.getProperty("project.name"));
			context.put("userName", userName);
			Template t = velocityEngine.getTemplate(template);
			t.merge( context, writer );
		} catch (Exception e) {
			logger.error("",e);
		}
		return writer.toString();
	}
}