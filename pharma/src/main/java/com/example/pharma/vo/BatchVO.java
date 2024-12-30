/**
 * 
 */
package com.example.pharma.vo;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 
 */
@Data
@Entity
@Table(name = "BATCH_INFO")
public class BatchVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "BATCH_CODE")
	@NotNull(message = "batchCode cannot be null")
	private String batchCode;

	@ManyToOne
	@JoinColumn(name = "medicine_code", referencedColumnName = "medicine_code", nullable = false)
	@NotNull(message = "medicine Code cannot be null")
	@Valid
	private MedicineMasterEntity medicineMasterEntity;

	@ManyToOne
	@NotNull(message = "medicine Type Code cannot be null")
	@JoinColumn(name = "medicine_type_code", referencedColumnName = "medicine_type_code", nullable = false)
	@Valid
	private MedicineTypeMasterEntity medicineTypeMasterEntity;

	@Column(name = "WEIGHT")
	@Min(value = 100, message = "Weight must be >100 ")
	private Double weight;

	@Column(name = "PRICE")
	private double price;

	@Column(name = "WEIGHT_RANGE")
	private String weightRange;

	@Column(name = "CARE_LEVEL")
	private String careLevel;

	@Column(name = "SHIPPING_CHARGE")
	private double shippingCharge;

	@Column(name = "REFRIGERATION", nullable = true)
	private Boolean refrigeration;

	public BatchVO() {

	}

	public Boolean isRefrigeration() {
		return refrigeration;
	}

	public void setRefrigeration(Boolean refrigeration) {
		this.refrigeration = refrigeration;
	}

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public MedicineMasterEntity getMedicineMasterEntity() {
		return medicineMasterEntity;
	}

	public void setMedicineMasterEntity(MedicineMasterEntity medicineMasterEntity) {
		this.medicineMasterEntity = medicineMasterEntity;
	}

	public MedicineTypeMasterEntity getMedicineTypeMasterEntity() {
		return medicineTypeMasterEntity;
	}

	public void setMedicineTypeMasterEntity(MedicineTypeMasterEntity medicineTypeMasterEntity) {
		this.medicineTypeMasterEntity = medicineTypeMasterEntity;
	}

	public Boolean getRefrigeration() {
		return refrigeration;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getWeightRange() {
		return weightRange;
	}

	public void setWeightRange(String weightRange) {
		this.weightRange = weightRange;
	}

	public String getCareLevel() {
		return careLevel;
	}

	public void setCareLevel(String careLevel) {
		this.careLevel = careLevel;
	}

	public double getShippingCharge() {
		return shippingCharge;
	}

	public void setShippingCharge(double shippingCharge) {
		this.shippingCharge = shippingCharge;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "BatchVO [batchCode=" + batchCode + ", medicineMasterEntity=" + medicineMasterEntity
				+ ", medicineTypeMasterEntity=" + medicineTypeMasterEntity + ", weight=" + weight + ", price=" + price
				+ ", weightRange=" + weightRange + ", careLevel=" + careLevel + ", shippingCharge=" + shippingCharge
				+ ", refrigeration=" + refrigeration + "]";
	}

}
