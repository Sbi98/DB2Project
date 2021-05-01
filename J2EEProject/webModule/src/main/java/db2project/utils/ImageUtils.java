package db2project.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {

	public static byte[] readImage(InputStream imageInputStream) throws IOException {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		/*
			TODO Rispettiamo i tipi del database:
		    TINYBLOB   : max 255 byte
		    BLOB       : max 65,535 byte
		    MEDIUMBLOB : max 16,777,215 byte      <--- Ciò che usiamo ora
		    LONGBLOB   : max 4,294,967,295 byte
		*/
		byte[] buffer = new byte[4096];// image can be maximum of 4MB TODO Non sono 4KB? Ho provato con immagine di 190KB e funziona
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
