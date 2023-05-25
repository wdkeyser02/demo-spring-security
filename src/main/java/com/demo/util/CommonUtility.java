package com.demo.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;

/**
 *
 * @author Demo User
 * @date   14-Apr-2023
 *
 */
@Component
@AllArgsConstructor
public class CommonUtility {

	public static final Predicate<String> NOT_NULL_NOT_EMPTY_STRING = s -> s != null && !s.isEmpty();

	public static final Predicate<String> NOT_NULL_NOT_EMPTY_NOT_BLANK_STRING = s -> s != null && !s.isEmpty() && !s.isBlank();

	public static final Predicate<List<?>> NOT_NULL_NOT_EMPTY_LIST = s -> s != null && !s.isEmpty();

	public static final Predicate<Map<?, ?>> NOT_NULL_NOT_EMPTY_MAP = s -> s != null && !s.isEmpty();

	/**
	 * This map will be used to get the four day (current + next 3 days) of the week.
	 */
	private static final Map<Integer, List<Integer>> nextFourDayMap = new HashMap<>();

	/**
	 * Encoding String in BCryptPasswordEncoder Used for Oauth
	 *
	 * @param  string
	 * @return
	 */
	public static String generateBcrypt(final String string) {
		return new BCryptPasswordEncoder().encode(string);
	}

	/**
	 * Generate random number for OTP
	 *
	 * @return
	 */
	public static int getRandomNumber() {
		final SecureRandom number = new SecureRandom();
		return 100000 + number.nextInt(899999);
	}

	public static String getRandomAlphaNumericNumber() {
		final SecureRandom number = new SecureRandom();
		String alphaNumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyz";
		StringBuilder sb = new StringBuilder(16);
		for (int i = 0; i < 16; i++) {
			sb.append(alphaNumeric.charAt(number.nextInt(alphaNumeric.length())));
		}
		return sb.toString();
	}

	public static String getRandom8DigitNumericNumber() {
		final SecureRandom number = new SecureRandom();
		String numeric = "1234567890";
		StringBuilder sb = new StringBuilder(8);
		for (int i = 0; i < 8; i++) {
			sb.append(numeric.charAt(number.nextInt(numeric.length())));
		}
		return sb.toString();
	}

	/**
	 * Return Map with new DistinctFileName,fileNameWithOutExtension,extension
	 *
	 * @param  file
	 * @return
	 */
	public static Map<String, String> getDistinctAssetProperties(final MultipartFile file) {
		Calendar calendar = Calendar.getInstance();
		long timeInMillis = calendar.getTimeInMillis();
		String assetFile = StringUtils.cleanPath(file.getOriginalFilename());
		String extension = "";
		StringBuilder fileNameWithOutExtension;

		int lastIndex = assetFile.lastIndexOf('.');

		extension = assetFile.substring(lastIndex + 1);
		fileNameWithOutExtension = new StringBuilder(assetFile.substring(0, lastIndex));

		Map<String, String> fileProperties = new HashMap<>();
		StringBuilder newFileNameWithOutExtension = fileNameWithOutExtension.append("_").append(String.valueOf(timeInMillis));
		fileProperties.put("newfileNameWithOutExtension", newFileNameWithOutExtension.toString());
		fileProperties.put("extension", extension);
		StringBuilder newFileName = newFileNameWithOutExtension.append(".").append(extension);
		fileProperties.put("newFileName", newFileName.toString());

		return fileProperties;
	}

	public static Map<String, String> getDistinctFileProperties(final File file) {
		Calendar calendar = Calendar.getInstance();
		long timeInMillis = calendar.getTimeInMillis();
		String assetFile = StringUtils.cleanPath(file.getName());
		String extension = "";
		StringBuilder fileNameWithOutExtension;

		int lastIndex = assetFile.lastIndexOf('.');

		extension = assetFile.substring(lastIndex + 1);
		fileNameWithOutExtension = new StringBuilder(assetFile.substring(0, lastIndex));

		Map<String, String> fileProperties = new HashMap<>();
		StringBuilder newFileNameWithOutExtension = fileNameWithOutExtension.append("_").append(String.valueOf(timeInMillis));
		fileProperties.put("newfileNameWithOutExtension", newFileNameWithOutExtension.toString());
		fileProperties.put("extension", extension);
		StringBuilder newFileName = newFileNameWithOutExtension.append(".").append(extension);
		fileProperties.put("newFileName", newFileName.toString());

		return fileProperties;
	}

	public static String getDistinctFileName(final String fileName) {
		Calendar calendar = Calendar.getInstance();
		long timeInMillis = calendar.getTimeInMillis();
		String extension = "";
		StringBuilder fileNameWithOutExtension;

		int lastIndex = fileName.lastIndexOf('.');

		extension = fileName.substring(lastIndex + 1);
		fileNameWithOutExtension = new StringBuilder(fileName.substring(0, lastIndex));

		StringBuilder newFileName = fileNameWithOutExtension.append("_").append(String.valueOf(timeInMillis));

		return newFileName.append(".").append(extension).toString();
	}

	public static String decode(final String url) {
		try {
			String prevURL = "";
			String decodeURL = url;
			while (!prevURL.equals(decodeURL)) {
				prevURL = decodeURL;
				decodeURL = URLDecoder.decode(decodeURL, "UTF-8");
			}
			return decodeURL;
		} catch (final UnsupportedEncodingException e) {
			return "Issue while decoding" + e.getMessage();
		}
	}

	public static String encode(final String url) {
		try {
			return URLEncoder.encode(url, "UTF-8");
		} catch (final UnsupportedEncodingException e) {
			return "Issue while encoding" + e.getMessage();
		}
	}

	public static LocalDate convetUtilDatetoLocalDate(final Date date) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault()).toLocalDate();
	}

	public static LocalDateTime convetUtilDatetoLocalDateTime(final Date date) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
	}

	public static Date getDateWithoutTime(final Date date) {
		return Date.from(convetUtilDatetoLocalDate(date).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	public static Date getTomorrowDateWithoutTime(final Date date) {
		return Date.from(convetUtilDatetoLocalDate(date).plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	public static Date convertLocalDateToUtilDate(final LocalDate localDate) {
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	public static Date convertLocalDateTimeToUtilDate(final LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static Double distance(final Double lat1, final Double lon1, final Double lat2, final Double lon2) {
		if (lat1.equals(lat2) && lon1.equals(lon2)) {
			return 0d;
		} else {
			Double theta = lon1 - lon2;
			Double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2))
					+ Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
			dist = Math.acos(dist);
			dist = Math.toDegrees(dist);
			dist *= 60 * 1.1515 * 1.609344;
			return round(dist);
		}
	}

	/**
	 * This method returns the 2 decimal value of any given double value, if the double value is null, then it return 0...
	 *
	 * @param  doubleValue
	 * @return
	 */
	public static Double round(final Double doubleValue) {

		if (doubleValue != null) {
			Long orderAmtLong = Math.round(doubleValue * 100);
			return orderAmtLong.doubleValue() / 100;
		} else {
			return 0.0d;
		}

	}

	public static Double roundToHalfDecimal(final Double doubleValue) {
		Double decimalPoint = Math.abs(doubleValue - Math.floor(doubleValue));
		if (decimalPoint == 0.00) {
			return doubleValue;
		} else if (decimalPoint <= 0.25) {
			return Double.sum(Math.floor(doubleValue), 0.25);
		} else if (decimalPoint <= 0.50) {
			return Double.sum(Math.floor(doubleValue), 0.50);
		} else if (decimalPoint <= 0.75) {
			return Double.sum(Math.floor(doubleValue), 0.75);
		} else {
			return (double) Math.round(doubleValue);
		}
	}

	/**
	 * @param createdAt
	 */
	public static Long getDifferenceInSecondsBetweenDateAndCurrentInstant(final Date createdAt) {
		Long currentMilliSeconds = Instant.now().toEpochMilli();
		Long pastDateMilliSecond = createdAt.getTime();

		return Long.sum(currentMilliSeconds, pastDateMilliSecond * -1) / 1000;
	}

	public static int getDayOfTheWeekFromDate(final Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * This mehtod return the 1 year previous date of the current date
	 *
	 * @param  dateFilterDTO
	 * @return
	 */
	public static Date get1YearPastDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);
		return cal.getTime();
	}

	/**
	 * Return date after 1 year of current date
	 *
	 * @return
	 */
	public static Date get1YearAfterDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, 1);
		return cal.getTime();
	}

	/**
	 * Returns date after 2 months of current date
	 *
	 * @return
	 */
	public static Date get2MonthsAfterDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 2);
		return cal.getTime();
	}

	public static List<Integer> getNextFourDayList(final Integer dayOfWeek) {
		if (nextFourDayMap.isEmpty()) {
			nextFourDayMap.put(1, Arrays.asList(1, 2, 3, 4));
			nextFourDayMap.put(2, Arrays.asList(2, 3, 4, 5));
			nextFourDayMap.put(3, Arrays.asList(3, 4, 5, 6));
			nextFourDayMap.put(4, Arrays.asList(4, 5, 6, 7));
			nextFourDayMap.put(5, Arrays.asList(5, 6, 7, 1));
			nextFourDayMap.put(6, Arrays.asList(6, 7, 1, 2));
			nextFourDayMap.put(7, Arrays.asList(7, 1, 2, 3));
			return nextFourDayMap.get(dayOfWeek);
		} else {
			return nextFourDayMap.get(dayOfWeek);
		}
	}

	public static boolean nameValidator(final String name) {
		final String NAME_REGEX = "^[\\w \\d .'(),]*+$";
		Pattern pattern;
		Matcher matcher;

		pattern = Pattern.compile(NAME_REGEX, Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(name);
		return matcher.matches();
	}

	public static List<Date> dateBetween(final Date startDate, final Date endDate) {
		List<Date> datesInRange = new ArrayList<>();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(startDate);
		calendar.add(Calendar.DATE, 1);
		while (calendar.getTime().before(endDate)) {
			Date result = calendar.getTime();
			datesInRange.add(result);
			calendar.add(Calendar.DATE, 1);
		}
		return datesInRange;
	}

	public static Double discountConverter(final Double percentage, final Double originalAmount, final Double discountAmount) {
		if (percentage != null) {
			return originalAmount - originalAmount * percentage / 100;
		} else {
			return 100 - 100 * discountAmount / originalAmount;
		}
	}

	/**
	 * @param  date
	 * @return
	 */
	public static Date getDateAfter1YearFromGivenDate(final Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, 1);
		return cal.getTime();
	}
}
