package io.github.astrapi69.gradle.migration.data;

import org.apache.commons.text.WordUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The unit test class for the class {@link GradleRunConfigurationsCopier}
 */
public class GradleRunConfigurationsCopierTest
{

	@Test void getProjectVersionKeyName()
	{
		String actual;
		String expected;
		String input;
		String templateProjectName;
		templateProjectName = DependenciesInfo.JAVA_LIBRARY_TEMPLATE_NAME;

		input = templateProjectName;
		expected = "JavaLibraryTemplate";
		assertEquals(expected, WordUtils
			.capitalizeFully(input, new char[]{'-'}).replaceAll("-", ""));

		actual = GradleRunConfigurationsCopier.getProjectVersionKeyName(input);
		expected = "javaLibraryTemplateVersion";
		assertEquals(expected, actual);
	}
}
