package com.assignsecurities.service.impl.doc.processor.test;

import java.util.HashMap;
import java.util.Map;

import com.assignsecurities.service.impl.doc.processor.DocPlaceHolderDataProcessor;

public class TestDocPlaceHolderDataProcessor extends DocPlaceHolderDataProcessor{
	@Override
	public Map<String, String> preparePlaceHolderData(Object obj){
		Map<String, String> substitutionData = new HashMap<>();
		substitutionData.put("${name}", "Narendra Chouhan");
		substitutionData.put("${first}", "Dishika Chouhan");
		substitutionData.put("${second}", "Anvika Chouhan");
		substitutionData.put("${age}", "26");
		substitutionData.put("${shareholder}", "Rashmi Chouhan");
		return substitutionData;

	}
}
