package com.assignsecurities.service.impl.doc.processor;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.service.impl.doc.processor.customeragreement.CustomerAgreemntDocTablePlaceHolderDataProcessor;
import com.assignsecurities.service.impl.doc.processor.other.table.OtherTablePlaceHolderDataProcessor;
import com.assignsecurities.service.impl.doc.processor.referral.ReferralDocTablePlaceHolderDataProcessor;
import com.assignsecurities.service.impl.doc.processor.rta.table.RtaDocTablePlaceHolderDataProcessor;
import com.assignsecurities.service.impl.doc.processor.test.TestDocTablePlaceHolderDataProcessor;

public class DocTablePlaceHolderDataProcessorFactory {

	public static DocTablePlaceHolderDataProcessor getInstance(DocTemplateEnum docTemplateEnum) {
		DocTablePlaceHolderDataProcessor processor =null;
		switch (docTemplateEnum) {
		case AFFIDAVIT_KARVY:
//			System.out.println("Low level");
			break;
		case CustomerAgreement:
			processor = new CustomerAgreemntDocTablePlaceHolderDataProcessor();
			break;
		
		case RTALetter:
		case RTALetter1:
			processor = new RtaDocTablePlaceHolderDataProcessor();
			break;
		case Other:
			processor = new OtherTablePlaceHolderDataProcessor();
			break;
		case TEST:
			processor = new TestDocTablePlaceHolderDataProcessor();
			break;
		case ReferralAgreement:
		case FranchiseReferralAgreement:
			processor = new ReferralDocTablePlaceHolderDataProcessor();
			break;
		default:
			throw new ServiceException("UnSupported Operation");
		}
		return processor;
	}
}
