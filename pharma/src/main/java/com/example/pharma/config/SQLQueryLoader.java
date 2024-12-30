/**
 * 
 */
package com.example.pharma.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 
 */
@Component
public class SQLQueryLoader {

	@Value("${insertBatchInfo}")
	public String insertBatchInfo;

	@Value("${getShipChrgeFrmShippingMaster}")
	public String getShipChrgeFrmShippingMaster;

	@Value("${checkBatchCodeExists}")
	public String checkBatchCodeExists;

	@Value("${checkIfMedicineCodeExists}")
	public String checkIfMedicineCodeExists;

	@Value("${checkIfMedTypeCodeExists}")
	public String checkIfMedTypeCodeExists;

	/**
	 * @return the insertBatchInfo
	 */
	public String getInsertBatchInfo() {
		return insertBatchInfo;
	}

	/**
	 * @return the getShipChrgeFrmShippingMaster
	 */
	public String getGetShipChrgeFrmShippingMaster() {
		return getShipChrgeFrmShippingMaster;
	}

	/**
	 * @return the checkBatchCodeExists
	 */
	public String getCheckBatchCodeExists() {
		return checkBatchCodeExists;
	}

	/**
	 * @return the checkIfMedicineCodeExists
	 */
	public String getCheckIfMedicineCodeExists() {
		return checkIfMedicineCodeExists;
	}

	/**
	 * @return the checkIfMedTypeCodeExists
	 */
	public String getCheckIfMedTypeCodeExists() {
		return checkIfMedTypeCodeExists;
	}

}
