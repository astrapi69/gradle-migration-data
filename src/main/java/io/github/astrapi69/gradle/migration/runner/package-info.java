/**
 * This package provides classes and utilities for managing and migrating Gradle project
 * configurations. It focuses on copying and modifying Gradle run configurations between different
 * projects, as well as handling dependencies and version management within Gradle projects.
 *
 * The main functionalities include:
 *
 * <ul>
 * <li>Copying Gradle run configurations from one project to another, with the ability to modify
 * configuration details as needed.</li>
 * <li>Externalizing version information from the build.gradle file to the gradle.properties file
 * for better version management and reuse.</li>
 * <li>Interacting with Git to add newly created or modified files during the migration
 * process.</li>
 * </ul>
 *
 * The following classes are included in this package:
 *
 * <ul>
 * <li>{@link io.github.astrapi69.gradle.migration.runner.GradleRunConfigurationsCopier} -
 * Responsible for copying and modifying Gradle run configurations and externalizing versions from
 * the build.gradle file.</li>
 * </ul>
 *
 * This package is designed to streamline the process of migrating Gradle-based projects, making it
 * easier to manage project dependencies, run configurations, and version control.
 */
package io.github.astrapi69.gradle.migration.runner;
