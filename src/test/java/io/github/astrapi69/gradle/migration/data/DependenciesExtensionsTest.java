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
package io.github.astrapi69.gradle.migration.data;

import static io.github.astrapi69.gradle.migration.extension.DependenciesExtensions.getLibsVersionTomlMapAsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.text.WordUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.github.astrapi69.file.read.ReadFileExtensions;
import io.github.astrapi69.file.search.PathFinder;
import io.github.astrapi69.gradle.migration.extension.DependenciesExtensions;

/**
 * The unit test class for the class {@link DependenciesExtensions}
 */
class DependenciesExtensionsTest
{

	@Test
	void getDependenciesContent() throws IOException
	{
		File projectDirectory = PathFinder.getProjectDirectory();
		File gradleDirectory = PathFinder.getRelativePath(projectDirectory, "gradle");
		File dependenciesGradle = PathFinder.getRelativePath(gradleDirectory,
			"dependencies.gradle");
		String dependenciesContent = DependenciesExtensions
			.getDependenciesContent(dependenciesGradle);
		assertNotNull(dependenciesContent);
	}

	@Test
	void getDependenciesAsStringList() throws IOException
	{
		File projectDirectory = PathFinder.getProjectDirectory();
		File gradleDirectory = PathFinder.getRelativePath(projectDirectory, "gradle");
		File dependenciesGradle = PathFinder.getRelativePath(gradleDirectory,
			"dependencies.gradle");
		String dependenciesContent = DependenciesExtensions
			.getDependenciesContent(dependenciesGradle);
		List<String> dependenciesAsStringList = DependenciesExtensions
			.getDependenciesAsStringList(dependenciesContent);
		assertNotNull(dependenciesAsStringList);
	}

	/**
	 * Test method for {@link DependenciesExtensions#getDependencyInfo(String)}
	 */
	@Test
	void testgetDependencyInfo()
	{
		DependencyInfo actual;
		DependencyInfo expected;
		String dependencyRow = "    compileOnly(\"org.projectlombok:lombok:$lombokVersion\")";
		actual = DependenciesExtensions.getDependencyInfo(dependencyRow);
		assertNotNull(actual);
		expected = DependencyInfo.builder().scope("compileOnly").groupId("org.projectlombok")
			.artifactId("lombok").version("$lombokVersion").build();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link DependenciesExtensions#getProjectVersionKeyName(String)}
	 */
	@Test
	void getProjectVersionKeyName()
	{
		String actual;
		String expected;
		String input;
		String templateProjectName;
		templateProjectName = DependenciesInfo.JAVA_LIBRARY_TEMPLATE_NAME;

		input = templateProjectName;
		expected = "JavaLibraryTemplate";
		assertEquals(expected,
			WordUtils.capitalizeFully(input, new char[] { '-' }).replaceAll("-", ""));

		actual = DependenciesExtensions.getProjectVersionKeyName(input);
		expected = "javaLibraryTemplateVersion";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link DependenciesExtensions#getVersionMap(File, String)}
	 */
	@Test
	@Disabled
	public void testGetVersionMap() throws IOException
	{
		File gradlePropertiesFile = PathFinder.getRelativePath(PathFinder.getProjectDirectory(),
			"gradle.properties");
		assertTrue(gradlePropertiesFile.exists());
		// 1. Load all version from gradle.properties
		Map<String, String> versionMap = DependenciesExtensions.getVersionMap(gradlePropertiesFile,
			"Version");

		assertEquals(18, versionMap.size());
	}

	/**
	 * Test method for generation of the file 'libs.versions.toml'
	 */
	@Test
	@Disabled
	public void testGenerateLibsVersionTomlFile() throws IOException
	{
		File projectDirectory = PathFinder.getProjectDirectory();
		File gradleDirectory = PathFinder.getRelativePath(projectDirectory, "gradle");
		File dependenciesGradle = PathFinder.getRelativePath(gradleDirectory,
			"dependencies.gradle");

		File gradlePropertiesFile = PathFinder.getRelativePath(projectDirectory,
			"gradle.properties");
		assertTrue(gradlePropertiesFile.exists());
		// 1. Load all version from gradle.properties
		Map<String, String> versionMap = DependenciesExtensions.getVersionMap(gradlePropertiesFile,
			"Version");
		String dependenciesContent = DependenciesExtensions
			.getDependenciesContent(dependenciesGradle);
		List<String> dependenciesAsStringList = DependenciesExtensions
			.getDependenciesAsStringList(dependenciesContent);


		List<DependencyInfo> dependencyInfos = DependenciesExtensions
			.getDependencyInfos(dependenciesAsStringList, versionMap);
		assertNotNull(dependencyInfos);
		assertTrue(dependencyInfos.size() == 17);

		String newDependenciesStructure = DependenciesExtensions
			.getNewDependenciesStructure(dependencyInfos);

		System.out.println(newDependenciesStructure);

		String libsVersionTomlMapAsString = getLibsVersionTomlMapAsString(dependencyInfos);
		// 2. Load all version from libs.versions.toml
		File libsVersionsToml = PathFinder.getRelativePath(gradleDirectory, "libs.versions.toml");
		String libsVersionsTomlFileContent = ReadFileExtensions.fromFile(libsVersionsToml);
		assertEquals(libsVersionsTomlFileContent, libsVersionTomlMapAsString);

	}

}