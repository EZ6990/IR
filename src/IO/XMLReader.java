package IO;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
        String date = "";
        String Text = "";
        String Country = "";

        Element XMLDocument = this.doc.first();
        Element XMLHeader = null;
        try {
            XMLHeader = (Element) XMLDocument.getElementsByTag("HEADER").get(0);
        }catch(Exception e){
            //e.printStackTrace();
        }

        path = this.xml_file.getPath();


        if (XMLDocument != null) {
            Elements FP104 = XMLDocument.getElementsByTag("<F P=104>");
            ID = XMLDocument.getElementsByTag("DOCNO").text();
            Text = this.doc.first().getElementsByTag("TEXT").text();
            if (FP104 != null)
                Country = FP104.text().trim().split(" ")[0];
        }
        if (XMLHeader != null) {
            date = XMLHeader.getElementsByTag("DATE1").text();
        }
        else{
            date = XMLDocument.getElementsByTag("DATE").text();
//          Date oDate = new Date(Integer.parseInt(DATE.substring(0, 2)), Integer.parseInt(DATE.substring(2, 4)), Integer.parseInt(DATE.substring(4, 6)));
//          date = new SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH).format(oDate);

        }

        this.doc = this.doc.next();


        return new TextOperations.Document(path,ID,Country,date,Text);
    }
    public boolean hasNext(){
        return this.doc.size() > 0;
    }
}
