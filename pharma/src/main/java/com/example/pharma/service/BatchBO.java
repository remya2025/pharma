/**
 * 
 */
package com.example.pharma.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.pharma.constant.PharmaBatchConstants;
import com.example.pharma.exception.PharmaBusinessException;
import com.example.pharma.exception.PharmaException;
import com.example.pharma.repository.BatchRepository;
import com.example.pharma.repository.MedicineMasterRepository;
import com.example.pharma.repository.MedicineTypeMasterRepository;
import com.example.pharma.repository.ShippingMasterRepository;
import com.example.pharma.vo.BatchVO;
import com.example.pharma.vo.MedicineMasterEntity;
import com.example.pharma.vo.MedicineTypeMasterEntity;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 
 */
@Service
public class BatchBO implements IBatchBO {
	@Autowired
	private BatchRepository batchRepository;

	@Autowired
	private ShippingMasterRepository shippingMasterRepository;

	@Autowired
	private MedicineMasterRepository medicineMasterRepository;

	@Autowired
	private MedicineTypeMasterRepository medicineTypeMasterRepository;

	// Creating a Logger instance using SLF4J
	private static final Logger logger = LoggerFactory.getLogger(BatchService.class);

	private void validateRequired(BatchVO batchVO) throws PharmaBusinessException {
		logger.info("Start - validateRequired() method");
		if (batchVO.getBatchCode() == null || batchVO.getBatchCode().isEmpty()
				|| batchVO.getMedicineTypeMasterEntity().getMedicineTypeCode() == null
				|| batchVO.getMedicineMasterEntity().getMedicineCode().isEmpty() || batchVO.getWeight() <= 0) {
			throw new PharmaBusinessException(PharmaBatchConstants.MANDATORY_FIELDS_MISSING,
					PharmaBatchConstants.ERROR_CODE_MANDATORY_FIELDS);
		}
		logger.info("End - validateRequired() method");
	}

	private void validateMedicineTypeCode(BatchVO batchVO) throws PharmaBusinessException {
		logger.info("Start - validateMedicineTypeCode() method");
		if (!medicineTypeMasterRepository
				.existsByMedicineTypeCode(batchVO.getMedicineTypeMasterEntity().getMedicineTypeCode())) {

			throw new PharmaBusinessException(PharmaBatchConstants.MEDICINE_TYPE_CODE_NOT_EXIST,
					PharmaBatchConstants.ERROR_CODE_MEDICINE_TYPE_NOT_EXIST); // Error code 404
		}
		logger.info("End - validateMedicineTypeCode() method");
	}

	public boolean validateBeforeAddAllBatch(BatchVO batchVO, List<String> errorMessages) {
		logger.info("Start - validateBeforeAddAllBatch() method");
		try {
			validateBatchInfo(batchVO);
			return true;
		} catch (Exception e) {
			logger.error("Error occured:" + e.getMessage());
			errorMessages.add(e.getMessage());
			return false;
		} finally {
			logger.info("End - validateBeforeAddAllBatch() method");
		}
	}

	public void validateBatchInfo(BatchVO batchVO) throws PharmaBusinessException {
		logger.info("Start - validateBatchInfo() method");
		// validateRequired(batchVO);
		validateMedicineTypeCode(batchVO);
		validateMedicineCode(batchVO);
		validateIfBatchCode(batchVO);
		// validateWeight(batchVO);
		calculateShippingCharge(batchVO);
		findCareLevelMessage(batchVO);
		logger.info("End - validateBatchInfo() method");

	}

	private void findCareLevelMessage(BatchVO batchVO) {
		logger.info("Start - findCareLevelMessage() method");
		// Determine the care level based on the medicine type
		String careLevel = getCareLevel(batchVO.getMedicineTypeMasterEntity().getMedicineTypeCode());
		batchVO.setCareLevel(careLevel);
		logger.info("End - findCareLevelMessage() method");

	}

	private void calculateShippingCharge(BatchVO batchVO) throws PharmaBusinessException {
		logger.info("Start - calculateShippingCharge() method");
		String weightCategory = getWeightCategory(batchVO.getWeight());
		batchVO.setWeightRange(weightCategory);

		// Get the shipping charge from the SHIPPING_MASTER table based on the medicine
		// type and weight category
		double shippingCharge = getShippingCharge(batchVO.getMedicineTypeMasterEntity().getMedicineTypeCode(),
				weightCategory);

		// If the medicine requires refrigeration, apply the 5% surcharge
		if (batchVO.isRefrigeration()) {
			shippingCharge += (shippingCharge * 0.05); // Adding 5% surcharge
		}
		batchVO.setShippingCharge(shippingCharge);
		logger.info("End - calculateShippingCharge() method");

	}

	private void validateWeight(BatchVO batchVO) throws PharmaBusinessException {
		logger.info("Start - validateWeight() method");
		// Check if batch weight is less than 100
		if (batchVO.getWeight() < 100) {
			// Error code 512
			throw new PharmaBusinessException(PharmaBatchConstants.BATCH_WEIGHT_TOO_LOW,
					PharmaBatchConstants.ERROR_CODE_BATCH_WEIGHT_TOO_LOW);
		}
		logger.info("End - validateWeight() method");
	}

	private void validateIfBatchCode(BatchVO batchVO) throws PharmaBusinessException {
		logger.info("Start - validateIfBatchCode() method");
		if (!batchVO.getBatchCode().matches("^BTC-\\d{4}$")) {
			// Error code 513
			throw new PharmaBusinessException(
					PharmaBatchConstants.BATCH_CODE_INVALID_FORMAT + " Batch Code given :" + batchVO.getBatchCode(),
					PharmaBatchConstants.ERROR_CODE_BATCH_CODE_INVALID_FORMAT);
		}

		if (batchRepository.existsByBatchCode(batchVO.getBatchCode())) {
			// Error code 511
			throw new PharmaBusinessException(PharmaBatchConstants.BATCH_CODE_ALREADY_EXISTS + batchVO.getBatchCode(),
					PharmaBatchConstants.ERROR_CODE_BATCH_ALREADY_EXISTS);
		}
		logger.info("End - validateIfBatchCode() method");

	}

	private void validateMedicineCode(BatchVO batchVO) throws PharmaBusinessException {
		logger.info("Start - validateMedicineCode() method");
		// Check if medicine code exists
		if (!medicineMasterRepository.existsByMedicineCode(batchVO.getMedicineMasterEntity().getMedicineCode())) {
			// Error code 510
			throw new PharmaBusinessException(PharmaBatchConstants.MEDICINE_CODE_NOT_EXIST,
					PharmaBatchConstants.ERROR_CODE_MEDICINE_CODE_NOT_EXIST);
		}
		logger.info("End - validateMedicineCode() method");
	}

	@Override
	public boolean addBatch(BatchVO batchVO) throws PharmaBusinessException, PharmaException {
		logger.info("Start - addBatch() method");
		try {
			validateBatchInfo(batchVO);
			// Save the batch details using the DAO
			BatchVO savedBatchVO = batchRepository.save(batchVO);

			if (savedBatchVO != null && savedBatchVO.getBatchCode() != null) {
				// The batch was saved successfully
				return true;
			} else {
				// The batch was not saved
				return false;
			}
		} finally {
			logger.info("End - addBatch() method");
		}

	}

	/**
	 * @param medicineTypeCode
	 * @return
	 */
	private String getCareLevel(String medicineTypeCode) {
		logger.info("Start - getCareLevel() method");
		try {
			if ("C".equalsIgnoreCase(medicineTypeCode)) {
				return PharmaBatchConstants.CARE_LEVEL_NORMAL; // Care level for Capsules
			} else if ("S".equalsIgnoreCase(medicineTypeCode)) {
				return PharmaBatchConstants.CARE_LEVEL_EXTREMELY_HIGH; // Care level for Syrups or other types
			}
			return PharmaBatchConstants.CARE_LEVEL_NORMAL; // Default care level for unknown types
		} finally {
			logger.info("End - getCareLevel() method");
		}
	}

	/**
	 * @param medicineTypeCode
	 * @param weightCategory
	 * @return
	 */
	public double getShippingCharge(String medicineTypeCode, String weightRange) throws PharmaBusinessException {
		logger.info("Start - getShippingCharge() method");
		try {
			Double shippingCharge = shippingMasterRepository.getShippingCharge(medicineTypeCode, weightRange);
			// Check if shipping charge is null, and handle accordingly
			if (shippingCharge == null) {
				throw new PharmaBusinessException(PharmaBatchConstants.NO_SHIPPING_CHARGE_FOUND,
						PharmaBatchConstants.ERROR_SHIPPING_CHARGE_NOT_FOUND);
			}
			return shippingCharge;
		} finally {
			logger.info("End - getShippingCharge() method");
		}

	}

	/**
	 * @param weight
	 * @return
	 */
	private String getWeightCategory(double weight) {
		logger.info("Start - getWeightCategory() method");
		try {
			if (weight <= 500) {
				return PharmaBatchConstants.WEIGHT_CATEGORY_W1;
			} else if (weight <= 1000) {
				return PharmaBatchConstants.WEIGHT_CATEGORY_W2;
			} else if (weight <= 1500) {
				return PharmaBatchConstants.WEIGHT_CATEGORY_W3;
			} else if (weight <= 3000) {
				return PharmaBatchConstants.WEIGHT_CATEGORY_W4;
			}
			return PharmaBatchConstants.WEIGHT_CATEGORY_W5;
		} finally {
			logger.info("End - getWeightCategory() method");
		}
	}

	@Override
	public Page<BatchVO> findAllBatchInfo(Pageable pageable) throws PharmaBusinessException, PharmaException {
		return batchRepository.findAll(pageable);

	}

	@Override
	public boolean updateBatch(String id, BatchVO batchVO) throws PharmaBusinessException, PharmaException {
		logger.info("Start - updateBatch() method");
		try {
			BatchVO batchInfo = batchRepository.findById(id)
					.orElseThrow(() -> new PharmaBusinessException(PharmaBatchConstants.BATCH_CODE_NOT_FOUND,
							PharmaBatchConstants.ERROR_CODE_BATCH_CODE_NOT_FOUND));

			// Update the batch properties with values from batchVO
			batchInfo.setPrice(batchVO.getPrice());
			batchInfo.setWeight(batchVO.getWeight());

			// Save the updated batch
			batchRepository.save(batchInfo);

			return true;
		} finally {
			logger.info("End - updateBatch() method");
		}
	}

	public boolean addAllBatch(Set<BatchVO> batchvoRecords) throws PharmaBusinessException, PharmaException {
		logger.info("Start - updateBatch() method");
		try {
			List<BatchVO> insertedList = batchRepository.saveAll(batchvoRecords);
			if (insertedList != null) {
				return true;
			} else {
				return false;
			}
		} finally {
			logger.info("End - updateBatch() method");
		}
	}

	@Override
	public List<BatchVO> findAllBatchInfo() throws PharmaBusinessException, PharmaException {
		return batchRepository.findAll();
	}

	@Override
	// public void parseAndLoadCSV(MultipartFile file) throws
	// PharmaBusinessException, PharmaException, IOException {
	public File parseAndLoadCSV(MultipartFile file) throws PharmaBusinessException, PharmaException, IOException {
		logger.info("Start - parseAndLoadCSV() method");
		Set<BatchVO> batchvoRecords = new HashSet<>();
		List<String[]> invalidEntries = new ArrayList<>();
		List<String> errorMessages = new ArrayList<>(); // To store error messages
		File invalidCsvFile = null;
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));

				CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

			for (CSVRecord csvRecord : csvParser) {
				BatchVO batchVO = new BatchVO();
				boolean isValid = true;
				try {
					// Mapping CSV fields to the respective Java objects
					batchVO.setBatchCode(csvRecord.get("batchCode"));
					// Mapping MedicineMasterEntity
					MedicineMasterEntity medicineMasterEntity = new MedicineMasterEntity();
					medicineMasterEntity.setMedicineCode(csvRecord.get("medicineCode"));
					batchVO.setMedicineMasterEntity(medicineMasterEntity);

					// Mapping MedicineTypeMasterEntity
					MedicineTypeMasterEntity medicineTypeMasterEntity = new MedicineTypeMasterEntity();
					medicineTypeMasterEntity.setMedicineTypeCode(csvRecord.get("medicineTypeCode"));
					batchVO.setMedicineTypeMasterEntity(medicineTypeMasterEntity);

					batchVO.setWeight(Double.parseDouble(csvRecord.get("weight")));
					batchVO.setPrice(Double.parseDouble(csvRecord.get("price")));
					batchVO.setRefrigeration(Boolean.parseBoolean(csvRecord.get("refrigeration")));
					/*
					 * if (validateBeforeAddAllBatch(batchVO,errorMessages)) {
					 * batchvoRecords.add(batchVO); } else { isValid = false; }
					 */
					// batchBO.addBatch(batchVO);
					if (!validateBeforeAddAllBatch(batchVO, errorMessages)) {
						isValid = false;
					} else {
						batchvoRecords.add(batchVO);
					}
				} catch (Exception e) {
					isValid = false;
					errorMessages.add("Error processing record: " + e.getMessage()); // Add generic error message
				}

				/*
				 * if (!isValid) { // Collect invalid entries for later export //
				 * invalidEntries.add(csvRecord.toList().toArray(new String[0]));
				 * 
				 * // Collect invalid entries for later export List<String> entryList = new
				 * ArrayList<>(); for (String field : csvRecord) { entryList.add(field); }
				 * invalidEntries.add(entryList.toArray(new String[0]));
				 * 
				 * }
				 */
				if (!isValid) {
					List<String> entryWithError = new ArrayList<>();
					for (String field : csvRecord) {
						entryWithError.add(field);
					}
					entryWithError.add(String.join(", ", errorMessages)); // Add the error messages for this record
					invalidEntries.add(entryWithError.toArray(new String[0]));
					errorMessages.clear(); // Clear error messages for the next row
				}

			}
			addAllBatch(batchvoRecords);
			if (!invalidEntries.isEmpty()) {
				invalidCsvFile = writeInvalidEntriesToCSV(invalidEntries);
			}
		}

		finally {
			logger.info("Start - parseAndLoadCSV() method");
		}
		return invalidCsvFile;
	}

	private File writeInvalidEntriesToCSV(List<String[]> invalidEntries) throws IOException {
		File invalidCsvFile = new File("invalid_entries.csv");

		try (BufferedWriter writer = Files.newBufferedWriter(invalidCsvFile.toPath());
				CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("batchCode", "medicineCode",
						"medicineTypeCode", "weight", "price", "refrigeration", "errorMessages"))) {

			for (String[] entry : invalidEntries) {
				csvPrinter.printRecord((Object[]) entry);
			}
		}

		return invalidCsvFile;
	}

	@Override
	public byte[] generatePDF(List<BatchVO> batchVOList) throws DocumentException {
		// create PDF Document
		logger.info("Start - generatePDF() method");
		try {

			Document document = new Document();
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

			PdfWriter.getInstance(document, byteArrayOutputStream);

			document.open();

			PdfPTable table = new PdfPTable(9);
			table.addCell("Batch Code");
			table.addCell("Medicine\nCode");
			table.addCell("Weight");
			table.addCell("Price");
			table.addCell("Medicine\nType\nCode");
			table.addCell("Shipping\nCharge");
			table.addCell("Refrigeration");
			table.addCell("weight\nRange");
			table.addCell("Care\nLevel\nMessage");
			for (BatchVO batchVO : batchVOList) {
				table.addCell(batchVO.getBatchCode());
				table.addCell(batchVO.getMedicineMasterEntity().getMedicineCode());
				table.addCell(String.valueOf(batchVO.getWeight()));
				table.addCell(String.valueOf(batchVO.getPrice()));
				table.addCell(batchVO.getMedicineTypeMasterEntity().getMedicineTypeCode());
				table.addCell(String.valueOf(batchVO.getShippingCharge()));
				table.addCell(String.valueOf(batchVO.getRefrigeration()));
				table.addCell(batchVO.getWeightRange());
				table.addCell(batchVO.getCareLevel());
			}
			document.add(table);
			document.close();

			byte[] pdfBytes = byteArrayOutputStream.toByteArray();

			return pdfBytes;
		} finally {
			logger.info("End - generatePDF() method");
		}
	}

	@Override
	public boolean deleteBatch(String id) throws PharmaException, PharmaBusinessException {
		try {
			logger.info("Start - deleteBatch() method");
			if (batchRepository.existsById(id)) {
				batchRepository.deleteById(id);
				return true;
			} else {
				throw new PharmaBusinessException(PharmaBatchConstants.BATCH_CODE_NOT_FOUND,
						PharmaBatchConstants.ERROR_CODE_BATCH_CODE_NOT_FOUND);
			}
		} finally {
			logger.info("End - deleteBatch() method");
		}
	}

}
