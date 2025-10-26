package sk.discordtranslatorbot.data;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.util.ArrayList;
import java.util.List;

public class GoogleSheetsStorage {

    private final Sheets service;
    private final String spreadsheetId;
    private final String sheetName = "Games";

    public GoogleSheetsStorage(String credentialsPath, String spreadsheetId) throws Exception {
        this.spreadsheetId = spreadsheetId;
        service = new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                GoogleSheetsAuth.getCredentials(credentialsPath)
        ).setApplicationName("Discord Reservation Bot").build();
    }

    /**
     * Získa všetky hry z Google Sheets
     */
    public List<Game> getAll() throws Exception {
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, sheetName + "!A:G")
                .execute();

        List<List<Object>> values = response.getValues();
        List<Game> games = new ArrayList<>();

        if (values != null && values.size() > 1) {
            for (int i = 1; i < values.size(); i++) { // preskočíme hlavičku
                List<Object> row = values.get(i);
                String name = row.size() > 0 ? row.get(0).toString() : "";
                if (name.isBlank()) continue;

                Game g = new Game(name);

                // CZ a SK rezervácie (B a C)
                g.setReservedByCz(row.size() > 1 && !row.get(1).toString().isBlank() ? row.get(1).toString() : null);
                g.setReservedBySk(row.size() > 2 && !row.get(2).toString().isBlank() ? row.get(2).toString() : null);

                // Steam link (D)
                g.setSteamLink(row.size() > 3 && !row.get(3).toString().isBlank() ? row.get(3).toString() : null);

                // Verzie (E a F)
                g.setCzechVersion(row.size() > 4 && !row.get(4).toString().isBlank() ? row.get(4).toString() : null);
                g.setSlovakVersion(row.size() > 5 && !row.get(5).toString().isBlank() ? row.get(5).toString() : null);

                // Completed (G)
                g.setCompleted(row.size() > 6 && "Hotovo".equalsIgnoreCase(row.get(6).toString()));

                games.add(g);
            }
        }

        return games;
    }

    /**
     * Uloží alebo aktualizuje hru v Google Sheets
     */
    public void save(Game g) throws Exception {
        List<Game> all = getAll();
        int existingIndex = -1;

        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getName().equalsIgnoreCase(g.getName())) {
                existingIndex = i + 2; // preskočenie hlavičky a 1-based index
                break;
            }
        }

        List<List<Object>> values = List.of(List.of(
                g.getName(),                             // A
                g.getReservedByCz() != null ? g.getReservedByCz() : "", // B
                g.getReservedBySk() != null ? g.getReservedBySk() : "", // C
                g.getSteamLink() != null ? g.getSteamLink() : "",       // D
                g.getCzechVersion() != null ? g.getCzechVersion() : "", // E
                g.getSlovakVersion() != null ? g.getSlovakVersion() : "", // F
                g.isCompleted() ? "Hotovo" : ""                          // G <-- zmena
        ));

        ValueRange body = new ValueRange().setValues(values);

        if (existingIndex != -1) {
            // aktualizuj existujúci riadok
            String range = sheetName + "!A" + existingIndex + ":G" + existingIndex;
            service.spreadsheets().values()
                    .update(spreadsheetId, range, body)
                    .setValueInputOption("RAW")
                    .execute();
        } else {
            // pridaj nový riadok
            service.spreadsheets().values()
                    .append(spreadsheetId, sheetName + "!A:G", body)
                    .setValueInputOption("RAW")
                    .execute();
        }
    }
}
