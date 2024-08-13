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
import java.io.IOException;

import io.github.astrapi69.file.create.DirectoryFactory;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * The class {@link MigrationInfo} holds information related to the migration of a Gradle project,
 * including references to the project directory and the Gradle directory.
 */
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MigrationInfo
{
	/** The Gradle directory within the project. */
	File gradleDirectory;

	/** The root directory of the project. */
	File projectDirectory;

	/**
	 * Creates a new instance of {@link MigrationInfo} from the absolute path of a project
	 * directory. This method will create the necessary directories if they do not already exist.
	 *
	 * @param projectDirectoryName
	 *            the absolute path of the project directory
	 * @return a new {@link MigrationInfo} instance with the project and Gradle directories
	 *         initialized
	 * @throws IOException
	 *             if an I/O error occurs during directory creation
	 */
	public static MigrationInfo fromAbsolutePath(String projectDirectoryName) throws IOException
	{
		File projectDirectory = DirectoryFactory.newDirectory(projectDirectoryName);
		File gradleDirectory = DirectoryFactory.newDirectory(projectDirectory, "gradle");
		return MigrationInfo.builder().projectDirectory(projectDirectory)
			.gradleDirectory(gradleDirectory).build();
	}
}
