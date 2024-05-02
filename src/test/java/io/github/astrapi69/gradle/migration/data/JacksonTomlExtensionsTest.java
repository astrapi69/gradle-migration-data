package io.github.astrapi69.gradle.migration.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.collection.list.ListExtensions;
import io.github.astrapi69.collection.list.ListFactory;
import io.github.astrapi69.collection.map.MapFactory;
import io.github.astrapi69.collection.properties.PropertiesExtensions;
import io.github.astrapi69.file.search.PathFinder;
import lombok.NonNull;

/**
 * The unit test class for the class {@link JacksonTomlExtensions}
 */
public class JacksonTomlExtensionsTest
{
	/**
	 * Test method for {@link JacksonTomlExtensions}
	 */
	@Test
	// @Disabled
	public void testReadTomlFileWithJackson() throws IOException
	{
		File tomlFile = PathFinder.getRelativePath(PathFinder.getSrcTestResourcesDir(),
			"libs.versions.toml");


		assertTrue(tomlFile.exists());
		Map toml = JacksonTomlExtensions.read(tomlFile);
		System.out.println(toml);
		assertNotNull(toml);


	}

	/**
	 * Test method for {@link JacksonTomlExtensions}
	 */
	@Test
	// @Disabled
	public void testReadGradleProperties() throws IOException
	{
		File gradlePropertiesFile = PathFinder.getRelativePath(PathFinder.getProjectDirectory(),
			"gradle.properties");
		assertTrue(gradlePropertiesFile.exists());
		// 1. Load all version from gradle.properties
		Map<String, String> versionMap = getVersionMap(gradlePropertiesFile, "Version");
		System.out.println(versionMap);
		// 2. read and load all dependencies from dependencies.gradle file
		// to implement
		// 3. read and load all plugins from build.gradle file
		// to implement
		// 4. create toml file and combine all information together
	}

	public static Map<String, String> getVersionMap(final @NonNull File gradlePropertiesFile,
		String keyVersionSuffix) throws IOException
	{
		Properties properties = PropertiesExtensions.loadProperties(gradlePropertiesFile);
		Map<String, String> versionMap = MapFactory.newLinkedHashMap();
		properties.keySet().stream().forEach(e -> {
			String versionKey = e.toString();
			if (versionKey.endsWith(keyVersionSuffix))
			{
				versionMap.put(versionKey, properties.getProperty(versionKey));
			}
		});
		return versionMap;
	}

	/**
	 * Test method for {@link JacksonTomlExtensions}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(JacksonTomlExtensions.class);
	}
}
