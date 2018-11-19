package controllers;
import play.mvc.*;
import java.util.*;
import static play.libs.Scala.asScala;
import play.data.Form;
import play.data.FormFactory;
import javax.inject.Inject;

public class ListController extends Controller {
    private Form<TaskData> form;
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
}