/**
 * 
 */
package com.example.pharma.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.example.pharma.exception.PharmaBusinessException;
import com.example.pharma.exception.PharmaException;
import com.example.pharma.vo.BatchVO;
import com.itextpdf.text.DocumentException;

/**
 * 
 */
public interface IBatchBO {

	public abstract boolean addBatch(BatchVO batchVO) throws PharmaBusinessException, PharmaException;

	public abstract boolean updateBatch(String id, BatchVO batchVO) throws PharmaBusinessException, PharmaException;

	public abstract Page<BatchVO> findAllBatchInfo(Pageable pageable) throws PharmaBusinessException, PharmaException;

	public abstract List<BatchVO> findAllBatchInfo() throws PharmaBusinessException, PharmaException;

	public abstract File parseAndLoadCSV(MultipartFile file)
			throws PharmaBusinessException, PharmaException, IOException;

	public abstract byte[] generatePDF(List<BatchVO> batchVOList) throws DocumentException;

	public abstract boolean deleteBatch(String id) throws PharmaException, PharmaBusinessException;
}
