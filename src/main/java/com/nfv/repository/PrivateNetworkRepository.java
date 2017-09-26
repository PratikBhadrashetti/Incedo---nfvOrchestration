package com.nfv.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nfv.entity.PrivateNetwork;

@Transactional
@Repository
public interface PrivateNetworkRepository extends CrudRepository<PrivateNetwork, Long> {
	
}
