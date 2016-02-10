package com.epam.freelancer.business.resize;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageResize {

	private static final int IMG_HEIGHT_LG = 500;
	private static final int IMG_WIDTH_LG = 500;
	private static final int IMG_WIDTH_MD = 200;
	private static final int IMG_HEIGHT_MD = 200;
	private static final int IMG_WIDTH_SM = 100;
	private static final int IMG_HEIGHT_SM = 100;

	public ImageResize(String pathToOriginalImg, String pathToSaveSmImg,
			String pathToSaveMdImg, String pathToSaveLgImg) throws IOException
	{
		BufferedImage originalImage = ImageIO.read(new File(pathToOriginalImg));
		int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB
				: originalImage.getType();

		BufferedImage imageSM = resizeImage(originalImage, type, IMG_WIDTH_SM,
				IMG_HEIGHT_SM);
		ImageIO.write(imageSM, "jpg", new File(pathToSaveSmImg));

		BufferedImage imageMD = resizeImage(originalImage, type, IMG_WIDTH_MD,
				IMG_HEIGHT_MD);
		ImageIO.write(imageMD, "jpg", new File(pathToSaveMdImg));

		BufferedImage imageLG = resizeImage(originalImage, type, IMG_WIDTH_LG,
				IMG_HEIGHT_LG);
		ImageIO.write(imageLG, "jpg", new File(pathToSaveLgImg));
	}

	public static void main(String[] args) throws IOException {
		new ImageResize("original.jpg", "sm.jpg", "md.jpg", "lg.jpg");
	}

	private BufferedImage resizeImage(BufferedImage originalImage, int type,
			int IMG_WIDTH, int IMG_HEIGHT)
	{
		BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT,
				type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
		g.dispose();

		return resizedImage;
	}
}
