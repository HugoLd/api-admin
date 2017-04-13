package org.cap.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Util {
	private final static String FORMAT_WITH_DAY = "EEEE, dd/MM/yyyy";
	private final static String FORMAT_WITHOUT_DAY= "dd-MM-yyyy";
	
	public static String generateUUID(String uuid , String mail , String date){
		return UUID.nameUUIDFromBytes((uuid + "+" + mail + "+" + date).getBytes()).toString();
	}

	/**
	 * get the date formatted with day
	 * @return the today's date
	 */
	public static String getDateNowWithDayOfWeek() {
		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_WITH_DAY);
		return formatter.format(date);
	}

	/**
	 * get the date formatted without day
	 * @return the today's date
	 */
	public static String getDateNow() {
		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_WITHOUT_DAY);
		return formatter.format(date);
	}
}
