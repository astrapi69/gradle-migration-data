package io.github.astrapi69.gradle.migration.shell;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The class {@link ShellExecutor} provides methods for execute commands in the shell
 */
public class ShellExecutor
{

	/**
	 * Executes the given command in the given execution path with the given executable
	 *
	 * @param shellPath
	 *            the path where the shell executable locates
	 * @param executionPath
	 *            the path where the command shell be executed
	 * @param command
	 *            the command to execute
	 * @return the output if any
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	public static String execute(String shellPath, String executionPath, String command)
		throws IOException, InterruptedException
	{
		if (executionPath.contains("~"))
		{
			executionPath = executionPath.replace("~", System.getProperty("user.home"));
		}
		File executionDirectory = new File(executionPath);
		if (!executionDirectory.exists())
		{
			throw new IllegalArgumentException("Execution directory does not exist");
		}
		String cdToExecutionPath = "cd " + executionPath;
		String commands = cdToExecutionPath + " && " + command;
		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command(shellPath, "-c", commands);

		Process process = processBuilder.start();
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

		String line;
		while ((line = reader.readLine()) != null)
		{
			stringBuilder.append(line + "\n");
		}
		int waitFor = process.waitFor();

		if (waitFor != 0)
			stringBuilder.append("Exit code: ").append(waitFor).append("\n");
		return stringBuilder.toString();
	}
}
