import sbt._
import Keys._
import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform

lazy val buildSettings = Seq(
  version := "0.0.1",
  scalaVersion := "2.12.1",
  organization := "com.xperiall",
  headerLicense := Some(HeaderLicense.MPLv2_NoCopyright),
  scalacOptions ++= Seq(
    "-unchecked",
    "-deprecation",
    "-encoding",
    "utf8",
    "-feature",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-Ydelambdafy:method",
    "-target:jvm-1.8"
  ),
  resolvers ++= Seq(
    "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/",
    Resolver.sonatypeRepo("snapshots"),
    Resolver.sonatypeRepo("releases"),
    Resolver.bintrayRepo("hseeberger", "maven")
  )
)

lazy val akkaV = "2.5.19"
lazy val akkaHttpV = "10.1.5"
lazy val akkaCors = "0.3.3"
lazy val circeV = "0.10.0"
lazy val akkaCirceV = "1.22.0"
lazy val scalaTestV = "3.0.5"
lazy val logbackV = "1.2.3"
lazy val catsVersion = "1.5.0"
lazy val gcloudSpeechVersion = "0.74.0-beta"
lazy val swaggerVersion = "2.0.0"
lazy val `template` = project
  .in(file("."))
  .settings(buildSettings: _*)
  .settings(mainClass in assembly := Some(
    "com.xperiall.http.server.Server"))
  .settings(
    name := "voice-recognition-api",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % akkaHttpV,
      "com.typesafe.akka" %% "akka-slf4j" % akkaV,
      "de.heikoseeberger" %% "akka-http-circe" % akkaCirceV,
      "ch.megard" %% "akka-http-cors" % akkaCors,
      "ch.qos.logback" % "logback-classic" % logbackV,
      "io.circe" %% "circe-core" % circeV,
      "io.circe" %% "circe-generic" % circeV,
      "io.circe" %% "circe-jawn" % circeV,
      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV % "test",
      "org.scalatest" %% "scalatest" % scalaTestV % "test",
      "com.google.cloud" % "google-cloud-speech" % gcloudSpeechVersion,
      "javax.ws.rs" % "javax.ws.rs-api" % "2.0.1",
      "org.typelevel" %% "cats-core" % catsVersion,
      "com.github.swagger-akka-http" %% "swagger-akka-http" % swaggerVersion,
      "com.github.swagger-akka-http" %% "swagger-scala-module" % "2.0.2"
    )
  )
  .enablePlugins(AutomateHeaderPlugin)

SbtScalariform.autoImport.scalariformPreferences := SbtScalariform.autoImport.scalariformPreferences.value
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 100)
  .setPreference(DoubleIndentConstructorArguments, true)
  .setPreference(RewriteArrowSymbols, true)

wartremoverWarnings ++= Warts.unsafe

//Test specific configuration
test in assembly := {}
parallelExecution in Test := false
fork in Test := true

//docker container info

mainClass in Compile := Some("com.xperiall.http.server.Server")
enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)