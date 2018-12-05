package controllers;
import play.mvc.*;

import java.net.URL;
import java.net.HttpURLConnection;
import java.util.*;
import java.io.IOException;
import play.Logger;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;

import static play.libs.Scala.asScala;
import play.data.Form;
import play.data.FormFactory;
import javax.inject.Inject;
import play.libs.Json;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ListController extends Controller /*implements WSBodyReadables, WSBodyWritables*/ {
    private Form<TaskData> form;
    @Inject
    public ListController(FormFactory formFactory) {
        this.form = formFactory.form(TaskData.class);
    }

    public Result addToList() {
        // fill this in
        return ok(views.html.index.render());
    }

    public Result jqueryJson() {
        JsonNode json = Json.parse(makeGETrequest());
        String jsonString = Json.stringify(json);
        return ok(views.html.jqueryJson.render(jsonString, form));
    }

    public Result httpRequestTest() {
        final Form<TaskData> boundForm = form.bindFromRequest();
        TaskData data = boundForm.get();
        Logger.error(data.taskName);
        ObjectNode json = Json.newObject();
        json.put("taskName", data.taskName);
        json.put("status", false);
        //could replace HttpURLConnection with play.mvc.http.requestBuilder
        //POST request
        try {
            byte[] bytes = Json.stringify(json).getBytes();
            URL url = new URL("http://localhost:9000/v1/posts");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.addRequestProperty("Accept", "application/json");
            con.addRequestProperty("Connection", "close");
            con.addRequestProperty("Content-Length", Integer.toString(bytes.length));
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            DataOutputStream outputStream = new DataOutputStream(con.getOutputStream());
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
            int status = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())); //don't need read out
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                Logger.error(inputLine);
            }
            in.close();
            con.disconnect();
            Logger.error("successful http request");
        }
        catch (Exception e) {
            Logger.error("error with http request");
        }
        //now make get request to get json for jqueryJson
        try {
            String jsonString = makeGETrequest();
            return ok(views.html.jqueryJson.render(jsonString, form));
        }
        catch (Exception e) {
            Logger.error("error with http request");
            return ok("error: http request failed");
        }
    }
    //make this more modular. could have request methods share variables/connections
    public String makeGETrequest() {
        try {
            //a ton of redundancy
            URL url = new URL("http://localhost:9000/v1/posts");
            HttpURLConnection con2 = (HttpURLConnection) url.openConnection();
            con2.setRequestMethod("GET");
            BufferedReader in2 = new BufferedReader(new InputStreamReader(con2.getInputStream()));
            String jsonString = in2.readLine();
            Logger.error(jsonString);
            in2.close();
            con2.disconnect();
            return jsonString;
        }
        catch (Exception e) {
            Logger.error("error with http request"); //make more specific
            return null;
        }
    }

    public void makePUTrequest(String jsonString,String id) {
        try {
            URL url = new URL("http://localhost:9000/v1/posts/" + id);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            byte[] bytes = jsonString.getBytes();
            con.addRequestProperty("Accept", "application/json");
            con.addRequestProperty("Connection", "close");
            con.addRequestProperty("Content-Length", Integer.toString(bytes.length));
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            DataOutputStream outputStream = new DataOutputStream(con.getOutputStream());
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
            con.disconnect();
            int responseStatus = con.getResponseCode();
            Logger.error("put request complete and returned status " + responseStatus);
        }
        catch (Exception e) {
            Logger.error("error with put request");
        }
    }
    public Result update(String id) {
        /*
        String newjsonString = makeGETrequest(); //get entire jsonString so we have other fields
        JsonNode rootnode = Json.parse(newjsonString);//mapper.readTree(newjsonString); //not sure this is necessary
        Iterator<JsonNode> i = rootnode.elements();
        String status = "";
        String taskName = "";

            while (i.hasNext()) {
                JsonNode element = i.next();
                if (element.path("id").asText().equals(id)) {
                    Logger.error("the ID matches");
                    status = element.path("status").asText();
                    Logger.error("type of status is: " + status.getClass().getName());
                    Logger.error("status is :" + status);
                    taskName = element.path("taskName").asText();
                    Logger.error("taskName is :" + taskName);
                }
        }
        */
        jsonFields jsonfields = getJsonFields(id);
        Logger.error("JSONFIELDS.STATUS :"+ jsonfields.status);
        //example json:
        // {"id":"2","link":"http://localhost:9000/v1/posts/2","taskName":"test2","status":"false","updateLink":"http://localhost:9000/v1/posts/2/update"},
        // {"id":"3","link":"http://localhost:9000/v1/posts/3","taskName":"test5","status":"false","updateLink":"http://localhost:9000/v1/posts/3/update"}]
        ObjectNode requestBody = Json.newObject();
        requestBody.put("taskName", jsonfields.taskName); //update with value from json
        if (jsonfields.status.equals("false")) {
            requestBody.put("status", "true");
        }
        else if (jsonfields.status.equals("true")) {
            requestBody.put("status", "false");
        }

        makePUTrequest(Json.stringify(requestBody),id);
        String jsonString = makeGETrequest();
        return ok(views.html.jqueryJson.render(jsonString, form));
    }
    private static class jsonFields {
        String taskName;
        String status;
    }
    private jsonFields getJsonFields(String id) {
        String newjsonString = makeGETrequest(); //get entire jsonString so we have other fields
        JsonNode rootnode = Json.parse(newjsonString);//mapper.readTree(newjsonString); //not sure this is necessary
        //JsonNode taskNameNode = rootnode.path(0).path("id");
        //Logger.error("testing path " + taskNameNode.textValue());
        Iterator<JsonNode> i = rootnode.elements();
        jsonFields jsonfields = new jsonFields();

        while (i.hasNext()) {
            JsonNode element = i.next();
                /*Logger.error("this element is :" + element);
                Logger.error("the id is :" + element.path("id"));
                Logger.error("looking for id :" + id);
                */
            if (element.path("id").asText().equals(id)) {
                Logger.error("the ID matches");
                jsonfields.status = element.path("status").asText();
                Logger.error("type of status is: " + jsonfields.status.getClass().getName());
                Logger.error("status is :" + jsonfields.status);
                jsonfields.taskName = element.path("taskName").asText();
                Logger.error("taskName is :" + jsonfields.taskName);
                return jsonfields;
            }
        }
        Logger.error("id not found");
        return null;
    }
}
