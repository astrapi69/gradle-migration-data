package io.github.astrapi69.gradle.migration.extension;

import org.apache.commons.text.CaseUtils;
import org.apache.commons.text.WordUtils;

public class CaseExtensions
{

	public static String kebabToCamelCase(String kebabCaseProjectName)
	{
		return WordUtils.capitalizeFully(kebabCaseProjectName, new char[] { '-' }).replaceAll("-",
			"");
	}

	public static String kebabToDotCase(String kebabCase)
	{
		if (kebabCase == null || kebabCase.isEmpty())
		{
			return kebabCase;
		}
		return kebabCase.replace('-', '.');
	}

	public static String kebabToSnakeCase(String kebabCase)
	{
		if (kebabCase == null || kebabCase.isEmpty())
		{
			return kebabCase;
		}
		return kebabCase.replace('-', '_');
	}

	public static String kebabToUpperSnakeCase(String kebabCase)
	{
		if (kebabCase == null || kebabCase.isEmpty())
		{
			return kebabCase;
		}
		return kebabCase.replace('-', '_').toUpperCase();
	}

	public static String snakeToPascalCase(String snakeCase)
	{
		String pascalCase = CaseUtils.toCamelCase(snakeCase, true, '_');
		return pascalCase;
	}
}
