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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.github.astrapi69.file.read.ReadFileExtensions;
import io.github.astrapi69.file.search.PathFinder;
import io.github.astrapi69.gradle.migration.info.DependenciesInfo;
import io.github.astrapi69.gradle.migration.info.MigrationInfo;
import io.github.astrapi69.gradle.migration.info.ProjectTomlStructureInfo;
import io.github.astrapi69.gradle.migration.runner.GradleRunConfigurationsCopier;


public class MigrateToTomlVersionsTest
{

	public static File getGradleDirectory()
	{
		File projectDirectory = PathFinder.getProjectDirectory();
		return PathFinder.getRelativePath(projectDirectory, "gradle");
	}

	@Test
	@Disabled
	public void testNewLibsVersionsTomlFile() throws IOException
	{
		File gradleDirectory = getGradleDirectory();

		String targetProjectName = "xstream-extensions";
		String targetProjectDirNamePrefix = "/run/media/astrapi69/backups/git/hub/astrapi69/";
		String projectDirectoryName = targetProjectDirNamePrefix + targetProjectName;

		File libsVersionsTomlFile = MigrateToTomlVersions.migrateToTomlVersions(gradleDirectory,
			targetProjectName, targetProjectDirNamePrefix);

		MigrationInfo migrationInfo = MigrationInfo.fromAbsolutePath(projectDirectoryName);
		String libsVersionTomlMapAsString = MigrateToTomlVersions
			.newLibsVersionsTomlAsString(migrationInfo);
		String libsVersionsTomlFileContent = ReadFileExtensions.fromFile(libsVersionsTomlFile);
		assertEquals(libsVersionsTomlFileContent, libsVersionTomlMapAsString);

		String sourceProjectName = DependenciesInfo.JAVA_LIBRARY_TEMPLATE_NAME;
		String sourceProjectDirNamePrefix = "/run/media/astrapi69/backups/git/hub/astrapi69/";
		GradleRunConfigurationsCopier.copyOnlyRunConfigurations(sourceProjectName,
			targetProjectName, sourceProjectDirNamePrefix, targetProjectDirNamePrefix);
	}

	@Test
	@Disabled
	public void testMigrateToNewProjectStructure() throws IOException
	{
		File gradleDirectory;
		String sourceProjectDirNamePrefix;
		String targetProjectDirNamePrefix;
		String sourceProjectName;
		String targetProjectName;
		String sourceGithubUser;
		String targetGithubUser;

		gradleDirectory = getGradleDirectory();

		targetProjectName = "mystic-crypt";
		sourceProjectName = DependenciesInfo.JAVA_LIBRARY_TEMPLATE_NAME;
		sourceGithubUser = "astrapi69";
		targetGithubUser = "astrapi69";
		sourceProjectDirNamePrefix = "/run/media/astrapi69/backups/git/hub/" + sourceGithubUser
			+ "/";
		targetProjectDirNamePrefix = "/run/media/astrapi69/backups/git/hub/" + targetGithubUser
			+ "/";

		ProjectTomlStructureInfo projectTomlStructureInfo = ProjectTomlStructureInfo.builder()
			.gradleDirectory(gradleDirectory).sourceProjectName(sourceProjectName)
			.sourceProjectDirNamePrefix(sourceProjectDirNamePrefix)
			.targetProjectName(targetProjectName)
			.targetProjectDirNamePrefix(targetProjectDirNamePrefix).build();

		MigrateToTomlVersions.migrateToNewProjectStructure(projectTomlStructureInfo);
	}

	@Test
	@Disabled
	public void testCopyRunConfigurationsOfVersionCatalogUpdate()
	{
		String sourceProjectName = DependenciesInfo.JAVA_LIBRARY_TEMPLATE_NAME;
		String targetProjectName = "json-extensions";
		String sourceProjectDirNamePrefix = "/run/media/astrapi69/backups/git/hub/astrapi69/";
		String targetProjectDirNamePrefix = "/run/media/astrapi69/backups/git/hub/astrapi69/";
		GradleRunConfigurationsCopier.copyOnlyRunConfigurations(sourceProjectName,
			targetProjectName, sourceProjectDirNamePrefix, targetProjectDirNamePrefix);
	}
}
