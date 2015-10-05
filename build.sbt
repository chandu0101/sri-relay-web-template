enablePlugins(ScalaJSPlugin)

name := "sri-relay-web-template"

scalaVersion := "2.11.7"

val sriVersion = "0.1.0-SNAPSHOT"

libraryDependencies ++= Seq("com.github.chandu0101.sri" %%% "web" % sriVersion,
  "com.github.chandu0101.sri" %%% "relay" % sriVersion)


  val relayWebExamplesAssets = "assets/"

  crossTarget in(Compile, fullOptJS) := file(relayWebExamplesAssets)
    crossTarget in(Compile, fastOptJS) := file(relayWebExamplesAssets)
    crossTarget in(Compile, packageScalaJSLauncher) := file(relayWebExamplesAssets)
    artifactPath in(Compile, fastOptJS) := ((crossTarget in(Compile, fastOptJS)).value /
      ((moduleName in fastOptJS).value + "-opt.js"))
  
