package game.builder;

import game.TextAdventuresGame;
import game.elements.Blocker;
import game.elements.Equipable;
import game.elements.Location;

import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class StaticGameBuilder implements GameBuilder {

    protected static final String KEY_NAME = "keyInNorthEast";
    protected static final Equipable keyEquipable =
            new Equipable(KEY_NAME, "Some description for the key");

    protected static final String SWORD_NAME = "swordName";
    protected static final Equipable swordEquipable =
            new Equipable(SWORD_NAME, "some sword description");

    protected Blocker southBlocker;
    protected Blocker tigerBlocker;

    protected Location initial;
    protected Location north;
    protected Location northEastWithKey;
    protected Location northWestWithSword;
    protected Location southBlockedByDoor;
    protected Location southSouth;
    protected Location southSouthEastWinning;
    protected Location southSouthWestLosing;
    protected Set<Location> locations;

    @Override
    public TextAdventuresGame build() {
        southBlocker = Blocker.Builder.aBlocker()
                .withDescription("Lock")
                .withUnblocksWithEquipable(KEY_NAME) // unblocks with key in northEast
                .withBlockedDirections(new ArrayList<>(singletonList("south")))
                .build();
        tigerBlocker = Blocker.Builder.aBlocker()
                .withDescription("Tiger")
                .withUnblocksWithEquipable(SWORD_NAME)
                .withBlockedDirections(new ArrayList<>(asList("east", "west")))
                .build();

        initial = Location.Builder.aLocation()
                .withName("initialName")
                .withMessage("initial message")
                .withDirectionToLocationMap(new HashMap<String, String>() {{
                    put("north", "northName");
                    put("south", "southName");
                }})
                .withBlockers(singletonList(southBlocker))
                .build();
        north = Location.Builder.aLocation()
                .withName("northName")
                .withMessage("north message")
                .withDirectionToLocationMap(new HashMap<String, String>() {{
                    put("south", "initialName");
                    put("east", "northEastName");
                    put("west", "northWestName");
                }})
                .build();
        northEastWithKey = Location.Builder.aLocation()
                .withName("northEastName")
                .withMessage("northEast message")
                .withDirectionToLocationMap(new HashMap<String, String>() {{
                    put("west", "northName");
                }})
                .withEquipables(new ArrayList<>(singletonList(keyEquipable)))
                .build();
        northWestWithSword = Location.Builder.aLocation()
                .withName("northWestName")
                .withMessage("northWest message")
                .withDirectionToLocationMap(new HashMap<String, String>() {{
                    put("east", "northName");
                }})
                .withEquipables(new ArrayList<>(singletonList(swordEquipable)))
                .build();
        southBlockedByDoor = Location.Builder.aLocation()
                .withName("southName")
                .withMessage("south message")
                .withDirectionToLocationMap(new HashMap<String, String>() {{
                    put("north", "initial");
                    put("south", "southSouthName");
                }})
                .build();
        southSouth = Location.Builder.aLocation()
                .withName("southSouthName")
                .withMessage("south south message")
                .withDirectionToLocationMap(new HashMap<String, String>() {{
                    put("north", "southName");
                    put("east", "southSouthEastName");
                    put("west", "southSouthWestName");
                }})
                .withBlockers(singletonList(tigerBlocker))
                .build();
        southSouthEastWinning = Location.Builder.aLocation()
                .withName("southSouthEastName")
                .withMessage("south south east message")
                .withDirectionToLocationMap(new HashMap<>())
                .withType(Location.LocationType.WINNING)
                .build();
        southSouthWestLosing = Location.Builder.aLocation()
                .withName("southSouthWestName")
                .withMessage("south south west message")
                .withDirectionToLocationMap(new HashMap<>())
                .withType(Location.LocationType.LOSING)
                .build();

        locations = new HashSet<Location>() {{
            add(initial);
            add(north);
            add(northEastWithKey);
            add(southBlockedByDoor);
            add(northWestWithSword);
            add(southSouth);
            add(southSouthEastWinning);
            add(southSouthWestLosing);
        }};

        return TextAdventuresGame.Builder.aTextAdventuresGame()
                .withCurrentLocation(initial)
                .withLocations(locations)
                .build();
    }

}
