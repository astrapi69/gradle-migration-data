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
package io.github.astrapi69.gradle.migration.toml;

import static io.github.astrapi69.gradle.migration.extension.DependenciesExtensions.getLibsVersionTomlMapAsString;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import io.github.astrapi69.file.copy.CopyFileExtensions;
import io.github.astrapi69.file.create.FileFactory;
import io.github.astrapi69.file.search.PathFinder;
import io.github.astrapi69.file.write.StoreFileExtensions;
import io.github.astrapi69.gradle.migration.extension.DependenciesExtensions;
import io.github.astrapi69.gradle.migration.info.DependenciesInfo;
import io.github.astrapi69.gradle.migration.info.DependencyInfo;
import io.github.astrapi69.gradle.migration.info.GradleProjectInfo;
import io.github.astrapi69.gradle.migration.info.MigrationInfo;
import io.github.astrapi69.gradle.migration.info.ProjectTomlStructureInfo;
import io.github.astrapi69.gradle.migration.runner.GradleRunConfigurationsCopier;

/**
 * The class {@link MigrateToTomlVersions} provides methods for migrate to new toml project
 * structure
 */
public class MigrateToTomlVersions
{

	public static GradleProjectInfo newLibsVersionsTomlAsString(MigrationInfo migrationInfo)
		throws IOException
	{
		GradleProjectInfo gradleProjectInfo = GradleProjectInfo.builder().build();
		File dependenciesGradle = PathFinder.getRelativePath(migrationInfo.getGradleDirectory(),
			DependenciesInfo.DEPENDENCIES_GRADLE_FILENAME);

		File gradlePropertiesFile = PathFinder.getRelativePath(migrationInfo.getProjectDirectory(),
			DependenciesInfo.GRADLE_PROPERTIES_FILENAME);
		// 1. Load all version from gradle.properties
		Map<String, String> versionMap = DependenciesExtensions.getVersionMap(gradlePropertiesFile,
			"Version");
		if (!dependenciesGradle.exists())
		{
			throw new RuntimeException("Could not find file dependencies.gradle");
		}
		String dependenciesContent = DependenciesExtensions
			.getDependenciesContent(dependenciesGradle);
		List<String> dependenciesAsStringList = DependenciesExtensions
			.getDependenciesAsStringList(dependenciesContent);

		List<DependencyInfo> dependencyInfos = DependenciesExtensions
			.getDependencyInfos(dependenciesAsStringList, versionMap);
		gradleProjectInfo.setDependencyInfos(dependencyInfos);

		String newDependenciesStructure = DependenciesExtensions
			.getNewDependenciesStructure(dependencyInfos);

		System.out.println(newDependenciesStructure);

		String libsVersionTomlMapAsString = getLibsVersionTomlMapAsString(dependencyInfos);
		StoreFileExtensions.toFile(dependenciesGradle, newDependenciesStructure,
			StandardCharsets.UTF_8);
		gradleProjectInfo.setLibsVersionTomlMapAsString(libsVersionTomlMapAsString);
		return gradleProjectInfo;
	}

	public static GradleProjectInfo newLibsVersionsTomlFile(MigrationInfo migrationInfo)
		throws IOException
	{
		GradleProjectInfo gradleProjectInfo = newLibsVersionsTomlAsString(migrationInfo);
		String libsVersionTomlMapAsString = gradleProjectInfo.getLibsVersionTomlMapAsString();
		// 2. store all version to libs.versions.toml
		File libsVersionsToml = PathFinder.getRelativePath(migrationInfo.getGradleDirectory(),
			DependenciesInfo.LIBS_VERSIONS_TOML_FILENAME);
		gradleProjectInfo.setLibsVersionsTomlFile(libsVersionsToml);
		if (!libsVersionsToml.exists())
		{
			StoreFileExtensions.toFile(libsVersionsToml, libsVersionTomlMapAsString);
		}
		return gradleProjectInfo;
	}

	public static GradleProjectInfo newLibsVersionsTomlFile(String projectDirectoryName)
		throws IOException
	{
		MigrationInfo migrationInfo = MigrationInfo.fromAbsolutePath(projectDirectoryName);
		return newLibsVersionsTomlFile(migrationInfo);
	}

	public static GradleProjectInfo migrateToTomlVersions(File gradleDirectory,
		String targetProjectName, String targetProjectDirNamePrefix) throws IOException
	{
		String versionCatalogUpdateFileName = DependenciesInfo.VERSION_CATALOG_UPDATE_GRADLE_FILENAME;
		File sourceVersionCatalogUpdateFile = PathFinder.getRelativePath(gradleDirectory,
			versionCatalogUpdateFileName);
		String projectDirectoryName = targetProjectDirNamePrefix + targetProjectName;
		GradleProjectInfo gradleProjectInfo = newLibsVersionsTomlFile(projectDirectoryName);
		MigrationInfo migrationInfo = MigrationInfo.fromAbsolutePath(projectDirectoryName);
		gradleProjectInfo.setMigrationInfo(migrationInfo);
		File destinationVersionCatalogUpdateFile = FileFactory
			.newFile(migrationInfo.getGradleDirectory(), versionCatalogUpdateFileName);
		CopyFileExtensions.copyFile(sourceVersionCatalogUpdateFile,
			destinationVersionCatalogUpdateFile, StandardCharsets.UTF_8, StandardCharsets.UTF_8,
			true);
		return gradleProjectInfo;
	}

	public static GradleProjectInfo migrateToNewProjectStructure(
		ProjectTomlStructureInfo projectTomlStructureInfo) throws IOException
	{
		return migrateToNewProjectStructure(projectTomlStructureInfo.getGradleDirectory(),
			projectTomlStructureInfo.getSourceProjectName(),
			projectTomlStructureInfo.getTargetProjectName(),
			projectTomlStructureInfo.getSourceProjectDirNamePrefix(),
			projectTomlStructureInfo.getTargetProjectDirNamePrefix());
	}

	public static GradleProjectInfo migrateToNewProjectStructure(File gradleDirectory,
		String sourceProjectName, String targetProjectName, String sourceProjectDirNamePrefix,
		String targetProjectDirNamePrefix) throws IOException
	{
		GradleProjectInfo gradleProjectInfo = migrateToTomlVersions(gradleDirectory,
			targetProjectName, targetProjectDirNamePrefix);

		GradleRunConfigurationsCopier.copyOnlyRunConfigurations(sourceProjectName,
			targetProjectName, sourceProjectDirNamePrefix, targetProjectDirNamePrefix);
		return gradleProjectInfo;
	}

}
