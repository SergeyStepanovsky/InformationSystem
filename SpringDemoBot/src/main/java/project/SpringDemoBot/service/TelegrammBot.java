package project.SpringDemoBot.service;


import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import project.SpringDemoBot.config.BotConfig;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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

        Message message = update.getMessage();
        if (message == null || !message.hasText()) {
            return;
        }
        String text = message.getText();

        // Попытка преобразования текста в формулу LaTeX
        TeXFormula formula;
        try {
            formula = new TeXFormula(text);
        } catch (Exception e) {
            return;
        }

        // Создание объекта TeXIcon с заданным размером и стилем
        TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 50);

        // Получение изображения из объекта TeXIcon
        BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, icon.getIconWidth(), icon.getIconHeight());
        JLabel jl = new JLabel();
        jl.setForeground(Color.BLACK);
        icon.paintIcon(jl, g2, 0, 0);

        // Отправка изображения пользователю в Telegram
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(message.getChatId().toString());
        File file = new File("image.png");
        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendPhoto.setPhoto(new InputFile(file));
        sendPhoto.setCaption("Результат преобразования формулы LaTeX: " + text);
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private byte[] imageToBytes(BufferedImage image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
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
