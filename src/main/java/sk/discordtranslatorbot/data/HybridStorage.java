package sk.discordtranslatorbot.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HybridStorage {

    private final GameStorage localStorage;
    private final GoogleSheetsStorage remoteStorage;

    public HybridStorage(GameStorage localStorage, GoogleSheetsStorage remoteStorage) {
        this.localStorage = localStorage;
        this.remoteStorage = remoteStorage;
    }

    /**
     * Pridá hru do lokálneho aj Google Sheets storage.
     */
    public synchronized void addGame(String name) {
        Game g = localStorage.get(name);
        if (g == null) {
            localStorage.addGame(name);
            g = localStorage.get(name);
        }

        // uloženie do Google Sheets
        if (remoteStorage != null && g != null) {
            try {
                remoteStorage.save(g);
            } catch (Exception e) {
                System.err.println("⚠️ Nepodarilo sa zapísať do Google Sheets: " + e.getMessage());
            }
        }

        // uloženie lokálne
        localStorage.save();
    }

    /**
     * Získa hru podľa mena. Ak nie je lokálne, pokúsi sa načítať z Google Sheets.
     */
    public synchronized Game get(String name) {
        Game g = localStorage.get(name);
        if (g == null && remoteStorage != null) {
            try {
                List<Game> allRemote = remoteStorage.getAll();
                for (Game gr : allRemote) {
                    if (gr.getName().equalsIgnoreCase(name)) {
                        // pridaj do lokálneho storage
                        localStorage.addGame(gr.getName());
                        localStorage.save();
                        return gr;
                    }
                }
            } catch (Exception e) {
                System.err.println("⚠️ Chyba pri čítaní z Google Sheets: " + e.getMessage());
            }
        }
        return g;
    }

    /**
     * Získa všetky hry, lokálne aj z Google Sheets (bez duplicitných mien).
     */
    public synchronized Collection<Game> getAll() {
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
     * Uloží hru do lokálneho a Google Sheets storage.
     */
    public synchronized void save(Game g) {
        // najprv uloží lokálne, potom do Sheets
        localStorage.save();

        if (remoteStorage != null) {
            try {
                remoteStorage.save(g);
            } catch (Exception e) {
                System.err.println("⚠️ Chyba pri ukladaní do Google Sheets: " + e.getMessage());
            }
        }
    }
}
