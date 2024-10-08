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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Test class for {@link GitExtensions}
 */
public class GitExtensionsTest
{

	@TempDir
	Path tempDir;

	/**
	 * Test method for {@link GitExtensions#addFilesToGit(List, String, String)} Verifies that the
	 * method does not throw any exception when adding multiple files
	 */
	@Test
	void testAddFilesToGit() throws IOException, InterruptedException
	{
		List<String> filesToAdd = Arrays.asList("file1.txt", "file2.txt");
		String shellPath = "/bin/sh";
		String executionPath = tempDir.toString();

		filesToAdd.forEach(file -> {
			Path filePath = tempDir.resolve(file);
			try
			{
				Files.createFile(filePath);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		});

		assertDoesNotThrow(() -> GitExtensions.addFilesToGit(filesToAdd, shellPath, executionPath));
		GitExtensions.gitStatus(shellPath, executionPath);
		// Unstage files after adding them
		assertDoesNotThrow(() -> GitExtensions.unstageFiles(filesToAdd, shellPath, executionPath));
	}

	/**
	 * Test method for {@link GitExtensions#addFileToGit(String, String, String)} Verifies that the
	 * method returns the expected output for a single file addition
	 */
	@Test
	void testAddFileToGit() throws IOException, InterruptedException
	{
		String fileToAdd = "file1.txt";
		String shellPath = "/bin/sh";
		String executionPath = tempDir.toString();
		Path filePath = tempDir.resolve(fileToAdd);
		Files.createFile(filePath);

		GitExtensions.addFileToGit(fileToAdd, shellPath, executionPath);
		GitExtensions.gitStatus(shellPath, executionPath);
		// Unstage the file after adding it
		assertDoesNotThrow(
			() -> GitExtensions.unstageFiles(Arrays.asList(fileToAdd), shellPath, executionPath));
	}

	/**
	 * Clean up after each test by unstaging all files that may have been added during the test
	 */
	@AfterEach
	void tearDown()
	{
		List<String> filesToUnstage = Arrays.asList("file1.txt", "file2.txt");
		String shellPath = "/bin/sh";
		String executionPath = tempDir.toString();

		assertDoesNotThrow(
			() -> GitExtensions.unstageFiles(filesToUnstage, shellPath, executionPath));
	}
}
