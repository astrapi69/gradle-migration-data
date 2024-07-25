package io.github.astrapi69.gradle.migration.packageinfo;

import static io.github.astrapi69.gradle.migration.packageinfo.PackageHtmlToPackageInfoJavaFileConverter.processDirectory;

import java.io.*;
import java.nio.file.*;

import org.junit.jupiter.api.*;

class PackageHtmlToPackageInfoJavaFileConverterTest
{

	@Test
	@Disabled
	void testConversion() throws IOException
	{
		String targetProjectDirNamePrefix;
		String targetProjectName;
		String targetGithubUser;

		targetProjectName = "meanbean-factories";
		// targetGithubUser = "lightblueseas";
		targetGithubUser = "astrapi69";
		targetProjectDirNamePrefix = "/run/media/astrapi69/backups/git/hub/" + targetGithubUser
			+ "/";

		String projectDirectoryName = targetProjectDirNamePrefix + targetProjectName
			+ "/src/main/java/";
		Path startDir = Paths.get(projectDirectoryName); // Change this to your project's root
															// directory
		File startDirFile = startDir.toFile();

		try
		{
			processDirectory(startDir);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
