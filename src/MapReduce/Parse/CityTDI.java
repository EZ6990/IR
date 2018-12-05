package MapReduce.Parse;

import java.util.ArrayList;
import java.util.List;

public class CityTDI extends AbstractTermDocumentInfo {

    private String country;
    private String currency;
    private String population;
    private List<Integer> termLocation;

    public CityTDI(Term term, String documentID, String country, String currency, String population) {
        super(term, documentID);
        this.country = country;
        this.currency = currency;
        this.termLocation = new ArrayList<>();
        this.population = population;
    }

    public CityTDI(Term term, String documentID, CountryInfo info) {
        super(term, documentID);
        this.country = info.getCountryName();
        this.currency = info.getCurrency();
        this.population = info.getPopulation();
        this.termLocation = new ArrayList<>();
    }

    public void addLocation(int location) {
        this.termLocation.add(location);
    }

    public String getCountry() {
        return country;
    }

    public String getCurrency() {
        return currency;
    }

    public String getPopulation() {
        return population;
    }

    public List<Integer> getTermLocation() {
        return termLocation;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(getTerm().getData());
        builder.append(";").append(this.country).append("!").append(this.currency).append("!").append(this.population).append("?").append(this.getDocumentID());
        for (Integer location : this.termLocation) {
            builder.append("|").append(location);
        }

        return builder.toString();
    }

}
