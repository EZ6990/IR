package IO;

import TextOperations.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

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
        this.doc = Jsoup.parse(fis, null, "", Parser.xmlParser()).getElementsByTag("top");
    }

    @Override
    public Document getNextDocument() {

        String path = "";
        String ID = "";
        String date = "";
        String Text = "";
        String Country = "";
        String Language = "";


        Element XMLDocument = this.doc.first();

        path = this.xml_file.getPath();

        if (XMLDocument != null) {
            String data = XMLDocument.getElementsByTag("num").text().split("\\r\\n\\r\\n")[0].substring(8);
            ID = data.substring(0,data.indexOf(' ')).trim();
            Text = this.doc.first().getElementsByTag("title").text().split("\\r\\n\\r\\n")[0].trim();
        }

        this.doc = this.doc.next();


        return new TextOperations.Document(path,ID,Country,date,Text,Language);

    }
}
