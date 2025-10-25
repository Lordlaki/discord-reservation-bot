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
        )
                .setApplicationName("Discord Reservation Bot")
                .build();
    }

    /**
     * Načíta všetky hry z Google Sheets.
     */
    public List<Game> getAll() throws Exception {
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, sheetName + "!A:C")
                .execute();

        List<List<Object>> values = response.getValues();
        List<Game> games = new ArrayList<>();

        if (values != null) {
            for (List<Object> row : values) {
                String name = row.size() > 0 ? row.get(0).toString() : "";
                if (name.isBlank()) continue;

                String reservedBy = row.size() > 1 ? row.get(1).toString() : null;
                String reservedById = row.size() > 2 ? row.get(2).toString() : null;

                Game g = new Game(name);
                g.setReservedBy((reservedBy == null || reservedBy.isBlank()) ? null : reservedBy);
                g.setReservedById((reservedById == null || reservedById.isBlank()) ? null : reservedById);
                games.add(g);
            }
        }

        return games;
    }

    /**
     * Uloží alebo aktualizuje hru v Google Sheets.
     */
    public void save(Game g) throws Exception {
        List<Game> all = getAll();
        int existingIndex = -1;

        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getName().equalsIgnoreCase(g.getName())) {
                existingIndex = i + 1; // Sheets index od 1
                break;
            }
        }

        List<List<Object>> values = new ArrayList<>();
        values.add(List.of(
                g.getName(),
                g.getReservedBy() != null ? g.getReservedBy() : "",
                g.getReservedById() != null ? g.getReservedById() : ""
        ));

        ValueRange body = new ValueRange().setValues(values);

        if (existingIndex != -1) {
            // aktualizácia existujúceho riadku
            String range = sheetName + "!A" + existingIndex + ":C" + existingIndex;
            service.spreadsheets().values()
                    .update(spreadsheetId, range, body)
                    .setValueInputOption("RAW")
                    .execute();
        } else {
            // pridanie nového riadku
            service.spreadsheets().values()
                    .append(spreadsheetId, sheetName + "!A:C", body)
                    .setValueInputOption("RAW")
                    .execute();
        }
    }
}
