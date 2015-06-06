name := "scala-streams"

version := "1.0"

scalaVersion := "2.11.4"

resolvers ++= Seq(Resolver.sonatypeRepo("releases"), 
		  Resolver.sonatypeRepo("snapshots"))

libraryDependencies ++= Seq(
  "org.scalacheck" %% "scalacheck" % "1.11.6" % "test",
  "org.scala-lang" % "scala-reflect" % "2.11.4"
//"com.github.biboudis" % "jmh-profilers" % "0.1.2"
)

scalacOptions ++= Seq("-optimise",
		      "-Yclosure-elim",
		      "-Yinline",
		      "-Yinline-warnings")

// javaOptions in run ++= Seq("-Xmx3G", "-Xms3G", "-XX:+TieredCompilation", "-XX:+UseParallelGC")

enablePlugins(JmhPlugin)

javaOptions in run ++= Seq("-Xms2G", "-Xmx2G")

testOptions in Test += Tests.Argument(TestFrameworks.ScalaCheck, 
				      "-maxSize", "5", 
				      "-minSuccessfulTests", "100", 
				      "-workers", "1", 
				      "-verbosity", "1")

ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }

// miniboxing:
libraryDependencies += "org.scala-miniboxing.plugins" %% "miniboxing-runtime" % "0.4-M4"

// addCompilerPlugin("org.scala-miniboxing.plugins" %% "miniboxing-plugin" % "0.4-M4")

// Generic
// scalacOptions ++= Seq("-no-specialization")

// Specialized
// no flags

// Miniboxed
// scalacOptions ++= Seq("-P:minibox:mark-all", "-P:minibox:Ykeep-functionX-values")

// Miniboxed+functions
// scalacOptions ++= Seq("-P:minibox:mark-all")
