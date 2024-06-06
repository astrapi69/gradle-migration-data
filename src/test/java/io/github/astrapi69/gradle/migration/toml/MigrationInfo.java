package io.github.astrapi69.gradle.migration.toml;

import io.github.astrapi69.file.create.DirectoryFactory;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.File;
import java.io.IOException;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MigrationInfo {
    File gradleDirectory;
    File projectDirectory;
    public static MigrationInfo fromAbsolutePath(String projectDirectoryName) throws IOException {
        File projectDirectory = DirectoryFactory.newDirectory(projectDirectoryName);
        File gradleDirectory = DirectoryFactory.newDirectory(projectDirectory, "gradle");
        return MigrationInfo.builder()
                .projectDirectory(projectDirectory)
                .gradleDirectory(gradleDirectory)
                .build();
    }
}
