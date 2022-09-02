package com.assignsecurities.app.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.poi.util.IOUtils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.assignsecurities.dm.PropertyKeys;
import com.assignsecurities.service.impl.ApplicationPropertiesService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileCloudUtil {
	private String bucketName, keyName;
	private BufferedImage buffImage;
	private File file;
	private byte[] bFile;
	private String base64FileString;

	public FileCloudUtil(String fileName, String bucketName, BufferedImage buffImage) {
		this.buffImage = buffImage;
		this.bucketName = bucketName;
		this.keyName = fileName;
	}

	public FileCloudUtil(String fileName, String bucketName, File file) {
		this.file = file;
		this.bucketName = bucketName;
		this.keyName = fileName;
	}

	public FileCloudUtil(String fileName, String bucketName, byte[] bFile) {
		this.bFile = bFile;
		this.bucketName = bucketName;
		this.keyName = fileName;
	}

	public FileCloudUtil(String fileName, String bucketName, String base64FileString) {
		this.base64FileString = base64FileString;
		this.bucketName = bucketName;
		this.keyName = fileName;
	}
	
	
	public boolean upload() throws AmazonClientException, InterruptedException, IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ObjectMetadata meta = new ObjectMetadata();
		InputStream is = null;
		if (Objects.nonNull(buffImage)) {
			ImageIO.write(buffImage, "jpg", os);
			byte[] buffer = os.toByteArray();
			meta.setContentLength(buffer.length);
			is = new ByteArrayInputStream(buffer);
		} else if (Objects.nonNull(file)) {
			byte[] buffer = FileUtils.readFileToByteArray(file);
			meta.setContentLength(buffer.length);
			is = new FileInputStream(file);
		} else if (Objects.nonNull(bFile)) {
			meta.setContentLength(bFile.length);
			is = new ByteArrayInputStream(bFile);
		} else if (Objects.nonNull(base64FileString)) {
			byte[] buffer = Base64.getDecoder().decode(base64FileString.getBytes());
			meta.setContentLength(buffer.length);
			is = new ByteArrayInputStream(buffer);
		}

		AmazonS3 s3 = createClient();
		log.info("Cloud uploading started... " + bucketName + "/" + keyName);
		s3.putObject(new PutObjectRequest(bucketName, keyName, is, meta));
		log.info("Cloud uploading finished... " + bucketName + "/" + keyName);
		return true;
	}

	public static Boolean isObjectExists(String bucketName, String keyName) {
		AmazonS3 client = createClient();
		return client.doesObjectExist(bucketName, keyName);
	}
	
	public static void delete(String bucketName, String keyName) {
		AmazonS3 client = createClient();
		client.deleteObject(bucketName, keyName);
	}
	

	public static InputStream downloadFileStream(String bucketName, String keyName) {
		AmazonS3 client = createClient();
		S3Object o = client.getObject(bucketName, keyName);
		S3ObjectInputStream s3is = o.getObjectContent();
		return s3is.getDelegateStream();
	}

	public static byte[] downloadFile(String bucketName, String keyName) {
		byte[] content = null;
		AmazonS3 client = createClient();
		log.info("Downloading an object with key= " + keyName);
		final S3Object s3Object = client.getObject(bucketName, keyName);
		final S3ObjectInputStream stream = s3Object.getObjectContent();
		try {
			content = IOUtils.toByteArray(stream);
			log.info("File downloaded successfully.");
			s3Object.close();
		} catch (final IOException ex) {
			log.info("IO Error Message= " + ex.getMessage());
		}
		return content;
	}
	
	public static String downloadFileBase64(String bucketName, String keyName) {
		byte[] content =downloadFile(bucketName, keyName);
		return Base64.getEncoder().encodeToString(content);
	}

	public static AmazonS3 createClient() {
//		Regions clientRegion = Regions.AP_SOUTH_1;
		Regions clientRegion = Regions.US_EAST_2;
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(
				ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.AWS_S3_ACCESSKEY),
				ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.AWS_S3_SECRETKEY));
		ClientConfiguration clientConfiguration = new ClientConfiguration().withConnectionTimeout(1000) // 30,000 ms
				.withSocketTimeout(10000);
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(clientRegion)
				.withClientConfiguration(clientConfiguration)
				.withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();
		return s3Client;
	}

	public Boolean uploadBytes() throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(buffImage, "png", os);
		byte[] buffer = os.toByteArray();
		InputStream is = new ByteArrayInputStream(buffer);
		ObjectMetadata meta = new ObjectMetadata();
		meta.setContentLength(buffer.length);
		createClient().putObject(new PutObjectRequest(bucketName, keyName, is, meta));
		return true;
	}

	public static void main(String[] args) {
		ApplicationPropertiesService.addProperty(PropertyKeys.AWS_S3_ACCESSKEY, "AKIAJOSOBOATWQZUXGLA");
		ApplicationPropertiesService.addProperty(PropertyKeys.AWS_S3_SECRETKEY,
				"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");
//		System.out.println(ImageCloudUtil.isObjectExists("blogoffline.blogs.images.mobileapp", "dipalipullarwar-NowBlogIt-1574302688277.jpg"));
		try {
			String fileName = "myTeplate.docx";
			File file1 = new File("E:/ShareProject/docx/template/myTeplate.docx");
			String bucketName = "assignsecurities";
			FileCloudUtil imageCloudUtil = new FileCloudUtil(fileName, bucketName, file1);
//			System.out.println("=====>"+imageCloudUtil.isObjectExists(bucketName, fileName));
//			imageCloudUtil.upload();
//			System.out.println("Cloud uploading finished... " + bucketName + "/" + fileName);
//			client.getObject(bucketName, key)
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	String encoded = Base64.getEncoder().encodeToString("Hello".getBytes());
//	println(encoded);   // Outputs "SGVsbG8="
//
//	String decoded = new String(Base64.getDecoder().decode(encoded.getBytes()));
//	println(decoded)    // Outputs "Hello"
	
	
//	public File getFile(String fileName) throws Exception {
//	    if (StringUtils.isEmpty(fileName)) {
//	        throw new Exception("file name can not be empty");
//	    }
//	    S3Object s3Object = amazonS3.getObject("bucketname", fileName);
//	    if (s3Object == null) {
//	        throw new Exception("Object not found");
//	    }
//	    File file = new File("you file path");
//	    Files.copy(s3Object.getObjectContent(), file.toPath());
//	    inputStream.close();
//	    return file;
//	}
}