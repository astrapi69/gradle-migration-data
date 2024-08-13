package io.github.astrapi69.gradle.migration.info;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * The class {@link DependencyInfo} represents information about a single dependency in a Gradle
 * project, including its scope, group ID, artifact ID, and version.
 */
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DependencyInfo
{
	/** The scope of the dependency (e.g., implementation, testImplementation). */
	String scope;

	/**
	 * The group ID of the dependency, which typically represents the organization or project that
	 * provides the library.
	 */
	String groupId;

	/** The artifact ID of the dependency, which identifies the library within its group. */
	String artifactId;

	/** The version of the dependency. */
	String version;
}
