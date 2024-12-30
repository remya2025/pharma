package com.example.pharma.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.pharma.exception.PharmaBusinessException;
import com.example.pharma.exception.PharmaException;
import com.example.pharma.vo.BatchVO;
import com.itextpdf.text.DocumentException;

@Service
public class BatchService implements IBatchBO {
	@Autowired
	private IBatchBO batchBO;

	@Override
	public boolean addBatch(BatchVO batchVO) throws PharmaBusinessException, PharmaException {
		return batchBO.addBatch(batchVO);
	}

	@Override
	public boolean updateBatch(String id, BatchVO batchVO) throws PharmaBusinessException, PharmaException {
		return batchBO.updateBatch(id, batchVO);
	}

	@Override
	public Page<BatchVO> findAllBatchInfo(Pageable pageable) throws PharmaBusinessException, PharmaException {
		return batchBO.findAllBatchInfo(pageable);
	}

	@Override
	public List<BatchVO> findAllBatchInfo() throws PharmaBusinessException, PharmaException {
		return batchBO.findAllBatchInfo();
	}

	@Override
	public File parseAndLoadCSV(MultipartFile file) throws PharmaBusinessException, PharmaException, IOException {
		return batchBO.parseAndLoadCSV(file);
	}

	@Override
	public byte[] generatePDF(List<BatchVO> batchVOList) throws DocumentException {
		return batchBO.generatePDF(batchVOList);
	}

	@Override
	public boolean deleteBatch(String id) throws PharmaException, PharmaBusinessException {
		return batchBO.deleteBatch(id);
	}

}
