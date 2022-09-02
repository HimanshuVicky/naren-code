package com.assignsecurities.dm.writer.excel;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SXSSFexampleTest {

	public static void main(String[] args) throws Throwable {
		long jspStartTime = System.currentTimeMillis();
		FileInputStream inputStream = new FileInputStream("c:/Test/MA_template.xlsx");
		XSSFWorkbook wb_template = new XSSFWorkbook(inputStream);
		inputStream.close();

		SXSSFWorkbook wb = new SXSSFWorkbook(wb_template);
		wb.setCompressTempFiles(true);

		SXSSFSheet sh = (SXSSFSheet) wb.getSheetAt(1);
		sh.setRandomAccessWindowSize(100);// keep 100 rows in memory, exceeding
											// rows will be flushed to disk
		for (int rownum = 4; rownum < 1048580; rownum++) {
			Row row = sh.createRow(rownum);
			System.out.println(rownum);
			for (int cellnum = 0; cellnum < 10; cellnum++) {
				Cell cell = row.createCell(cellnum);
				String address = new CellReference(cell).formatAsString();
				cell.setCellValue(address);
			}

		}
		 sh = (SXSSFSheet) wb.getSheetAt(2);
			sh.setRandomAccessWindowSize(100);// keep 100 rows in memory, exceeding
												// rows will be flushed to disk
			for (int rownum = 4; rownum < 1048580; rownum++) {
				Row row = sh.createRow(rownum);
				System.out.println(rownum);
				for (int cellnum = 0; cellnum < 10; cellnum++) {
					Cell cell = row.createCell(cellnum);
					String address = new CellReference(cell).formatAsString();
					cell.setCellValue(address);
				}

			}
		FileOutputStream out = new FileOutputStream("c:/Test/tempsxssf.xlsx");
		wb.write(out);
		out.close();
		System.out.println("Done in......."+(System.currentTimeMillis() - jspStartTime));
	}

}
