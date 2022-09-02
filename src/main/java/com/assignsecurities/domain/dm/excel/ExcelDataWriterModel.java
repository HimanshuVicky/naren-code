package com.assignsecurities.domain.dm.excel;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.assignsecurities.domain.dm.DataWriterModel;
import com.assignsecurities.domain.dm.ObjectConfigBean;
public class ExcelDataWriterModel implements DataWriterModel {

	private SXSSFWorkbook wb = null;
	private Object dataWriterObject = null;
	private InputStream inp = null;

	
	public void initDataWriterObject(ObjectConfigBean configModel,
									 InputStream fileIoInputStream) throws IOException,
			InvalidFormatException {
		XSSFWorkbook wb_template = new XSSFWorkbook(fileIoInputStream);
		wb = new SXSSFWorkbook(wb_template);
		wb.setCompressTempFiles(true);
	}

	public Object getExcelWorkBook() {
		return wb;
	}

	public void setExcelWorkBook(SXSSFWorkbook wb) {
		if (this.wb != null && wb==null) {
			this.wb.dispose();
		}
		this.wb = wb;
	}

	
	public void distroyDataWriterObject()
			throws IOException {
		try {
			if (wb != null) {
				wb.dispose();
				wb = null;
			}
		} catch (Exception e) {
			// ignoure exception
		}
		if (inp != null) {
			inp.close();
		}

	}

	
	public Object getDataWriterObject() {
		return dataWriterObject;
	}

	
	public void setDataWriterObject(Object dataWriterObject) {
		this.dataWriterObject = dataWriterObject;
	}

}
