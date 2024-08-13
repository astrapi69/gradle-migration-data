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
package io.github.astrapi69.gradle.migration.info;

import java.io.File;
import java.util.List;
import java.util.Properties;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * The class {@link DependenciesInfo} holds information related to Gradle dependencies and project
 * files. It contains references to key files and properties used in a Gradle project, along with
 * version strings for managing dependency versions.
 */
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DependenciesInfo
{
	/** The template name for a Java library project. */
	public static final String JAVA_LIBRARY_TEMPLATE_NAME = "java-library-template";

	/** The template name for a Spring Boot project. */
	public static final String SPRING_BOOT_TEMPLATE_NAME = "spring-boot-template";

	/** The filename of the settings.gradle file. */
	public static final String SETTINGS_GRADLE_FILENAME = "settings.gradle";

	/** The name of the directory that contains Gradle wrapper and related files. */
	public static final String PROJECT_GRADLE_DIRECTORY_NAME = "gradle";

	/** The filename of the version-catalog-update.gradle file. */
	public static final String VERSION_CATALOG_UPDATE_GRADLE_FILENAME = "version-catalog-update.gradle";

	/** The filename of the libs.versions.toml file. */
	public static final String LIBS_VERSIONS_TOML_FILENAME = "libs.versions.toml";

	/** The filename of the dependencies.gradle file. */
	public static final String DEPENDENCIES_GRADLE_FILENAME = "dependencies.gradle";

	/** The filename of the build.gradle file. */
	public static final String BUILD_GRADLE_FILENAME = "build.gradle";

	/** The filename of the gradle.properties file. */
	public static final String GRADLE_PROPERTIES_FILENAME = "gradle.properties";

	/**
	 * The name of the .github directory, typically used for GitHub workflows and configurations.
	 */
	public static final String DOT_GITHUB_DIRECTORY_NAME = ".github";

	/** The filename of the .travis.yml file, used for Travis CI configuration. */
	public static final String DOT_TRAVIS_FILENAME = ".travis.yml";

	/**
	 * The filename of the CODE_OF_CONDUCT.md file, which typically contains the project's code of
	 * conduct.
	 */
	public static final String CODE_OF_CONDUCT_FILENAME = "CODE_OF_CONDUCT.md";

	/**
	 * The filename of the README.md file, which typically contains the project's readme
	 * documentation.
	 */
	public static final String README_FILENAME = "README.md";

	/** The build.gradle file associated with the project. */
	File buildGradle;

	/** The settings.gradle file associated with the project. */
	File settingsGradle;

	/** The properties loaded from the gradle.properties file. */
	Properties properties;

	/** A list of version strings representing the versions of dependencies. */
	List<String> versionStrings;
}
