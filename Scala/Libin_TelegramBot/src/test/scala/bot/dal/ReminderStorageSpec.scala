package bot.dal

import bot.domain.Reminder
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import doobie._
import doobie.implicits._
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class ReminderStorageSpec extends AnyFunSuite with Matchers with BeforeAndAfterAll {

  private val transactor: Transactor[IO] = Transactor.fromDriverManager[IO](
    driver = "org.h2.Driver",
    url = "jdbc:h2:mem:reminderStorage;DB_CLOSE_DELAY=-1",
    user = "",
    pass = ""
  )

  private val reminderSql: ReminderSql = ReminderSql.make
  private val reminderStorage: ReminderStorage[IO] = ReminderStorage.impl[IO](reminderSql, transactor)

  private def runInit(): ConnectionIO[Int] =
    sql"""
      CREATE TABLE reminders (
        id       BIGINT PRIMARY KEY AUTO_INCREMENT,
        date_time BIGINT NOT NULL,
        chat_id  BIGINT NOT NULL,
        message  TEXT NOT NULL
      )
    """.update.run

  override def beforeAll(): Unit = {
    runInit().transact(transactor).unsafeRunSync() shouldBe 0
    super.beforeAll()
  }

  test("create creates a reminder and returns it") {
    val reminder = Reminder(None, 1700000500L, 111L, "Storage test reminder")
    val created = reminderStorage.create(reminder).unsafeRunSync()
    created.chatId shouldBe 111L
    created.message shouldBe "Storage test reminder"
    created.id.isDefined shouldBe true
  }

  test("findByChatId returns reminders for a chat") {
    val reminder1 = Reminder(None, 1700000600L, 222L, "Reminder A")
    val reminder2 = Reminder(None, 1700000700L, 222L, "Reminder B")
    reminderStorage.create(reminder1).unsafeRunSync()
    reminderStorage.create(reminder2).unsafeRunSync()
    val list = reminderStorage.findByChatId(222L).unsafeRunSync()
    list.map(_.message) should contain allOf ("Reminder A", "Reminder B")
  }

  test("findDueReminders returns only due reminders") {
    val now = System.currentTimeMillis() / 1000
    val pastReminder = Reminder(None, now - 5, 333L, "Due reminder")
    val futureReminder = Reminder(None, now + 5, 333L, "Not due")
    reminderStorage.create(pastReminder).unsafeRunSync()
    reminderStorage.create(futureReminder).unsafeRunSync()
    val due = reminderStorage.findDueReminders(now).unsafeRunSync()
    due.map(_.message) should contain ("Due reminder")
    due.map(_.message) should not contain ("Not due")
  }

  test("deleteById returns true if reminder is deleted, false otherwise") {
    val reminder = Reminder(None, 1700000800L, 444L, "To delete")
    val created = reminderStorage.create(reminder).unsafeRunSync()
    val deleted = reminderStorage.deleteById(created.id.get).unsafeRunSync()
    deleted shouldBe true

    val deletedAgain = reminderStorage.deleteById(created.id.get).unsafeRunSync()
    deletedAgain shouldBe false
  }

  test("update returns true if update is successful, false if reminder does not exist") {
    val reminder = Reminder(None, 1700000900L, 555L, "To update")
    val created = reminderStorage.create(reminder).unsafeRunSync()
    val newDateTime = created.dateTime + 1000
    val updated = reminderStorage.update(created.id.get, newDateTime, "Updated text").unsafeRunSync()
    updated shouldBe true

    val nonExistentUpdate = reminderStorage.update(9999L, newDateTime, "Text").unsafeRunSync()
    nonExistentUpdate shouldBe false
  }
}

