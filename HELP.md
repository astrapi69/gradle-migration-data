# how to migrate to libs.versions.toml

1. Get the target project directory absolute path from the project to migrate and set it to the ProjectTomlStructureInfo
   variable 'targetProjectDirNamePrefix' and set also the target project name 'targetProjectName'. Get the source
   project directory absolute path from the source project and set it to the ProjectTomlStructureInfo variable
   'sourceProjectDirNamePrefix' and set also the target project name 'sourceProjectName', this is usually the
   java-library-template project. Than run the method of the unit test class
   MigrateToTomlVersionsTest#testMigrateToNewProjectStructure(remove or comment the annotation 'Disabled' before). This
   will create the file 'libs.versions.toml' with the corresponding versions from the gradle.properties and copy the
   file version-catalog-update.gradle to target project, and the corresponding idea run configurations. Stage all files
   for add to git
2. in the file 'libs.versions.toml' there are missing the section [plugins] and the corresponding versions in the
   section [versions] and add the section [bundles]
3. update following sections in the build.gradle file: buildscript, plugins and also add the version-catalog-update
   gradle plugin
4. adapt the dependencies in the dependencies.gradle file for the unit-testing bundle
5. removed unused versions from gradle.properties
6. run the idea run configuration 'versionCatalogFormat' for formating all entries in the file 'libs.versions.toml'.
7. run the idea run configuration 'build' for verify that everything works and push to git repository.
8. now run the idea run configuration 'versionCatalogUpdate' for update for the first time your project dependency
   versions automatically. Now check again if everything works with the new versions and if everything works you can
   update CHANGELOG.md file with the new versions and push it
