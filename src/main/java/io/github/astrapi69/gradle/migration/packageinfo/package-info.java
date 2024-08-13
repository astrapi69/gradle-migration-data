/**
 * This package provides utilities for converting and maintaining package documentation files within
 * a Gradle project. It specifically handles the migration of obsolete package.html files to the
 * more modern package-info.java format.
 *
 * The primary functionalities include:
 *
 * <ul>
 * <li>Traversing project directories to identify and process package.html files.</li>
 * <li>Extracting the content of the <body> tag from package.html files and transferring it to newly
 * created package-info.java files.</li>
 * <li>Ensuring the correct package structure is maintained in the generated package-info.java
 * files.</li>
 * <li>Deleting obsolete package.html files after successful conversion.</li>
 * </ul>
 *
 * The tools in this package are particularly useful for modernizing Java projects that still rely
 * on older documentation practices, ensuring compatibility with current standards.
 */
package io.github.astrapi69.gradle.migration.packageinfo;
