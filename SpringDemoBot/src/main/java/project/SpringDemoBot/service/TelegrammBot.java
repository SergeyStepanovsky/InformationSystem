package project.SpringDemoBot.service;


import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import project.SpringDemoBot.config.BotConfig;

import java.util.LinkedList;
import java.util.Queue;

@Component
public class TelegrammBot extends TelegramLongPollingBot {

    final BotConfig config;

    public TelegrammBot(BotConfig config) {
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotname();
    }

    @Override
    public String getBotToken() {
        return config.getTokenl();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();


            switch (messageText) {
                case "/start":
                    try {
                        startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    } catch (TelegramApiException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                default:
                    try {
                        sendMessage(chatId, "Sorry, we will add other features in the near future, \n but for now Egor sucks  ");
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }

            }
        }
    }

    private void startCommandReceived(long chatId, String name) throws TelegramApiException, InterruptedException {

        String answer = "Hi, " + name + ", nice to meet you!\n If your name is Egor, then go and fuck yourself by yourself";


        sendMessage(chatId, answer);



        Thread.sleep(3000);

        sendMessage(chatId, "Nigaaaaaaaa");
        Queue<String> queue = new LinkedList<>();
        queue.add("Бля, Егор, по русски уже тебе говорю, съеби отсюда!");
        queue.add("Тебя даже Скайнет не уважает");
        queue.add("Дебил");

        while (!queue.isEmpty()) {
        Thread.sleep(3000);
        sendMessage(chatId, queue.remove());
        }
    }

    private void sendMessage(long chatId, String textToSend) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {

        }
    }
}
