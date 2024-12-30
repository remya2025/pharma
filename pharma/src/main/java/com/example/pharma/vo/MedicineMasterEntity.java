package com.example.pharma.vo;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "MEDICINE_MASTER")
public class MedicineMasterEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "MEDICINE_CODE")
	private String medicineCode;

	@Column(name = "MEDICINE_NAME")
	private String medicineName;

	// Getters and Setters
	public String getMedicineCode() {
		return medicineCode;
	}

	public void setMedicineCode(String medicineCode) {
		this.medicineCode = medicineCode;
	}

	public String getMedicineName() {
		return medicineName;
	}

	public void setMedicineName(String medicineName) {
		this.medicineName = medicineName;
	}

	@Override
	public String toString() {
		return "MedicineMasterEntity [medicineCode=" + medicineCode + ", medicineName=" + medicineName + "]";
	}

}
