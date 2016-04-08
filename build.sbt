name := """play-scala-intro-moni"""

version := "1.0-SNAPSHOT"

inConfig(Compile)(mappings in packageBin <++= Defaults.sourceMappings)

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers += "Scalaz Bintray Repo" at "https://dl.bintray.com/scalaz/releases"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "1.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "1.0.0",
  "com.h2database" % "h2" % "1.4.177",
  "com.typesafe.cinnamon" %% "cinnamon-takipi" % "1.2.2",
  "com.typesafe.cinnamon" %% "cinnamon-slf4j-mdc" % "1.2.2",
  specs2 % Test
)     

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

bashScriptExtraDefines += """addJava -agentlib:TakipiAgent"""
