package MapReduce;

import Main.Term;

public class CityTDI extends TermDocumentInfo {

    private String country;
    private String currency;
    private String population;
    private String termLocation;

    public CityTDI(Term term, int documentID, String country, String currency, String population) {
        super(term, documentID);
        this.country = country;
        this.currency = currency;
        this.termLocation = "";
        this.population = population;
    }

    public void addLocation(int location) {
        if (termLocation.length() > 0)
            termLocation += ",";
        termLocation += location;
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

    public String getTermLocation() {
        return termLocation;
    }
}
