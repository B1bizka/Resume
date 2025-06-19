package bot.api

import telegramium.bots.high.{Api, Methods}
import telegramium.bots._
import cats.effect.Async
import cats.implicits.toFunctorOps
import java.time.{Instant, ZoneId, LocalDateTime}
import java.time.format.DateTimeFormatter

class BotUtils[F[_]: Async](bot: Api[F]) {

  private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")


  def sendMessage(chatId: Long, text: String): F[Unit] =
    bot.execute(Methods.sendMessage(ChatIntId(chatId), text)).void


  def formatTimestamp(timestamp: Long): String = {
    val dateTime = Instant.ofEpochSecond(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime
    dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
  }

  def parseToTimestamp(dateStr: String, timeStr: String): Long = {
    try {
      val normalizedTime = timeStr.split(":") match {
        case Array(h, m) =>
          val paddedH = if (h.length == 1) s"0$h" else h
          val paddedM = if (m.length == 1) s"0$m" else m
          s"$paddedH:$paddedM"
        case _ => timeStr
      }

      val normalizedDate = dateStr.split("-") match {
        case Array(y, m, d) =>
          val year = y.toInt
          val month = m.toInt
          val day = d.toInt
          f"$year%04d-$month%02d-$day%02d"
        case _ => dateStr
      }

      val dateTimeString = s"$normalizedDate $normalizedTime"
      val parsedDate = LocalDateTime.parse(dateTimeString, dateTimeFormatter)

      parsedDate.atZone(ZoneId.systemDefault()).toEpochSecond
    } catch {
      case _: Exception => 0L
    }
  }

}

