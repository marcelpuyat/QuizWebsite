package customObjects;

import java.util.Calendar;

import org.json.JSONObject;

public class CustomDate {
	
	private int year;
	private int month;
	private int date;
	private int hours;
	private int minutes;
	private int seconds;
	
	public CustomDate(Calendar calendar) {
		this.setYear(calendar.get(Calendar.YEAR));
		this.setMonth(calendar.get(Calendar.MONTH) + 1); // MONTHS ARE INDEXED FROM 0!
		this.setDate(calendar.get(Calendar.DATE));
		this.setHours(calendar.get(Calendar.HOUR_OF_DAY));
		this.setMinutes(calendar.get(Calendar.MINUTE));
		this.setSeconds(calendar.get(Calendar.SECOND));
	}

	/**
	 * @param year the year to set
	 */
	private void setYear(int year) {
		this.year = year;
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param month the month to set
	 */
	private void setMonth(int month) {
		this.month = month;
	}

	/**
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * @param date the date to set
	 */
	private void setDate(int date) {
		this.date = date;
	}

	/**
	 * @return the date
	 */
	public int getDate() {
		return date;
	}

	/**
	 * @param hours the hours to set
	 */
	private void setHours(int hours) {
		this.hours = hours;
	}

	/**
	 * @return the hours
	 */
	public int getHours() {
		return hours;
	}

	/**
	 * @param minutes the minutes to set
	 */
	private void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	/**
	 * @return the minutes
	 */
	public int getMinutes() {
		return minutes;
	}

	/**
	 * @param seconds the seconds to set
	 */
	private void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	/**
	 * @return the seconds
	 */
	public int getSeconds() {
		return seconds;
	}
	
	public JSONObject toJSON() {
		JSONObject dateInfo = new JSONObject();

		dateInfo.put("year", this.getYear());
		dateInfo.put("month", this.getMonth());
		dateInfo.put("date", this.getDate());
		dateInfo.put("hours", this.getHours());
		dateInfo.put("minutes", this.getMinutes());
		dateInfo.put("seconds", this.getSeconds());
		return dateInfo;
	}
}
