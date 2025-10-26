package sk.discordtranslatorbot.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class GameStorage {

    private static final String FILE = "games.json";
    private final Gson gson = new Gson();
    private Map<String, Game> games = new HashMap<>();

    public GameStorage() { load(); }

    private String key(String name) { return name.trim().toLowerCase(); }

    public synchronized void addGame(String name) {
        String k = key(name);
        if (!games.containsKey(k)) {
            games.put(k, new Game(name.trim()));
            save();
        }
    }

    public synchronized void addOrUpdateGame(Game game) {
        String k = key(game.getName());
        Game existing = games.get(k);
        if (existing == null) {
            games.put(k, game);
        } else {
            existing.setReservedByCz(game.getReservedByCz());
            existing.setReservedByIdCz(game.getReservedByIdCz());
            existing.setReservedBySk(game.getReservedBySk());
            existing.setReservedByIdSk(game.getReservedByIdSk());
            existing.setSteamLink(game.getSteamLink());
            existing.setCzechVersion(game.getCzechVersion());
            existing.setSlovakVersion(game.getSlovakVersion());
            existing.setCompleted(game.isCompleted());
        }
        save();
    }

    public synchronized Game get(String name) { return games.get(key(name)); }

    public synchronized Collection<Game> getAll() { return games.values(); }

    public synchronized void removeGame(String name) {
        String k = key(name);
        if (games.containsKey(k)) {
            games.remove(k);
            save();
        }
    }

    public synchronized void save() {
        try (Writer w = new OutputStreamWriter(new FileOutputStream(FILE), StandardCharsets.UTF_8)) {
            gson.toJson(games, w);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public synchronized void load() {
        File f = new File(FILE);
        if (!f.exists()) return;
        try (Reader r = new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8)) {
            Type type = new TypeToken<Map<String, Game>>() {}.getType();
            Map<String, Game> loaded = gson.fromJson(r, type);
            if (loaded != null) games = loaded;
        } catch (IOException e) { e.printStackTrace(); }
    }
}
