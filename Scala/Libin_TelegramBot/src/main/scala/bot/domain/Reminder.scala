package bot.domain

import doobie._


case class Reminder(
                     id: Option[Long],
                     dateTime: Long,
                     chatId: Long,
                     message: String
                   )

object Reminder {

  implicit val longAsTimestampMeta: Meta[Long] =
    Meta[BigDecimal].timap(_.toLong)(BigDecimal.valueOf)


  implicit val reminderRead: Read[Reminder] =
    Read[(Option[Long], Long, Long, String)].map {
      case (id, dateTime, chatId, msg) => Reminder(id, dateTime, chatId, msg)
    }


  implicit val reminderWrite: Write[Reminder] =
    Write[(Option[Long], Long, Long, String)].contramap { reminder =>
      (reminder.id, reminder.dateTime, reminder.chatId, reminder.message)
    }
}
