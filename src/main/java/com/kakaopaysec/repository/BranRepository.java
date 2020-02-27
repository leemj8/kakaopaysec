package com.kakaopaysec.repository;

import com.kakaopaysec.model.BranDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranRepository extends JpaRepository<BranDTO, Long> {
    //지점정보 세팅
    List<BranDTO> findAll();
}
