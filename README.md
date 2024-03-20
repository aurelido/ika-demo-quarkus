# Ikaros Quarkus Demo

This is a minimal CRUD service exposing a couple of endpoints over REST,
with a front-end based on Angular so the alternatives can be evaluated from the browser.

While the code is simple, under the hood this is using:
### Development frameworks
- RESTEasy to expose the REST endpoints
- Hibernate ORM with Panache to perform the operations on an Oracle database. This includes to analyze the meta information extracted from the existing PL/SQL procedures allocated into the DB.
- A Oracle database; see below to run one via Docker
- ArC, the CDI inspired dependency injection tool with zero overhead
- The high performance Agroal connection pool
- Infinispan based caching
- All safely coordinated by the Narayana Transaction Manager
- Acceptance/Functional tests are implemented using RestAssured and Cucumber to verify the REST API and the Acceptance criteria.
- Rest of the tests Integration/Unit using JUnit5
  
### Test frameworks
- JUnit5 unit/integration testing
- RestAssured unit tests for JAX-RS endpoints
- Cucumber acceptance tests to verify the acceptance criteria

## Requirements

To compile and run this demo you will need:

- OpenJdk 17+
- Maven

In addition, you will need either an Oracle database, or Docker to run one.

## Building the demo

Launch the Maven build on the checked out sources of this demo:

```shell script
mvn package
```

## Running the demo

### Live coding with Quarkus

The Maven Quarkus plugin provides a development mode that supports
live coding. To try this out:

```shell script
mvn quarkus:dev
```

Navigate to:

<http://localhost:8080/index.html>

## Building Docker base images

The [`quarkus-docker`](quarkus-docker) module defines Docker base images for running JVM and native applications.
These base images need to be built once, before building the [`quarkus-example-app`](quarkus-example-app) image.
Go to this directory and build the Docker base images with:

    $ export QUARKUS_DATASOURCE_URL=jdbc:oracle:thin:@//$(hostname):1521/ORCLPDB1
    $ mvn package -Pdocker 

## Building and running from Docker

Go to the directory [`quarkus-example-app`](quarkus-example-app). Build the Docker image as follows:

    $ mvn package -Pdocker,oracle

Run the application in Docker from Maven:

    $ mvn docker:run -Pdocker

To run the application in Docker from the command-line:

    $ export QUARKUS_DATASOURCE_URL=jdbc:oracle:thin:@//$(hostname):1521/ORCLPDB1
    $ docker run --rm -it -p 8080:8080 -e QUARKUS_DATASOURCE_URL=${QUARKUS_DATASOURCE_URL} quarkus/quarkus-example-app


### Run Quarkus in JVM mode

When you're done iterating in developer mode, you can run the application as a
conventional jar file.

First compile it:

> $ mvn package

Next we need to make sure you have a PostgreSQL instance running (Quarkus automatically starts one for dev and test mode). To set up a PostgreSQL database with Docker:

> docker run -it --rm=true --name quarkus_test -e POSTGRES_USER=quarkus_test -e POSTGRES_PASSWORD=quarkus_test -e POSTGRES_DB=quarkus_test -p 5432:5432 postgres:13.3

Connection properties for the Agroal datasource are defined in the standard Quarkus configuration file,
`src/main/resources/application.properties`.

Then run it:

> java -jar ./target/quarkus-app/quarkus-run.jar

    Have a look at how fast it boots.
    Or measure total native memory consumption...

### Run Quarkus as a native application

You can also create a native executable from this application without making any
source code changes. A native executable removes the dependency on the JVM:
everything needed to run the application on the target platform is included in
the executable, allowing the application to run with minimal resource overhead.

Compiling a native executable takes a bit longer, as GraalVM performs additional
steps to remove unnecessary codepaths. Use the  `native` profile to compile a
native executable:

> $ mvn package -Dnative

After getting a cup of coffee, you'll be able to run this binary directly:

> ./target/hibernate-orm-panache-quickstart-1.0.0-SNAPSHOT-runner

    Please brace yourself: don't choke on that fresh cup of coffee you just got.
    
    Now observe the time it took to boot, and remember: that time was mostly spent to generate the tables in your database and import the initial data.
    
    Next, maybe you're ready to measure how much memory this service is consuming.

N.B. This implies all dependencies have been compiled to native;
that's a whole lot of stuff: from the bytecode enhancements that Panache
applies to your entities, to the lower level essential components such as the PostgreSQL JDBC driver, the Undertow webserver.

## See the demo in your browser




## Running the demo in Kubernetes

This section provides extra information for running both the database and the demo on Kubernetes.
As well as running the DB on Kubernetes, a service needs to be exposed for the demo to connect to the DB.

Then, rebuild demo docker image with a system property that points to the DB.

```bash
-Dquarkus.datasource.jdbc.url=jdbc:postgresql://<DB_SERVICE_NAME>/quarkus_test
```

# Docker Compose example

The directory [docker-compose](docker-compose) contains a Docker Compose configuration to run a containerized application
and Oracle database.

## Prerequisites

First build an Oracle container image as described in [https://github.com/oracle/docker-images/tree/master/OracleDatabase/SingleInstance](https://github.com/oracle/docker-images/tree/master/OracleDatabase/SingleInstance).
For Oracle Database 19.3.0 Enterprise Edition this involves the following steps:

1. Place `LINUX.X64_193000_db_home.zip` in `dockerfiles/19.3.0`.
2. Go to `dockerfiles` and run `buildDockerImage.sh -v 19.3.0 -e`

## Build the application

Go to directory [`quarkus-example-app`](quarkus-example-app) and build the application image:

     $ mvn package -Pdocker,oracle

## Start Oracle and build the database

Go to the directory [`docker-compose`](docker-compose). First start the database container:

    $ docker-compose up -d oracledb
    $ docker-compose logs -f oracledb

Follow the log file and wait until the database is ready to use.

## Running the application:

When the database is ready start the application container:

    $ docker-compose up -d  example-app
    $ docker-compose logs -f example-app






# Ikaros Demo: Hibernate ORM with Panache and RESTEasy.

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
$ mvn compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
$ mvn package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
$ mvn package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
$ mvn package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
$ mvn package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/code-with-quarkus-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Related Guides

- Hibernate Validator ([guide](https://quarkus.io/guides/validation)): Validate object properties (field, getter) and method parameters for your beans (REST, CDI, Jakarta Persistence)
- SmallRye OpenAPI ([guide](https://quarkus.io/guides/openapi-swaggerui)): Document your REST APIs with OpenAPI - comes with Swagger UI
- Hibernate ORM with Panache ([guide](https://quarkus.io/guides/hibernate-orm-panache)): Simplify your persistence code for Hibernate ORM via the active record or the repository pattern
- RESTEasy Classic ([guide](https://quarkus.io/guides/resteasy)): REST endpoint framework implementing Jakarta REST and more
- SmallRye JWT ([guide](https://quarkus.io/guides/security-jwt)): Secure your applications with JSON Web Token
- Liquibase ([guide](https://quarkus.io/guides/liquibase)): Handle your database schema migrations with Liquibase
- SmallRye Health ([guide](https://quarkus.io/guides/smallrye-health)): Monitor service health
- Cucumber ([guide](https://quarkiverse.github.io/quarkiverse-docs/quarkus-cucumber/dev/index.html)): Run tests using Cucumber
- JDBC Driver - Oracle ([guide](https://quarkus.io/guides/datasource)): Connect to the Oracle database via JDBC

## Provided Code

### Hibernate ORM

Create your first JPA entity

[Related guide section...](https://quarkus.io/guides/hibernate-orm)

[Related Hibernate with Panache section...](https://quarkus.io/guides/hibernate-orm-panache)


### RESTEasy JAX-RS

Easily start your RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)

### SmallRye Health

Monitor your application's health using SmallRye Health

[Related guide section...](https://quarkus.io/guides/smallrye-health)
