package bot.api

import bot.api.{BotUtils, MenuHandler}
import bot.dal.ReminderStorage
import bot.domain.Reminder
import cats.effect.{Async, Ref, Temporal}
import cats.Parallel
import telegramium.bots.high._
import telegramium.bots._
import cats.syntax.all._



class ReminderBot[F[_]: Async: Parallel: Temporal](
                                                    reminderStorage: ReminderStorage[F],
                                                    botUtils: BotUtils[F]
                                                  )(implicit bot: Api[F]) extends LongPollBot[F](bot) {

  private val pendingEdits: Ref[F, Map[Long, Int]] =
    Ref.unsafe(Map.empty[Long, Int])

  override def onMessage(msg: Message): F[Unit] = {
    msg.text match {
      case Some(text) =>
        pendingEdits.get.flatMap { edits =>
          edits.get(msg.chat.id) match {
            case Some(reminderIndex) =>
              val parts = text.split(" ", 3)
              if (parts.length < 3) {
                bot.execute(
                  Methods.sendMessage(
                    ChatIntId(msg.chat.id),
                    "❌ Неверный формат редактирования! Формат: YYYY-MM-DD HH:MM ТЕКСТ"
                  )
                ).void
              } else {
                val dateStr = parts(0)
                val timeStr = parts(1)
                val newMessage = parts(2)
                val normalizedTime = timeStr.split(":") match {
                  case Array(h, m) =>
                    val paddedH = if (h.length == 1) s"0$h" else h
                    val paddedM = if (m.length == 1) s"0$m" else m
                    s"$paddedH:$paddedM"
                  case _ => timeStr
                }
                try {
                  val newDateTime = botUtils.parseToTimestamp(dateStr, normalizedTime)
                  editReminder(msg.chat.id, reminderIndex, s"$dateStr $normalizedTime", newMessage) >>
                    pendingEdits.update(_ - msg.chat.id)
                } catch {
                  case _: Exception =>
                    bot.execute(
                      Methods.sendMessage(
                        ChatIntId(msg.chat.id),
                        "❌ Ошибка при парсинге даты и времени. Формат: YYYY-MM-DD HH:MM"
                      )
                    ).void
                }
              }

            case None =>
              text match {
                case "/start" =>
                  sendMainMenu(msg.chat.id)
                case t if t.startsWith("/remind") =>
                  handleRemindCommand(msg)
                case t if t.startsWith("/delete") =>
                  handleDeleteCommand(msg)
                case t if t.startsWith("/edit") =>
                  val parts = t.stripPrefix("/edit").trim.split(" ", 2)
                  if (parts.isEmpty || parts(0).isEmpty) {
                    bot.execute(
                      Methods.sendMessage(
                        ChatIntId(msg.chat.id),
                        "❌ Укажите номер напоминания для изменения. Пример: /edit 1"
                      )
                    ).void
                  } else {
                    val reminderIndex = parts(0).toIntOption.getOrElse(0)
                    if (reminderIndex <= 0) {
                      bot.execute(
                        Methods.sendMessage(
                          ChatIntId(msg.chat.id),
                          "❌ Неверный номер напоминания!"
                        )
                      ).void
                    } else {
                      askForReminderEdit(msg.chat.id, reminderIndex)
                    }
                  }
                case _ =>
                  ().pure[F]
              }
          }
        }
      case None => ().pure[F]
    }
  }

  override def onCallbackQuery(query: CallbackQuery): F[Unit] = {
    query.data match {
      case Some("add_reminder") =>
        askForTextReminder(query.from.id)

      case Some("active_reminders") =>
        showActiveReminders(query.from.id)

      case Some("delete_reminder") =>
        askForReminderDeletion(query.from.id)

      case Some("edit_reminders") =>
        askForReminderEdit(query.from.id)

      case Some(reminderId) if reminderId.startsWith("delete_") =>
        val reminderIndexOption = reminderId.stripPrefix("delete_").toIntOption
        reminderIndexOption match {
          case Some(reminderIndex) =>
            deleteReminder(query.from.id, reminderIndex)
          case None =>
            bot.execute(
              Methods.sendMessage(
                ChatIntId(query.from.id),
                "❌ Неверный номер напоминания!"
              )
            ).void
        }

      case Some(reminderId) if reminderId.startsWith("edit_") =>
        val reminderIndexOption = reminderId.stripPrefix("edit_").toIntOption
        reminderIndexOption match {
          case Some(reminderIndex) =>
            pendingEdits.update(_.updated(query.from.id, reminderIndex)) >>
              bot.execute(
                Methods.sendMessage(
                  ChatIntId(query.from.id),
                  s"🔄 Вы выбрали напоминание с индексом $reminderIndex.\nВведите новое время и текст в формате:\nYYYY-MM-DD HH:MM ТЕКСТ"
                )
              ).void
          case None =>
            bot.execute(
              Methods.sendMessage(
                ChatIntId(query.from.id),
                "❌ Неверный номер напоминания!"
              )
            ).void
        }

      case _ =>
        ().pure[F]
    }
  }

  private def handleRemindCommand(msg: Message): F[Unit] = {
    val parts = msg.text.get.stripPrefix("/remind").trim.split(" ", 3)
    if (parts.length < 3) {
      bot.execute(
        Methods.sendMessage(
          ChatIntId(msg.chat.id),
          "❌ Неверный формат!\nИспользуйте: /remind YYYY-MM-DD HH:MM ТЕКСТ"
        )
      ).void
    } else {
      val dateStr = parts(0)
      val timeStr = parts(1)
      val text = parts(2)
      val normalizedTime = timeStr.split(":") match {
        case Array(h, m) if h.length == 1 => s"0$h:$m"
        case _ => timeStr
      }
      try {
        val dateTime = botUtils.parseToTimestamp(dateStr, normalizedTime)
        val now = System.currentTimeMillis() / 1000
        if (dateTime <= now) {
          bot.execute(
            Methods.sendMessage(
              ChatIntId(msg.chat.id),
              "⚠ Напоминание не может быть в прошлом или в текущее время. Укажите время в будущем!"
            )
          ).void
        } else {
          reminderStorage.create(Reminder(None, dateTime, msg.chat.id, text)) >>
            bot.execute(
              Methods.sendMessage(
                ChatIntId(msg.chat.id),
                s"🔔 Напоминание установлено на ${botUtils.formatTimestamp(dateTime)}!"
              )
            ).void
        }
      } catch {
        case _: Exception =>
          bot.execute(
            Methods.sendMessage(
              ChatIntId(msg.chat.id),
              "❌ Ошибка! Убедитесь, что вводите дату и время корректно.\nФормат: YYYY-MM-DD HH:MM"
            )
          ).void
      }
    }
  }

  private def handleDeleteCommand(msg: Message): F[Unit] = {
    val parts = msg.text.get.stripPrefix("/delete").trim.split(" ", 2)
    if (parts.isEmpty || parts(0).isEmpty) {
      bot.execute(
        Methods.sendMessage(
          ChatIntId(msg.chat.id),
          "❌ Укажите номер напоминания для удаления.\nНапример: /delete 1"
        )
      ).void
    } else {
      parts(0).toIntOption match {
        case Some(reminderIndex) if reminderIndex > 0 =>
          deleteReminder(msg.chat.id, reminderIndex)
        case _ =>
          bot.execute(
            Methods.sendMessage(
              ChatIntId(msg.chat.id),
              "❌ Неверный номер напоминания!"
            )
          ).void
      }
    }
  }

  private def sendMainMenu(chatId: Long): F[Unit] = {
    bot.execute(
      Methods.sendMessage(
        ChatIntId(chatId),
        text = "Выберите действие:",
        replyMarkup = Some(MenuHandler.mainMenu)
      )
    ).void
  }

  private def askForTextReminder(chatId: Long): F[Unit] = {
    bot.execute(
      Methods.sendMessage(
        ChatIntId(chatId),
        text = "Введите напоминание в формате:\n/remind YYYY-MM-DD HH:MM ТЕКСТ"
      )
    ).void
  }

  private def showActiveReminders(chatId: Long): F[Unit] = {
    reminderStorage.findByChatId(chatId).flatMap { reminders =>
      if (reminders.isEmpty) {
        bot.execute(
          Methods.sendMessage(ChatIntId(chatId), "❌ У вас нет активных напоминаний!")
        ).void
      } else {
        val currentTime = botUtils.formatTimestamp(System.currentTimeMillis() / 1000)
        val remindersText = reminders.zipWithIndex.map { case (r, idx) =>
          s"${idx + 1}. 📅 ${botUtils.formatTimestamp(r.dateTime)} - ${r.message}"
        }.mkString("\n")
        bot.execute(
          Methods.sendMessage(
            ChatIntId(chatId),
            s"📋 Ваши активные напоминания на $currentTime:\n$remindersText"
          )
        ).void
      }
    }
  }

  private def askForReminderDeletion(chatId: Long): F[Unit] = {
    reminderStorage.findByChatId(chatId).flatMap { reminders =>
      if (reminders.isEmpty) {
        bot.execute(
          Methods.sendMessage(ChatIntId(chatId), "❌ У вас нет активных напоминаний для удаления!")
        ).void
      } else {
        val remindersText = reminders.zipWithIndex.map { case (r, idx) =>
          s"${idx + 1}. 📅 ${botUtils.formatTimestamp(r.dateTime)} - ${r.message}"
        }.mkString("\n")
        bot.execute(
          Methods.sendMessage(
            ChatIntId(chatId),
            s"🔔 Укажите номер напоминания для удаления командой /delete номер\n$remindersText"
          )
        ).void
      }
    }
  }

  private def askForReminderEdit(chatId: Long): F[Unit] = {
    reminderStorage.findByChatId(chatId).flatMap { reminders =>
      if (reminders.isEmpty) {
        bot.execute(
          Methods.sendMessage(ChatIntId(chatId), "❌ У вас нет активных напоминаний для изменения!")
        ).void
      } else {
        val remindersText = reminders.zipWithIndex.map { case (r, idx) =>
          s"${idx + 1}. 📅 ${botUtils.formatTimestamp(r.dateTime)} - ${r.message}"
        }.mkString("\n")
        bot.execute(
          Methods.sendMessage(
            ChatIntId(chatId),
            s"🔔 Выберите номер напоминания для изменения командой /edit номер\n$remindersText"
          )
        ).void
      }
    }
  }

  private def askForReminderEdit(chatId: Long, reminderIndex: Int): F[Unit] = {
    reminderStorage.findByChatId(chatId).flatMap { reminders =>
      if (reminders.isEmpty) {
        bot.execute(
          Methods.sendMessage(ChatIntId(chatId), "❌ У вас нет активных напоминаний для изменения!")
        ).void
      } else if (reminderIndex < 1 || reminderIndex > reminders.size) {
        bot.execute(
          Methods.sendMessage(ChatIntId(chatId), "❌ Неверный номер напоминания!")
        ).void
      } else {
        pendingEdits.update(_.updated(chatId, reminderIndex)) >>
          bot.execute(
            Methods.sendMessage(
              ChatIntId(chatId),
              s"🔄 Вы выбрали напоминание с индексом $reminderIndex.\nВведите новое время и текст в формате:\nYYYY-MM-DD HH:MM ТЕКСТ"
            )
          ).void
      }
    }
  }

  def editReminder(chatId: Long, reminderIndex: Int, newDateTimeStr: String, newMessage: String): F[Unit] = {
    val parts = newDateTimeStr.split(" ")
    if (parts.length != 2) {
      bot.execute(
        Methods.sendMessage(
          ChatIntId(chatId),
          "❌ Неверный формат даты и времени! Формат: YYYY-MM-DD HH:MM"
        )
      ).void
    } else {
      val dateStr = parts(0)
      val timeStr = parts(1)
      val newDateTime = botUtils.parseToTimestamp(dateStr, timeStr)
      val now = System.currentTimeMillis() / 1000
      if (newDateTime <= now) {
        bot.execute(
          Methods.sendMessage(
            ChatIntId(chatId),
            "❌ Указанное время не может быть в прошлом или равным текущему времени. Пожалуйста, нажмите заново Изменить напоминание и укажите время в будущем."
          )
        ).void
      } else {
        reminderStorage.findByChatId(chatId).flatMap { reminders =>
          if (reminderIndex >= 1 && reminderIndex <= reminders.length) {
            val reminder = reminders(reminderIndex - 1)
            reminderStorage.update(reminder.id.get, newDateTime, newMessage) >>
              bot.execute(
                Methods.sendMessage(
                  ChatIntId(chatId),
                  s"✅ Напоминание обновлено: $newMessage"
                )
              ).void
          } else {
            bot.execute(
              Methods.sendMessage(
                ChatIntId(chatId),
                "❌ Неверный номер напоминания!"
              )
            ).void
          }
        }
      }
    }
  }


  def deleteReminder(chatId: Long, reminderIndex: Int): F[Unit] = {
    reminderStorage.findByChatId(chatId).flatMap { reminders =>
      if (reminderIndex >= 1 && reminderIndex <= reminders.length) {
        val reminder = reminders(reminderIndex - 1)
        reminderStorage.deleteById(reminder.id.get).flatMap { deleted =>
          if (deleted)
            bot.execute(Methods.sendMessage(ChatIntId(chatId), s"✅ Напоминание удалено: ${reminder.message}")).void
          else
            bot.execute(Methods.sendMessage(ChatIntId(chatId), "Напоминание было удалено, либо уже выполнилось")).void
        }
      } else {
        bot.execute(Methods.sendMessage(ChatIntId(chatId), "❌ Неверный номер напоминания!")).void
      }
    }
  }



  def checkReminders: F[Unit] = {
    for {
      now <- Async[F].delay(System.currentTimeMillis() / 1000)
      dueReminders <- reminderStorage.findDueReminders(now)
      _ <- dueReminders.traverse_ { reminder =>
        bot.execute(
          Methods.sendMessage(
            ChatIntId(reminder.chatId),
            s"🔔 Напоминание: ${reminder.message} (${botUtils.formatTimestamp(reminder.dateTime)})"
          )
        ) >> reminderStorage.deleteById(reminder.id.get)
      }
    } yield ()
  }
}