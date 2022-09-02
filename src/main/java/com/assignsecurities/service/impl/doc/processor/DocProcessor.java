package com.assignsecurities.service.impl.doc.processor;

import java.util.List;
import java.util.Map;

import com.assignsecurities.bean.RowBean;

public class DocProcessor {

	public void replaceTextInDoc(String srcPath, String destPath, String destPathPdf, DocTemplateEnum docTemplateEnum, Object objPlaceHolder, Object forTable) {
		Map<String, String> substitutionData = DocPlaceHolderDataProcessorFactory.getInstance(docTemplateEnum).preparePlaceHolderData(objPlaceHolder);
		Map<String, List<RowBean>> tableData = DocTablePlaceHolderDataProcessorFactory.getInstance(docTemplateEnum).preparePlaceHolderData(forTable);
		WordPlaceHolderReplacer.replaceTextInDoc(srcPath, destPath, destPathPdf, substitutionData, tableData);
	}
	public static void main(String[] args) {
		DocProcessor docProcessor = new DocProcessor();
		String srcPath = "E:/ShareProject/docx/template/myTeplate.docx";
		String destPath = "E:/ShareProject/docx/docs/myFileNC_4.docx";
		String destPathPdf = "E:/ShareProject/docx/docs/myFile_nrc.pdf";
		DocTemplateEnum docTemplateEnum = DocTemplateEnum.TEST;
		Object objPlaceHolder = null;
		Object forTable = null;//This is template specific
		docProcessor.replaceTextInDoc(srcPath, destPath, destPathPdf, docTemplateEnum, objPlaceHolder,forTable);
		System.out.println("DocProcessor : Executed Successfully ");
	}
}
