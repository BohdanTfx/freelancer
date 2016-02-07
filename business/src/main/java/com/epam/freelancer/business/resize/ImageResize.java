package com.epam.freelancer.business.resize;


import org.apache.log4j.Logger;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageResize {

    private static final int IMG_HEIGHT_LG = 500;
    private static final int IMG_WIDTH_LG = 500;
    private static final int IMG_WIDTH_MD = 200;
    private static final int IMG_HEIGHT_MD = 200;
    private static final int IMG_WIDTH_SM = 100;
    private static final int IMG_HEIGHT_SM = 100;

    public ImageResize(String pathToOriginalImg, String pathToSaveSmImg, String pathToSaveMdImg, String pathToSaveLgImg) throws IOException {
        BufferedImage originalImage = ImageIO.read(new File(pathToOriginalImg));
        int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

        BufferedImage imageSM = resizeImage(originalImage, type, IMG_WIDTH_SM, IMG_HEIGHT_SM);
        ImageIO.write(imageSM, "jpg", new File(pathToSaveSmImg));

        BufferedImage imageMD = resizeImage(originalImage, type, IMG_WIDTH_MD, IMG_HEIGHT_MD);
        ImageIO.write(imageMD, "jpg", new File(pathToSaveMdImg));

        BufferedImage imageLG = resizeImage(originalImage, type, IMG_WIDTH_LG, IMG_HEIGHT_LG);
        ImageIO.write(imageLG, "jpg", new File(pathToSaveLgImg));
    }

/*    public static void main(String[] args) {
        try {
            new ImageResize("image/вова.jpg", "image/sm/sm.jpg", "image/sm/md.jpg", "image/sm/lg.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    private BufferedImage resizeImage(BufferedImage originalImage, int type, int IMG_WIDTH, int IMG_HEIGHT) {
        BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
        g.dispose();

        return resizedImage;
    }
}
