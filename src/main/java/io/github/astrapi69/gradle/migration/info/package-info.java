/**
 * This package provides classes that hold information and configurations related to Gradle
 * projects, facilitating project migration, dependency management, and version control. The classes
 * in this package are primarily designed to manage and transfer settings, configurations, and
 * dependencies between different Gradle projects.
 *
 * The key functionalities include:
 *
 * <ul>
 * <li>Storing and managing Gradle project dependencies and related information.</li>
 * <li>Facilitating the migration of Gradle project configurations from one project to another.</li>
 * <li>Handling and organizing Gradle's TOML structure for version catalogs.</li>
 * <li>Converting and maintaining critical Gradle configuration files during migrations.</li>
 * </ul>
 *
 * The following classes are included in this package:
 *
 * <ul>
 * <li>{@link io.github.astrapi69.gradle.migration.info.CopyGradleRunConfigurations} - Holds
 * configuration information required to copy Gradle run configurations between projects.</li>
 * <li>{@link io.github.astrapi69.gradle.migration.info.DependenciesInfo} - Stores information
 * related to Gradle dependencies, project files, and versioning information.</li>
 * <li>{@link io.github.astrapi69.gradle.migration.info.DependencyInfo} - Represents information
 * about a single dependency in a Gradle project, including its scope, group ID, artifact ID, and
 * version.</li>
 * <li>{@link io.github.astrapi69.gradle.migration.info.GradleProjectInfo} - Contains comprehensive
 * information about a Gradle project, including dependencies and migration details.</li>
 * <li>{@link io.github.astrapi69.gradle.migration.info.MigrationInfo} - Provides details about the
 * migration process for a Gradle project, including project and Gradle directory references.</li>
 * <li>{@link io.github.astrapi69.gradle.migration.info.ProjectTomlStructureInfo} - Holds
 * information related to the TOML structure of a Gradle project, including source and target
 * project details.</li>
 * </ul>
 *
 * These classes collectively support the smooth migration and management of Gradle-based projects
 * by handling configurations, dependencies, and version control efficiently.
 */
package io.github.astrapi69.gradle.migration.info;
