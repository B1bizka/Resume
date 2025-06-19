package bot.api

import telegramium.bots._

object MenuHandler {


  def mainMenu: InlineKeyboardMarkup = InlineKeyboardMarkup(
    inlineKeyboard = List(
      List(InlineKeyboardButton(text = "➕ Добавить напоминание", callbackData = Some("add_reminder"))),
      List(
        InlineKeyboardButton(text = "❌ Удалить напоминание", callbackData = Some("delete_reminder")),
        InlineKeyboardButton(text = "📋 Активные напоминания", callbackData = Some("active_reminders"))
      ),
      List(InlineKeyboardButton(text = "✏ Изменить напоминания", callbackData = Some("edit_reminders")))
    )
  )
}

