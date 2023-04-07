package project.SpringDemoBot.LaTexConvert;

import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class LatexBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        // Получение сообщения от пользователя
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
        TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20);

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
        sendPhoto.setPhoto(new InputFile(String.valueOf(imageToBytes(image))));
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

    @Override
    public String getBotUsername() {
        return "LatexBot";
    }

    @Override
    public String getBotToken() {
        return "5636929469:AAFHlcMJ03W5f14ZW5BxflABj46LZL81v8Q";
    }
}