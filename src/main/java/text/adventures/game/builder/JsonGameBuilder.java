package text.adventures.game.builder;

import text.adventures.game.TextAdventuresGame;
import text.adventures.game.elements.Blocker;
import text.adventures.game.elements.Equipable;
import text.adventures.game.elements.Location;
import text.adventures.game.elements.Location.LocationType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class JsonGameBuilder implements GameBuilder {

    private final String jsonText;

    public JsonGameBuilder(InputStream inputStream) {
        this.jsonText = readInputText(inputStream);
    }

    @Override
    public TextAdventuresGame build() {
        JSONObject json = new JSONObject(jsonText);
        return buildGameFromText(json);
    }

    private String readInputText(InputStream inputStream) {
        return new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    protected TextAdventuresGame buildGameFromText(JSONObject json) {

        String initialLocation = safeGetString(json, "currentLocation");
        Set<Location> locations = buildLocations(safeGetArray(json, "locations"));

        Location currentLocation = findCurrentLocation(initialLocation, locations);

        return TextAdventuresGame.Builder.aTextAdventuresGame()
                .withCurrentLocation(currentLocation)
                .withLocations(locations)
                .build();
    }

    private Set<Location> buildLocations(JSONArray locations) {
        int numberOfLocations = locations.length();
        Set<Location> locationSet = new HashSet<>();

        for (int i = 0; i < numberOfLocations; i++) {
            Location location = buildLocation(locations.getJSONObject(i));
            locationSet.add(location);
        }

        return locationSet;
    }

    private Location buildLocation(JSONObject json) {

        return Location.Builder.aLocation()
                .withName(normalize(safeGetString(json, "name")))
                .withMessage(normalize(safeGetString(json, "message")))
                .withType(buildLocationType(
                        normalize(safeGetString(json, "type"))
                ))
                .withDirectionToLocationMap(
                        buildLocationToDirectionMap(safeGetArray(json, "directionToLocationMap"))
                ).withBlockers(
                        buildBlockers(safeGetArray(json, "blockers"))
                ).withEquipables(
                        buildEquipables(safeGetArray(json, "equipables"))
                )
                .build();
    }

    private JSONArray safeGetArray(JSONObject json, String equipables) {
        return json.has(equipables) ? json.getJSONArray(equipables) : new JSONArray();
    }

    protected String safeGetString(JSONObject json, String name) {
        return json.has(name) ? json.getString(name) : "";
    }

    private List<Equipable> buildEquipables(JSONArray jsonArray) {
        List<Equipable> result = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Equipable equipable = new Equipable(
                    normalize(safeGetString(jsonObject, "name")),
                    normalize(safeGetString(jsonObject, "description"))
            );
            result.add(equipable);
        }

        return result;
    }

    private List<Blocker> buildBlockers(JSONArray jsonArray) {
        List<Blocker> result = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Blocker blocker = Blocker.Builder.aBlocker()
                    .withDescription(normalize(safeGetString(jsonObject, "description")))
                    .withUnblocksWithEquipable(normalize(safeGetString(jsonObject, "unblocksWith")))
                    .withBlockedDirections(
                            buildBlockedDirections(safeGetArray(jsonObject, "blockedDirections"))
                    )
                    .build();
            result.add(blocker);
        }

        return result;
    }

    private List<String> buildBlockedDirections(JSONArray jsonArray) {
        List<String> result = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            result.add(jsonArray.getString(i));
        }

        return result;
    }

    private Map<String, String> buildLocationToDirectionMap(JSONArray jsonArray) {
        Map<String, String> result = new HashMap<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            result.put(
                    normalize(safeGetString(jsonObject, "direction")),
                    normalize(safeGetString(jsonObject, "location")));
        }

        return result;
    }

    private LocationType buildLocationType(String type) {
        if ("winning".equalsIgnoreCase(type)) {
            return LocationType.WINNING;
        } else if ("losing".equalsIgnoreCase(type)) {
            return LocationType.LOSING;
        } else {
            return LocationType.STANDARD;
        }
    }

    private static String normalize(String input) {

        return input == null ? "" : input
                .trim()
                .replaceAll("\\s+", " ")
                .replaceAll("[^a-zA-Z0-9 ]", "");
    }

    private Location findCurrentLocation(String initialLocation, Set<Location> locations) {
        Optional<Location> locationOption = locations.stream()
                .filter(it -> it.getName().equalsIgnoreCase(initialLocation))
                .findFirst();
        if (locationOption.isPresent()) {
            return locationOption.get();
        } else {
            throw new IllegalArgumentException(
                    String.format("The initialLocation : [%s] must be in the name of at least one of the locations' names", initialLocation));
        }
    }

}
