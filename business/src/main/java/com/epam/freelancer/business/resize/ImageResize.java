package com.epam.freelancer.business.resize;

import java.io.File;
import java.io.IOException;

import net.coobird.thumbnailator.Thumbnails;

public class ImageResize {

	public ImageResize() {
	}

	public static void resizeImage(String path, String pathToSave, int width,
			int height) throws IOException
	{
		Thumbnails.of(new File(path)).size(width, height).outputFormat("JPEG")
				.outputQuality(1).toFile(pathToSave);
	}
}
