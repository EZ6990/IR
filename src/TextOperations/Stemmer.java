package TextOperations;

import Main.Term;
import org.tartarus.snowball.ext.englishStemmer;

public class Stemmer {

    private englishStemmer stemmer;
    public Stemmer(){
        stemmer = new englishStemmer();
    }

    public String stemString(String word){
        this.stemmer.setCurrent(word);
        if (this.stemmer.stem())
            return this.stemmer.getCurrent();

        return word;
    }


}
