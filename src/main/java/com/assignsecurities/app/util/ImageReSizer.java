package com.assignsecurities.app.util;

import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.assignsecurities.app.exception.ServiceException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;


public class ImageReSizer {


	/**
	 * This method will resize the image to the specified width and will maintain
	 * aspect ratio for the height of the picture to maintain quality
	 * 
	 * @param width
	 * @return
	 * @throws Exception
	 */
	public static byte[] resize(byte[] sourceBFile, int width) {
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(sourceBFile);
			Iterator<?> readers = ImageIO.getImageReadersByFormatName("jpg");
			// Logic to implement image resizing
			ImageReader reader = (ImageReader) readers.next();
			Object source = bis;
			ImageInputStream iis = ImageIO.createImageInputStream(source);
			reader.setInput(iis, true);
			ImageReadParam param = reader.getDefaultReadParam();

			Image image = reader.read(0, param);
			// got an image file

			BufferedImage bImageFromConvert = new BufferedImage(image.getWidth(null), image.getHeight(null),
					BufferedImage.TYPE_INT_RGB);
			// ByteArrayInputStream in = new ByteArrayInputStream(sourceBFile);
			// BufferedImage bImageFromConvert = ImageIO.read(in);
			// BufferedImage bim = ImageIO.read(in);
			Image resizedImg = bImageFromConvert.getScaledInstance(width, -1, Image.SCALE_FAST);
			int scaled_height = resizedImg.getHeight(null);

			BufferedImage rBimg = new BufferedImage(width, scaled_height, bImageFromConvert.getType());
			// Create Graphics object
			Graphics2D g = rBimg.createGraphics();// Draw the resizedImg from 0,0 with no ImageObserver
			g.drawImage(resizedImg, 0, 0, null);

			// Dispose the Graphics object, we no longer need it
			g.dispose();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(rBimg, "jpg", baos);
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			baos.close();
			return imageInByte;

		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}
	public static BufferedImage decodeToImage(String imageString) {
		 
        BufferedImage image = null;
        byte[] imageByte;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
        	throw new ServiceException(e.getMessage(), e);
        }
        return image;
    }
	
	public static byte[] resize(String imageString, int width) {
		try {
			 BufferedImage bImageFromConvert = decodeToImage(imageString);
			// BufferedImage bim = ImageIO.read(in);
			Image resizedImg = bImageFromConvert.getScaledInstance(width, -1, Image.SCALE_FAST);
			int scaled_height = resizedImg.getHeight(null);

			BufferedImage rBimg = new BufferedImage(width, scaled_height, bImageFromConvert.getType());
			// Create Graphics object
			Graphics2D g = rBimg.createGraphics();// Draw the resizedImg from 0,0 with no ImageObserver
			g.drawImage(resizedImg, 0, 0, null);

			// Dispose the Graphics object, we no longer need it
			g.dispose();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(rBimg, "jpg", baos);
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			baos.close();
			return imageInByte;

		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}
//	public static byte[] scaleImageByWidth(byte[] imageBytes, int width,
//			String imageFormatName) {
//		Image img = null;
//		int height = 0;
//		try {
//			img = ImageIO.read(new ByteArrayInputStream(imageBytes));
//			int old_w = img.getWidth(null);
//			int old_h = img.getHeight(null);
//			if (old_h > 0 && old_w > 0) {
//				height = (int) ((float) width / old_w * old_h);
//			}
//		} catch (Exception e) {
//			throw new ServiceException(e.getMessage(), e);
//		}
//		return ImageUtils.scaleImage(imageBytes, width, height,
//				Image.SCALE_SMOOTH, imageFormatName);
//	}
}
