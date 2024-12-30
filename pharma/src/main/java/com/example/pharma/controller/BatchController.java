package com.example.pharma.controller;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.pharma.exception.PharmaBusinessException;
import com.example.pharma.exception.PharmaException;
import com.example.pharma.service.BatchService;
import com.example.pharma.vo.BatchVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
public class BatchController {
	@Autowired
	private BatchService batchService;

	// Creating a Logger instance using SLF4J
	private static final Logger logger = LoggerFactory.getLogger(BatchService.class);

	@PostMapping("/add")
	public ResponseEntity<String> addBatch(@Validated @RequestBody BatchVO batchVO) {

		try {
			logger.info("Start - addBatch method");
			if (batchService.addBatch(batchVO)) {
				return ResponseEntity.ok("Batch added successfully.");
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add batch.");
			}

		} catch (PharmaBusinessException | PharmaException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} finally {
			logger.info("End - addBatch method");
		}
	}

	@GetMapping("/batchinfos")
	@Cacheable(value = "cacheBatchInfo")
	public ResponseEntity<Page<BatchVO>> findAllBatchInfo(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "2") int size) {
		logger.info("Start - findAllBatchInfo() method");
		try {
			Pageable pageable = PageRequest.of(page, size);
			return new ResponseEntity<>(batchService.findAllBatchInfo(pageable), HttpStatus.OK);
		} catch (PharmaBusinessException | PharmaException e) {
			logger.error("Error occurred while fetching batch information", e);
			Page<BatchVO> emptyPage = Page.empty(); // This creates an empty page with no data
			return new ResponseEntity<>(emptyPage, HttpStatus.INTERNAL_SERVER_ERROR);

		} finally {
			logger.info("End - findAllBatchInfo() method");
		}
	}

	@GetMapping(value = "/generatepdf", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> generatePDF() {
		logger.info("Start - generatePDF() method");
		try {
			List<BatchVO> batchList = batchService.findAllBatchInfo();
			byte[] pdfContent = batchService.generatePDF(batchList);

			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/pdf");
			headers.add("Content-Disposition", "attachment; filename=document.pdf");

			// return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);

			return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF) // Ensure the content
																								// type is set correctly
					.body(pdfContent); //
		} catch (Exception e) {
			logger.error("Error occurred while generating pdf file for batch information", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} finally {
			logger.info("End - generatePDF() method");
		}
	}

	@PutMapping("/batchinfos/{id}")
	@CacheEvict(value = "cacheBatchInfo")
	public ResponseEntity<String> updateBatch(@PathVariable String id, @RequestBody BatchVO batchVO) {
		logger.info("Start - updateBatch() method");
		try {
			if (batchService.updateBatch(id, batchVO)) {
				return ResponseEntity.ok("batch_info table updated with new values");
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update Batch");
			}
		} catch (PharmaBusinessException | PharmaException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} finally {
			logger.info("End - updateBatch() method");
		}

	}

	@DeleteMapping("/batchinfos/{id}")
	@CacheEvict(value = "cacheBatchInfo")
	public ResponseEntity<String> deleteBatch(@PathVariable String id) {
		logger.info("Start - deleteBatch() method");
		try {
			boolean isDeleted = batchService.deleteBatch(id);
			if (isDeleted) {
				return ResponseEntity.ok("Batch deleted successfully.");
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Batch not found.");
			}
		} catch (PharmaBusinessException | PharmaException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} finally {
			logger.info("End - deleteBatch() method");
		}
	}

	@Operation(summary = "Upload a CSV file", description = "Upload a CSV file to the server", responses = {
			@ApiResponse(responseCode = "200", description = "File uploaded successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid file format") })
	@PostMapping(value = "/upload", consumes = "multipart/form-data")
	@Transactional
	public ResponseEntity<?> uploadCSV(@RequestParam("file") MultipartFile file) {
		logger.info("Start - uploadCSV() method");
		File invalidCsvFile = null;
		try {

			// Validate file type (optional)
			if (!file.getContentType().equals("text/csv")) {
				return ResponseEntity.badRequest().body("Invalid file type. Please upload a CSV file.");
			}
			invalidCsvFile = batchService.parseAndLoadCSV(file);
			if (invalidCsvFile != null && invalidCsvFile.exists()) {
				// Send back the invalid entries CSV file
				return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=invalid_entries.csv")
						.contentType(MediaType.parseMediaType("text/csv")).body(new FileSystemResource(invalidCsvFile));
			}
			return ResponseEntity.ok("File uploaded and data saved to DB.");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file.");
		} finally {
			logger.info("End - uploadCSV() method");
		}
	}
}