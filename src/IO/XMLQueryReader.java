package IO;

import TextOperations.Document;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class XMLQueryReader extends XMLReader {
    public XMLQueryReader(String path) throws IOException {
        super(path);
    }

    public XMLQueryReader(File file) throws IOException {
        super(file);
    }

    @Override
    protected void initializeReader() throws IOException {
        FileInputStream fis = new FileInputStream(this.xml_file);
        this.doc = Jsoup.parse(fis, null, "", Parser.xmlParser()).getElementsByTag("title");
    }

    @Override
    public Document getNextDocument() {
        return null;
    }
}
