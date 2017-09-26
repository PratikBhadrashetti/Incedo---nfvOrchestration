package com.nfv.repository;


import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nfv.entity.Tenant;
import com.nfv.entity.TenantNetwork;
import com.nfv.entity.Vim;

@Transactional
@Repository
public interface TenantNetworkRepository extends CrudRepository<TenantNetwork, Long> {
	public Set<TenantNetwork> findByTenantAndVim(Tenant tenant, Vim v);
}
