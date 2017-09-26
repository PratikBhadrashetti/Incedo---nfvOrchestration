package com.nfv.repository;


import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nfv.entity.Tenant;
import com.nfv.entity.Vim;
import com.nfv.entity.Vnf;

@Transactional
@Repository
public interface VnfRepository extends CrudRepository<Vnf, Long> {
	public Iterable<Vnf> findDistinctByCatalogIsNotNullAndVim(Vim v);
	public Set<Vnf> findDistinctByCatalogIsNotNullAndTenantIsNullAndVimOrCatalogIsNotNullAndTenantAndVim(Vim v1, Tenant t, Vim v2);
	public Iterable<Vnf> findByTenantAndVim(Tenant tenant, Vim v);
	public Set<Vnf> findByVim(Vim v);
}
