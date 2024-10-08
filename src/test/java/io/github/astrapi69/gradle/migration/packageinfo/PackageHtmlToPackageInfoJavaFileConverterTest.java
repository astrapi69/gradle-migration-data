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

		targetProjectName = "swing-base-components";
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
