package jobs;

import org.apache.groovy.util.Maps;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

@OnApplicationStart
public class Bootstrap extends Job {
    @Override
    public void doJob() throws Exception {
        Map<String, String> settings = Maps.of(
                "application.secret", "EVENTMAP_SECRET",
                "google.client_id", "GOOGLE_CLIENT_ID",
                "google.client_secret", "GOOGLE_CLIENT_SECRET",
                "google.api_key", "GOOGLE_API_KEY",
                "facebook.client_id", "FACEBOOK_CLIENT_ID",
                "facebook.client_secret", "FACEBOOK_CLIENT_SECRET"
        );
        // use environment variables
        settings.forEach((k, v) -> {
            if (System.getenv(v) != null) {
                Play.configuration.setProperty(k, System.getenv(v));
            }
        });
        // use .env
        if (Files.exists(Paths.get(".env"))) {
            Properties config = new Properties();
            try (Reader reader = Files.newBufferedReader(Paths.get(".env"), StandardCharsets.UTF_8)) {
                config.load(reader);
            }
            settings.forEach((k, v) -> {
                if (config.containsKey(v)) {
                    Play.configuration.setProperty(k, config.getProperty(v));
                }
            });
        }
        // 檢查參數
        settings.forEach((k, v) -> {
            if (Play.configuration.getProperty(k) == null || Play.configuration.getProperty(k).trim().isEmpty()) {
                throw new RuntimeException(k + " is not set.");
            }
        });
    }
}