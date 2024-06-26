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

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import io.github.astrapi69.collection.array.ArrayExtensions;
import io.github.astrapi69.collection.list.ListExtensions;
import io.github.astrapi69.collection.list.ListFactory;
import io.github.astrapi69.collection.properties.PropertiesExtensions;
import io.github.astrapi69.file.copy.CopyFileExtensions;
import io.github.astrapi69.file.create.FileFactory;
import io.github.astrapi69.file.exception.FileDoesNotExistException;
import io.github.astrapi69.file.exception.FileIsADirectoryException;
import io.github.astrapi69.file.modify.ModifyFileExtensions;
import io.github.astrapi69.file.read.ReadFileExtensions;
import io.github.astrapi69.file.rename.RenameFileExtensions;
import io.github.astrapi69.file.search.FileSearchExtensions;
import io.github.astrapi69.file.write.StoreFileExtensions;
import io.github.astrapi69.gradle.migration.extension.DependenciesExtensions;
import io.github.astrapi69.gradle.migration.info.CopyGradleRunConfigurations;
import io.github.astrapi69.gradle.migration.info.DependenciesInfo;
import io.github.astrapi69.io.StreamExtensions;
import io.github.astrapi69.string.CaseExtensions;
import io.github.astrapi69.string.StringExtensions;

public class GradleRunConfigurationsCopier
{
	private final CopyGradleRunConfigurations copyGradleRunConfigurations;
	private final Logger log = Logger.getLogger(GradleRunConfigurationsCopier.class.getName());

	public static void copyOnlyRunConfigurations(String sourceProjectName, String targetProjectName,
		String sourceProjectDirNamePrefix, String targetProjectDirNamePrefix)
	{
		copyRunConfigurations(sourceProjectName, targetProjectName, sourceProjectDirNamePrefix,
			targetProjectDirNamePrefix, true, false);
	}

	public static void copyRunConfigurations(String sourceProjectName, String targetProjectName,
		String sourceProjectDirNamePrefix, String targetProjectDirNamePrefix,
		boolean onlyRunConfigurations, boolean runConfigurationsInSameFolder)
	{
		CopyGradleRunConfigurations copyGradleRunConfigurationsData = GradleRunConfigurationsCopier
			.newCopyGradleRunConfigurations(sourceProjectName, targetProjectName,
				sourceProjectDirNamePrefix, targetProjectDirNamePrefix, onlyRunConfigurations,
				runConfigurationsInSameFolder);
		GradleRunConfigurationsCopier.of(copyGradleRunConfigurationsData).copy();
	}

	private GradleRunConfigurationsCopier(CopyGradleRunConfigurations copyGradleRunConfigurations)
	{
		Objects.requireNonNull(copyGradleRunConfigurations);
		this.copyGradleRunConfigurations = copyGradleRunConfigurations;
	}

	public static CopyGradleRunConfigurations newCopyGradleRunConfigurations(
		String sourceProjectName, String targetProjectName, String sourceProjectDirNamePrefix,
		String targetProjectDirNamePrefix, boolean onlyRunConfigurations,
		boolean runConfigurationsInSameFolder)
	{
		File sourceProjectDir;
		File targetProjectDir;
		File ideaSourceDir;
		File ideaTargetDir;
		File sourceRunConfigDir;
		File targetRunConfigDir;
		String sourceFilenamePrefix;
		String targetFilenamePrefix;
		String sourceProjectDirName;
		String targetProjectDirName;

		sourceFilenamePrefix = StringUtils.replace(sourceProjectName, "-", "_");
		targetFilenamePrefix = StringUtils.replace(targetProjectName, "-", "_");
		sourceProjectDirName = sourceProjectDirNamePrefix + sourceProjectName;
		targetProjectDirName = targetProjectDirNamePrefix + targetProjectName;
		sourceProjectDir = new File(sourceProjectDirName);
		targetProjectDir = new File(targetProjectDirName);
		ideaSourceDir = new File(sourceProjectDir, CopyGradleRunConfigurations.IDEA_DIR_NAME);
		ideaTargetDir = new File(targetProjectDir, CopyGradleRunConfigurations.IDEA_DIR_NAME);
		sourceRunConfigDir = new File(ideaSourceDir,
			CopyGradleRunConfigurations.RUN_CONFIGURATIONS_DIR_NAME);
		targetRunConfigDir = new File(ideaTargetDir,
			CopyGradleRunConfigurations.RUN_CONFIGURATIONS_DIR_NAME);

		return CopyGradleRunConfigurations.builder().onlyRunConfigurations(onlyRunConfigurations)
			.sourceProjectDir(sourceProjectDir).targetProjectDir(targetProjectDir)
			.ideaSourceDir(ideaSourceDir).ideaTargetDir(ideaTargetDir)
			.sourceRunConfigDir(sourceRunConfigDir).targetRunConfigDir(targetRunConfigDir)
			.sourceFilenamePrefix(sourceFilenamePrefix).targetFilenamePrefix(targetFilenamePrefix)
			.sourceProjectName(sourceProjectName).targetProjectName(targetProjectName)
			.runConfigurationsInSameFolder(runConfigurationsInSameFolder).build();
	}

	public static GradleRunConfigurationsCopier of(
		CopyGradleRunConfigurations copyGradleRunConfigurations)
	{
		return new GradleRunConfigurationsCopier(copyGradleRunConfigurations);
	}

	public void copy()
	{
		try
		{
			copy(this.copyGradleRunConfigurations);
		}
		catch (IOException | FileIsADirectoryException | FileDoesNotExistException e)
		{
			throw new RuntimeException(e);
		}
	}

	private void copy(CopyGradleRunConfigurations copyGradleRunConfigurationsData)
		throws IOException, FileIsADirectoryException, FileDoesNotExistException
	{
		copyRunConfigurations(copyGradleRunConfigurationsData);

		if (!copyGradleRunConfigurationsData.isOnlyRunConfigurations())
		{
			externalizeVersionFromBuildGradle(
				copyGradleRunConfigurationsData.getTargetProjectDir());
		}
	}

	private void copyRunConfigurations(CopyGradleRunConfigurations copyGradleRunConfigurationsData)
		throws IOException, FileIsADirectoryException, FileDoesNotExistException
	{
		List<File> allFiles;
		if (copyGradleRunConfigurationsData.isRunConfigurationsInSameFolder())
		{
			// find all run configurations files
			allFiles = FileSearchExtensions.findAllFiles(
				copyGradleRunConfigurationsData.getTargetRunConfigDir(),
				copyGradleRunConfigurationsData.getSourceFilenamePrefix() + ".*");
		}
		else
		{
			// find all run configurations files for copy
			allFiles = FileSearchExtensions.findAllFiles(
				copyGradleRunConfigurationsData.getSourceRunConfigDir(),
				copyGradleRunConfigurationsData.getSourceFilenamePrefix() + ".*");
			// copy found run configurations files to the target directory
			CopyFileExtensions.copyFiles(allFiles,
				copyGradleRunConfigurationsData.getTargetRunConfigDir(), StandardCharsets.UTF_8,
				StandardCharsets.UTF_8, true);
			// find all run configurations files for rename
			allFiles = FileSearchExtensions.findAllFiles(
				copyGradleRunConfigurationsData.getTargetRunConfigDir(),
				copyGradleRunConfigurationsData.getSourceFilenamePrefix() + ".*");
		}
		// rename all run configurations files
		for (File file : allFiles)
		{
			String name = file.getName();
			String newName;
			newName = name.replace(copyGradleRunConfigurationsData.getSourceFilenamePrefix(),
				copyGradleRunConfigurationsData.getTargetFilenamePrefix());
			RenameFileExtensions.renameFile(file, newName);
		}
		// Find all renamed run configurations files
		allFiles = FileSearchExtensions.findAllFiles(
			copyGradleRunConfigurationsData.getTargetRunConfigDir(),
			copyGradleRunConfigurationsData.getTargetFilenamePrefix() + ".*");
		// replace content of all run configurations files so they can run appropriate to the new
		// project
		for (File file : allFiles)
		{
			Path inFilePath = file.toPath();
			ModifyFileExtensions.modifyFile(inFilePath, (count, input) -> {
				return input.replaceAll(copyGradleRunConfigurationsData.getSourceProjectName(),
					copyGradleRunConfigurationsData.getTargetProjectName())
					+ System.lineSeparator();
			});
		}
	}

	private void externalizeVersionFromBuildGradle(File targetProjectDir) throws IOException
	{
		File buildGradle = new File(targetProjectDir, DependenciesInfo.BUILD_GRADLE_FILENAME);
		File gradleProperties = new File(targetProjectDir,
			DependenciesInfo.GRADLE_PROPERTIES_FILENAME);
		FileFactory.newFile(gradleProperties);
		String dependenciesContent = DependenciesExtensions.getDependenciesContent(buildGradle);
		List<String> stringList = getDependenciesAsStringList(dependenciesContent);
		DependenciesInfo dependenciesInfo = getGradlePropertiesWithVersions(stringList);
		String newDependenciesContent = getNewDependenciesContent(dependenciesInfo);
		String replaceDependenciesContent = replaceDependenciesContent(buildGradle,
			newDependenciesContent, dependenciesInfo.getProperties());
		PropertiesExtensions.export(dependenciesInfo.getProperties(),
			StreamExtensions.getOutputStream(gradleProperties));
		StoreFileExtensions.toFile(buildGradle, dependenciesContent);
	}

	private List<String> getDependenciesAsStringList(String dependenciesContent)
	{
		String[] lines = dependenciesContent.split("\n");
		List<String> stringList = ArrayExtensions.asList(lines);
		ListExtensions.removeFirst(stringList);
		ListExtensions.removeLast(stringList);
		return stringList;
	}

	public static String getContentOf(String section, File buildGradle) throws IOException
	{
		String buildGradleContent = ReadFileExtensions.fromFile(buildGradle);
		return getContentOf(section, buildGradleContent);
	}

	public static String getContentOf(String section, String buildGradleContent)
	{
		int indexOfStart = buildGradleContent.indexOf(section + " {");
		if (indexOfStart == -1)
		{
			throw new IllegalArgumentException("Section not found: " + section);
		}
		int indexOfEnd = findClosingBrace(buildGradleContent, indexOfStart + section.length() + 2);
		return buildGradleContent.substring(indexOfStart, indexOfEnd + 1);
	}

	private static int findClosingBrace(String content, int start)
	{
		int braceCount = 1;
		for (int i = start; i < content.length(); i++)
		{
			if (content.charAt(i) == '{')
			{
				braceCount++;
			}
			else if (content.charAt(i) == '}')
			{
				braceCount--;
				if (braceCount == 0)
				{
					return i;
				}
			}
		}
		throw new IllegalArgumentException("No matching closing brace found.");
	}

	public String getBuildscriptContent(File buildGradle) throws IOException
	{
		return GradleRunConfigurationsCopier.getContentOf("buildscript", buildGradle);
	}

	private DependenciesInfo getGradlePropertiesWithVersions(List<String> stringList)
	{
		List<String> versionStrings = ListFactory.newArrayList();
		Properties properties = new Properties();
		stringList.forEach(entry -> {
			String dependency = StringUtils.substringBetween(entry, "'");
			if (dependency != null)
			{
				String[] strings = dependency.split(":");
				String group = strings[0];
				String artifact = strings[1];
				String version = strings[2];
				String[] split = artifact.split("-");
				StringBuilder sb = new StringBuilder();
				if (1 < split.length)
				{
					for (int i = 0; i < split.length; i++)
					{
						if (i == 0)
						{
							sb.append(split[i]);
							continue;
						}
						String artifactPart = split[i];
						String artifactPartFirstCharacterToUpperCase = StringExtensions
							.firstCharacterToUpperCase(artifactPart);
						sb.append(artifactPartFirstCharacterToUpperCase);
					}
				}
				else
				{
					sb.append(split[0]);
				}
				String propertiesKey = sb.toString().trim() + "Version";
				properties.setProperty(propertiesKey, version);
				String newDependency = group + ":" + artifact + ":$" + propertiesKey;
				String newEntry = StringUtils.replace(entry, dependency, newDependency);
				versionStrings.add(newEntry);
			}
			else
			{
				log.info("dependency for entry" + entry + " is null");
			}
		});

		return DependenciesInfo.builder().properties(properties).versionStrings(versionStrings)
			.build();
	}

	private String getNewDependenciesContent(DependenciesInfo dependenciesInfo)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("dependencies {").append("\n");
		dependenciesInfo.getVersionStrings().forEach(entry -> sb.append(entry).append("\n"));
		sb.append("}");
		return sb.toString();
	}

	private String getVersion(String buildGradleContent, Properties gradleProperties)
	{
		String projectVersionKey = "projectVersion";
		String versionPrefix = "version = ";
		int versionPrefixLength = versionPrefix.length();
		int indexOfVersionStart = buildGradleContent.indexOf(versionPrefix);
		String versionQuotationMark = buildGradleContent.substring(
			indexOfVersionStart + versionPrefixLength,
			indexOfVersionStart + versionPrefixLength + 1);
		String substring = buildGradleContent
			.substring(indexOfVersionStart + versionPrefixLength + 1);
		int ie = substring.indexOf(versionQuotationMark);
		int indexOfVersionEnd = ie + indexOfVersionStart + versionPrefixLength + 2;
		String versionLine = buildGradleContent.substring(indexOfVersionStart, indexOfVersionEnd);
		String versionValue = StringUtils.substringsBetween(versionLine, versionQuotationMark,
			versionQuotationMark)[0];
		gradleProperties.setProperty(projectVersionKey, versionValue);
		String newVersionLine = StringUtils.replace(versionLine, versionValue,
			"$" + projectVersionKey);
		return StringUtils.replace(buildGradleContent, versionLine, newVersionLine);
	}

	public String replaceDependenciesContent(File buildGradle, String newDependenciesContent,
		Properties gradleProperties) throws IOException
	{
		String buildGradleContent = ReadFileExtensions.fromFile(buildGradle);
		int indexOfStart = buildGradleContent.indexOf("dependencies {");
		int indexOfEnd = buildGradleContent.substring(indexOfStart).indexOf("}") + indexOfStart + 1;
		String dependencies = buildGradleContent.substring(indexOfStart, indexOfEnd);
		String replacedBuildGradleContent = StringUtils.replace(buildGradleContent, dependencies,
			newDependenciesContent);
		replacedBuildGradleContent = getVersion(replacedBuildGradleContent, gradleProperties);
		return StringUtils.replace(replacedBuildGradleContent, "'", "\"");
	}

}
