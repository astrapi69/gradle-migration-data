package io.github.astrapi69.gradle.migration.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.collection.list.ListExtensions;
import io.github.astrapi69.collection.list.ListFactory;
import io.github.astrapi69.collection.map.MapFactory;
import io.github.astrapi69.collection.properties.PropertiesExtensions;
import io.github.astrapi69.file.search.PathFinder;
import lombok.NonNull;

/**
 * The unit test class for the class {@link JacksonTomlExtensions}
 */
public class JacksonTomlExtensionsTest
{
	/**
	 * Test method for {@link JacksonTomlExtensions}
	 */
	@Test
	// @Disabled
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
	// @Disabled
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
	// @Disabled
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
