package bot.dal

import bot.domain.Reminder
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import doobie._
import doobie.implicits._
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class ReminderSqlSpec extends AnyFunSuite with Matchers with BeforeAndAfterAll {

  private val transactor: Transactor[IO] = Transactor.fromDriverManager[IO](
    driver = "org.h2.Driver",
    url = "jdbc:h2:mem:reminders;DB_CLOSE_DELAY=-1",
    user = "",
    pass = ""
  )

  private val reminderSql: ReminderSql = ReminderSql.make

  private def run[A](cio: ConnectionIO[A]): A = cio.transact(transactor).unsafeRunSync()

  override def beforeAll(): Unit = {
    val createTable: Update0 = sql"""
      CREATE TABLE reminders (
        id       BIGINT PRIMARY KEY AUTO_INCREMENT,
        date_time BIGINT NOT NULL,
        chat_id  BIGINT NOT NULL,
        message  VARCHAR NOT NULL
      )
    """.update
    run(createTable.run)
    super.beforeAll()
  }

  test("create should insert a reminder and return it with assigned id") {
    val reminder = Reminder(None, 1700000000L, 123L, "Test reminder")
    val resultEither = run(reminderSql.create(reminder))
    resultEither match {
      case Right(rem) =>
        rem.id.isDefined shouldBe true
        rem.chatId shouldBe 123L
        rem.message shouldBe "Test reminder"
      case Left(err) => fail(s"Expected Right, got Left($err)")
    }
  }

  test("findByChatId should return reminders for given chatId") {
    val reminder1 = Reminder(None, 1700000100L, 456L, "Reminder 1")
    val reminder2 = Reminder(None, 1700000200L, 456L, "Reminder 2")
    run(reminderSql.create(reminder1))
    run(reminderSql.create(reminder2))
    val reminders = run(reminderSql.findByChatId(456L))
    reminders.map(_.message) should contain allOf ("Reminder 1", "Reminder 2")
  }

  test("findDueReminders returns only reminders with date_time <= now") {
    val now = System.currentTimeMillis() / 1000
    val pastReminder = Reminder(None, now - 10, 789L, "Past reminder")
    val futureReminder = Reminder(None, now + 10, 789L, "Future reminder")
    run(reminderSql.create(pastReminder))
    run(reminderSql.create(futureReminder))
    val dueReminders = run(reminderSql.findDueReminders(now))
    dueReminders.map(_.message) should contain ("Past reminder")
    dueReminders.map(_.message) should not contain ("Future reminder")
  }

  test("deleteById deletes a reminder") {
    val reminder = Reminder(None, 1700000300L, 321L, "To be deleted")
    val created = run(reminderSql.create(reminder)).toOption.get
    val deleteCount = run(reminderSql.deleteById(created.id.get))
    deleteCount should be > 0
    val reminders = run(reminderSql.findByChatId(321L))
    reminders should not contain (created)
  }

  test("updateReminder updates a reminder") {
    val reminder = Reminder(None, 1700000400L, 555L, "Original message")
    val created = run(reminderSql.create(reminder)).toOption.get
    val newDateTime = created.dateTime + 1000
    val newMessage = "Updated message"
    val updateCount = run(reminderSql.updateReminder(created.id.get, newDateTime, newMessage))
    updateCount should be > 0
    val updatedReminder = run(reminderSql.findByChatId(555L)).find(_.id == created.id)
    updatedReminder.map(_.message) should contain (newMessage)
  }
}

