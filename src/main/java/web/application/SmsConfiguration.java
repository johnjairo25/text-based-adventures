package web.application;

import game.builder.GameBuilder;
import game.builder.JsonGameBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import web.manager.CachedGameManager;

import java.io.InputStream;

@Configuration
public class SmsConfiguration {

    private static final String GAME_FILENAME = "basicGame.json";

    @Bean
    public GameBuilder gameBuilder() {
        InputStream is = WebApplication.class
                .getClassLoader()
                .getResourceAsStream(GAME_FILENAME);
        return new JsonGameBuilder(is);
    }

    @Bean
    public CachedGameManager gameManager(GameBuilder builder) {
        return new CachedGameManager(builder);
    }

}
