package game;

import game.builder.JsonGameBuilder;
import game.builder.StaticGameBuilder;

import java.io.InputStream;
import java.util.Scanner;

public class ConsoleMain {

    public static void main(String[] args) {
        playJsonBasicGame();
    }

    private static void playJsonBasicGame() {
        InputStream is = readBasicFile();
        TextAdventuresGame game = new JsonGameBuilder(is).build();
        playGame(new Scanner(System.in), game);
    }

    private static InputStream readBasicFile() {
        return ConsoleMain.class
                .getClassLoader()
                .getResourceAsStream("basicGame.json");
    }

    private static void playStaticGame() {
        Scanner sc = new Scanner(System.in);
        StaticGameBuilder builder = new StaticGameBuilder();
        playGame(sc, builder.build());
    }

    private static void playGame(Scanner sc, TextAdventuresGame game) {
        System.out.println(game.startGame());
        while (true) {
            String command = sc.nextLine();
            String result = game.applyCommand(command);
            System.out.println(result);
        }
    }

}
