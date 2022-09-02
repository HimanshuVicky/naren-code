package com.assignsecurities.service.impl.doc.processor;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.service.impl.doc.processor.customeragreement.CustomerAgreemntDocPlaceHolderDataProcessor;
import com.assignsecurities.service.impl.doc.processor.other.OtherPlaceHolderDataProcessor;
import com.assignsecurities.service.impl.doc.processor.referral.ReferralDocPlaceHolderDataProcessor;
import com.assignsecurities.service.impl.doc.processor.rta.RtaDocPlaceHolderDataProcessor;
import com.assignsecurities.service.impl.doc.processor.test.TestDocPlaceHolderDataProcessor;

public class DocPlaceHolderDataProcessorFactory {
	public static DocPlaceHolderDataProcessor getInstance(DocTemplateEnum docTemplateEnum) {
		DocPlaceHolderDataProcessor processor =null;
		switch (docTemplateEnum) {
		case AFFIDAVIT_KARVY:
//			System.out.println("Low level");
			break;
		case CustomerAgreement:
			processor = new CustomerAgreemntDocPlaceHolderDataProcessor();
			break;
		case RTALetter:
		case RTALetter1:
			processor = new RtaDocPlaceHolderDataProcessor();
			break;
		case Other:
			processor = new OtherPlaceHolderDataProcessor();
			break;
		case TEST:
			processor = new TestDocPlaceHolderDataProcessor();
			break;
		case ReferralAgreement:
		case FranchiseReferralAgreement:
			processor = new ReferralDocPlaceHolderDataProcessor();
			break;
		default:
			throw new ServiceException("UnSupported Operation");
		}
		return processor;
	}
}
