package com.assignsecurities.app;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SystemFileManager {
	private static final Logger logger = LogManager.getLogger(SystemFileManager.class);
	private static String fileStorePath;
	private static Set<String> supportedExtention = new HashSet<>();
	static {
		try {
			
			String envFileStorePath = System.getenv("PDMS_FILE_STORE_PATH");
			String path = null;
			if (null == envFileStorePath) {
				path = new File(".").getCanonicalPath();
				Path fileStorePathTemp = Paths.get(path + "/../../"
						+ "FileStore");
				// if directory exists?
				if (!Files.exists(fileStorePathTemp)) {
					try {
						Files.createDirectories(fileStorePathTemp);
					} catch (IOException e) {
						// fail to create directory
						e.printStackTrace();
					}
				}
				fileStorePath = fileStorePathTemp.toString();
			} else {
				fileStorePath = envFileStorePath;
			}
			supportedExtention.add("png");
			supportedExtention.add("jpg");
			supportedExtention.add("jpeg");
			supportedExtention.add("pdf");
//			, .jpg, .jpeg
		} catch (IOException e) {
			logger.error(e);
		}
	}
	
	public static String getBaseFilePath() {
		return fileStorePath;
	}

	public static byte[] getFile(String orgGroupId, String fileName) {
		try {
			String filePathTemp = fileStorePath + File.separator + orgGroupId
					+ File.separator + fileName;
			Path filePath = Paths.get(filePathTemp);
//			System.out.println("getFile::filePath===>"+filePath);
			if (Files.exists(filePath)) {
				return Files.readAllBytes(filePath);
			}
			for(String ext : supportedExtention){
				 filePath = Paths.get(filePathTemp+"."+ext);
				if (Files.exists(filePath)) {
					return Files.readAllBytes(filePath);
				}
			}
			return null;
		} catch (IOException e) {
			logger.error(e);
			return null;

		}
	}

	public static boolean saveFile(String orgGroupId, String fileName,
			byte[] bFile) {
		try {
			String filePathTemp = fileStorePath + File.separator + orgGroupId;
			Path filePath = Paths.get(filePathTemp);
			if (!Files.exists(filePath)) {
				Files.createDirectory(filePath);
			}
			filePathTemp =filePathTemp + File.separator + fileName;
			filePath = Paths.get(filePathTemp);
//			System.out.println("saveFile::filePath===>"+filePath);
			if (Files.exists(filePath)) {
				Files.delete(filePath);
			}
			Files.write(filePath, bFile);
		} catch (IOException e) {
			logger.error(e);
			return false;
		}	
		return true;
	}

	public static String getMimeType(byte data[]) {		
		InputStream is = new BufferedInputStream(new ByteArrayInputStream(data));
		String mimeType=null;
		try {
			mimeType = URLConnection.guessContentTypeFromStream(is);
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
		}
		return mimeType;
	}
	
	public static boolean deleteFile(String orgGroupId, String fileName) {
		try {
			String filePathTemp = fileStorePath + File.separator + orgGroupId;
			Path filePath = Paths.get(filePathTemp);
			if (!Files.exists(filePath)) {
				Files.createDirectory(filePath);
			}
			filePathTemp =filePathTemp + File.separator + fileName;
			filePath = Paths.get(filePathTemp);
//			System.out.println("saveFile::filePath===>"+filePath);
			if (Files.exists(filePath)) {
				Files.delete(filePath);
			}
		} catch (IOException e) {
			logger.error(e);
			return false;
		}	
		return true;
	}
}
