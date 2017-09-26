package com.nfv.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.nfv.entity.VnfInstance;
import com.nfv.repository.VnfInstanceRepository;

@Component
@Scope("prototype")
public class ClearwaterUtil extends Thread {
	
	private static final Logger logger = LoggerFactory.getLogger(ClearwaterUtil.class);
	
	private String tempFilePath;
	
	private Iterable<VnfInstance> vnfInstances;
	
	private VnfInstanceRepository vnfInstanceRepository;
	
	@Override
	public void run() {
		try {
			logger.info("Starting file create");
			createFile();
			logger.info("Starting file copy");
			for (VnfInstance vnfInstance: vnfInstances) {
				copyFile(vnfInstance);
			}
			List<Thread> threads = new ArrayList<>();
			for (VnfInstance vnfInstance: vnfInstances) {
				ScriptExecution s = new ScriptExecution();
				s.set(vnfInstance, vnfInstanceRepository);
				Thread t = new Thread(s);
				t.start();
				threads.add(t);
			}
			for (Thread t: threads) {
				t.join();
			}
			logger.info("Clearwater util ended");
		} catch (Exception e) {
			logger.error("Clearwater Util failed!!!", e);
		}
	}
	
	public void set(Iterable<VnfInstance> vnfInstances, VnfInstanceRepository vnfInstanceRepository) {
		this.vnfInstances = vnfInstances;
		this.vnfInstanceRepository = vnfInstanceRepository;
	}
	
	private void createFile() throws Exception {
		File temp = File.createTempFile("clearwater_vims", ".tmp");
		temp.deleteOnExit();
		tempFilePath = temp.getAbsolutePath();
		Properties prop = new Properties();
		for (VnfInstance vnfInstance: vnfInstances) {
			prop.setProperty(vnfInstance.getName() + "_ip", vnfInstance.getPrivateip());
			prop.setProperty(vnfInstance.getName() + "_public_ip", vnfInstance.getPublicip());
		}
		
		OutputStream output = null;
		try {
			output = new FileOutputStream(tempFilePath);
			prop.store(output, null);
		} catch (IOException e) {
			throw e;
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	private void copyFile(VnfInstance vnfInstance) throws Exception {
		logger.info("Starting file copy for " + vnfInstance.getName() + " having public IP " + vnfInstance.getPublicip());
		JSch jsch = new JSch();
		Session session = jsch.getSession("root", vnfInstance.getPublicip(), 22);
		session.setPassword("ubuntu");
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.connect();
		Channel channel = session.openChannel("sftp");
		channel.connect();
		ChannelSftp channelSftp = (ChannelSftp)channel;
		channelSftp.cd("/home/ubuntu/clearwater");
		File f = new File(tempFilePath);
		channelSftp.put(new FileInputStream(f), "nodes.ip");
		channelSftp.disconnect();
		channel.disconnect();
		session.disconnect();
	}
}
