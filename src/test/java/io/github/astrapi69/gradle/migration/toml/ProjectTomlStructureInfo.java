package io.github.astrapi69.gradle.migration.toml;

import java.io.File;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectTomlStructureInfo
{
	File gradleDirectory;
	String sourceProjectDirNamePrefix;
	String targetProjectDirNamePrefix;
	String sourceProjectName;
	String targetProjectName;
}
