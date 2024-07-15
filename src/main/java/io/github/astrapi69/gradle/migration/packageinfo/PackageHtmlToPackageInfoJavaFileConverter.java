package io.github.astrapi69.gradle.migration.packageinfo;

import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

/**
 * A utility class for converting obsolete package.html files to package-info.java files. It
 * traverses through all the directories of a given project, checks for the existence of
 * package.html files, reads the content of the <body> tag, creates a new package-info.java file
 * with the extracted content, and deletes the obsolete package.html file.
 */
public class PackageHtmlToPackageInfoJavaFileConverter
{

	public static final String PACKAGE_HTML = "package.html";
	public static final String PACKAGE_INFO_JAVA = "package-info.java";
	public static final Pattern BODY_PATTERN = Pattern.compile("<body>(.*?)</body>",
		Pattern.DOTALL);

	/**
	 * Processes recursively the path of the given directory
	 *
	 * @param projectDirectory
	 *            the path of the directory to process
	 */
	public static void processDirectory(Path projectDirectory) throws IOException
	{
		Files.walk(projectDirectory).filter(path -> path.getFileName().toString().equals(PACKAGE_HTML))
			.forEach(PackageHtmlToPackageInfoJavaFileConverter::processPackageHtml);
	}

	/**
	 * Processes a package.html file by extracting the content of the <body> tag, creating a
	 * package-info.java file, and deleting the obsolete package.html file.
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
