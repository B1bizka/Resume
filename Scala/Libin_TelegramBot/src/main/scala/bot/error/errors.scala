package bot.error

import bot.error.errors.AppError
import cats.syntax.option._

object errors {

  sealed abstract class AppError(
                                  val message: String,
                                  val cause: Option[Throwable] = None
                                )

  object AppError {
    case class ReminderAlreadyExists() extends AppError("Reminder with the same date and chatId already exists")
    case class ReminderNotFound(chatId: Long, dateTime: String) extends AppError(s"Reminder for chatId $chatId at $dateTime not found")
    case class InternalError(
                              cause0: Throwable
                            ) extends AppError("Internal error", cause0.some)

  }

}

}
