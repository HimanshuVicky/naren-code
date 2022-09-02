package com.assignsecurities.app.util;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.assignsecurities.app.exception.ServiceException;


/**
 * This class creates DateFormatSymbols for a given locale
 * 
 */
public class DateFormatterUtil {
	private static final Class THIS = DateFormatterUtil.class;
	private static final String UNKNOWN_WEEK = "unknownWeek";

	private static final Logger logger = LogManager.getLogger(DateFormatterUtil.class);

	public static SimpleDateFormat getDateFormat(String pattern, Locale locale,
			ResourceBundleUtil resourceBundleUtil) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern, locale);
		formatter.setLenient(false);
		formatter.setDateFormatSymbols(createFormatSymbol(locale,
				resourceBundleUtil));

		return formatter;
	}

	public static SimpleDateFormat getDateFormatForLocal(String pattern,
			Locale locale, ResourceBundleUtil resourceBundleUtil) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern, locale);
		formatter.setLenient(false);
		formatter.setDateFormatSymbols(createFormatSymbol(locale,
				resourceBundleUtil));

		return formatter;
	}

	public static DateFormatSymbols createFormatSymbol(Locale locale,
			ResourceBundleUtil resourceBundleUtil) throws ServiceException {
		return create(locale, resourceBundleUtil);
	}

	private static DateFormatSymbols create(Locale locale,
			ResourceBundleUtil resourceBundleUtil) throws ServiceException {
		DateFormatSymbols dateFormat = new DateFormatSymbols(locale);

		dateFormat.setAmPmStrings(getAmPmStrings(locale, resourceBundleUtil));
		dateFormat.setWeekdays(getWeekdayStrings(locale, resourceBundleUtil));
		dateFormat.setShortWeekdays(getShortWeekdayStrings(locale,
				resourceBundleUtil));
		dateFormat.setMonths(getMonthStrings(locale, resourceBundleUtil));
		dateFormat.setShortMonths(getShortMonthStrings(locale,
				resourceBundleUtil));

		// TODO: Set timezone string -Jp
		// Since we dont really support timezone formats, we are OK for now. But
		// for next release
		// we should implement it
		return dateFormat;
	}

	private static String[] getAmPmStrings(Locale locale,
			ResourceBundleUtil resourceBundleUtil) throws ServiceException {
		return new String[] {
				resourceBundleUtil.getLabel("lable.calander.am", locale),
				resourceBundleUtil.getLabel("lable.calander.pm", locale) };
	}

	private static String[] getWeekdayStrings(Locale locale,
			ResourceBundleUtil resourceBundleUtil) throws ServiceException {
		// Hack: Java calendar treat [1-7] as Sun-Sat and NOT [0-6] This is not
		// very clear in their documentation - Jp
		return new String[] {
				UNKNOWN_WEEK,
				resourceBundleUtil.getLabel("lable.calander.sunday", locale),
				resourceBundleUtil.getLabel("lable.calander.monday", locale),
				resourceBundleUtil.getLabel("lable.calander.tuesday", locale),
				resourceBundleUtil.getLabel("lable.calander.wednesday", locale),
				resourceBundleUtil.getLabel("lable.calander.thursday", locale),
				resourceBundleUtil.getLabel("lable.calander.friday", locale),
				resourceBundleUtil.getLabel("lable.calander.saturday", locale) };
	}

	private static String[] getShortWeekdayStrings(Locale locale,
			ResourceBundleUtil resourceBundleUtil) throws ServiceException {
		return new String[] {
				UNKNOWN_WEEK,
				resourceBundleUtil.getLabel("lable.calander.short.sunday",
						locale),
				resourceBundleUtil.getLabel("lable.calander.short.monday",
						locale),
				resourceBundleUtil.getLabel("lable.calander.short.tuesday",
						locale),
				resourceBundleUtil.getLabel("lable.calander.short.wednesday",
						locale),
				resourceBundleUtil.getLabel("lable.calander.short.thursday",
						locale),
				resourceBundleUtil.getLabel("lable.calander.short.friday",
						locale),
				resourceBundleUtil.getLabel("lable.calander.short.saturday",
						locale) };
	}

	private static String[] getMonthStrings(Locale locale,
			ResourceBundleUtil resourceBundleUtil) throws ServiceException {
		return new String[] {
				resourceBundleUtil.getLabel("lable.calander.january", locale),
				resourceBundleUtil.getLabel("lable.calander.february", locale),
				resourceBundleUtil.getLabel("lable.calander.march", locale),
				resourceBundleUtil.getLabel("lable.calander.april", locale),
				resourceBundleUtil.getLabel("lable.calander.may", locale),
				resourceBundleUtil.getLabel("lable.calander.june", locale),
				resourceBundleUtil.getLabel("lable.calander.july", locale),
				resourceBundleUtil.getLabel("lable.calander.august", locale),
				resourceBundleUtil.getLabel("lable.calander.september", locale),
				resourceBundleUtil.getLabel("lable.calander.october", locale),
				resourceBundleUtil.getLabel("lable.calander.november", locale),
				resourceBundleUtil.getLabel("lable.calander.december", locale) };
	}

	private static String[] getShortMonthStrings(Locale locale,
			ResourceBundleUtil resourceBundleUtil) throws ServiceException {
		return new String[] {
				resourceBundleUtil.getLabel("lable.calander.short.january", locale),
				resourceBundleUtil.getLabel("lable.calander.short.february", locale),
				resourceBundleUtil.getLabel("lable.calander.short.march", locale),
				resourceBundleUtil.getLabel("lable.calander.short.april", locale),
				resourceBundleUtil.getLabel("lable.calander.short.may", locale),
				resourceBundleUtil.getLabel("lable.calander.short.june", locale),
				resourceBundleUtil.getLabel("lable.calander.short.july", locale),
				resourceBundleUtil.getLabel("lable.calander.short.august", locale),
				resourceBundleUtil.getLabel("lable.calander.short.september", locale),
				resourceBundleUtil.getLabel("lable.calander.short.october", locale),
				resourceBundleUtil.getLabel("lable.calander.short.november", locale),
				resourceBundleUtil.getLabel("lable.calander.short.december", locale) };
	}
}
