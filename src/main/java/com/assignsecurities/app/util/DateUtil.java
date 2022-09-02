package com.assignsecurities.app.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * *****************************************************************************
 * *****************************************************************************
 * Please do not use this class.Instead Please use DateDiff in the same package
 * which is tested thoroughly.Thanks
 * *****************************************************************************
 * *****************************************************************************
 */
public class DateUtil {
	private static final Logger logger =LogManager.getLogger(DateUtil.class);
	public static Class THIS_CLASS = DateUtil.class;

	public static final String INTERNAL_DATE_DISPLAY_FORMAT = "MM/dd/yyyy";

	public static final String LONG_DATE_DISPLAY_FORMAT = "dd/MM/yyyy hh:mm:ss a";

	private static DateFormat INTERNAL_DATE_FORMATTER = new SimpleDateFormat(
			INTERNAL_DATE_DISPLAY_FORMAT);

	public static final long ONE_DAY_IN_MILLISECOND = 24 * 60 * 60 * 1000;

	public static Date getTodaysDate(boolean withTime) {
		if (withTime) {
			return new Date();
		} else {
			Calendar calendar = Calendar.getInstance();
			// clear Time component
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			return calendar.getTime();
		}

	}

	public static Date addDays(Date dtValue, int noOfDays) {
		Calendar cal = getCalendar(dtValue);
		cal.add(Calendar.DAY_OF_YEAR, noOfDays);
		return cal.getTime();
	}

	public static Calendar addDays(Calendar cal, int noOfDays) {
		cal.add(Calendar.DAY_OF_YEAR, noOfDays);
		return cal;
	}

	/**
	 * @param sdate
	 * @return
	 */
	public static Date getDate(String sdate) {
		try {
			return INTERNAL_DATE_FORMATTER.parse(sdate);
		} catch (ParseException pse) {
			logger.log(Level.ERROR, "Error parsing date:" + sdate, null, pse.getMessage(),
					pse);
			return null;
		}
	}

	/**
	 * @param sdate
	 * @return
	 */
	public static Calendar getCalendar(String sdate) {
		Date date = getDate(sdate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	public static Calendar getCalendar(Date dtValue) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dtValue);
		return cal;
	}

	/**
	 * @author priyeshkannan
	 * @param dtValue
	 * @param locale
	 * @return Calendar
	 */
	public static Calendar getCalendar(Date dtValue, Locale locale) {
		Calendar cal = Calendar.getInstance(locale);
		cal.setTime(dtValue);
		return cal;
	}

	/**
	 * @param startDateString
	 * @param endDateString
	 * @return
	 * @deprecated Please use isEndDateAfterStartDate(Date, Date)
	 */
	public static boolean isEndDateAfterStartDate(String startDateString,
			String endDateString) {
		return isEndDateAfterStartDate(getDate(startDateString),
				getDate(endDateString));
	}

	/**
	 * @param startDate
	 * @param endDateString
	 * @return
	 * @deprecated Please use isEndDateAfterStartDate(Date, Date)
	 */
	public static boolean isEndDateAfterStartDate(Date startDate,
			String endDateString) {
		return isEndDateAfterStartDate(startDate, getDate(endDateString));
	}

	/**
	 * @param startDateString
	 * @param endDate
	 * @return
	 * @deprecated Please use isEndDateAfterStartDate(Date, Date)
	 */
	public static boolean isEndDateAfterStartDate(String startDateString,
			Date endDate) {
		return isEndDateAfterStartDate(getDate(startDateString), endDate);
	}

	public static boolean isEndDateAfterStartDate(Date startDateValue,
			Date endDateValue) {
		logger.info( "Inside isEndDateBeforeStartDate");
		Calendar startDate = getCalendar(startDateValue);
		logger.info( "Start date " + startDate);
		logger.info( "Start date in string " + startDate.toString());

		Calendar endDate = getCalendar(endDateValue);
		endDate.set(Calendar.MILLISECOND, endDate.get(Calendar.MILLISECOND) + 1);
		logger.info( "End date " + endDate);
		logger.info( "Start date in string " + endDate.toString());
		logger.info(
				"End date after start date " + endDate.after(startDate));
		return endDate.after(startDate);
	}

	/**
	 * @param startDateString
	 * @param endDateString
	 * @return
	 * @deprecated Please use isEndDateBeforeStartDate(Date, Date)
	 */
	public static boolean isEndDateBeforeStartDate(String startDateString,
			String endDateString) {
		return isEndDateBeforeStartDate(getDate(startDateString),
				getDate(endDateString));
	}

	/**
	 * @param startDate
	 * @param endDateString
	 * @return
	 * @deprecated Please use isEndDateBeforeStartDate(Date, Date)
	 */
	public static boolean isEndDateBeforeStartDate(Date startDate,
			String endDateString) {
		return isEndDateBeforeStartDate(startDate, getDate(endDateString));
	}

	/**
	 * @param startDateString
	 * @param endDate
	 * @return
	 * @deprecated Please use isEndDateBeforeStartDate(Date, Date)
	 */
	public static boolean isEndDateBeforeStartDate(String startDateString,
			Date endDate) {
		return isEndDateBeforeStartDate(getDate(startDateString), endDate);
	}

	public static boolean isEndDateBeforeStartDate(Date startDateValue,
			Date endDateValue) {
		logger.info( "Inside isEndDateBeforeStartDate");
		Calendar startDate = getCalendar(startDateValue);
		logger.info( "Start date " + startDate);
		Calendar endDate = getCalendar(endDateValue);
		endDate.set(Calendar.MILLISECOND, endDate.get(Calendar.MILLISECOND) + 1);
		logger.info( "End date " + endDate);
		logger.info(
				"End date before start date " + endDate.before(startDate));
		return endDate.before(startDate);
	}

	// Ignore Time

	public static boolean isSameDayOfYear(Date date1, Date date2) {
		Calendar base = Calendar.getInstance();
		Calendar compare = Calendar.getInstance();
		base.setTime(date1);
		compare.setTime(date2);
		return DateUtil.isSameDayOfYear(base, compare);
	}

	/**
	 * please note that time component of the date is ignored!
	 *
	 * @param date1
	 *            Description of the Parameter
	 * @param date2
	 *            Description of the Parameter
	 * @return true if date1 and date2 are the same days of year
	 * @return false otherwise
	 */
	public static boolean isSameDayOfYear(Calendar date1, Calendar date2) {

		if (date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR)) {
			if (date1.get(Calendar.DAY_OF_YEAR) == date2
					.get(Calendar.DAY_OF_YEAR)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isAfterIgnoreTime(Date baseDate, Date compareDate) {
		Calendar base = Calendar.getInstance();
		Calendar compare = Calendar.getInstance();
		base.setTime(baseDate);
		compare.setTime(compareDate);
		return DateUtil.isAfterIgnoreTime(base, compare);
	}

	/**
	 * Checks if compare is after base. Ignores time component of dates
	 *
	 * @param base
	 *            Description of the Parameter
	 * @param compare
	 *            Description of the Parameter
	 * @return true if compare is after base
	 * @return false otherwise
	 */
	public static boolean isAfterIgnoreTime(Calendar base, Calendar compare) {
		boolean flag = false;

		// ignore time component
		compare.set(Calendar.AM_PM, base.get(Calendar.AM_PM));
		compare.set(Calendar.HOUR_OF_DAY, base.get(Calendar.HOUR_OF_DAY));
		compare.set(Calendar.MINUTE, base.get(Calendar.MINUTE));
		compare.set(Calendar.SECOND, base.get(Calendar.SECOND));
		compare.set(Calendar.MILLISECOND, base.get(Calendar.MILLISECOND));

		if (compare.after(base)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * Checks if compareDate is before baseDate. Ignores time component of dates
	 *
	 * @param baseDate
	 *            Description of the Parameter
	 * @param compareDate
	 *            Description of the Parameter
	 * @return true if compareDate is before baseDate
	 * @return false otherwise
	 */
	public static boolean isBeforeIgnoreTime(Date baseDate, Date compareDate) {
		Calendar base = Calendar.getInstance();
		Calendar compare = Calendar.getInstance();
		base.setTime(baseDate);
		compare.setTime(compareDate);
		return DateUtil.isBeforeIgnoreTime(base, compare);
	}

	public static boolean isBeforeIgnoreTime(Calendar base, Calendar compare) {
		boolean flag = false;
		// ignore time component
		compare.set(Calendar.AM_PM, base.get(Calendar.AM_PM));
		compare.set(Calendar.HOUR_OF_DAY, base.get(Calendar.HOUR_OF_DAY));
		compare.set(Calendar.MINUTE, base.get(Calendar.MINUTE));
		compare.set(Calendar.SECOND, base.get(Calendar.SECOND));
		compare.set(Calendar.MILLISECOND, base.get(Calendar.MILLISECOND));

		if (compare.before(base)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * Checks if compareDate is between date1 and date2, date1 need not be after
	 * date2
	 *
	 * @param date1
	 *            Description of the Parameter
	 * @param date2
	 *            Description of the Parameter
	 * @param compareDate
	 *            Description of the Parameter
	 * @return true if compareDate between date1 & date2, false otherwise
	 */
	public static boolean isBetweenIgnoreTime(Date date1, Date date2,
			Date compareDate) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		Calendar compare = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		compare.setTime(compareDate);
		return DateUtil.isBetweenIgnoreTime(cal1, cal2, compare);
	}

	/**
	 * Checks if compareDate is between date1 and date2, date1 need not be after
	 * date2
	 *
	 * @param date1
	 *            Description of the Parameter
	 * @param date2
	 *            Description of the Parameter
	 * @param compareDate
	 *            Description of the Parameter
	 * @return true if compareDate between date1 & date2, false otherwise
	 */
	public static boolean isBetweenIgnoreTime(Calendar date1, Calendar date2,
			Calendar compareDate) {
		boolean flag = false;
		if (DateUtil.isSameDayOfYear(date1, compareDate)
				|| DateUtil.isSameDayOfYear(date2, compareDate)) {
			flag = true;
		} else {
			// switch date1 and date2 if date1 after date2
			if (DateUtil.isAfterIgnoreTime(date2, date1)) {
				Calendar temp = date1;
				date1 = date2;
				date2 = temp;
			}
			if (DateUtil.isAfterIgnoreTime(date1, compareDate)
					&& DateUtil.isBeforeIgnoreTime(date2, compareDate)) {
				flag = true;
			}
		}
		return flag;
	}

	public static Date getParsedDateIgnoreNull(String sdate)
			throws ParseException {
		if (sdate != null && sdate.length() > 0) {
			return INTERNAL_DATE_FORMATTER.parse(sdate);
		} else {
			return null;
		}
	}

	public static Date getParsedDate(String sdate) throws ParseException {
		return INTERNAL_DATE_FORMATTER.parse(sdate);
	}

	public static String format(Date date) {
		return INTERNAL_DATE_FORMATTER.format(date);
	}

	public static String format(Date date, String format) {
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	public static int getDateDifferenceIgnoreTime(Date startDate, Date endDate) {

		// Calendar start = getCalendar(startDate);
		// Calendar end = getCalendar(endDate);
		//
		// end.set(Calendar.AM_PM, start.get(Calendar.AM_PM));
		// end.set(Calendar.HOUR_OF_DAY, start.get(Calendar.HOUR_OF_DAY));
		// end.set(Calendar.MINUTE, start.get(Calendar.MINUTE));
		// end.set(Calendar.SECOND, start.get(Calendar.SECOND));
		// end.set(Calendar.MILLISECOND, start.get(Calendar.MILLISECOND));
		//
		// long difference = end.getTime().getTime() -
		// start.getTime().getTime();
		//
		// return (int) (difference / ONE_DAY_IN_MILLISECOND);

		Calendar start = getCalendar(startDate);
		Calendar end = getCalendar(endDate);

		// Bug Id : 21649 - Mrudul
		end.set(Calendar.HOUR, 0);
		end.set(Calendar.MINUTE, 0);
		end.set(Calendar.SECOND, 0);
		end.set(Calendar.MILLISECOND, 0);

		start.set(Calendar.HOUR, 0);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
		start.set(Calendar.MILLISECOND, 0);

		// Bug Id : 21649 - Mrudul
		// Converting it into UTC - This then takes care of the Day Light
		// savings time.
		long utcStartTime = start.getTime().getTime()
				- (start.getTime().getTimezoneOffset() * 60000);
		long utcEndTime = end.getTime().getTime()
				- (end.getTime().getTimezoneOffset() * 60000);

		// long difference = (end.getTime().getTime() ) - (
		// start.getTime().getTime() ) ;
		long difference = utcEndTime - utcStartTime;

		return (int) (difference / ONE_DAY_IN_MILLISECOND);

	}

	/**
	 * Removed the time component in a given Date object. for example 12 May
	 * 2004 12:56:23.461 will be converted to 12 May 2004 00:00:00.000 This
	 * method is useful for comparing dates ignoring time.
	 *
	 * @todo Refactor ignoreTime methods to use this method. -Jp 12 May 2004
	 *
	 * @param date
	 *            Any date object in which the time component has to be removed
	 * @return date with time set to 00:00:00.000. Null, if the argument is null
	 */
	public static Date stripTime(Date date) {
		if (date != null) {
			Calendar cal = getCalendar(date);
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			return cal.getTime();
		} else {
			return null;
		}
	}

	public static String getDateAfterDays(Date d, int days) {
		String DATE_FORMAT = "dd MMM yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(d);
		c1.add(Calendar.DATE, days);
		// System.out.println("Date "+ days+" days is : " +
		// sdf.format(c1.getTime()));
		return sdf.format(c1.getTime());
	}

	public static Date getDateAfterDays(Date d, int days, boolean isResultDate) {
		DateFormat formatter;
		Date date = null;
		try {
			formatter = new SimpleDateFormat("dd MMM yyyy");
			date = (Date) formatter.parse(getDateAfterDays(d, days));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;

	}

	/**
	 * This method is added in issue ELNNEW-49385 - Setting Default time for
	 * task/scheduler feature. In this feature we save time in format of minute
	 * gaps.For eg .0 for setting 12:00 AM,MAX is 1439 for setting 11:59 PM.
	 * This method takes long value and return array which contains
	 * meridiem,hour and minute w.r.t. incoming value
	 * 
	 * @param defaultTime
	 * @return
	 * @throws Exception
	 */
	public static int[] getDecodedDefaultTimeForScheduler(long defaultTime)
			throws Exception {
		int[] defaultTimeDetails = new int[3];
		defaultTimeDetails[0] = 0; // set for AM
		if (defaultTime >= 720) {
			defaultTimeDetails[0] = 1; // set for PM
		}
		defaultTimeDetails[1] = (int) defaultTime / 60; // set for hour
		defaultTimeDetails[2] = (int) defaultTime % 60; // set for minute
		return defaultTimeDetails;
	}

	/**
	 * This method is added in issue ELNNEW-49385 - Setting Default time for
	 * task/scheduler feature. In this feature we save time in format of minute
	 * gaps.For eg .0 for setting 12:00 AM,MAX is 1439 for setting 11:59 PM.
	 * This method takes array which contains meridiem,hour and minute and
	 * return long value as per stated above
	 * 
	 * @param defaultTime
	 * @return
	 * @throws Exception
	 */
	public static long getEncodedDefaultTimeForScheduler(
			String[] defaultTimeDetails) throws Exception {
		try {
			long defaultTime = 0;
			if (defaultTimeDetails[0].equals("43200"))
				defaultTime = 720;

			int hour = new Integer(defaultTimeDetails[1]).intValue();
			if (hour != 12)
				defaultTime += (hour * 60);

			int minutes = new Integer(defaultTimeDetails[2]).intValue();
			defaultTime += minutes;
			return defaultTime;
		} catch (Exception exception) {
			throw new Exception(
					"Error in encoding of default time for scheduler",
					exception);
		}
	}

	/**
	 * This method is added in issue ELNNEW-51132 - Setting Default time for
	 * task/scheduler feature. In this feature we save time in format of minute
	 * gaps.For eg 0 for setting 12:00 AM,MAX is 1439 for setting 11:59 PM. This
	 * method takes number and convert it to time in string format for eg 0 ->
	 * 12:00 AM, 65-> 01:05 AM, 720-> 12:00 PM, 1439-> 11:59 PM
	 * 
	 * @param defaultTime
	 * @return
	 * @throws Exception
	 */
	public static String getDecodedDefaultTimeInStringFormatForScheduler(
			long defaultTimeInMinutes) throws Exception {
		int[] defaultTimeDetails = new int[3];
		StringBuffer defaultTimeString = new StringBuffer("");
		defaultTimeDetails[0] = 0; // set for AM
		if (defaultTimeInMinutes >= 720) {
			defaultTimeDetails[0] = 1; // set for PM
		}
		defaultTimeDetails[1] = (int) defaultTimeInMinutes / 60; // set for hour
		defaultTimeDetails[2] = (int) defaultTimeInMinutes % 60; // set for
																	// minute
		int hourValue = defaultTimeDetails[1];
		if (hourValue > 12) {
			hourValue = hourValue - 12; // This is to set hour value for eg 13
										// to 1;14 to 2 etc

		} else if (hourValue == 0) {
			hourValue = 12; // This is to set hour value to 12 if its 0
		}
		if (hourValue < 10) {
			defaultTimeString.append("0"); // This is to append 0 if hour less
											// than 10 for eg 9 -> 09,8->08
		}
		defaultTimeString.append(hourValue);
		defaultTimeString.append(":");
		if (defaultTimeDetails[2] < 10) {
			defaultTimeString.append("0");// This is to append 0 if minutes less
											// than 10 for eg 9 -> 09,8->08
		}
		defaultTimeString.append(defaultTimeDetails[2]);
		defaultTimeString.append(" ");
		if (defaultTimeDetails[0] == 0) {
			defaultTimeString.append("AM");
		} else {
			defaultTimeString.append("PM");
		}
		return defaultTimeString.toString();
	}

	private static final String[] formats = { "E MMM dd HH:mm:ss Z yyyy",
			"EEE MMM dd HH:mm:ss Z yyyy", "yyyy.MM.dd G 'at' HH:mm:ss z",
			"EEE, d MMM yyyy HH:mm:ss Z", "yyyy-MM-dd'T'HH:mm:ss'Z'",
			"yyyy-MM-dd'T'HH:mm:ssZ", "yyyy-MM-dd'T'HH:mm:ss",
			"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
			"yyyy-MM-dd HH:mm:ss", "MM/dd/yyyy HH:mm:ss",
			"MM/dd/yyyy'T'HH:mm:ss.SSS'Z'", "MM/dd/yyyy'T'HH:mm:ss.SSSZ",
			"MM/dd/yyyy'T'HH:mm:ss.SSS", "MM/dd/yyyy'T'HH:mm:ssZ",
			"MM/dd/yyyy'T'HH:mm:ss", "yyyy:MM:dd HH:mm:ss", "yyyyMMdd", };

	/**
	 * 
	 * @param d
	 * @return
	 */
	public static String getDateFormat(String d) {
		if (d != null) {
			for (String parse : formats) {
				SimpleDateFormat sdf = new SimpleDateFormat(parse);
				try {
					sdf.parse(d);
					return parse;
				} catch (ParseException e) {
					continue;
				}
			}
		}
		return null;
	}

	/**
	 * @param sdate
	 * @return
	 */
	public static Date getDate(String sdate, String format) {
		try {
			DateFormat dateFormat = new SimpleDateFormat(format);
			return dateFormat.parse(sdate);
		} catch (ParseException pse) {
			logger.log(Level.ERROR,"Error parsing date:" + sdate, null, pse.getMessage(),
					pse);
			return null;
		}
	}
	/**
	 * 
	 * @param sdate
	 * @param format
	 * @return
	 */
	public static String getFormatedDate(Date sdate, String format) {
			DateFormat dateFormat = new SimpleDateFormat(format);
			return dateFormat.format(sdate);
	}
	/**
	 * @param sdate
	 * @return
	 */
	public static Date getDateWithOutTime(Date sdate, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date dateWithoutTime = sdf.parse(sdf.format(sdate));
			return dateWithoutTime;
		} catch (ParseException pse) {
			logger.log(Level.ERROR,"Error parsing date:" + sdate, null, pse.getMessage(),
					pse);
			return null;
		}
	}
	
	public static LocalDateTime getLocalDateTime(String sdate, String format) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(format);  
	        Date date = formatter.parse(sdate);
	        return date.toInstant()
				      .atZone(ZoneId.systemDefault())
				      .toLocalDateTime();
	    } catch (ParseException e) {
	    	logger.log(Level.ERROR,"Error parsing date:" + sdate, null, e.getMessage(),
					e);
	    	} 
		return null;
	}
	
	public static LocalDate getWeekStartDate(LocalDate localDate) {
		LocalDate now = LocalDate.now(); // Gets the current date
		DayOfWeek day = now.getDayOfWeek(); // Gets the day-of-week field, which is an enum DayOfWeek.
		int value = day.getValue(); // Gets the day-of-week int value. as it starts from 0 which is monday
		LocalDate weekStart = now.minusDays(value - 1); // subtracting weekday value from the current date to find the
														// weekstart date
		return weekStart;

	}

	public static LocalDate getWeekEndDate(LocalDate localDate) {
		LocalDate weekStart = getWeekStartDate(localDate);
		LocalDate weekEnd = weekStart.plusDays(6); // adding value 6 to the weekstart gives us week
		return weekEnd;
	}

	public static LocalDate getMonthStartDate(LocalDate localDate) {
		return LocalDate.now().withDayOfMonth(1); // Returns a copy of this LocalDate with the day-of-month altered.

	}

	public static LocalDate getMonthEndDate(LocalDate localDate) {
		return localDate.withDayOfMonth(localDate.lengthOfMonth()); // Returns a copy of this LocalDate with the
																			// day-of-month altered.
	}
	
	public static LocalDate getYearStartDate(LocalDate localDate) {
		return LocalDate.now().withDayOfYear(1); // Returns a copy of this LocalDate with the day-of-month altered.

	}

	public static LocalDate getYearEndDate(LocalDate localDate) {
		return localDate.withDayOfYear(localDate.lengthOfYear()); // Returns a copy of this LocalDate with the
																			// day-of-month altered.
	}
	public static int calculateAge(LocalDate birthDate, LocalDate currentDate) {
        if ((birthDate != null) && (currentDate != null)) {
            return Period.between(birthDate, currentDate).getYears();
        } else {
            return 0;
        }
    }
}
