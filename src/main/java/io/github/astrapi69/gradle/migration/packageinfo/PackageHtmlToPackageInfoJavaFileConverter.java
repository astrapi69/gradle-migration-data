/**
 * The MIT License
 *
 * Copyright (C) 2023 Asterios Raptis
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.astrapi69.gradle.migration.packageinfo;

import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

/**
 * A utility class for converting obsolete package.html files to package-info.java files. It
 * traverses through all the directories of a given project, checks for the existence of
 * package.html files, reads the content of the body tag, creates a new package-info.java file with
 * the extracted content, and deletes the obsolete package.html file
 */
public final class PackageHtmlToPackageInfoJavaFileConverter
{

	/**
	 * Private constructor to prevent instantiation
	 */
	private PackageHtmlToPackageInfoJavaFileConverter()
	{
	}

	/**
	 * The constant PACKAGE_HTML, representing the name of the package.html file.
	 */
	public static final String PACKAGE_HTML = "package.html";

	/**
	 * The constant PACKAGE_INFO_JAVA, representing the name of the package-info.java file.
	 */
	public static final String PACKAGE_INFO_JAVA = "package-info.java";

	/**
	 * The constant BODY_PATTERN, representing the regular expression pattern to match the content
	 * within the &#x3C;body&#x3E; tag in the package.html file.
	 */
	public static final Pattern BODY_PATTERN = Pattern.compile("<body>(.*?)</body>",
		Pattern.DOTALL);

	/**
	 * Processes recursively the path of the given directory.
	 *
	 * @param projectDirectory
	 *            the path of the directory to process
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static void processDirectory(Path projectDirectory) throws IOException
	{
		Files.walk(projectDirectory)
			.filter(path -> path.getFileName().toString().equals(PACKAGE_HTML))
			.forEach(PackageHtmlToPackageInfoJavaFileConverter::processPackageHtml);
	}

	/**
	 * Processes a package.html file by extracting the content of the &#x3C;body&#x3E; tag, creating
	 * a package-info.java file, and deleting the obsolete package.html file
	 *
	 * @param packageHtmlPath
	 *            the path to the package.html file
	 */
	public static void processPackageHtml(Path packageHtmlPath)
	{
		try
		{
			String content = new String(Files.readAllBytes(packageHtmlPath));
			Matcher matcher = BODY_PATTERN.matcher(content);
			if (matcher.find())
			{
				String bodyContent = matcher.group(1).trim();
				createPackageInfoJava(packageHtmlPath.getParent(), bodyContent);
				Files.delete(packageHtmlPath);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Creates a package-info.java file with the given content.
	 *
	 * @param directory
	 *            the directory where the package-info.java file should be created
	 * @param bodyContent
	 *            the content to be included in the package-info.java file
	 */
	public static void createPackageInfoJava(Path directory, String bodyContent)
	{
		Path packageInfoPath = directory.resolve(PACKAGE_INFO_JAVA);
		String packageName = getPackageName(directory);

		String packageInfoContent = String.format("/**\n * %s\n */\npackage %s;\n", bodyContent,
			packageName);

		try (BufferedWriter writer = Files.newBufferedWriter(packageInfoPath))
		{
			writer.write(packageInfoContent);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves the package name based on the directory structure.
	 *
	 * @param directory
	 *            the directory for which the package name is determined
	 * @return the package name
	 */
	private static String getPackageName(Path directory)
	{
		Path projectRoot = directory;
		while (projectRoot != null && !projectRoot.endsWith("src/main/java"))
		{
			projectRoot = projectRoot.getParent();
		}
		if (projectRoot != null)
		{
			return directory.toString().substring(projectRoot.toString().length() + 1)
				.replace(File.separator, ".");
		}
		throw new IllegalStateException(
			"Could not determine the project root from directory: " + directory);
	}
}
