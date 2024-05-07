package io.github.astrapi69.gradle.migration.data;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.dataformat.toml.TomlMapper;

public class JacksonTomlExtensions
{

	public static Map read(File tomlFile) throws IOException
	{
		TomlMapper mapper = new TomlMapper();
		Map map = mapper.readValue(tomlFile, Map.class);
		return map;
	}


	public static void writeValue(File tomlFile, Map<String, Map> libsVersionTomlMap)
		throws IOException
	{
		TomlMapper mapper = new TomlMapper();
		String valueAsString = mapper.writeValueAsString(libsVersionTomlMap);
		mapper.writeValue(tomlFile, valueAsString);
	}
}
