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
    //Этот метод используется для получения сообщения от пользователя
    //кароч, когда приходит новое сообщение вызывается этот метод
    // и хоба, мы начинаем это сообщение обрабатывать


    public void onUpdateReceived(Update update) {// класс update  представляет собой обновление, полученное от сервера Telegram
        // Получение сообщения от пользователя в нём содержится входящая информация
        Message message = update.getMessage();
        // в этом условии идёт проверка на пустое сообщение и наличие ткста
        if (message == null || !message.hasText()) {
            return;
        }
        //Получаем текст сообщения
        String text = message.getText();

        // Попытка преобразования текста в формулу LaTeX
        TeXFormula formula;
        try {
               formula = new TeXFormula(text);
               formula.setBackground(Color.GREEN);
        } catch (Exception e) {
            return;
        }

        // Создание объекта TeXIcon с заданным размером и стилем
        //этот объект представляет изображение формулы
        TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_SCRIPT, 20);

        // Получение изображения из объекта TeXIcon
        //BufferedImage — это класс, представляющий
        // изображение в памяти,
        // которое можно использовать для хранения
        // и манипуляции пикселами.
        BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(Color.GREEN);
        g2.fillRect(0, 0, icon.getIconWidth(), icon.getIconHeight());
        //родитель класса TexIcon прдставляет текс в граф итерфейсе gui
        JLabel jl = new JLabel();
        jl.setForeground(Color.GREEN);
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
