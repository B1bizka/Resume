package bot.config

import cats.effect.IO
import pureconfig.ConfigSource
import pureconfig.generic.auto._



final case class AppConfig(dbConfig: DbConfig, telegramConfig: TelegramConfig)

object AppConfig {
  def load: IO[AppConfig] =
    IO.delay(AppConfig(DbConfig.load, TelegramConfig.load))
}

final case class DbConfig(url: String, user: String, password: String)

private object DbConfig {
  def load: DbConfig = ConfigSource.default.at("db").loadOrThrow[DbConfig]
}

final case class TelegramConfig(token: String)

private object TelegramConfig {
  def load: TelegramConfig = ConfigSource.default.at("telegram").loadOrThrow[TelegramConfig]
}
