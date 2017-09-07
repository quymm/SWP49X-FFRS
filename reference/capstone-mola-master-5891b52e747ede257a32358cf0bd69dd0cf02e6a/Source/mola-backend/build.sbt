name := "mola"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.11"

libraryDependencies += javaJpa
libraryDependencies += "org.hibernate" % "hibernate-core" % "5.2.5.Final"

libraryDependencies += javaWs % "test"

libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % "test"
libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % "test"
libraryDependencies += "org.mockito" % "mockito-core" % "2.1.0" % "test"
testOptions in Test += Tests.Argument(TestFrameworks.JUnit, "-a", "-v")
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.36"
libraryDependencies += "org.apache.shiro" % "shiro-all" % "1.4.0"
libraryDependencies += "commons-beanutils" % "commons-beanutils" % "1.9.2"

// For upload file
libraryDependencies += "org.apache.james" % "apache-mime4j" % "0.3"
libraryDependencies += "commons-io" % "commons-io" % "1.4"
libraryDependencies += "commons-logging" % "commons-logging" % "1.1.1"
libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.1-alpha1"
libraryDependencies += "org.apache.httpcomponents" % "httpcore" % "4.1-alpha1"
libraryDependencies += "org.apache.httpcomponents" % "httpmime" % "4.0-alpha4"

// Hibernate Lucena fulltext search
libraryDependencies += "org.hibernate" % "hibernate-search-orm" % "5.7.1.Final"
// https://mvnrepository.com/artifact/com.nimbusds/nimbus-jose-jwt
libraryDependencies += "com.nimbusds" % "nimbus-jose-jwt" % "4.36"
libraryDependencies ++= Seq(
  javaWs
)

// Redis
// include play-redis library
libraryDependencies += "com.typesafe.play.modules" %% "play-modules-redis" % "2.5.0"
resolvers += "google-sedis-fix" at "http://pk11-scratch.googlecode.com/svn/trunk"