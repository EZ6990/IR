package Main;

import IO.HTTPWebRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class CountryInfo {

    private HTTPWebRequest request;
    private String SiteUrl = "https://restcountries.eu/rest/v2/capital";
    private String Params = "fields=name;capital;population;currencies";
    private JSONObject jsonDetails;
    private String CountryName;
    private String CapitalName;
    private String Population;
    private String Currency;

    public CountryInfo(String CapitalName) throws IOException {
        request = new HTTPWebRequest();
        jsonDetails = request.post(this.SiteUrl + "/" + CapitalName + "?" + this.Params);

        JSONArray result = jsonDetails.getJSONArray("result");

        JSONObject data = result.getJSONObject(0);
        this.CountryName = data.get("name").toString();
        this.CapitalName = data.get("capital").toString();
        this.Population = data.get("population").toString();
        this.CountryName = data.get("name").toString();
        this.Currency = data.getJSONArray("currencies").getJSONObject(0).get("name").toString();

    }

    public String getCapitalName() {
        return this.CapitalName;
    }
    public String getCountryName() {
        return this.CountryName;
    }
    public String getPopulation() {
        return this.Population;
    }
    public String getCurrency() {
        return this.Currency;
    }
}
