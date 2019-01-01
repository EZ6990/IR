package IO.Segments;

import IO.DatamuseObject;
import IO.HTTPWebRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
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

    public List<DatamuseObject> getAdjectivesWords(List<String> str) throws IOException {
        List<DatamuseObject> result = null;

        try{
            String query = str.get(0);
            for (int i = 1; i < str.size(); i++) {
                query += "+" + str.get(i);
            }
            HTTPWebRequest request;
            request = new HTTPWebRequest();

            JSONObject jsonDetails = request.post(this.webServiceURL + "?rel_jjb=" + query + "&max=10");
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

    public List<DatamuseObject> getAdjectivesWords(List<String> str,List<String> lstTopic) {
        List<DatamuseObject> result = null;
        try {
            String query = str.get(0);
            String topic = lstTopic.get(0);
            for (int i = 1; i < str.size(); i++) {
                query += "+" + str.get(i);
            }
            for (int i = 1; i < lstTopic.size(); i++) {
                topic += "+" + lstTopic.get(i);
            }
            HTTPWebRequest request;
            request = new HTTPWebRequest();

            JSONObject jsonDetails = request.post(this.webServiceURL + "?rel_jjb=" + query + "&topic=" + topic + "&max=10");
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

    public List<DatamuseObject> getStronglyAssociatedWords(List<String> str) {
        List<DatamuseObject> result = null;

        try {
            String query = str.get(0);
            for (int i = 1; i < str.size(); i++) {
                query += "+" + str.get(i);
            }
            HTTPWebRequest request;
            request = new HTTPWebRequest();

            JSONObject jsonDetails = request.post(this.webServiceURL + "?rel_trg=" + query + "&max=10");
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
