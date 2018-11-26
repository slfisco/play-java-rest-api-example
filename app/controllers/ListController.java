package controllers;
import play.mvc.*;

import java.net.URL;
import java.net.HttpURLConnection;
import java.util.*;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import java.io.IOException;
import play.Logger;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import play.libs.ws.*;
import java.util.concurrent.CompletionStage;
import java.io.DataOutputStream;

import static play.libs.Scala.asScala;
import play.data.Form;
import play.data.FormFactory;
import javax.inject.Inject;
import play.libs.Json;
import com.fasterxml.jackson.databind.JsonNode;

public class ListController extends Controller /*implements WSBodyReadables, WSBodyWritables*/ {
    private Form<TaskData> form;
    //private static final java.util.logging.Logger LOGGER = Logger.getLogger("testLogger");
   /* private final WSClient ws;

    @Inject
    public ListController(WSClient ws) {
        this.ws = ws;
    }
*/
    @Inject
    public ListController(FormFactory formFactory) {
        this.form = formFactory.form(TaskData.class);
    }
    public Result listArgs() {
        List<String> argsList = new ArrayList<>();
        argsList.add("hello");
        argsList.add("spencer");
        //may want to associate these with the class itself
        return ok(views.html.listTasks.render(asScala(argsList),form));
    }
    public Result addToList() {
        // fill this in
        return ok(views.html.index.render());
    }
    public Result parseJson() {
        JsonNode testJson = Json.parse("[{\"firstName\":\"Foo\", \"lastName\":\"Bar\", \"age\":13},{\"firstName\":\"Cat\", \"lastName\":\"Dog\", \"age\":5}]");
        for (int i = 0; i < 2; ++i) {
            //return ok(views.html.parseJson.render(Json.stringify(testJson)));//views.html.listTasks.render(testJson));
            renderMultipleTemplates();
        }
        return ok(views.html.index.render());
    }
    public Result renderMultipleTemplates() {
        return ok(views.html.index.render());
    }
    public Result jqueryJson() {
        JsonNode json = Json.parse("{ \"employees\" : [ {\"firstName\":\"John\", \"lastName\":\"Doe\"}, {\"firstName\":\"Anna\", \"lastName\":\"Smith\"}]}");
        String jsonString = Json.stringify(json);
        return ok(views.html.jqueryJson.render(jsonString, form));
    }
    public Result fetchTest() {
        JsonNode json = Json.parse("{\"title\" : \"title 7\", \"body\" : \"test3\"}");
        String jsonString = Json.stringify(json);
        return ok(views.html.fetchTest.render(jsonString));
    }
    public Result httpRequestTest() {
        JsonNode json = Json.parse("{\"title\" : \"title 7\", \"body\" : \"test3\"}");
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
        String jsonString = Json.stringify(json);
        return ok(views.html.jqueryJson.render(jsonString, form));
    }
    public Result loggerTest() {
        Logger.error("logging an error");
        return ok(views.html.index.render());
    }
/*    public Result playHttpRequest() {
        return ok(views.html.index.render());
    }
    */
}