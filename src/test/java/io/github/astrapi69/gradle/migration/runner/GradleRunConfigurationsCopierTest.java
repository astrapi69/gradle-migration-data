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
package io.github.astrapi69.gradle.migration.runner;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;

import io.github.astrapi69.gradle.migration.data.CopyGradleRunConfigurations;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.github.astrapi69.file.search.PathFinder;

/**
 * The unit test class for the class {@link GradleRunConfigurationsCopier}
 */
public class GradleRunConfigurationsCopierTest
{

	@Test
	public void testGetContentOfNonExistingSection()
	{
		File srcTestResourcesTest = PathFinder.getRelativePath(PathFinder.getSrcTestResourcesDir(),
			"test");

		File buildGradle = new File(srcTestResourcesTest, "/test-build.gradle");
		assertThrows(IllegalArgumentException.class, () -> {
			GradleRunConfigurationsCopier.getContentOf("nonExistingSection", buildGradle);
		});
	}

	@Test
	public void testGetContentOfDependencies() throws IOException
	{
		File srcTestResourcesTest = PathFinder.getRelativePath(PathFinder.getSrcTestResourcesDir(),
			"test");

		File buildGradle = new File(srcTestResourcesTest, "/test-build.gradle");
		String expectedContent = "dependencies {\n"
			+ "        classpath \"com.github.ben-manes:gradle-versions-plugin:$gradlePluginVersionsVersion\"\n"
			+ "        classpath \"gradle.plugin.com.hierynomus.gradle.plugins:license-gradle-plugin:$licenseGradlePluginVersion\"\n"
			+ "        classpath \"org.ajoberstar.grgit:grgit-gradle:$grgitGradlePluginVersion\"\n"
			+ "        classpath \"com.diffplug.spotless:spotless-plugin-gradle:$spotlessGradlePluginVersion\"\n"
			+ "    }";
		String actualContent = GradleRunConfigurationsCopier.getContentOf("dependencies",
			buildGradle);
		assertEquals(expectedContent, actualContent);
	}

	@Test
	public void testGetContentOfRepositories() throws IOException
	{
		File srcTestResourcesTest = PathFinder.getRelativePath(PathFinder.getSrcTestResourcesDir(),
			"test");

		File buildGradle = new File(srcTestResourcesTest, "/test-build.gradle");
		String expectedContent = "repositories {\n" + "        maven {\n"
			+ "            url \"https://plugins.gradle.org/m2/\"\n" + "        }\n" + "    }";
		String actualContent = GradleRunConfigurationsCopier.getContentOf("repositories",
			buildGradle);
		assertEquals(expectedContent, actualContent);
	}

	@Test
	public void testGetContentOfBuildscript() throws IOException
	{
		File srcTestResourcesTest = PathFinder.getRelativePath(PathFinder.getSrcTestResourcesDir(),
			"test");

		File buildGradle = new File(srcTestResourcesTest, "/test-build.gradle");
		String expectedContent = "buildscript {\n    repositories {\n        maven {\n            url \"https://plugins.gradle.org/m2/\"\n        }\n    }\n    dependencies {\n        classpath \"com.github.ben-manes:gradle-versions-plugin:$gradlePluginVersionsVersion\"\n        classpath \"gradle.plugin.com.hierynomus.gradle.plugins:license-gradle-plugin:$licenseGradlePluginVersion\"\n        classpath \"org.ajoberstar.grgit:grgit-gradle:$grgitGradlePluginVersion\"\n        classpath \"com.diffplug.spotless:spotless-plugin-gradle:$spotlessGradlePluginVersion\"\n    }\n}";
		String actualContent = GradleRunConfigurationsCopier.getContentOf("buildscript",
			buildGradle);
		assertEquals(expectedContent, actualContent);
	}

	@Test
	void testConstructorWithNullShouldThrowException()
	{
		assertThrows(NullPointerException.class, () -> {
			GradleRunConfigurationsCopier.of(null);
		});
	}

	/**
	 * Test method for
	 * {@link GradleRunConfigurationsCopier#newCopyGradleRunConfigurations(String, String, String, String, boolean, boolean)}
	 * that copy run configurations file from a source project to a target project and modifies its
	 * content
	 */
	@Test
	@Disabled
	public void testCopyIdeaRunConfigurations()
	{
		String sourceProjectDirNamePrefix;
		String targetProjectDirNamePrefix;
		CopyGradleRunConfigurations copyGradleRunConfigurationsData;
		boolean onlyRunConfigurations;
		boolean runConfigurationsInSameFolder;
		String sourceProjectName;
		String targetProjectName;
		String sourceGithubUser;
		String targetGithubUser;
		// copy
		sourceProjectName = "gradle-migration-data";
		targetProjectName = "email-tails";
		sourceGithubUser = "astrapi69";
		targetGithubUser = "lightblueseas";
		// targetGithubUser = "astrapi69";
		sourceProjectDirNamePrefix = "/home/astrapi69/dev/git/hub/" + sourceGithubUser + "/";
		targetProjectDirNamePrefix = "/home/astrapi69/dev/git/hub/" + targetGithubUser + "/";
		runConfigurationsInSameFolder = false;
		onlyRunConfigurations = false;

		GradleRunConfigurationsCopier.copyRunConfigurations(sourceProjectName, targetProjectName,
				sourceProjectDirNamePrefix, targetProjectDirNamePrefix, onlyRunConfigurations,
				runConfigurationsInSameFolder);
	}

}
