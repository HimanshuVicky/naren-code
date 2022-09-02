package com.assignsecurities.service.impl.doc.processor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.PositionInParagraph;
import org.apache.poi.xwpf.usermodel.TextSegement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGrid;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.bean.ColumnBean;
import com.assignsecurities.bean.RowBean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WordPlaceHolderReplacer {

	private static final String MISSING = " Missing***";

	public static void replaceTextInDoc(String srcPath, String destPath, String destPathPdf,
			Map<String, String> substitutionData, Map<String, List<RowBean>> tableData) {
		try {
			Path templatePath = Paths.get(srcPath);
			XWPFDocument doc = new XWPFDocument(Files.newInputStream(templatePath));
			doc = replaceText(doc, substitutionData, tableData);
			saveWord(destPath, doc);
			wordToPDFPOI(destPath, destPathPdf);
		} catch (Exception e) {

			throw new ServiceException(e);
		}
	}

	private static XWPFDocument replaceText(XWPFDocument doc, Map<String, String> substitutionData,
			Map<String, List<RowBean>> tableData) {
		Iterator<XWPFParagraph> iterator = doc.getParagraphsIterator();
		XWPFParagraph para;
		while (iterator.hasNext()) {
			para = iterator.next();
			replaceInPara(para, substitutionData);
		}
		replaceInTable(doc, substitutionData);
		for (XWPFParagraph p : doc.getParagraphs()) {
			List<XWPFRun> runs = p.getRuns();
			if (runs != null) {
				for (XWPFRun r : runs) {
					substitutionData.entrySet().forEach(findText -> {
						setTextValue(r, findText);
					});
				}
			}
		}
		for (XWPFTable tbl : doc.getTables()) {
			for (XWPFTableRow row : tbl.getRows()) {
				for (XWPFTableCell cell : row.getTableCells()) {
					for (XWPFParagraph p : cell.getParagraphs()) {
						for (XWPFRun r : p.getRuns()) {
							substitutionData.entrySet().forEach(findText -> {
								setTextValue(r, findText);
							});
						}
					}
				}
			}
		} 
		replaceTable(doc, tableData);
		return doc;
	}
	
	

	private static void setTextValue(XWPFRun r, Map.Entry<String, String> findText) {
		String text = r.getText(0);
		Matcher matcher = matcher(text);
		if (Objects.nonNull(matcher) &&  matcher.find()) {
	//		System.out.println("findText.getKey()===>"+findText.getKey());
			if (text != null && text.contains(findText.getKey().trim())) {
				String valueToReplace = findText.getValue();
				if (Objects.isNull(valueToReplace) || "null".equalsIgnoreCase(valueToReplace)) {
					valueToReplace = findText.getKey()+MISSING;
				}
	//			System.out.println("valueToReplace===>"+ valueToReplace);
				text = text.replace(findText.getKey(), valueToReplace);
				r.setText(text, 0);
			}
		}
	}

	private static void saveWord(String filePath, XWPFDocument doc) throws FileNotFoundException, IOException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(filePath);
			doc.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
			out.close();
			} catch (Exception e) {
				//ignore
			}
		}
	}

	/* Replaces table */
	private static long replaceTable(XWPFDocument doc, Map<String, List<RowBean>> tableData) {
		XWPFTable table = null;
		long count = 0;
		if (Objects.isNull(tableData) || tableData.isEmpty()) {
			return count;
		}
		for (Entry<String, List<RowBean>> dataTable : tableData.entrySet()) {
			if (Objects.isNull(dataTable) || Objects.isNull(dataTable.getValue()) || dataTable.getValue().isEmpty()) {
				return count;
			}
			for (XWPFParagraph paragraph : doc.getParagraphs()) {
				List<XWPFRun> runs = paragraph.getRuns();
				String find = dataTable.getKey();
				TextSegement found = paragraph.searchText(find, new PositionInParagraph());
				if (found != null) {
					count++;
					if (found.getBeginRun() == found.getEndRun()) {
						// whole search string is in one Run
						XmlCursor cursor = paragraph.getCTP().newCursor();
						table = doc.insertNewTbl(cursor);
						XWPFRun run = runs.get(found.getBeginRun());
						// Clear the "%TABLE" from doc
						String runText = run.getText(run.getTextPosition());
						String replaced = runText.replace(find, "");
						run.setText(replaced, 0);
					} else {
						// The search string spans over more than one Run
						StringBuilder b = new StringBuilder();
						for (int runPos = found.getBeginRun(); runPos <= found.getEndRun(); runPos++) {
							XWPFRun run = runs.get(runPos);
							b.append(run.getText(run.getTextPosition()));
						}
						String connectedRuns = b.toString();
						XmlCursor cursor = paragraph.getCTP().newCursor();
						table = doc.insertNewTbl(cursor);
						String replaced = connectedRuns.replace(find, ""); // Clear search text

						// The first Run receives the replaced String of all connected Runs
						XWPFRun partOne = runs.get(found.getBeginRun());
						partOne.setText(replaced, 0);
						// Removing the text in the other Runs.
						for (int runPos = found.getBeginRun() + 1; runPos <= found.getEndRun(); runPos++) {
							XWPFRun partNext = runs.get(runPos);
							partNext.setText("", 0);
						}
					}
				}
			}
			if(Objects.nonNull(table)) {
				fillTable(table, dataTable.getValue());
			}
		}
		return count;
	}

	private static void fillTable(XWPFTable table, List<RowBean> rows) {
		if (Objects.isNull(rows) || rows.isEmpty()) {
			return;
		}
		RowBean firstRow = null;
		firstRow  = rows.get(0);
		for (RowBean row : rows) {
			if (Objects.isNull(row.getColumns()) || row.getColumns().isEmpty()) {
				return;
			}
			CTTblGrid grid = table.getCTTbl().getTblGrid();
			if (grid == null) {
				table.getCTTbl().addNewTblGrid();
			}
			XWPFTableRow tableRow = table.createRow();
			
			for (ColumnBean column : row.getColumns()) {
				XWPFTableCell tableCell = tableRow.addNewTableCell();
				XWPFParagraph paragraph = tableCell.addParagraph();
				XWPFRun currRun = paragraph.createRun();
				currRun.setText(column.getColValue());
				ParagraphAlignment paragraphAlignment = ParagraphAlignment.CENTER;
				if ("Left".equalsIgnoreCase(column.getHAlognment())) {
					paragraphAlignment = ParagraphAlignment.LEFT;
				} else if ("Left".equalsIgnoreCase(column.getHAlognment())) {
					paragraphAlignment = ParagraphAlignment.RIGHT;
				}
				paragraph.setAlignment(paragraphAlignment);
//				  CTTcPr ctPr = cell.getCTTc().addNewTcPr();
				CTTc cttc = tableCell.getCTTc();
				CTP ctp = cttc.getPList().get(0);
				CTPPr ctppr = ctp.getPPr();
				if (ctppr == null) {
					ctppr = ctp.addNewPPr();
				}
				CTJc ctjc = ctppr.getJc();
				if (ctjc == null) {
					ctjc = ctppr.addNewJc();
				}
				if ("Left".equalsIgnoreCase(column.getHAlognment())) {
					ctjc.setVal(STJc.LEFT);
				} else if ("Left".equalsIgnoreCase(column.getHAlognment())) {
					ctjc.setVal(STJc.RIGHT);
				} else {
					ctjc.setVal(STJc.CENTER);
				}
			}
		}
		table.removeRow(0);
		
		for (XWPFTableRow row : table.getRows()) {
			int counter = 0;
			int size = row.getTableCells().size();
			log.info("size===>" + size);
//    		int tableWidht = ((100/size-1)*100)+(1000*(size/2));
			int percentWidth = 100 / (size - 1);
			int tableWidht = percentWidth * 100;
			int cellCounter=0;
			int orgtableWidht=tableWidht;
			for (XWPFTableCell cell : row.getTableCells()) {
				
				if(Objects.nonNull(firstRow) && firstRow.getColumns().size()> cellCounter 
						&& firstRow.getColumns().get(cellCounter).getColWidht()>0) {
					tableWidht= firstRow.getColumns().get(cellCounter).getColWidht();
					log.info("Mod:::tableWidht===>" + tableWidht);
				}else if(Objects.nonNull(firstRow) && (Objects.nonNull(firstRow.getLastColWidht())
						&& firstRow.getLastColWidht()>0)) {
					tableWidht = firstRow.getLastColWidht();
				}
				log.info("tableWidht===>" + tableWidht);
				CTTblWidth ctTblWidth = cell.getCTTc().addNewTcPr().addNewTcW();
				if (0 == counter++) {
					ctTblWidth.setW(new BigInteger("1"));
				} else {
					ctTblWidth.setW(new BigInteger("" + tableWidht));
				}
//    	        ctTblWidth.setType(STTblWidth.AUTO);
				ctTblWidth.setType(STTblWidth.DXA);
				cellCounter++;
			}
		}
		int counter = 0;
		for (RowBean row : rows) {
			if (Objects.isNull(row.getColumns()) || row.getColumns().isEmpty()) {
				return;
			}
			if(Objects.nonNull(row.getFromCell()) && Objects.nonNull(row.getToCell())) {
				spanCellsAcrossRow(table, counter, row.getFromCell(), row.getToCell());
			}
			counter++;
		}
//		mergeCellsHorizontal(table, 0, 2, 2);
//		spanCellsAcrossRow(table, 0, 2, 2);
//		spanCellsAcrossRow(table, 1, 1, 1);
	}
	/*
	private static void mergeCellsHorizontal(XWPFTable table, int row, int fromCell, int toCell) {  
//		CTVMerge vmerge = CTVMerge.Factory.newInstance();
        for (int cellIndex = fromCell; cellIndex <= toCell; cellIndex++) {  
            XWPFTableCell cell = table.getRow(row).getCell(cellIndex);
            CTTcPr ctPr = cell.getCTTc().addNewTcPr();
            if ( cellIndex == fromCell ) {  
//            	vmerge.setVal(STMerge.RESTART);
                // The first merged cell is set with RESTART merge value  
//                cell.getCTTc().getTcPr().setVMerge(vmerge);
            	ctPr.addNewHMerge().setVal(STMerge.RESTART);
            } else {
//            	vmerge.setVal(STMerge.CONTINUE);
                // Cells which join (merge) the first one, are set with CONTINUE  
            	ctPr.addNewHMerge().setVal(STMerge.CONTINUE);
//            	cell.getCTTc().getTcPr().setVMerge(vmerge);
            }  
        }  
    }  
    */
	
	private static void spanCellsAcrossRow(XWPFTable table, int rowNum, int colNum, int span) {
	    XWPFTableCell  cell = table.getRow(rowNum).getCell(colNum);
	    cell.getCTTc().getTcPr().addNewGridSpan();
	    cell.getCTTc().getTcPr().getGridSpan().setVal(BigInteger.valueOf((long)span));
	}

	public static void wordToPDFPOI(String srcPath, String destPath) throws Exception {
		log.info("inputFile:" + srcPath + ",outputFile:" + destPath);
		FileInputStream in = new FileInputStream(srcPath);
		XWPFDocument document = new XWPFDocument(in);
		File outFile = new File(destPath);
		OutputStream out = new FileOutputStream(outFile);
		PdfOptions options = PdfOptions.create();
		document.createNumbering();
		PdfConverter.getInstance().convert(document, out, options);
	}

	public static void main(String[] args) {
		String srcPath = "E:/ShareProject/docx/template/myTeplate.docx";
		String destPath = "E:/ShareProject/docx/docs/myFileNC_4.docx";
		String destPathPdf = "E:/ShareProject/docx/docs/myFile_nrc.pdf";

		Map<String, String> substitutionData = new HashMap<>();
		substitutionData.put("${name}", "Narendra Chouhan");
		substitutionData.put("${first}", "Dishika Chouhan");
		substitutionData.put("${second}", "Anvika Chouhan");
		substitutionData.put("${age}", "26");
		substitutionData.put("${shareholder}", "Rashmi Chouhan");

		Map<String, List<RowBean>> tableData = new HashMap<>();
		List<RowBean> rows = new ArrayList<>();
		List<ColumnBean> columns = new ArrayList<>();

		columns.add(ColumnBean.builder().colValue("Share Certificate No").build());
		columns.add(
				ColumnBean.builder().colValue("Distinctive No\r\n" + "From                      To\r\n" + "").build());
		columns.add(ColumnBean.builder().colValue("No of Shares").build());
		rows.add(RowBean.builder().columns(columns).build());

		columns = new ArrayList<>();
		columns.add(ColumnBean.builder().colValue("Cert 1").build());
		columns.add(ColumnBean.builder().colValue("2-Jan-2020                      2-Jan-2021").build());
		columns.add(ColumnBean.builder().colValue("125").build());
		rows.add(RowBean.builder().columns(columns).build());

		columns = new ArrayList<>();
		columns.add(ColumnBean.builder().colValue("Cert 2").build());
		columns.add(ColumnBean.builder().colValue("12-Sep-2019                      11-Sep-2021").build());
		columns.add(ColumnBean.builder().colValue("125").build());
		rows.add(RowBean.builder().columns(columns).build());

		columns = new ArrayList<>();
		columns.add(ColumnBean.builder().colValue("Cert 3").build());
		columns.add(ColumnBean.builder().colValue("2-Mar-2018                      12-Jan-2020").build());
		columns.add(ColumnBean.builder().colValue("125").build());
		rows.add(RowBean.builder().columns(columns).build());

		tableData.put("${SharesTable}", rows);

		rows = new ArrayList<>();
		columns = new ArrayList<>();
		columns.add(ColumnBean.builder().colValue("Name").build());
		columns.add(ColumnBean.builder().colValue("Address").build());
		columns.add(ColumnBean.builder().colValue("City").build());
		columns.add(ColumnBean.builder().colValue("Pin").build());
		rows.add(RowBean.builder().columns(columns).build());

		columns = new ArrayList<>();
		columns.add(ColumnBean.builder().colValue("Niranjan Mantri").build());
		columns.add(ColumnBean.builder().colValue(
				"Sr. No. 78/60, Plot No. 1, Sai Vision, A, Flat N Kunal Icon Rd, Pimple Saudagar PUNE Maharashtra")
				.build());
		columns.add(ColumnBean.builder().colValue("Pune").build());
		columns.add(ColumnBean.builder().colValue("411027").build());
		rows.add(RowBean.builder().columns(columns).build());

		tableData.put("${SharesTable11}", rows);
		replaceTextInDoc(srcPath, destPath, destPathPdf, substitutionData, tableData);
		System.out.println("<================Done===============>");
	}
	private static void replaceInPara(XWPFParagraph para, Map<String, String> param) {
		List<XWPFRun> runs;
		String tempString = "";
		char lastChar = ' ';
//		String paraText = para.getParagraphText();
		if (matcher(para.getParagraphText()).find()) {
			runs = para.getRuns();
			Set<XWPFRun> runSet = new HashSet<XWPFRun>();
			for (XWPFRun run : runs) {
         	   	String text = run.getText(0);
//         	   	System.out.println("=======>"+text);
                if(text==null)continue;
                text = replaceText(text,param);
                run.setText("",0);
                run.setText(text,0);
                  
                for(int i=0;i<text.length();i++){
             	   char ch = text.charAt(i);
                    if(ch == '$'){
                       runSet = new HashSet<XWPFRun>();
                       runSet.add(run);
                       tempString = text;
                    }else if(ch == '{'){
                 	   if(lastChar == '$'){
                 		   if(runSet.contains(run)){
                    				  
                 		   }else{
                 			   runSet.add(run);
                 			   tempString = tempString+text;
                 		   }
                 	   }else{
                 		   runSet = new HashSet<XWPFRun>(); 
                 		   tempString = "";
                 	   }
                    }else if(ch == '}'){
                 	   if(tempString!=null&&tempString.indexOf("${")>=0){
                 		   if(runSet.contains(run)){
                				  
                 		   }else{
                 			   runSet.add(run);
                 			   tempString = tempString+text;
                 		   }
                 	   }else{
                 		   runSet = new HashSet<XWPFRun>(); 
                 		   tempString = ""; 
                 	   }
                 	   if(runSet.size()>0){
                 		   String replaceText = replaceText(tempString,param);
                 		   if(!replaceText.equals(tempString)){
                 			   int index = 0;
                 			   XWPFRun aRun = null;
                 			   for(XWPFRun tempRun:runSet){
                 				   tempRun.setText("",0);
                 				   if(index==0){
                 					   aRun = tempRun;
                 				   }
                 				   index++;
                 			   }
                 			   aRun.setText(replaceText,0);
                 		   }
                 		   runSet = new HashSet<XWPFRun>(); 
                 		   tempString = ""; 
                 	   }
                    }else{
                 	   if(runSet.size()<=0)continue;
                 	   if(runSet.contains(run))continue;
                 	   runSet.add(run);
                 	   tempString = tempString+text;
                    }
                    lastChar = ch;
                }
            }
			
			// This method will cause the placeholder to be disassembled and parsed, not recognized and replaced
			/*for (int i = 0; i < runs.size(); i++) {
				XWPFRun run = runs.get(i);
				String runText = run.toString();
				System.out.println("====>run:"+runText);
				matcher = this.matcher(runText);
				if (matcher.find()) {
					while ((matcher = this.matcher(runText)).find()) {
						runText = matcher.replaceFirst(String.valueOf(param.get(matcher.group(1))));
					}
					 / / Directly call XWPFRun's setText () method to set the text, at the bottom will re-create an XWPFRun, the text is appended to the current text,
					 // So we can't set the value directly, we need to delete the current run first, and then manually insert a new run.
					para.removeRun(i);
					if(runText.equals("null")){
						runText="";
					}
					para.insertNewRun(i).setText(runText);
				}
			}*/
		}
	}
	
	/**
	  * Replace the variables in the table
	  * @param doc document to be replaced
	  * @param params parameter
	 */
	private static void replaceInTable(XWPFDocument doc, Map<String, String> params) {
		Iterator<XWPFTable> iterator = doc.getTablesIterator();
		XWPFTable table;
		List<XWPFTableRow> rows;
		List<XWPFTableCell> cells;
		List<XWPFParagraph> paras;
		while (iterator.hasNext()) {
			table = iterator.next();
			rows = table.getRows();
			for (XWPFTableRow row : rows) {
				cells = row.getTableCells();
				for (XWPFTableCell cell : cells) {
					
					// This method will cause the format in the table to be lost
					/*String cellTextString = cell.getText();
                   for (Entry<String, Object> e : params.entrySet()) {
                       if (cellTextString.contains("${"+e.getKey()+"}"))
                           cellTextString = cellTextString.replace("${"+e.getKey()+"}", e.getValue().toString());
                   }
                   cell.removeParagraph(0);
                   if(cellTextString.contains("${") && cellTextString.contains("}")){
                   	cellTextString = "";
                   }
                   cell.setText(cellTextString);*/
					
					// Call the paragraph to replace the placeholder way
					paras = cell.getParagraphs();
					for (XWPFParagraph para : paras) {
						replaceInPara(para, params);
					}
					
				}
			}
		}
	}
	
	/**
	  * Replace placeholders
	 * @param text
	 * @param map
	 * @return
	 */
  	private static String replaceText(String text, Map<String, String> map) {
  		String valueKey="";
		if(text != null){
			/*for (Entry<String, String> entry : map.entrySet()) {
				if (text.contains("${"+entry.getKey()+"}")){
					text = text.replace("${"+entry.getKey()+"}", entry.getValue().toString());
				}
			}*/
			
			Matcher matcher = matcher(text);
			if (matcher.find()) {
				while ((matcher = matcher(text)).find()) {
					valueKey = matcher.group(1);
					text = matcher.replaceFirst(String.valueOf(map.get(matcher.group(1))));
				}
				
			}
		}
		if(Objects.isNull(text) || text.equalsIgnoreCase("null")){
			text=valueKey + MISSING;
		}
//		System.out.println("Before ::  replaceText::text=>"+text+"<=======");
//		text = text.trim();
//		text = StringUtils.replace(text, "null", "");
		text = text.replace("null", valueKey + MISSING);
//		System.out.println("replaceText::text=>"+text+"<=======");
		return text;
	}
	
	/**
	  * Regular match string
	 * @param str
	 * @return
	 */
	private static Matcher matcher(String str) {
		if(Objects.nonNull(str)) {
			Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}", Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(str);
			return matcher;
		}
		return null;
	}
	
	
}