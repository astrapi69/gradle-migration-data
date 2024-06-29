# how to migrate to libs.versions.toml

1. Get the target project directory absolute path from the project to migrate and set it to the ProjectTomlStructureInfo
   variable 'targetProjectDirNamePrefix' and set also the target project name 'targetProjectName'. Get the source
   project directory absolute path from the source project and set it to the ProjectTomlStructureInfo variable
   'sourceProjectDirNamePrefix' and set also the target project name 'sourceProjectName', this is usually the
   java-library-template project. Then run the method of the unit test class
   MigrateToTomlVersionsTest#testMigrateToNewProjectStructure(remove or comment the annotation 'Disabled' before). This
   will create the file 'libs.versions.toml' with the corresponding versions from the gradle.properties and copy the
   file version-catalog-update.gradle to target project, and the corresponding idea run configurations. All files
   will be staged and added to git repository. the file '.github/workflows/gradle.yml' file is also upgraded
2. check the file 'libs.versions.toml' if every version follows the name conventions
3. check if the build.gradle file if everything is works as expected
4. adapt the dependencies in the dependencies.gradle file for the unit-testing bundle and add entries to CHANGELOG.md
5. run the idea run configuration 'versionCatalogFormat' for formating all entries in the file 'libs.versions.toml'.
6. run the idea run configuration 'build' for verify that everything works and push to git repository.
7. now run the idea run configuration 'versionCatalogUpdate' for update for the first time your project dependency
   versions automatically. Now check again if everything works with the new versions and if everything works you can
   update CHANGELOG.md file with the new versions and push it
8. removed unused versions from gradle.properties
