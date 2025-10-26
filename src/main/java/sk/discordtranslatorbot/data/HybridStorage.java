package sk.discordtranslatorbot.data;

import java.util.ArrayList;
import java.util.List;

public class HybridStorage {

    private final GameStorage localStorage;
    private final GoogleSheetsStorage remoteStorage;

    public HybridStorage(GameStorage localStorage, GoogleSheetsStorage remoteStorage) {
        this.localStorage = localStorage;
        this.remoteStorage = remoteStorage;
    }

    /**
     * Pridá alebo aktualizuje hru v lokálnom aj Google Sheets storage.
     */
    public synchronized void addOrUpdateGame(Game game) {
        Game local = localStorage.get(game.getName());
        if (local == null) {
            // vytvor novú hru lokálne
            localStorage.addGame(game.getName());
            local = localStorage.get(game.getName());
        }

        // aktualizuj všetky údaje
        local.setReservedBy(game.getReservedBy());
        local.setReservedById(game.getReservedById());
        local.setSteamLink(game.getSteamLink());
        local.setCzechVersion(game.getCzechVersion());
        local.setCompleted(game.isCompleted());
        localStorage.save();

        if (remoteStorage != null) {
            try {
                remoteStorage.save(local); // uloží aktualizovanú lokálnu hru
            } catch (Exception e) {
                System.err.println("⚠️ Nepodarilo sa zapísať do Google Sheets: " + e.getMessage());
            }
        }
    }

    /**
     * Získa hru podľa mena. Ak nie je lokálne, pokúsi sa načítať z Google Sheets.
     */
    public synchronized Game get(String name) {
        Game g = localStorage.get(name);
        if (g == null && remoteStorage != null) {
            try {
                List<Game> remoteGames = remoteStorage.getAll();
                for (Game rg : remoteGames) {
                    if (rg.getName().equalsIgnoreCase(name)) {
                        // pridaj do lokálneho storage
                        localStorage.addGame(rg.getName());
                        Game localGame = localStorage.get(rg.getName());
                        localGame.setReservedBy(rg.getReservedBy());
                        localGame.setReservedById(rg.getReservedById());
                        localGame.setSteamLink(rg.getSteamLink());
                        localGame.setCzechVersion(rg.getCzechVersion());
                        localGame.setCompleted(rg.isCompleted());
                        localStorage.save();
                        return localGame;
                    }
                }
            } catch (Exception e) {
                System.err.println("⚠️ Chyba pri čítaní z Google Sheets: " + e.getMessage());
            }
        }
        return g;
    }

    /**
     * Získa všetky hry lokálne aj z Google Sheets (bez duplicitných mien).
     */
    public synchronized List<Game> getAll() {
        List<Game> all = new ArrayList<>(localStorage.getAll());

        if (remoteStorage != null) {
            try {
                List<Game> remote = remoteStorage.getAll();
                for (Game g : remote) {
                    boolean exists = all.stream()
                            .anyMatch(x -> x.getName().equalsIgnoreCase(g.getName()));
                    if (!exists) all.add(g);
                }
            } catch (Exception e) {
                System.err.println("⚠️ Nepodarilo sa načítať z Google Sheets: " + e.getMessage());
            }
        }

        return all;
    }

    /**
     * Uloží alebo aktualizuje hru v lokálnom a Google Sheets storage.
     */
    public synchronized void save(Game g) {
        addOrUpdateGame(g);
    }

    /**
     * Vyhľadá hru podľa názvu (čiastočne alebo presne).
     */
    public synchronized Game findGameByName(String name) {
        if (name == null || name.isBlank()) return null;
        String lower = name.toLowerCase();

        // najprv lokálne
        for (Game g : localStorage.getAll()) {
            if (g.getName().toLowerCase().contains(lower)) {
                return g;
            }
        }

        // ak nenájde, skús aj z Google Sheets
        if (remoteStorage != null) {
            try {
                for (Game g : remoteStorage.getAll()) {
                    if (g.getName().toLowerCase().contains(lower)) {
                        return g;
                    }
                }
            } catch (Exception e) {
                System.err.println("⚠️ Chyba pri hľadaní hry v Google Sheets: " + e.getMessage());
            }
        }

        return null;
    }
}
