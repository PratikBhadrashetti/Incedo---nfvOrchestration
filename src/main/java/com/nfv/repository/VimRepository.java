package com.nfv.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nfv.entity.Vim;

@Transactional
@Repository
public interface VimRepository extends CrudRepository<Vim, Long> {
}
