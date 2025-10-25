package sk.discordtranslatorbot.data;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class GoogleSheetsAuth {
    public static GoogleCredential getCredentials(String credentialsPath) throws IOException {
        try (FileInputStream stream = new FileInputStream(credentialsPath)) {
            return GoogleCredential.fromStream(stream)
                    .createScoped(List.of("https://www.googleapis.com/auth/spreadsheets"));
        }
    }
}
