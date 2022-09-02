package com.assignsecurities.scheduler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Enumeration;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.assignsecurities.app.util.Util;
import com.assignsecurities.dm.PropertyKeys;
import com.assignsecurities.service.impl.ApplicationPropertiesService;
import com.assignsecurities.service.impl.ScriptService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("BhavCopyExtractor")
@Component
@EnableAsync
@EnableScheduling
public class BhavCopyExtractor {

	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";
	public static final String REFERER  =  "https://www1.nseindia.com/products/content/equities/equities/archieve_eq.htm";

	@Autowired
	private ScriptService scriptService;

	@Scheduled(cron = "${bhav.copy.extractor}")
	public void execute() {
		try {
			String bSEurl = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.BHAV_BSE_URL);
			String nSEurl = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.BHAV_NSE_URL);
			String destination = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.BHAV_COPY_ROOT);
			log.info("bSEurl===>"+bSEurl);
			log.info("nSEurl===>"+nSEurl);
			log.info("destination===>"+destination);
			extract(bSEurl, nSEurl, destination);
		} catch (Exception e) {
			log.error("Error executing BhavCopyExtractor.extract()", e);
			e.printStackTrace();
		}
	}

	public void extract(String bSEurl, String nSEurl, String destination) throws IOException {
		LocalDate downloadDate = LocalDate.now().minusDays(1);
		String url;
		String destinationBSE = destination  + downloadDate + "_BSE.zip";
		Response resultImageResponse;
		FileOutputStream jsoupOut = null;
		String destinationNSE = destination + downloadDate + "_NSE.zip";
		int dayOfMonth = downloadDate.getDayOfMonth();
		String dayOfMonthStr = (dayOfMonth<10) ? "0"+dayOfMonth : dayOfMonth+"";
		String yearString = downloadDate.getYear()+"";
		yearString = yearString.substring(2);
		String monthString = downloadDate.getMonthValue() + "";
		if(monthString.length()==1) {
			monthString = "0"+monthString;
		}
		try {
			Util.deleteDirectory(new File(destination));
		} catch (Exception e) {
			log.error("Error while loading tikkar/Bhave copy", e);
		}
		try {
			url = bSEurl.replace("$DD$", dayOfMonthStr).replace("$MM$", monthString)
					.replace("$YY$", yearString);

			log.info("URL===>" + url);
//			System.out.println("URL===>" + url);
			resultImageResponse = Jsoup.connect(url).ignoreContentType(true).userAgent(USER_AGENT).execute();
			// output here
			jsoupOut = (new FileOutputStream(new java.io.File(destinationBSE)));
			jsoupOut.write(resultImageResponse.bodyAsBytes());
			String bseDestination = destination + "bse";
			FileUtils.deleteDirectory(new File(bseDestination));
			extractFolder(destinationBSE, bseDestination);
		} catch (Exception e) {
			log.error("Error while loading BSE tikkar", e);
		} finally {
			if (Objects.nonNull(jsoupOut)) {
				jsoupOut.close();
			}
		}
		try {
			String monthForNse = downloadDate.getMonth().name().substring(0, 3);

			url = nSEurl.replace("$YYYY$", downloadDate.getYear() + "").replace("$MMM$", monthForNse).replace("$DD$",
					dayOfMonthStr + "");

			log.info("URL===>" + url);
//			System.out.println("URL===>" + url);
//			System.out.println("destinationNSE===>" + destinationNSE);
//			download(url, destinationNSE);
			resultImageResponse = Jsoup.connect(url).ignoreContentType(true).userAgent(USER_AGENT).referrer(REFERER).execute();

			// output here
			jsoupOut = (new FileOutputStream(new java.io.File(destinationNSE)));
			jsoupOut.write(resultImageResponse.bodyAsBytes());
			String nseDestination = destination + "nse";
			FileUtils.deleteDirectory(new File(nseDestination));
			extractFolder(destinationNSE, nseDestination);
		} catch (Exception e) {
			log.error("Error while loading NSE tikkar", e);
		} finally {
			if (Objects.nonNull(jsoupOut)) {
				jsoupOut.close();
			}
		}
		scriptService.loadAndUpdateScriptValue(destination);
		log.info("Extraction Done");
	}

	public static void extractFolder(String zipFile, String extractFolder) {
		try {
			int BUFFER = 2048;
			File file = new File(zipFile);
			ZipFile zip = new ZipFile(file);

			new File(extractFolder).mkdir();
			@SuppressWarnings("rawtypes")
			Enumeration zipFileEntries = zip.entries();

			ZipEntry entry;

			// Process each entry
			while (zipFileEntries.hasMoreElements()) {
				// grab a zip file entry
				entry = (ZipEntry) zipFileEntries.nextElement();
				String currentEntry = entry.getName();

				File destFile = new File(extractFolder, currentEntry);
				File destinationParent = destFile.getParentFile();

				// create the parent directory structure if needed
				destinationParent.mkdirs();

				if (!entry.isDirectory()) {
					BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry));
					int currentByte;
					// establish buffer for writing file
					byte data[] = new byte[BUFFER];

					// write the current file to disk
					FileOutputStream fos = new FileOutputStream(destFile);
					BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);

					// read and write until last byte is encountered
					while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
						dest.write(data, 0, currentByte);
					}
					dest.flush();
					dest.close();
					is.close();
				}
			}
			zip.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		BhavCopyExtractor bhavCopyExtractor = new BhavCopyExtractor();
		try {
//			https://www.bseindia.com/download/BhavCopy/Equity/EQ030321_CSV.ZIP
//			https://www.bseindia.com/download/BhavCopy/Equity/EQ_ISINCODE_030321.zip
//			https://www1.nseindia.com/content/historical/EQUITIES/2021/MAR/cm03MAR2021bhav.csv.zip
//			https://www1.nseindia.com/content/historical/EQUITIES/2021/MAR/cm03MAR2021bhav.csv.zip
			bhavCopyExtractor.extract("http://www.bseindia.com/download/BhavCopy/Equity/EQ_ISINCODE_$DD$$MM$$YY$.zip",
					"https://www1.nseindia.com/content/historical/EQUITIES/$YYYY$/$MMM$/cm$DD$$MMM$$YYYY$bhav.csv.zip",
					"E:/ShareProject/");
		} catch (IOException e) {
			log.error("Error executing BhavCopyExtractor.extract()", e);
		}
	}
}
