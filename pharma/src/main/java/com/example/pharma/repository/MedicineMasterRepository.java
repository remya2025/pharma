package com.example.pharma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pharma.vo.MedicineMasterEntity;

@Repository
public interface MedicineMasterRepository extends JpaRepository<MedicineMasterEntity, String> {
	boolean existsByMedicineCode(String medicineCode);
}
