ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.16"

val catsEffectVersion = "3.5.2"
val telegramiumVersion = "9.77.0"
val http4sVersion = "0.23.23"
val doobieVersion = "1.0.0-RC2"

lazy val root = (project in file("."))
  .settings(
    name := "Libin_TelegramBot",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect" % catsEffectVersion,
      "io.github.apimorphism" %% "telegramium-core" % telegramiumVersion,
      "io.github.apimorphism" %% "telegramium-high" % telegramiumVersion,
      "org.http4s" %% "http4s-ember-client" % http4sVersion,
      "org.tpolecat" %% "doobie-core"     % doobieVersion,
      "org.tpolecat" %% "doobie-hikari"   % doobieVersion,
      "org.tpolecat" %% "doobie-postgres" % doobieVersion,
      "org.tpolecat" %% "doobie-scalatest" % "1.0.0-RC2" % Test,
      "org.postgresql" % "postgresql"     % "42.7.2",
      "com.github.pureconfig" %% "pureconfig" % "0.17.4",
      "org.tpolecat" %% "doobie-h2" % doobieVersion,

      "tf.tofu" %% "tofu-logging" % "0.12.0.1",
      "tf.tofu" %% "tofu-logging-derivation" % "0.12.0.1",
      "tf.tofu" %% "tofu-logging-layout" % "0.12.0.1",
      "tf.tofu" %% "tofu-logging-logstash-logback" % "0.12.0.1",
      "tf.tofu" %% "tofu-logging-structured" % "0.12.0.1",
      "tf.tofu" %% "tofu-core-ce3" % "0.12.0.1",
      "tf.tofu" %% "tofu-doobie-logging-ce3" % "0.12.0.1"
    )

  )
