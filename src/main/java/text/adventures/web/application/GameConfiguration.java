package text.adventures.web.application;

import text.adventures.game.builder.GameBuilder;
import text.adventures.game.builder.JsonGameBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import text.adventures.web.sms.SmsGameManager;
import text.adventures.web.voice.VoiceGameManager;

import java.io.InputStream;

@Configuration
public class GameConfiguration {

    private static final String GAME_FILENAME = "fullGame.json";

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
    public VoiceGameManager voiceGameManager(GameBuilder builder) {
        return new VoiceGameManager(builder);
    }

}
