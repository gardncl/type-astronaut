name := "shapelessplayground"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "com.chuusai" %% "shapeless" % "2.3.2"
)

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)

fork in run := true