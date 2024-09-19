package io.github.astrapi69.gradle.task;

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Files;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class CopyDependenciesToM2TaskTest
{

	@Test
	@Disabled
	void testCopyDependenciesToM2() throws Exception
	{
		// Setup test project directory
		File projectDir = Files.createTempDirectory("gradle-test").toFile();
		File buildFile = new File(projectDir, "build.gradle");

		// Write build script to the test project directory
		String buildScript = """
			buildscript {
				repositories {
					mavenCentral()
				}
				dependencies {
					classpath 'org.apache.ivy:ivy:2.5.0' // Or the latest version
				}
			}
			plugins {
				id 'java'
				id 'maven-publish'
			}

			repositories {
				mavenCentral()
			}

			dependencies {
				implementation 'org.apache.commons:commons-lang3:3.12.0'
			}

			import org.apache.ivy.util.url.CredentialsStore

			task copyDependenciesToM2 {
				doLast {
					def localM2Repo = file("${System.properties['user.home']}/.m2/repository")
					configurations.each { config ->
						config.resolvedConfiguration.resolvedArtifacts.each { artifact ->
							def groupPath = artifact.moduleVersion.id.group.replace('.', '/')
							def artifactDir = new File(localM2Repo, "${groupPath}/${artifact.moduleVersion.id.name}/${artifact.moduleVersion.id.version}")
							artifactDir.mkdirs()
							def artifactFile = artifact.file
							def targetFile = new File(artifactDir, "${artifact.name}-${artifact.moduleVersion.id.version}.${artifact.extension}")

							if (!targetFile.exists()) {
								copy {
									from artifactFile
									into artifactDir
									rename { fileName -> "${artifact.name}-${artifact.moduleVersion.id.version}.${artifact.extension}" }
								}
							}
						}
					}
				}
			}

			copyDependenciesToM2.dependsOn configurations.runtimeClasspath
			""";

		Files.write(buildFile.toPath(), buildScript.getBytes());

		// Run the task using GradleRunner
		BuildResult result = GradleRunner.create().withProjectDir(projectDir)
			.withArguments("copyDependenciesToM2").withPluginClasspath().build();

		// Verify that the task ran successfully
		assertEquals(SUCCESS, result.task(":copyDependenciesToM2").getOutcome());

		// Verify that the dependency was copied to the local Maven repository
		File expectedFile = new File(System.getProperty("user.home")
			+ "/.m2/repository/org/apache/commons/commons-lang3/3.12.0/commons-lang3-3.12.0.jar");
		assertTrue(expectedFile.exists(), "Dependency should be copied to local Maven repository");

		// Clean up
		deleteDirectory(projectDir);
	}

	// Helper method to delete directory
	private void deleteDirectory(File directory)
	{
		File[] files = directory.listFiles();
		if (files != null)
		{
			for (File file : files)
			{
				if (file.isDirectory())
				{
					deleteDirectory(file);
				}
				else
				{
					file.delete();
				}
			}
		}
		directory.delete();
	}
}
