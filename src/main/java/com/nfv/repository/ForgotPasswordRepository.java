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

import com.nfv.entity.ForgotPassword;

@Transactional
@Repository
public interface ForgotPasswordRepository extends CrudRepository<ForgotPassword, Long> {
	ForgotPassword findByToken(String token);
	ForgotPassword findByEmail(String email);
	ForgotPassword findByUsername(String username);
}

