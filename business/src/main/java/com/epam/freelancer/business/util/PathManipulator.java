package com.epam.freelancer.business.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.epam.freelancer.business.service.AdminService;

public class PathManipulator {
	private String rootPath;
	private final String reservedRootPath;

	/**
	 * set object root path equals to rootPath param
	 * 
	 * @param rootPath
	 */
	public PathManipulator(String rootPath) {
		this.rootPath = rootPath;
		reservedRootPath = rootPath;
	}

	/**
	 * @param class1
	 *            - root path will be detected from class1
	 * @throws UnsupportedEncodingException
	 */
	public PathManipulator(Class<?> class1) throws UnsupportedEncodingException
	{
		rootPath = URLDecoder.decode(class1.getProtectionDomain()
				.getCodeSource().getLocation().getPath().substring(1), "UTF-8");
		reservedRootPath = rootPath;
	}

	/**
	 * reset root path
	 */
	public void resetPath() {
		rootPath = reservedRootPath;
	}

	public void appendRootPath(String appender) {
		rootPath += appender;
	}

	/**
	 * @param appender
	 *            - path that will be appended to <b>root path</b>
	 * @return
	 * @throws IOException
	 */
	public Path appendDirectory(String appender) throws IOException {
		Path path = Paths.get(rootPath + appender);
		return Files.createDirectories(path);
	}

	public String getRootPath() {
		return rootPath;
	}

	public Path getPath(String relativePath) {
		return Paths.get(rootPath + relativePath);
	}

	public static void main(String[] args) throws Exception {
		PathManipulator pathCreator = new PathManipulator(AdminService.class);
		System.out.println(pathCreator.getRootPath());
		System.out.println(pathCreator.appendDirectory("userData/images"));
		Path path = pathCreator.getPath("customers/5/photo_medium.jpg");
		System.out.println(path);
	}

}
