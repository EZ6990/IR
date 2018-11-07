package IO;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class XMLReader {

    private File xml_file;
    private Document doc;

    public XMLReader(String path) throws IOException {
        this.xml_file = new File(path);
        FileInputStream fis = new FileInputStream(this.xml_file);
        this.doc = Jsoup.parse(fis, null, "", Parser.xmlParser());
    }

    public XMLReader(File file) throws IOException {
        this.xml_file = file;
        FileInputStream fis =  new FileInputStream(this.xml_file);
        this.doc = Jsoup.parse(fis, null, "", Parser.xmlParser());
    }
}
