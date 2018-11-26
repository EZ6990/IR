package IO;


import org.jsoup.nodes.Document;

import org.jsoup.Jsoup;

import java.io.IOException;

public class HTTPWebRequest {

    public Document post(String URL) throws IOException {
         return Jsoup.connect(URL).ignoreContentType(true).execute().parse();
    }
}
