package IO.Segments;

import IO.DatamuseObject;
import IO.HTTPWebRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SemanticDatamuse {

    private String webServiceURL;
    public SemanticDatamuse(String WebServiceURL){
        this.webServiceURL = WebServiceURL;

    }

    public List<DatamuseObject> getSimilarMeaningWords(List<String> str) {
        List<DatamuseObject> result = null;
        try {
            String query = str.get(0);
            for (int i = 1; i < str.size(); i++) {
                query += "+" + str.get(i);
            }
            HTTPWebRequest request;
            request = new HTTPWebRequest();

            JSONObject jsonDetails = request.post(this.webServiceURL + "?ml=" + query + "&max=10");
            JSONArray queryResult = jsonDetails.getJSONArray("result");
            if (!queryResult.isEmpty()) {
                result = new ArrayList<DatamuseObject>();
                for (Object obj : queryResult) {
                    JSONObject data = (JSONObject) obj;
                    result.add(new DatamuseObject(data));
                }
            }
        }
        catch (Exception e){
            result = null;
        }
        return result;
    }

    public List<DatamuseObject> getAdjectivesWords(String str) throws IOException {
        List<DatamuseObject> result = null;

        try{
            HTTPWebRequest request;
            request = new HTTPWebRequest();

            JSONObject jsonDetails = request.post(this.webServiceURL + "?rel_jjb=" + str + "&max=10");
            JSONArray queryResult = jsonDetails.getJSONArray("result");
            if (!queryResult.isEmpty()) {
                result = new ArrayList<DatamuseObject>();
                for (Object obj : queryResult) {
                    JSONObject data = (JSONObject) obj;
                    result.add(new DatamuseObject(data));
                }
            }
        }catch (Exception e){
            result = null;
        }
        return result;
    }

    public List<DatamuseObject> getAdjectivesWords(String str,List<String> lstTopic) {
        List<DatamuseObject> result = null;
        try {
            String topic = lstTopic.get(0);
            for (int i = 1; i < lstTopic.size(); i++) {
                topic += "+" + lstTopic.get(i);
            }
            HTTPWebRequest request;
            request = new HTTPWebRequest();

            JSONObject jsonDetails = request.post(this.webServiceURL + "?rel_jjb=" + URLEncoder.encode(str,"UTF-8") + "&topics=" + URLEncoder.encode(topic,"UTF-8") + "&max=3"  );
            JSONArray queryResult = jsonDetails.getJSONArray("result");
            if (!queryResult.isEmpty()) {
                result = new ArrayList<DatamuseObject>();
                for (Object obj : queryResult) {
                    JSONObject data = (JSONObject) obj;
                    result.add(new DatamuseObject(data));
                }
            }
        }catch (Exception e){
            result = null;
        }
        return result;
    }

    public List<DatamuseObject> getStronglyAssociatedWords(String str) {
        List<DatamuseObject> result = null;

        try {
            HTTPWebRequest request;
            request = new HTTPWebRequest();

            JSONObject jsonDetails = request.post(this.webServiceURL + "?rel_trg=" + str + "&max=10");
            JSONArray queryResult = jsonDetails.getJSONArray("result");
            if (!queryResult.isEmpty()) {
                result = new ArrayList<DatamuseObject>();
                for (Object obj : queryResult) {
                    JSONObject data = (JSONObject) obj;
                    result.add(new DatamuseObject(data));
                }
            }
        }catch (Exception e){
            result = null;
        }
        return result;
    }



}
