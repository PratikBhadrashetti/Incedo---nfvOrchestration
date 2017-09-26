/**
 * 
 */
package com.nfv.repository;

/**
 * @author rohit.patel
 *
 */
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nfv.entity.Registration;

@Transactional
@Repository
public interface RegistrationRepository extends CrudRepository<Registration, Long> {
	Registration findByToken(String token);
	Registration findByEmail(String email);
	Registration findByUsername(String username);
}

