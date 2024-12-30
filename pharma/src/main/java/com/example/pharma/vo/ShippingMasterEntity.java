/**
 * 
 */
package com.example.pharma.vo;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * 
 */
@Data
@Entity
@Table(name = "SHIPPING_MASTER")
public class ShippingMasterEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "MEDICINE_TYPE_CODE")
	private String medicineTypeCode;

	@Column(name = "WEIGHT_RANGE")
	private String weightRange;

	@Column(name = "SHIPPING_CHARGE")
	private double shippingCharge;

	public String getMedicineTypeCode() {
		return medicineTypeCode;
	}

	public void setMedicineTypeCode(String medicineTypeCode) {
		this.medicineTypeCode = medicineTypeCode;
	}

	public String getWeightRange() {
		return weightRange;
	}

	public void setWeightRange(String weightRange) {
		this.weightRange = weightRange;
	}

	public double getShippingCharge() {
		return shippingCharge;
	}

	public void setShippingCharge(double shippingCharge) {
		this.shippingCharge = shippingCharge;
	}

	@Override
	public String toString() {
		return "ShippingMasterEntity [medicineTypeCode=" + medicineTypeCode + ", weightRange=" + weightRange
				+ ", shippingCharge=" + shippingCharge + "]";
	}
	
	
}
