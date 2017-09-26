/**
 * 
 */
package com.nfv.config;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.nfv.entity.Tenant;
import com.nfv.repository.TenantRepository;

/**
 * @author rohit.patel
 *
 */
@Component
public class ServerInitializer implements ApplicationRunner {
	
	private static final Logger logger = LoggerFactory.getLogger(ServerInitializer.class);
	
	@Value("${tenantadmin.db.name}")
	String tenantadminDbName;
	
	@Value("${tenant.project.name}")
	String tenantprojectDbName;
	
	@Value("${tenant.project.user}")
	String tenantprojectDbUser;
	
	@Value("${tenant.project.password}")
	String tenantprojectDbPassword;
	
	@Resource
	private TenantRepository tenantRepository;
	
	@Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
		logger.info("start run : ServerInitializer");
		Tenant admintenant = tenantRepository.findByName(tenantadminDbName);
		if(null == admintenant)
		{
			admintenant = new Tenant();
			admintenant.setName(tenantadminDbName);
			admintenant.setProject(tenantprojectDbName);
			admintenant.setProjectusername(tenantprojectDbUser);
			admintenant.setProjectpassword(tenantprojectDbPassword);
			tenantRepository.save(admintenant);
			logger.info("Admin record inserted successfully into Tenant table");
		}
		else
		{
			admintenant.setProject(tenantprojectDbName);
			admintenant.setProjectusername(tenantprojectDbUser);
			admintenant.setProjectpassword(tenantprojectDbPassword);
			tenantRepository.save(admintenant);
			logger.info("Admin record updated successfully into Tenant table ");
		}	
		logger.info("end run : ServerInitializer");
    }
}

