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
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.StringUtils;

import io.github.astrapi69.collection.array.ArrayExtensions;
import io.github.astrapi69.collection.list.ListFactory;
import io.github.astrapi69.collection.map.MapFactory;
import io.github.astrapi69.collection.properties.PropertiesExtensions;
import io.github.astrapi69.gradle.migration.info.DependencyInfo;
import io.github.astrapi69.gradle.migration.runner.GradleRunConfigurationsCopier;
import io.github.astrapi69.string.CaseExtensions;
import io.github.astrapi69.string.StringExtensions;
import lombok.NonNull;

public class DependenciesExtensions
{

	public static String getDependenciesContent(File buildGradle) throws IOException
	{
		return GradleRunConfigurationsCopier.getContentOf("dependencies", buildGradle);
	}

	/**
	 * Transforms the given project name(that is in a name convention of kebab-case) in camel-case
	 * and appends 'Version' to it and returns the result
	 * 
	 * @param kebabCaseProjectName
	 *            the project name that is in a name convention of kebab-case
	 * @return The project name with appended 'Version' string
	 */
	public static String getProjectVersionKeyName(String kebabCaseProjectName)
	{
		String camelCased = CaseExtensions.kebabToCamelCase(kebabCaseProjectName);
		String projectVersionKeyName = StringExtensions.firstCharacterToLowerCase(camelCased);
		return projectVersionKeyName + "Version";
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
		dependencyRows.forEach(row -> {
			DependencyInfo dependencyInfo = DependenciesExtensions.getDependencyInfo(row);
			if (dependencyInfo != null)
			{
				String versionAlias = dependencyInfo.getVersion();
				if (versionAlias != null)
				{
					String stringVersion = versionAlias.substring(1);
					String actualVersion = versionMap.get(stringVersion);
					dependencyInfo.setVersion(actualVersion);
				}
				dependencyInfos.add(dependencyInfo);
			}
		});
		return dependencyInfos;
	}

	public static DependencyInfo getDependencyInfo(String dependencyRow)
	{
		String stripped = dependencyRow.strip();
		int indexOf = stripped.indexOf("(");
		if (indexOf != -1)
		{
			String scope = stripped.substring(0, indexOf);
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
		return null;
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
		properties.keySet().forEach(e -> {
			String versionKey = e.toString();
			if (versionKey.endsWith(keyVersionSuffix))
			{
				versionMap.put(versionKey, properties.getProperty(versionKey));
			}
		});
		return versionMap;
	}

	public static String getNewDependenciesStructure(List<DependencyInfo> dependencyInfos)
	{
		StringBuilder sb = new StringBuilder();
		List<String> versionKeys = ListFactory.newArrayList();
		sb.append("dependencies {").append(System.lineSeparator());
		dependencyInfos.forEach(dependencyInfo -> {
			String dependencyInfoVersionKey = dependencyInfo.getArtifactId() + "-version";
			if (!versionKeys.contains(dependencyInfoVersionKey))
			{
				versionKeys.add(dependencyInfoVersionKey);
				String scope = dependencyInfo.getScope();
				if (dependencyInfo.getArtifactId() != null)
				{
					String artifactId = dependencyInfo.getArtifactId().replaceAll("-", ".");
					sb.append(scope).append(" libs.").append(artifactId)
						.append(System.lineSeparator());
				}
			}
		});
		sb.append("}").append(System.lineSeparator());
		return sb.toString();
	}


	public static String getLibsVersionTomlMapAsString(List<DependencyInfo> dependencyInfos)
	{
		AtomicBoolean withLombok = new AtomicBoolean(false);
		AtomicBoolean withJunit = new AtomicBoolean(false);
		StringBuilder sb = new StringBuilder();
		List<String> versionKeys = ListFactory.newArrayList();
		sb.append("[versions]").append(System.lineSeparator());
		dependencyInfos.forEach(dependencyInfo -> {

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
			if (dependencyInfo.getGroupId() != null
				&& dependencyInfo.getGroupId().equals("org.projectlombok"))
			{
				withLombok.set(true);
			}
			if (dependencyInfo.getGroupId() != null
				&& dependencyInfo.getGroupId().equals("org.junit.jupiter"))
			{
				withJunit.set(true);
			}
		});
		// add plugin versions
		sb.append("gradle-plugin-grgit-version").append(" = ").append("\"").append("5.2.2")
			.append("\"").append(System.lineSeparator());

		sb.append("gradle-plugin-license-version").append(" = ").append("\"").append("0.16.1")
			.append("\"").append(System.lineSeparator());

		if (withLombok.get())
		{

			sb.append("gradle-plugin-lombok-version").append(" = ").append("\"").append("8.6")
				.append("\"").append(System.lineSeparator());
		}

		sb.append("gradle-plugin-spotless-version").append(" = ").append("\"").append("7.0.0.BETA1")
			.append("\"").append(System.lineSeparator());

		sb.append("gradle-plugin-version-catalog-update-version").append(" = ").append("\"")
			.append("0.8.4").append("\"").append(System.lineSeparator());

		sb.append("gradle-plugin-versions-version").append(" = ").append("\"").append("0.51.0")
			.append("\"").append(System.lineSeparator());

		sb.append(System.lineSeparator());
		List<String> libraryKeys = ListFactory.newArrayList();
		sb.append("[libraries]").append(System.lineSeparator());
		dependencyInfos.forEach(dependencyInfo -> {
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
		// add default bundles section ...
		sb.append(System.lineSeparator()).append("[bundles]").append(System.lineSeparator());
		if (withJunit.get())
		{
			String junitDefaultBundles = """
				unit-testing = [
				    "junit-jupiter",
				    "meanbean",
				    "test-object",
				]
								""";
			sb.append(junitDefaultBundles).append(System.lineSeparator());
		}
		else
		{
			String testngDefaultBundles = """
				unit-testing = [
				    "meanbean",
				    "test-object",
				    "testng",
				]
								""";
			sb.append(testngDefaultBundles).append(System.lineSeparator());
		}
		// TODO add default plugins section...
		// add default plugins section ...
		sb.append("[plugins]").append(System.lineSeparator());

		if (withLombok.get())
		{
			String withLombokPlugins = """
				gradle-versions-plugin = { id = "com.github.ben-manes.versions", version.ref = "gradle-plugin-versions-version" }
				grgit-gradle = { id = "org.ajoberstar.grgit", version.ref = "gradle-plugin-grgit-version" }
				license-gradle-plugin = { id = "com.github.hierynomus.license", version.ref = "gradle-plugin-license-version" }
				lombok-plugin = { id = "io.freefair.lombok", version.ref = "gradle-plugin-lombok-version" }
				spotless-plugin-gradle = { id = "com.diffplug.spotless", version.ref = "gradle-plugin-spotless-version" }
				version-catalog-update = { id = "nl.littlerobots.version-catalog-update", version.ref = "gradle-plugin-version-catalog-update-version" }
												""";
			sb.append(withLombokPlugins).append(System.lineSeparator());
		}
		else
		{
			String defaultPlugins = """
				gradle-versions-plugin = { id = "com.github.ben-manes.versions", version.ref = "gradle-plugin-versions-version" }
				grgit-gradle = { id = "org.ajoberstar.grgit", version.ref = "gradle-plugin-grgit-version" }
				license-gradle-plugin = { id = "com.github.hierynomus.license", version.ref = "gradle-plugin-license-version" }
				spotless-plugin-gradle = { id = "com.diffplug.spotless", version.ref = "gradle-plugin-spotless-version" }
				version-catalog-update = { id = "nl.littlerobots.version-catalog-update", version.ref = "gradle-plugin-version-catalog-update-version" }
												""";
			sb.append(defaultPlugins).append(System.lineSeparator());
		}
		return sb.toString();
	}
}
