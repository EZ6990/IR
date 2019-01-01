package IO;

import MapReduce.Parse.Parsers.NumberParser;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DatamuseObject {

    private String word;
    private int score;
    private List<Tag> tags;

    public enum Tag {jja,jjb,syn,trg,ant,spc,gen,com,par,bga,bgb,rhy,nry,hom,cns};

    public DatamuseObject(JSONObject data) throws IOException{
        this.word = data.get("word").toString();
        this.score = data.getInt("score");
        try {
            JSONArray tags = data.getJSONArray("tags");
            this.tags = new ArrayList<Tag>();
            for (Object obj : tags) {
                if (obj.toString().equals("jja"))
                    this.tags.add(Tag.jja);
                else if (obj.toString().equals("jjb"))
                    this.tags.add(Tag.jjb);
                else if (obj.toString().equals("syn"))
                    this.tags.add(Tag.syn);
                else if (obj.toString().equals("trg"))
                    this.tags.add(Tag.trg);
                else if (obj.toString().equals("ant"))
                    this.tags.add(Tag.ant);
                else if (obj.toString().equals("spc"))
                    this.tags.add(Tag.spc);
                else if (obj.toString().equals("gen"))
                    this.tags.add(Tag.gen);
                else if (obj.toString().equals("com"))
                    this.tags.add(Tag.com);
                else if (obj.toString().equals("par"))
                    this.tags.add(Tag.par);
                else if (obj.toString().equals("bga"))
                    this.tags.add(Tag.bga);
                else if (obj.toString().equals("bgb"))
                    this.tags.add(Tag.bgb);
                else if (obj.toString().equals("rhy"))
                    this.tags.add(Tag.rhy);
                else if (obj.toString().equals("nry"))
                    this.tags.add(Tag.nry);
                else if (obj.toString().equals("hom"))
                    this.tags.add(Tag.hom);
                else if (obj.toString().equals("cns"))
                    this.tags.add(Tag.cns);
            }
        }catch (org.json.JSONException e){this.tags = null;}
    }


}
