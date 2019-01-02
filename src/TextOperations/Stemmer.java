package TextOperations;

import com.sun.corba.se.impl.orbutil.concurrent.Mutex;
import org.tartarus.snowball.ext.englishStemmer;

public class Stemmer {

    private englishStemmer stemmer;
    private Mutex m_mutex;
    public Stemmer(){
        stemmer = new englishStemmer();
        this.m_mutex = new Mutex();
    }

    public String stemString(String word){
        try {
            this.m_mutex.acquire();
            this.stemmer.setCurrent(word);
            if (this.stemmer.stem()) {
                String s=this.stemmer.getCurrent();
                this.m_mutex.release();
                return s;
            }
        } catch (InterruptedException e) {
            //e.printStackTrace();
        }
        this.m_mutex.release();
        return word;
    }


}
