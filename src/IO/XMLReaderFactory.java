package IO;

import java.io.File;
import java.io.IOException;

public class XMLReaderFactory {

    public XMLReader getXMLReader(XMLReader.Type type, String path) throws IOException {

        if (type.equals(XMLReader.Type.DOCUMENT))
            return new XMLDocumentReader(path);
        else if (type.equals(XMLReader.Type.QUERY)){
            return new XMLQueryReader(path);
        }
        return null;
    }
    public XMLReader getXMLReader(XMLReader.Type type, File f) throws IOException {
        if (type.equals(XMLReader.Type.DOCUMENT))
            return new XMLDocumentReader(f);
        else if (type.equals(XMLReader.Type.QUERY)){
            return new XMLQueryReader(f);
        }
        return null;
    }

}
