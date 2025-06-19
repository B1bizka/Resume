package bot.dal

import bot.error.errors.AppError
import doobie._
import doobie.implicits._
import cats.syntax.either._
import bot.domain.Reminder

trait ReminderSql {
  def findByChatId(chatId: Long): ConnectionIO[List[Reminder]]
  def findDueReminders(now: Long): ConnectionIO[List[Reminder]]
  def findActiveReminders(chatId: Long, now: Long): ConnectionIO[List[Reminder]]
  def create(reminder: Reminder): ConnectionIO[Either[AppError, Reminder]]
  def deleteById(id: Long): ConnectionIO[Int]
  def updateReminder(id: Long, newDateTime: Long, newMessage: String): ConnectionIO[Int]
}

object ReminderSql {

  private object sqls {

    def findByChatIdSql(chatId: Long): Query0[Reminder] =
      sql"""
           SELECT id, date_time, chat_id, message
           FROM reminders
           WHERE chat_id = $chatId
         """.query[Reminder]

    def findDueRemindersSql(now: Long): Query0[Reminder] =
      sql"""
           SELECT id, date_time, chat_id, message
           FROM reminders
           WHERE date_time <= $now
         """.query[Reminder]

    def findActiveRemindersSql(chatId: Long, now: Long): Query0[Reminder] =
      sql"""
           SELECT id, date_time, chat_id, message
           FROM reminders
           WHERE chat_id = $chatId AND date_time > $now
         """.query[Reminder]

    def insertSql(reminder: Reminder): ConnectionIO[Long] =
      sql"""
    INSERT INTO reminders (date_time, chat_id, message)
    VALUES (${reminder.dateTime}, ${reminder.chatId}, ${reminder.message})
  """.update
        .withGeneratedKeys[Long]("id")
        .compile
        .lastOrError


    def deleteByIdSql(id: Long): Update0 =
      sql"""
           DELETE FROM reminders
           WHERE id = $id
         """.update

    def updateReminderSql(id: Long, newDateTime: Long, newMessage: String): Update0 =
      sql"""
           UPDATE reminders
           SET date_time = $newDateTime, message = $newMessage
           WHERE id = $id
         """.update
  }

  private final class Impl extends ReminderSql {
    import sqls._

    override def findByChatId(chatId: Long): ConnectionIO[List[Reminder]] =
      findByChatIdSql(chatId).to[List]

    override def findDueReminders(now: Long): ConnectionIO[List[Reminder]] =
      findDueRemindersSql(now).to[List]

    override def findActiveReminders(chatId: Long, now: Long): ConnectionIO[List[Reminder]] =
      findActiveRemindersSql(chatId, now).to[List]

    override def create(reminder: Reminder): ConnectionIO[Either[AppError, Reminder]] =
      insertSql(reminder)
        .attemptSql
        .map(_.leftMap(AppError.InternalError)
          .map(id => reminder.copy(id = Some(id))))

    override def deleteById(id: Long): ConnectionIO[Int] =
      deleteByIdSql(id).run

    override def updateReminder(id: Long, newDateTime: Long, newMessage: String): ConnectionIO[Int] =
      updateReminderSql(id, newDateTime, newMessage).run
  }

  def make: ReminderSql = new Impl
}

