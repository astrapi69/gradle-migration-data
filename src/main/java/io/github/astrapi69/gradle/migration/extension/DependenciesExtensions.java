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

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import io.github.astrapi69.gradle.migration.data.DependencyInfo;
import io.github.astrapi69.gradle.migration.runner.GradleRunConfigurationsCopier;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

import io.github.astrapi69.collection.array.ArrayExtensions;
import io.github.astrapi69.collection.list.ListFactory;
import io.github.astrapi69.collection.map.MapFactory;
import io.github.astrapi69.collection.properties.PropertiesExtensions;
import io.github.astrapi69.file.read.ReadFileExtensions;
import io.github.astrapi69.string.StringExtensions;
import lombok.NonNull;

public class DependenciesExtensions
{

	public static String getDependenciesContent(File buildGradle) throws IOException
	{
		String buildGradleContent = ReadFileExtensions.fromFile(buildGradle);
		int indexOfStart = buildGradleContent.indexOf("dependencies {");
		int indexOfEnd = buildGradleContent.substring(indexOfStart).indexOf("}") + indexOfStart + 1;
		return GradleRunConfigurationsCopier.getContentOf("dependencies", buildGradle);
	}

	public static String getProjectVersionKeyName(String projectName)
	{
		String camelCased = toCamelCase(projectName);
		String projectVersionKeyName = StringExtensions.firstCharacterToLowerCase(camelCased);
		return projectVersionKeyName + "Version";
	}

	public static String toCamelCase(String projectName)
	{
		return WordUtils.capitalizeFully(projectName, new char[] { '-' }).replaceAll("-", "");
	}

	public static List<String> getDependenciesAsStringList(String dependenciesContent)
	{
		String[] lines = dependenciesContent.split("\n");
		String[] copyOfRange = Arrays.copyOfRange(lines, 1, lines.length - 1);
		return ArrayExtensions.asList(copyOfRange);
	}

	public static List<DependencyInfo> getDependencyInfos(List<String> dependencyRows,
														  Map<String, String> versionMap)
	{
		List<DependencyInfo> dependencyInfos = ListFactory.newArrayList();
		dependencyRows.stream().forEach(row -> {
			DependencyInfo dependencyInfo = DependenciesExtensions.getDependencyInfo(row);
			String versionAlias = dependencyInfo.getVersion();
			if (versionAlias != null)
			{
				String stringVersion = versionAlias.substring(1);
				String actualVersion = versionMap.get(stringVersion);
				dependencyInfo.setVersion(actualVersion);
			}
			dependencyInfos.add(dependencyInfo);
		});
		return dependencyInfos;
	}

	public static DependencyInfo getDependencyInfo(String dependencyRow)
	{
		String stripped = dependencyRow.strip();
		String scope = stripped.substring(0, stripped.indexOf("("));
		String dependency = StringUtils.substringBetween(stripped, "\"");
		String[] split = dependency.split(":");
		if (split.length == 2)
		{
			return DependencyInfo.builder().scope(scope).groupId(split[0]).artifactId(split[1])
				.build();
		}
		if (split.length == 3)
		{
			return DependencyInfo.builder().scope(scope).groupId(split[0]).artifactId(split[1])
				.version(split[2]).build();
		}
		return DependencyInfo.builder().scope(scope).build();
	}


	/**
	 * Reads all version properties in the gradle.properties file and saves to a java map that will
	 * be returned
	 * 
	 * @param gradlePropertiesFile
	 *            the gradle.properties file
	 * @param keyVersionSuffix
	 *            the version suffix
	 * @return a java map with the all versions as key the version string and the actual version as
	 *         value. For instance 'key:lombokVersion, value:1.18.32'
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	public static Map<String, String> getVersionMap(final @NonNull File gradlePropertiesFile,
		String keyVersionSuffix) throws IOException
	{
		Properties properties = PropertiesExtensions.loadProperties(gradlePropertiesFile);
		Map<String, String> versionMap = MapFactory.newLinkedHashMap();
		properties.keySet().stream().forEach(e -> {
			String versionKey = e.toString();
			if (versionKey.endsWith(keyVersionSuffix))
			{
				versionMap.put(versionKey, properties.getProperty(versionKey));
			}
		});
		return versionMap;
	}

	public static Map<String, Map> getLibsVersionTomlMap(List<DependencyInfo> dependencyInfos)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[versions]").append(System.lineSeparator());
		Map<String, Map> libsVersionsTomlMap = new LinkedHashMap<>();
		Map<String, String> versionsMap = new LinkedHashMap<>();
		libsVersionsTomlMap.put("versions", versionsMap);
		dependencyInfos.stream().forEach(dependencyInfo -> {
			String dependencyInfoVersionKey = dependencyInfo.getArtifactId() + "-version";
			sb.append(dependencyInfoVersionKey).append(" = ").append("\"")
				.append(dependencyInfo.getVersion()).append("\"").append(System.lineSeparator());
			versionsMap.put(dependencyInfoVersionKey, dependencyInfo.getVersion());
		});
		Map<String, Map<String, Map<String, String>>> librariesMap = new LinkedHashMap<>();

		Map<String, Map<String, String>> librariesValuesMap = new LinkedHashMap<>();
		libsVersionsTomlMap.put("libraries", librariesValuesMap);

		sb.append("[libraries]").append(System.lineSeparator());
		dependencyInfos.stream().forEach(dependencyInfo -> {
			String dependencyInfoVersionKey = dependencyInfo.getArtifactId() + "-version";
			String libraryKey = dependencyInfo.getArtifactId();
			sb.append(libraryKey).append(" = { module = \"").append(dependencyInfo.getGroupId())
				.append(":").append(dependencyInfo.getGroupId()).append("\", version.ref = \"")
				.append(dependencyInfoVersionKey).append("\"}").append(System.lineSeparator());
			Map<String, String> libraryValueMap = new LinkedHashMap<>();
			libraryValueMap.put("module",
				dependencyInfo.getGroupId() + ":" + dependencyInfo.getGroupId());
			libraryValueMap.put("version", libraryKey + "-version");
			librariesValuesMap.put(libraryKey, libraryValueMap);
		});
		return libsVersionsTomlMap;
	}

	public static String getNewDependenciesStructure(List<DependencyInfo> dependencyInfos)
	{
		StringBuilder sb = new StringBuilder();
		List<String> versionKeys = ListFactory.newArrayList();
		sb.append("dependencies {").append(System.lineSeparator());
		dependencyInfos.stream().forEach(dependencyInfo -> {
			String dependencyInfoVersionKey = dependencyInfo.getArtifactId() + "-version";
			if (!versionKeys.contains(dependencyInfoVersionKey))
			{
				versionKeys.add(dependencyInfoVersionKey);
				String scope = dependencyInfo.getScope();
				String artifactId = dependencyInfo.getArtifactId().replaceAll("-", ".");
				sb.append(scope).append(" libs.").append(artifactId).append(System.lineSeparator());
			}
		});
		sb.append("}").append(System.lineSeparator());
		return sb.toString();
	}


	public static String getLibsVersionTomlMapAsString(List<DependencyInfo> dependencyInfos)
	{
		StringBuilder sb = new StringBuilder();
		List<String> versionKeys = ListFactory.newArrayList();
		sb.append("[versions]").append(System.lineSeparator());
		dependencyInfos.stream().forEach(dependencyInfo -> {

			if (dependencyInfo.getVersion() != null)
			{
				String dependencyInfoVersionKey = dependencyInfo.getArtifactId() + "-version";
				if (!versionKeys.contains(dependencyInfoVersionKey))
				{
					versionKeys.add(dependencyInfoVersionKey);
					sb.append(dependencyInfoVersionKey).append(" = ").append("\"")
						.append(dependencyInfo.getVersion()).append("\"")
						.append(System.lineSeparator());
				}
			}
		});

		sb.append(System.lineSeparator());
		List<String> libraryKeys = ListFactory.newArrayList();
		sb.append("[libraries]").append(System.lineSeparator());
		dependencyInfos.stream().forEach(dependencyInfo -> {
			String libraryKey = dependencyInfo.getArtifactId();

			if (!libraryKeys.contains(libraryKey))
			{
				String dependencyInfoVersionKey = dependencyInfo.getArtifactId() + "-version";
				sb.append(libraryKey).append(" = { module = \"").append(dependencyInfo.getGroupId())
					.append(":").append(dependencyInfo.getArtifactId());
				if (dependencyInfo.getVersion() != null)
				{
					sb.append("\", version.ref = \"").append(dependencyInfoVersionKey).append("\"}")
						.append(System.lineSeparator());
				}
				else
				{
					sb.append("\"}").append(System.lineSeparator());
				}
				libraryKeys.add(libraryKey);
			}
		});
		return sb.toString();
	}
}
