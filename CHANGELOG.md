## Change log
----------------------

Version 2.3-SNAPSHOT
-------------

ADDED:

- new class GradleProjectInfo for handle migration
- new test dependency jgit

CHANGED:

- update of dependency lombok to new version 1.18.34
- update of dependency file-worker to new version 17.2
- extract string file names of gradle files to constants
- update of dependency silly-strings to new version 9.1
- update of test dependency throwable to new version 3
- remove of deprecated method in class GradleRunConfigurationsCopier

Version 2.2
-------------

ADDED:

- new libs.versions.toml file for new automatic catalog versions update
- new class MigrateToTomlVersions that provides methods for migrate to new gradle project with libs.versions.toml file
- new method for get the content of a section from the build.gradle file

CHANGED:

- update gradle to new version 8.8
- update of gradle-plugin dependency with id 'com.github.ben-manes.versions' to new patch version 0.51.0
- update of gradle-plugin dependency with id 'org.ajoberstar.grgit' to new patch version 5.2.2
- update of gradle-plugin dependency with id 'io.freefair.lombok' to new patch version 8.6
- update of gradle-plugin dependency with id 'com.diffplug.spotless' to new beta version 7.0.0.BETA1
- new class MigrateToTomlVersions that provides methods for migrate to new gradle project with libs.versions.toml file
- new method for get the content of a section from the build.gradle file
- update of test dependency mockito-core to new version 5.12.0
- update of test dependency junit-jupiter-api to new version 5.11.0-M2
- rename of package data to info

Version 2.1
-------------

ADDED:

- new class DependenciesExtensions for handling with the dependencies
- new class DependencyInfo for dependency information
- new file libs.versions.toml for automate version updates
- new gradle plugin version-catalog-update for automate version updates

CHANGED:

- update gradle to new version 8.8-rc-1
- update of dependency lombok to new version 1.18.32
- update of gradle-plugin dependency with id 'com.github.ben-manes.versions' to new minor version 0.51.0
- update of gradle-plugin dependency with id 'com.diffplug.spotless' to new beta version 6.25.0
- update of dependency silly-io to new version 3
- update of test dependency commons-lang3 to new version 3.14.0
- update of test dependency junit-jupiter-api to new version 5.11.0-M1

Version 2
-------------

CHANGED:

- update to jdk version 17
- update gradle to new version 8.4
- update of dependency lombok to new version 1.18.30
- update gradle-plugin dependency of com.github.ben-manes:gradle-versions-plugin to new version 0.49.0
- new gradle plugin dependency of 'com.diffplug.spotless:spotless-plugin-gradle' to minor version 6.22.0
- update of dependency silly-collections to new version 27
- update of dependency silly-io to new version 2.2
  - update of dependency silly-strings to new version 9
  - update of dependency file-worker to new version 17.1
- update of test dependency commons-lang3 to new version 3.13.0
- update of test dependency junit-jupiter-api to new version 5.10.0

Version 1.3
-------------

CHANGED:

- update to jdk version 11
- update gradle to new version 7.5.1
- update gradle-plugin dependency of com.github.ben-manes:gradle-versions-plugin to new version 0.42.0
- new gradle plugin dependency of 'com.diffplug.spotless:spotless-plugin-gradle' to minor version 6.11.0
- update of dependency silly-strings to new version 8.2
- update of dependency silly-io to new version 2.1
- update of dependency file-worker to new version 11.3
- update of test dependency junit-jupiter-api to new version 5.9.1
- update of test dependency throwable to new version 2.3

Version 1.2.1
-------------

CHANGED:

- update of dependency silly-strings to new version 8.1

Version 1.2
-------------

CHANGED:

- update gradle to new version 7.2
- update of dependency lombok to new version 1.18.22
- update of dependency silly-collections to new version 18
- update of dependency silly-io to new version 1.6
- update of dependency silly-strings to new version 8
- update of dependency file-worker to new version 5.10
- update of test dependency throw-able to new version 1.7
- update of test dependency junit-jupiter-api to new version 5.8.1

Version 1.1
-------------

CHANGED:

- downgrade to openjdk8
- update gradle to new version 7.1
- update gradle-plugin dependency of gradle.plugin.com.hierynomus.gradle.plugins:license-gradle-plugin to new version 0.16.1

Version 1
-------------

ADDED:

- new CHANGELOG.md file created

Notable links:
[keep a changelog](http://keepachangelog.com/en/1.0.0/) Don’t let your friends dump git logs into
changelogs
