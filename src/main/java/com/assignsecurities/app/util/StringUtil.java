package com.assignsecurities.app.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.assignsecurities.app.exception.ServiceException;



public class StringUtil {

	public static final String UNDERSCORE = "_";
	private static final String SEPERATOR = ",";
	private static final String TOKEN_START = "<@";
	private static final String TOKEN_END = "@>";
	private static final int TOKEN_START_LENGTH = TOKEN_START.length();
	private static final int TOKEN_END_LENGTH = TOKEN_END.length();
	private static final String NULL_REPLACEMENT = "   ";
	private static final String ZERO = "0";
	private static final String NEGATIVE_UNITY = "-1";
	public static final String EMPTY = "";
	private static final String TRUE = "true";

	public static String replaceAll(String rep, String toReplace,
			String newString) {

		boolean finished = false;

		StringBuffer sbuf = new StringBuffer(rep);
		int pos = 0;

		while (!finished) {

			int index = rep.indexOf(toReplace, pos);

			if (index != -1) {
				if (newString != null)
					sbuf.replace(index, index + toReplace.length(), newString);
				else
					sbuf.replace(index, index + toReplace.length(), "");

				rep = sbuf.toString();
				pos = index + newString.length();
			} else {
				finished = true;
			}
		}

		return sbuf.toString();
	}

	public static String getDelimitorSeparatedString(long[] valueArray,
			char delimitor) {

		StringBuffer strBuff = new StringBuffer();
		char separator = ' ';

		if (valueArray != null) {
			for (int index = 0; index < valueArray.length; index++) {
				strBuff.append(separator).append(valueArray[index]);
				separator = delimitor;
			}
		}
		return strBuff.toString();
	}

	// public static void main( String[] args ) throws Exception {
	//
	// String org = "http://hercules:8000/jtcgi/wte.pyc?superkey=session_key";
	// String toRep ="session_key";
	// String newString="abcd";
	//
	// String ret = replaceAll( org, toRep, newString );
	//
	// testProcessTemplate();
	// //System.out.println( "ret:"+ ret);
	//
	//
	// }

	/**
	 * Converts any given array to a comma seperator String
	 * 
	 * @param objArray
	 *            Array to convert to String
	 * @return Comma seperated String value
	 */
	public static String toString(Object[] objArray) {
		StringBuffer bfr = new StringBuffer();

		if ((objArray == null)) {
			// invalid Array
			return String.valueOf(objArray);
		} // end of if ()

		for (int i = 0; i < objArray.length; i++) {
			bfr.append(String.valueOf(objArray[i]));
			if (i + 1 != objArray.length) {
				bfr.append(SEPERATOR);
			}
		} // end of for (int i = 0; i < ; i++)

		return bfr.toString();
	}

	public static boolean isValidString(String value) {
		return (value != null) && (value.trim().length() > 0);
	}

	/**
	 * Converts an array of longs to a string sutable for use after an IN clause
	 * in an sql statement.
	 * 
	 * @param ids
	 *            array of ids to be included in string
	 * @return list of ids suitable for use in an in clause e.g. (1,2,3,4,5)
	 *         parens included or (-1.5) if ids is empty or null.
	 * @author mcline
	 */
	public static String getIdsSQLFromArray(long[] ids) {
		StringBuffer idsSql = new StringBuffer();
		idsSql.append('(');
		if ((ids != null) && (ids.length > 0)) {
			int numIds = ids.length;
			for (int i = 0; i < numIds; i++) {
				if (i == (numIds - 1)) {
					idsSql.append(ids[i]);
				} else {
					idsSql.append(ids[i]).append(',');
				}
			}
		} else {
			idsSql.append("-1.5");
		}
		idsSql.append(')');
		return idsSql.toString();
	}

	// TODO: Provide util methods to convert collections to comma
	// seperated String

	/*
	 * This method should be moved out
	 */
	public static boolean isNumber(String str) {
		try {
			Double.parseDouble(str);

		} catch (Throwable ex) {
			return false;
		}

		return true;

	}

	public static String[] split(String str, char delimiter) {
		// return no groups if we have an empty string
		if ((str == null) || "".equals(str)) {
			return new String[0];
		}

		ArrayList parts = new ArrayList();
		int currentIndex;
		int previousIndex = 0;

		while ((currentIndex = str.indexOf(delimiter, previousIndex)) > 0) {
			String part = str.substring(previousIndex, currentIndex).trim();
			parts.add(part);
			previousIndex = currentIndex + 1;
		}

		parts.add(str.substring(previousIndex, str.length()).trim());

		String[] result = new String[parts.size()];
		parts.toArray(result);

		return result;
	}

	/**
	 * Process a template string and replace tokens found with the value. The
	 * token will be used key to look up the value in the token map
	 * <p>
	 * The tokens are identified between a start marker (&lt;@) and end marker
	 * (@&gt;)
	 * <p>
	 * Note: If the value is not found for a token, it quietly ignored and
	 * replaced with empty space (for backward compatibility and ease of use.
	 * <p>
	 * 
	 * @usage TemplateString (template): Order id#<@ORDER_ID@>,
	 *        Name:<@ORDER_NAME@> has been published Map (values): [ORDER_ID,
	 *        101],[ORDER_NAME, 'Office Automation'] Return String: Order
	 *        id#101, Name:Office Automation has been published
	 * 
	 *        This method is same as calling processTemplate(String, Map,
	 *        boolean) with boolean flag TRUE
	 * 
	 * @param template
	 *            Template String which contains zero or more tokens to replace
	 * @param values
	 *            Map of token and values
	 * 
	 * @return Processed template String
	 */
	public static String processTemplate(String template, Map values) {
		try {
			return processTemplate(template, values, true);
		} catch (ServiceException e) {
			// This block should never be reached
		}

		// This code should never be reached
		return null;
	}

	/**
	 * Process a template string and replace tokens found with the value. The
	 * token will be used key to look up the value in the token map
	 * <p>
	 * The tokens are identified between a start marker (&lt;@) and end marker
	 * (@&gt;)
	 * <p>
	 * Note: If the value is not found for a token, it quietly ignored and
	 * replaced with empty space (for backward compatibility and ease of use.
	 * <p>
	 * 
	 * @usage TemplateString (template): Order id#<@ORDER_ID@>,
	 *        Name:<@ORDER_NAME@> has been published Map (values): [ORDER_ID,
	 *        101],[ORDER_NAME, 'Office Automation'] Return String: Order
	 *        id#101, Name:Office Automation has been published
	 * 
	 * @param template
	 *            Template String which contains zero or more tokens to replace
	 * @param values
	 *            Map of token and values
	 * 
	 * @return Processed template String
	 */
	public static String processTemplate(String template, Map values,
			boolean lenient) throws ServiceException {

		StringBuffer st = new StringBuffer(template);
		int startIndex = 0;
		int endIndex = 0;
		String replaceString = null;
		String token = null;
		Object replaceValue = null;

		do {

			startIndex = st.indexOf(TOKEN_START, startIndex);
			if (startIndex == -1) {
				return st.toString();
			}
			endIndex = st.indexOf(TOKEN_END, startIndex);
			if (endIndex == -1) {
				return st.toString();
			}
			token = st.substring(startIndex + TOKEN_START_LENGTH, endIndex);

			/***
			 * this code is added while fixing RPX testing defects in several
			 * place for ***properly replacing the template param's with values
			 **/

			String normalizedtoken = "";
			if (!Normalizer.isNormalized(token, Normalizer.Form.NFKD)) {
				normalizedtoken = Normalizer.normalize(token,
						Normalizer.Form.NFKD);
			} else {
				normalizedtoken = token;
			}
			if (!lenient && !values.containsKey(normalizedtoken)) {
				throw new ServiceException("The token: " + token
						+ " defined in template is not available");
			}

			replaceValue = values.get(normalizedtoken);

			// should we replace with blank string or throw exception?
			replaceString = (replaceValue == null ? NULL_REPLACEMENT : String
					.valueOf(replaceValue));

			st.replace(startIndex, endIndex + TOKEN_END_LENGTH, replaceString);
			// startIndex = st.lastIndexOf(replaceString) +
			// replaceString.length();
			startIndex = startIndex + replaceString.length();
		} while (startIndex != -1);
		return st.toString();

	}

	public static boolean isValidId(String id) {
		if (!(ZERO.equals(id) || NEGATIVE_UNITY.equals(id))) {
			return isNumber(id);
		}

		return false;
	}

	/**
	 * Find how many times a character repeats in a String
	 * 
	 * @param str
	 *            - String to be searched against
	 * @param chr
	 *            - Character to be searched for
	 * @return No of times, the characters repeats. Zero for none
	 */
	public static int count(String str, char chr) {
		return count(str, chr, 0, str.length());
	}

	/**
	 * Find how many times a character repeats in a String
	 * 
	 * @param str
	 *            - String to be searched against
	 * @param chr
	 *            - Character to be searched for
	 * @param fromPos
	 *            - Start char index position in the String
	 * @param toPos
	 *            - End char index position in the String
	 * @return No of times, the characters repeats. Zero for none
	 */
	public static int count(String str, char chr, int fromPos, int toPos) {
		int counter = 0;

		for (int i = fromPos; i <= toPos; i++) {
			if (str.charAt(i) == chr) {
				counter++;
			}
		}

		return counter;
	}

	public static boolean isTrue(String value) {
		return isValidString(value) && TRUE.equalsIgnoreCase(value);
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static int getNumberOfDecimalPlace(Double value) {
		// For whole numbers like 0
		if (Math.round(value) == value)
			return 0;
		// NumberFormat f = new DecimalFormat("0.###################");
		DecimalFormat f = new DecimalFormat("#.000000000000000");
		String sp = f.format(value);
		BigDecimal bd1 = new BigDecimal(sp).stripTrailingZeros();
		String s = bd1.toString();
		return bd1.scale();
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public static String removeAnchor(String s) {
		int aStartTagStartPosition = s.indexOf("<a");

		int aEndTagStartPosition = s.indexOf("</a>");

		String removedAnchorString = null;

		String anchorTag = s.substring(aStartTagStartPosition,
				aEndTagStartPosition + 4);
		// System.out.println("anchorTag==:" + anchorTag);

		String hrefSTring = "href=";
		int hrefPostion = anchorTag.indexOf(hrefSTring);
		// System.out.println("hrefPostion>" + hrefPostion);
		int aStartTagEndPosition = anchorTag.indexOf(">");
		String href = anchorTag.substring(
				(hrefPostion + hrefSTring.length() + 1), aStartTagEndPosition);
		// System.out.println("href1==:" + href);
		int doubleQPosition = href.indexOf("\"");
		href = href.substring(0, doubleQPosition);
		// System.out.println("href==:" + href);
		removedAnchorString = StringUtils.replace(s, anchorTag, href);
		// s.replaceAll(anchorTag, href);
		return removedAnchorString;
	}

	/**
	 * Applies the specified mask to the card number.
	 *
	 * @param valueToMask The card number in plain format
	 * @param mask The number mask pattern. Use # to include a digit from the
	 * card number at that position, use x to skip the digit at that position
	 *
	 * @return The masked card number
	 */
	public static String maskCardNumber(String valueToMask, String mask) {

	    // format the number
	    int index = 0;
	    StringBuilder maskedNumber = new StringBuilder();
	    for (int i = 0; i < mask.length(); i++) {
	        char c = mask.charAt(i);
	        if (c == '#') {
	            maskedNumber.append(valueToMask.charAt(index));
	            index++;
	        } else if (c == 'x') {
	            maskedNumber.append(c);
	            index++;
	        } else {
	            maskedNumber.append(c);
	        }
	    }

	    // return the masked number
	    return maskedNumber.toString();
	}
	/**
	 * Applies the specified mask to the card number.
	 *
	 * @param cardNumber The card number in plain format
	 * @param mask The number mask pattern. Use # to include a digit from the
	 * card number at that position, use x to skip the digit at that position
	 *
	 * @return The masked card number
	 */
	public static String maskCardNumber(String valueToMask, int preMaxLength, int postMaxLength) {
		if(!ArgumentHelper.isValid(valueToMask)) {
			return valueToMask;
		}
		String maskString ="";
		for(int i=0 ; i<(valueToMask.length()-preMaxLength-postMaxLength);i++) {
			maskString = maskString +"x";
		}
//		System.out.println("(valueToMask.length()-hashSymbolLength)==>"+(valueToMask.length()-preMaxLength+postMaxLength));
		String preMaskString = "";
		for(int i=0 ; i<preMaxLength;i++) {
			preMaskString = preMaskString +"#";
		}
//		System.out.println("preMaskString==>"+preMaskString);
		String postMaskString = "";
		for(int i=0 ; i<postMaxLength;i++) {
			postMaskString = postMaskString +"#";
		}
//		System.out.println("postMaskString==>"+postMaskString);
		String calculateTasking  = preMaskString + maskString +postMaskString;
//		System.out.println("calculateTasking==>"+calculateTasking);
		return maskCardNumber(valueToMask, calculateTasking);
	}
	
	public static boolean isValid(String regExp,String stringValue) {
		return Pattern.compile(regExp).matcher(stringValue).matches();  
	}
	
	public static double roundDouble(double d, int places) {
		 
        BigDecimal bigDecimal = new BigDecimal(Double.toString(d));
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
 
	public static float roundFloat(float f, int places) {
 
        BigDecimal bigDecimal = new BigDecimal(Float.toString(f));
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.floatValue();
    }
	
	public static void main(String[] args) {
//		System.out.println(maskCardNumber("1234123412341234", "xxxx-xxxx-xxxx-####"));
		String valueToMask = "813206930444";
		int hashSymbolLength =5;
		String maskString ="";
		for(int i=0 ; i<(valueToMask.length()-hashSymbolLength);i++) {
			maskString = maskString +"x";
		}
		
		String calculateTasking  = "##" + maskString +"###";
		System.out.println("valueToMask     ==>"+valueToMask);
		System.out.println("calculateTasking==>"+calculateTasking);
		System.out.println(maskCardNumber(valueToMask, calculateTasking));
		System.out.println("=====================================");
		 valueToMask = "813206930444";
		System.out.println(maskCardNumber(valueToMask,2,3));
		System.out.println("=====================================");
		
		String regExp = "^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{6,15}$";
		String stringValue ="Nc-=123";
		System.out.println("stringValue     ==>"+stringValue);
		System.out.println(isValid(regExp,stringValue));
		
		stringValue ="Nc@123";
		System.out.println("stringValue     ==>"+stringValue);
		System.out.println(isValid(regExp,stringValue));
		
//		String htmlContent = "narenda <a href=\"www.narendra.com\" > Narenda</a> CONDITIONS WHICH CAN BE VIEWED AT: <br><br><a href=\"http://www.centurylink.com/Pages/AboutUs/CompanyInformation/DoingBusiness\" target=\"_blank\">http://www.centurylink.com/Pages/AboutUs/CompanyInformation/DoingBusiness</a><br><br>SHIPMENT OF GOODS OR PERFORMANCE";
//		htmlContent = htmlContent
//				+ "CONDITIONS WHICH CAN BE VIEWED AT: <br><br><a href=\"http://www.centurylink.com/Pages/AboutUs/CompanyInformation/DoingBusiness\" target=\"_blank\">http://www.centurylink.com/Pages/AboutUs/CompanyInformation/DoingBusiness</a><br><br>SHIPMENT OF GOODS OR PERFORMANCE";
//		htmlContent = htmlContent
//				+ "<BR> <a href=\"/elance/proserv/common/testapp/GetPages?bug_file_loc=aaa\" target=\"getPages\">Enter new bug in bugzilla</a><br>";
//		// htmlContent="CONDITIONS WHICH CAN BE VIEWED AT: <br><br>";
//		// while (htmlContent.contains("<a")) {
//		// // anchor= "Link is <a href=\"www.narendra.com\" > Narenda</a> ";
//		// htmlContent = removeAnchor(htmlContent);
//		// System.out.println("anchor==>" + htmlContent);
//		// }
//		int counter = 0;
//		try {
//			while (htmlContent.contains("<a") && counter++ < 100) {
//				htmlContent = StringUtil.removeAnchor(htmlContent);
//			}
//		} catch (Exception exception) {
//			exception.printStackTrace();
//			// ignore
//		}
//		System.out.println("htmlContent=====>" + htmlContent);
	}
}
