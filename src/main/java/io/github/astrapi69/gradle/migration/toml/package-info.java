/**
 * This package provides utilities and methods for migrating Gradle projects to a TOML-based
 * structure. The tools in this package are designed to help manage and convert project
 * configurations, dependencies, and versioning information into the TOML format, which is
 * increasingly used in modern Gradle projects.
 *
 * The primary functionalities include:
 *
 * <ul>
 * <li>Generating and storing version information in a libs.versions.toml file for better dependency
 * management.</li>
 * <li>Converting existing project structures and configurations to align with the TOML format,
 * which is used by Gradle for version catalogs.</li>
 * <li>Facilitating the migration of Gradle projects from older formats to the TOML-based project
 * structure, ensuring compatibility with modern Gradle practices.</li>
 * <li>Copying and modifying project configuration files, including Gradle's run configurations and
 * dependency files, to support the new project structure.</li>
 * </ul>
 *
 * The classes in this package are intended to streamline the process of updating and maintaining
 * Gradle projects as they transition to using TOML for managing dependencies and other
 * configurations.
 */
package io.github.astrapi69.gradle.migration.toml;
