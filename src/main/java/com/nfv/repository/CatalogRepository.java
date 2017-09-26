package com.nfv.repository;


import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nfv.entity.Catalog;
import com.nfv.entity.Tenant;
import com.nfv.entity.Vim;
import com.nfv.entity.Vnf;

@Transactional
@Repository
public interface CatalogRepository extends CrudRepository<Catalog, Long> {
	public Catalog findByName(String Catalog);
	public Set<Catalog> findByTenantAndVimOrTenantIsNullAndVim(Tenant tenant, Vim v1, Vim v2);
	public Set<Catalog> findDistinctByVnfAndTenantIsNullOrVnfAndTenant(Vnf v1, Vnf v2, Tenant t);
	public Set<Catalog> findByVim(Vim vim);
}
