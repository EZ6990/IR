package IO;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

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

        String path = "";
        String ID = "";
        String Date = "";
        String Text = "";
        String Country = "";

        Element XMLDocument = this.doc.first();
        Element XMLHeader = (Element)XMLDocument.getElementsByTag("Header").get(0);


        path = this.xml_file.getPath();


        if (XMLDocument != null) {
            Elements FP104 = XMLDocument.getElementsByTag("<F P=104>");
            ID = XMLDocument.getElementsByTag("DOCNO").text();
            Text = this.doc.first().getElementsByTag("TEXT").text();
            if (FP104 != null)
                Country = FP104.text();
        }
        if (XMLHeader != null) {
            Date = XMLHeader.getElementsByTag("DATE1").text();
        }

        this.doc = this.doc.next();


        return new TextOperations.Document(path,ID,Country,Date,Text);

    }
}
