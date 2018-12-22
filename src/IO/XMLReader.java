package IO;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class XMLReader {

    protected File xml_file;
    protected Elements doc;
    public enum Type{DOCUMENT,QUERY};

    public XMLReader(String path) throws IOException {
        this.xml_file = new File(path);
        initializeReader();
    }

    public XMLReader(File file) throws IOException {
        this.xml_file = file;
        initializeReader();
    }

    protected abstract void initializeReader() throws IOException;

    public abstract TextOperations.Document getNextDocument();

    public boolean hasNext(){
        return this.doc.size() > 0;
    }
}
