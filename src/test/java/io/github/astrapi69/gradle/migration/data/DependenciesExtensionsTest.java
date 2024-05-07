package io.github.astrapi69.gradle.migration.data;

import static io.github.astrapi69.gradle.migration.data.DependenciesExtensions.getLibsVersionTomlMapAsString;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.text.WordUtils;
import org.junit.jupiter.api.Test;

import io.github.astrapi69.file.search.PathFinder;

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
	// @Disabled
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

	@Test
	public void testGetDependencyInfos() throws IOException
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

		String libsVersionTomlMapAsString = getLibsVersionTomlMapAsString(dependencyInfos);
		System.out.println(libsVersionTomlMapAsString);

	}

}