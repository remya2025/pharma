package com.example.pharma.vo;

public class CsvRecord {
	private String batchCode;
	private MedicineMasterEntity medicineMasterEntity;
	private MedicineTypeMasterEntity medicineTypeMasterEntity;
	private double weight;
	private double price;
	private boolean refrigeration;

	// Getters and Setters
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

	public boolean isRefrigeration() {
		return refrigeration;
	}

	public void setRefrigeration(boolean refrigeration) {
		this.refrigeration = refrigeration;
	}
}
