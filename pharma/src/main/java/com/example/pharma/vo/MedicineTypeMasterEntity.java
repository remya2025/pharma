package com.example.pharma.vo;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "MEDICINE_TYPE_MASTER")
public class MedicineTypeMasterEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "MEDICINE_TYPE_CODE")
	private String medicineTypeCode;

	@Column(name = "MEDICINE_TYPE_NAME")
	private String medicineTypeName;

	// Getters and Setters
	public String getMedicineTypeCode() {
		return medicineTypeCode;
	}

	public void setMedicineTypeCode(String medicineTypeCode) {
		this.medicineTypeCode = medicineTypeCode;
	}

	public String getMedicineTypeName() {
		return medicineTypeName;
	}

	public void setMedicineTypeName(String medicineTypeName) {
		this.medicineTypeName = medicineTypeName;
	}

	@Override
	public String toString() {
		return "MedicineTypeMasterEntity [medicineTypeCode=" + medicineTypeCode + ", medicineTypeName="
				+ medicineTypeName + "]";
	}

}
