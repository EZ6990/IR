package IO;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class XMLReader {

    private File xml_file;
    private Elements doc;

    public XMLReader(String path) throws IOException {
        this.xml_file = new File(path);
        FileInputStream fis = new FileInputStream(this.xml_file);
        this.doc = Jsoup.parse(fis, null, "", Parser.xmlParser()).getElementsByTag("DOC");
    }

    public XMLReader(File file) throws IOException {
        this.xml_file = file;
        FileInputStream fis = new FileInputStream(this.xml_file);
        this.doc = Jsoup.parse(fis, null, "", Parser.xmlParser()).getElementsByTag("DOC");

    }

    public TextOperations.Document getNextDocument() {


        String path = this.xml_file.getPath();
        String ID = this.doc.first().getElementsByTag("DOCNO").text();
        String Header = this.doc.first().getElementsByTag("DOCNO").text();
        String Date = this.doc.first().getElementsByTag("DOCNO").text();
        String Text = this.doc.first().getElementsByTag("TEXT").text();
        this.doc = this.doc.next();


        return new TextOperations.Document(path,ID,Header,Date,Text);

    }
}