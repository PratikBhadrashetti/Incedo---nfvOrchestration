package com.nfv.mail;

import java.io.StringWriter;

import javax.annotation.Resource;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.nfv.entity.ForgotPassword;
import com.nfv.entity.Registration;

@Component
public class EmailRegistrationContent {
	
	private static final Logger logger = LoggerFactory.getLogger(EmailRegistrationContent.class);
	
	@Resource
    private Environment environment;
	
	@Autowired
	private VelocityEngine velocityEngine;

	public String emailNotificationDetails(Registration registration, String template, String uri, String username) {
		StringWriter writer = new StringWriter();
		try {
			velocityEngine.init();
			VelocityContext context = new VelocityContext();
			context.put("projectName", environment.getProperty("project.name"));
			context.put("projectLink", "http://"+environment.getProperty("email.host.link")+uri+registration.getToken());
			if (null != username) {
				context.put("userName", username);
			}
			Template t = velocityEngine.getTemplate(template);
			t.merge(context, writer);
		} catch (Exception e) {
			logger.error("",e);
		}
		return writer.toString();
	}
	
	public String emailNotificationDetailsForTenant(Registration registration, String template, String uri, String username) {
		StringWriter writer = new StringWriter();
		try {
			velocityEngine.init();
			VelocityContext context = new VelocityContext();
			context.put("projectName", environment.getProperty("project.name"));
			context.put("projectLink", "http://"+environment.getProperty("email.host.link")+uri+registration.getToken()+"/"+registration.getEmail()+"/"+registration.getTenantname());
			if (null != username) {
				context.put("userName", username);
			}
			Template t = velocityEngine.getTemplate(template);
			t.merge(context, writer);
		} catch (Exception e) {
			logger.error("",e);
		}
		return writer.toString();
	}
	
	
	public String emailNotificationDetails(ForgotPassword forgotPassword, String template, String uri, String username) {
		StringWriter writer = new StringWriter();
		try {
			velocityEngine.init();
			VelocityContext context = new VelocityContext();
			context.put("projectName", environment.getProperty("project.name"));
			context.put("projectLink", "http://"+environment.getProperty("email.host.link")+uri+forgotPassword.getToken());
			if (null != username) {
				context.put("userName", username);
			}
			Template t = velocityEngine.getTemplate(template);
			t.merge(context, writer);
		} catch (Exception e) {
			logger.error("",e);
		}
		return writer.toString();
	}
}
