package db2project.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;

public class Utils {

	public static Date getYesterday() {
		long now = Instant.now().getEpochSecond();
		return Date.from(Instant.ofEpochSecond(now - (now % (24 * 60 * 60)) - 24 * 60 * 60));
	}

	public static boolean isBeforeToday(Date date) {
		long now = Instant.now().getEpochSecond();
		return date.getTime() < (now - now % (24 * 60 * 60))*1000;
	}

	public static Date utcDateFromString(String s) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		try {
			return sdf.parse(s);
		} catch (ParseException e) {
			return null;
		}
	}

	public static byte[] readImage(InputStream imageInputStream) throws IOException {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		/*
			TODO Rispettiamo i tipi del database:
		    TINYBLOB   : max 255 byte
		    BLOB       : max 65,535 byte
		    MEDIUMBLOB : max 16,777,215 byte      <--- Ciò che usiamo ora
		    LONGBLOB   : max 4,294,967,295 byte
		*/
		byte[] buffer = new byte[4096];// image can be maximum of 4MB
		int bytesRead = -1;

		try {
			while ((bytesRead = imageInputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
			byte[] imageBytes = outputStream.toByteArray();
			return imageBytes;
		} catch (IOException e) {
			throw e;
		}

	}

}
