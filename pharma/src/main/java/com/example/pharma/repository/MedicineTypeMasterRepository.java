package com.example.pharma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pharma.vo.MedicineTypeMasterEntity;

@Repository
public interface MedicineTypeMasterRepository extends JpaRepository<MedicineTypeMasterEntity, String> {

	boolean existsByMedicineTypeCode(String medicineTypeCode);
}