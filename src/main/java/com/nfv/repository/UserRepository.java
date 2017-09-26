package com.nfv.repository;


import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nfv.entity.Catalog;
import com.nfv.entity.Tenant;
import com.nfv.entity.User;

@Transactional
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	public User findByOpeniamid(String openiamid);
	public User findByUsername(String username);
	public Set<User> findByRole(String role);
	public Set<User> findByRoleAndTenant(String role, Tenant t);
	public Set<User> findByRoleIsNot(String role);
	public Set<User> findByTenantAndIdIsNot(Tenant t, long id);
	public Set<User> findByCatalogAndTenant(Catalog c, Tenant t);
	public Set<User> findByCatalogAndTenantIsNot(Catalog c, Tenant t);
}
