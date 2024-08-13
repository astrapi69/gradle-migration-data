package io.github.astrapi69.gradle.migration.extension;

import java.io.IOException;
import java.util.List;

import io.github.astrapi69.io.shell.LinuxShellExecutor;
import lombok.extern.java.Log;

/**
 * The class {@code GitAddExtensions} provides methods to add files to a Git repository
 */
@Log
public class GitExtensions
{

	/**
	 * Adds a list of files to Git using the specified shell path and execution path
	 *
	 * @param filesToAdd
	 *            the list of files to add
	 * @param shellPath
	 *            the path to the shell executable
	 * @param executionPath
	 *            the path where the command should be executed
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws InterruptedException
	 *             if the current thread is interrupted while waiting
	 */
	public static void addFilesToGit(List<String> filesToAdd, String shellPath,
		String executionPath) throws IOException, InterruptedException
	{
		String output;
		for (String file : filesToAdd)
		{
			output = addFileToGit(file, shellPath, executionPath);
			log.info("output of command: " + output);
		}
	}

	/**
	 * Adds a single file to Git using the specified shell path and execution path
	 *
	 * @param fileToAdd
	 *            the file to add
	 * @param shellPath
	 *            the path to the shell executable
	 * @param executionPath
	 *            the path where the command should be executed
	 * @return the output of the executed command
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws InterruptedException
	 *             if the current thread is interrupted while waiting
	 */
	public static String addFileToGit(String fileToAdd, String shellPath, String executionPath)
		throws IOException, InterruptedException
	{
		String command;
		String output;
		String gitAddCommandPrefix;
		gitAddCommandPrefix = "git -c credential.helper= -c core.quotepath=false -c log.showSignature=false add "
			+ "--ignore-errors -A -f -- ";
		command = gitAddCommandPrefix + fileToAdd;
		log.info("Executing command: " + command);

		output = LinuxShellExecutor.execute(shellPath, executionPath, command);
		return output;
	}

	/**
	 * Utility method to unstage files from the Git staging area
	 *
	 * @param filesToUnstage
	 *            the list of files to unstage
	 * @param shellPath
	 *            the path to the shell executable
	 * @param executionPath
	 *            the path where the command should be executed
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws InterruptedException
	 *             if the current thread is interrupted while waiting
	 */
	public static void unstageFiles(List<String> filesToUnstage, String shellPath,
		String executionPath) throws IOException, InterruptedException
	{
		for (String file : filesToUnstage)
		{
			String command = "git -c credential.helper= restore --staged " + file;
			log.info("Unstaging file with command: " + command);
			String output = LinuxShellExecutor.execute(shellPath, executionPath, command);
			log.info("output of unstaging command: " + output);
		}
	}

	/**
	 * Executes the 'git status' command in the specified directory using the given shell path.
	 *
	 * @param shellPath
	 *            the path to the shell executable
	 * @param executionPath
	 *            the directory in which the command should be executed
	 * @throws IOException
	 *             if an I/O error occurs during the execution of the command
	 * @throws InterruptedException
	 *             if the execution of the command is interrupted
	 */
	public static void gitStatus(String shellPath, String executionPath)
		throws IOException, InterruptedException
	{
		String output;
		String command;
		command = "git status";
		log.info("Executing command: " + command);
		output = LinuxShellExecutor.execute(shellPath, executionPath, command);
		log.info("output of command: " + output);
	}
}
