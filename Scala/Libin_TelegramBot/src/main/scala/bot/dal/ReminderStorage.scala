package bot.dal

import doobie._
import doobie.implicits._
import cats.effect._
import cats.syntax.all._
import bot.domain.Reminder

trait ReminderStorage[F[_]] {
  def findByChatId(chatId: Long): F[List[Reminder]]
  def findDueReminders(now: Long): F[List[Reminder]]
  def findActiveReminders(chatId: Long, now: Long): F[List[Reminder]]
  def create(reminder: Reminder): F[Reminder]
  def deleteById(id: Long): F[Boolean]
  def update(id: Long, newDateTime: Long, newMessage: String): F[Boolean]
}

object ReminderStorage {
  def impl[F[_]: Async](
                         reminderSql: ReminderSql,
                         transactor: Transactor[F]
                       ): ReminderStorage[F] = new ReminderStorage[F] {

    override def findByChatId(chatId: Long): F[List[Reminder]] =
      reminderSql.findByChatId(chatId).transact(transactor)

    override def findDueReminders(now: Long): F[List[Reminder]] =
      reminderSql.findDueReminders(now).transact(transactor)

    override def findActiveReminders(chatId: Long, now: Long): F[List[Reminder]] =
      reminderSql.findActiveReminders(chatId, now).transact(transactor)

    override def create(reminder: Reminder): F[Reminder] =
      reminderSql.create(reminder).transact(transactor).flatMap {
        case Right(rem) => Async[F].pure(rem)
        case Left(error) => Async[F].raiseError(new Exception(error.message))
      }

    override def deleteById(id: Long): F[Boolean] =
      reminderSql.deleteById(id).transact(transactor).map(_ > 0)


    override def update(id: Long, newDateTime: Long, newMessage: String): F[Boolean] =
      reminderSql.updateReminder(id, newDateTime, newMessage).transact(transactor).map(_ > 0)
  }
}

