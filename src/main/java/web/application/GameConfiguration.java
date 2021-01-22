package web.application;

import game.builder.GameBuilder;
import game.builder.JsonGameBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import web.sms.SmsGameManager;
import web.voice.VoiceGameManagerImpl;

import java.io.InputStream;

@Configuration
public class GameConfiguration {

    private static final String GAME_FILENAME = "basicGame.json";

    @Bean
    public GameBuilder gameBuilder() {
        InputStream is = WebApplication.class
                .getClassLoader()
                .getResourceAsStream(GAME_FILENAME);
        return new JsonGameBuilder(is);
    }

    @Bean
    public SmsGameManager gameManager(GameBuilder builder) {
        return new SmsGameManager(builder);
    }

    @Bean
    public VoiceGameManagerImpl voiceGameManager(GameBuilder builder) {
        return new VoiceGameManagerImpl(builder);
    }

}
