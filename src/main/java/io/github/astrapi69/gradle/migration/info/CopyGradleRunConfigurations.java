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

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * The class {@link CopyGradleRunConfigurations} holds the configuration information required to
 * copy Gradle run configurations from one project to another.
 */
@Data
@SuperBuilder
public class CopyGradleRunConfigurations
{
	/** The name of the IntelliJ IDEA directory where configuration files are stored. */
	public static final String IDEA_DIR_NAME = ".idea";

	/** The name of the directory where run configurations are stored within the IDEA directory. */
	public static final String RUN_CONFIGURATIONS_DIR_NAME = "runConfigurations";

	/** The source IDEA directory. */
	File ideaSourceDir;

	/** The target IDEA directory. */
	File ideaTargetDir;

	/** The prefix for the source filename. */
	String sourceFilenamePrefix;

	/** The source project directory. */
	File sourceProjectDir;

	/** The name of the source project. */
	String sourceProjectName;

	/** The directory where run configurations are stored in the source project. */
	File sourceRunConfigDir;

	/** The prefix for the target filename. */
	String targetFilenamePrefix;

	/** The target project directory. */
	File targetProjectDir;

	/** Flag indicating whether only run configurations should be copied. */
	boolean onlyRunConfigurations;

	/**
	 * Flag indicating whether run configurations are stored in the same folder in the target
	 * project.
	 */
	boolean runConfigurationsInSameFolder;

	/** The name of the target project. */
	String targetProjectName;

	/** The directory where run configurations are stored in the target project. */
	File targetRunConfigDir;
}
