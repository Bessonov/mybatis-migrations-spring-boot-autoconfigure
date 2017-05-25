
[![Project is](https://img.shields.io/badge/Project%20is-fantastic-ff69b4.svg)](https://github.com/Bessonov/mybatis-migrations-spring-boot-autoconfigure)
[![Build Status](https://travis-ci.org/Bessonov/mybatis-migrations-spring-boot-autoconfigure.svg?branch=master)](https://travis-ci.org/Bessonov/mybatis-migrations-spring-boot-autoconfigure)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.bessonov/mybatis-migrations-spring-boot-autoconfigure/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.bessonov/mybatis-migrations-spring-boot-autoconfigure/)
[![License](http://img.shields.io/:license-MIT-blue.svg)](https://raw.githubusercontent.com/Bessonov/mybatis-migrations-spring-boot-autoconfigure/master/LICENSE)

# Spring Boot Autoconfiguration and CLI for MyBatis Migrations

To activate autoconfiguration just add dependency from [maven central](https://maven-badges.herokuapp.com/maven-central/de.bessonov/mybatis-migrations-spring-boot-autoconfigure/).

## Usage

This project is intended to be used with [spring-boot executable fat jar](http://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-running-your-application.html#using-boot-running-as-a-packaged-application).

In distinction to MyBatis Migrations CLI there is no need for `drivers` and `environments` folders. Drivers are managed as maven dependencies and environments are configured through spring boot properties and profiles.

To place `scripts` folder in classpath (for example in `src/main/resources` folder) should be enough. You can use [migrations-maven-plugin](http://www.mybatis.org/migrations-maven-plugin/) to create migration scripts.

After you package your app with `mvn package`, app would like to migrate database on startup and then run itself.

Furthermore, you can use your app for controlled migrations. See full usage with:
```
java -jar target/app.jar migrations
```

and status of your database with:
```
java -jar target/app.jar migrations status
```

If you want to provide additional Spring or Java parameters, ensure to place migration commands last:
```
java -jar target/app.jar --spring.profiles.active=test migrations up
```

### Supported commands

Currently following commands are supported:

- status: Report the current state of the database
- bootstrap: Runs the bootstrap migration script
- up: Run unapplied migrations
- pending: Run pending migrations
- version: Migrate to specific version
- down: Runs the undo section of migration scripts

## Configuration defaults

Following defaults are provided by autoconfiguration:

```
mybatis.migrations:
  enabled: true
  path: classpath:scripts
  charset: UTF-8
  properties:
  cliCommand: migrations
  migrationCommand: [--no-exit, up]
```

MyBatis Migrations defaults are described by [DatabaseOperationOption.java](https://github.com/mybatis/migrations/blob/master/src/main/java/org/apache/ibatis/migration/options/DatabaseOperationOption.java).

## Parameters

`enabled`: is useful to disable migrations. For example to use Hibernate Schema Updates for development.

`path`: path to migrations scripts. This can be a physical location as well:
```
mybatis.migrations.path: file:/home/user/workspace/migration/scripts
```

`properties`: Additional properties for placeholder replacement in migration scripts:
```
mybatis.migrations:
  properties:
    myVariable: Hello world!
    anotherVariable: yes, I can!
```

`cliCommand`: which command is used for CLI. Useful on commands clash.

`migrationCommand`: which command should run before app is started.

## Use MyBatis Migrations in Tests

Just inject `SpringMyBatisMigrations` bean. For example:

```
@Autowired
private SpringMyBatisMigrations springMyBatisMigrations;

@Before
public void createDb() {
	springMyBatisMigrations.up(null);
}

@After
public void dropDb() {
	springMyBatisMigrations.down(2);
}
```

# License

The MIT License (MIT)

Copyright (c) 2017, Anton Bessonov

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
