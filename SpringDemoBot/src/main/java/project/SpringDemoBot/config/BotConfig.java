package project.SpringDemoBot.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data // аннотация, которая автоматически сздает конструкторы, геттеры и сетеры
@PropertySource("application.properties")
public class BotConfig {


@Value("${bot.name}")
    String botname;

@Value("${bot.token}")
    String tokenl;

}
