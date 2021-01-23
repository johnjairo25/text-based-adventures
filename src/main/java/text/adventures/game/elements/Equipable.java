package text.adventures.game.elements;

public class Equipable {

    private final String name;
    private final String description;

    public Equipable(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
