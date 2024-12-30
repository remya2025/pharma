package com.example.pharma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pharma.vo.BatchVO;

@Repository
public interface BatchRepository extends JpaRepository<BatchVO, String> {
	boolean existsByBatchCode(String batchCode);
}
