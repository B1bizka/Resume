package bot

import bot.api.{BotUtils, ReminderBot}
import bot.config.AppConfig
import bot.dal.{ReminderSql, ReminderStorage}
import cats.effect.{ExitCode, IO, IOApp, Resource}
import doobie.util.transactor.Transactor
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.client.middleware.Logger
import telegramium.bots.high.{Api, BotApi}
import fs2.Stream

import scala.concurrent.duration._

object App extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    (for {

      config <- Resource.eval(AppConfig.load)

      transactor = Transactor.fromDriverManager[IO](
        "org.postgresql.Driver",
        config.dbConfig.url,
        config.dbConfig.user,
        config.dbConfig.password
      )

      reminderSql = ReminderSql.make
      reminderStorage = ReminderStorage.impl[IO](reminderSql, transactor)

      _ <- Resource.eval(
        BlazeClientBuilder[IO].resource
          .use { httpClient =>
            val http = Logger(logBody = false, logHeaders = false)(httpClient)
            implicit val api: Api[IO] = createBotBackend(http, config.telegramConfig.token)

            val botUtils = new BotUtils[IO](api)

            val botInstance = new ReminderBot[IO](reminderStorage, botUtils)

            Stream.awakeEvery[IO](1.minute)
              .evalMap(_ => botInstance.checkReminders)
              .compile
              .drain
              .start >> botInstance.start()
          }
      )
    } yield ()).useForever.as(ExitCode.Success)

  private def createBotBackend(http: org.http4s.client.Client[IO], token: String) =
    BotApi(http, baseUrl = s"https://api.telegram.org/bot$token")
}
