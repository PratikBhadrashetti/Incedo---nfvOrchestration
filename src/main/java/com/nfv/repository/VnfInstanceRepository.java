package com.nfv.repository;


import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nfv.entity.Catalog;
import com.nfv.entity.Tenant;
import com.nfv.entity.User;
import com.nfv.entity.Vim;
import com.nfv.entity.Vnf;
import com.nfv.entity.VnfInstance;

@Transactional
@Repository
public interface VnfInstanceRepository extends CrudRepository<VnfInstance, Long> {
	public void deleteByVnf(Vnf vnf);
	public Set<VnfInstance> findByCatalogAndVnf(Catalog catalog, Vnf vnf);
	public Set<VnfInstance> findByCatalog(Catalog catalog);
	public Set<VnfInstance> findByCatalogAndTenant(Catalog catalog, Tenant tenant);
	public Set<VnfInstance> findByCatalogAndTenantAndUser(Catalog catalog, Tenant tenant, User user);
	public Set<VnfInstance> findByVnfAndTenant(Vnf vnf, Tenant tenant);
	public Set<VnfInstance> findByTenantAndVim(Tenant tenant, Vim v);
	public Set<VnfInstance> findByTackername(String tackername);
	public Set<VnfInstance> findByInitstatus(Long initstatus);
	public Set<VnfInstance> findByVim(Vim v);
}
