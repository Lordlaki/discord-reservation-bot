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

    public List<Game> getAll() throws Exception {
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, sheetName + "!A:D")
                .execute();

        List<List<Object>> values = response.getValues();
        List<Game> games = new ArrayList<>();

        if (values != null && values.size() > 1) {
            // preskočíme prvý riadok (hlavičku)
            for (int i = 1; i < values.size(); i++) {
                List<Object> row = values.get(i);
                String name = row.size() > 0 ? row.get(0).toString() : "";
                if (name.isBlank()) continue;

                String reservedBy = row.size() > 1 ? row.get(1).toString() : null;
                String reservedById = row.size() > 2 ? row.get(2).toString() : null;
                String steamLink = row.size() > 3 ? row.get(3).toString() : null;

                Game g = new Game(name);
                g.setReservedBy((reservedBy == null || reservedBy.isBlank()) ? null : reservedBy);
                g.setReservedById((reservedById == null || reservedById.isBlank()) ? null : reservedById);
                g.setSteamLink((steamLink == null || steamLink.isBlank()) ? null : steamLink);
                games.add(g);
            }
        }

        return games;
    }

    public void save(Game g) throws Exception {
        List<Game> all = getAll();
        int existingIndex = -1;

        // index +2, aby sa preskočila hlavička
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getName().equalsIgnoreCase(g.getName())) {
                existingIndex = i + 2; // Sheets index od 1, +1 pre hlavičku
                break;
            }
        }

        List<List<Object>> values = new ArrayList<>();
        values.add(List.of(
                g.getName(),
                g.getReservedBy() != null ? g.getReservedBy() : "",
                g.getReservedById() != null ? g.getReservedById() : "",
                g.getSteamLink() != null ? g.getSteamLink() : ""
        ));

        ValueRange body = new ValueRange().setValues(values);

        if (existingIndex != -1) {
            String range = sheetName + "!A" + existingIndex + ":D" + existingIndex;
            service.spreadsheets().values()
                    .update(spreadsheetId, range, body)
                    .setValueInputOption("RAW")
                    .execute();
        } else {
            service.spreadsheets().values()
                    .append(spreadsheetId, sheetName + "!A:D", body)
                    .setValueInputOption("RAW")
                    .execute();
        }
    }
}
