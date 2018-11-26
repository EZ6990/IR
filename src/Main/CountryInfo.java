package Main;

import IO.HTTPWebRequest;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class CountryInfo {

    private HTTPWebRequest request;
    private String SiteUrl = "https://restcountries.eu/rest/v2/capital";
    private String Params = "fields=name;capital;population;currencies";
    private Document doc;

    public CountryInfo(String CapitalName) throws IOException {
        request = new HTTPWebRequest();
        doc = request.post(this.SiteUrl + "/" + CapitalName + "?" + this.Params);
    }

    public String getCapitalName() {
        return doc.getElementsByTag("").text();
    }
    public String getCountryName() {
        return doc.getElementsByTag("").text();
    }
    public String getPopulation() {
        return doc.getElementsByTag("").text();
    }
    public String getCurrency() {
        return doc.getElementsByTag("").text();
    }
}
