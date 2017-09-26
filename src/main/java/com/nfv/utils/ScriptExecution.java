package com.nfv.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.nfv.entity.VnfInstance;
import com.nfv.repository.VnfInstanceRepository;

@Component
@Scope("prototype")
public class ScriptExecution implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(ScriptExecution.class);
	
	private VnfInstanceRepository vnfInstanceRepository;
	
	private VnfInstance vnfInstance;
	
	@Override
	public void run() {
		try {
			executeScript("sed -i -e 's/\r//g' /home/ubuntu/clearwater/nodes.ip");
			executeScript("/home/ubuntu/clearwater/install-clearwater-base.sh");
			executeScript("/home/ubuntu/clearwater/node-config.sh /home/ubuntu/clearwater/nodes.ip");
			updateServiceStatus(vnfInstance);
		} catch (Exception e) {
			logger.error("Failed to update " + vnfInstance.getName(), e);
		}
	}

	public void set(VnfInstance vnfInstance, VnfInstanceRepository vnfInstanceRepository) {
		this.vnfInstance = vnfInstance;
		this.vnfInstanceRepository = vnfInstanceRepository;
	}
	
	private void executeScript(String script) throws Exception {
		logger.info("Starting executeScript for " + vnfInstance.getName() + " having public IP " + vnfInstance.getPublicip() + " and script " + script);
		JSch jsch = new JSch();
		Session session = jsch.getSession("root", vnfInstance.getPublicip(), 22);
		session.setPassword("ubuntu");
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.connect();
		ChannelExec channelExec = (ChannelExec)session.openChannel("exec");
		InputStream in = channelExec.getInputStream();
		channelExec.setCommand(script);
		channelExec.connect();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		while ((line = reader.readLine()) != null)
			logger.info(line);
		logger.info("Exit code for " + vnfInstance.getName() + " having public IP " + vnfInstance.getPublicip() + " and script " + script + " is " + channelExec.getExitStatus());
		reader.close();
		in.close();
		channelExec.disconnect();
		session.disconnect();
	}
	
	private void updateServiceStatus(VnfInstance vnfInstance) {
		vnfInstance.setInitstatus(3l);
		vnfInstance.setServicestatus(true);
		vnfInstanceRepository.save(vnfInstance);
	}
}
