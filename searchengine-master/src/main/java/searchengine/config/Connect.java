package searchengine.config;

import lombok.NoArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

@NoArgsConstructor
public class Connect {

    public Connection.Response getDocumentConnect(String link) throws IOException {

        return  Jsoup.connect(link)
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:101.0) Gecko/20100101 Firefox/101.0")
                .ignoreContentType(true)
                .referrer("http://www.google.com").execute();


    }
}
