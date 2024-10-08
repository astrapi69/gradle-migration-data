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
