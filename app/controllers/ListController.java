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
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
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
            Logger.error("error with http request");
            return null;
        }
    }
}