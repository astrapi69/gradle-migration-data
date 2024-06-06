package io.github.astrapi69.gradle.migration.toml;

import io.github.astrapi69.file.copy.CopyFileExtensions;
import io.github.astrapi69.file.create.FileFactory;
import io.github.astrapi69.file.read.ReadFileExtensions;
import io.github.astrapi69.file.search.PathFinder;
import io.github.astrapi69.file.write.StoreFileExtensions;
import io.github.astrapi69.gradle.migration.data.DependenciesInfo;
import io.github.astrapi69.gradle.migration.data.DependencyInfo;
import io.github.astrapi69.gradle.migration.extension.DependenciesExtensions;
import io.github.astrapi69.gradle.migration.runner.GradleRunConfigurationsCopier;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static io.github.astrapi69.gradle.migration.extension.DependenciesExtensions.getLibsVersionTomlMapAsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The unit test class for migrate to toml versions
 */
public class MigrateToTomlVersions
{

	public static File getGradleDirectory() throws IOException
	{
		File projectDirectory = PathFinder.getProjectDirectory();
		File gradleDirectory = PathFinder.getRelativePath(projectDirectory, "gradle");
		return gradleDirectory;
	}


	public static String newLibsVersionsTomlAsString(MigrationInfo migrationInfo) throws IOException {
		File dependenciesGradle = PathFinder.getRelativePath(migrationInfo.getGradleDirectory(),
				"dependencies.gradle");

		File gradlePropertiesFile = PathFinder.getRelativePath(migrationInfo.getProjectDirectory(),
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

		String newDependenciesStructure = DependenciesExtensions
				.getNewDependenciesStructure(dependencyInfos);

		System.out.println(newDependenciesStructure);

		String libsVersionTomlMapAsString = getLibsVersionTomlMapAsString(dependencyInfos);
		StoreFileExtensions.toFile(dependenciesGradle, newDependenciesStructure, StandardCharsets.UTF_8);
		return libsVersionTomlMapAsString;
	}

	public static File newLibsVersionsTomlFile(MigrationInfo migrationInfo) throws IOException {
		String libsVersionTomlMapAsString = newLibsVersionsTomlAsString(migrationInfo);
		// 2. store all version to libs.versions.toml
		File libsVersionsToml = PathFinder.getRelativePath(migrationInfo.getGradleDirectory(), "libs.versions.toml");
		if (!libsVersionsToml.exists())
		{
			StoreFileExtensions.toFile(libsVersionsToml, libsVersionTomlMapAsString);
		}
		return libsVersionsToml;
	}

	public static File newLibsVersionsTomlFile(String projectDirectoryName) throws IOException {
		return newLibsVersionsTomlFile(MigrationInfo.fromAbsolutePath(projectDirectoryName));
	}

	@Test
	public void testNewLibsVersionsTomlFile() throws IOException
	{
		String projectDirectoryName;

		File gradleDirectory = getGradleDirectory();

		String versionCatalogUpdateFileName = "version-catalog-update.gradle";
		File sourceVersionCatalogUpdateFile = PathFinder.getRelativePath(gradleDirectory, versionCatalogUpdateFileName);

		String targetProjectName = "xml-base";
		String targetProjectDirNamePrefix = "/run/media/astrapi69/backups/git/hub/astrapi69/";
		projectDirectoryName = targetProjectDirNamePrefix + targetProjectName;
		File libsVersionsTomlFile = newLibsVersionsTomlFile(projectDirectoryName);
		MigrationInfo migrationInfo = MigrationInfo.fromAbsolutePath(projectDirectoryName);
		File destinationVersionCatalogUpdateFile = FileFactory.newFile(migrationInfo.getGradleDirectory(), versionCatalogUpdateFileName);
		CopyFileExtensions.copyFile(sourceVersionCatalogUpdateFile, destinationVersionCatalogUpdateFile, StandardCharsets.UTF_8, StandardCharsets.UTF_8, true);
		String libsVersionTomlMapAsString = newLibsVersionsTomlAsString(migrationInfo);
		String libsVersionsTomlFileContent = ReadFileExtensions.fromFile(libsVersionsTomlFile);
		assertEquals(libsVersionsTomlFileContent, libsVersionTomlMapAsString);
	}

	@Test
	public void testCopyRunConfigurationsOfVersionCatalogUpdate() throws IOException
	{
		String sourceProjectName = DependenciesInfo.JAVA_LIBRARY_TEMPLATE_NAME;
		String targetProjectName = "xml-base";
		String sourceProjectDirNamePrefix = "/run/media/astrapi69/backups/git/hub/astrapi69/";
		String targetProjectDirNamePrefix = "/run/media/astrapi69/backups/git/hub/astrapi69/";
		GradleRunConfigurationsCopier.copyOnlyRunConfigurations(sourceProjectName, targetProjectName, sourceProjectDirNamePrefix, targetProjectDirNamePrefix);
	}

}
