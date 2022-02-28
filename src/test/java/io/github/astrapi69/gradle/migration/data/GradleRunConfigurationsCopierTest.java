/**
 * The MIT License
 *
 * Copyright (C) 2021 Asterios Raptis
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
package io.github.astrapi69.gradle.migration.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.text.WordUtils;
import org.junit.jupiter.api.Test;

/**
 * The unit test class for the class {@link GradleRunConfigurationsCopier}
 */
public class GradleRunConfigurationsCopierTest
{

	/**
	 * Test method for {@link GradleRunConfigurationsCopier#getProjectVersionKeyName(String)}
	 */
	@Test
	void getProjectVersionKeyName()
	{
		String actual;
		String expected;
		String input;
		String templateProjectName;
		templateProjectName = DependenciesInfo.JAVA_LIBRARY_TEMPLATE_NAME;

		input = templateProjectName;
		expected = "JavaLibraryTemplate";
		assertEquals(expected,
			WordUtils.capitalizeFully(input, new char[] { '-' }).replaceAll("-", ""));

		actual = GradleRunConfigurationsCopier.getProjectVersionKeyName(input);
		expected = "javaLibraryTemplateVersion";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for copy run configurations file from a source project to a target project and
	 * modifies its content
	 */
	@Test
	// @Disabled
	public void testCopyIdeaRunConfigurations()
	{
		String sourceProjectDirNamePrefix;
		String targetProjectDirNamePrefix;
		CopyGradleRunConfigurations copyGradleRunConfigurationsData;
		boolean onlyRunConfigurations;
		boolean runConfigurationsInSameFolder;
		String sourceProjectName;
		String targetProjectName;
		String sourceGithubUser;
		String targetGithubUser;
		// copy
		sourceProjectName = "gradle-migration-data";
		targetProjectName = "auth";
		sourceGithubUser = "astrapi69";
		// targetGithubUser = "lightblueseas";
		targetGithubUser = "astrapi69";
		sourceProjectDirNamePrefix = "/home/astrapi69/dev/github/" + sourceGithubUser + "/";
		targetProjectDirNamePrefix = "/home/astrapi69/dev/github/" + targetGithubUser + "/";
		runConfigurationsInSameFolder = false;
		onlyRunConfigurations = false;
		copyGradleRunConfigurationsData = GradleRunConfigurationsCopier
			.newCopyGradleRunConfigurations(sourceProjectName, targetProjectName,
				sourceProjectDirNamePrefix, targetProjectDirNamePrefix, onlyRunConfigurations,
				runConfigurationsInSameFolder);
		GradleRunConfigurationsCopier.of(copyGradleRunConfigurationsData).copy();
	}

}
