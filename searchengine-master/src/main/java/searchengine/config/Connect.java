package searchengine.config;

import lombok.Getter;
import lombok.Setter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Getter
@Setter
@Component
public class Connect {

    @ConfigurationProperties(prefix = "jsoup-setting")
    public Connection.Response getDocumentConnect(String link) throws IOException {
        return Jsoup.connect(link).execute();
    }
}
