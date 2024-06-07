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
package io.github.astrapi69.gradle.migration.extension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

import com.fasterxml.jackson.dataformat.toml.TomlMapper;

import io.github.astrapi69.file.search.PathFinder;
import io.github.astrapi69.gradle.migration.data.DependencyInfo;

/**
 * The unit test class for the class {@link JacksonTomlExtensions}
 */
public class JacksonTomlExtensionsTest
{


	private File tempFile;

	@BeforeEach
	public void setUp() throws IOException
	{
		tempFile = Files.createTempFile("test", ".toml").toFile();
	}

	@AfterEach
	public void tearDown()
	{
		if (tempFile.exists())
		{
			tempFile.delete();
		}
	}


	@Test
	public void testRead() throws IOException
	{
		String tomlContent = "key = \"value\"";
		try (FileWriter writer = new FileWriter(tempFile))
		{
			writer.write(tomlContent);
		}

		Map<String, String> result = JacksonTomlExtensions.read(tempFile);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("value", result.get("key"));
	}

	@Test
	public void testWriteValue() throws IOException
	{
		Map<String, Map<String, String>> data = new HashMap<>();
		Map<String, String> innerMap = new HashMap<>();
		innerMap.put("key", "value");
		data.put("section", innerMap);

		JacksonTomlExtensions.writeValue(tempFile, data);

		TomlMapper mapper = new TomlMapper();
		Map<String, Map> result = mapper.readValue(tempFile, Map.class);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("value", ((Map)result.get("section")).get("key"));
	}

	/**
	 * Test method for {@link JacksonTomlExtensions}
	 */
	@Test
	@Disabled
	public void testWriteToTomlFileWithJackson() throws IOException
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

		Map<String, Map> libsVersionTomlMap = DependenciesExtensions
			.getLibsVersionTomlMap(dependencyInfos);
	}

	/**
	 * Test method for {@link JacksonTomlExtensions}
	 */
	@Test
	@Disabled
	public void testReadTomlFileWithJackson() throws IOException
	{
		File tomlFile = PathFinder.getRelativePath(PathFinder.getSrcTestResourcesDir(),
			"libs.versions.toml");


		assertTrue(tomlFile.exists());
		Map toml = JacksonTomlExtensions.read(tomlFile);
		System.out.println(toml);
		assertNotNull(toml);


	}

	/**
	 * Test method for {@link JacksonTomlExtensions}
	 */
	@Test
	@Disabled
	public void testReadGradleProperties() throws IOException
	{
		File gradlePropertiesFile = PathFinder.getRelativePath(PathFinder.getProjectDirectory(),
			"gradle.properties");
		assertTrue(gradlePropertiesFile.exists());
		// 1. Load all version from gradle.properties
		Map<String, String> versionMap = DependenciesExtensions.getVersionMap(gradlePropertiesFile,
			"Version");

		assertEquals(18, versionMap.size());


		// 2. read and load all dependencies from dependencies.gradle file
		// to implement
		// 3. read and load all plugins from build.gradle file
		// to implement
		// 4. create toml file and combine all information together
	}

	/**
	 * Test method for {@link JacksonTomlExtensions}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(JacksonTomlExtensions.class);
	}
}
