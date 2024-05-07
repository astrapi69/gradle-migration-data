package io.github.astrapi69.gradle.migration.data;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = false)
public class DependencyInfo
{
	String scope;
	String groupId;
	String artifactId;
	String version;
}
