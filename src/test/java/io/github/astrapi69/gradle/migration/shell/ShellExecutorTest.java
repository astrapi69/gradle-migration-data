package io.github.astrapi69.gradle.migration.shell;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.github.astrapi69.file.search.PathFinder;

class ShellExecutorTest
{

	@Test
	void execute() throws IOException, InterruptedException
	{

		String shellPath;
		String executionPath;
		String command;

		shellPath = "/usr/bin/zsh";

		executionPath = PathFinder.getProjectDirectory().getAbsolutePath();
		command = "git add HELP.md";

		String shellOutput = ShellExecutor.execute(shellPath, executionPath, command);
		System.out.println(shellOutput);
	}

}