package IO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class XMLDocumentReader extends XMLReader {
    public XMLDocumentReader(String path) throws IOException {
        super(path);
    }

    public XMLDocumentReader(File file) throws IOException {
        super(file);
    }

    @Override
    protected void initializeReader() throws IOException {
        FileInputStream fis = new FileInputStream(this.xml_file);
        this.doc = Jsoup.parse(fis, null, "", Parser.xmlParser()).getElementsByTag("DOC");
    }

    public AbstractDocument getNextDocument() {

        String path = "";
        String ID = "";
        String date = "";
        String Text = "";
        String Country = "";
        String Language = "";


        Element XMLDocument = this.doc.first();
        Element XMLHeader = null;
        try {
            XMLHeader = (Element) XMLDocument.getElementsByTag("HEADER").get(0);
        }catch(Exception e){
            //e.printStackTrace();
        }

        path = this.xml_file.getPath();


        if (XMLDocument != null) {
            Elements F = XMLDocument.getElementsByTag("F");
            ID = XMLDocument.getElementsByTag("DOCNO").text();
            Text = this.doc.first().getElementsByTag("TEXT").text();
            if (F != null && F.size() > 0) {
                for (int i = 0; i < F.size(); i++) {
                    Element FP = F.get(i);
                    if (FP.attr("P").equals("104"))
                        Country = FP.text().trim().split(" ")[0].toUpperCase();
                    else if (FP.attr("P").equals("105"))
                        Language = FP.text().trim();
                }
            }
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


        return new TextOperations.Document(path,ID,Country,date,Text,Language);
    }


}
