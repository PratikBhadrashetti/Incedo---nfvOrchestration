package com.nfv.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nfv.entity.Tenant;

@Transactional
@Repository
public interface TenantRepository extends CrudRepository<Tenant, Long> {
	Tenant findByName(String name);
	Tenant findByProjectid(String projectid);
	
	@Query("select t from tenant t where t.id <> 1")
	public List<Tenant> findAllExceptAdmin();
	
	/*@Modifying
	@Query("update tenant t set t.isActive = :isActive where t.id = :id")
	Integer updateAsActive(@Param("id") Long id, @Param("isActive") boolean isActive);*/
}
