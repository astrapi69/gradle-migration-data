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
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.github.astrapi69.collection.list.ListFactory;
import io.github.astrapi69.file.copy.CopyFileExtensions;
import io.github.astrapi69.file.create.DirectoryFactory;
import io.github.astrapi69.file.create.FileFactory;
import io.github.astrapi69.file.read.ReadFileExtensions;
import io.github.astrapi69.file.search.PathFinder;
import io.github.astrapi69.gradle.migration.extension.CaseExtensions;
import io.github.astrapi69.gradle.migration.info.DependenciesInfo;
import io.github.astrapi69.gradle.migration.info.DependencyInfo;
import io.github.astrapi69.gradle.migration.info.GradleProjectInfo;
import io.github.astrapi69.gradle.migration.info.MigrationInfo;
import io.github.astrapi69.gradle.migration.info.ProjectTomlStructureInfo;
import io.github.astrapi69.gradle.migration.runner.GradleRunConfigurationsCopier;
import io.github.astrapi69.io.shell.LinuxShellExecutor;


public class MigrateToTomlVersionsTest
{

	public static File getGradleDirectory()
	{
		File projectDirectory = PathFinder.getProjectDirectory();
		return PathFinder.getRelativePath(projectDirectory,
			DependenciesInfo.PROJECT_GRADLE_DIRECTORY_NAME);
	}

	@Test
	@Disabled
	public void testNewLibsVersionsTomlFile() throws IOException
	{
		File gradleDirectory = getGradleDirectory();

		String targetProjectName = "xstream-extensions";
		String targetProjectDirNamePrefix = "/run/media/astrapi69/backups/git/hub/astrapi69/";
		String projectDirectoryName = targetProjectDirNamePrefix + targetProjectName;

		GradleProjectInfo libsVersionsTomlFile = MigrateToTomlVersions
			.migrateToTomlVersions(gradleDirectory, targetProjectName, targetProjectDirNamePrefix);

		MigrationInfo migrationInfo = MigrationInfo.fromAbsolutePath(projectDirectoryName);
		GradleProjectInfo libsVersionTomlMapAsString = MigrateToTomlVersions
			.newLibsVersionsTomlAsString(migrationInfo);
		String libsVersionsTomlFileContent = ReadFileExtensions
			.fromFile(libsVersionsTomlFile.getLibsVersionsTomlFile());
		assertEquals(libsVersionsTomlFileContent, libsVersionTomlMapAsString);

		String sourceProjectName = DependenciesInfo.JAVA_LIBRARY_TEMPLATE_NAME;
		String sourceProjectDirNamePrefix = "/run/media/astrapi69/backups/git/hub/astrapi69/";
		GradleRunConfigurationsCopier.copyOnlyRunConfigurations(sourceProjectName,
			targetProjectName, sourceProjectDirNamePrefix, targetProjectDirNamePrefix);
	}

	private void addFileToGit(Git git, String filePattern) throws GitAPIException
	{
		System.out.println("Adding file: " + filePattern);
		git.add().addFilepattern(filePattern).call();
	}

	public void addGitFilesWithJGit(String targetProjectDirNamePrefix, String targetProjectName)
		throws IOException, GitAPIException
	{

		// Add files to git...
		File targetProjectDir = DirectoryFactory
			.newDirectory(targetProjectDirNamePrefix + targetProjectName);

		if (!targetProjectDir.exists())
		{
			throw new IOException(
				"Target project directory does not exist: " + targetProjectDir.getAbsolutePath());
		}

		File dotGitDirectory = DirectoryFactory.newDirectory(targetProjectDir, ".git");
		if (!dotGitDirectory.exists())
		{
			throw new IOException(
				".git directory does not exist in: " + dotGitDirectory.getAbsolutePath());
		}
		Git git = Git.open(dotGitDirectory);

		File tomlFile = FileFactory.newFile(PathFinder.getRelativePath(targetProjectDir, "gradle"),
			"libs.versions.toml");
		boolean exists = tomlFile.exists();
		File gradleFile = FileFactory.newFile(
			PathFinder.getRelativePath(targetProjectDir, "gradle"),
			"version-catalog-update.gradle");
		boolean exists1 = gradleFile.exists();
		File versionCatalogFormatFile = FileFactory.newFile(
			PathFinder.getRelativePath(targetProjectDir, ".idea", "runConfigurations"),
			CaseExtensions.kebabToSnakeCase(targetProjectName) + "__versionCatalogFormat_.xml");
		boolean exists2 = versionCatalogFormatFile.exists();
		File versionCatalogUpdateFile = FileFactory.newFile(
			PathFinder.getRelativePath(targetProjectDir, ".idea", "runConfigurations"),
			CaseExtensions.kebabToSnakeCase(targetProjectName) + "__versionCatalogUpdate_.xml");
		boolean exists3 = versionCatalogUpdateFile.exists();

		git.add().addFilepattern("gradle/libs.versions.toml").call();
		git.add().addFilepattern("gradle/version-catalog-update.gradle").call();
		git.add().addFilepattern(".idea/runConfigurations/"
			+ CaseExtensions.kebabToSnakeCase(targetProjectName) + "__versionCatalogFormat_.xml")
			.call();
		git.add().addFilepattern(".idea/runConfigurations/"
			+ CaseExtensions.kebabToSnakeCase(targetProjectName) + "__versionCatalogUpdate_.xml")
			.call();
	}


	public void addGitFilesWithJGit2(String targetProjectDirNamePrefix, String targetProjectName)
		throws IOException, GitAPIException
	{
		// Add files to git...
		File targetProjectDir = new File(targetProjectDirNamePrefix + targetProjectName);

		if (!targetProjectDir.exists())
		{
			throw new IOException(
				"Target project directory does not exist: " + targetProjectDir.getAbsolutePath());
		}

		File dotGitDirectory = new File(targetProjectDir, ".git");
		if (!dotGitDirectory.exists())
		{
			throw new IOException(
				".git directory does not exist in: " + dotGitDirectory.getAbsolutePath());
		}

		try (
			Repository repository = new FileRepositoryBuilder().setGitDir(dotGitDirectory)
				.readEnvironment().findGitDir().build();
			Git git = new Git(repository))
		{

			addFileToGit(git, "gradle/libs.versions.toml");
			addFileToGit(git, "gradle/version-catalog-update.gradle");
			addFileToGit(git,
				".idea/runConfigurations/" + CaseExtensions.kebabToSnakeCase(targetProjectName)
					+ "__versionCatalogFormat_.xml");
			addFileToGit(git,
				".idea/runConfigurations/" + CaseExtensions.kebabToSnakeCase(targetProjectName)
					+ "__versionCatalogUpdate_.xml");

			System.out.println("Files added successfully.");
		}
		catch (RepositoryNotFoundException e)
		{
			throw new IOException("Could not open Git repository: " + e.getMessage(), e);
		}
	}

	@Test
	@Disabled
	public void testMigrateToNewProjectStructure()
		throws IOException, InterruptedException, GitAPIException
	{
		File gradleDirectory;
		String sourceProjectDirNamePrefix;
		String targetProjectDirNamePrefix;
		String sourceProjectName;
		String targetProjectName;
		String sourceGithubUser;
		String targetGithubUser;

		gradleDirectory = getGradleDirectory();

		targetProjectName = "zip-worker";
		sourceProjectName = DependenciesInfo.JAVA_LIBRARY_TEMPLATE_NAME;
		sourceGithubUser = "astrapi69";
		// targetGithubUser = "lightblueseas";
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

		GradleProjectInfo gradleProjectInfo = MigrateToTomlVersions
			.migrateToNewProjectStructure(projectTomlStructureInfo);

		Thread.sleep(5000);

		File targetProjectDir = DirectoryFactory
			.newDirectory(targetProjectDirNamePrefix + targetProjectName);

		// Replace build.gradle content with new one...

		updateBuildGradleFile(targetProjectDir, gradleProjectInfo);

		updateGradleYamlFile(targetProjectDir, gradleProjectInfo);

		addGitFiles(targetProjectDir, targetProjectName);

		addGitFilesWithJGit2(targetProjectDirNamePrefix, targetProjectName);
	}

	private static void updateBuildGradleFile(File targetProjectDir,
		GradleProjectInfo gradleProjectInfo) throws IOException
	{
		File srcTestResourcesTest = PathFinder.getRelativePath(PathFinder.getSrcTestResourcesDir(),
			"test");

		AtomicBoolean withLombok = new AtomicBoolean(false);

		File targetBuildGradle = PathFinder.getRelativePath(targetProjectDir, "build.gradle");

		File buildWithLombokGradle = new File(srcTestResourcesTest, "/build-with-lombok.gradle");

		File buildWithoutLombokGradle = new File(srcTestResourcesTest,
			"/build-without-lombok.gradle");

		List<DependencyInfo> dependencyInfos = gradleProjectInfo.getDependencyInfos();
		dependencyInfos.stream().forEach(dependencyInfo -> {
			if (dependencyInfo.getGroupId() != null
				&& dependencyInfo.getGroupId().equals("org.projectlombok"))
			{
				withLombok.set(true);
			}
		});
		if (withLombok.get())
		{
			CopyFileExtensions.copyFile(buildWithLombokGradle, targetBuildGradle);
		}
		else
		{
			CopyFileExtensions.copyFile(buildWithoutLombokGradle, targetBuildGradle);
		}
	}

	private static void updateGradleYamlFile(File targetProjectDir,
		GradleProjectInfo gradleProjectInfo) throws IOException
	{
		File targetGradleYamlFile = PathFinder.getRelativePath(targetProjectDir, ".github",
			"workflows", "gradle.yml");
		File sourceGradleYamlFile = PathFinder.getRelativePath(PathFinder.getProjectDirectory(),
			".github", "workflows", "gradle.yml");

		CopyFileExtensions.copyFile(sourceGradleYamlFile, targetGradleYamlFile);
	}

	private static void addGitFiles(File targetProjectDir, String targetProjectName)
		throws IOException, InterruptedException
	{
		String command;
		String shellPath;
		String executionPath;

		// Ensure you are executing in the root of the git repository
		shellPath = "/usr/bin/zsh";
		executionPath = targetProjectDir.getAbsolutePath();

		// Log the current directory
		System.out.println("Executing commands in: " + executionPath);

		// Adding files in batch
		List<String> filesToAdd = ListFactory.newArrayList();
		filesToAdd.add(PathFinder.getRelativePath(targetProjectDir, "gradle", "libs.versions.toml")
			.getAbsolutePath());
		filesToAdd.add(
			PathFinder.getRelativePath(targetProjectDir, "gradle", "version-catalog-update.gradle")
				.getAbsolutePath());
		filesToAdd.add(PathFinder
			.getRelativePath(targetProjectDir, ".idea", "runConfigurations",
				CaseExtensions.kebabToSnakeCase(targetProjectName) + "__versionCatalogFormat_.xml")
			.getAbsolutePath());
		filesToAdd.add(PathFinder
			.getRelativePath(targetProjectDir, ".idea", "runConfigurations",
				CaseExtensions.kebabToSnakeCase(targetProjectName) + "__versionCatalogUpdate_.xml")
			.getAbsolutePath());
		String output;
		// Add files to git
		for (String file : filesToAdd)
		{
			command = "git add " + file;
			System.out.println("Executing command: " + command);
			output = LinuxShellExecutor.execute(shellPath, executionPath, command);
			System.out.println("output of command: " + output);
		}

		// Check git status
		command = "git status";
		System.out.println("Executing command: " + command);
		output = LinuxShellExecutor.execute(shellPath, executionPath, command);
		System.out.println("output of command: " + output);

		command = "git -c credential.helper= -c core.quotepath=false -c log.showSignature=false add --ignore-errors -A"
			+ " -f -- gradle/libs.versions.toml gradle/version-catalog-update.gradle";

		System.out.println("Executing command: " + command);
		output = LinuxShellExecutor.execute(shellPath, executionPath, command);
		System.out.println("output of command: " + output);

		command = "git -c credential.helper= -c core.quotepath=false -c log.showSignature=false add --ignore-errors -A"
			+ " -f -- .idea/runConfigurations/" + CaseExtensions.kebabToSnakeCase(targetProjectName)
			+ "__versionCatalogFormat_.xml .idea/runConfigurations/"
			+ CaseExtensions.kebabToSnakeCase(targetProjectName) + "__versionCatalogUpdate_.xml";

		System.out.println("Executing command: " + command);
		output = LinuxShellExecutor.execute(shellPath, executionPath, command);
		System.out.println("output of command: " + output);

		// Optional: Commit changes
		// command = "git commit -m 'Added required files'";
		// System.out.println("Executing command: " + command);
		// output = LinuxShellExecutor.execute(shellPath, executionPath, command);
		// System.out.println("output of command: " + output);
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
