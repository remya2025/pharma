package com.example.pharma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.pharma.vo.ShippingMasterEntity;

@Repository
public interface ShippingMasterRepository extends JpaRepository<ShippingMasterEntity, Long> {
	@Query("SELECT s.shippingCharge FROM ShippingMasterEntity s WHERE s.medicineTypeCode = :medicineTypeCode AND s.weightRange = :weightRange")
	Double getShippingCharge(String medicineTypeCode, String weightRange);
}
