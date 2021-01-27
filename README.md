# Overview

<div align="center">

[![Build Status](https://travis-ci.org/astrapi69/gradle-migration-data.svg?branch=master)](https://travis-ci.org/astrapi69/gradle-migration-data)
[![Coverage Status](https://coveralls.io/repos/github/astrapi69/gradle-migration-data/badge.svg?branch=develop)](https://coveralls.io/github/astrapi69/gradle-migration-data?branch=develop)
[![Open Issues](https://img.shields.io/github/issues/astrapi69/gradle-migration-data.svg?style=flat)](https://github.com/astrapi69/gradle-migration-data/issues)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.astrapi69/gradle-migration-data/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.astrapi69/gradle-migration-data)
[![Javadocs](http://www.javadoc.io/badge/io.github.astrapi69/gradle-migration-data.svg)](http://www.javadoc.io/doc/io.github.astrapi69/gradle-migration-data)
[![MIT license](http://img.shields.io/badge/license-MIT-brightgreen.svg?style=flat)](http://opensource.org/licenses/MIT)
[![Donate](https://img.shields.io/badge/donate-❤-ff2244.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=GVBTWLRAZ7HB8)

</div>

This project holds pojo classes for migrate maven projects to gradle projects and adapt them

If you like this project put a ⭐ and donate

## Note

No animals were harmed in the making of this library.

# Donations

If you like this library, please consider a donation through paypal: <a href="https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=B37J9DZF6G9ZC" target="_blank">
<img src="https://www.paypalobjects.com/en_US/GB/i/btn/btn_donateCC_LG.gif" alt="PayPal this" title="PayPal – The safer, easier way to pay online!" border="0" />
</a>

or over bitcoin or bitcoin-cash with:

36JxRRDfRazLNqUV6NsywCw1q7TK38ukpC

or over ether with:

0x588Aa02De98B1Ef70afeDC3ec5290130a3E5e273

or over flattr:
<a href="https://flattr.com/submit/auto?user_id=astrapi69&url=https://github.com/astrapi69/gradle-migration-data" target="_blank">
<img src="http://api.flattr.com/button/flattr-badge-large.png" alt="Flattr this" title="Flattr this" border="0" />
</a>

## License

The source code comes under the liberal MIT License, making gradle-migration-data great for all types of applications.

## Maven dependency

Maven dependency is now on sonatype.
Check out [sonatype repository](https://oss.sonatype.org/index.html#nexus-search;gav~io.github.astrapi69~gradle-migration-data~~~) for latest snapshots and releases.

Add the following maven dependency to your project `pom.xml` if you want to import the core 
functionality of gradle-migration-data:

Than you can add the dependency to your dependencies:

	<properties>
			...
		<!-- gradle-migration-data version -->
		<gradle-migration-data.version>1</gradle-migration-data.version>
			...
	</properties>
			...
		<dependencies>
			...
			<!-- gradle-migration-data DEPENDENCY -->
			<dependency>
				<groupId>io.github.astrapi69</groupId>
				<artifactId>gradle-migration-data</artifactId>
				<version>${gradle-migration-data.version}</version>
			</dependency>
			...
		</dependencies>

## gradle dependency

You can first define the version in the ext section and add than the following gradle dependency to
your project `build.gradle` if you want to import the core functionality of gradle-migration-data:

define version in file gradle.properties
```
gradleMigrationDataVersion=1
```

or in build.gradle ext area

```
ext {
			...
    gradleMigrationDataVersion = "1"
			...
}
```

and than add the dependency to the dependencies area

```
dependencies {
			...
    implementation("io.github.astrapi69:gradle-migration-data:$gradleMigrationDataVersion")
			...
}
```

## Semantic Versioning

The versions of gradle-migration-data are maintained with the Semantic Versioning guidelines.

Release version numbers will be incremented in the following format:

`<major>.<minor>.<patch>`

For detailed information on versioning you can visit the [wiki page](https://github.com/lightblueseas/mvn-parent-projects/wiki/Semantic-Versioning).

## Want to Help and improve it? ###

The source code for gradle-migration-data are on GitHub. Please feel free to fork and send pull requests!

Create your own fork of [astrapi69/gradle-migration-data/fork](https://github.com/astrapi69/gradle-migration-data/fork)

To share your changes, [submit a pull request](https://github.com/astrapi69/gradle-migration-data/pull/new/develop).

Don't forget to add new units tests on your changes.

## Contacting the Developers

Do not hesitate to contact the gradle-migration-data developers with your questions, concerns, comments, bug reports, or feature requests.
- Feature requests, questions and bug reports can be reported at the [issues page](https://github.com/astrapi69/gradle-migration-data/issues).

## Similar projects

## Credits

