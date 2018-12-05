package MapReduce;

import Main.CountryInfo;
import Main.Term;

public class CityTDI extends AbstractTermDocumentInfo{

    private String country;
    private String currency;
    private String population;
    private String termLocation;

    public CityTDI(Term term, String documentID, String country, String currency, String population) {
        super(term, documentID);
        this.country = country;
        this.currency = currency;
        this.termLocation = "";
        this.population = population;
    }

    public CityTDI(Term term, String documentID, CountryInfo info) {
        super(term, documentID);
        this.country = info.getCountryName();
        this.currency = info.getCurrency();
        this.population = info.getPopulation();
        this.termLocation = "";
    }

    public void addLocation(int location) {
        if (this.termLocation.length() > 0)
            this.termLocation += ",";
        this.termLocation += location;
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


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(getTerm().getData());

        return builder.append(";").append(this.country).append(" ") + this.currency + "," + this.population + "," + this.termLocation;
    }

}
