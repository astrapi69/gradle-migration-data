package io.github.astrapi69.gradle.migration.extension;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CaseExtensionsTest
{


	@Test
	void kebabToUpperSnakeCase()
	{
		String actual;
		String expected;
		String input;

		input = "example-string-for-conversion";

		// new scenario...
		actual = CaseExtensions.kebabToUpperSnakeCase(input);
		expected = "EXAMPLE_STRING_FOR_CONVERSION";
		assertEquals(expected, actual);
	}

	@Test
	void kebabToSnakeCase()
	{
		String actual;
		String expected;
		String input;

		input = "example-string-for-conversion";

		// new scenario...
		actual = CaseExtensions.kebabToSnakeCase(input);
		expected = "example_string_for_conversion";
		assertEquals(expected, actual);
	}

	@Test
	void snakeToPascalCase()
	{
		String actual;
		String expected;
		String input;

		input = "example_string_for_conversion";

		// new scenario...
		actual = CaseExtensions.snakeToPascalCase(input);
		expected = "ExampleStringForConversion";
		assertEquals(expected, actual);
	}

	@Test
	void kebabToDotCase()
	{
		String actual;
		String expected;
		String input;

		input = "example-string-for-conversion";

		// new scenario...
		actual = CaseExtensions.kebabToDotCase(input);
		expected = "example.string.for.conversion";
		assertEquals(expected, actual);
	}

}