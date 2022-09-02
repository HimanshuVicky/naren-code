package com.assignsecurities.dm.reader.excel;

import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.assignsecurities.domain.dm.ObjectConfigBean;
import com.assignsecurities.domain.dm.excel.ExcelDataColumnModel;
import com.assignsecurities.domain.dm.excel.ExcelDataRowModel;



public class ExcelTemplateSheetHandler extends DefaultHandler {
	private boolean nextIsString;
	private StringBuffer value;
	private int noOfHeaderCols;
	private int countrows;
	private int countColumn;
	private SheetHandler.xssfDataType nextDataType;
	private int formatIndex;
	private String formatString;
	private StylesTable stylesTable;
	private ObjectConfigBean objDefModels;
	private SharedStringsTable sst;
	private ExcelDataRowModel dataRowModel;

	public ExcelTemplateSheetHandler(StylesTable styles,
			SharedStringsTable sst, ObjectConfigBean objectConfigModel) {
		this.stylesTable = styles;
		this.sst = sst;
		this.noOfHeaderCols = objectConfigModel.getHeaderRowNumber();
		this.noOfHeaderCols=this.noOfHeaderCols-1;
		this.objDefModels = objectConfigModel;
		countrows = 0;
		countColumn = 0;
		value=new StringBuffer(0);
	}

	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		if (("inlineStr".equals(name)) || ("v".equals(name))) {
			this.nextIsString = true;

			this.value.setLength(0);

		} else {
			nextIsString = false;
		}
		if ("c".equals(name) && countrows + 1 >= noOfHeaderCols) {

			this.nextDataType = SheetHandler.xssfDataType.NUMBER;
			this.formatIndex = -1;
			this.formatString = null;
			String cellType = attributes.getValue("t");
			String cellStyleStr = attributes.getValue("s");
			if ("b".equals(cellType)) {
				this.nextDataType = SheetHandler.xssfDataType.BOOL;
			} else if ("e".equals(cellType)) {
				this.nextDataType = SheetHandler.xssfDataType.ERROR;
			} else if ("inlineStr".equals(cellType)) {
				this.nextDataType = SheetHandler.xssfDataType.INLINESTR;
			} else if ("s".equals(cellType)) {
				this.nextDataType = SheetHandler.xssfDataType.SSTINDEX;
			} else if ("str".equals(cellType)) {
				this.nextDataType = SheetHandler.xssfDataType.FORMULA;
			} else if (cellStyleStr != null) {
				int styleIndex = Integer.parseInt(cellStyleStr);
				XSSFCellStyle style = this.stylesTable.getStyleAt(styleIndex);
				this.formatIndex = style.getDataFormat();
				this.formatString = style.getDataFormatString();
				if (this.formatString == null)
					this.formatString = BuiltinFormats
							.getBuiltinFormat(this.formatIndex);
			}
		}
		if ("row".equals(name)) {
			if (countrows + 1 == noOfHeaderCols) {
				dataRowModel = new ExcelDataRowModel();
				dataRowModel.setRowIndex(countrows);
				dataRowModel.setTabIndex(objDefModels.getTabIndex());
			}
			countrows++;
			countColumn = 0;

		}
		this.value.setLength(0);
	}

	public void endElement(String uri, String localName, String name)
			throws SAXException {

		if ("v".equals(name) && countrows == noOfHeaderCols) {
			String lastContents = null;
			switch (this.nextDataType) {
			case BOOL:
				char first = this.value.charAt(0);
				lastContents = first == '0' ? "FALSE" : "TRUE";
				break;
			case ERROR:
				lastContents = this.value.toString();
				break;
			case FORMULA:
				lastContents = this.value.toString();
				break;
			case INLINESTR:
				XSSFRichTextString rtsi = new XSSFRichTextString(this.value
						.toString());
				lastContents = rtsi.toString();
				break;
			case SSTINDEX:
				String sstIndex = this.value.toString();
				try {
					int idx = Integer.parseInt(sstIndex);
					XSSFRichTextString rtss = new XSSFRichTextString(this.sst
							.getEntryAt(idx));
					lastContents = rtss.toString();
				} catch (NumberFormatException ex) {
					throw new SAXException(ex);
				}
				break;
			case NUMBER:

				break;
			default:
				lastContents = this.value.toString();
			}

			addNewExcelColumnModel(dataRowModel, lastContents);

			countColumn++;

			lastContents = "";
		}
	}

	/**
	 * 
	 * @param dataRowModel2
	 * @param lastContents2
	 */
	private void addNewExcelColumnModel(ExcelDataRowModel dataRowModel2,
			String lastContents2) {
		ExcelDataColumnModel columnModel = new ExcelDataColumnModel();
		columnModel.setColIndex(countColumn + 1);
		columnModel.setColValue(lastContents2);
		dataRowModel2.addColumns(columnModel);

	}

	/**
	 * Captures characters only if a suitable element is open. Originally was
	 * just "v"; extended for inlineStr also.
	 */
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (isNextIsString()) {
			// ch[799];
			this.value.append(ch, start, length);
		}
	}

	private boolean isNextIsString() {
		return nextIsString;
	}

	/**
	 * Converts an Excel column name like "C" to a zero-based index.
	 * 
	 * @param name
	 * @return Index corresponding to the specified name
	 */
	@SuppressWarnings("unused")
	private int nameToColumn(String name) {
		int column = -1;
		for (int i = 0; i < name.length(); ++i) {
			int c = name.charAt(i);
			column = (column + 1) * 26 + c - 'A';
		}
		return column;
	}

	/**
	 * @return the dataRowModel
	 */
	public ExcelDataRowModel getDataRowModel() {
		return dataRowModel;
	}

	/**
	 * @param dataRowModel the dataRowModel to set
	 */
	public void setDataRowModel(ExcelDataRowModel dataRowModel) {
		this.dataRowModel = dataRowModel;
	}

	/**
	 * @return the objDefModels
	 */
	public ObjectConfigBean getObjDefModels() {
		return objDefModels;
	}

	/**
	 * @param objDefModels the objDefModels to set
	 */
	public void setObjDefModels(ObjectConfigBean objDefModels) {
		this.objDefModels = objDefModels;
	}
	
}
