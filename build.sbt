name := "meetup"

version := "1.0"

scalaVersion := "2.11.8"

scalacOptions ++= Seq(
  "-language:postfixOps",
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-Ywarn-unused-import",
  "-unchecked",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Xfuture",
  "-Xlint"
)

scalacOptions in(Compile, console) ~= (_ filterNot (_ == "-Ywarn-unused-import"))

scalacOptions in(Test, console) := (scalacOptions in(Compile, console)).value

resolvers ++= Seq(
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases",
  "Twitter repo" at "http://maven.twttr.com/",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Mesosphere Public Repository" at "http://downloads.mesosphere.io/maven",
  Resolver.bintrayRepo("websudos", "oss-releases")
)

libraryDependencies ++= Seq(
  "com.twitter" %% "finagle-http" % "6.39.0",
  "com.twitter" %% "finagle-stats" % "6.39.0",
  "com.twitter" %% "util-eval" % "6.38.0",
  "com.twitter" %% "twitter-server" % "1.24.0",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.4.4",
  "mesosphere" %% "jackson-case-class-module" % "0.1.2",
  "com.fasterxml.jackson.datatype" % "jackson-datatype-joda" % "2.4.4",
  "io.zipkin.finagle" %% "zipkin-finagle-http" % "0.3.0",
  "ch.qos.logback" % "logback-classic" % "1.1.7"
)

//test
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)

coverageMinimum := 80
coverageFailOnMinimum := true

addCommandAlias("test", "testQuick")

addCommandAlias("devrun", "~re-start")

addCommandAlias("cov", "; clean; coverage; test")

Revolver.settings
