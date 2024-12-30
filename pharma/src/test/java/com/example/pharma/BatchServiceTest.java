package com.example.pharma;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.pharma.exception.PharmaBusinessException;
import com.example.pharma.exception.PharmaException;
import com.example.pharma.repository.BatchRepository;
import com.example.pharma.repository.MedicineMasterRepository;
import com.example.pharma.repository.MedicineTypeMasterRepository;
import com.example.pharma.repository.ShippingMasterRepository;
import com.example.pharma.service.BatchBO;
import com.example.pharma.vo.BatchVO;
import com.example.pharma.vo.MedicineMasterEntity;
import com.example.pharma.vo.MedicineTypeMasterEntity;

public class BatchServiceTest {
	@Mock
	private BatchRepository batchRepository;

	@Mock
	private ShippingMasterRepository shippingMasterRepository;

	@Mock
	private MedicineMasterRepository medicineMasterRepository;

	@Mock
	private MedicineTypeMasterRepository medicineTypeMasterRepository;

	@InjectMocks
	private BatchBO batchBO;

	private BatchVO validBatchVO;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		validBatchVO = new BatchVO();
		validBatchVO.setBatchCode("BTC-1234");
		validBatchVO.setWeight(500.0);
		validBatchVO.setPrice(150.0);
		validBatchVO.setRefrigeration(false);

		MedicineMasterEntity medicineMasterEntity = new MedicineMasterEntity();
		medicineMasterEntity.setMedicineCode("MC_301");
		medicineMasterEntity.setMedicineName("Fepanil");
		validBatchVO.setMedicineMasterEntity(medicineMasterEntity);

		MedicineTypeMasterEntity medicineTypeMasterEntity = new MedicineTypeMasterEntity();
		medicineTypeMasterEntity.setMedicineTypeCode("C");
		medicineTypeMasterEntity.setMedicineTypeName("Capsules");
		validBatchVO.setMedicineTypeMasterEntity(medicineTypeMasterEntity);

	}

	// Should return True When Batch is Valid and saved successfully
	@Test
	void testAddBatch() throws PharmaBusinessException, PharmaException {
		// Mock repository behavior
		when(medicineTypeMasterRepository.existsByMedicineTypeCode(anyString())).thenReturn(true);
		when(medicineMasterRepository.existsByMedicineCode(anyString())).thenReturn(true);
		when(batchRepository.save(any(BatchVO.class))).thenReturn(validBatchVO);

		boolean result = batchBO.addBatch(validBatchVO);

		assertTrue(result, "Batch should be added successfully.");
		verify(batchRepository, times(1)).save(validBatchVO); // Verify that save was called once
	}

	// this method should return True when Batch is updated successfully
	@Test
	void testUpdateBatch() throws PharmaBusinessException, PharmaException {
		BatchVO updatedBatchVO = new BatchVO();
		updatedBatchVO.setBatchCode("BTC-1234");
		updatedBatchVO.setWeight(550);
		updatedBatchVO.setPrice(160);

		BatchVO existingBatchVO = new BatchVO();
		existingBatchVO.setBatchCode("BTC-1234");
		existingBatchVO.setWeight(500);
		existingBatchVO.setPrice(150);

		when(batchRepository.findById("BTC-1234")).thenReturn(Optional.of(existingBatchVO));
		when(batchRepository.save(any(BatchVO.class))).thenReturn(updatedBatchVO);

		boolean result = batchBO.updateBatch("BTC-1234", updatedBatchVO);

		assertTrue(result, "Batch should be updated successfully.");
	}

	// this method should return true When Batch is deleted successfully
	@Test
	void testDeleteBatch() throws PharmaBusinessException, PharmaException {
		when(batchRepository.existsById("BTC-1234")).thenReturn(true);

		boolean result = batchBO.deleteBatch("BTC-1234");

		assertTrue(result, "Batch should be deleted.");
		verify(batchRepository, times(1)).deleteById("BTC-1234");
	}

}
