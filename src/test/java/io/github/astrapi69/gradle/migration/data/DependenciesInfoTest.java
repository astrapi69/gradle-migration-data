package io.github.astrapi69.gradle.migration.data;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

/**
 * The unit test class for the class {@link DependenciesInfo}
 */
public class DependenciesInfoTest
{

	/**
	 * Test method for {@link DependenciesInfo}
	 */
	@Test @Disabled public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(DependenciesInfo.class);
	}

}
