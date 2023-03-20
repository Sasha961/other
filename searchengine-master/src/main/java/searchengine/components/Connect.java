package searchengine.components;

import lombok.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jsoup-setting")
public class Connect {
    public static Connection.Response getDocumentConnect(String link) throws IOException {
        return Jsoup.connect(link)
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:101.0) Gecko/20100101 Firefox/101.0")
                .referrer("http://www.google.com")
                .ignoreHttpErrors(true)
                .followRedirects(false)
                .execute();
    }
}
