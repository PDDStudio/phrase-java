Phrase - Java and Android String Template *Engine*
==================================

Based upon [Phrase by Square](https://github.com/square/phrase).

A small String Template Library (10k) for Android and Java.

[![license](http://img.shields.io/badge/license-apache_2.0-red.svg?style=flat)](https://raw.githubusercontent.com/pddstudio/phrase-java/master/LICENSE.txt) [![](https://jitpack.io/v/PDDStudio/phrase-java.svg)](https://jitpack.io/#PDDStudio/phrase-java)

API
---

The usage is as simple as shown in the snippet below:

```java
CharSequence formatted = Phrase.from("Hi {first_name}, you are {age} years old.")
  .put("first_name", firstName)
  .put("age", age)
  .format();
```

Download
--------

You can download [the latest jar here](https://github.com/PDDStudio/phrase-java/releases).

You can also depend on the .jar through JitPack:

```xml
<!-- JitPack Repository -->
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>

<!-- Library Dependency -->
<dependency>
  <groupId>com.github.pddstudio</groupId>
  <artifactId>phrase</artifactId>
  <version>(insert latest version)</version>
</dependency>
```

or through Gradle:

```groovy

//top level build.gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}

//module dependencies
dependencies {
  compile 'com.github.pddstudio:phrase-java:(latest version)'
}
```

License
-------

	Copyright 2017 Patrick Jung
    Copyright 2013 Square, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
