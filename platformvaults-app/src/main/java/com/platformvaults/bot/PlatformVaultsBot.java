package com.platformvaults.bot;

import com.platformvaults.entity.User;
import com.platformvaults.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlatformVaultsBot extends TelegramLongPollingBot {

    private final UserService userService;

    @Value("${spring.telegram.bot.token}")
    private String botToken;

    @Value("${spring.telegram.bot.username}")
    private String botUsername;

    @Value("${spring.telegram.bot.name}")
    private String botName;

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || update.getMessage() == null) {
            return;
        }

        var message = update.getMessage();
        if (message.getText() == null) {
            return;
        }

        String text = message.getText().trim();
        Long chatId = message.getChatId();
        var telegramUser = message.getFrom();

        if (telegramUser == null) {
            return;
        }

        Long telegramId = telegramUser.getId();
        String firstName = telegramUser.getFirstName();
        String lastName = telegramUser.getLastName();
        String username = telegramUser.getUserName();
        String languageCode = telegramUser.getLanguageCode();

        log.info("Received message from telegramId={}, text={}", telegramId, text);

        switch (text) {
            case "/start" -> handleStart(telegramId, username, firstName, lastName, languageCode, chatId);
            case "/help" -> handleHelp(chatId);
            default -> handleUnknown(chatId);
        }
    }

    private void handleStart(Long telegramId, String username, String firstName,
                             String lastName, String languageCode, Long chatId) {
        User user = userService.registerUser(telegramId, username, firstName, lastName, languageCode);
        String response = String.format(
                "Привет, %s! 👋\n\n" +
                "Добро пожаловать в PlatformVaults!\n\n" +
                "Ваш ID: %d\n" +
                "Статус: %s\n\n" +
                "Используйте /help для справки.",
                firstName, telegramId, user.getStatus().name()
        );
        sendMessage(chatId, response);
    }

    private void handleHelp(Long chatId) {
        String response = String.format(
                "%s — PlatformVaults Bot\n\n" +
                "/start — Начать работу\n" +
                "/help — Показать справку\n\n" +
                "Скоро здесь будет кошелёк! 🔐",
                botName
        );
        sendMessage(chatId, response);
    }

    private void handleUnknown(Long chatId) {
        sendMessage(chatId, "Неизвестная команда. Используйте /help");
    }

    private void sendMessage(Long chatId, String text) {
        try {
            var request = SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .build();
            execute(request);
        } catch (TelegramApiException e) {
            log.error("Failed to send message to chatId={}", chatId, e);
        }
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }
}
