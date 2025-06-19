package bot.api

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import cats.effect.IO

class BotUtilsSpec extends AnyFunSuite with Matchers {

  val botUtils = new BotUtils[IO](null)

  test("parseToTimestamp and formatTimestamp work correctly using system default zone") {
    val dateStr = "2025-03-04"
    val timeStr = "06:30"
    val ts = botUtils.parseToTimestamp(dateStr, timeStr)
    val formatted = botUtils.formatTimestamp(ts)
    formatted shouldBe s"$dateStr 06:30"
  }

  test("parseToTimestamp normalizes single-digit hour (e.g. '6:5' -> '06:05')") {
    val dateStr = "2025-03-04"
    val timeStr = "6:5"
    val ts = botUtils.parseToTimestamp(dateStr, timeStr)
    val formatted = botUtils.formatTimestamp(ts)
    formatted shouldBe s"$dateStr 06:05"
  }

  test("parseToTimestamp returns 0 for invalid date/time") {
    val invalidDate = "not-a-date"
    val invalidTime = "25:99"
    val ts = botUtils.parseToTimestamp(invalidDate, invalidTime)
    ts shouldBe 0L
  }

  test("parseToTimestamp -> formatTimestamp cycle produces the same date/time") {
    val dateStr = "2025-07-15"
    val timeStr = "07:45"
    val ts = botUtils.parseToTimestamp(dateStr, timeStr)
    val formatted = botUtils.formatTimestamp(ts)
    formatted shouldBe s"$dateStr 07:45"
  }
}
