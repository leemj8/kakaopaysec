package com.kakaopaysec.repository;

import com.kakaopaysec.model.AccountDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<AccountDTO, Long> {
    //List<AccountDTO> findAllByAccNo(String accNo);

    List<AccountDTO> findAllByAccNo(String accNo);
    List<AccountDTO> findAll();

}
